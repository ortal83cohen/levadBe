package com.levadbe.berlin.app.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.levadbe.berlin.app.R;

public class AddGroupWidgetsFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private Spinner widgetTypeSpinner;
    private Spinner groupsSpinner;
    private FrameLayout widgetContainer;

    public AddGroupWidgetsFragment() {
        // Required empty public constructor
    }

    public static AddGroupWidgetsFragment newInstance() {
        AddGroupWidgetsFragment fragment = new AddGroupWidgetsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_widgets, container, false);

        widgetTypeSpinner = (Spinner) view.findViewById(R.id.widget_type_spinner);
        String[] widgetTypeItems = new String[]{"בחר ויג'ט","ויג'ט מפה", "ויג'ט לינק", "ויג'ט הודעה"};
        ArrayAdapter<String> widgetTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, widgetTypeItems);
        widgetTypeSpinner.setAdapter(widgetTypeAdapter);

        groupsSpinner = (Spinner) view.findViewById(R.id.groups_spinner);
        String[] groupsItems = new String[]{"קבוצה 1"};
        ArrayAdapter<String>   groupsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, groupsItems);
        groupsSpinner.setAdapter(groupsAdapter);

        widgetContainer = (FrameLayout) view.findViewById(R.id.widget_container);





        return view;
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
