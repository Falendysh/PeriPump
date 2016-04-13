package com.peptideweb.peripump;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Falendysh on 06.11.2015.
 */
public class PumpContainerGenerator {

    public static PumpContainer pumpContainer;


    public static PumpContainer GenerateContainer(int pq){


        pumpContainer = new PumpContainer();
        switch (pq){

            case 1:{
                pumpContainer.pumpContainerViewID = R.layout.fragment_container_1;
                pumpContainer.fragmentLayoutID = R.layout.fragment_pump_1;
                pumpContainer.containerIDList.add(R.id.pumpItemLayout11);
                break;
            }
            case 2:{
                pumpContainer.pumpContainerViewID = R.layout.fragment_container_2;
                pumpContainer.fragmentLayoutID = R.layout.fragment_pump_2;
                pumpContainer.containerIDList.add(R.id.pumpItemLayout11);
                pumpContainer.containerIDList.add(R.id.pumpItemLayout12);
                break;
            }
            case 3:{
                pumpContainer.pumpContainerViewID = R.layout.fragment_container_4;
                pumpContainer.fragmentLayoutID = R.layout.fragment_pump;
                pumpContainer.containerIDList.add(R.id.pumpItemLayout11);
                pumpContainer.containerIDList.add(R.id.pumpItemLayout12);
                pumpContainer.containerIDList.add(R.id.pumpItemLayout21);
                break;
            }
            case 4:{
                pumpContainer.pumpContainerViewID = R.layout.fragment_container_4;
                pumpContainer.fragmentLayoutID = R.layout.fragment_pump;
                pumpContainer.containerIDList.add(R.id.pumpItemLayout11);
                pumpContainer.containerIDList.add(R.id.pumpItemLayout12);
                pumpContainer.containerIDList.add(R.id.pumpItemLayout21);
                pumpContainer.containerIDList.add(R.id.pumpItemLayout22);
                break;
            }
        }
        return pumpContainer;
    }
}
