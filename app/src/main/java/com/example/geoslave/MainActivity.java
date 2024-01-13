package com.example.geoslave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.location.GnssAntennaInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));



        AREABYHERON.likeimg = findViewById(R.id.AreaByHeronLike);
        AREABYHEIGHTANDSIDE.likeimg = findViewById(R.id.AreaByHeightAndSideLike);
        AREABYTWOSIDESONEANGLE.likeimg = findViewById(R.id.AreaByTwoSidesOneAngleLike);
        AREABYONESIDETWOANGLES.likeimg = findViewById(R.id.AreaByOneSideTwoAnglesLike);
        AREABYINCIRLE.likeimg = findViewById(R.id.AreaByIncircleLike);
        AREABYEXCIRLE.likeimg = findViewById(R.id.AreaByExcircleLike);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrateDevice();
                likeFunction(v);
            }
        };
        AREABYHERON.likeimg.setOnClickListener(listener);
        AREABYHEIGHTANDSIDE.likeimg.setOnClickListener(listener);
        AREABYTWOSIDESONEANGLE.likeimg.setOnClickListener(listener);
        AREABYONESIDETWOANGLES.likeimg.setOnClickListener(listener);
        AREABYINCIRLE.likeimg.setOnClickListener(listener);
        AREABYEXCIRLE.likeimg.setOnClickListener(listener);


        AREABYHERON.card = findViewById(R.id.heroncard);
        AREABYHERON.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormulaActivity.class);
                startActivity(intent);
            }
        });
    }
    public void likeFunction(View v){
        if (v.getId() == R.id.AreaByHeronLike){
            if(AREABYHERON.isLiked){
                AREABYHERON.likeimg.setImageResource(R.drawable.heart_empty);
                AREABYHERON.isLiked=false;
            }
            else{
                AREABYHERON.isLiked=true;
                animateHeart(AREABYHERON.likeimg);
            }
        }
        else if (v.getId() == R.id.AreaByTwoSidesOneAngleLike){
         if(AREABYTWOSIDESONEANGLE.isLiked){
                AREABYTWOSIDESONEANGLE.likeimg.setImageResource(R.drawable.heart_empty);
                AREABYTWOSIDESONEANGLE.isLiked=false;
            }
            else{
                AREABYTWOSIDESONEANGLE.isLiked=true;
                animateHeart(AREABYTWOSIDESONEANGLE.likeimg);
            }
        }
        else if (v.getId() == R.id.AreaByHeightAndSideLike) {
            if(AREABYHEIGHTANDSIDE.isLiked){
                AREABYHEIGHTANDSIDE.likeimg.setImageResource(R.drawable.heart_empty);
                AREABYHEIGHTANDSIDE.isLiked=false;
            }
            else{
                AREABYHEIGHTANDSIDE.isLiked=true;
                animateHeart(AREABYHEIGHTANDSIDE.likeimg);
            }
        }
        else if (v.getId() == R.id.AreaByTwoSidesOneAngleLike) {
            if(AREABYTWOSIDESONEANGLE.isLiked){
                AREABYTWOSIDESONEANGLE.likeimg.setImageResource(R.drawable.heart_empty);
                AREABYTWOSIDESONEANGLE.isLiked=false;
            }
            else{
                AREABYTWOSIDESONEANGLE.isLiked=true;
                animateHeart(AREABYTWOSIDESONEANGLE.likeimg);
            }
        }
        else if (v.getId() == R.id.AreaByOneSideTwoAnglesLike) {
            if(AREABYONESIDETWOANGLES.isLiked){
                AREABYONESIDETWOANGLES.likeimg.setImageResource(R.drawable.heart_empty);
                AREABYONESIDETWOANGLES.isLiked=false;
            }
            else{
                AREABYONESIDETWOANGLES.isLiked=true;
                animateHeart(AREABYONESIDETWOANGLES.likeimg);
            }
        }
        else if (v.getId() == R.id.AreaByIncircleLike) {
            if(AREABYINCIRLE.isLiked){
                AREABYINCIRLE.likeimg.setImageResource(R.drawable.heart_empty);
                AREABYINCIRLE.isLiked=false;
            }
            else{
                AREABYINCIRLE.isLiked=true;
                animateHeart(AREABYINCIRLE.likeimg);
            }
        }
        else if (v.getId() == R.id.AreaByExcircleLike) {
            if(AREABYEXCIRLE.isLiked){
                AREABYEXCIRLE.likeimg.setImageResource(R.drawable.heart_empty);
                AREABYEXCIRLE.isLiked=false;
            }
            else{
                AREABYEXCIRLE.isLiked=true;
                animateHeart(AREABYEXCIRLE.likeimg);
            }
        }
    }
    private void animateHeart(ImageView imageView) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation start
                imageView.setImageResource(R.drawable.heart_red);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Change the drawable to the red heart after the animation

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeat
            }
        });

        imageView.startAnimation(scaleAnimation);
    }
    private void vibrateDevice() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Deprecated in API 26, but still necessary for older devices
                vibrator.vibrate(40);
            }
        }
    }
}