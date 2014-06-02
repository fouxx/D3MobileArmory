package fouxx.D3MobileArmory;

import java.io.IOException;

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
	private String careerString;
	private D3MobileArmorySQLiteHelper database;
	private AsyncDelegate delegate;
	
	public ProfileDownloader(Context context, AsyncDelegate delegate){
		this.context = context;
		this.Dialog  = new ProgressDialog(context);
		this.Error = null;
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
        Dialog.setMessage("Downloading profile info..");
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
    	
    	careerString = getSource(url_career);
    	
    	try {
            JSONObject career = new JSONObject(careerString);
        	if(!career.isNull("code") && career.getString("code").equals("NOTFOUND")){
        		Error = career.getString("reason");
        		return null;
        	}
            
            String btag = career.getString("battleTag");
            btag = btag.replace("#", "-");
            String paragonSC = career.getString("paragonLevel"); 
            String paragonHC = career.getString("paragonLevelHardcore"); 
        	Player newPlayer = new Player(btag, paragonSC, paragonHC);
        	Error = database.addPlayer(newPlayer);
        	if(!Error.equals(""))
        		return null;
            
            JSONArray heroes = career.getJSONArray("heroes");
            for(int i = 0; i < heroes.length(); i++){
            	JSONObject hero = heroes.getJSONObject(i);
            	
            	String heroID = hero.getString("id");
            	String heroName = hero.getString("name");
            	String heroLevel = hero.getString("level");
            	String heroClass = hero.getString("class");
            	String heroGender = hero.getString("gender");
            	if(heroGender.equals("1"))
            		heroGender = "female";
            	else
            		heroGender = "male";
            	String heroGameMode = hero.getString("hardcore");
            	if(heroGameMode.equals("true"))
            		heroGameMode = "Hardcore";
            	else
            		heroGameMode = "";
            	String heroDead = hero.getString("dead");
            	if(heroDead.equals("true"))
            		heroGameMode += " - Dead";
            	String heroParagon = hero.getString("paragonLevel");
            	
            	Hero newHero = new Hero(heroID, heroName, heroGender, heroLevel, heroClass, heroGameMode, "false", btag, heroParagon);
            	database.addHero(newHero);
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
