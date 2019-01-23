package app.smarthouse.fragment;


import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import app.smarthouse.R;
import app.smarthouse.util.IConstants;
import app.smarthouse.util.Utils;

import static android.app.Activity.RESULT_OK;


public class LightPanelFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;


    CardView cardViewLiving;
    CardView cardViewKitchen;
    CardView cardViewBedroom;
    CardView cardViewBathroom;
    CardView cardViewGarage;
    CardView cardViewEntrance;
    //Computed
    TextView textViewLightConsumption;
    TextView textViewLightTotal;

    //Rooms
    //Row 1
    ImageView thumbnail_living;
    TextView textViewLiving;

    ImageView thumbnail_bedroom;
    TextView textViewBedroom;

    ImageView thumbnail_kitchen;
    TextView textViewKitchen;

    //Row2
    ImageView thumbnail_bathroom;
    TextView textViewBathroom;

    ImageView thumbnail_garage;
    TextView textViewGarage;

    ImageView thumbnail_entrance;
    TextView textViewEntrance;

    FloatingActionButton btnVoice;

    String living;
    String bedroom ;
    String kitchen;

    //Row2
    String bathroom ;
    String garage ;
    String entrance ;
    ArrayList<String> allComands;

    private Handler handler = new Handler();
    private Runnable timedTask;

    int countLights=0;

    private final int REQ_CODE_SPEECH_INPUT = 100;


    public LightPanelFragment() {
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
    public static LightPanelFragment newInstance(String param1, String param2) {
        LightPanelFragment fragment = new LightPanelFragment();
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
        return inflater.inflate(R.layout.fragment_light_panel, container, false);

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

        AndroidNetworking.initialize(getActivity().getApplicationContext());
        //Computed
        textViewLightConsumption = view.findViewById(R.id.textViewLightConsumption);
        textViewLightTotal = view.findViewById(R.id.textViewLightTotal);

        //Rooms
        //Row1
        thumbnail_living = view.findViewById(R.id.thumbnail_living);
        textViewLiving= view.findViewById(R.id.textViewLiving);

        thumbnail_bedroom = view.findViewById(R.id.thumbnail_bedroom);
        textViewBedroom= view.findViewById(R.id.textViewBedroom);

        thumbnail_kitchen = view.findViewById(R.id.thumbnail_kitchen);
        textViewKitchen= view.findViewById(R.id.textViewKitchen);

        //Row2
        thumbnail_bathroom = view.findViewById(R.id.thumbnail_bathroom);
        textViewBathroom= view.findViewById(R.id.textViewBathroom);

        thumbnail_garage = view.findViewById(R.id.thumbnail_garage);
        textViewGarage= view.findViewById(R.id.textViewGarage);

        thumbnail_entrance = view.findViewById(R.id.thumbnail_entrance);
        textViewEntrance= view.findViewById(R.id.textViewEntrance);

        btnVoice = view.findViewById(R.id.btnVoice);


        cardViewLiving = view.findViewById(R.id.card_view_living);

        cardViewLiving.setClickable(true);

        cardViewKitchen = view.findViewById(R.id.card_view_kitchen);
        cardViewKitchen.setClickable(true);

        cardViewBedroom= view.findViewById(R.id.card_view_bedroom);
        cardViewBedroom.setClickable(true);

        cardViewBathroom= view.findViewById(R.id.card_view_bathroom);
        cardViewBathroom.setClickable(true);

        cardViewGarage= view.findViewById(R.id.card_view_garage);
        cardViewGarage.setClickable(true);

        cardViewEntrance= view.findViewById(R.id.card_view_entrance);
        cardViewEntrance.setClickable(true);


        getResults();


        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        timedTask = new Runnable(){

            @Override
            public void run() {
                getResults();
                handler.postDelayed(timedTask, 100000);
            }};


        handler.post(timedTask);

        cardViewLiving.setOnClickListener(cardViewLivingClickListener );


        cardViewKitchen.setOnClickListener(cardViewKitchenClickListener);

        cardViewBedroom.setOnClickListener(cardViewBedroomClickListener);

        cardViewBathroom.setOnClickListener(cardViewBathroomClickListener);

        cardViewGarage.setOnClickListener(cardViewGarageClickListener);


        cardViewEntrance.setOnClickListener(cardViewEntranceClickListener);

        allComands = Utils.getArrayOfCommands();


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


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
        }
    }

    public void updateView() {
        if(countLights==0) {
            textViewLightTotal.setText(countLights + " Lights Are OFF");
            textViewLightConsumption.setText(String.valueOf(0.1*countLights) + " Watts In Use");
        }else if(countLights==1) {
            textViewLightTotal.setText(countLights + " Light is ON");
            textViewLightConsumption.setText(String.valueOf(0.1*countLights) + " Watts In Use");
        }else if(countLights==3) {
            textViewLightTotal.setText(countLights + " Lights Are ON");
            textViewLightConsumption.setText("0.3 Watts In Use");
        }else if(countLights==6) {
            textViewLightTotal.setText(countLights + " Lights Are ON");
            textViewLightConsumption.setText("0.6 Watts In Use");
        }

        else {
            textViewLightTotal.setText(countLights + " Lights Are ON");
            textViewLightConsumption.setText(String.valueOf(0.1*countLights) + " Watts In Use");
        }

    }

    private void getResults() {
        AndroidNetworking.get(IConstants.SERVER_URL + "/getLightRooms")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            countLights=0;
                            //Row1
                            living = response.getString("living" );
                            bedroom = response.getString("bedroom");
                            kitchen = response.getString("kitchen");

                            //Row2
                            bathroom = response.getString("bathroom");
                            garage = response.getString("garage");
                            entrance = response.getString("entrance");

                            //Row1
                            if(living.equals("0")) {
                                textViewLiving.setText("OFF");
                                thumbnail_living.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                            }else {
                                countLights++;
                                textViewLiving.setText("ON");
                                thumbnail_living.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));

                            }

                            if(bedroom.equals("0")) {
                                textViewBedroom.setText("OFF");
                                thumbnail_bedroom.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                            }else {
                                countLights++;
                                textViewBedroom.setText("ON");
                                thumbnail_bedroom.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));

                            }


                            if(kitchen.equals("0")) {
                                textViewKitchen.setText("OFF");
                                thumbnail_kitchen.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                            }else {
                                countLights++;
                                textViewKitchen.setText("ON");
                                thumbnail_kitchen.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));

                            }


                            //Row2
                            if(bathroom.equals("0")) {
                                textViewBathroom.setText("OFF");
                                thumbnail_bathroom.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                            }else {
                                countLights++;
                                textViewBathroom.setText("ON");
                                thumbnail_bathroom.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));

                            }

                            if(garage.equals("0")) {
                                textViewGarage.setText("OFF");
                                thumbnail_garage.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                            }else {
                                countLights++;
                                textViewGarage.setText("ON");
                                thumbnail_garage.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));

                            }

                            if(entrance.equals("0")) {
                                textViewEntrance.setText("OFF");
                                thumbnail_entrance.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                            }else {
                                countLights++;
                                textViewEntrance.setText("ON");
                                thumbnail_entrance.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));

                            }
                            DecimalFormat df = new DecimalFormat("#.#");
                            textViewLightConsumption.setText(String.valueOf(df.format(0.1*countLights)) + " Watts In Use");
                            if(countLights == 0) {
                                textViewLightTotal.setText("Lights Are OFF");
                            }else if (countLights == 1) {
                                textViewLightTotal.setText(countLights + " Light Is ON");
                            }else if (countLights==3) {
                                textViewLightTotal.setText(countLights + " Lights Are ON");
                                textViewLightConsumption.setText("0.3 Watts In Use");
                            }
                            else {
                                textViewLightTotal.setText(countLights + " Lights Are ON");
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
//                        Toast.makeText(getApplication(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (allComands.contains(result.get(0).toLowerCase())) {
                        AndroidNetworking.post(IConstants.SERVER_URL + "/voiceCommands").addBodyParameter("command", result.get(0).toLowerCase())
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                        }catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                    }
                                });
                        getResults();
                    }
                }
                break;
            }

        }
    }

    private View.OnClickListener cardViewLivingClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            AndroidNetworking.post(IConstants.SERVER_URL + "/lightOnOff").addBodyParameter("room", "living")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int state = Integer.valueOf(response.getString("state"));
                                if(state == 0) {
                                    countLights++;
                                    textViewLiving.setText("ON");
                                    thumbnail_living.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));
                                    if(countLights==0) {
                                        updateView();
                                    }else {
                                        updateView();
                                    }
                                }else {
                                    countLights--;
                                    textViewLiving.setText("OFF");
                                    thumbnail_living.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                                    if(countLights==0) {
                                        textViewLightTotal.setText(countLights + " Light Is ON");
                                        textViewLightConsumption.setText(String.valueOf(0.1*countLights) + " Watts In Use");
                                    }else {
                                        updateView();
                                    }
                                }

                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(getActivity().getApplicationContext(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    };

    private View.OnClickListener cardViewKitchenClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            AndroidNetworking.post(IConstants.SERVER_URL + "/lightOnOff").addBodyParameter("room", "kitchen")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int state = Integer.valueOf(response.getString("state"));
                                if(state == 0) {
                                    countLights++;
                                    textViewKitchen.setText("ON");
                                    thumbnail_kitchen.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));
                                    if(countLights==0) {
                                        updateView();
                                    }else {
                                        updateView();
                                    }
                                }else {
                                    countLights--;
                                    textViewKitchen.setText("OFF");
                                    thumbnail_kitchen.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                                    if(countLights==0) {
                                        updateView();
                                    }else {
                                        updateView();
                                    }
                                }

                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
