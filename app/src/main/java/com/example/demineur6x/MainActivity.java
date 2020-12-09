package com.example.demineur6x;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.media.TimedText;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TileFragment[][] _tileArray = new TileFragment[5][5];
    private Button start;
    private ProgressBar timer;
    private RadioGroup TimeChoice;
    private TextView TimeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer= (ProgressBar) findViewById(R.id.timer);
        start= (Button) findViewById(R.id.go);
        TimeChoice = findViewById(R.id.TimeGroup);
        TimeLeft=findViewById(R.id.TimeText);

        timer.setProgress(0);
        timer.setVisibility(View.GONE);
        TimeLeft.setVisibility(View.GONE);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeChoice.setVisibility(View.GONE);
                timer.setVisibility(View.VISIBLE);
                timer.setProgress(0);
                switch (v.getId()){
                    case R.id.go:
                        new Calculation().execute();
                        break;
                }
            }
        };
        start.setOnClickListener(listener);
/*
        FragmentManager fm = getSupportFragmentManager();
        TileFragment fragment;

        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout00, fragment).commit();
        _tileArray[0][0] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout01, fragment).commit();
        _tileArray[0][1] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout02, fragment).commit();
        _tileArray[0][2] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout03, fragment).commit();
        _tileArray[0][3] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout04, fragment).commit();
        _tileArray[0][4] = fragment;

        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout10, fragment).commit();
        _tileArray[1][0] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout11, fragment).commit();
        _tileArray[1][1] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout12, fragment).commit();
        _tileArray[1][2] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout13, fragment).commit();
        _tileArray[1][3] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout14, fragment).commit();
        _tileArray[1][4] = fragment;

        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout20, fragment).commit();
        _tileArray[2][0] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout21, fragment).commit();
        _tileArray[2][1] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout22, fragment).commit();
        _tileArray[2][2] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout23, fragment).commit();
        _tileArray[2][3] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout24, fragment).commit();
        _tileArray[2][4] = fragment;

        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout30, fragment).commit();
        _tileArray[3][0] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout31, fragment).commit();
        _tileArray[3][1] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout32, fragment).commit();
        _tileArray[3][2] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout33, fragment).commit();
        _tileArray[3][3] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout34, fragment).commit();
        _tileArray[3][4] = fragment;

        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout40, fragment).commit();
        _tileArray[4][0] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout41, fragment).commit();
        _tileArray[4][1] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout42, fragment).commit();
        _tileArray[4][2] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout43, fragment).commit();
        _tileArray[4][3] = fragment;
        fragment = new TileFragment();
        fm.beginTransaction().add(R.id.frameLayout44, fragment).commit();
        _tileArray[4][4] = fragment;

        Random rand = new Random();

        setBomb(rand.nextInt(5),rand.nextInt(5));
        setBomb(rand.nextInt(5),rand.nextInt(5));
        setBomb(rand.nextInt(5),rand.nextInt(5));
 */

        createBoard(5,5);
    }

    public void setBomb(int x,int y){
        _tileArray[x][y].setBomb(Boolean.TRUE);

        try{_tileArray[x+1][y].setNearbyBombs(_tileArray[x+1][y].getNearbyBombs()+1);}catch(Exception e){}
        try{_tileArray[x-1][y].setNearbyBombs(_tileArray[x-1][y].getNearbyBombs()+1);}catch(Exception e){}
        try{_tileArray[x][y-1].setNearbyBombs(_tileArray[x][y-1].getNearbyBombs()+1);}catch(Exception e){}
        try{_tileArray[x][y+1].setNearbyBombs(_tileArray[x][y+1].getNearbyBombs()+1);}catch(Exception e){}

        // On set des tiles différentes car selon la colonne les coordonnées des tiles adjacentes sont différentes
        if(x%2==0){
            try{_tileArray[x+1][y-1].setNearbyBombs(_tileArray[x+1][y-1].getNearbyBombs()+1);}catch(Exception e){}
            try{_tileArray[x-1][y-1].setNearbyBombs(_tileArray[x-1][y-1].getNearbyBombs()+1);}catch(Exception e){}
            System.out.println("PAIR");
        } else {
            try{_tileArray[x+1][y+1].setNearbyBombs(_tileArray[x+1][y+1].getNearbyBombs()+1);}catch(Exception e){}
            try{_tileArray[x-1][y+1].setNearbyBombs(_tileArray[x-1][y+1].getNearbyBombs()+1);}catch(Exception e){}
            System.out.println("IMPAIR");
        }
    }

    public TileFragment[][] createBoard(int length, int height) {
        TileFragment[][] board = new TileFragment[length][height];
        FrameLayout[][] frameBoard = new FrameLayout[length][height];

        ConstraintLayout mainLayout = (ConstraintLayout)findViewById(R.id.MainLayout);
        ConstraintSet constraintSet = new ConstraintSet();

        for(int x=0;x<length;x++){
            for(int y=0;y<height;y++){
                frameBoard[x][y] = new FrameLayout(this);
                frameBoard[x][y].setId(x*10+y);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200,200);
                frameBoard[x][y].setLayoutParams(params);
                mainLayout.addView(frameBoard[x][y]);
            }
        }

        constraintSet.clone(mainLayout);

        for(int x=0;x<length;x++){
            for(int y=0;y<height;y++) {
                if (x % 2 == 0){
                    if(y==0) {
                        try {constraintSet.connect(frameBoard[x][y].getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 0); } catch (Exception e) {}
                        try {constraintSet.connect(frameBoard[x][y].getId(), ConstraintSet.LEFT, frameBoard[x - 1][y].getId(), ConstraintSet.RIGHT, 0);} catch (Exception e) {}
                    }
                    else {
                        try {constraintSet.connect(frameBoard[x][y].getId(), ConstraintSet.TOP, frameBoard[x][y - 1].getId(), ConstraintSet.BOTTOM, 0);} catch (Exception e) {}
                        try {constraintSet.connect(frameBoard[x][y].getId(), ConstraintSet.LEFT, frameBoard[x - 1][y - 1].getId(), ConstraintSet.RIGHT, 0);} catch (Exception e) {}
                    }
                    try{constraintSet.connect(frameBoard[x][y].getId(), ConstraintSet.LEFT, frameBoard[x-1][y-1].getId(), ConstraintSet.RIGHT, 0);}catch(Exception e){}

                } else {
                    try{constraintSet.connect(frameBoard[x][y].getId(), ConstraintSet.LEFT, frameBoard[x-1][y].getId(), ConstraintSet.RIGHT, 0);}catch(Exception e){}
                    if(y==0)
                        try{constraintSet.connect(frameBoard[x][y].getId(),ConstraintSet.TOP,mainLayout.getId(),ConstraintSet.TOP,75);}catch(Exception e){}
                    else
                        try{constraintSet.connect(frameBoard[x][y].getId(),ConstraintSet.TOP,frameBoard[x][y-1].getId(),ConstraintSet.BOTTOM,0);}catch(Exception e){}
                }
                constraintSet.applyTo(mainLayout);
            }
        }


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tm = fm.beginTransaction();

        for(int x=0;x<length;x++){
            for(int y=0;y<height;y++){
                board[x][y] = new TileFragment();
                tm.add(frameBoard[x][y].getId(), board[x][y]);
                board[x][y] = new TileFragment();
            }
        }
        tm.commitNow();
        return board;
    }

    public class Calculation extends AsyncTask<Void,Integer,Void> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            timer.setProgress(0);
        }
        @Override
        protected void onProgressUpdate(Integer...values){
            super.onProgressUpdate();
            timer.setProgress(values[0]);
        }
        @Override
        protected Void doInBackground(Void...params){
            int Time = TimeChoice.indexOfChild(findViewById(TimeChoice.getCheckedRadioButtonId()));
            int time=0;
            switch (Time){
                case 0:
                    time=3;
                    break;
                case 1:
                    time=5;
                    break;
                case 2:
                    time=8;
                    break;
            }
            for(int i=0;i<=time;i++){
                try{
                    Thread.sleep(time*100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                publishProgress(i*100/time);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            timer.setVisibility(View.GONE);
            TimeLeft.setText("TEMPS ECOULE");
            TimeLeft.setBackgroundColor(getResources().getColor(R.color.colorRed));
            TimeLeft.setTextColor(getResources().getColor(R.color.colorWhite));
            TimeLeft.setVisibility(View.VISIBLE);
        }
    }
}