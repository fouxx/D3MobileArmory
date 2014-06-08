package fouxx.D3MobileArmory;

import java.text.DecimalFormat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatsFragment extends Fragment {
	D3MobileArmorySQLiteHelper database;
	DecimalFormat df;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        database = new D3MobileArmorySQLiteHelper(getActivity());
        
        Hero hero;
		Bundle extras = getArguments();
		hero = (Hero) extras.getSerializable("HERO");
		hero = database.getHero(hero.ID);
		
		df = new DecimalFormat("###,###,###");
		
		TextView attributesName = (TextView) rootView.findViewById(R.id.attributesName);
		attributesName.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/DiabloLight.ttf"));
		attributesName.setText("ATTRIBUTES");
		
		TextView strValue = (TextView) rootView.findViewById(R.id.strValue);
		double str = Double.parseDouble(hero.a_str);
		strValue.setText(df.format(str));
		
		TextView dexValue = (TextView) rootView.findViewById(R.id.dexValue);
		double dex = Double.parseDouble(hero.a_dex);
		dexValue.setText(df.format(dex));
		
		TextView intValue = (TextView) rootView.findViewById(R.id.intValue);
		double intel = Double.parseDouble(hero.a_int);
		intValue.setText(df.format(intel));
		
		TextView vitValue = (TextView) rootView.findViewById(R.id.vitValue);
		double vit = Double.parseDouble(hero.a_vit);
		vitValue.setText(df.format(vit));
		
		TextView damageValue = (TextView) rootView.findViewById(R.id.damageValue);
		double dmg = Double.parseDouble(hero.damage);
		damageValue.setText(df.format(dmg));
		
		TextView toughnessValue = (TextView) rootView.findViewById(R.id.toughnessValue);
		double tgh = Double.parseDouble(hero.toughness);
		toughnessValue.setText(df.format(tgh));
		
		TextView healingValue = (TextView) rootView.findViewById(R.id.healingValue);
		double heal = Double.parseDouble(hero.healing);
		healingValue.setText(df.format(heal));
         
        return rootView;
    }
}
