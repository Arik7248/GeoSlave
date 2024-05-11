package com.example.geoslave.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geoslave.Formula;
import com.example.geoslave.FormulaActivity;
import com.example.geoslave.MainActivity;
import com.example.geoslave.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder>{

    private Context context;
    private List<Formula> likedFormulas;
    private LikeAdapter.onChangeListener onChangeListener;
    private RecyclerView recyclerView;
    private ConstraintLayout emptyFavs;

    public LikeAdapter(Context context, List<Formula> likedFormulas, RecyclerView recyclerView, ConstraintLayout emptyFavs) {
        this.context = context;
        this.likedFormulas = likedFormulas;
        this.recyclerView = recyclerView;
        this.emptyFavs = emptyFavs;
    }

    public interface onChangeListener {
        void onChange();
    }

    public void setOnNoItemListener(LikeAdapter.onChangeListener onChangeListener){
        this.onChangeListener = onChangeListener;
    }



    @NonNull
    @Override
    public LikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LikeAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LikeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        Glide.with(context).load(formulas.get(position).getImg_url()).into(holder.img);
//        holder.eventName.setText(likedFormulas.get(position).getName());
//        holder.eventDateTime.setText(formulas.get(position).getDate_time_list().get(0));
//
//        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MainActivity.likedCards.remove(likedFormulas.get(holder.getPosition()));
//                notifyItemRemoved(holder.getPosition());
//                onChangeListener.onChange();
//                if(MainActivity.likedCards.isEmpty()) {
//                    recyclerView.setVisibility(View.GONE);
//                    emptyFavs.setVisibility(View.VISIBLE);
//                }
//                else {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    emptyFavs.setVisibility(View.GONE);
//                }
//
//            }
//        });
//
//        holder.likedCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, FormulaActivity.class);
//                //i.putExtra("info", formulas.get(holder.getPosition()));
//                context.startActivity(i);
//            }
//        });

        holder.nameView.setText(likedFormulas.get(position).getName());
        holder.imageView.setImageResource(likedFormulas.get(position).getImage());
        holder.nameView.setTextSize(likedFormulas.get(position).getTextSize());
        holder.imageButton.setImageResource(R.drawable.heart_red);
//        holder.imageButton.setOnClickListener(v -> {
//            if (likedFormulas.get(position).isLiked()){
//                likedFormulas.get(position).setLiked(false);
//                holder.imageButton.setImageResource(R.drawable.heart_empty);
//
//                for (Formula likedCard: MainActivity.LikedFormulas ) {
//                    if (likedCard.getName().equals(MainActivity.Formulas.get(position).getName())){
//                        MainActivity.Formulas.get(position).setLiked(false);
//
//                        break;
//                    }
//                }
//                MainActivity.LikedFormulas.remove(likedFormulas.get(position));
//
//            }
//            if(MainActivity.LikedFormulas.isEmpty()) {
//                recyclerView.setVisibility(View.GONE);
//            }
//            else {
//                recyclerView.setVisibility(View.VISIBLE);
//                emptyFavs.setVisibility(View.GONE);
//            }
//            recyclerView.setAdapter(new LikeAdapter(context, MainActivity.LikedFormulas, recyclerView, emptyFavs));
//
//        });

    }

    @Override
    public int getItemCount() {
        return likedFormulas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameView;
        public ImageButton imageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.eventImage);
            nameView = itemView.findViewById(R.id.eventName);
            imageButton = itemView.findViewById(R.id.likeBtn);
        }
    }
}
