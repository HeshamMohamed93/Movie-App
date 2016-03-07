package com.example.hesham.fimovie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by hesham on 09/10/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    public String[] x ;

    public ImageAdapter(Context c, String[] poster_paths) {
        context = c;
        x = poster_paths;

    }

    public int getCount() {
        return x.length;

    }


    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.imagelist, parent, false);
        } else {
            ImageView img = (ImageView) convertView.findViewById(R.id.image);
            String uri = "http://image.tmdb.org/t/p/w185/" +x[position];
            Picasso.with(context).load(uri).into(img);
            Log.v("kk",uri);

        }
        return convertView;


    }
}
