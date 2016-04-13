package com.peptideweb.peripump;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by Falendysh on 06.11.2015.
 */
public class PumpContainer {
    public Integer pumpContainerViewID;
    public ArrayList<Integer> containerIDList;
    public Integer fragmentLayoutID;

    public PumpContainer(){

        this.containerIDList =  new ArrayList<Integer>();
        this.fragmentLayoutID = null;
        this.pumpContainerViewID = null;

    }


}
