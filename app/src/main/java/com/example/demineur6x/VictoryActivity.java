package com.example.demineur6x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VictoryActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    List<Long> listScores;

    ListView timeList;
    Button restart;
    Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        listScores = new ArrayList<Long>();

        Intent intent = getIntent();
        long gameLength = intent.getLongExtra("LENGTH",0);
        int difficulty = intent.getIntExtra("DIFFICULTY", 0);

        switch (difficulty){
            case 0:
               prefs = getSharedPreferences("ScoresEasy", MODE_PRIVATE);
               break;
            case 1:
                prefs = getSharedPreferences("ScoresNormal", MODE_PRIVATE);
                break;
            case 2:
                prefs = getSharedPreferences("ScoresHard", MODE_PRIVATE);
                break;
            default:
                prefs = getSharedPreferences("ScoresEasy", MODE_PRIVATE);
                break;
        }
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

        listScores.add(gameLength);
        Collections.sort(listScores);

        listScores.remove(10);

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
        restart = findViewById(R.id.restart);
        menu = findViewById(R.id.menu);

        ArrayAdapter adapter = new ArrayAdapter<Long>(this,android.R.layout.simple_list_item_1,listScores);
        timeList.setAdapter(adapter);


    }
}