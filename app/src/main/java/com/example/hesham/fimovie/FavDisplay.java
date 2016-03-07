package com.example.hesham.fimovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Hesham Mohamed on 10/7/2015.
 */
public class FavDisplay extends ArrayAdapter<String> {

    private Context mContext;
    String[] moveisposters;

    public FavDisplay(Context context, String[] resource) {
        super(context, R.layout.imagelist);
        mContext = context;
        moveisposters = resource;

    }
    @Override
    public int getCount() {
        return moveisposters.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.imagelist, parent, false);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.image);
        //for(int i = 0 ; i<moveisposters.length ; i++) {

            String uri = "http://image.tmdb.org/t/p/w185/" + moveisposters[position];
            // Result temp = mforecust.get(position);

            Picasso.with(mContext).load(uri).into(img);

        //}

        return img;
    }


}
