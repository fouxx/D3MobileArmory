package fouxx.D3MobileArmory;

import java.io.File;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
		return name;		
	}
	
	OnClickListener getOnClickListener(final Context context){
		return new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				//TODO
				final Dialog dialog = new Dialog(context, R.style.customItemDialog);
				//dialog.setContentView(R.layout.item_details_dialog);
				LayoutInflater inflater = dialog.getLayoutInflater();
		        View layout = inflater.inflate(R.layout.item_details_dialog, null);
		        layout.setFocusable(true);
		        layout.setFocusableInTouchMode(true);
		        dialog.setContentView(layout);
		        
				//dialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
				//dialog.getWindow().getAttributes().height = LayoutParams.MATCH_PARENT;
				
				int x = context.getResources().getDisplayMetrics().widthPixels;
				int y = context.getResources().getDisplayMetrics().heightPixels;
				dialog.getWindow().setLayout(x-10, LayoutParams.WRAP_CONTENT);//y-40);
				
				TextView title = (TextView) dialog.findViewById(android.R.id.title);
				Drawable drawable = context.getResources().getDrawable(context.getResources().getIdentifier(color+"_title" , "drawable", context.getPackageName()));
				title.setBackgroundDrawable(drawable);
				title.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
				title.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(color , "color", context.getPackageName())));
				title.setSingleLine(false);
				title.setText(name);
				
				LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.ll);
				
				ll.setOnClickListener(new View.OnClickListener(){
		             @Override
		             public void onClick(View v){
		                 dialog.dismiss();
		             }
		        });
				
				ImageView itemIcon = (ImageView) layout.findViewById(R.id.itemIcon);
				File storagePath = Environment.getExternalStorageDirectory();
	    	    File dir = new File (storagePath.getAbsolutePath() + "/D3MobileArmory");
				File imageFile = new File(dir, icon+".png");
		        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		        if(slot.contains("Finger") || slot.equals("neck") || slot.equals("waist")){
		        	itemIcon.requestLayout();
		        	itemIcon.getLayoutParams().width = 82;
		        	itemIcon.getLayoutParams().height = 82;
		        }
		        itemIcon.setImageBitmap(bitmap);
			  	Drawable bg = context.getResources().getDrawable(context.getResources().getIdentifier("item_"+color+"_no_click" , "drawable", context.getPackageName()));
			  	itemIcon.setBackgroundDrawable(bg);
			  	
			  	TextView itemType = (TextView) layout.findViewById(R.id.itemType);
			  	itemType.setTextColor(context.getResources().getColor(context.getResources().getIdentifier(color , "color", context.getPackageName())));
			  	itemType.setText(type);
				
			  	TextView armorOrDps = (TextView) layout.findViewById(R.id.armorOrDps);
			  	TextView armorOrDpsDesc = (TextView) layout.findViewById(R.id.armorOrDpsDesc);
			  	
			  	TextView damageOrBlock = (TextView) layout.findViewById(R.id.damageOrBlock);
			  	TextView damageOrBlockDesc = (TextView) layout.findViewById(R.id.damageOrBlockDesc);
			  	TextView attackSpeed = (TextView) layout.findViewById(R.id.attackSpeed);
			  	TextView attackSpeedDesc = (TextView) layout.findViewById(R.id.attackSpeedDesc);
			  	if(!armor.equals("")){
			  		armorOrDps.setText(armor);
			  		armorOrDpsDesc.setText("Armor");
			  	}else if(!DPS.equals("")){
			  		armorOrDps.setText(DPS);
			  		armorOrDpsDesc.setText("Damage Per Second");
			  		
			  		attackSpeed.setText(Item.this.attackSpeed);
			  		attackSpeedDesc.setText(" Attacks Per Second");
			  	}else{
			  		armorOrDps.setVisibility(View.GONE);
			  		armorOrDpsDesc.setVisibility(View.GONE);
			  		
			  		attackSpeed.setVisibility(View.GONE);
			  		attackSpeedDesc.setVisibility(View.GONE);
			  	}
			  	
			  	if(!blockChance.equals("")){
			  		damageOrBlock.setText(blockChance);
			  		damageOrBlockDesc.setText(" Chance To Block");
			  	}else if(!DPS.equals("")){
			  		damageOrBlock.setText(damage);
			  		damageOrBlockDesc.setText(" Damage");
			  	}else{			  	
			  		damageOrBlock.setVisibility(View.GONE);
			  		damageOrBlockDesc.setVisibility(View.GONE);
			  	}
			  	TextView primaryName = (TextView) layout.findViewById(R.id.primaryName);
			  	TextView primaryAttr = (TextView) layout.findViewById(R.id.primaryAttr);
			  	TextView secondaryName = (TextView) layout.findViewById(R.id.secondaryName);
			  	TextView secondaryAttr = (TextView) layout.findViewById(R.id.secondaryAttr);
			  	TextView passiveAttr = (TextView) layout.findViewById(R.id.passiveAttr);
			  	
			  	if(!primaryAtr.equals("")){
			  		primaryName.setText("Primary");
			  		primaryAttr.setText(primaryAtr.substring(0, primaryAtr.length() - 1));
			  	}else{
			  		primaryName.setVisibility(View.GONE);
			  		primaryAttr.setVisibility(View.GONE);
			  	}
			  	if(!secondaryAtr.equals("")){
			  		secondaryName.setText("Secondary");
			  		secondaryAttr.setText(secondaryAtr.substring(0, secondaryAtr.length() - 1));
			  	}else{
			  		secondaryName.setVisibility(View.GONE);
			  		secondaryAttr.setVisibility(View.GONE);
			  	}
			  	
			  	if(!passive.equals("")){
			  		passiveAttr.setText(passive.substring(0, passive.length() - 1));
			  	}else
			  		passiveAttr.setVisibility(View.GONE);
			  	
		  		TextView requiredLvl = (TextView) dialog.findViewById(R.id.requiredLvl);
		  		requiredLvl.setText("Required level: "+level);
		  		
		  		TextView flavorTxt = (TextView) dialog.findViewById(R.id.flavorText);
			  	
			  	if(!flavorText.equals("")){
			  		View line = (View) dialog.findViewById(R.id.lineSeparator);
			  		line.setVisibility(View.VISIBLE);
			  		flavorTxt.setText(flavorText);
			  	}else
			  		flavorTxt.setVisibility(View.GONE);
			  		
				dialog.show();
			}
		};
	}
}
