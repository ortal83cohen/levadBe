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

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.levadbe.berlin.app.LevadBeApplication;
import com.levadbe.berlin.app.R;
import com.levadbe.berlin.app.helper.ItemTouchHelperAdapter;
import com.levadbe.berlin.app.helper.ItemTouchHelperViewHolder;
import com.levadbe.berlin.app.helper.OnStartDragListener;
import com.levadbe.berlin.app.model.AddGroupWidgetWidget;
import com.levadbe.berlin.app.model.CurrencyConverterWidget;
import com.levadbe.berlin.app.model.LinkWidget;
import com.levadbe.berlin.app.model.NotificationWidget;
import com.levadbe.berlin.app.model.Weather;
import com.levadbe.berlin.app.model.WeatherWidget;
import com.levadbe.berlin.app.model.Widget;
import com.levadbe.berlin.app.viewmodel.ManagerWidgetViewModel;
import com.levadbe.berlin.app.viewmodel.WidgetViewModel;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;


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

    public static final int WIDGET_TYPE_NOTIFICATION = 4;
    public static final int WIDGET_TYPE_ADD_GROUP_WIDGET = 5;
    public static final int WIDGET_TYPE_MAP = 6;
    public static final int WIDGET_TYPE_CURRENCY_CONVERTER = 7;
    public static final int WIDGET_TYPE_LINK = 8;
    private final OnStartDragListener mDragStartListener;
    private final OnWidgetClickListener mOnWidgetClickListener;
    BaseActivity mContext;
    private WidgetViewModel mWidgetViewModel;

    public WidgetListAdapter(BaseActivity context, OnStartDragListener dragStartListener, OnWidgetClickListener onWidgetClickListener) {
        mDragStartListener = dragStartListener;
        mOnWidgetClickListener = onWidgetClickListener;
        mContext = context;

        mWidgetViewModel = ViewModelProviders.of(context).get(LevadBeApplication.isManager() ? ManagerWidgetViewModel.class : WidgetViewModel.class);

        mWidgetViewModel.getWidget().observe((context), new Observer<List<Widget>>() {
            @Override
            public void onChanged(@Nullable List<Widget> widgets) {
                notifyDataSetChanged();
            }
        });

    }


    public void updateState() {

        mWidgetViewModel = ViewModelProviders.of(mContext).get(LevadBeApplication.isManager() ? ManagerWidgetViewModel.class : WidgetViewModel.class);
        notifyDataSetChanged();
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
                widgetItemViewHolder = new NotificationViewHolderWidget(container);
                break;

            case WIDGET_TYPE_MAP:
                linearContainer.inflate(mContext, R.layout.simple_item, linearContainer);
                widgetItemViewHolder = new MapViewHolderWidget(container);
                break;
            case WIDGET_TYPE_ADD_GROUP_WIDGET:
                linearContainer.inflate(mContext, R.layout.simple_item, linearContainer);
                widgetItemViewHolder = new SendNotificationViewHolderWidget(container);
                break;
            case WIDGET_TYPE_CURRENCY_CONVERTER:
                linearContainer.inflate(mContext, R.layout.simple_item, linearContainer);
                widgetItemViewHolder = new CurrencyViewHolderWidget(container);
                break;
                case WIDGET_TYPE_LINK:
                linearContainer.inflate(mContext, R.layout.simple_item, linearContainer);
                widgetItemViewHolder = new LinkViewHolderWidget(container);
                break;
            default:
//                linearContainer.inflate(mContext, R.layout.simple_item, linearContainer);
                widgetItemViewHolder = null;//new NotificationViewHolderWidget(container);
        }
        return widgetItemViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return mWidgetViewModel.getWidget().getValue().get(position).type;
    }

    @Override
    public void onBindViewHolder(final WidgetItemViewHolder holder, int position) {
        holder.bind(mContext, mWidgetViewModel.getWidget().getValue().get(position), mOnWidgetClickListener);

    }

    @Override
    public void onItemDismiss(int position) {
        mWidgetViewModel.removeWidget(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mWidgetViewModel.getWidget().getValue(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mWidgetViewModel.getWidget().getValue().size();
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

        public void bind(Context context, final Widget widget, final OnWidgetClickListener onWidgetClickListener) {
            textView.setText(context.getText(R.string.forum));
            item.setBackground(context.getDrawable(R.drawable.ripple_color_white));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWidgetClickListener.onWidgetClicked(v, widget);
                }
            });
        }
    }

    public static class WeatherViewHolderWidget extends WidgetItemViewHolder {

        private TextView condDescr;
        private TextView temp;

        private TextView windSpeed;
        private TextView windDeg;

        private TextView hum;
        private ImageView imgView;

        public WeatherViewHolderWidget(View itemView) {
            super(itemView, false, false);
            condDescr = (TextView) itemView.findViewById(R.id.condDescr);
            temp = (TextView) itemView.findViewById(R.id.temp);
            hum = (TextView) itemView.findViewById(R.id.hum);
            windSpeed = (TextView) itemView.findViewById(R.id.windSpeed);
            windDeg = (TextView) itemView.findViewById(R.id.windDeg);
            imgView = (ImageView) itemView.findViewById(R.id.condIcon);
        }

        public void bind(Context context, Widget widget, OnWidgetClickListener onWidgetClickListener) {
            Weather weather = ((WeatherWidget) widget).getWeather();

            if (weather.location != null) {

                Picasso.with(context).load(weather.iconData).into(imgView);
                condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
                temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "�C");
                hum.setText("" + weather.currentCondition.getHumidity() + "%");
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

    public static class NotificationViewHolderWidget extends WidgetItemViewHolder {


        public final TextView textView;
//        public final ImageView handleView;

        public NotificationViewHolderWidget(View itemView) {
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
            textView.setText(((NotificationWidget) widget).text);
        }
    }

    public static class SendNotificationViewHolderWidget extends WidgetItemViewHolder {


        public final TextView textView;
        public final FrameLayout item;

        public SendNotificationViewHolderWidget(View itemView) {
            super(itemView, true, true);
            textView = (TextView) itemView.findViewById(R.id.text);
            item = (FrameLayout) itemView.findViewById(R.id.item);
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

        public void bind(Context context, final Widget widget, final OnWidgetClickListener onWidgetClickListener) {
            textView.setText(((AddGroupWidgetWidget) widget).text);
            item.setBackground(context.getDrawable(R.drawable.ripple_color_white));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWidgetClickListener.onWidgetClicked(v, widget);
                }
            });
        }
    }

    public static class CurrencyViewHolderWidget extends WidgetItemViewHolder {


        public final TextView textView;
        public final FrameLayout item;

        public CurrencyViewHolderWidget(View itemView) {
            super(itemView, true, true);
            textView = (TextView) itemView.findViewById(R.id.text);
            item = (FrameLayout) itemView.findViewById(R.id.item);
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

        public void bind(Context context, final Widget widget, final OnWidgetClickListener onWidgetClickListener) {
            textView.setText(((CurrencyConverterWidget) widget).text);
            item.setBackground(context.getDrawable(R.drawable.ripple_color_white));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWidgetClickListener.onWidgetClicked(v, widget);
                }
            });
        }
    }
    public static class LinkViewHolderWidget extends WidgetItemViewHolder {


        public final TextView textView;
        public final FrameLayout item;

        public LinkViewHolderWidget(View itemView) {
            super(itemView, true, true);
            textView = (TextView) itemView.findViewById(R.id.text);
            item = (FrameLayout) itemView.findViewById(R.id.item);
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

        public void bind(Context context, final Widget widget, final OnWidgetClickListener onWidgetClickListener) {
            textView.setText(((LinkWidget)widget).text);
            textView.setTextColor(context.getResources().getColor(R.color.tw__blue_pressed));
            item.setBackground(context.getDrawable(R.drawable.ripple_color_white));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWidgetClickListener.onWidgetClicked(v, widget);
                }
            });
        }
    }

    public static class MapViewHolderWidget extends WidgetItemViewHolder {


        public final TextView textView;
        public final FrameLayout item;

        public MapViewHolderWidget(View itemView) {
            super(itemView, true, true);
            textView = (TextView) itemView.findViewById(R.id.text);
            item = (FrameLayout) itemView.findViewById(R.id.item);
        }

        @Override
        public void onItemSelected() {
            //itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            // itemView.setBackgroundColor(0);
        }

        public void bind(Context context, final Widget widget, final OnWidgetClickListener onWidgetClickListener) {
            textView.setText("לחץ לפתיחת מפה");
            item.setBackground(context.getDrawable(R.drawable.ripple_color_white));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWidgetClickListener.onWidgetClicked(v, widget);
                }
            });
        }
    }



}
