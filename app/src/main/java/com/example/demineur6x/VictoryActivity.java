package com.example.demineur6x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.Templates;

public class VictoryActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    List<Long> listScores;

    TextView difficultyText;
    TextView score;
    ListView timeList;
    Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        difficultyText = findViewById(R.id.difficultyText);

        listScores = new ArrayList<Long>();

        //Retrieve difficulty and game length
        Intent intent = getIntent();
        long gameLength = intent.getLongExtra("LENGTH",0);
        int difficulty = intent.getIntExtra("DIFFICULTY", 0);

        switch (difficulty){ //get scoreboard for the corresponding difficulty
            case 0:
               prefs = getSharedPreferences("ScoresEasy", MODE_PRIVATE);
               difficultyText.setText("Mode Facile");
               break;
            case 1:
                prefs = getSharedPreferences("ScoresNormal", MODE_PRIVATE);
                difficultyText.setText("Mode Normal");
                break;
            case 2:
                prefs = getSharedPreferences("ScoresHard", MODE_PRIVATE);
                difficultyText.setText("Mode Difficile");
                break;
            default:
                prefs = getSharedPreferences("ScoresEasy", MODE_PRIVATE);
                difficultyText.setText("Mode Facile");
                break;
        }
        //Populate the score list with the shared setting scoreboard with some default value if there is no scores yet
        listScores.add(prefs.getLong("SCORE0",25000));
        listScores.add(prefs.getLong("SCORE1",30000));
        listScores.add(prefs.getLong("SCORE2",35000));
        listScores.add(prefs.getLong("SCORE3",40000));
        listScores.add(prefs.getLong("SCORE4",45000));
        listScores.add(prefs.getLong("SCORE5",50000));
        listScores.add(prefs.getLong("SCORE6",55000));
        listScores.add(prefs.getLong("SCORE7",60000));
        listScores.add(prefs.getLong("SCORE8",65000));
        listScores.add(prefs.getLong("SCORE9",70000));

        //We add the user's score to the list, sort it and keep the 10 bests
        listScores.add(gameLength);
        Collections.sort(listScores);

        listScores.remove(10);
        List<String> displayList= new ArrayList<>();

        //We format the score (time) to be easily readable
        for(Long score : listScores){
            String text = String.valueOf((int)(score/1000))+':'+score%1000;
            displayList.add(text);
        }

        //We update our shared setting with the new top 10 best times
        editor = prefs.edit();
        editor.putLong("SCORE0", listScores.get(0));
        editor.putLong("SCORE1", listScores.get(1));
        editor.putLong("SCORE2", listScores.get(2));
        editor.putLong("SCORE3", listScores.get(3));
        editor.putLong("SCORE4", listScores.get(4));
        editor.putLong("SCORE5", listScores.get(5));
        editor.putLong("SCORE6", listScores.get(6));
        editor.putLong("SCORE7", listScores.get(7));
        editor.putLong("SCORE8", listScores.get(8));
        editor.putLong("SCORE9", listScores.get(9));
        editor.apply();

        timeList = findViewById(R.id.timeList);
        menu = findViewById(R.id.menu);
        score = findViewById(R.id.score);

        //If the value of the game length correspond to the max value of a long, we detect loose
        if(gameLength==Long.MAX_VALUE) score.setText("Perdu");
        //Else we show the game length
        else{
        String text = String.valueOf((int)(gameLength/1000))+':'+String.valueOf(gameLength%1000);
        score.setText(text);}

        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,displayList);
        timeList.setAdapter(adapter);

        //Button to go back to the menu
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating and initializing an Intent object
                Intent intent = new Intent(v.getContext(), Menu.class);
                //Going back to the menu
                startActivity(intent);
            }
        });
    }
}