package com.example.demineur6x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class Menu extends AppCompatActivity {

    private RadioGroup bombeChoice;
    private RadioGroup sizeChoice;
    private Button startButton;
    private Button easyButton;
    private Button normalButton;
    private Button hardButton;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bombeChoice = findViewById(R.id.bombGroup);
        sizeChoice = findViewById(R.id.sizeGroup);
        startButton = findViewById(R.id.startButton);

        easyButton = findViewById(R.id.easyButton);
        normalButton = findViewById(R.id.normalButton);
        hardButton = findViewById(R.id.hardButton);

        //Select the default radiobuttons
        bombeChoice.check(R.id.radioButton3);
        sizeChoice.check(R.id.radioButton55);

        //Play some background music
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music_loop2);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //When clicking on start retreive the selected settings and start the custom game
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nbBombes = 0;
                int length = 0;
                int height = 0;

                int bomb = bombeChoice.indexOfChild(findViewById(bombeChoice.getCheckedRadioButtonId()));
                switch (bomb){
                    case 0:
                        nbBombes=3;
                        break;
                    case 1:
                        nbBombes=5;
                        break;
                    case 2:
                        nbBombes=7;
                        break;
                }

                int size = sizeChoice.indexOfChild(findViewById(sizeChoice.getCheckedRadioButtonId()));
                switch (size){
                    case 0:
                        length=5;
                        height=5;
                        break;
                    case 1:
                        length=6;
                        height=6;
                        break;
                    case 2:
                        length=6;
                        height=7;
                        break;
                }

                //creating and initializing an Intent object with the settings selected
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                intent.putExtra("NBOMBS", nbBombes);
                intent.putExtra("LENGTH", length);
                intent.putExtra("HEIGHT", height);
                intent.putExtra("CUSTOM", true);

                //starting the game with the settings
                startActivity(intent);
            }
        });

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nbBombes = 3;
                int length = 6;
                int height = 7;

                //creating and initializing an Intent object with the settings selected
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                intent.putExtra("NBOMBS", nbBombes);
                intent.putExtra("LENGTH", length);
                intent.putExtra("HEIGHT", height);
                intent.putExtra("CUSTOM", false);
                intent.putExtra("DIFFICULTY", 0);

                //starting the game with the settings
                startActivity(intent);
            }
        });

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nbBombes = 5;
                int length = 6;
                int height = 7;

                //creating and initializing an Intent object with the settings selected
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                intent.putExtra("NBOMBS", nbBombes);
                intent.putExtra("LENGTH", length);
                intent.putExtra("HEIGHT", height);
                intent.putExtra("CUSTOM", false);
                intent.putExtra("DIFFICULTY", 1);

                //starting the game with the settings
                startActivity(intent);
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nbBombes = 5;
                int length = 5;
                int height = 5;

                //creating and initializing an Intent object with the settings selected
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                intent.putExtra("NBOMBS", nbBombes);
                intent.putExtra("LENGTH", length);
                intent.putExtra("HEIGHT", height);
                intent.putExtra("CUSTOM", false);
                intent.putExtra("DIFFICULTY", 2);

                //starting the game with the settings
                startActivity(intent);
            }
        });

    }
}