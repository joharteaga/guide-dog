package com.cs4351.guidedogtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReaderActivity extends AppCompatActivity {
    private Button readerReturnMenu;
    private Button light;
    private Button ocr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        //setup buttons
        readerReturnMenu = (Button) findViewById(R.id.readerReturn);
        light = (Button) findViewById(R.id.lightButton);
        ocr = (Button) findViewById(R.id.ocrButton);

        //if return to main menu button pressed
        readerReturnMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // setup main menu activity
                Intent intent = new Intent(ReaderActivity.this, MainMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish();
            }
        });
    }
}
