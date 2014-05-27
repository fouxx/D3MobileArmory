package fouxx.D3MobileArmory;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.example.d3ma.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Hero> heroList;
	
	ListViewAdapter(Context context, ArrayList<Hero> heroList){
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
		 }

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		View v = convertView;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			  v = inflater.inflate(R.layout.listview_item, null);
			  
			  holder = new ViewHolder();
			  
			  holder.heroPortrait 	= (ImageView) v.findViewById(R.id.heroPortrait);
			  holder.heroName 		= (TextView)  v.findViewById(R.id.heroName);
			  holder.heroClass 		= (TextView)  v.findViewById(R.id.heroClass);
			  
			  Hero hero = new Hero();
			  hero = heroList.get(position);
			  
			  
			  String c = hero.heroClass;
			  if(c.contains("-"))
				  c = c.replace("-", "");
			  
			  String uri = "drawable/"+c+"_"+hero.gender;
			  System.out.println(uri);
			  
			  int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
			  Drawable image = context.getResources().getDrawable(imageResource);
			  
			  holder.heroPortrait.setImageDrawable(image);
			  
			  c = hero.heroClass;
			  if(c.contains("-"))
				  c = c.replace("-", " ");
			  c = StringUtils.capitalize(c);
			  
			  holder.heroName.setText(hero.name);
			  holder.heroClass.setText(c);
			  
		return v;
	}

}
