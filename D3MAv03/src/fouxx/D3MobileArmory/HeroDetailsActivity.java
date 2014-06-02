package fouxx.D3MobileArmory;

import java.io.File;
import java.util.List;

import com.example.d3ma.R;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class HeroDetailsActivity extends ActionBarActivity {
	
	D3MobileArmorySQLiteHelper database;
	List<Item> itemList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hero_details);
		getActionBar().hide();
		
		database = new D3MobileArmorySQLiteHelper(this);
		
		Hero hero;
		Bundle extras = getIntent().getExtras();
		hero = (Hero) extras.getSerializable("HERO");
		itemList = database.getAllHerosItems(hero);
				
		for(final Item item : itemList){
			if(!item.slot.equals("head") && !item.slot.equals("torso"))
				continue;
			File imageFile = new File("/sdcard/D3MobileArmory/"+item.icon+".png");
	        ImageButton jpgView = (ImageButton) findViewById(getResources().getIdentifier(item.slot, "id", getPackageName()));
	        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
	        jpgView.setImageBitmap(bitmap);
	        
	        jpgView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
// TODO			   Toast.makeText(HeroDetailsActivity.this, item.name, Toast.LENGTH_SHORT).show();
				}
			});
			
		}
	}
}
