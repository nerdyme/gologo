package main.gologo.constants;

import java.util.ArrayList;

import main.gologo.adapter.Groupcontactdata;
import main.gologo.contact.Locationdata;

/**
 * Created by surbhi on 4/1/16.
 */
public class Constants {

    public static String gcmRegId="";
    public static int appversion=0;
    public static String senderid=   "731395736136";
    public static String Projectname ="gramvaani";
    public static String ProjectID =  "gramvaani-1238";
    public static String ServerAPIkey="AIzaSyCIZaRw4k2Kzu5u-CBdxpzK4b4aTebK4yc";
    public static String phone="";

    //Gramvaani GUI password : surbhi, surbhi@123 -- URL - http://internal.gramvaani.org:8080/vapp/ang/#/
    //API key preeti :: username=preeti & api_key=38dc9ea2dc8878b8ac674b3baef02d1973de5362
    //public static String baseurl="http://10.207.165.68/";
    // Admin login phone : 9718658816,,,pin :982545
    //phone :: 8826533273  pin :239644
    //ictd username ictdlab--password
    //Amazon credentials :: Ip address : http://52.25.169.219/ username: myvoice password : surbhi

    //To run logs on the rails :: tail -f log/development.log

    //public static String baseurl="http://10.237.27.182:3000/";
    //public static String baseurl="http://10.237.27.166:3000/";
    public static String baseurl="http://52.25.169.219:3000/";
   // public static String baseurl="http://10.192.15.52:3000/";
    //public static String baseurl="http://10.192.50.143:3000/";
    //path to create audio file : /storage/emulated/0/AudioRecorder/1463512098402.mp3


    public static String login=baseurl+"login"; //done
    public static String pinforget=baseurl+"getpin"; //done
    public static String creategroup=baseurl+"contactlist"; //done
    public static String launch_survey=baseurl+"launchsurvey"; //done
    public static String createcontact=baseurl+"contact";//done
    public static String launchmessage=baseurl+"message";//done partial
    public static String recording=baseurl+"recording"; //done
    public static String get_survey=baseurl+"getsurveys"; //done
    public static String location=baseurl+"getlocations";
    public static String get_survey_questions=baseurl+"getquestions"; //done
    public static String getadmininfo=baseurl+"getadmininfo";


    public static String view_survey1="http://internal.gramvaani.org:8080/vapp/api/v1/form_question/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json&form_id=";
    public static String location1="http://internal.gramvaani.org:8080/vapp/api/v1/location_location/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json&ai_id=10";
    public static String contact_groups1="http://internal.gramvaani.org:8080/vapp/api/v1/callerinfo_contact_list/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json";

    public static ArrayList<Locationdata> locationlist=null;
    public static ArrayList<Groupcontactdata> grouplist=null;

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars).trim();
    }


}


