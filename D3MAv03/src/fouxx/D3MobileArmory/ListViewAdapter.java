package fouxx.D3MobileArmory;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	private Context context;
	private List<Hero> heroList;
	
	ListViewAdapter(Context context, List<Hero> heroList){
		this.context = context;
		this.heroList = heroList;
	}
	
	@Override
	public int getCount() {
		return heroList.size();
	}

	@Override
	public Object getItem(int position) {
		return heroList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return heroList.indexOf(getItem(position));
	}
	
	private class ViewHolder {
		  ImageView heroPortrait;
		  TextView heroName;
		  TextView heroClass;
		  TextView heroLevel;
		  TextView heroParagonLevel;
		  TextView heroMode;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		View v = convertView;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null){
		 	v = inflater.inflate(R.layout.listview_item, null);
		}
		  
	  	holder = new ViewHolder();
	  
	  	holder.heroPortrait 	  = (ImageView) v.findViewById(R.id.heroPortrait);
	  	holder.heroName 		  = (TextView)  v.findViewById(R.id.heroName);
	  	holder.heroClass 		  = (TextView)  v.findViewById(R.id.heroClass);
	  	holder.heroLevel		  = (TextView)  v.findViewById(R.id.heroLevel);
	  	holder.heroParagonLevel   = (TextView)  v.findViewById(R.id.heroParagonLevel);
	  	holder.heroMode 		  = (TextView)  v.findViewById(R.id.heroMode);
	  
	  	Hero hero = new Hero();
	  	hero = heroList.get(position);
	  
	  	String c = hero.heroClass;
	  	if(c.contains("-"))
	  		c = c.replace("-", "");
	  
	  	String uri = c+"_"+hero.gender;
	  	if(uri.contains(" "))
	  		uri = uri.replace(" ", "");
	  
	  	Resources res = context.getResources();
	  	int resID = res.getIdentifier(uri , "drawable", context.getPackageName());
	  	Drawable drawable = res.getDrawable(resID );
	  	holder.heroPortrait.setImageDrawable(drawable);
	  
	  	c = hero.heroClass;
	  	if(c.contains("-"))
	  		c = c.replace("-", " ");
	  	c = StringUtils.capitalize(c);
	  
	  	holder.heroName.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/DiabloLight.ttf"));
	  	holder.heroName.setText(StringUtils.upperCase(hero.name));

	  	holder.heroLevel.setText(hero.level);
	  	holder.heroParagonLevel.setText("("+hero.paragon+")");
	  	if(hero.paragon.equals("0"))
	  		holder.heroParagonLevel.setText("");
	  	holder.heroClass.setText(c);
	  	holder.heroMode.setText(hero.mode);
			  
		return v;
	}

}