//                        Toast.makeText(getApplication(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    };

    private View.OnClickListener cardViewBedroomClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AndroidNetworking.post(IConstants.SERVER_URL + "/lightOnOff").addBodyParameter("room", "bedroom")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int state = Integer.valueOf(response.getString("state"));
                                if(state == 0) {
                                    countLights++;
                                    textViewBedroom.setText("ON");
                                    thumbnail_bedroom.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));
                                    if(countLights==0) {
                                        updateView();
                                    }else {
                                        updateView();
                                    }
                                }else {
                                    countLights--;
                                    textViewBedroom.setText("OFF");
                                    thumbnail_bedroom.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                                    if(countLights==0) {
                                        updateView();
                                    }else {
                                        updateView();
                                    }
                                }

                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
//                        Toast.makeText(getApplication(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    };

    private View.OnClickListener cardViewBathroomClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AndroidNetworking.post(IConstants.SERVER_URL + "/lightOnOff").addBodyParameter("room", "bathroom")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int state = Integer.valueOf(response.getString("state"));
                                if(state == 0) {
                                    countLights++;
                                    textViewBathroom.setText("ON");
                                    thumbnail_bathroom.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));
                                    if(countLights==0) {
                                        updateView();
                                    }else {
                                        updateView();
                                    }
                                }else {
                                    countLights--;
                                    textViewBathroom.setText("OFF");
                                    thumbnail_bathroom.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                                    if(countLights==0) {
                                        updateView();
                                    }else {
                                        updateView();
                                    }
                                }

                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
