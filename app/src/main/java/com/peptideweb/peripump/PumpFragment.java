package com.peptideweb.peripump;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.DialogPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PumpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PumpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PumpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LAYOUT_ID = "LAYOUT_ID";
    private static final boolean MODE_SPEED = false;
    private static final boolean MODE_FLOW = true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int layoutID;
    public boolean rotationDirection = true; // true - clockwise, false - counterclockwise
    public boolean speedDisplayMode = MODE_FLOW;


    public Tubing currentPumpTubing = Tubing.TUBING_16x48x16;
    public Double currentSpeed = 0.0;
    public Double currentFlow = 0.0;

    TextView pumpIndexTextView = null;
    TextView pumpTextView = null;
    ProgressBar pumpProgressBar = null;

    Tubing dialogSelectedTubing = Tubing.TUBING_16x48x16;




    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PumpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PumpFragment newInstance(String param1, String param2, int layoutID) {
        PumpFragment fragment = new PumpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(LAYOUT_ID,layoutID);
        fragment.setArguments(args);
        return fragment;
    }

    public PumpFragment() {
        // Required empty public constructor
    }

    void displayFlowInformation(){

        String flowText;
        String flowTextUnits;


        if (speedDisplayMode == MODE_FLOW)
        {
            currentSpeed = currentFlow / currentPumpTubing.getTubeProductivity();
        }
        else
        {
            currentFlow = currentSpeed * currentPumpTubing.getTubeProductivity();
        }

        if (currentSpeed < 0)  {currentSpeed = 0.0;} // speed check-out
        if (currentSpeed > 600){currentSpeed = 600.0;}

        currentFlow = currentSpeed*currentPumpTubing.getTubeProductivity(); // recalculate flow with checked speed value

        flowText = speedDisplayMode == MODE_FLOW ? String.format("%.3f",currentFlow) : String.format("%.3f",currentSpeed);
        flowTextUnits = speedDisplayMode==MODE_FLOW ? "\n ml/min": "\n RPM";

        pumpTextView.setText(flowText + flowTextUnits);
        pumpProgressBar.setProgress(currentSpeed.intValue());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            this.layoutID = getArguments().getInt(LAYOUT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(layoutID, container, false);

        pumpIndexTextView = (TextView) view.findViewById(R.id.pump_index_view);
        pumpIndexTextView.setText("PUMP " + mParam2);

        pumpTextView = (TextView) view.findViewById(R.id.pump_text_view);

        pumpProgressBar = (ProgressBar) view.findViewById(R.id.pump_progressbar);
        pumpProgressBar.setMax(600);

        final Button btnFlow = (Button) view.findViewById(R.id.button_flow_fragment);
         btnFlow.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 speedDisplayMode = !speedDisplayMode;

                 String btnFlowText = (speedDisplayMode == MODE_FLOW) ? "FLOW":"SPEED";
                 btnFlow.setText(btnFlowText);

                 displayFlowInformation();

             }
         });

        Button btnStart = (Button) view.findViewById(R.id.button_start_fragment);
         btnStart.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Toast.makeText(getActivity().getApplicationContext(),"Start pump "+mParam2,Toast.LENGTH_SHORT).show();
             }
         });

        Button btnStop = (Button) view.findViewById(R.id.button_stop_fragment);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSpeed = 0.0;
                displayFlowInformation();
            }
        });

        Button btnInc = (Button) view.findViewById(R.id.button_increase_fragment);
        btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (speedDisplayMode == MODE_FLOW)
                {
                    Double step = 0.1*currentPumpTubing.getTubeProductivity();
                    currentFlow += step;
                }
                else
                {
                    currentSpeed +=10;
                }

                displayFlowInformation();

            }
        });

        Button btnDec = (Button) view.findViewById(R.id.button_decrease_fragment);
        btnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (speedDisplayMode == MODE_FLOW)
                {
                    Double step = 0.1*currentPumpTubing.getTubeProductivity();
                    currentFlow -= step;
                }
                else
                {
                    currentSpeed -=10;
                }

                displayFlowInformation();

            }
        });

        Button btnSet = (Button) view.findViewById(R.id.button_set);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder pumpParameterSettingDialogBuilder = new AlertDialog.Builder(getActivity());
                pumpParameterSettingDialogBuilder.setTitle("Pump " + mParam2 + " tubing and direction");



                View pumpParameterSettingDialogView = View.inflate(getActivity(),R.layout.dialog_layout,null);
                ListView pumpParameterSettingDialogListView = (ListView) pumpParameterSettingDialogView.findViewById(R.id.dialog_list_view);

                final ArrayList<Tubing> tubingNames = new ArrayList<Tubing>();
                tubingNames.add(Tubing.TUBING_16x48x16);
                tubingNames.add(Tubing.TUBING_32x64x16);
                tubingNames.add(Tubing.TUBING_48x74x16);
                tubingNames.add(Tubing.TUBING_64x96x16);
                tubingNames.add(Tubing.TUBING_79x111x16);

                ArrayAdapter<Tubing> tubingNamesAdapter = new ArrayAdapter<Tubing>(getActivity(),android.R.layout.simple_list_item_single_choice);
                tubingNamesAdapter.addAll(tubingNames);
                pumpParameterSettingDialogListView.setAdapter(tubingNamesAdapter);
                pumpParameterSettingDialogListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                pumpParameterSettingDialogListView.setSelection(tubingNames.indexOf(currentPumpTubing));

                pumpParameterSettingDialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        dialogSelectedTubing = tubingNames.get(i);
                    }
                });

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                pumpParameterSettingDialogView.setLayoutParams(lp);

                Button rotationSettingDialogButton = (Button) pumpParameterSettingDialogView.findViewById(R.id.dialog_button);

                String rotation = rotationDirection ? "DIRECRION CLOCKWISE" : "DIRECTION COUNTERCLOCKWISE";
                rotationSettingDialogButton.setText(rotation);

                rotationSettingDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        rotationDirection = !rotationDirection;

                        String rotation = rotationDirection ? "DIRECRION CLOCKWISE" : "DIRECTION COUNTERCLOCKWISE";

                        ((Button) view).setText(rotation);
                    }
                });

                pumpParameterSettingDialogBuilder.setView(pumpParameterSettingDialogView);

                pumpParameterSettingDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        currentPumpTubing = dialogSelectedTubing;

                        displayFlowInformation();

                        dialogInterface.dismiss();

                    }
                });

                pumpParameterSettingDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });




                pumpParameterSettingDialogBuilder.create().show();


            }
        });

        if (this.layoutID == R.layout.fragment_pump_1){
            Button btnMET = (Button) view.findViewById(R.id.button_met);
             btnMET.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     Toast.makeText(getActivity()," MET works",Toast.LENGTH_SHORT).show();

                 }
             });
        }


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
