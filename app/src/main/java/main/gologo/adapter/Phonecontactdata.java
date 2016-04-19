package main.gologo.adapter;

/**
 * Created by surbhi on 4/18/16.
 */
public class Phonecontactdata {

    String name,phone;

    public Phonecontactdata(String name, String phone)
    {
        this.name=name;
        this.phone=phone;
    }

    public String getname()
    {
        return this.name;
    }

    public String getPhone()
    {
        return this.phone;
    }
}
