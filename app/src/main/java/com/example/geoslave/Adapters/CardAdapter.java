package com.example.geoslave.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoslave.Formula;
import com.example.geoslave.FormulaActivity;
import com.example.geoslave.MainActivity;
import com.example.geoslave.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import android.content.Intent;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {


    Context context;
    List<Formula> formulas;
    Vibrator vibrator;
    RecyclerView recyclerView;
    ConstraintLayout emptyFavs;
    public CardAdapter(Context context, List<Formula> formulas, Vibrator vibrator,RecyclerView recyclerView, ConstraintLayout emptyFavs) {
        this.context = context;
        this.formulas = formulas;
        this.vibrator = vibrator;
        this.recyclerView = recyclerView;
        this.emptyFavs = emptyFavs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {
        holder.nameView.setText(formulas.get(position).getName());
        holder.imageView.setImageResource(formulas.get(position).getImage());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FormulaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.nameView.setTextSize(formulas.get(position).getTextSize());
        holder.imageButton.setOnClickListener(v -> {
            if (formulas.get(position).isLiked()){
                formulas.get(position).setLiked(false);
                holder.imageButton.setImageResource(R.drawable.heart_empty);

                for (Formula likedCard: MainActivity.LikedFormulas ) {
                    if (likedCard.getName().equals(formulas.get(position).getName())){
                        MainActivity.LikedFormulas.remove(likedCard);
                        break;
                    }
                }
            }else{
                formulas.get(position).setLiked(true);
                animateHeart(holder.imageButton);
                VibrateDevice();

                MainActivity.LikedFormulas.add(formulas.get(position));

            }
            if(MainActivity.LikedFormulas.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyFavs.setVisibility(View.GONE);
            }
            recyclerView.setAdapter(new LikeAdapter(context, MainActivity.LikedFormulas, recyclerView, emptyFavs));

            MainActivity.Liked.put("LikedFormulas",MainActivity.LikedFormulas);
            MainActivity.documentReference.set(MainActivity.Liked).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("TAG", "onSuccess: for user"+MainActivity.userID);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "onFailure: "+e.toString());
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return formulas.size();
    }
    private void animateHeart(ImageButton imageView) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageView.setImageResource(R.drawable.heart_red);
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        imageView.startAnimation(scaleAnimation);
    }
    public void VibrateDevice() {
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                // Deprecated in API 26, but still necessary for older devices
                vibrator.vibrate(40);
            }
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView nameView;
        public ImageButton imageButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.eventImage);
            nameView = itemView.findViewById(R.id.eventName);
            imageButton = itemView.findViewById(R.id.likeBtn);
        }
    }
}
