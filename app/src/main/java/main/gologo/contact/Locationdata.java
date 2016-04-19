package main.gologo.contact;

/**
 * Created by surbhi on 4/17/16.
 */
public class Locationdata {

    String location;
    String locationURL;

    public Locationdata(String s1, String s2)
    {
        this.location=s1;
        this.locationURL=s2;
    }
    public String getlocation()
    {
        return this.location;
    }

    public String getlocationURL()
    {
        return this.locationURL;
    }
}
