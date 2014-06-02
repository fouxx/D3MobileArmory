package fouxx.D3MobileArmory;

import java.util.ArrayList;

import com.example.d3ma.R;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

@SuppressLint("DefaultLocale")
public class HeroListActivity extends ActionBarActivity implements AsyncDelegate {
	
	ListView heroList;
	TextView playerBtag;
	TextView playerNumber;
	
	ListViewAdapter adapter;
	
	Player player;
	ArrayList <Hero> list;
	MySQLiteHelper db;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hero_list);
		getActionBar().hide();
		
		heroList = (ListView) findViewById(R.id.heroList);
		player = new Player();
		list = new ArrayList <Hero>();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			list = (ArrayList<Hero>) extras.getSerializable("LIST");
			player = (Player) extras.getSerializable("PLAYER");
		}
		playerBtag = (TextView) findViewById(R.id.btag);
		playerNumber = (TextView) findViewById(R.id.number);
		playerBtag.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/DiabloLight.ttf"));
		String btag = player.btag;
		String[] parts = btag.split("-");
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
				Hero hero = list.get(position);
				if(hero.downloaded.equals("false")){
					String heroProfile = "http://eu.battle.net/api/d3/profile/"+player.btag+"/hero/"+hero.ID;
					new HeroDownloader(HeroListActivity.this, HeroListActivity.this).execute(heroProfile, hero.ID);
				}
	        	//Intent i = new Intent(getApplicationContext(), HeroGearActivity.class);
	        	//i.putExtra("HERO", hero);
	        	//startActivity(i);
			}
		});
		
	}

	@Override
	public void asyncComplete(boolean success) {
		if(success){
			System.out.println("Hooray!");
		}	
	}

}
