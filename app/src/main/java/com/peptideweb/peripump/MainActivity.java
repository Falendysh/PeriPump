package com.peptideweb.peripump;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    public LinearLayout pumpsLayout = null; // Container for all pumpFragments
    public static Integer PQ = 4;
    PumpContainer pumpFragmentContainer;

    public static final  String[] PUMP_INDEX = {"A","B","C","D"};

    public static boolean detectorIsOn = true;

    View container = null;

    public Button detectorStartButton;
    public Button detectorStopButton;
    public Button detectorClearButton;
    public TextView detectorTextView;
    public TextView detectorHeadView;
    public LinearLayout detectorLayout;

    public LinearLayout masterControlLayout;
    public TextView masterControlTextView;
    public Button masterControlStartButton;
    public Button masterControlStopButton;
    public Button masterControlDetectorButton;
    public Button masterControlMETButton;

    ArrayList<PumpFragment> pumpFragments = new ArrayList<PumpFragment>();

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    void RebuildPumpContainer(){ // when user changes the number of pumps the GUI should be rebuilt

        fragmentTransaction = fragmentManager.beginTransaction();

        for (int i = 0; i < pumpFragments.size(); i++){
            fragmentTransaction.remove(pumpFragments.get(i));
        }

        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();

        pumpsLayout.removeAllViews();


        pumpFragmentContainer = PumpContainerGenerator.GenerateContainer(PQ);

        container = View.inflate(getApplicationContext(),pumpFragmentContainer.pumpContainerViewID,pumpsLayout);


        int lid = pumpFragmentContainer.fragmentLayoutID;


        for (int i = 0; i < PQ; i++){

            PumpFragment pumpFragment = PumpFragment.newInstance(String.valueOf(i),PUMP_INDEX[i],lid);

            fragmentTransaction.replace(pumpFragmentContainer.containerIDList.get(i), pumpFragment, String.valueOf(i));
            pumpFragments.add(i, pumpFragment);
        }

        fragmentTransaction.commit();



        int detectorVisibility = detectorIsOn ? View.VISIBLE: View.GONE;
        detectorLayout.setVisibility(detectorVisibility);

        detectorClearButton.setActivated(detectorIsOn);
        detectorStartButton.setActivated(detectorIsOn);
        detectorStopButton.setActivated(detectorIsOn);
        detectorTextView.setActivated(detectorIsOn);
        String detectorMessage = detectorIsOn ? "Detector curve here":"Detector is OFF. \n No image available";
        detectorTextView.setText(detectorMessage);
        detectorHeadView.setActivated(detectorIsOn);

        boolean mca = PQ==1 ? false:true;
        masterControlLayout.setActivated(mca);
        int visible = PQ==1 ? View.GONE : View.VISIBLE ;
        masterControlLayout.setVisibility(visible);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pumpsLayout = (LinearLayout) findViewById(R.id.pumpsLayout);

        detectorTextView = (TextView) findViewById(R.id.detector_text_view);
        detectorHeadView = (TextView) findViewById(R.id.detector_head_view);
        detectorStopButton = (Button) findViewById(R.id.detector_stop_button);
        detectorStartButton = (Button) findViewById(R.id.detector_start_button);
        detectorClearButton = (Button) findViewById(R.id.detector_clear_button);
        detectorLayout = (LinearLayout) findViewById(R.id.detector_linear_layout);

        masterControlLayout = (LinearLayout) findViewById(R.id.master_control_layout);
        masterControlDetectorButton = (Button) findViewById(R.id.master_control_detector_button);
        masterControlMETButton = (Button) findViewById(R.id.master_control_met_button);
        masterControlStartButton = (Button) findViewById(R.id.master_control_start_button);
        masterControlStopButton = (Button) findViewById(R.id.master_control_stop_button);
        masterControlTextView = (TextView) findViewById(R.id.master_control_text_view);

        masterControlMETButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"general MET is working",Toast.LENGTH_SHORT).show();
            }
        });



        fragmentManager = getFragmentManager();


        TextView starTextView = (TextView) findViewById(R.id.star_text);
        starTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder pumpCounterDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                pumpCounterDialogBuilder.setTitle("Pump Quantity");
                pumpCounterDialogBuilder.setMessage("Select pump quantity");

                final Integer[] pq = {1, 2, 3, 4};


                ArrayAdapter<Integer> pqListAdapter = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_list_item_single_choice);

                pqListAdapter.addAll(pq);

                View dialogView = View.inflate(getApplicationContext(),R.layout.dialog_layout,null);

                ListView dialogListView = (ListView) dialogView.findViewById(R.id.dialog_list_view);
                dialogListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialogListView.setLayoutParams(lp);

                dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        PQ = pq[i];
                    }
                });

                dialogListView.setAdapter(pqListAdapter);

                pumpCounterDialogBuilder.setView(dialogView);

                final Button dialogButton = (Button) dialogView.findViewById(R.id.dialog_button);

                String detectorOnOffButtonText = detectorIsOn ? "DETECTOR ON" : "DETECTOR OFF";
                dialogButton.setText(detectorOnOffButtonText);

                 dialogButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         detectorIsOn = !detectorIsOn;
                         String detectorOnOffButtonText = detectorIsOn ? "DETECTOR ON" : "DETECTOR OFF";
                         dialogButton.setText(detectorOnOffButtonText);
                     }
                 });


                pumpCounterDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), PQ.toString(), Toast.LENGTH_SHORT).show();
                        RebuildPumpContainer();
                        dialogInterface.dismiss();

                    }
                });

                pumpCounterDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog pumpCounterDialog = pumpCounterDialogBuilder.create();


                pumpCounterDialog.show();





            }
        });

    }








    @Override
    protected void onStart() {

        RebuildPumpContainer();

        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
