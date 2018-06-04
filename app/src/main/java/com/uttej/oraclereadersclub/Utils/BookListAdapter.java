package com.uttej.oraclereadersclub.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uttej.oraclereadersclub.Models.Photo;
import com.uttej.oraclereadersclub.R;

import java.util.List;

/**
 * Created by Clean on 29-03-2018.
 */

public class BookListAdapter extends ArrayAdapter<Photo>{

    private static final String TAG = "BookListAdapter";

    private LayoutInflater layoutInflater;
    private List<Photo> books = null;
    private int layoutResource;
    private Context context;

    public BookListAdapter(@NonNull Context context, int resource, @NonNull List<Photo> objects) {
        super(context, resource, objects);

        context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.books = objects;

    }

    private static class ViewHolder{
        TextView bookName;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.bookName = (TextView) convertView.findViewById(R.id.listViewBookName);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bookName.setText(getItem(position).getBook_title());
        return convertView;
    }
}
