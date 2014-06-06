package fouxx.D3MobileArmory;

import java.io.File;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.widget.ImageButton;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;

public class HeroDetailsActivity extends ActionBarActivity {
	
	D3MobileArmorySQLiteHelper database;
	List<Item> itemList;
	
	@SuppressWarnings("deprecation")
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
		
	  	TextView name = (TextView) findViewById(R.id.name);
	  	name.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/DiabloLight.ttf"));
	  	name.setText(hero.name.toUpperCase());
				
		for(final Item item : itemList){
	        ImageButton itemIcon = (ImageButton) findViewById(getResources().getIdentifier(item.slot, "id", getPackageName()));
			if(item.name.equals("empty")){
				Drawable drawable = getResources().getDrawable(getResources().getIdentifier("item_empty", "drawable", getPackageName()));
				itemIcon.setBackgroundDrawable(drawable);
				itemIcon.setClickable(false);
				continue;
			}
    	    File storagePath = Environment.getExternalStorageDirectory();
    	    File dir = new File (storagePath.getAbsolutePath() + "/D3MobileArmory");
			File imageFile = new File(dir, item.icon+".png");
	        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
	        itemIcon.setImageBitmap(bitmap);
		  	Drawable drawable = getResources().getDrawable(getResources().getIdentifier("item_"+item.color , "drawable", getPackageName()));
		  	itemIcon.setBackgroundDrawable(drawable);
		  	
		  	itemIcon.setOnClickListener(item.getOnClickListener(HeroDetailsActivity.this));
		}
	}
}
