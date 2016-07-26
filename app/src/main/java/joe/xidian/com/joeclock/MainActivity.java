package joe.xidian.com.joeclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {
    private TabHost mTabHost;
    private StopWatchView mStopWatchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTabHost= (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();
        mTabHost.addTab(mTabHost.newTabSpec("tabTime").setIndicator("时钟").setContent(R.id.tabTime));
        mTabHost.addTab(mTabHost.newTabSpec("tabAlarm").setIndicator("闹钟").setContent(R.id.tabAlarm));
        mTabHost.addTab(mTabHost.newTabSpec("tabTimer").setIndicator("计时器").setContent(R.id.tabTimer));
        mTabHost.addTab(mTabHost.newTabSpec("tabStoper").setIndicator("秒表").setContent(R.id.tabStoper));
        mStopWatchView = (StopWatchView) findViewById(R.id.tabStoper);

    }

    @Override
    protected void onDestroy() {
        mStopWatchView.onDestory();
        super.onDestroy();
    }
}
