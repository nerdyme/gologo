package main.gologo.adapter;

import java.util.Comparator;

/**
 * Created by surbhi on 4/18/16.
 */
public class Phonecomparator  implements Comparator<Phonecontactdata> {

    @Override
    public int compare(Phonecontactdata emp1, Phonecontactdata emp2) {
        return emp1.getname().compareToIgnoreCase(emp2.getname());
    }
}