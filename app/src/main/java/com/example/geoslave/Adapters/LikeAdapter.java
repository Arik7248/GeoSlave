package com.example.geoslave.Adapters;

import static com.example.geoslave.Logic.NetworkUtil.CheckNetwork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder>{

    private Context context;
    private List<Formula> likedFormulas;
    private ArrayList<RecyclerView> recyclerViews;
    Activity activity;
    Vibrator vibrator;
    LikeAdapter likeAdapter;
    ArrayList<CardAdapter> adapters;
    public LikeAdapter(Activity activity, Context context, List<Formula> likedFormulas,
                       ArrayList<RecyclerView> recyclerViews,Vibrator vibrator, ArrayList<CardAdapter> adapters,LikeAdapter likeAdapter) {
        this.context = context;
        this.likedFormulas = likedFormulas;
        this.recyclerViews = recyclerViews;
        this.activity = activity;
        this.vibrator = vibrator;
        this.adapters = adapters;
        this.likeAdapter = likeAdapter;
    }


    @NonNull
    @Override
    public LikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LikeAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }
    public void updateDataList(List<Formula> newDataList) {
        this.likedFormulas = newDataList;
        notifyDataSetChanged();
        notifyItemInserted(likedFormulas.size());
    }

    @Override
    public void onBindViewHolder(@NonNull LikeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.nameView.setText(likedFormulas.get(position).getName());
        holder.imageView.setImageResource(likedFormulas.get(position).getImage());
        holder.nameView.setTextSize(likedFormulas.get(position).getTextSize());
        holder.imageButton.setImageResource(R.drawable.heart_red);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNetwork(context, activity);
                Intent intent = new Intent(context, FormulaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MyFormulaImage",likedFormulas.get(position).getImage());
                intent.putExtra("MyFormulaFormulaImage",likedFormulas.get(position).getFImage());
                intent.putExtra("MyFormulaName",likedFormulas.get(position).getName());
                intent.putExtra("MyFormulaURL",likedFormulas.get(position).getURL());
                intent.putExtra("MyFormulaType",likedFormulas.get(position).getType());
                context.startActivity(intent);
            }
        };
        holder.imageView.setOnClickListener(listener);
        holder.cardView.setOnClickListener(listener);
        holder.imageButton.setOnClickListener(v -> {
            updateDataList(likedFormulas);
            notifyItemRemoved(position);
            likedFormulas.get(position).setLiked(false);
            holder.imageButton.setImageResource(R.drawable.heart_empty);
            outerloop:
            for (List<Formula> listF: MainActivity.arrFormulas) {
                for (Formula formula : listF) {
                    if (formula.getName().equals(likedFormulas.get(position).getName())) {
                        formula.setLiked(false);
                        //adapters.get(MainActivity.arrFormulas.indexOf(listF)).updateDataList(new ArrayList<>());
                        adapters.set(MainActivity.arrFormulas.indexOf(listF), new CardAdapter(activity, context,
                                listF, vibrator, recyclerViews, likeAdapter,adapters));
                        recyclerViews.get(MainActivity.arrFormulas.indexOf(listF)).setAdapter(adapters.get(MainActivity.arrFormulas.indexOf(listF)));
                        updateDataList(likedFormulas);
                        //adapters.get(MainActivity.arrFormulas.indexOf(listF)).notifyItemChanged(listF.indexOf(formula));
                        //adapters.get(MainActivity.arrFormulas.indexOf(listF)).notifyDataSetChanged();
                        //adapters.get(MainActivity.arrFormulas.indexOf(listF)).notifyAll();

                        break outerloop;
                    }
                }
            }
            MainActivity.LikedFormulas.remove(likedFormulas.get(position));
            notifyItemRemoved(position);
            updateDataList(likedFormulas);
            if(MainActivity.LikedFormulas.isEmpty()) {
                recyclerViews.get(4).setVisibility(View.GONE);
            }
            else {
                recyclerViews.get(4).setVisibility(View.VISIBLE);
            }
            //recyclerTriangleS.setAdapter(new CardAdapter(activity,context,MainActivity.TrianglesFormulasS, vibrator,recyclerView,emptyFavs,recyclerTriangleS));
            //recyclerView.setAdapter(new LikeAdapter(activity, context, likedFormulas, recyclerView, emptyFavs,recyclerTriangleS,vibrator));
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
        });

    }

    @Override
    public int getItemCount() {
        return likedFormulas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameView;
        public ImageButton imageButton;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.eventImage);
            nameView = itemView.findViewById(R.id.eventName);
            imageButton = itemView.findViewById(R.id.likeBtn);
            cardView = itemView.findViewById(R.id.eventCard);
        }
    }
}
