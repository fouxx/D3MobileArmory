package fouxx.D3MobileArmory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ViewConstructor")
public class D3MAToast extends Toast {
	
	Typeface font;

	public D3MAToast(Context context, String text) {
		super(context);
		font = Typeface.createFromAsset(context.getAssets(),"fonts/DiabloLight.ttf");
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.d3ma_toast, (ViewGroup) ((Activity) context).findViewById(R.id.toast_layout));
        this.setView(view);
        TextView tv = (TextView) view.findViewById(R.id.toast_text);
        tv.setTypeface(font);
        tv.setText(text.toUpperCase());
	}
}
