package com.query.social.app.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.query.social.app.service.GoogleDatabaseService;
import com.query.social.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddQuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddQuestionFragment extends Fragment implements AnimationUtils.Dismissible {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private OnDismissedListener onDismissedListener;
    // TODO: Rename and change types of parameters
    private RevealAnimationSetting revealAnimationSetting;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Button submit;
    private GoogleDatabaseService googleDatabaseService;
    private EditText questionHeader;

    public AddQuestionFragment() {
        // Required empty public constructor
    }

    public static AddQuestionFragment newInstance(RevealAnimationSetting constructRevealSettings) {
        AddQuestionFragment fragment = new AddQuestionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, constructRevealSettings);
        args.putString(ARG_PARAM2, "");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            revealAnimationSetting = getArguments().getParcelable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_question, container, false);

        AnimationUtils.registerCircularRevealAnimation(getContext(), view, revealAnimationSetting,
                revealAnimationSetting.getFabColor(),revealAnimationSetting.getPageColor());
        questionHeader = (EditText)view.findViewById(R.id.askQuestion);
        submit = (Button)view.findViewById(R.id.submitButton);
         googleDatabaseService = new GoogleDatabaseService();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleDatabaseService.saveNewQuestion(questionHeader.getText());

//                mListener.onFragmentInteraction("");
            }
        });
        return view;
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

    @Override
    public void dismiss(final OnDismissedListener listener) {
        AnimationUtils.startCircularRevealExitAnimation(getContext(), getView(), revealAnimationSetting,
                revealAnimationSetting.getFabColor(),revealAnimationSetting.getFabColor(), new AnimationUtils.AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                listener.onDismissed();
            }
        });
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
