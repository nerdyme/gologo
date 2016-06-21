package main.gologo.constants;

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

    // Admin login phone : 9718658816,,,pin :982545
    //phone :: 8826533273  pin :239644
    //ictd username ictdlab--password
    //Amazon credentials :: Ip address : http://52.25.169.219/ username: myvoice password : surbhi

    //To run logs on the rails :: tail -f log/development.log

   public static String baseurl="http://10.192.42.220:3000/";
    //public static String baseurl="http://10.237.27.182:3000/";
    //public static String baseurl="http://52.25.169.219:3000/";

    //path to create audio file : /storage/emulated/0/AudioRecorder/1463512098402.mp3


    public static String login=baseurl+"login"; //done
    public static String pinforget=baseurl+"getpin"; //done
    public static String creategroup=baseurl+"contactlist"; //done  /// for creating and getting both
    public static String launch_survey=baseurl+"launchsurvey"; //done
    public static String createcontact=baseurl+"contact";//done
    public static String launchmessage=baseurl+"message";//done 
    public static String recording=baseurl+"recording"; //done
    public static String get_survey=baseurl+"getsurveys/?ai_id="; //done
    public static String location=baseurl+"getlocations"; //done
    public static String get_survey_questions=baseurl+"getquestions"; //done
    public static String getadmininfo=baseurl+"getadmininfo";
    public static String get_survey_responses=baseurl + "getsurveyresponses?ai_id=60&survey_id=";

    public static String view_survey1="http://internal.gramvaani.org:8080/vapp/api/v1/form_question/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json&form_id=";
    public static String location1="http://internal.gramvaani.org:8080/vapp/api/v1/location_location/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json&ai_id=10";
    public static String contact_groups1="http://internal.gramvaani.org:8080/vapp/api/v1/callerinfo_contact_list/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json";
    public static String responses1="http://internal.gramvaani.org:8081/vapp/api/v1/survey_record/cdr_records/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json&ai_id=60&limit=20&page=1&survey_id=";

    public static String contact_grp1= "http://internal.gramvaani.org:8081/vapp/api/v1/callerinfo_contact_list/?api_key=37ddf510e72085ef218b150ad897675faec1f683&username=surbhi&format=json";



    public static int timeout=20000;
    public static int retrypolicy=1;
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

    public static boolean isValidDate(String dateString) {

        if (dateString == null || dateString.length() != "yyyy-MM-dd".length()) {
            return false;
        }
        String dateString1=removeCharAt(dateString,4);
        String dateString2=removeCharAt(dateString1,6);
        int date;
        try {
            date = Integer.parseInt(dateString2);
        } catch (NumberFormatException e) {
            return false;
        }

        int year = date / 10000;
        int month = (date % 10000) / 100;
        int day = date % 100;

        // leap years calculation not valid before 1581
        boolean yearOk = (year >= 1581) && (year <= 2500);
        boolean monthOk = (month >= 1) && (month <= 12);
        boolean dayOk = (day >= 1) && (day <= daysInMonth(year, month));

        return (yearOk && monthOk && dayOk);
    }

    private static int daysInMonth(int year, int month) {
        int daysInMonth;
        switch (month) {
            case 1: // fall through
            case 3: // fall through
            case 5: // fall through
            case 7: // fall through
            case 8: // fall through
            case 10: // fall through
            case 12:
                daysInMonth = 31;
                break;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    daysInMonth = 29;
                } else {
                    daysInMonth = 28;
                }
                break;
            default:
                // returns 30 even for nonexistant months
                daysInMonth = 30;
        }
        return daysInMonth;
    }

    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }
}/* GCM Message
GcmIntentService: Received: Bundle[{from=731395736136, message=Server Down!,
 android.support.content.wakelockid=4, collapse_key=updated_score}] */

/// For app on number 9891127941 ----Pin -- 333997
/*Pin : 555913
Kapil Thakkar*/

//survey dial out



//814434 -- pin kapil  7838168162
//368866 --- pin megha  9818041994

///Pin for 9718658816 ----  975424

// Pin for 9891127941 --- 123456

// Preeti Pin : 469991 No. 8826533273

//Received: Bundle[{extras=, code=4, from=731395736136, message=Message launched successfully!,
//android.support.content.wakelockid=1, collapse_key=do_not_collapse}]

//Received: Bundle[{extras=Demo Survey_survey - 27 - 70, code=1, from=731395736136,
//message=NGO Action India has launched a survey named Demo Survey_survey - 27 - 70 at location JH - Bokaro - Bermo, android.support.content.wakelockid=1, collapse_key=do_not_collapse}]