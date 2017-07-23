package com.levadbe.berlin.app.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.levadbe.berlin.app.R;
import com.levadbe.berlin.app.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Question} and makes a call to the
 * specified {@link QuestionsListFragment.OnListItemListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class QuestionItemRecyclerViewAdapter extends RecyclerView.Adapter<QuestionItemRecyclerViewAdapter.ViewHolder> {

    private final QuestionsListFragment.OnListItemListener mListener;
    Activity activity;
    private List<Question> mValues = new ArrayList<>();

    public QuestionItemRecyclerViewAdapter(Activity activity, List<Question> items, QuestionsListFragment.OnListItemListener listener) {
        mValues.addAll(items);
        mListener = listener;
        this.activity = activity;
    }

    public void setValues(List<Question> values) {
        mValues.clear();
        mValues.addAll(values);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_item, parent, false);
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ViewGroup.LayoutParams lp = view.getLayoutParams();

        lp.width = size.x;
//        lp.height = size.x;
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getmUserName());
        holder.mContentView.setText(mValues.get(position).getmHeader());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onQuestionItemSelected(holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mValues == null) {
            return 0;
        }
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final int color;
        public Question mItem;
        public ConstraintLayout mRoot;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mRoot = (ConstraintLayout) view.findViewById(R.id.root);

            Random rnd = new Random();
            color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            mRoot.setBackgroundColor(color);
        }

        public ConstraintLayout getmRoot() {
            return mRoot;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
