package fouxx.D3MobileArmory;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayerListViewAdapter extends BaseAdapter {
	private Context context;
	private List<Player> playerList;
	
	PlayerListViewAdapter(Context context, List<Player> playerList){
		this.context = context;
		this.playerList = playerList;
	}
	

	@Override
	public int getCount() {
		return playerList.size();
	}

	@Override
	public Object getItem(int position) {
		return playerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return playerList.indexOf(getItem(position));
	}
	
	private class ViewHolder {
		TextView battleTag;
		TextView paragonSC;
		TextView paragonHC;
		TextView paragonName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		View v = convertView;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null){
		 	v = inflater.inflate(R.layout.player_listview_item, null);
		}
		holder = new ViewHolder();
		holder.battleTag = (TextView) v.findViewById(R.id.battleTag);
		holder.paragonSC = (TextView) v.findViewById(R.id.paragonSC);
		holder.paragonHC = (TextView) v.findViewById(R.id.paragonHC);
		holder.paragonName = (TextView) v.findViewById(R.id.paragonName);
		
		Player player = playerList.get(position);
		
		holder.paragonName.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/DiabloLight.ttf"));
		holder.battleTag.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/DiabloLight.ttf"));
		holder.battleTag.setText(StringUtils.upperCase(player.btag).replace("-", "\n#"));

		if(!player.paragonSC.equals("0")){
			holder.paragonSC.setText(player.paragonSC);
		}else
			holder.paragonSC.setText("");
		
		if(!player.paragonHC.equals("0")){
			holder.paragonHC.setText(player.paragonHC);
		}else
			holder.paragonHC.setText("");
		
		return v;
	}

}
