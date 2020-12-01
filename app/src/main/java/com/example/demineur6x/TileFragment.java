package com.example.demineur6x;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

public class TileFragment extends Fragment {

    private static final String ARG_PARAM1 = "x";
    private static final String ARG_PARAM2 = "y";

    private Boolean _isBomb = false;
    private int _nearbyBombs = 0;
    private int _x = -1;
    private int _y = -1;
    private ImageView imageViewForeground = null;
    private TextView textViewNb = null;

    public TileFragment() {
        // Required empty public constructor
    }

    public static TileFragment newInstance(int x, int y) {
        TileFragment fragment = new TileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, x);
        args.putInt(ARG_PARAM2, y);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _x = getArguments().getInt(ARG_PARAM1);
            _y = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        imageViewForeground = getView().findViewById(R.id.imageViewForeground);
        textViewNb = getView().findViewById(R.id.textViewNb);
        textViewNb.setText("  " + (_isBomb ? "B" : (_nearbyBombs == 0 ? "":_nearbyBombs)));
        imageViewForeground.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imageViewForeground.setVisibility(View.INVISIBLE);
            }
        });
    }

    public Boolean getBomb(){
        return _isBomb;
    }

    public void setBomb(Boolean set){
        _isBomb = set;
    }

    public int getNearbyBombs(){
        return _nearbyBombs;
    }

    public void setNearbyBombs(int nb){
        _nearbyBombs = nb;
    }
}