package fouxx.D3MobileArmory;

import java.io.File;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class GearFragment extends Fragment {
	D3MobileArmorySQLiteHelper database;
	List<Item> itemList;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_gear, container, false);
        database = new D3MobileArmorySQLiteHelper(getActivity());
		
		Hero hero;
		Bundle extras = getArguments();
		hero = (Hero) extras.getSerializable("HERO");
		itemList = database.getAllHerosItems(hero);
		
	  	TextView name = (TextView) rootView.findViewById(R.id.name);
	  	
	  	name.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/DiabloLight.ttf"));
	  	name.setText(hero.name.toUpperCase());
	  	
		for(final Item item : itemList){
	        ImageButton itemIcon = (ImageButton) rootView.findViewById(getResources().getIdentifier(item.slot, "id", getActivity().getPackageName()));
			if(item.name.equals("empty")){
				Drawable drawable = getResources().getDrawable(getResources().getIdentifier("item_empty", "drawable", getActivity().getPackageName()));
				itemIcon.setBackgroundDrawable(drawable);
				itemIcon.setClickable(false);
				continue;
			}
    	    File storagePath = Environment.getExternalStorageDirectory();
    	    File dir = new File (storagePath.getAbsolutePath() + "/D3MobileArmory");
			File imageFile = new File(dir, item.icon+".png");
	        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
	        itemIcon.setImageBitmap(bitmap);
		  	Drawable drawable = getResources().getDrawable(getResources().getIdentifier("item_"+item.color , "drawable", getActivity().getPackageName()));
		  	itemIcon.setBackgroundDrawable(drawable);
		  	
		  	itemIcon.setOnClickListener(item.getOnClickListener(getActivity()));
		}
		
        return rootView;
    }
}
