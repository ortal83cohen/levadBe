package com.query.social.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.query.social.app.model.Question;
import com.query.social.app.R;
import com.query.social.app.viewmodel.QuestionsViewModel;
import com.query.social.app.viewmodel.UserViewModel;

import java.util.UUID;

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
    private QuestionsViewModel questionsViewModel;
    private EditText questionHeader;
    private UserViewModel userViewModel;

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
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.init(getActivity());
        AnimationUtils.registerCircularRevealAnimation(getContext(), view, revealAnimationSetting,
                revealAnimationSetting.getFabColor(),revealAnimationSetting.getPageColor());
        questionHeader = (EditText)view.findViewById(R.id.askQuestion);
        submit = (Button)view.findViewById(R.id.submitButton);
        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel.class);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userViewModel.getUser().getValue()==null){
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getContext());
                    }
                    builder.setTitle("Unknown user")
                            .setMessage("You can't post question without been logedin")
                            .setPositiveButton("Log in", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ((MainActivity)getActivity()).logInActivity();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else {
                    questionsViewModel.saveNewQuestion(new Question(UUID.randomUUID().toString(),
                            questionHeader.getText().toString(),userViewModel.getUser().getValue().getUid(),
                            userViewModel.getUser().getValue().getDisplayName()));
                    getActivity().onBackPressed();
                }
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
