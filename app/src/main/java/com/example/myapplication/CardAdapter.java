package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;
import java.util.Random;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardItem> cardItemList;
    private Context context;
    public CardAdapter(List<CardItem> cardItemList, Context context) {
        this.cardItemList = cardItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem cardItem = cardItemList.get(position);

        int randomColor = generateRandomColor();
        holder.textViewAvatar.setBackgroundResource(R.drawable.round_background);
        holder.textViewAvatar.getBackground().setColorFilter(randomColor, PorterDuff.Mode.SRC);

        holder.textViewTitle.setText(cardItem.getTitle());
        holder.textViewDate.setText(cardItem.getDate());
        holder.textViewAvatar.setText(cardItem.getFirstLetter());

        holder.textViewLocation.setText(cardItem.getLocation());

        holder.texViewJtime.setText(cardItem.getJtime());
        holder.textViewSalary.setText(cardItem.getSalary());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the DetailActivity with the selected card data
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("title", cardItem.getTitle());
                intent.putExtra("documentId", cardItem.getDocumentId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewAvatar;
        TextView textViewLocation;
        TextView textViewDate;  // Add this line
        TextView texViewJtime;
        TextView textViewSalary;

        public CardViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAvatar = itemView.findViewById(R.id.company_avatar);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewDate = itemView.findViewById(R.id.textViewDate);  // Add this line
            texViewJtime = itemView.findViewById(R.id.texViewJtime);
            textViewSalary = itemView.findViewById(R.id.textViewSalary);
        }
    }
    private int generateRandomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(150) + 120, random.nextInt(150) + 110, random.nextInt(150) + 120);

    }
}
