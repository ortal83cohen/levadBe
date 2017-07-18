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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.query.social.app.R;
import com.query.social.app.helper.ItemTouchHelperAdapter;
import com.query.social.app.helper.ItemTouchHelperViewHolder;
import com.query.social.app.helper.OnStartDragListener;
import com.query.social.app.model.Weather;
import com.query.social.app.model.WeatherWidget;
import com.query.social.app.model.Widget;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class WidgetListAdapter extends RecyclerView.Adapter<WidgetItemViewHolder>
        implements ItemTouchHelperAdapter {
    public static final int WIDGET_TYPE_FORM = 1;
    public static final int WIDGET_TYPE_WHETHER = 2;
    public static final int WIDGET_TYPE_CLOCK = 3;
    public static final int WIDGET_TYPE_NOTIFICATION = 4;
    private final List<Widget> mItems = new ArrayList<>();
    Context mContext;

    private final OnStartDragListener mDragStartListener;
    private final OnWidgetClickListener mOnWidgetClickListener;

    public WidgetListAdapter(Context context, OnStartDragListener dragStartListener, OnWidgetClickListener onWidgetClickListener, List<Widget> items) {
        mDragStartListener = dragStartListener;
        mOnWidgetClickListener = onWidgetClickListener;
        mContext = context;
        mItems.clear();
        mItems.addAll(items);
    }

    @Override
    public WidgetItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        WidgetItemViewHolder widgetItemViewHolder;
        View container = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_item, parent, false);
        LinearLayout linearContainer = (LinearLayout) container.findViewById(R.id.linear_container);
        switch (viewType) {
            case WIDGET_TYPE_FORM:
                linearContainer.inflate(mContext, R.layout.simple_item, linearContainer);
                widgetItemViewHolder = new FormViewHolderWidget(container);
                break;
            case WIDGET_TYPE_WHETHER:
                linearContainer.inflate(mContext, R.layout.weather_item, linearContainer);
                widgetItemViewHolder = new WeatherViewHolderWidget(container);
                break;
            case WIDGET_TYPE_NOTIFICATION:
                linearContainer.inflate(mContext, R.layout.simple_item, linearContainer);
                widgetItemViewHolder = new SimpleViewHolderWidget(container);
                break;
            case WIDGET_TYPE_CLOCK:
                linearContainer.inflate(mContext, R.layout.simple_item, linearContainer);
                widgetItemViewHolder = new ClockViewHolderWidget(container);
                break;
            default:
                linearContainer.inflate(mContext, R.layout.simple_item, linearContainer);
                widgetItemViewHolder = new SimpleViewHolderWidget(container);
        }
        return widgetItemViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).type;
    }

    @Override
    public void onBindViewHolder(final WidgetItemViewHolder holder, int position) {
        holder.bind(mContext, mItems.get(position), mOnWidgetClickListener);

    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<Widget> widgets) {
        mItems.clear();
        mItems.addAll(widgets);
        notifyDataSetChanged();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */


    public static class FormViewHolderWidget extends WidgetItemViewHolder {


        public final TextView textView;
        public final FrameLayout item;

        public FormViewHolderWidget(View itemView) {
            super(itemView, false, false);
            textView = (TextView) itemView.findViewById(R.id.text);
            item = (FrameLayout) itemView.findViewById(R.id.item);
        }

        @Override
        public void onItemSelected() {
            //itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            //itemView.setBackgroundColor(0);
        }

        public void bind(Context context, Widget widget, final OnWidgetClickListener onWidgetClickListener) {
            textView.setText(context.getText(R.string.forum));
            item.setBackground(context.getDrawable(R.drawable.ripple_color_white));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWidgetClickListener.onWidgetClicked(v);
                }
            });
        }
    }

    public static class WeatherViewHolderWidget extends WidgetItemViewHolder {
        private TextView cityText;
        private TextView condDescr;
        private TextView temp;
        private TextView press;
        private TextView windSpeed;
        private TextView windDeg;

        private TextView hum;
        private ImageView imgView;

        public WeatherViewHolderWidget(View itemView) {
            super(itemView, true, true);
            cityText = (TextView) itemView.findViewById(R.id.cityText);
            condDescr = (TextView) itemView.findViewById(R.id.condDescr);
            temp = (TextView) itemView.findViewById(R.id.temp);
            hum = (TextView) itemView.findViewById(R.id.hum);
            press = (TextView) itemView.findViewById(R.id.press);
            windSpeed = (TextView) itemView.findViewById(R.id.windSpeed);
            windDeg = (TextView) itemView.findViewById(R.id.windDeg);
            imgView = (ImageView) itemView.findViewById(R.id.condIcon);
        }

        public void bind(Context context, Widget widget, OnWidgetClickListener onWidgetClickListener) {
            Weather weather = ((WeatherWidget) widget).getWeather();

            if (weather.location != null) {

                Picasso.with(context).load(weather.iconData).into(imgView);


                cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
                condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
                temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "�C");
                hum.setText("" + weather.currentCondition.getHumidity() + "%");
                press.setText("" + weather.currentCondition.getPressure() + " hPa");
                windSpeed.setText("" + weather.wind.getSpeed() + " mps");
                windDeg.setText("" + weather.wind.getDeg() + "�");
            }
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }


    }

    public static class SimpleViewHolderWidget extends WidgetItemViewHolder {


        public final TextView textView;
//        public final ImageView handleView;

        public SimpleViewHolderWidget(View itemView) {
            super(itemView, true, true);
            textView = (TextView) itemView.findViewById(R.id.text);
//            handleView = (ImageView) itemView.findViewById(R.id.handle);
        }

        @Override
        public void onItemSelected() {
            //itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            // itemView.setBackgroundColor(0);
        }

        public void bind(Context context, Widget widget, OnWidgetClickListener onWidgetClickListener) {
            textView.setText(widget.text);
        }
    }

    public static class ClockViewHolderWidget extends WidgetItemViewHolder {


        public final TextView textView;
//        public final ImageView handleView;

        public ClockViewHolderWidget(View itemView) {
            super(itemView, false, false);
            textView = (TextView) itemView.findViewById(R.id.text);
//            handleView = (ImageView) itemView.findViewById(R.id.handle);
        }

        @Override
        public void onItemSelected() {
            //itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            // itemView.setBackgroundColor(0);
        }

        public void bind(Context context, Widget widget, OnWidgetClickListener onWidgetClickListener) {
            textView.setText(widget.text);
        }
    }


}
