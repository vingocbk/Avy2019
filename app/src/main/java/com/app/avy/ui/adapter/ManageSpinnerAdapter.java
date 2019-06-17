package com.app.avy.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.appcompat.widget.AppCompatTextView;
import com.app.avy.R;
import com.app.avy.database.word.Word;

import java.util.ArrayList;
import java.util.List;

public class ManageSpinnerAdapter extends ArrayAdapter<Word> {
    private List<Word> data;

    public ManageSpinnerAdapter(Context context, ArrayList<Word> data) {
        super(context, 0, data);
        this.data = data;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_manage_spinner, parent, false);
            convertView.setTag(ViewHolder.createViewHolder(convertView));
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        if (data.get(position).getSelect()) {
            holder.textCountry.setText(data.get(position).getMWord());
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    private static class ViewHolder {
        AppCompatTextView textCountry;

        static ViewHolder createViewHolder(View view) {
            ViewHolder holder = new ViewHolder();
            holder.textCountry = view.findViewById(R.id.tv_key);
            return holder;
        }
    }
}