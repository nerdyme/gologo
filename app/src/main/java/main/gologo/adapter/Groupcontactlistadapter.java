package main.gologo.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import main.gologo.R;

/**
 * Created by surbhi on 4/18/16.
 */
public class Groupcontactlistadapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener, Filterable
{
    public SparseBooleanArray mCheckStates;
    LayoutInflater mInflater;
    TextView tv1;
    CheckBox cb;
    Activity cnt;
    ArrayList<String> Filtered_Names,Names,Original_Names;
    ArrayList<Groupcontactdata> phonelist;
    filter_here filter;

    public Groupcontactlistadapter(ArrayList<Groupcontactdata> phonelist, Activity cnt)
    {
        mCheckStates = new SparseBooleanArray(phonelist.size());
        this.phonelist=phonelist;
        this.cnt=cnt;
        mInflater = (LayoutInflater)cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Original_Names=new ArrayList<>();
        Names=new ArrayList<>();
        Filtered_Names=new ArrayList<>();
        for (int i=0;i<phonelist.size();++i)
        {
            Original_Names.add(phonelist.get(i).getgroupname());
        }
        filter = new filter_here();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Names.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return Names.get(position);
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
        tv1.setText(Names.get(position));

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

    public class filter_here extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub


            FilterResults Result = new FilterResults();
            // if constraint is empty return the original names
            if(constraint.length() == 0 ){

                Result.values = Original_Names;
                Result.count = Original_Names.size();
                return Result;
            }


            String filterString = constraint.toString().toLowerCase();
            String filterableString;
            Filtered_Names.clear();
            for(int i = 0; i<Original_Names.size(); i++){
                filterableString = Original_Names.get(i);
                if(filterableString.toLowerCase().contains(filterString)){
                    Filtered_Names.add(filterableString);
                }
            }
            Result.values = Filtered_Names;
            Result.count = Filtered_Names.size();
            Log.d("size of filtered", "Size is :: " + Filtered_Names.size());
            return Result;
        }

        @Override
        protected void publishResults(CharSequence constraint,Filter.FilterResults results) {
            // TODO Auto-generated method stub
            Names = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        return filter;
    }
}