package com.devsoul.dima.rock_paper_scissors.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.devsoul.dima.rock_paper_scissors.R;

public class Choose extends Activity {
    private ImageButton imgbtn_rock, imgbtn_paper, imgbtn_scissors;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_weapon);

        imgbtn_rock =(ImageButton)findViewById(R.id.imgbtn_rock);
        imgbtn_paper =(ImageButton)findViewById(R.id.imgbtn_paper);
        imgbtn_scissors =(ImageButton)findViewById(R.id.imgbtn_scissors);

        // Set intent to game play
        intent = new Intent(getApplicationContext(), GamePlay.class);
        intent.putExtra("Round", getIntent().getIntExtra("Round", 0));
        intent.putExtra("Player", getIntent().getIntExtra("Player", 0));
        intent.putExtra("Computer", getIntent().getIntExtra("Computer", 0));

        imgbtn_rock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("P_Weapon", 1);
                startActivity(intent);
            }
        });

        imgbtn_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("P_Weapon", 2);
                startActivity(intent);
            }
        });

        imgbtn_scissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("P_Weapon", 3);
                startActivity(intent);
            }
        });
    }
}
