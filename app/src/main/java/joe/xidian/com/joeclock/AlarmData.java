package joe.xidian.com.joeclock;

import java.util.Calendar;

/**
 * Created by JOE on 2016/6/21.
 */
public class AlarmData {
    private long time = 0;
    private String timeLabel = "";
    private Calendar mCalendar;

    public AlarmData(long time) {
        this.time = time;
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        timeLabel = String.format("%d月%d日%d:%d",mCalendar.get(Calendar.MONTH )+1,mCalendar.get(Calendar.DAY_OF_MONTH),
                mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE));
    }

    public long getTime() {
        return time;
    }

    public int getId(){
        return (int)(getTime()/1000/60);
    }

    public String getTimeLabel() {
        return timeLabel;
    }

    @Override
    public String toString() {
        return getTimeLabel();
    }
}

