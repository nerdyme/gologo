package main.gologo.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import main.gologo.R;

/**
 * Created by surbhi on 4/18/16.
 */
public class Groupcontactlistadapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
{
    public SparseBooleanArray mCheckStates;
    LayoutInflater mInflater;
    TextView tv1;
    CheckBox cb;
    Activity cnt;
    ArrayList<Groupcontactdata> phonelist;


    public Groupcontactlistadapter(ArrayList<Groupcontactdata> phonelist, Activity cnt)
    {
        mCheckStates = new SparseBooleanArray(phonelist.size());
        this.phonelist=phonelist;
        this.cnt=cnt;
        mInflater = (LayoutInflater)cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return phonelist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi=convertView;
        if(convertView==null)
            vi = mInflater.inflate(R.layout.phonecontactadapter, null);
        tv1= (TextView) vi.findViewById(R.id.textView1);
        cb = (CheckBox) vi.findViewById(R.id.checkBox1);
        tv1.setText(phonelist.get(position).getgroupname());

        cb.setTag(position);
        cb.setChecked(mCheckStates.get(position, false));
        cb.setOnCheckedChangeListener(this);

        return vi;
    }
    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);
    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        // TODO Auto-generated method stub

        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
    }
}