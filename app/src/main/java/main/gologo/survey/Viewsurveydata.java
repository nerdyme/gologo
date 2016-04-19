package main.gologo.survey;

/**
 * Created by surbhi on 4/16/16.
 */
public class Viewsurveydata {

    String question;
    int no;

    Viewsurveydata(String str1,int no)
    {
        this.question=str1;
        this.no=no;
    }

    String getquestion()
    {
        return this.question;
    }

    int getquestionno()
    {
        return this.no;
    }
}
