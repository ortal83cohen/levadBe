/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.levadbe.berlin.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.levadbe.berlin.app.R;
import com.levadbe.berlin.app.helper.OnStartDragListener;
import com.levadbe.berlin.app.helper.SimpleItemTouchHelperCallback;

public class WidgetListFragment extends Fragment implements OnStartDragListener {
    private RecyclerView recyclerView;
    private View view;
    private ItemTouchHelper mItemTouchHelper;


    public WidgetListFragment() {
    }

    public static WidgetListFragment newInstance() {
        WidgetListFragment fragment = new WidgetListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_widget_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final WidgetListAdapter adapter = new WidgetListAdapter((BaseActivity) getActivity(), this, ((OnWidgetClickListener) getActivity()));

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);


        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("    לבד בברלין    ");

        loadBackdrop();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((WidgetListAdapter) recyclerView.getAdapter()).updateState();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) view.findViewById(R.id.backdrop);
        imageView.setImageDrawable(getActivity().getDrawable(R.drawable.berlin_panorama_mitte));
//        Glide.with(this).load(getActivity().getDrawable(R.drawable.berlin_panorama_mitte)).into(imageView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
