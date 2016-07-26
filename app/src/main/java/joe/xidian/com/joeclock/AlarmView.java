package joe.xidian.com.joeclock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by JOE on 2016/6/21.
 */
public class AlarmView extends LinearLayout {
    private static final String KEY_ALARM_LIST = "alarmList";
    private Button mAddBtn;
    private ListView mListView;
    private ArrayAdapter<AlarmData> adapter;
    private ArrayList<AlarmData> mList;
    private AlarmManager alarmManager;

    public AlarmView(Context context) {
        super(context);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAddBtn = (Button) findViewById(R.id.btn_add_alarm);
        mAddBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });
        mListView = (ListView) findViewById(R.id.lvAlarmlist);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext()).setTitle("操作选项").setItems(new CharSequence[]{
                        "删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                deleteAlarm(position);
                                break;
                            default:
                                break;
                        }

                    }
                }).setNegativeButton("取消",null).show();
                return true;
            }
        });
        mList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(adapter);
        readAlarmList();
    }

    private void deleteAlarm(int position) {
        mList.remove(position);
        adapter.notifyDataSetChanged();
        AlarmData data = mList.get(position);
        alarmManager.cancel(PendingIntent.getBroadcast(getContext(), data.getId(),
                new Intent(getContext(),AlarmReceiver.class),0));
        saveAlarmList();

    }

    public void addAlarm() {
        Calendar c = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND,0);
                        calendar.set(Calendar.MILLISECOND,0);
                        Calendar curTime = Calendar.getInstance();
                        if (calendar.getTimeInMillis() <= curTime.getTimeInMillis()) {
                            calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                        }
                        AlarmData data = new AlarmData(calendar.getTimeInMillis());
                        mList.add(data);
                        adapter.notifyDataSetChanged();
                        mListView.setSelection(mList.size() - 1);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                data.getTime(), 5 * 60 * 1000,
                                PendingIntent.getBroadcast(getContext(),
                                       data.getId(),
                                        new Intent(getContext(),AlarmReceiver.class),0));
                        saveAlarmList();
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void saveAlarmList() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(),
                Context.MODE_PRIVATE).edit();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mList.size(); i++) {
            sb.append(mList.get(i).getTime()).append(",");
        }
        if(sb.length() >1) {
            String content = sb.toString().substring(0, sb.length() - 1);
            editor.putString(KEY_ALARM_LIST, content);
        }else{
            editor.putString(KEY_ALARM_LIST,"");
        }
        editor.apply();
    }
    private void readAlarmList(){
        SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST,null);
        if(content != null){
            String[] timeString = content.split(",");
            for (String string : timeString) {
                mList.add(new AlarmData(Long.parseLong(string)));
            }
        }
    }

}
