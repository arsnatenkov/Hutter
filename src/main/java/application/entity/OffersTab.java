package application.entity;

import java.util.ArrayList;

public class OffersTab {
    static int call = 0;
    ArrayList<ArrayList<Offer>> lst;

    public OffersTab(ArrayList<ArrayList<Offer>> lst) {
        this.lst = lst;
    }

    public ArrayList<Offer> getList() {
        return lst.get(call++ % lst.size());
    }

    public ArrayList<Offer> getList(int index) {
        return lst.get(index);
    }
}
