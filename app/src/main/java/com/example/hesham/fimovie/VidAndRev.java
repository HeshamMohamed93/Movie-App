package com.example.hesham.fimovie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
 * Created by hesham on 09/10/15.
 */
public class VidAndRev extends MainActivityFragment {
    //Context mycontext = getContext();

    String mvideo = null;
    int counter = 0 ;
    String mmvideo = null;
    //String onelink = null;
    String donevideos [] = null;
    DBAdapter db = new DBAdapter(getActivity());

    public String [] getmydata (String [] fullvideodata)
    {
        donevideos  = fullvideodata;
        Log.v("cc",fullvideodata[0]);
        /*String DATA = fulldata;
        String id = null;
        String link = null;
        String name = null;
        String[] parts = DATA.split(">");
        id = parts[0]; // 004
        link = parts[1]; // 034556
        name = parts[2];*/
        return donevideos;


    }
    public String [] getfinal ()
    {
        //Log.v("hh",donevideos[0]);

        return donevideos;
    }

    public  void Videoandre (String  x)
    {

        mmvideo = x;
       /* try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long id = db.insertContact("hesham","mohamed","saudi", "hesham", "hesham", "hesham");
        db.close();*/


        mvideo = "http://api.themoviedb.org/3/movie/"+mmvideo+"/videos?api_key=50bce7edbac33ed4f4c7757ee875026d";
            Log.v("bbb", mvideo);
            jsonve getvideos = new jsonve();

       // String [] videos =    getvideos.execute(mvideo).get();


        }





    class jsonve extends AsyncTask<String, String, String []>
    {
        //private Context mContext=this ;

       /* public jsonve(Context mContext) {
            this.mContext = mContext;
        }*/

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



        @Override
        protected void onPostExecute(String [] strings) {
            super.onPostExecute(strings);
            final  String [] names =new String[strings.length];
            final String [] links =new String[strings.length];
            String getdatanow = null;
           // VidAndRev getit = new VidAndRev();
           // getit.getmydata(strings);
            Intent intent = new Intent(getActivity(), DetailActivity.class);

            intent.putExtra("videos", strings);
            /*intent.putExtra("title",titles[position]);
            intent.putExtra("overview",overviews[position]);
            intent.putExtra("date",release_dates[position]);
            intent.putExtra("imgpath", poster_paths[position]);
            intent.putExtra("vote", vote_averages[position]);*/

            startActivity(intent);
            for(int z = 0 ; z<strings.length ;z++) {
                String[] parts = strings[z].split(">");
                links[z] = parts[0]; // 004
                names[z] = parts[1]; // 034556

               /* Log.v("uu", links[z]);
                 Log.v("vv", names[z]);*/
               /*String currentID = mmvideo[counter];
                getdatanow = currentID+">"+links[z]+">"+names[z];

                getmydata(getdatanow);*/
                //Log.v("gg",currentID+counter+links[z]+names[z]);
                //Log.v("cc", String.valueOf(counter));
                /*VR_DB_Adapter vdb = new VR_DB_Adapter();
                try {
                    vdb.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                long id = vdb.insertContact(mmvideo[counter],names[z],links[z]);
               // Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                vdb.close();*/



            }
            //counter++;


        }
    }
}
