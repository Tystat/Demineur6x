package com.example.demineur6x;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private Boolean _check = false;
    private Boolean _flagged = false;
    private Boolean _longPressing = false;

    public TileFragment() {
        // Required empty public constructor
    }

    public static TileFragment newInstance(int param1, int param2) {
        TileFragment fragment = new TileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
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
        textViewNb = getView().findViewById(R.id.textViewNb);
        textViewNb.setText("  " + (_isBomb ? "B" : (_nearbyBombs == 0 ? "":_nearbyBombs)));

        // Disable click on transparent parts and handle press events
        imageViewForeground = getView().findViewById(R.id.imageViewForeground);
        imageViewForeground.setDrawingCacheEnabled(true);
        imageViewForeground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v){
                _longPressing = true;
                if(_flagged){
                    _flagged = false;
                    imageViewForeground.setImageDrawable(getResources().getDrawable(R.drawable.tile_foreground));
                } else {
                    _flagged = true;
                    imageViewForeground.setImageDrawable(getResources().getDrawable(R.drawable.tile_foreground_flagged));
                }
                return true;
            }
        });
        imageViewForeground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
                int color = 0;
                try {
                    color = bmp.getPixel((int) event.getX(), (int) event.getY());
                } catch (Exception e) {}
                if (color == Color.TRANSPARENT) {
                    return true;
                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_OUTSIDE:
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_SCROLL:
                            break;
                        case MotionEvent.ACTION_UP:
                            if(_longPressing)
                                _longPressing=false;
                            else
                                ClickImage();
                            return true;
                        default:
                            break;
                    }
                    return false;

                }
            }
        });
    }

    public void ClickImage(){
        if(!_flagged){
            imageViewForeground = getView().findViewById(R.id.imageViewForeground);
            imageViewForeground.setVisibility(View.INVISIBLE);
            _check=true;
            if(_nearbyBombs<=0 && !_isBomb){
                ((MainActivity)getActivity()).NTile(_x,_y);
            }
        }
    }

    public Boolean getCheck(){
        return _check;
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