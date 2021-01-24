package com.example.demineur6x;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TileFragment[][] tileArray;
    private ProgressBar timer;
    private RadioGroup TimeChoice;
    private TextView TimeLeft;
    private int remainingGoodTiles=1;
    private boolean isCustom;
    private long startingTimestamp;
    private long gameLength;
    private Button continueButton;
    private int difficulty;

    public boolean timerStarted = false;
    public boolean won = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the settings set from the menu
        Intent intent = getIntent();
        int nbBombes = intent.getIntExtra("NBOMBS",0);
        int length = intent.getIntExtra("LENGTH",0);
        int height = intent.getIntExtra("HEIGHT",0);
        isCustom = intent.getBooleanExtra("CUSTOM", false);
        if(!isCustom)
            difficulty = intent.getIntExtra("DIFFICULTY", 0);
        //Setup the timer GUI
        timer = (ProgressBar) findViewById(R.id.timer);
        TimeChoice = findViewById(R.id.TimeGroup);
        TimeLeft = findViewById(R.id.TimeText);
        timer.setProgress(0);
        timer.setVisibility(View.INVISIBLE);
        TimeLeft.setVisibility(View.INVISIBLE);
        //If it is not a custom game we hide the timer
        if(!isCustom)
            TimeChoice.setVisibility(View.INVISIBLE);

        //Setup the continue button
        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isCustom) {
                    //creating and initializing an Intent object
                    Intent intent = new Intent(v.getContext(), Menu.class);
                    //Going back to the menu
                    startActivity(intent);
                } else {
                    //creating and initializing an Intent object with the settings selected
                    Intent intent = new Intent(v.getContext(),VictoryActivity.class);
                    intent.putExtra("LENGTH", gameLength);
                    intent.putExtra("DIFFICULTY", difficulty);
                    //starting the game with the settings
                    startActivity(intent);
                }
            }
        });

        //Setup the board
        Random rand = new Random();
        tileArray = createBoard(length,height);

        for(int i=0; i<nbBombes;i++) {
            //We need to be sure we don't put 2 bombs in the same tile, that's what the do-while does
            int randX;
            int randY;
            do {
                randX = rand.nextInt(length);
                randY = rand.nextInt(height);
            }
            while(tileArray[randX][randY].getBomb());
            setBomb(randX, randY);
        }
        remainingGoodTiles=(length*height)-nbBombes;
        TimeChoice.check(R.id.Time3);
    }

    //Set a bomb in the tile at coords x,y
    public void setBomb(int x,int y){


        tileArray[x][y].setBomb(true);

        //Get the neighbooring tiles and update their bomb number
        int[][] neighbors = getNeighborhood(x,y);
        for(int[] neighbor:neighbors) {
            //Check if the neighbor actually exist (useful when dealing with bordering tiles)
            if((neighbor[0]>=0 && neighbor[0]<tileArray.length) && (neighbor[1]>=0 && neighbor[1]<tileArray[0].length))
                tileArray[neighbor[0]][neighbor[1]].setNearbyBombs(tileArray[neighbor[0]][neighbor[1]].getNearbyBombs()+1);
        }
    }

    //Create the game board
    public TileFragment[][] createBoard(int length, int height) {
        //Initialize tile board and frameLayout board
        TileFragment[][] board = new TileFragment[length][height];
        FrameLayout[][] frameBoard = new FrameLayout[length][height];

        //Calculate the size of the tile, it's a bit hacky, the size was adjusted on a Pixel 2 and we
        //compare the screen of the current device to the screen of the Pixel 2 and adjust tileWidth
        //and tileHeight accordingly
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        float currentScreenDensity = metrics.densityDpi;
        float originalScreenDensity = 420;
        float densityRatio = currentScreenDensity/originalScreenDensity;

        float tileWidth = 135f*densityRatio;
        float tileHeight = 157f*densityRatio;

        //Create all the frame layouts and set them up
        ConstraintLayout mainLayout = (ConstraintLayout)findViewById(R.id.MainLayout);
        ConstraintSet constraintSet = new ConstraintSet();
        for(int x=0;x<length;x++){
            for(int y=0;y<height;y++){
                frameBoard[x][y] = new FrameLayout(this);
                frameBoard[x][y].setId(100+x*10+y);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int)tileWidth,(int)tileHeight);
                frameBoard[x][y].setClipChildren(false);
                frameBoard[x][y].setClipToPadding(false);
                frameBoard[x][y].setLayoutParams(params);
                mainLayout.addView(frameBoard[x][y]);
            }
        }

        //Set the constraints between frame layouts, we can't do that earlier as we need all frame layout to be created before constraining them
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
                        try{constraintSet.connect(frameBoard[x][y].getId(),ConstraintSet.TOP,mainLayout.getId(),ConstraintSet.TOP,(int)tileHeight/2);}catch(Exception e){}
                    else
                        try{constraintSet.connect(frameBoard[x][y].getId(),ConstraintSet.TOP,frameBoard[x][y-1].getId(),ConstraintSet.BOTTOM,0);}catch(Exception e){}
                }
                constraintSet.applyTo(mainLayout);
            }
        }


        //Creates the fragments (tiles) in the frame layouts
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

    //Handle the timer
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

            //Get the time selected
            switch (Time){
                case 0:
                    time=10;
                    break;
                case 1:
                    time=30;
                    break;
                case 2:
                    time=60;
                    break;
            }
            // Start at one because we count after the first waiting period
            for(int i=1;i<=time;i++){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                // We do this because dividing int isn't working well
                int progress = (int)(((float)i/(float)time)*100);
                publishProgress(progress);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            //Loose when the timer finish
            loose();
        }
    }

    //Recursive function to reveal null tiles when clicking on one
    public void NTile(int x, int y){
        int[][] neighbors = getNeighborhood(x,y);
        for(int[] neighbor:neighbors){
            if((neighbor[0]>=0 && neighbor[0]<tileArray.length) && (neighbor[1]>=0 && neighbor[1]<tileArray[0].length)){
                if (!tileArray[neighbor[0]][neighbor[1]].getCheck())
                    tileArray[neighbor[0]][neighbor[1]].ClickImage();
            }
        }
    }

    //Function returning an array of coordinates of the neighboring tiles
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

        //As the tiles are hexagonal we need to check if the column is odd or even
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

    //Start the timer
    public void startTimer(){
        // If we aren't in a custom game we just save the starting timestamp to calculate the length of the game
        // Else we check if the timer is started and start it if not
        if(!timerStarted) {
            timerStarted=true;
            if(!isCustom) {
                startingTimestamp = System.currentTimeMillis();
            }else {
                TimeChoice.setVisibility(View.INVISIBLE);
                timer.setVisibility(View.VISIBLE);
                timer.setProgress(0);
                new MainActivity.Calculation().execute();
            }
        }
    }

    //Reveal all tiles and show a message informing the player he lost
    public void loose(){
        //Check if we won, in case the timer finish after the player win to avoid overriding the wining message
        if(!won) {
            for (TileFragment[] tileLine : tileArray) {
                for (TileFragment tile : tileLine) {
                    tile.RevealImage();
                }
            }
            continueButton.setVisibility(View.VISIBLE);
            timer.setVisibility(View.INVISIBLE);
            TimeChoice.setVisibility(View.INVISIBLE);
            TimeLeft.setText("PERDU !");
            TimeLeft.setBackgroundColor(getResources().getColor(R.color.colorRed));
            TimeLeft.setTextColor(getResources().getColor(R.color.colorWhite));
            TimeLeft.setVisibility(View.VISIBLE);
            gameLength = Long.MAX_VALUE;
        }
    }

    //Show a message informing the player he won
    public void win(){
        won = true;
        continueButton.setVisibility(View.VISIBLE);
        if(isCustom) {
            timer.setVisibility(View.INVISIBLE);
            TimeChoice.setVisibility(View.INVISIBLE);
        } else {
            long endTime = System.currentTimeMillis();
            gameLength = endTime - startingTimestamp;
        }
        TimeLeft.setText("BRAVO !");
        TimeLeft.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        TimeLeft.setTextColor(getResources().getColor(R.color.colorWhite));
        TimeLeft.setVisibility(View.VISIBLE);
    }

    //Decrease the number of tile remaining to discover before wining
    public void decreaseRemaining(){
        remainingGoodTiles--;
        if(remainingGoodTiles==0)
            win();
    }
}