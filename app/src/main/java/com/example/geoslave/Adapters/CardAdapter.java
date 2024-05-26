package com.example.geoslave.Adapters;

import static com.example.geoslave.Logic.NetworkUtil.CheckNetwork;

import android.app.Activity;
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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoslave.Formula;
import com.example.geoslave.FormulaActivity;
import com.example.geoslave.Logic.NetworkUtil;
import com.example.geoslave.MainActivity;
import com.example.geoslave.NoInternetActivity;
import com.example.geoslave.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.widget.Toast;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {


    Context context;
    List<Formula> formulas;
    Vibrator vibrator;
    Activity activity;
    LikeAdapter likeAdapter;
    ArrayList<RecyclerView> recyclerViews;
    ArrayList<CardAdapter> adapters;
    public CardAdapter(Activity activity, Context context, List<Formula> formulas, Vibrator vibrator,
                       ArrayList<RecyclerView> recyclerViews, LikeAdapter likeAdapter,ArrayList<CardAdapter> adapters) {
        this.context = context;
        this.formulas = formulas;
        this.vibrator = vibrator;
        this.activity = activity;
        this.recyclerViews = recyclerViews;
        this.likeAdapter = likeAdapter;
        this.adapters = adapters;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }
    public void updateDataList(List<Formula> newDataList) {
        this.formulas = newDataList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {
        holder.nameView.setText(formulas.get(position).getName());
        holder.imageView.setImageResource(formulas.get(position).getImage());
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNetwork(context, activity);
                Intent intent = new Intent(context, FormulaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MyFormulaImage",formulas.get(position).getImage());
                intent.putExtra("MyFormulaFormulaImage",formulas.get(position).getFImage());
                intent.putExtra("MyFormulaName",formulas.get(position).getName());
                intent.putExtra("MyFormulaURL",formulas.get(position).getURL());
                intent.putExtra("MyFormulaType",formulas.get(position).getType());
                context.startActivity(intent);
            }
        };
        holder.imageView.setOnClickListener(listener);
        holder.cardView.setOnClickListener(listener);
        holder.nameView.setTextSize(formulas.get(position).getTextSize());
        if (MainActivity.LikedFormulas.contains(formulas.get(position))){
            formulas.get(position).setLiked(true);
            holder.imageButton.setImageResource(R.drawable.heart_red);
        }
        holder.imageButton.setOnClickListener(v -> {
            CheckNetwork(context, activity);
            likeAdapter.updateDataList(MainActivity.LikedFormulas);

            if (formulas.get(position).isLiked()){
                formulas.get(position).setLiked(false);
                holder.imageButton.setImageResource(R.drawable.heart_empty);

                for (Formula likedCard: MainActivity.LikedFormulas ) {
                    if (likedCard.getName().equals(formulas.get(position).getName())){
                        MainActivity.LikedFormulas.remove(likedCard);
                        //likeAdapter.notifyItemRemoved(MainActivity.LikedFormulas.indexOf(likedCard));
                        break;
                    }
                }
                likeAdapter.updateDataList(MainActivity.LikedFormulas);

            }else{
                formulas.get(position).setLiked(true);
                animateHeart(holder.imageButton);
                VibrateDevice();

                if (!MainActivity.LikedFormulas.contains(formulas.get(position))){
                    MainActivity.LikedFormulas.add(formulas.get(position));
                }
                likeAdapter.updateDataList(MainActivity.LikedFormulas);


            }
            if(MainActivity.LikedFormulas.isEmpty()) {
                recyclerViews.get(4).setVisibility(View.GONE);
            }
            else {
                recyclerViews.get(4).setVisibility(View.VISIBLE);
            }

            MainActivity.Liked.remove("LikedFormulas");
            MainActivity.Liked.put("LikedFormulas",MainActivity.LikedFormulas);
            MainActivity.documentReference.set(MainActivity.Liked).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("TAG", "onSuccess: for user"+ MainActivity.mAuth.getCurrentUser().getEmail());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "onFailure: "+e.toString());
                }
            });
            //recyclerViews.get(4.setAdapter(new LikeAdapter(activity, context, MainActivity.LikedFormulas, recyclerViews.get(4, emptyFavs, recyclerTriangleS,vibrator));
            likeAdapter.updateDataList(MainActivity.LikedFormulas);
            recyclerViews.get(4).setAdapter(new LikeAdapter(activity, context, MainActivity.LikedFormulas, recyclerViews, vibrator, adapters,likeAdapter));
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
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.eventImage);
            nameView = itemView.findViewById(R.id.eventName);
            imageButton = itemView.findViewById(R.id.likeBtn);
            cardView = itemView.findViewById(R.id.eventCard);
        }
    }
}