package fouxx.D3MobileArmory;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Item {
	String slot;
	String heroID;
	
	String name;
	String icon;
	String color;
	String tooltip;
	String level;
	String accountBound;
	String flavorText;
	String type;
	String armor;
	String DPS;
	String attackSpeed;
	String damage;
	String blockChance;
	String primaryAtr;
	String secondaryAtr;
	String passive;
	
	public Item() { }
	
	public Item(String slot, String heroID,
			String name, 		 String icon, 			String color, 	String tooltip, 	String level,
			String accountBound, String flavorText, 	String type, 	String armor,
			String DPS, 		 String attackSpeed, 	String damage, 	String blockChance,
			String primaryAtr, 	 String secondaryAtr, 	String passive){
		
		this.slot = slot;
		this.heroID = heroID;
		
		this.name = name;
		this.icon = icon;
		this.color = color;
		this.tooltip = tooltip;
		this.level = level;
		this.accountBound = accountBound;
		this.flavorText = flavorText;
		this.type = type;
		this.armor = armor;
		this.DPS = DPS;
		this.attackSpeed = attackSpeed;
		this.damage = damage;
		this.blockChance = blockChance;
		this.primaryAtr = primaryAtr;
		this.secondaryAtr = secondaryAtr;
		this.passive = passive;
	}
	
	public Item(String slot, String heroID){
		this(slot, heroID, 
				"empty", "", "", "", "",
				"", "", "", "",
				"", "", "", "",
				"", "", "");
	}
	
	@Override
	public String toString(){
		//FIXME!
		return DPS;		
	}
	
	OnClickListener getOnClickListener(final Context context){
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//TODO
				Dialog dialog = new Dialog(context, R.style.customItemDialog);
				dialog.setContentView(R.layout.item_details_dialog);

				//dialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
				//dialog.getWindow().getAttributes().height = LayoutParams.MATCH_PARENT;
				
				int x = context.getResources().getDisplayMetrics().widthPixels;
				int y = context.getResources().getDisplayMetrics().heightPixels;
				dialog.getWindow().setLayout(x-10, y-40);
				
				TextView title = (TextView) dialog.findViewById(android.R.id.title);
				Drawable drawable = context.getResources().getDrawable(context.getResources().getIdentifier(color+"_title" , "drawable", context.getPackageName()));
				title.setBackgroundDrawable(drawable);
				title.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
				title.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(color , "color", context.getPackageName())));
				title.setText(name);

				dialog.show();
				
				Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
			}
		};
	}
}
