package main.gologo.adapter;

import java.util.Comparator;

/**
 * Created by surbhi on 4/19/16.
 */
public class Groupcomparator  implements Comparator<Groupcontactdata> {

    @Override
    public int compare(Groupcontactdata emp1, Groupcontactdata emp2) {
        return emp1.getgroupname().compareToIgnoreCase(emp2.getgroupname());
    }
}
