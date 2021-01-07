package com.example.demineur6x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class Menu extends AppCompatActivity {

    private RadioGroup bombeChoice;
    private RadioGroup sizeChoice;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bombeChoice = findViewById(R.id.bombGroup);
        sizeChoice = findViewById(R.id.sizeGroup);
        startButton = findViewById(R.id.startButton);

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

                //creating and initializing an Intent object
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                intent.putExtra("NBOMBS", nbBombes);
                intent.putExtra("LENGTH", length);
                intent.putExtra("HEIGHT", height);

                //starting the activity
                startActivity(intent);
            }
        });

    }
}