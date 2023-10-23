package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.w3c.dom.Text;

import java.util.List;

public class AdminCardAdapter extends RecyclerView.Adapter<AdminCardAdapter.CardViewHolder> {

    private List<CardItem> cardItemList;
    private Context context;
    public AdminCardAdapter(List<CardItem> cardItemList, Context context) {
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
        holder.textViewTitle.setText(cardItem.getTitle());
        holder.textViewDescription.setText(cardItem.getDescription());
        holder.textViewAvatar.setText(cardItem.getFirstLetter());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the DetailActivity with the selected card data
                Intent intent = new Intent(context, AdminDetailActivity.class);
                intent.putExtra("title", cardItem.getTitle());
                intent.putExtra("documentId", cardItem.getDocumentId());
                intent.putExtra("description", cardItem.getDescription());
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

        TextView textViewDescription;
        TextView textViewAvatar;

        public CardViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewAvatar = itemView.findViewById(R.id.company_avatar);

        }
    }
}
