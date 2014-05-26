package fouxx.D3MobileArmory;

import java.util.ArrayList;

import com.example.d3ma.R;

import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Bundle;

public class HeroListActivity extends ActionBarActivity {
	
	ListView heroList;
	ArrayAdapter<Hero> adapter;

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
		adapter = new ArrayAdapter<Hero>(this, R.layout.custom_textview, list);
		
		heroList.setAdapter(adapter);
		heroList.setTextFilterEnabled(true);
		
	}

}
