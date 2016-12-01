package com.cs4351.guidedogtest;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class WaiverFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Button agree;
    private Button disagree;

    public WaiverFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_waiver, container, false);

        //setup buttons
        agree = (Button) view.findViewById(R.id.agreeButton);
        disagree = (Button) view.findViewById(R.id.disagreeButton);

        //start clicklisteners
        agree.setOnClickListener(this);
        disagree.setOnClickListener(this);

        return view;
    }

    //when click detected
    @Override
    public void onClick(View v) {

        //determine button id
        switch (v.getId()) {
            case R.id.agreeButton: {
                //create intent to start navigation activity
                Intent navIntent = new Intent(getActivity(), NavigationActivity.class);

                //start activity
                startActivity(navIntent);
                break;
            }
            case R.id.disagreeButton: {
                //setup toast message
                Toast toast = Toast.makeText(getActivity(), "You must AGREE to use navigation "
                        + "feature.", Toast.LENGTH_LONG);

                //set toast message to display in center of screen (horiz/vert)
                toast.setGravity(Gravity.CENTER, 0, 0);

                //display toast
                toast.show();

                //return to main activity
                getActivity().onBackPressed();
                break;
            }
        }
    }
}
