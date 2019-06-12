package com.app.avy.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import com.app.avy.R;
import com.app.avy.database.cabinet.Cabinet;
import com.app.avy.listenner.OnItemSpinnerClickListenner;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<Cabinet> {
    private List<Cabinet> data;
    private OnItemSpinnerClickListenner mListener;

    public CustomSpinnerAdapter(Context context, ArrayList<Cabinet> data, OnItemSpinnerClickListenner listenner) {
        super(context, 0, data);
        this.data = data;
        mListener = listenner;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_items, parent, false);
            convertView.setTag(ViewHolder.createViewHolder(convertView));
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.textCountry.setText(data.get(position).getCabinet());
        holder.img_chose.setVisibility(data.get(position).getSelect() ? View.VISIBLE : View.GONE);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_chose.setVisibility(!data.get(position).getSelect() ? View.VISIBLE : View.GONE);
                data.get(position).setSelect(!data.get(position).getSelect());
                if (mListener != null) {
                    mListener.onItemSpinnerClick(data.get(position));
                }
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    private static class ViewHolder {
        AppCompatImageView img_chose;
        TextView textCountry;

        public static ViewHolder createViewHolder(View view) {
            ViewHolder holder = new ViewHolder();
            holder.textCountry = view.findViewById(R.id.textCountry);
            holder.img_chose = view.findViewById(R.id.img_chose);
            return holder;
        }
    }
}