package com.levadbe.berlin.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.levadbe.berlin.app.R;
import com.levadbe.berlin.app.helper.Strings;
import com.levadbe.berlin.app.viewmodel.AddWidgetViewModel;

public class AddGroupWidgetsFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    private Spinner widgetTypeSpinner;

    private Spinner groupsSpinner;

    private EditText header;

    private EditText link;

    private android.support.design.widget.TextInputLayout linkWrapper;

    private Button submitButton;

    private AddWidgetViewModel addWidgetViewModel;

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
        groupsSpinner = (Spinner) view.findViewById(R.id.groups_spinner);
        header = (EditText) view.findViewById(R.id.header);
        link = (EditText) view.findViewById(R.id.link);
        linkWrapper = (android.support.design.widget.TextInputLayout) view.findViewById(R.id.linkWrapper);
        submitButton = (Button) view.findViewById(R.id.button2);

        addWidgetViewModel = ViewModelProviders.of(this).get(AddWidgetViewModel.class);
        initViews();

        return view;
    }

    private void initViews() {

        final String[] widgetTypeItems = new String[]{getString(R.string.select_widget), getString(R.string.maps_widget),
                getString(R.string.link_widget), getString(
                R.string.massage_widget)};
        final ArrayAdapter<String> widgetTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                widgetTypeItems);
        widgetTypeSpinner.setAdapter(widgetTypeAdapter);
        widgetTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addWidgetViewModel.setWidgetTypeSpinner(widgetTypeItems[position]);
                switch (position) {
                    case 0:
                        setEnableItems(false, false);
                        break;
                    case 1:
                        setEnableItems(false, false);
                        break;
                    case 2:
                        setEnableItems(true, true);
                        break;
                    case 3:
                        setEnableItems(true, false);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final String[] groupsItems = new String[]{"בחר קבוצה", "קבוצה 1"};
        ArrayAdapter<String> groupsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, groupsItems);
        groupsSpinner.setAdapter(groupsAdapter);

        groupsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addWidgetViewModel.setGroupsSpinner(groupsItems[position]);
                validateSubmit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    addWidgetViewModel.saveGroupWidget(widgetTypeAdapter.getPosition((String) addWidgetViewModel.getWidgetTypeSpinner().getValue()), new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(getActivity());
                            }
                            builder.setTitle("הוספת ויג'ט")
//                                    .setMessage(" שם הקבוצה " + groups.get(0))
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().onBackPressed();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();
                        }
                    });
                }
            }
        });

        link.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addWidgetViewModel.setLink(s.toString());
                validateSubmit();
            }
        });
        header.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addWidgetViewModel.setHeader(s.toString());
                validateSubmit();
            }
        });
    }

    private boolean validateFields() {

        if (link.getVisibility() == View.VISIBLE && Strings.isNullOrEmpty(link.getText().toString()) ||
                header.getVisibility() == View.VISIBLE && Strings.isNullOrEmpty(header.getText().toString())) {
            return false;
        }
        if (link.getVisibility() == View.VISIBLE && !URLUtil.isValidUrl(link.getText().toString())) {
            linkWrapper.setError("חייב להיות לינק");
            return false;
        }
        linkWrapper.setError("");
        return true;
    }

    private void setEnableItems(boolean show_Header, boolean show_Link) {
        header.setVisibility(show_Header ? View.VISIBLE : View.GONE);
        link.setVisibility(show_Link ? View.VISIBLE : View.GONE);
        validateSubmit();
    }

    private void validateSubmit() {
        if (groupsSpinner.getSelectedItemPosition() != 0 &&
                widgetTypeSpinner.getSelectedItemPosition() != 0 &&
                validateFields()) {
            submitButton.setEnabled(true);
        } else {
            submitButton.setEnabled(false);
        }

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
