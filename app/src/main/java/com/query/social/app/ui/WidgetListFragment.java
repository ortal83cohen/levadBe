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

package com.query.social.app.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

import com.query.social.app.R;
import com.query.social.app.helper.OnStartDragListener;
import com.query.social.app.helper.SimpleItemTouchHelperCallback;
import com.query.social.app.model.Widget;
import com.query.social.app.viewmodel.WidgetViewModel;

import java.util.List;


public class WidgetListFragment extends Fragment implements OnStartDragListener {
    private RecyclerView recyclerView;
    private View view;
    private ItemTouchHelper mItemTouchHelper;
    private WidgetViewModel mWidgetViewModel;


    public WidgetListFragment() {
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

        mWidgetViewModel = ViewModelProviders.of(this).get(WidgetViewModel.class);

        final WidgetListAdapter adapter = new WidgetListAdapter(getActivity(), this, ((OnWidgetClickListener) getActivity()), mWidgetViewModel.getWidget().getValue());

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        mWidgetViewModel.getWidget().observe(((BaseActivity) getActivity()), new Observer<List<Widget>>() {
            @Override
            public void onChanged(@Nullable List<Widget> widgets) {
                adapter.setItems(widgets);
            }
        });

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("    לבד בברלין    ");

        loadBackdrop();
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
