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
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
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
    private ImageView boom = null;
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

        //Once the view is created we setup the tile with the number of nearby bombs or a "B" if it's a bomb
        textViewNb = getView().findViewById(R.id.textViewNb);
        textViewNb.setText(""+(_isBomb ? "B" : (_nearbyBombs == 0 ? "":_nearbyBombs)));

        // Disable click on transparent parts and manually handle press events
        imageViewForeground = getView().findViewById(R.id.imageViewForeground);
        boom = getView().findViewById(R.id.boom);

        imageViewForeground.setDrawingCacheEnabled(true);
        // If the tile is long-pressed we set a flag
        imageViewForeground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v){
                _longPressing = true;
                if(_flagged){
                    _flagged = false;
                    imageViewForeground.setImageDrawable(getResources().getDrawable(R.drawable.tile_foreground_violet));
                } else {
                    _flagged = true;
                    imageViewForeground.setImageDrawable(getResources().getDrawable(R.drawable.tile_foreground_flagged_violet));
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
                            else if(_flagged)
                                return false;
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

    //Reveal the tile and handle the event linked to its state (bomb or empty tile)
    public void ClickImage(){
        //We try to start the timer, in case this is the first tile pressed
        ((MainActivity)getActivity()).startTimer();
        //If this tile is flagged we do nothing
        if(!_flagged){
            //Reveal the tile
            imageViewForeground.setVisibility(View.INVISIBLE);
            _check=true;
            //If it was an empty tile, we call the recursive function in the main and decrease the remaining tiles
            if(_nearbyBombs<=0 && !_isBomb){
                ((MainActivity)getActivity()).decreaseRemaining();
                ((MainActivity)getActivity()).NTile(_x,_y);
            //If it was a bomb we loose
            } else if (_isBomb) {
                boom.setVisibility(View.VISIBLE);
                boomAnimation();
                ((MainActivity)getActivity()).loose();
            //If it was a tile with a number we decrease the remaining tiles
            } else {
                ((MainActivity)getActivity()).decreaseRemaining();
            }
        }
    }

    //Reveal the tile without any other actions, used when loosing to reveal the board
    public void RevealImage(){
        imageViewForeground.setVisibility(View.INVISIBLE);
    }

    public void boomAnimation() {
        Animation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); //to change visibility from visible to invisible
        animation.setDuration(200); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(3); //repeating 5 times
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        boom.startAnimation(animation); //to start animation
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