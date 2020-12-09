package com.example.demineur6x;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TileFragment[][] _tileArray;
    private Button start;
    private ProgressBar timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer= (ProgressBar) findViewById(R.id.timer);
        start= (Button) findViewById(R.id.go);

        timer.setProgress(0);
        timer.setVisibility(View.GONE);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        Random rand = new Random();

        setBomb(rand.nextInt(5),rand.nextInt(5));
        setBomb(rand.nextInt(5),rand.nextInt(5));
        setBomb(rand.nextInt(5),rand.nextInt(5));
 */

        _tileArray = createBoard(6,6);
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
                frameBoard[x][y].setId(100+x*10+y);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(150,175);
                frameBoard[x][y].setClipChildren(false);
                frameBoard[x][y].setClipToPadding(false);
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
                        try{constraintSet.connect(frameBoard[x][y].getId(),ConstraintSet.TOP,mainLayout.getId(),ConstraintSet.TOP,88);}catch(Exception e){}
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
                Bundle bundle = new Bundle();
                bundle.putInt("x", x);
                bundle.putInt("y", y);
                board[x][y] = new TileFragment();
                board[x][y].setArguments(bundle);
                tm.add(frameBoard[x][y].getId(), board[x][y]);
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
            for(int i=0;i<=10;i++){
                try{
                    Thread.sleep(1000L);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                publishProgress(i*10);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            timer.setVisibility(View.GONE);
        }
    }

    public void NTile(int x, int y){
        try{_tileArray[x+1][y].ClickImage();}catch(Exception e){}
        try{_tileArray[x-1][y].ClickImage();}catch(Exception e){}
        try{_tileArray[x][y-1].ClickImage();}catch(Exception e){}
        try{_tileArray[x][y+1].ClickImage();}catch(Exception e){}
        if(x%2==0){
            try{_tileArray[x+1][y-1].ClickImage();}catch(Exception e){}
            try{_tileArray[x-1][y-1].ClickImage();}catch(Exception e){}
        } else {
            try{_tileArray[x+1][y+1].ClickImage();}catch(Exception e){}
            try{_tileArray[x-1][y+1].ClickImage();}catch(Exception e){}
        }
    }
}