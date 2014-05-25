package com.example.d3ma;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.commons.lang3.StringUtils;

import com.example.d3ma.Player;
import com.example.d3ma.MySQLiteHelper;
import com.example.d3ma.HeroListActivity;

import android.support.v7.app.ActionBarActivity;
import android.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {
	
	Button addNewPlayer, add, cancel;
	Typeface font;
	Dialog dialog;
	ListView playerList;
	MySQLiteHelper db;
	
	String Career, Heroes;
	ArrayList<Player> list;
	ArrayAdapter<Player> adapter;

    String[] menuItems = {"DELETE", "UPDATE"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		
		db = new MySQLiteHelper(this);
		addNewPlayer = (Button) findViewById(R.id.addNewPlayer);
		font = Typeface.createFromAsset(getAssets(),"fonts/DiabloLight.ttf");
		addNewPlayer.setTypeface(font);

		playerList = (ListView) findViewById(R.id.playerList);
		list = db.getAllPlayers();
		adapter = new ArrayAdapter<Player>(this, R.layout.custom_textview, list);
		
		playerList.setAdapter(adapter);
		playerList.setTextFilterEnabled(true);

	    //registerForContextMenu(playerList);
		
		playerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
		        return onLongListItemClick(v, pos, id);
		    }
		});
		
		playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				Player clickedPlayer = list.get(position);
				ArrayList<Hero> clickedPlayersHeroes = db.getAllPlayersHeroes(clickedPlayer);
	        	Intent i = new Intent(getApplicationContext(), HeroListActivity.class);
	        	i.putExtra("PLAYER", clickedPlayer);
	        	i.putExtra("LIST", clickedPlayersHeroes);
	        	startActivity(i);
			}
		});
	}
	
	protected boolean onLongListItemClick(View v, final int pos, long id) {
		PopupMenu popup = new PopupMenu(MainActivity.this, v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
        	public boolean onMenuItemClick(MenuItem item) {        		
        		if(item.getTitle().equals("Delete")){
//!        			Add confirmation dialog!
        			Player deletePlayer = list.get(pos);
        			db.deletePlayer(deletePlayer);
        			
        			list = db.getAllPlayers();        		
        			ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(getApplicationContext(), R.layout.custom_textview, list);
        			playerList.setAdapter(adapter);
        			playerList.setTextFilterEnabled(true);
        		}
        		if(item.getTitle().equals("Update")){
        			//Do sth
        		}
        		return true;  
            }  
        });
        popup.show();
	    return true;
	}	
	
	/*
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
		if (v.getId()==R.id.playerList) {
		    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		    menu.setHeaderTitle(list.get(info.position).toString());
		    for (int i = 0; i < menuItems.length; i++) {
		    	menu.add(Menu.NONE, i, i, menuItems[i]);
		    }
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		
		if(menuItemIndex == 0){
			Player deletePlayer = list.get(info.position);
			db.deletePlayer(deletePlayer);
			
			list = db.getAllPlayers();        		
			ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(getApplicationContext(), R.layout.custom_textview, list);
			playerList.setAdapter(adapter);
			playerList.setTextFilterEnabled(true);
		}
		return true;
	}
	*/
	
	public void onAddNewPlayerClick(View v){
		dialog = new Dialog(this, R.style.customDialog);
		dialog.setContentView(R.layout.add_profile_dialog);
		TextView title = (TextView) dialog.findViewById(android.R.id.title);
		title.setText("ADD NEW PROFILE");
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		title.setTypeface(font);
		dialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
		
		add = (Button) dialog.findViewById(R.id.buttonAdd);
		cancel = (Button) dialog.findViewById(R.id.buttonCancel);
		add.setTypeface(font);
		cancel.setTypeface(font);
		
		dialog.show();
		
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isNetworkStatusAvialable(getApplicationContext())){
					customToast("Unable to download content.\nCheck Internet connection.");
					return;
				}				
				
				EditText new_btag = (EditText) dialog.findViewById(R.id.btag);
				String btag = new_btag.getText().toString();
				EditText new_number = (EditText) dialog.findViewById(R.id.number);
				String number = new_number.getText().toString();
				String profile = btag+"-"+number;
				String url_career = "http://eu.battle.net/d3/en/profile/"+profile+"/career";
				String url = "http://eu.battle.net/d3/en/profile/"+profile+"/";
				
				new DownloadProfile().execute(url_career, url);
				dialog.dismiss();
				
        		list = db.getAllPlayers();        		
        		adapter = new ArrayAdapter<Player>(getApplicationContext(), R.layout.custom_textview, list);
        		playerList.setAdapter(adapter);
        		playerList.setTextFilterEnabled(true);
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	
	public void customToast(String toast){
		Toast t = Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT);
		LinearLayout layout = (LinearLayout) t.getView();
		layout.setBackgroundResource(R.drawable.toast_background);
		if (layout.getChildCount() > 0) {
		  TextView tv = (TextView) layout.getChildAt(0);
		  tv.setText(tv.getText().toString().toUpperCase());
		  tv.setTypeface(font);
		  tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		}
		t.show();
	}
	
	public static boolean urlExists(String url){
        HttpURLConnection huc;
        System.setProperty("http.keepAlive", "false");
        try {
        	huc = (HttpURLConnection) new URL(url).openConnection();
        	huc.setRequestMethod("HEAD");
        	huc.setRequestProperty( "Accept-Encoding", "" ); 

            return (huc.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
         }
	}
	
    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) 
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
            if(netInfos.isConnected()) 
                return true;
        }
        return false;
    }
	
	private class DownloadProfile extends AsyncTask<String, Void, Void> {
        private final HttpClient Client = new DefaultHttpClient();
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        
        protected void onPreExecute() {
            Dialog.setMessage("Downloading source..");
            Dialog.show();
        }
        
        String getSource(String url){
        	try {
                HttpGet httpget = new HttpGet(url);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String source = Client.execute(httpget, responseHandler);
                return source;
            } catch (ClientProtocolException e) {
                Error = e.getMessage();
                cancel(true);
            } catch (IOException e) {
                Error = e.getMessage();
                cancel(true);
            }
        	
			return null;
        }
        
        @Override
        protected Void doInBackground(String... urls) {
        	String url_career = urls[0];
        	String url_heroes = urls[1];
        	if(!urlExists(url_career) || !urlExists(url_heroes)){
        		Error = "This profile doesn't exist";
        		return null;
        	}
        	
        	Career = getSource(url_career);
        	Heroes = getSource(url_heroes);
        	
        	String btagLine = StringUtils.substringBetween(Career, "Profile.baseUrl", ";");
        	String btag = StringUtils.substringBetween(btagLine, "profile/", "/");
        	String paragonSC = "-", paragonHC = "-";
        	if(Career.contains("kill-section paragon")){
	        	String paragon = StringUtils.substringBetween(Career, "kill-section paragon", "clear");
	        	if(paragon.contains("hardcore")){
	        		paragonSC = StringUtils.substringBetween(paragon, "num-kills\">", " |");
	        		paragonHC = StringUtils.substringBetween(paragon, "hardcore\">", "<");
	        		System.out.println(paragonSC+" "+paragonHC);
	        	}else{
	        		paragonSC = StringUtils.substringBetween(paragon, "num-kills\">", "<");
	        		System.out.println(paragonSC+" "+paragonHC);
	        	}
        	}
        	Player newPlayer = new Player(btag, paragonSC, paragonHC);
        	db.addPlayer(newPlayer);
        	
        	String heroes = StringUtils.substringBetween(Heroes, "Profile.heroes", "]");
            String heroesIds [] = StringUtils.substringsBetween(heroes, "id: ", ",");
            String heroesNames [] = StringUtils.substringsBetween(heroes, "name: '", "',");
            String heroesLevels [] = StringUtils.substringsBetween(heroes, "level: ", ",");
            String heroesClass [] = StringUtils.substringsBetween(heroes, "'class': '", "',");
            
            int noOfHeroes = heroesIds.length;
            String heroesGender [] = new String[noOfHeroes];
            String heroesGameMode [] = new String[noOfHeroes];
            
            for(int i = 0; i < noOfHeroes; i++){
            	String info = StringUtils.substringBetween(Heroes, "hero-tab "+heroesClass[i]+"-", "\" href=\""+heroesIds[i]);
            	if(info.contains("\n")){
            		info = StringUtils.substringAfterLast(info, "hero-tab "+heroesClass[i]+"-");
            	}
            	if(info.contains("-active"))
            		info = info.replace("-active", "");
            	if(info.contains(" active"))
            		info = info.replace(" active", "");

            	if(info.contains("hardcore")){
            		String[] parts = info.split(" ");
            		heroesGender[i] = parts[0];
            		heroesGameMode[i] = parts[1];
            	}else{
            		heroesGender[i] = info;
            		heroesGameMode[i] = "softcore";
            	}
            	
            	Hero newHero = new Hero(heroesIds[i], heroesNames[i], heroesGender[i], heroesLevels[i], heroesClass[i], heroesGameMode[i], "false", btag);
            	db.addHero(newHero);
            	          	
            	System.out.println(info);
            }
        	
			return null;
        }
        
        @Override
        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            if (Error != null) {
            	customToast(Error);
            } else {
                customToast("Done!");
        		list = db.getAllPlayers();        		
        		ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(getApplicationContext(), R.layout.custom_textview, list);
        		playerList.setAdapter(adapter);
        		playerList.setTextFilterEnabled(true);
            }
        }
	}
}
