package com.example.demineur6x;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();

        CaseFragment fragment = new CaseFragment();
        fm.beginTransaction().add(R.id.frameLayout00, fragment).commit();

        fragment = new CaseFragment();
        fm.beginTransaction().add(R.id.frameLayout01, fragment).commit();

        fragment = new CaseFragment();
        fm.beginTransaction().add(R.id.frameLayout02, fragment).commit();

        fragment = new CaseFragment();
        fm.beginTransaction().add(R.id.frameLayout10, fragment).commit();

        fragment = new CaseFragment();
        fm.beginTransaction().add(R.id.frameLayout11, fragment).commit();

        fragment = new CaseFragment();
        fm.beginTransaction().add(R.id.frameLayout12, fragment).commit();

    }
}