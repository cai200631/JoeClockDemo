package joe.xidian.com.joeclock;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by JOE on 2016/6/22.
 */
public class PlayAlarmActivity extends Activity {
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        mPlayer = MediaPlayer.create(this,R.raw.life);
        mPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        mPlayer.release();
    }
}
