package fouxx.D3MobileArmory;

import java.util.ArrayList;
import java.util.List;

import fouxx.D3MobileArmory.Player;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class D3MobileArmorySQLiteHelper extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "D3MobileArmory";
    
    public D3MobileArmorySQLiteHelper(Context context) {
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
        		"heroClass TEXT, mode TEXT, downloaded TEXT, btag TEXT, paragon TEXT, PRIMARY KEY(ID) )";
        db.execSQL(CREATE_HERO_TABLE);
        
        String CREATE_ITEM_TABLE = "CREATE TABLE items ( " +
        		"slot TEXT, heroID TEXT, " +
        		"name TEXT, icon TEXT, color TEXT, tooltip TEXT, level TEXT, " +
        		"accountBound TEXT, flavorText TEXT, type TEXT, armor TEXT, " +
        		"DPS TEXT, attackSpeed TEXT, damage TEXT, blockChance TEXT, " +
        		"primaryAtr TEXT, secondaryAtr TEXT, passive TEXT, PRIMARY KEY(slot, heroID) )";
        db.execSQL(CREATE_ITEM_TABLE);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS players");
        db.execSQL("DROP TABLE IF EXISTS heroes");
        db.execSQL("DROP TABLE IF EXISTS items");
        this.onCreate(db);
    }
    //Table PLAYERS
    private static final String TABLE_PLAYERS = "players";
    
    //Columns PLAYERS
    private static final String KEY_BTAG = "btag";    
    private static final String KEY_PARAGONSC = "paragonSC";
    private static final String KEY_PARAGONHC = "paragonHC";
    
    private static final String[] PLAYER_COLUMNS = {KEY_BTAG, KEY_PARAGONSC, KEY_PARAGONHC};
    
    public String addPlayer(Player player){
        Log.d("addPlayer", player.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        
        Cursor checkIfExists = db.query(TABLE_PLAYERS, PLAYER_COLUMNS, "btag = ?", new String[] { player.btag } , null, null, null, null);
        if (checkIfExists != null && checkIfExists.getCount() > 0)
            	return "This profile already exists.";
        
        ContentValues values = new ContentValues();
        values.put(KEY_BTAG, player.btag); 
        values.put(KEY_PARAGONSC, player.paragonSC); 
        values.put(KEY_PARAGONHC, player.paragonHC);
 
        db.insert(TABLE_PLAYERS, null, values);
        return "";
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
 
        Player player = new Player(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        Log.d("getPlayer("+btag+")", player.toString());

        return player;
    }
    
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<Player>();
  
        String query = "SELECT  * FROM " + TABLE_PLAYERS;
  
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
  
        if (cursor.moveToFirst()) {
            do {
                players.add(new Player(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
  
        Log.d("getAllPlayers()", players.toString());
        return players;
    }
    
    public void deletePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ArrayList <String> ids = new ArrayList<String>();
        String query = "SELECT * FROM " + TABLE_HEROES + " WHERE " + KEY_BTAG + " = '" + player.btag + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
            	ids.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        for(int j = 0; j < ids.size(); j++){
        	db.delete(TABLE_ITEMS, KEY_HERO_ID+" = ?", new String[] { ids.get(j) });
        }
 
        db.delete(TABLE_PLAYERS, KEY_BTAG+" = ?", 
        		new String[] { player.btag });
        db.delete(TABLE_HEROES, KEY_BTAG+" = ?", 
        		new String[] { player.btag });
 
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
    private static final String KEY_PARAGON = "paragon";
    
    
    private static final String[] HERO_COLUMNS = {KEY_ID, KEY_NAME, KEY_GENDER, KEY_LEVEL, KEY_HEROCLASS, KEY_MODE, KEY_DOWNLOADED, KEY_BTAG, KEY_PARAGON};
    
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
        values.put(KEY_PARAGON, hero.paragon);
 
        db.insert(TABLE_HEROES, null, values);
    }
    
    public ArrayList<Hero> getAllPlayersHeroes(Player player) {
        ArrayList<Hero> heroes = new ArrayList<Hero>();
  
        String query = "SELECT  * FROM " + TABLE_HEROES + " WHERE " + KEY_BTAG + " = '" + player.btag + "'";
  
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
                hero.paragon = cursor.getString(8);
  
                heroes.add(hero);
            } while (cursor.moveToNext());
        }
  
        Log.d("getAllPlayersHeroes()", heroes.toString());
        return heroes;
    }
    
    public boolean isGearDownloaded(String heroID){
        SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.query(TABLE_HEROES, HERO_COLUMNS, "ID = ?", new String[] { heroID } , null, null, null, null);
    	cursor.moveToFirst();
    	if(cursor.getString(6).equals("false")){
    		return false;
    	}    	
    	return true;
    }
    
    public void setGearDownloaded(String heroID){
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.execSQL("UPDATE "+ TABLE_HEROES +
                " SET " + KEY_DOWNLOADED + " = " + "'true'" +
                " WHERE " + KEY_ID + " = ?",
                new String[] { heroID });
    }
    
    public void addHeroStats(){
    	//TODO Adding stats
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    
    private static final String TABLE_ITEMS = "items";
    
    //Columns PLAYERS
    private static final String KEY_SLOT = "slot";    
    private static final String KEY_HERO_ID = "heroID";
    
    private static final String KEY_ITEM_NAME = "name";
    private static final String KEY_ICON = "icon";    
    private static final String KEY_COLOR = "color";
    private static final String KEY_TOOLTIP = "tooltip";
    private static final String KEY_ITEM_LEVEL = "level";
    private static final String KEY_ACCOUNT_BOUND = "accountBound";
    private static final String KEY_FLAVOR_TEXT = "flavorText";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ARMOR = "armor";
    private static final String KEY_DPS = "DPS";
    private static final String KEY_ATTACK_SPEED = "attackSpeed";
    private static final String KEY_DAMAGE = "damage";
    private static final String KEY_BLOCK_CHANCE = "blockChance";
    private static final String KEY_PRIMARY_ATR = "primaryAtr";
    private static final String KEY_SECONDARY_ATR = "secondaryAtr";
    private static final String KEY_PASSIVE = "passive";
    
    
    private static final String[] ITEM_COLUMNS = {KEY_SLOT, KEY_HERO_ID, 
    	KEY_ITEM_NAME, KEY_ICON, KEY_COLOR, KEY_TOOLTIP, KEY_ITEM_LEVEL, 
    	KEY_ACCOUNT_BOUND, KEY_FLAVOR_TEXT, KEY_TYPE, KEY_ARMOR,
    	KEY_DPS, KEY_ATTACK_SPEED, KEY_DAMAGE, KEY_BLOCK_CHANCE,
    	KEY_PRIMARY_ATR, KEY_SECONDARY_ATR, KEY_PASSIVE};
    
    public void addItem(Item item){
        Log.d("addHero", item.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        
        Cursor checkIfExists = db.query(TABLE_ITEMS, ITEM_COLUMNS, "slot = ? AND heroID = ?", new String[] { item.slot, item.heroID } , null, null, null, null);
        if (checkIfExists != null && checkIfExists.getCount() > 0)
            	return;
        
        ContentValues values = new ContentValues();
        values.put(KEY_SLOT, item.slot); 
        values.put(KEY_HERO_ID, item.heroID); 
        
        values.put(KEY_ITEM_NAME, item.name);
        values.put(KEY_ICON, item.icon);
        values.put(KEY_COLOR, item.color);
        values.put(KEY_TOOLTIP, item.tooltip);
        values.put(KEY_ITEM_LEVEL, item.level);
        values.put(KEY_ACCOUNT_BOUND, item.accountBound);
        values.put(KEY_FLAVOR_TEXT, item.flavorText);
        values.put(KEY_TYPE, item.type);
        values.put(KEY_ARMOR, item.armor);
        values.put(KEY_DPS, item.DPS);
        values.put(KEY_ATTACK_SPEED, item.attackSpeed);
        values.put(KEY_DAMAGE, item.damage);
        values.put(KEY_BLOCK_CHANCE, item.blockChance);
        values.put(KEY_PRIMARY_ATR, item.primaryAtr);
        values.put(KEY_SECONDARY_ATR, item.secondaryAtr);
        values.put(KEY_PASSIVE, item.passive);
 
        db.insert(TABLE_ITEMS, null, values);
    }
    
    public List<Item> getAllHerosItems(Hero hero) {
        List<Item> items = new ArrayList<Item>();
  
        String query = "SELECT  * FROM " + TABLE_ITEMS + " WHERE " + KEY_HERO_ID + " = '" + hero.ID + "'";
  
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
  
        Item item = null;
        if (cursor.moveToFirst()) {
            do {
            	item = new Item();
            	item.slot = cursor.getString(0);
            	item.heroID = cursor.getString(1);
            	item.name = cursor.getString(2);
            	item.icon = cursor.getString(3);
            	item.color = cursor.getString(4);
            	item.tooltip = cursor.getString(5);
            	item.level = cursor.getString(6);
            	item.accountBound = cursor.getString(7);
            	item.flavorText = cursor.getString(8);
            	item.type = cursor.getString(9);
            	item.armor = cursor.getString(10);
            	item.DPS = cursor.getString(11);
            	item.attackSpeed = cursor.getString(12);
            	item.damage = cursor.getString(13);
            	item.blockChance = cursor.getString(14);
            	item.primaryAtr = cursor.getString(15);
            	item.secondaryAtr = cursor.getString(16);
            	item.passive = cursor.getString(17);
  
                items.add(item);
            } while (cursor.moveToNext());
        }
  
        Log.d("getAllPlayersHeroes()", items.toString());
        return items;
    }
}
