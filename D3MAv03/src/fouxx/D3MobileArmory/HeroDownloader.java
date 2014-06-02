package fouxx.D3MobileArmory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DecimalFormat;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.d3ma.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HeroDownloader extends AsyncTask<String, Void, Void>{
	private final HttpClient Client;
    private String Error;
    private ProgressDialog Dialog;
    
    private Context context;
	private Typeface font;
	private String careerString;
	private D3MobileArmorySQLiteHelper database;
	private AsyncDelegate delegate;
	
	public HeroDownloader(Context context, AsyncDelegate delegate){
		this.context = context;
		this.Dialog  = new ProgressDialog(context);
		this.Error = "";
		this.Client = new DefaultHttpClient();
		this.font = Typeface.createFromAsset(context.getAssets(),"fonts/DiabloLight.ttf");
		this.database = new D3MobileArmorySQLiteHelper(context);
		this.delegate = delegate;
	}
	
	@SuppressLint("DefaultLocale")
	public void customToast(String toast){
		Toast t = Toast.makeText(context, toast, Toast.LENGTH_SHORT);
		LinearLayout layout = (LinearLayout) t.getView();
		layout.setBackgroundResource(R.drawable.toast_background);
		if (layout.getChildCount() > 0) {
		  TextView tv = (TextView) layout.getChildAt(0);
		  tv.setText(tv.getText().toString().toUpperCase());
		  tv.setTypeface(font);
		  tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		}
		t.show();
	}
	
    protected void onPreExecute() {
        Dialog.setMessage("Downloading source..");
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
		String heroID = urls[1];
		
		try {
			JSONObject details = new JSONObject(hero);
			JSONObject items = details.getJSONObject("items");
			
			String []slots = {"head", "torso", "feet", "hands", "shoulders", "legs", "bracers", 
  				  "mainHand", "offHand", "waist", "rightFinger", "leftFinger", "neck"};
			JSONObject slot;
			for(int i = 0; i < 13; i++){
				if(items.isNull(slots[i])){
					database.addItem(new Item(slots[i], heroID));
					continue;
				}
				slot = items.getJSONObject(slots[i]);				
				String itemName = slot.getString("name");
				String itemIcon = slot.getString("icon");
				String itemColor = slot.getString("displayColor");
				String itemTooltip = slot.getString("tooltipParams");
				
				downloadIcon(itemIcon);
		    	
		    	String itemTooltipUrl = "http://eu.battle.net/api/d3/data/"+itemTooltip;
		    	String tooltip = getSource(itemTooltipUrl);
		    	
		    	JSONObject item = new JSONObject(tooltip);
		    	String itemLevel = item.getString("requiredLevel");
		    	String accountBound = item.getString("accountBound");
		    	String itemFlavorText = (item.isNull("flavorText"))?"":item.getString("flavorText");
		    	String itemType = item.getString("typeName");
		    	
		    	DecimalFormat df = new DecimalFormat("#,###.00");
		    	DecimalFormat no_point = new DecimalFormat("#");
		    	
		    	String itemArmor = "";
		    	if(!item.isNull("armor")){
		    		itemArmor = item.getJSONObject("armor").getString("min");
		    	}
		    	String itemDPS = "", itemAttackSpeed = "", itemDamage = "";
		    	if(!item.isNull("dps")){
		    		Double d_itemDPS = item.getJSONObject("dps").getDouble("min");
		    		itemDPS = df.format(d_itemDPS);
		    		Double d_itemAttackSpeed = item.getJSONObject("attacksPerSecond").getDouble("min");
		    		itemAttackSpeed = df.format(d_itemAttackSpeed);
		    		Double d_itemMinDamage = item.getJSONObject("minDamage").getDouble("min");
		    		Double d_itemMaxDamage = item.getJSONObject("maxDamage").getDouble("min");
		    		JSONObject attributes = item.getJSONObject("attributes");
		    		JSONArray primary = attributes.getJSONArray("primary");
		    		
		    		if(primary.length() > 0){
		    			String text = primary.getJSONObject(0).getString("text");
		    			if(text.contains("Damage") && text.length() - text.replace(" ", "").length() == 2){
		    				String[] parts = text.split(" ", 2);
		    				parts[0] = parts[0].replace("+", "");
		    				String []damage = parts[0].split("�");		//Special character instead of ordinary "-" !!!    				
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
	    		Item new_item = new Item(slots[i], heroID, 
	    				itemName, itemIcon, itemColor, itemTooltip, itemLevel,
	    				accountBound, itemFlavorText, itemType, itemArmor,
	    				itemDPS, itemAttackSpeed, itemDamage, itemBlockChance,
	    				itemPrimary, itemSecondary, itemPassive);
	    		database.addItem(new_item);
	    		// TODO Gems!
			}
			
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
        	customToast(Error);
        } else {
            customToast("Done!");
        }
    }

}
