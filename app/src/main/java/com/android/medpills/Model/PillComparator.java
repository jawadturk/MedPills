package com.android.medpills.Model;

import java.util.Comparator;

public class PillComparator implements Comparator<Pill> {

    @Override
    public int compare(Pill firstPill, Pill secondPill){

        String firstPillName = firstPill.getPillName();
        String secondPillName = secondPill.getPillName();
        return firstPillName.compareTo(secondPillName);
    }
}
