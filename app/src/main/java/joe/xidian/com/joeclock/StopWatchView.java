package joe.xidian.com.joeclock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JOE on 2016/6/25.
 */
public class StopWatchView extends LinearLayout implements View.OnClickListener {
    private TextView mStopHour;
    private TextView mStopMin;
    private TextView mStopSec;
    private TextView mStopMilisec;
    private Button mBtnStart;
    private Button mBtnPause;
    private Button mBtnReset;
    private Button mBtnResume;
    private Button mBtnLap;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask = null;
    private int mTenSecs = 0;
    private TimerTask showTimeTask = null;
    private  Handler mHandler = new MyHandler(getContext(), this);
    private static final int MSG_SHOW_TIME = 1;


    public StopWatchView(Context context) {
        super(context);
    }

    public StopWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StopWatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mStopHour = (TextView) findViewById(R.id.stoper_hour);
        mStopHour.setHint("0");
        mStopMin = (TextView) findViewById(R.id.stoper_min);
        mStopMin.setHint("0");
        mStopMilisec = (TextView) findViewById(R.id.stoper_milisec);
        mStopMilisec.setHint("0");
        mStopSec = (TextView) findViewById(R.id.stoper_sec);
        mStopSec.setHint("0");

        mBtnStart = (Button) findViewById(R.id.SWstart_btn);
        mBtnStart.setOnClickListener(this);
        mBtnPause = (Button) findViewById(R.id.SWpause_btn);
        mBtnPause.setOnClickListener(this);
        mBtnReset = (Button) findViewById(R.id.SWreset_btn);
        mBtnReset.setOnClickListener(this);
        mBtnResume = (Button) findViewById(R.id.SWresume_btn);
        mBtnResume.setOnClickListener(this);
        mBtnLap = (Button) findViewById(R.id.SWlap_btn);
        mBtnLap.setOnClickListener(this);

        mBtnPause.setVisibility(View.GONE);
        mBtnReset.setVisibility(View.GONE);
        mBtnResume.setVisibility(View.GONE);
        mBtnLap.setVisibility(View.GONE);

        mListView = (ListView) findViewById(R.id.lvWatchTimer);
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);
        showTimeTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_SHOW_TIME);
            }
        };
        mTimer.scheduleAtFixedRate(showTimeTask, 200, 200);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SWstart_btn:
                startTimer();
                mBtnStart.setVisibility(View.GONE);
                mBtnPause.setVisibility(View.VISIBLE);
                mBtnLap.setVisibility(View.VISIBLE);
                break;
            case R.id.SWpause_btn:
                stopTimer();
                mBtnPause.setVisibility(View.GONE);
                mBtnResume.setVisibility(View.VISIBLE);
                mBtnLap.setVisibility(View.GONE);
                mBtnReset.setVisibility(View.VISIBLE);
                break;
            case R.id.SWreset_btn:
                stopTimer();
                mTenSecs = 0;
                mAdapter.clear();
                mBtnPause.setVisibility(View.GONE);
                mBtnReset.setVisibility(View.GONE);
                mBtnResume.setVisibility(View.GONE);
                mBtnLap.setVisibility(View.GONE);
                mBtnStart.setVisibility(View.VISIBLE);
                break;
            case R.id.SWresume_btn:
                startTimer();
                mBtnPause.setVisibility(View.VISIBLE);
                mBtnResume.setVisibility(View.GONE);
                mBtnReset.setVisibility(View.GONE);
                mBtnLap.setVisibility(View.VISIBLE);
                break;
            case R.id.SWlap_btn:
                mAdapter.insert(String.format("%d:%d:%d.%d", mTenSecs / 100 / 60 / 60,
                        mTenSecs / 100 / 60 % 60,mTenSecs / 100 % 60, mTenSecs % 100), 0);
                break;
            default:
                break;
        }
    }

    private void startTimer() {
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mTenSecs++;
                }
            };
            mTimer.scheduleAtFixedRate(mTimerTask, 10, 10);
        }
    }

    private void stopTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    public void onDestory() {
        mTimer.cancel();
    }

    private static class MyHandler extends Handler {
        private Context context;
        private WeakReference<StopWatchView> view;

        public MyHandler(Context context, StopWatchView view) {
            this.context = context;
            this.view = new WeakReference<>(view);
        }
        @Override
        public void handleMessage(Message msg) {
            StopWatchView stopView = view.get();
            switch (msg.what) {
                case MSG_SHOW_TIME:
                    stopView.mStopHour.setText(stopView.mTenSecs / 100 / 60 / 60 + "");
                    stopView.mStopMin.setText(stopView.mTenSecs / 100 / 60 % 60 + "");
                    stopView.mStopSec.setText(stopView.mTenSecs / 100 % 60 + "");
                    stopView.mStopMilisec.setText(stopView.mTenSecs % 100 + "");
                    break;
                default:
                    break;
            }
        }

    }
}

