package com.query.social.app.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.query.social.app.R;
import com.query.social.app.model.Question;
import com.query.social.app.viewmodel.QuestionsViewModel;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListItemListener}
 * interface.
 */
public class QuestionsListFragment extends Fragment {

    private OnListItemListener mListItemListener;
    private OnAddMoreItemButtonListener mAddMoreItemButtonListener;
    private QuestionItemRecyclerViewAdapter questionItemRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingButton;
    private QuestionsViewModel questionsViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionsListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static QuestionsListFragment newInstance() {
        QuestionsListFragment fragment = new QuestionsListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questionitem_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);      // Set the adapter
        floatingButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);      // Set the adapter
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(  new DividerItemDecoration(getActivity().getDrawable(R.drawable.list_devider),
                false, true));
        questionsViewModel = ViewModelProviders.of(this).get(QuestionsViewModel.class);
        LiveData<List<Question>> questions = questionsViewModel.getQuestions();
         questionItemRecyclerViewAdapter= new QuestionItemRecyclerViewAdapter(questions.getValue(), mListItemListener);
        recyclerView.setAdapter(questionItemRecyclerViewAdapter);
        questions.observe((BaseActivity) getActivity(), new Observer<List<Question>>() {
            @Override
            public void onChanged(@Nullable List<Question> questions) {
                questionItemRecyclerViewAdapter.setValues(questions);
            }
        });
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddMoreItemButtonListener.onAddItemInteraction();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListItemListener = (OnListItemListener) context;
        mAddMoreItemButtonListener = (OnAddMoreItemButtonListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListItemListener = null;
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
    public interface OnListItemListener {
        // TODO: Update argument type and name
        void onListInteraction(Question item);
    }


    public interface OnAddMoreItemButtonListener {
        void onAddItemInteraction();
    }

    public RevealAnimationSetting constructRevealSettings() {
        return RevealAnimationSetting.with(
                (int) (floatingButton.getX() + floatingButton.getWidth() / 2),
                (int) (floatingButton.getY() + floatingButton.getHeight() / 2),
                getView().getWidth(),
                getView().getHeight(), floatingButton.getBackgroundTintList().getDefaultColor(), getActivity().getColor(android.R.color.white));
    }
}
