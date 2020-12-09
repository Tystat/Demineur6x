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

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TileFragment[][] _tileArray;
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

        Random rand = new Random();
/*
        setBomb(rand.nextInt(5),rand.nextInt(5));
        setBomb(rand.nextInt(5),rand.nextInt(5));
        setBomb(rand.nextInt(5),rand.nextInt(5));
 */
        _tileArray = createBoard(6,6);
        setBomb(3,3);

    }

    public void setBomb(int x,int y){
        _tileArray[x][y].setBomb(Boolean.TRUE);

        int[][] neighbors = getNeighborhood(x,y);

        for(int[] neighbor:neighbors) {
            if((neighbor[0]>=0 && neighbor[0]<_tileArray.length) && (neighbor[1]>=0 && neighbor[1]<_tileArray[0].length))
                _tileArray[neighbor[0]][neighbor[1]].setNearbyBombs(_tileArray[neighbor[0]][neighbor[1]].getNearbyBombs()+1);
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

    public void NTile(int x, int y){
        int[][] neighbors = getNeighborhood(x,y);

        for(int[] neighbor:neighbors){
            if((neighbor[0]>=0 && neighbor[0]<_tileArray.length) && (neighbor[1]>=0 && neighbor[1]<_tileArray[0].length)){
                if (!_tileArray[neighbor[0]][neighbor[1]].getCheck())
                    _tileArray[neighbor[0]][neighbor[1]].ClickImage();
            }
        }
    }

    public int[][] getNeighborhood(int x,int y){
        int[][] neighborhoodCoords = new int[6][2];
        neighborhoodCoords[0][0]=x+1;
        neighborhoodCoords[0][1]=y;
        neighborhoodCoords[1][0]=x-1;
        neighborhoodCoords[1][1]=y;
        neighborhoodCoords[2][0]=x;
        neighborhoodCoords[2][1]=y-1;
        neighborhoodCoords[3][0]=x;
        neighborhoodCoords[3][1]=y+1;

        if(x%2==0){
            neighborhoodCoords[4][0]=x+1;
            neighborhoodCoords[4][1]=y-1;
            neighborhoodCoords[5][0]=x-1;
            neighborhoodCoords[5][1]=y-1;
        } else {
            neighborhoodCoords[4][0]=x+1;
            neighborhoodCoords[4][1]=y+1;
            neighborhoodCoords[5][0]=x-1;
            neighborhoodCoords[5][1]=y+1;
        }
        return(neighborhoodCoords);
    }
}