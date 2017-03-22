package com.devsoul.dima.rock_paper_scissors.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.*;
import android.view.View;
import android.widget.*;
import com.devsoul.dima.rock_paper_scissors.R;

import java.util.Random;

public class GamePlay extends Activity {
    private ImageView imgview_comp, imgview_player;
    private int Round_Number = 0, Computer_Score = 0, Player_Score = 0;
    private TextView Round, Score, Outcome;
    private Button btn_next;
    private char winner;
    private String message;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        Round = (TextView) findViewById(R.id.Round);
        Score = (TextView) findViewById(R.id.Result);
        Outcome = (TextView) findViewById(R.id.Outcome);
        imgview_player = (ImageView) findViewById(R.id.img_player);
        imgview_comp = (ImageView) findViewById(R.id.img_computer);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to choose weapon screen and save the round number, score and if it's a multi player game
                Intent intent = new Intent(getApplicationContext(), Choose.class);
                intent.putExtra("Round", Round_Number);
                intent.putExtra("Player", Player_Score);
                intent.putExtra("Computer", Computer_Score);
                startActivity(intent);
            }
        });

        // Load round
        Round_Number = getIntent().getIntExtra("Round", 0);
        // Update Round on display
        Round_Number++;
        Round.append(" " + String.valueOf(Round_Number));
        // Underline text
        Round.setPaintFlags(Round.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Load score
        Player_Score = getIntent().getIntExtra("Player", 0);
        Computer_Score = getIntent().getIntExtra("Computer", 0);

        Play(savedInstanceState);
    }

    public void Play(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int player_weapon = intent.getIntExtra("P_Weapon", 0);
        int computer_weapon = intent.getIntExtra("C_Weapon", 0);

        // Doesn't changed the orientation
        if (savedInstanceState == null) {
            Random r = new Random();
            computer_weapon = (r.nextInt(3) + 1);
            intent.putExtra("C_Weapon", computer_weapon);
        }

        Weapon(true, player_weapon, imgview_player);
        Weapon(false, computer_weapon, imgview_comp);
        char result = Win(computer_weapon, player_weapon);
        if (result == 'w') {
            Outcome.setText("You Win");
            Player_Score++;
        } else if (result == 't')
            Outcome.setText("It's a Tie");
        else {
            Outcome.setText("You Lose");
            Computer_Score++;
        }
        // Update Score on display
        Score.setText(String.valueOf(Player_Score) + "    :    " + String.valueOf(Computer_Score));

        // End of the game
        if (EndOfGame(Player_Score, Computer_Score)) {
            btn_next.setEnabled(false);
            winner = WhoWin(Player_Score, Computer_Score);
            if (winner == 'p')
                message = "Congratulations! You win the game.";
            else
                message = "You lose the game.";

            // Show dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // back to main menu
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(intent);
                        }
                    });
            alert = builder.create();

            // Execute some code after 1 seconds have passed
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Show dialog
                    alert.show();
                }
            }, 1000);
        }
    }

    // Set the image appropriate to the weapon
    // Player = true, Computer = false
    public void Weapon(boolean player, int weapon, ImageView img) {
        if (player) {
            switch (weapon) {
                case 1:
                    img.setImageResource(R.drawable.rock);
                    break;
                case 2:
                    img.setImageResource(R.drawable.paper);
                    break;
                case 3:
                    img.setImageResource(R.drawable.scissors);
                    break;
            }
        }
        else
        {
            switch (weapon) {
                case 1:
                    img.setImageResource(R.drawable.rock_computer);
                    break;
                case 2:
                    img.setImageResource(R.drawable.paper_computer);
                    break;
                case 3:
                    img.setImageResource(R.drawable.scissors_computer);
                    break;
            }
        }
    }

    // Return 'w' if player wins computer
    // Return 'l' if player lose computer
    // Return 't' if it's tie
    public char Win(int comp_weapon, int player_weapon) {
        char outcome = 'l';

        if ((player_weapon == 1 && comp_weapon == 3) ||
                (player_weapon == 3 && comp_weapon == 2) ||
                (player_weapon == 2 && comp_weapon == 1))
            outcome = 'w';
        else if ((player_weapon == 1 && comp_weapon == 1) ||
                (player_weapon == 2 && comp_weapon == 2) ||
                (player_weapon == 3 && comp_weapon == 3))
            outcome = 't';
        return outcome;
    }

    // Return true if its end of the game, false else
    public boolean EndOfGame(int Player_Score, int Computer_Score) {
        boolean end = false;

        if (Player_Score == 3 || Computer_Score == 3)
            end = true;
        return end;
    }

    // Return who win the game,
    // 'p' for player, 'c' for computer
    public char WhoWin(int Player_Score, int Computer_Score) {
        char who = '0';

        if (Player_Score == 3)
            who = 'p';
        if (Computer_Score == 3)
            who = 'c';
        return who;
    }
}

