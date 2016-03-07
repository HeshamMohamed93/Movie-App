package com.example.hesham.fimovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }
    private Callbacks mCallbacks;

    public interface Callbacks{
        void movieSelected(String id , String title , String overview , String date , String poster , String vote);
    }

    GridView gridView;
    private ArrayAdapter<String> MovieAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_main, container, false);
        //hasOptionsMenu(true);
        MovieAdapter = new ArrayAdapter<String>(getActivity(),R.layout.fragment_main,R.id.gridView , new ArrayList<String>());


        MovieTask task = new MovieTask();
        task.execute("http://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22c&api_key=50bce7edbac33ed4f4c7757ee875026d");
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(MovieAdapter);
        gridView.setClickable(true);
        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
       // inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.v("cc", String.valueOf(id));
        if(id==R.id.action_popular)
        {
            MovieTask task = new MovieTask();
            Log.i("OptionsMenu", "option 1 selected from frag 1");
            task.execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=50bce7edbac33ed4f4c7757ee875026d");
            return true;
        }
        else if (id==R.id.action_rated)
        {
            MovieTask task = new MovieTask();
            task.execute("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=50bce7edbac33ed4f4c7757ee875026d");
            return true;
        }
        else if (id==R.id.action_favourit)
        {
            final DBAdapter db = new DBAdapter(getActivity());
            try {
                db.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            final Cursor c = db.getAllContacts();
            int index = 0;
            String [] movieFav = new String[c.getCount()];
            String DATA = null;
            if (c.moveToFirst()) {
                do {
                    DATA = c.getString(0)+">"+c.getString(1)+">"+c.getString(2)+">"+c.getString(3)+">"+c.getString(4)+">"+c.getString(5);
                    movieFav[index] = DATA;
                    index++;
                    Log.v("data",DATA);

                } while (c.moveToNext());
                DisplayContact (movieFav);


            }
            db.close();
            return true;

        }
        return false;
    }

    public void DisplayContact(String[] c) {

        final String [] id =new String[c.length];
        final String [] poster = new String[c.length];
        final String [] title = new String[c.length];
        final String [] date = new String[c.length];
        final String [] vote = new String[c.length];
        final String [] desc = new String[c.length];
        final int [] intarray = new int[c.length];
        int ids = 0;
        for(int i = 0 ; i<c.length;i++)
        {
            String[] parts = c[i].split(">");
            id[i] = parts[0]; // 004
            ids = Integer.parseInt(id[i]);
            intarray[i] = ids;
            title[i]=parts[1];
            date[i] = parts[2]; // 034556
            poster[i]=parts[3];
            vote[i] = parts[4];
            desc[i]=parts[5];
        }

        final String[] myid = {null};

        final FavDisplay adubter = new FavDisplay(getActivity(), poster);
        gridView.setAdapter(adubter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), DetailActivity.class);

                intent.putExtra("overview", desc[i]);
                Log.v("aa", desc[i]);
                intent.putExtra("date", date[i]);
                Log.v("bb", date[i]);
                intent.putExtra("vote", vote[i]);
                Log.v("cc", vote[i]);
                intent.putExtra("title", title[i]);
                Log.v("dd", title[i]);
                intent.putExtra("imgpath", poster[i]);
                Log.v("ee", poster[i]);
                StringBuilder sb = new StringBuilder();
                sb.append(intarray[i]);
                myid[0] = sb.toString();
                intent.putExtra("id",myid[0]);
                Log.v("ff", myid[0]);
                // Log.v("kk", String.valueOf(id[i]));
               // startActivity(intent);
                mCallbacks.movieSelected(myid[0],title[i],desc[i],date[i], poster[i],vote[i]);


            }
        });
    }


    class MovieTask extends AsyncTask<String, String, String []>
    {
        @Override
        protected String [] doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String id = null;
            String title = null;
            String overview = null;
            String release_date = null;
            String poster_path = null;
            String vote_average = null;
            List<String> list1  = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                String finaljsonv = buffer.toString();
                JSONObject parent = new JSONObject(finaljsonv);
                JSONArray jsonArray = parent.getJSONArray("results");
                int i =  jsonArray.length();
                String [] video = new String[i];
                for (int x = 0 ; x<jsonArray.length() ; x++) {
                    JSONObject finaldata = jsonArray.getJSONObject(x);
                    id = finaldata.getString("id");
                    title = finaldata.getString("title");
                    overview = finaldata.getString("overview");
                    release_date = finaldata.getString("release_date");
                    poster_path = finaldata.getString("poster_path");
                    vote_average = finaldata.getString("vote_average");

                    video[x] = id+">"+title+">"+overview+">"+release_date+">"+poster_path+">"+vote_average;


                }

                return video;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }finally {

                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader !=null)
                    { reader.close();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String [] strings) {
            super.onPostExecute(strings);

            final  String [] ids =new String[strings.length];
            final String [] titles =new String[strings.length];
            final String []  overviews =new String[strings.length];
            final String [] release_dates = new String[strings.length];
            final String []  poster_paths = new String[strings.length];
            final String [] vote_averages = new String[strings.length];
            for(int z = 0 ; z<strings.length ;z++) {
                String[] parts = strings[z].split(">");
                ids[z] = parts[0]; // 004
                titles[z] = parts[1]; // 034556
                overviews[z] = parts[2];
                release_dates[z]= parts[3];
                poster_paths[z] = parts[4];
                vote_averages[z] = parts[5];



            }

            final ImageAdapter adapter = new ImageAdapter(getActivity(),poster_paths);

            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    Intent intent = new Intent(getActivity(), DetailActivity.class);
//                    intent.putExtra("id", ids[position]);
//                    intent.putExtra("title",titles[position]);
//                    intent.putExtra("overview",overviews[position]);
//                    intent.putExtra("date",release_dates[position]);
//                    intent.putExtra("imgpath", poster_paths[position]);
//                    intent.putExtra("vote", vote_averages[position]);
//                    startActivity(intent);

                    mCallbacks.movieSelected(ids[position],titles[position],overviews[position],release_dates[position], poster_paths[position],vote_averages[position]);

                }
            });

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = null;
    }
}
