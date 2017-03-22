package com.devsoul.dima.rock_paper_scissors.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import com.devsoul.dima.rock_paper_scissors.R;

/**
 * Created by Dima on 7/7/2016.
 */
public class MenuActivity extends AppCompatActivity {

    private ImageButton imgbtn_single, imgbtn_multi, imgbtn_rules, imgbtn_exit;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        imgbtn_single =(ImageButton)findViewById(R.id.img_Single);
        imgbtn_multi =(ImageButton)findViewById(R.id.img_Multi);
        imgbtn_rules =(ImageButton)findViewById(R.id.img_Rules);
        imgbtn_exit =(ImageButton)findViewById(R.id.img_Exit);

        imgbtn_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Choose.class);
                startActivity(intent);
            }
        });

        imgbtn_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
                startActivity(intent);
            }
        });

        // Rules
        imgbtn_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RulesActivity.class);
                startActivity(intent);
            }
        });

        // Exit
        imgbtn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to end activity
                Intent intent = new Intent(getApplicationContext(), EndActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        return;
    }
}
