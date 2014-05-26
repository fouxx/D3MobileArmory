package fouxx.D3MobileArmory;

import java.util.ArrayList;

import fouxx.D3MobileArmory.Player;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "D3MobileArmory";
    
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PLAYER_TABLE = "CREATE TABLE players ( " +
                "btag TEXT, " + 
                "paragonSC TEXT, "+
                "paragonHC TEXT, PRIMARY KEY(btag) )";
        db.execSQL(CREATE_PLAYER_TABLE);
        
        String CREATE_HERO_TABLE = "CREATE TABLE heroes ( " + 
        		"ID TEXT, name TEXT, gender TEXT, level TEXT, "+
        		"heroClass TEXT, mode TEXT, downloaded TEXT, btag TEXT, PRIMARY KEY(ID) )";
        db.execSQL(CREATE_HERO_TABLE);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS players");
        db.execSQL("DROP TABLE IF EXISTS heroes");
        this.onCreate(db);
    }
    //Table PLAYERS
    private static final String TABLE_PLAYERS = "players";
    
    //Columns PLAYERS
    private static final String KEY_BTAG = "btag";    
    private static final String KEY_PARAGONSC = "paragonSC";
    private static final String KEY_PARAGONHC = "paragonHC";
    
    private static final String[] PLAYER_COLUMNS = {KEY_BTAG, KEY_PARAGONSC, KEY_PARAGONHC};
    
    public void addPlayer(Player player){
        Log.d("addPlayer", player.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        
        Cursor checkIfExists = db.query(TABLE_PLAYERS, PLAYER_COLUMNS, "btag = ?", new String[] { player.getBtag() } , null, null, null, null);
        if (checkIfExists != null && checkIfExists.getCount() > 0)
            	return;
        
        ContentValues values = new ContentValues();
        values.put(KEY_BTAG, player.getBtag()); 
        values.put(KEY_PARAGONSC, player.getParagonSC()); 
        values.put(KEY_PARAGONHC, player.getParagonHC());
 
        db.insert(TABLE_PLAYERS, null, values);
    }
    
    public Player getPlayer(String btag){
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = 
                db.query(TABLE_PLAYERS, // a. table
                PLAYER_COLUMNS, // b. column names
                " btag = ?", // c. selections 
                new String[] { btag }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
 
        if (cursor != null)
            cursor.moveToFirst();
        else
        	return null;
 
        Player player = new Player();
        player.setBtag(cursor.getString(0));
        player.setParagonSC(cursor.getString(1));
        player.setParagonHC(cursor.getString(2));
 
        Log.d("getPlayer("+btag+")", player.toString());

        return player;
    }
    
    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();
  
        String query = "SELECT  * FROM " + TABLE_PLAYERS;
  
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
  
        Player player = null;
        if (cursor.moveToFirst()) {
            do {
                player = new Player();
                player.setBtag(cursor.getString(0));
                player.setParagonSC(cursor.getString(1));
                player.setParagonHC(cursor.getString(2));
  
                players.add(player);
            } while (cursor.moveToNext());
        }
  
        Log.d("getAllPlayers()", players.toString());
        return players;
    }
    
    public void deletePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(TABLE_PLAYERS, KEY_BTAG+" = ?", 
        		new String[] { player.getBtag() });
        db.delete(TABLE_HEROES, KEY_BTAG+" = ?", 
        		new String[] { player.getBtag() });
 
        db.close();
        Log.d("deletePlayer", player.toString());
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    
    private static final String TABLE_HEROES = "heroes";
    
    //Columns PLAYERS
    private static final String KEY_ID = "ID";    
    private static final String KEY_NAME = "name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_LEVEL = "level";    
    private static final String KEY_HEROCLASS = "heroClass";
    private static final String KEY_MODE = "mode";
    private static final String KEY_DOWNLOADED = "downloaded";
    
    private static final String[] HERO_COLUMNS = {KEY_ID, KEY_NAME, KEY_GENDER, KEY_LEVEL, KEY_HEROCLASS, KEY_MODE, KEY_DOWNLOADED, KEY_BTAG};
    
    public void addHero(Hero hero){
        Log.d("addHero", hero.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        
        Cursor checkIfExists = db.query(TABLE_HEROES, HERO_COLUMNS, "ID = ?", new String[] { hero.ID } , null, null, null, null);
        if (checkIfExists != null && checkIfExists.getCount() > 0)
            	return;
        
        ContentValues values = new ContentValues();
        values.put(KEY_ID, hero.ID); 
        values.put(KEY_NAME, hero.name); 
        values.put(KEY_GENDER, hero.gender);
        values.put(KEY_LEVEL, hero.level);
        values.put(KEY_HEROCLASS, hero.heroClass);
        values.put(KEY_MODE, hero.mode);
        values.put(KEY_DOWNLOADED, hero.downloaded);
        values.put(KEY_BTAG, hero.btag);
 
        db.insert(TABLE_HEROES, null, values);
    }
    
    public ArrayList<Hero> getAllPlayersHeroes(Player player) {
        ArrayList<Hero> heroes = new ArrayList<Hero>();
  
        String query = "SELECT  * FROM " + TABLE_HEROES + " WHERE " + KEY_BTAG + " = '" + player.getBtag() + "'";
  
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
  
        Hero hero = null;
        if (cursor.moveToFirst()) {
            do {
                hero = new Hero();
                hero.ID = cursor.getString(0);
                hero.name = cursor.getString(1);
                hero.gender = cursor.getString(2);
                hero.level = cursor.getString(3);
                hero.heroClass = cursor.getString(4);
                hero.mode = cursor.getString(5);
                hero.downloaded = cursor.getString(6);
                hero.btag = cursor.getString(7);
  
                heroes.add(hero);
            } while (cursor.moveToNext());
        }
  
        Log.d("getAllPlayersHeroes()", heroes.toString());
        return heroes;
    }
}
