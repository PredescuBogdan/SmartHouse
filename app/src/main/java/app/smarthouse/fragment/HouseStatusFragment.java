package app.smarthouse.fragment;


import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import app.smarthouse.R;
import app.smarthouse.model.Sensor;
import app.smarthouse.util.IConstants;


public class HouseStatusFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView textViewLight;
    TextView textViewThief;
    TextView textViewTemperature;
    TextView textViewHumidity;
    TextView textViewGas;
    FloatingActionButton btnRefresh;
    private Handler handler = new Handler();
    private Runnable timedTask;



    private OnFragmentInteractionListener mListener;


    public HouseStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HouseStatusFragment newInstance(String param1, String param2) {
        HouseStatusFragment fragment = new HouseStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_house_status, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    handler.removeCallbacks(timedTask);
                }
                return false;
            }
        } );

        textViewLight = view.findViewById(R.id.textViewLight);
        textViewThief = view.findViewById(R.id.textViewThief);
        textViewTemperature = view.findViewById(R.id.textViewTemperature);
        textViewHumidity = view.findViewById(R.id.textViewHumidity);
        textViewGas = view.findViewById(R.id.textViewGas);

        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(buttonShowClickListener);
        getResults();

        timedTask = new Runnable(){

            @Override
            public void run() {
                getResults();
                handler.postDelayed(timedTask, 1000);
            }};


//        handler.post(timedTask);

    }

    View.OnClickListener buttonShowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getResults();
        }
    };

    private void getResults() {
        AndroidNetworking.get(IConstants.SERVER_URL + "/getHouseParameters")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Sensor sensor = new Sensor(response.getString("temperature"), response.getString("humidity"), response.getString("gase"), response.getString("presence"), response.getString("lightIntensity"));

                            textViewTemperature.setText(sensor.getTemperature() + " \u2103" );
                            textViewHumidity.setText(sensor.getHumidity() + " %");
                            textViewGas.setText(sensor.getGase());
                            textViewThief.setText(sensor.getPresence());
                            textViewLight.setText("Light is " + sensor.getLightIntensity());

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //TODO: STOP THREAD ON BACK PRESSED

    public boolean onBackPressed() {
        return false;
    }
 }
