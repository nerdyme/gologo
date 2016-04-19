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

    public static String baseurl="http://52.25.169.219:3000/";

    public static String login=baseurl+"login";
    public static String pinforget=baseurl+"getpin";
    public static String creategroup=baseurl+"contactlist";
    public static String fetch_survey= baseurl +"surveys";
    public static String launch_survey=baseurl+"launchsurvey";
    public static String createcontact=baseurl+"contact";
    public static String launchmessage=baseurl+"message";
    public static String recording=baseurl+"recording";


    public static String view_survey1="http://internal.gramvaani.org:8080/vapp/api/v1/form_question/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json&form_id=";
    public static String location1="http://internal.gramvaani.org:8080/vapp/api/v1/location_location/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json&ai_id=10";
    public static String contact_groups1="http://internal.gramvaani.org:8080/vapp/api/v1/callerinfo_contact_list/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json";

    public static ArrayList<Locationdata> locationlist=null;
    public static ArrayList<Groupcontactdata> grouplist=null;
}

