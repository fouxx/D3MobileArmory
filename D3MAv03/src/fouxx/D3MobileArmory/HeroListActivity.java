package fouxx.D3MobileArmory;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

@SuppressLint("DefaultLocale")
public class HeroListActivity extends ActionBarActivity implements AsyncDelegate {
	
	ListView heroList;
	TextView playerBtag;
	TextView playerNumber;
	
	ListViewAdapter adapter;
	
	Player player;
	Hero hero;
	List<Hero> list;
	D3MobileArmorySQLiteHelper database;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hero_list);
		getActionBar().hide();
		
		database = new D3MobileArmorySQLiteHelper(this);
		heroList = (ListView) findViewById(R.id.heroList);
		Bundle extras = getIntent().getExtras();
		
		list = (ArrayList<Hero>) extras.getSerializable("LIST");
		player = (Player) extras.getSerializable("PLAYER");
		
		playerBtag = (TextView) findViewById(R.id.btag);
		playerNumber = (TextView) findViewById(R.id.number);
		playerBtag.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/DiabloLight.ttf"));
		String[] parts = player.btag.split("-");
		playerBtag.setText(parts[0].toUpperCase());
		playerNumber.setText("#"+parts[1]);
		
		adapter = new ListViewAdapter(getApplicationContext(), list);
        heroList.setAdapter(adapter);
		
		heroList.setAdapter(adapter);
		heroList.setTextFilterEnabled(true);
		
		heroList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				hero = list.get(position);
				
				if(!database.isGearDownloaded(hero.ID)){
					if(!isNetworkStatusAvialable(getApplicationContext())){
						new D3MAToast(HeroListActivity.this, "Unable to download content.\nCheck Internet connection.").show();
						return;
					}	
					String heroProfile = "http://eu.battle.net/api/d3/profile/"+player.btag+"/hero/"+hero.ID;
					new HeroDownloader(HeroListActivity.this, HeroListActivity.this).execute(heroProfile, hero.ID);
				}else{
					Intent i = new Intent(getApplicationContext(), HeroTabsActivity.class);
		        	i.putExtra("HERO", hero);
		        	startActivity(i);
				}
			}
		});
		
	}
	
    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
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
			database.setGearDownloaded(hero.ID);
			Intent i = new Intent(getApplicationContext(), HeroTabsActivity.class);
        	i.putExtra("HERO", hero);
        	startActivity(i);
			System.out.println("Hooray!");
		}	
	}

}
