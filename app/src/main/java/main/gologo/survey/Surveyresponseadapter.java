package main.gologo.survey;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import main.gologo.R;

/**
 * Created by surbhi on 6/11/16.
 */
public class Surveyresponseadapter extends RecyclerView.Adapter<Surveyresponseadapter.PersonViewHolder2>{

    ArrayList<Viewsurveydata> al;
    Context ct;


    public Surveyresponseadapter(ArrayList<Viewsurveydata> al,Activity ct)
    {
        this.al=al;
        this.ct=ct;
    }


    @Override
    public PersonViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.surveyresponseadapter, parent, false);
        PersonViewHolder2 pvh = new PersonViewHolder2(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder2 holder, final int position) {
        holder.surveyquestionno.setText(Integer.toString(al.get(position).getquestionno()));
        holder.surveyquestion.setText(al.get(position).getquestion());
        holder.surveyresponse.setText("Number of Responses Recorded : " +Integer.toString(al.get(position).getresponses()));
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

    public static class PersonViewHolder2 extends RecyclerView.ViewHolder {
        CardView cv;
        TextView surveyquestion;
        TextView surveyquestionno;
        TextView surveyresponse;

        PersonViewHolder2(View itemView) {
            super(itemView);
            this.cv = (CardView) itemView.findViewById(R.id.cv);
            this.surveyquestion = (TextView) itemView.findViewById(R.id.surveyquestion);
            this.surveyquestionno = (TextView) itemView.findViewById(R.id.surveyquestionno);
            this.surveyresponse=(TextView) itemView.findViewById(R.id.surveyresponse);
        }
    }
}
