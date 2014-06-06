package fouxx.D3MobileArmory;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;

public class TestDialog extends Dialog {
	
	Context context;

	public TestDialog(Context context) {
		super(context, R.style.customItemDialog);
		this.context = context;
	}
	
	private float mDownX;
	private float mDownY;
	private final float SCROLL_THRESHOLD = 10;
	private boolean isOnClick;
	
	@Override
	public boolean dispatchTouchEvent (MotionEvent ev) {
	  return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    switch (ev.getAction() & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_DOWN:
	            mDownX = ev.getX();
	            mDownY = ev.getY();
	            isOnClick = true;
                new D3MAToast(context, "down");
	            break;
	        case MotionEvent.ACTION_CANCEL:
	        case MotionEvent.ACTION_UP:
	            if (isOnClick) {
	                this.dismiss();
	            }
	            break;
	        case MotionEvent.ACTION_MOVE:
	            if (isOnClick && (Math.abs(mDownX - ev.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - ev.getY()) > SCROLL_THRESHOLD)) {
	                isOnClick = false;
	            }
	            break;
	        default:
	            break;
	    }
	    return true;
	}

}
