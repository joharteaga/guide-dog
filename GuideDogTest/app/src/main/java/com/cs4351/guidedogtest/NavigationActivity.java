package com.cs4351.guidedogtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NavigationActivity extends AppCompatActivity {
    private Button returnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        returnMenu = (Button) findViewById(R.id.navigationReturn);

        //return to main menu button pressed
        returnMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // intent to return to main menu activity
                Intent intent = new Intent(NavigationActivity.this, MainMenu.class);

                //clear back history
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                //start main activity
                startActivity(intent);
                finish();
            }
        });
    }
}
