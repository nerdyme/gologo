package main.gologo.survey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import main.gologo.R;
import main.gologo.sendoptions.ContactOptions;

/**
 * Created by surbhi on 4/13/16.
 */
public class Surveyadapter extends RecyclerView.Adapter<Surveyadapter.PersonViewHolder>{

    ArrayList<Surveydata> al;
    Context ct;


    public Surveyadapter(ArrayList<Surveydata> al,Activity ct)
    {
       // super(ac,-1,al);
        //this.ct=ac;
        this.al=al;
        this.ct=ct;
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.surveyadapter, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, final int position) {
        holder.surveyName.setText(al.get(position).getname());
        holder.view.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ct, Viewsurveys.class);
                Bundle bundle=new Bundle();
                bundle.putString("ActivityName","ViewSurvey");
                bundle.putString("form_id",al.get(position).getformid());

                i.putExtras(bundle);
                ct.startActivity(i);
                //Toast.makeText(this,al.get(position).getformid(), Toast.LENGTH_LONG).show();
            }
        });

        holder.launch.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ct, ContactOptions.class);
                Bundle bundle=new Bundle();
                bundle.putString("ActivityName","LaunchSurvey");
                bundle.putString("form_id",al.get(position).getformid());
                bundle.putString("survey_name",al.get(position).getname());
                i.putExtras(bundle);
                ct.startActivity(i);
                //Toast.makeText(this,al.get(position).getformid(), Toast.LENGTH_LONG).show();
            }
        });
        //holder.personAge.setText(String.valueOf(persons.get(position).getformid()));
    }

    @Override
    public int getItemCount() {
        if (al != null) {
            return al.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView surveyName;
        LinearLayout view;
        LinearLayout launch;


        PersonViewHolder(View itemView) {
            super(itemView);
            this.cv = (CardView) itemView.findViewById(R.id.cv);
            this.surveyName = (TextView) itemView.findViewById(R.id.surveyname);
            this.view=(LinearLayout)itemView.findViewById(R.id.viewll);
            this.launch=(LinearLayout)itemView.findViewById(R.id.launchll);
        }
    }


   /* @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ct
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.surveyadapter, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.textView1);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(al.get(position).getname());
        return rowView;
    }*/
}
