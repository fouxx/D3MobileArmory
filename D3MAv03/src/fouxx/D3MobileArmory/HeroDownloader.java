package fouxx.D3MobileArmory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

public class HeroDownloader extends AsyncTask<String, Void, Void>{
	private final HttpClient Client;
    private String Error;
    private ProgressDialog Dialog;
    
    private Context context;
	private D3MobileArmorySQLiteHelper database;
	private AsyncDelegate delegate;
	
	public HeroDownloader(Context context, AsyncDelegate delegate){
		this.context = context;
		this.delegate = delegate;
		
		this.Dialog  = new ProgressDialog(context);
		this.Error = "";
		this.Client = new DefaultHttpClient();
		this.database = new D3MobileArmorySQLiteHelper(context);
	}
	
    protected void onPreExecute() {
        Dialog.setMessage("Downloading source...");
        Dialog.show();
    }
    
    String getSource(String url){
    	try {
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String source = Client.execute(httpget, responseHandler);
            return source;
        } catch (ClientProtocolException e) {
            Error = e.getMessage();
            cancel(true);
        } catch (IOException e) {
            Error = e.getMessage();
            cancel(true);
        }
    	
		return null;
    }
    
    private void downloadIcon(String itemIcon){
    	try{
    		URL url = new URL ("http://media.blizzard.com/d3/icons/items/large/"+itemIcon+".png");
	    	InputStream input = url.openStream();
	    	try {
	    	    File storagePath = Environment.getExternalStorageDirectory();
	    	    File dir = new File (storagePath.getAbsolutePath() + "/D3MobileArmory");
	    	    if(!dir.exists())
	    	    	dir.mkdir();
	    	    OutputStream output = new FileOutputStream (new File(dir, itemIcon+".png"));
	    	    try {
	    	        byte[] buffer = new byte[4096];
	    	        int bytesRead = 0;
	    	        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
	    	            output.write(buffer, 0, bytesRead);
	    	        }
	    	    } finally {
	    	        output.close();
	    	    }
	    	} finally {
	    	    input.close();
	    	}
	    } catch (ClientProtocolException e) {
	        Error = e.getMessage();
	        cancel(true);
	    } catch (IOException e) {
	        Error = e.getMessage();
	        cancel(true);
	    }
    }

	@Override
	protected Void doInBackground(String... urls) {
		String heroUrl = urls[0];	
		String hero = getSource(heroUrl);
		String btag = urls[1];
		String heroID = urls[2];
		
		try {
			JSONObject details = new JSONObject(hero);
			JSONObject items = details.getJSONObject("items");
			
			String []slots = {"head", "torso", "feet", "hands", "shoulders", "legs", "bracers", 
  				  "mainHand", "offHand", "waist", "rightFinger", "leftFinger", "neck"};
			JSONObject slot;
			for(String slotName : slots){
				if(items.isNull(slotName)){
					database.addItem(new Item(slotName, heroID));
					continue;
				}
				slot = items.getJSONObject(slotName);				
				String itemName = slot.getString("name");
				String itemIcon = slot.getString("icon");
				String itemColor = slot.getString("displayColor");
				String itemTooltip = slot.getString("tooltipParams");
				
				if(itemIcon.equals("crusadershield_000_demonhunter_male"))
					itemIcon = "crusadershield_001_demonhunter_male";
				if(itemIcon.equals("shield_000_demonhunter_male"))
					itemIcon = "shield_001_demonhunter_male";
				downloadIcon(itemIcon);
		    	
		    	String itemTooltipUrl = "http://eu.battle.net/api/d3/data/"+itemTooltip;
		    	String tooltip = getSource(itemTooltipUrl);
		    	
		    	JSONObject item = new JSONObject(tooltip);
		    	String itemLevel = item.getString("requiredLevel");
		    	String accountBound = item.getString("accountBound");
		    	String itemFlavorText = (item.isNull("flavorText"))?"":item.getString("flavorText");
		    	String itemType = item.getString("typeName");
		    	
		    	DecimalFormat df = new DecimalFormat("#,###.0");
		    	DecimalFormat two_places = new DecimalFormat("#.00");
		    	DecimalFormat no_point = new DecimalFormat("#");
		    	
		    	Double d_itemArmor;
		    	String itemArmor = "";
		    	if(!item.isNull("armor")){
		    		d_itemArmor = item.getJSONObject("armor").getDouble("min");
		    		itemArmor = no_point.format(d_itemArmor);
		    	}
		    	String itemDPS = "", itemAttackSpeed = "", itemDamage = "";
		    	if(!item.isNull("dps")){
		    		Double d_itemDPS = item.getJSONObject("dps").getDouble("min");
		    		itemDPS = df.format(d_itemDPS);
		    		Double d_itemAttackSpeed = item.getJSONObject("attacksPerSecond").getDouble("min");
		    		itemAttackSpeed = two_places.format(d_itemAttackSpeed);
		    		Double d_itemMinDamage = item.getJSONObject("minDamage").getDouble("min");
		    		Double d_itemMaxDamage = item.getJSONObject("maxDamage").getDouble("min");
		    		JSONObject attributes = item.getJSONObject("attributes");
		    		JSONArray primary = attributes.getJSONArray("primary");
		    		
		    		if(primary.length() > 0){
		    			String text = primary.getJSONObject(0).getString("text");
		    			if(!text.contains("imum") && text.contains("Damage") && text.length() - text.replace(" ", "").length() == 2){
		    				String[] parts = text.split(" ", 2);
		    				parts[0] = parts[0].replace("+", "");
		    				String []damage = parts[0].split("–");		//Special character instead of ordinary "-" !!!    				
		    				Double min = Double.parseDouble(damage[0]);
		    				Double max = Double.parseDouble(damage[1]);
		    				d_itemMinDamage += min;
		    				d_itemMaxDamage += max;
		    			}
		    		}
		    		String itemMinDamage = no_point.format(d_itemMinDamage);
		    		String itemMaxDamage = no_point.format(d_itemMaxDamage);
		    		itemDamage = itemMinDamage+"-"+itemMaxDamage;
		    	}
		    	String itemBlockChance = "";
		    	if(!item.isNull("blockChance")){
		    		Double d_itemBlockChance = item.getJSONObject("blockChance").getDouble("min");
		    		d_itemBlockChance *= 100;
		    		itemBlockChance = no_point.format(d_itemBlockChance)+"%";
		    	}
		    	
	    		JSONObject attributes = item.getJSONObject("attributes");
	    		JSONArray a_primary = attributes.getJSONArray("primary");
	    		String itemPrimary = "";
	    		for(int j = 0; j < a_primary.length(); j++){
	    			itemPrimary += a_primary.getJSONObject(j).getString("text")+"\n";
	    		}
	    		
	    		JSONArray a_secondary = attributes.getJSONArray("secondary");
	    		String itemSecondary = "";
	    		for(int j = 0; j < a_secondary.length(); j++){
	    			itemSecondary += a_secondary.getJSONObject(j).getString("text")+"\n";
	    		}
	    		JSONArray a_passive = attributes.getJSONArray("passive");
	    		String itemPassive = "";
	    		for(int j = 0; j < a_passive.length(); j++){
	    			itemPassive += a_passive.getJSONObject(j).getString("text")+"\n";
	    		}
	    		Item new_item = new Item(slotName, heroID, 
	    				itemName, itemIcon, itemColor, itemTooltip, itemLevel,
	    				accountBound, itemFlavorText, itemType, itemArmor,
	    				itemDPS, itemAttackSpeed, itemDamage, itemBlockChance,
	    				itemPrimary, itemSecondary, itemPassive);
	    		database.addItem(new_item);
	    		// TODO Gems!
			}
			String url = "http://eu.battle.net/d3/en/profile/"+btag+"/hero/"+heroID;
			String stats = getSource(url);
			
			String attributes = StringUtils.substringBetween(stats, "attributes-core", "resources");
			
			String strength = StringUtils.substringBetween(attributes, "Strength", "</li>");
			strength = StringUtils.substringBetween(strength, "value\">", "<");
			
			String dexterity = StringUtils.substringBetween(attributes, "Dexterity", "</li>");
			dexterity = StringUtils.substringBetween(dexterity, "value\">", "<");
			
			String intelligence = StringUtils.substringBetween(attributes, "Intelligence", "</li>");
			intelligence = StringUtils.substringBetween(intelligence, "value\">", "<");
			
			String vitality = StringUtils.substringBetween(attributes, "Vitality", "</li>");
			vitality = StringUtils.substringBetween(vitality, "value\">", "<");
			
			String damage = StringUtils.substringBetween(attributes, "Damage", "</li>");
			damage = StringUtils.substringBetween(damage, "value\">", "<");
			
			String toughness = StringUtils.substringBetween(attributes, "Toughness", "</li>");
			toughness = StringUtils.substringBetween(toughness, "value\">", "<");
			
			String healing = StringUtils.substringBetween(attributes, "Healing", "</li>");
			healing = StringUtils.substringBetween(healing, "value\">", "<");
			
			String life = details.getJSONObject("stats").getString("life");
			String resource = details.getJSONObject("stats").getString("primaryResource");

			String resource2 = details.getJSONObject("stats").getString("secondaryResource");
			if(!resource2.equals("0"))
				resource = resource+" "+resource2;
			
			
			database.addHeroStats(heroID, strength, dexterity, intelligence, vitality,
					damage, toughness, healing, life, resource);
		} catch (JSONException e) {
            e.printStackTrace();
        }
		return null;
	}
	
    @Override
    protected void onPostExecute(Void unused) {
        delegate.asyncComplete(true);
        Dialog.dismiss();
        if (!Error.equals("")) {
        	new D3MAToast(context, Error).show();
        } else {
        	new D3MAToast(context, "Done!").show();
        }
    }

}
