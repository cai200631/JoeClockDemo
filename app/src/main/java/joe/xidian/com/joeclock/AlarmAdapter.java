package joe.xidian.com.joeclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JOE on 2016/6/21.
 */
public class AlarmAdapter extends BaseAdapter {
    private List<AlarmData> mAlarmDatas = new ArrayList<>();
    private LayoutInflater  mInflater;

    public AlarmAdapter(Context context, List<AlarmData> alarmDatas) {
        mAlarmDatas = alarmDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mAlarmDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlarmDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
