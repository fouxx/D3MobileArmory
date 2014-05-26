package fouxx.D3MobileArmory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.d3ma.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileDownloader extends AsyncTask<String, Void, Void> {
	private final HttpClient Client;
    private String Error;
    private ProgressDialog Dialog;
    
	private Context context;
	private Typeface font;
	private String Career, Heroes;
	private MySQLiteHelper database;
	private AsyncDelegate delegate;
	
	public ProfileDownloader(Context context, AsyncDelegate delegate){
		this.context = context;
		this.Dialog  = new ProgressDialog(context);
		this.Error = null;
		this.Client = new DefaultHttpClient();
		this.font = Typeface.createFromAsset(context.getAssets(),"fonts/DiabloLight.ttf");
		this.database = new MySQLiteHelper(context);
		this.delegate = delegate;
	}
    
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
	
	public static boolean urlExists(String url){
        HttpURLConnection huc;
        System.setProperty("http.keepAlive", "false");
        try {
        	huc = (HttpURLConnection) new URL(url).openConnection();
        	huc.setRequestMethod("HEAD");
        	huc.setRequestProperty( "Accept-Encoding", "" ); 

            return (huc.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
         }
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
    
    @Override
    protected Void doInBackground(String... urls) {
    	String url_career = urls[0];
    	String url_heroes = urls[1];
    	if(!urlExists(url_career) || !urlExists(url_heroes)){
    		Error = "This profile doesn't exist";
    		return null;
    	}
    	
    	Career = getSource(url_career);
    	Heroes = getSource(url_heroes);
    	
    	String btagLine = StringUtils.substringBetween(Career, "Profile.baseUrl", ";");
    	String btag = StringUtils.substringBetween(btagLine, "profile/", "/");
    	String paragonSC = "-", paragonHC = "-";
    	if(Career.contains("kill-section paragon")){
        	String paragon = StringUtils.substringBetween(Career, "kill-section paragon", "clear");
        	if(paragon.contains("hardcore")){
        		paragonSC = StringUtils.substringBetween(paragon, "num-kills\">", " |");
        		paragonHC = StringUtils.substringBetween(paragon, "hardcore\">", "<");
        		System.out.println(paragonSC+" "+paragonHC);
        	}else{
        		paragonSC = StringUtils.substringBetween(paragon, "num-kills\">", "<");
        		System.out.println(paragonSC+" "+paragonHC);
        	}
    	}
    	Player newPlayer = new Player(btag, paragonSC, paragonHC);
    	database.addPlayer(newPlayer);
    	
    	String heroes = StringUtils.substringBetween(Heroes, "Profile.heroes", "]");
        String heroesIds [] = StringUtils.substringsBetween(heroes, "id: ", ",");
        String heroesNames [] = StringUtils.substringsBetween(heroes, "name: '", "',");
        String heroesLevels [] = StringUtils.substringsBetween(heroes, "level: ", ",");
        String heroesClass [] = StringUtils.substringsBetween(heroes, "'class': '", "',");
        
        int noOfHeroes = heroesIds.length;
        String heroesGender [] = new String[noOfHeroes];
        String heroesGameMode [] = new String[noOfHeroes];
        
        for(int i = 0; i < noOfHeroes; i++){
        	String info = StringUtils.substringBetween(Heroes, "hero-tab "+heroesClass[i]+"-", "\" href=\""+heroesIds[i]);
        	if(info.contains("\n")){
        		info = StringUtils.substringAfterLast(info, "hero-tab "+heroesClass[i]+"-");
        	}
        	if(info.contains("-active"))
        		info = info.replace("-active", "");
        	if(info.contains(" active"))
        		info = info.replace(" active", "");

        	if(info.contains("hardcore")){
        		String[] parts = info.split(" ");
        		heroesGender[i] = parts[0];
        		heroesGameMode[i] = parts[1];
        	}else{
        		heroesGender[i] = info;
        		heroesGameMode[i] = "softcore";
        	}
        	
        	Hero newHero = new Hero(heroesIds[i], heroesNames[i], heroesGender[i], heroesLevels[i], heroesClass[i], heroesGameMode[i], "false", btag);
        	database.addHero(newHero);
        	          	
        	System.out.println(info);
        }
    	
		return null;
    }
    
    @Override
    protected void onPostExecute(Void unused) {
        delegate.asyncComplete(true);
        Dialog.dismiss();
        if (Error != null) {
        	customToast(Error);
        } else {
            customToast("Done!");
        }
    }
}