//                        Toast.makeText(getApplication(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    };

    private View.OnClickListener cardViewGarageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AndroidNetworking.post(IConstants.SERVER_URL + "/lightOnOff").addBodyParameter("room", "garage")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int state = Integer.valueOf(response.getString("state"));
                                if(state == 0) {
                                    textViewGarage.setText("ON");
                                    thumbnail_garage.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));
                                    countLights++;
                                    if(countLights==0) {
                                        updateView();
                                    }else {
                                        updateView();
                                    }
                                }else {
                                    textViewGarage.setText("OFF");
                                    thumbnail_garage.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                                    countLights--;
                                    if(countLights==0) {
                                        updateView();
                                    }else {
                                        updateView();
                                    }
                                }

                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
//                        Toast.makeText(getApplication(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    };

    private View.OnClickListener cardViewEntranceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AndroidNetworking.post(IConstants.SERVER_URL + "/lightOnOff").addBodyParameter("room", "entrance")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int state = Integer.valueOf(response.getString("state"));
                                if(state == 0) {
                                    textViewEntrance.setText("ON");
                                    countLights++;
                                    thumbnail_entrance.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_4));
                                    if(countLights==0) {
                                        updateView();
                                    }else {
                                        updateView();
                                    }
                                }else {
                                    countLights--;
                                    textViewEntrance.setText("OFF");
                                    thumbnail_entrance.setBackground(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.gradient_7));
                                    updateView();

                                }

                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
//                        Toast.makeText(getApplication(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    };
}
