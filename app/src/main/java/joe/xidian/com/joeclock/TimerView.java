package joe.xidian.com.joeclock;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JOE on 2016/6/22.
 */
public class TimerView extends LinearLayout {
    private static int allTimerCounter = 0;
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask = null;
    private static Button mBtnStart, mBtnPause, mBtnReset, mBtnResume;
    private static EditText mEditHour, mEditMin, mEditSec;
    private Handler mHandler = new Myhandler(this,getContext());
    private  static final int MSG_WHTA_TIME_IS_UP = 1;
    private  static final int MSG_WHTA_TIME_TICK = 2;

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                mBtnStart.setVisibility(View.GONE);
                mBtnPause.setVisibility(View.VISIBLE);
                mBtnReset.setVisibility(View.VISIBLE);
            }
        });
        mBtnReset = (Button) findViewById(R.id.btn_reset);
        mBtnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                mEditHour.setText("0");
                mEditMin.setText("0");
                mEditSec.setText("0");
                mBtnReset.setVisibility(View.GONE);
                mBtnResume.setVisibility(View.GONE);
                mBtnPause.setVisibility(View.GONE);
                mBtnStart.setVisibility(View.VISIBLE);
            }
        });
        mBtnPause = (Button) findViewById(R.id.btn_pause);
        mBtnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                mBtnPause.setVisibility(View.GONE);
                mBtnResume.setVisibility(View.VISIBLE);
            }
        });
        mBtnResume = (Button) findViewById(R.id.btn_resume);
        mBtnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                mBtnResume.setVisibility(View.GONE);
                mBtnPause.setVisibility(View.VISIBLE);
            }
        });

        mEditHour = (EditText) findViewById(R.id.edit_hour);
        mEditMin = (EditText) findViewById(R.id.edit_min);
        mEditSec = (EditText) findViewById(R.id.edit_sec);
        mEditHour.setHint("00");
        mEditHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());
                    if (value > 59) {
                        mEditMin.setText("59");
                    } else if (value < 0) {
                        mEditMin.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditSec.setHint("00");
        mEditSec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());
                    if (value > 59) {
                        mEditMin.setText("59");
                    } else if (value < 0) {
                        mEditMin.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditMin.setHint("00");
        mEditMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int value = Integer.parseInt(s.toString());
                    if (value > 59) {
                        mEditMin.setText("59");
                    } else if (value < 0) {
                        mEditMin.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBtnStart.setVisibility(View.VISIBLE);
        mBtnStart.setEnabled(false);
        mBtnReset.setVisibility(View.GONE);
        mBtnResume.setVisibility(View.GONE);
        mBtnPause.setVisibility(View.GONE);
    }

    private void startTimer() {
        allTimerCounter = Integer.parseInt(mEditHour.getText().toString()) * 60 * 60 + Integer.parseInt(mEditMin.getText().toString()) * 60 +
                Integer.parseInt(mEditSec.getText().toString());
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    allTimerCounter--;

                    mHandler.sendEmptyMessage(MSG_WHTA_TIME_TICK);
                    if(allTimerCounter <= 0){
                        mHandler.sendEmptyMessage(MSG_WHTA_TIME_IS_UP);
                        stopTimer();
                    }
                }
            };
            mTimer.schedule(mTimerTask, 1000, 1000);

        }
    }

    private void stopTimer() {
        if(mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void checkToEnableBtnStart() {
        mBtnStart.setEnabled((!TextUtils.isEmpty(mEditHour.getText().toString()) && Integer.parseInt(mEditHour.getText().toString()) > 0) ||
                (!TextUtils.isEmpty(mEditSec.getText().toString()) && Integer.parseInt(mEditSec.getText().toString()) > 0) ||
                (!TextUtils.isEmpty(mEditMin.getText().toString()) && Integer.parseInt(mEditMin.getText().toString()) > 0));

    }


    private static class Myhandler extends  Handler {
        private final WeakReference<TimerView> mTimerView;
        private Context context;

        public Myhandler(TimerView timerView, Context context) {
            mTimerView = new WeakReference<TimerView>(timerView);
            this.context = context;
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHTA_TIME_IS_UP:
                    new AlertDialog.Builder(context).setTitle("Time Up!").setMessage("Time is up").
                            setNegativeButton("Cancel", null).show();
                    mBtnReset.setVisibility(View.GONE);
                    mBtnResume.setVisibility(View.GONE);
                    mBtnPause.setVisibility(View.GONE);
                    mBtnStart.setVisibility(View.VISIBLE);
                    break;
                case MSG_WHTA_TIME_TICK:
                    int hour = allTimerCounter/60/60;
                    int min = (allTimerCounter/60)%60;
                    int sec = allTimerCounter%60;
                    mEditHour.setText(hour+"");
                    mEditMin.setText(min+"");
                    mEditSec.setText(sec+"");
                    break;
                default:
                    break;
            }
        }
    }
}
