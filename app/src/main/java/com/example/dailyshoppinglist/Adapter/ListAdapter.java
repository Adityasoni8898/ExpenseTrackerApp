package com.example.dailyshoppinglist.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyshoppinglist.Model.Data;
import com.example.dailyshoppinglist.R;
import com.example.dailyshoppinglist.Interface.RecyclerViewInterface;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    ArrayList<Data> list;

    public ListAdapter(ArrayList<Data> list, RecyclerViewInterface recyclerViewInterface) {
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Data data = list.get(position);
        holder.type.setText(data.getType());
        holder.note.setText(data.getNote());
        holder.date.setText(data.getDate());
        String stringAmount = "₹" + String.valueOf(data.getAmount());
        holder.amount.setText(stringAmount);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        TextView date, type, amount, note;

        public ListViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            date = itemView.findViewById(R.id.tv_list_date);
            type = itemView.findViewById(R.id.tv_list_type);
            amount = itemView.findViewById(R.id.tv_list_amount);
            note = itemView.findViewById(R.id.tv_list_note);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null) {
                        int pos = getBindingAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }


    }
}
