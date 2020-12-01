package com.example.demineur6x;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;

import java.security.cert.TrustAnchor;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TileFragment[][] _tileArray = new TileFragment[5][5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}