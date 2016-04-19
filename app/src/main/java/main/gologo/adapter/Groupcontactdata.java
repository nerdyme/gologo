package main.gologo.adapter;

/**
 * Created by surbhi on 4/18/16.
 */
public class Groupcontactdata {

        String name;
        int id;

        public Groupcontactdata(String name,  int id)
        {
            this.name=name;
            this.id=id;
        }

        public String getgroupname()
        {
            return this.name;
        }

        public int getgroupid()
        {
            return this.id;
        }
    }


