package com.example.hesham.fimovie;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements MainActivityFragment.Callbacks {

    MainActivityFragment mFragment = new MainActivityFragment();

    protected int getResID(){
        return R.layout.activity_master_detail;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResID());


        if (savedInstanceState != null){
            mFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag("FFDF");
        }else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainfragment, mFragment, "FFDF")
                    .commit();

        }if(findViewById(R.id.details_frame) == null) {

        }else{
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.details_frame, new DetailActivityFragment(), "DFTT"
            ).commit();

        }

    }

    public void movieSelected(String movie  , String title , String overview , String date , String poster , String vote) {

        if(findViewById(R.id.details_frame) == null) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, movie);

            intent.putExtra("id", movie);
            intent.putExtra("title",title);
            intent.putExtra("overview",overview);
            intent.putExtra("date",date);
            intent.putExtra("imgpath", poster);
            intent.putExtra("vote", vote);
            startActivity(intent);


        }else{
            ((DetailActivityFragment)getSupportFragmentManager().
                    findFragmentByTag("DFTT")).updateUI(movie ,  title ,  overview , date ,  poster ,  vote);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
       return super.onCreateOptionsMenu(menu);
    }



    //@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return false;
        }
        else if (id == R.id.action_popular)
        {
            return false;
        }
        else if (id == R.id.action_rated)
        {
            return false;
        }


        return false;
    }
}
