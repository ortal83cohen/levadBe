package com.levadbe.berlin.app.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.levadbe.berlin.app.R;
import com.levadbe.berlin.app.helper.Strings;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CurrencyWidgetsFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.button)
    ToggleButton button;
    @BindView(R.id.numberEditText)
    EditText numberEditText;
    @BindView(R.id.results)
    TextView results;
    private float number;

    private OnFragmentInteractionListener mListener;

    public CurrencyWidgetsFragment() {
        // Required empty public constructor
    }

    public static CurrencyWidgetsFragment newInstance(Float number) {
        CurrencyWidgetsFragment fragment = new CurrencyWidgetsFragment();
        Bundle args = new Bundle();
        args.putFloat(ARG_PARAM1, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            number = getArguments().getFloat(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_currency, container, false);

        ButterKnife.bind(getActivity(), view);// not working

        button = (ToggleButton) view.findViewById(R.id.button);
        numberEditText = (EditText) view.findViewById(R.id.numberEditText);
        results = (TextView) view.findViewById(R.id.results);
        button.setText("ממיר מיורו לשקל");
        button.setTextOff("ממיר מיורו לשקל");
        button.setTextOn("ממיר משקל ליורו");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberEditText.setText(numberEditText.getText());
            }
        });
        numberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Strings.isNullOrEmpty(s.toString())) {
                    results.setText("0");
                } else if (!button.isChecked()) {
                    results.setText(String.valueOf(Float.valueOf(s.toString()) * number));
                } else {
                    results.setText(String.valueOf(Float.valueOf(s.toString()) / number));
                }
            }
        });

        numberEditText.setText("1");

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
}
