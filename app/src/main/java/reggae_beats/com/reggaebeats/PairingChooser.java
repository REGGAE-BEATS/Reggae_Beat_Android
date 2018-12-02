package reggae_beats.com.reggaebeats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PairingChooser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PairingChooser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PairingChooser extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String PAIR_CHOOSER_ACTION = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Activity activity;

    private OnFragmentInteractionListener mListener;

    public PairingChooser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PairingChooser.
     */
    // TODO: Rename and change types and number of parameters
    public static PairingChooser newInstance(String param1, String param2) {
        PairingChooser fragment = new PairingChooser();
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
        View view = inflater.inflate(R.layout.fragment_pairing_chooser, container, false);
        LinearLayout sendPairRequest = view.findViewById(R.id.send_pair_request);
        LinearLayout pairWithFriends = view.findViewById(R.id.pair_with_friends);
        sendPairRequest.setOnClickListener(this);
        pairWithFriends.setOnClickListener(this);
        //set click listeners now

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String pairChoosserAction) {
        if (mListener != null) {
            mListener.onFragmentInteraction(pairChoosserAction);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.send_pair_request:
                PAIR_CHOOSER_ACTION = "SEND_PAIR_REQUEST";
                onButtonPressed(PAIR_CHOOSER_ACTION);
                Intent intent = new Intent(activity, PairRequestActivity.class);
                startActivity(intent);
                break;
            case R.id.pair_with_friends:
                PAIR_CHOOSER_ACTION = "PAIR_WITH_FRIENDS";
                Intent intent1 = new Intent(activity, ChatActivity.class);
                startActivity(intent1);
                onButtonPressed(PAIR_CHOOSER_ACTION);
                break;
        }

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
        void onFragmentInteraction(String PairChooserAction);
    }
}
