package joe.xidian.com.joeclock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * Created by JOE on 2016/6/21.
 */
public class TimeView extends LinearLayout {
    private  TextView mTextView;
    private  MyHandler mHandler = new MyHandler(this);

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextView = (TextView) findViewById(R.id.tvTime);
        mTextView.setText("Hello,Time");
        mHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility == View.VISIBLE){
            mHandler.sendEmptyMessage(0);
        }else{
            mHandler.removeMessages(0);
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<TimeView> mTimeView;
        private TimeView view;

        private MyHandler(TimeView timeView) {
            mTimeView = new WeakReference<>(timeView);
            view = mTimeView.get();
        }

        @Override
        public void handleMessage(Message msg) {
            refreshTime();
            if(view.mTextView.getVisibility() == View.VISIBLE){
                sendEmptyMessageDelayed(0,1000);
            }
        }

        private void refreshTime() {
            Calendar c = Calendar.getInstance();
            view.mTextView.setText(String.format("%d:%d:%d",c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),
                    c.get(Calendar.SECOND)));
        }
    }
}
