package main.gologo.survey;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by surbhi on 4/16/16.
 */
public class Viewsurveydata implements Parcelable{

    String question;
    int no;
    int ques_id;
    int responses;

    Viewsurveydata(String str1,int no,int id, int responses)
    {
        this.question=str1;
        this.no=no;
        this.ques_id=id;
        this.responses=responses;
    }

    String getquestion()
    {
        return this.question;
    }

    int getquestionno()
    {
        return this.no;
    }

    int getresponses() { return this.responses; }

    int getquestionid() { return this.ques_id; }

    public Viewsurveydata(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Viewsurveydata> CREATOR = new Parcelable.Creator<Viewsurveydata>() {
        public Viewsurveydata createFromParcel(Parcel in) {
            return new Viewsurveydata(in);
        }

        public Viewsurveydata[] newArray(int size) {

            return new Viewsurveydata[size];
        }

    };

    public void readFromParcel(Parcel in) {
        no = in.readInt();
        ques_id = in.readInt();
        responses = in.readInt();
        question = in.readString();

    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(no);
        dest.writeInt(ques_id);
        dest.writeInt(responses);
        dest.writeString(question);
    }

    public void setresponses(int r)
    {
        this.responses=r;
    }
}
