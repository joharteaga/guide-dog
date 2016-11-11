package com.cs4351.guidedogtest;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    private Button navigation;
    private Button reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //hide action bar
        getSupportActionBar().hide();

        //setup buttons
        navigation = (Button) findViewById(R.id.navigationButton);
        reader = (Button) findViewById(R.id.readerButton);

        //start clickListeners for buttons
        navigation.setOnClickListener(this);
        reader.setOnClickListener(this);


    }

    //When a button press is detected
    @Override
    public void onClick(View view) {

        //get id of button pressed
        switch (view.getId()) {

            //if navigation button pressed
            case R.id.navigationButton: {
                //setup fragment transaction
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                //create fragment
                WaiverFragment waiver = new WaiverFragment();

                //set fragment view to replace current activity view
                fragmentTransaction.replace(R.id.activity_main_menu, waiver);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                //set back press to return to main menu
                fragmentTransaction.addToBackStack(null);

                //start fragment
                fragmentTransaction.commit();
                break;
            }
            //if reader button pressed
            case R.id.readerButton: {
                //create intent to start new activity 'ReaderActivity'
                Intent readerIntent = new Intent(MainMenu.this, ReaderActivity.class);

                //start activity
                startActivity(readerIntent);
                break;
            }
        }
    }
}


