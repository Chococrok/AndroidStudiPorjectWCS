package matterless.fr.wcs.matterless;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keru on 18/04/17.
 */

public class TutoListAdapter extends ArrayAdapter {

    private Activity mContext;
    private ArrayList<String> mList;
    private int mLayout;


    public TutoListAdapter(Activity context, int layout, ArrayList<String> list) {
        super(context, layout, list);

        this.mContext = context;
        this.mLayout = layout;
        this.mList = list;
    }

    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        public TextView textViewTutoItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;
        if (rowView == null) {

            LayoutInflater layoutInflater = mContext.getLayoutInflater();
            rowView = layoutInflater.inflate(mLayout, null, true);
            viewHolder = new ViewHolder();
            viewHolder.textViewTutoItem = (TextView) rowView.findViewById(R.id.textViewTutoItem);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        String item = mList.get(position);
        viewHolder.textViewTutoItem.setText(item);
        return rowView;
    }
}

