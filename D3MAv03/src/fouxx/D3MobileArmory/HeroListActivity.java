package fouxx.D3MobileArmory;

import java.util.ArrayList;
import com.example.d3ma.R;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

@SuppressLint("DefaultLocale")
public class HeroListActivity extends ActionBarActivity {
	
	ListView heroList;
	TextView playerBtag;
	TextView playerNumber;
	
	ListViewAdapter adapter;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hero_list);
		getActionBar().hide();
		
		heroList = (ListView) findViewById(R.id.heroList);
		Player player = new Player();
		ArrayList <Hero> list = new ArrayList <Hero>();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			list = (ArrayList<Hero>) extras.getSerializable("LIST");
			player = (Player) extras.getSerializable("PLAYER");
			System.out.println(player.toString());
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
		
	}

}
