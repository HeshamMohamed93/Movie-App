package com.example.hesham.fimovie;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
//import android.app.Fragment;

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
public class DetailActivityFragment extends Fragment {


    ImageView img;
    TextView title1 ;
    TextView date1 ;
    TextView desc1 ;

    public DetailActivityFragment() {
    }
    View view ;
    String imgo = null;

    //@TargetApi(Build.VERSION_CODES.MARSHMALLOW)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_detail, container, false);
        final Intent intent = getActivity().getIntent();

        if (intent != null
                && intent.hasExtra("id")
                &&intent.hasExtra("title")
                &&intent.hasExtra("overview")
                &&intent.hasExtra("date")
                &&intent.hasExtra("imgpath")
                &&intent.hasExtra("vote")) {

            img = (ImageView) view.findViewById(R.id.image1);
            Log.v("eee", intent.getStringExtra("imgpath"));
            String uri = "http://image.tmdb.org/t/p/w185/" +intent.getStringExtra("imgpath") ;

            Picasso.with(getActivity()).load(uri).into(img);

            ((TextView) view.findViewById(R.id.title))
                    .setText(intent.getStringExtra("title"));
            Log.v("aaa", intent.getStringExtra("title"));
            Log.v("xxx", intent.getStringExtra("vote"));
           // Log.v("ids",intent.getStringExtra("id"));

            float f = Float.parseFloat(intent.getStringExtra("vote"));
            ((RatingBar) view.findViewById(R.id.rate))
                    .setRating(f);
            ((TextView) view.findViewById(R.id.date))
                    .setText(intent.getStringExtra("date"));
            ((TextView) view.findViewById(R.id.overview))
                    .setText(intent.getStringExtra("overview"));

            Button button1 = (Button)view.findViewById(R.id.btn);
            final DBAdapter db = new DBAdapter(getActivity());
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        db.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    long id = db.insertContact(intent.getStringExtra("id"), intent.getStringExtra("title"), intent.getStringExtra("date"), intent.getStringExtra("imgpath"), intent.getStringExtra("vote"), intent.getStringExtra("overview"));
                    Log.v("X", String.valueOf(id));
                    Toast.makeText(getActivity().getBaseContext(), "Done", Toast.LENGTH_LONG).show();
                    db.close();

                }
            });
            Button buttonvideo = (Button)view.findViewById(R.id.btn2);
            buttonvideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vid = "http://api.themoviedb.org/3/movie/"+intent.getStringExtra("id")+"/videos?api_key=50bce7edbac33ed4f4c7757ee875026d";
                    new jsonve().execute(vid);
                }
            });
            Button buttonreviews = (Button)view.findViewById(R.id.btn3);
            buttonreviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String reviews = "http://api.themoviedb.org/3/movie/"+intent.getStringExtra("id")+"/reviews?api_key=50bce7edbac33ed4f4c7757ee875026d";
                    new jsonre().execute(reviews);
                }
            });




            /***********************************************************************/


        }
        return view;
    }

    class jsonve extends AsyncTask<String, String, String []> {
        @Override
        protected String [] doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String key = null;
            String name = null;
            String site = null;
            String [] Vid = new String[3];
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
                // Log.v ("xx" , String.valueOf(i));
                for (int x = 0 ; x<jsonArray.length() ; x++) {
                    JSONObject finaldata = jsonArray.getJSONObject(x);
                    key = "https://www.youtube.com/watch?v="+finaldata.getString("key");
                    name = finaldata.getString("name");
                    site = finaldata.getString("site");

                    video[x] = key+">"+name+">"+site;

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
        public String[] getvid (String [] strings)
        {
            return strings;
        }

        @Override
        protected void onPostExecute(String [] strings) {

            super.onPostExecute(strings);
            final  String [] names =new String[strings.length];
            final String [] links =new String[strings.length];
            for(int z = 0 ; z<strings.length ;z++) {
                String[] parts = strings[z].split(">");
                links[z] = parts[0]; // 004
                names[z] = parts[1]; // 034556
                String part3 = parts[2];
                //Log.v("uu", part1);
                // Log.v("vv", part3);

            }

            ListView listView;
            listView = (ListView) view.findViewById(R.id.list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_list_item_1, names);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String x = links[position];
                    //Log.v("pp" , x);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(x));
                    startActivity(browserIntent);
                }
            });
        }
    }

    class jsonre extends AsyncTask<String, String, String[]> {
        String jsonstring = "";
        String result = "";

        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String author = null;
            List<String> list1 = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finaljsonv = buffer.toString();
                JSONObject parent = new JSONObject(finaljsonv);
                JSONArray jsonArray = parent.getJSONArray("results");
                int i = jsonArray.length();
                String[] review = new String[i];
                // Log.v ("xx" , String.valueOf(i));
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject finaldata = jsonArray.getJSONObject(x);
                    author = finaldata.getString("author");
                    String content = finaldata.getString("content");
                    review[x] = author + "\n" + content;

                }

                // Log.v("mm", String.valueOf(review));
                return review;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        //@Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            // Log.v("TT", String.valueOf(result));
            ListView listView;
            if(result!=null) {

                listView = (ListView) view.findViewById(R.id.list1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, result);
                listView.setAdapter(adapter);
            }else{ listView = (ListView) view.findViewById(R.id.list1);
                TextView emptyText = (TextView)view.findViewById(android.R.id.empty);
                listView.setEmptyView(emptyText);}

        }
    }

    public void updateUI(final String movie , final String title , final String overview , final String date , final String poster , final String vote){

        img = (ImageView) view.findViewById(R.id.image1);
        Log.v("poster", poster);
        String uri = "http://image.tmdb.org/t/p/w185/" +poster ;

       title1 = (TextView) view.findViewById(R.id.title);
               title1.setText(title);
        float f = Float.parseFloat(vote);
        ((RatingBar) view.findViewById(R.id.rate))
                .setRating(f);
        date1 = (TextView) view.findViewById(R.id.date);
        date1.setText(date);

        desc1 = (TextView) view.findViewById(R.id.overview);
        date1.setText(overview);


        /********************************************************/
        Button button1 = (Button)view.findViewById(R.id.btn);
        final DBAdapter db = new DBAdapter(getActivity());
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                long id = db.insertContact(movie, title, date, poster, vote, overview);
                Log.v("X", String.valueOf(id));
                Toast.makeText(getActivity().getBaseContext(), "Done", Toast.LENGTH_LONG).show();
                db.close();

            }
        });
        /*******************************************************************************/



        Picasso.with(getActivity()).load(uri).into(img);
        String vid = "http://api.themoviedb.org/3/movie/"+movie+"/videos?api_key=50bce7edbac33ed4f4c7757ee875026d";
        new jsonve().execute(vid);

        String reviews = "http://api.themoviedb.org/3/movie/"+movie+"/reviews?api_key=50bce7edbac33ed4f4c7757ee875026d";
        new jsonre().execute(reviews);


    }
}
