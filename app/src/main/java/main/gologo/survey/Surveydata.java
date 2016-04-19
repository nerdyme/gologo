package main.gologo.survey;

/**
 * Created by surbhi on 4/13/16.
 */
public class Surveydata {

    String surveyname;
    String surveyid;
    String formid;

    Surveydata(String a, String b , String c)
    {
        surveyname=a;
        surveyid=b;
        formid=c;
    }

    String getname()
    {
        return this.surveyname;
    }

    String getsurveyid()
    {
        return this.surveyid;
    }

    String getformid()
    {
        return this.formid;
    }
}
