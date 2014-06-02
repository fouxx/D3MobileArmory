package fouxx.D3MobileArmory;

import java.util.ArrayList;

import com.example.d3ma.R;

import fouxx.D3MobileArmory.HeroListActivity;
import fouxx.D3MobileArmory.MySQLiteHelper;
import fouxx.D3MobileArmory.Player;
import fouxx.D3MobileArmory.ProfileDownloader;
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
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity implements AsyncDelegate {
	
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
        			
//!        			TODO Add confirmation dialog!
        			
        			Player deletePlayer = list.get(pos);
        			db.deletePlayer(deletePlayer);
        			
        			list = db.getAllPlayers();        		
        			ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(getApplicationContext(), R.layout.custom_textview, list);
        			playerList.setAdapter(adapter);
        			playerList.setTextFilterEnabled(true);
        		}
        		if(item.getTitle().equals("Update")){
        			
//					TODO Update
        			
        		}
        		return true;  
            }  
        });
        popup.show();
	    return true;
	}
	
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
				String url = "http://eu.battle.net/api/d3/profile/"+btag+"-"+number+"/";
				
				new ProfileDownloader(MainActivity.this, MainActivity.this).execute(url);
				
				list = db.getAllPlayers();        		
    			adapter = new ArrayAdapter<Player>(getApplicationContext(), R.layout.custom_textview, list);
    			playerList.setAdapter(adapter);
    			playerList.setTextFilterEnabled(true);
    			
				dialog.dismiss();	
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

	@Override
	public void asyncComplete(boolean success) {
		if(success){
			list = db.getAllPlayers();        		
			adapter = new ArrayAdapter<Player>(getApplicationContext(), R.layout.custom_textview, list);
			playerList.setAdapter(adapter);
			playerList.setTextFilterEnabled(true);
		}
	}
}
