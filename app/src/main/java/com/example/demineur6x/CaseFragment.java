package com.example.demineur6x;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CaseFragment extends Fragment {

    private static final String ARG_PARAM1 = "x";
    private static final String ARG_PARAM2 = "y";

    private Boolean _isBomb = false;
    private int _nearbyBombs = 0;
    private int _x = -1;
    private int _y = -1;
    private ImageView imageViewForeground = null;


    public CaseFragment() {
        // Required empty public constructor
    }

    public static CaseFragment newInstance(int x, int y) {
        CaseFragment fragment = new CaseFragment();
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
        return inflater.inflate(R.layout.fragment_case, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        imageViewForeground = getView().findViewById(R.id.imageViewForeground);

        imageViewForeground.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imageViewForeground.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setNearbyBombs(){

    }

    public Boolean getBomb(){
        return _isBomb;
    }

    public void setBomb(Boolean _set){
        _isBomb = _set;
    }
}