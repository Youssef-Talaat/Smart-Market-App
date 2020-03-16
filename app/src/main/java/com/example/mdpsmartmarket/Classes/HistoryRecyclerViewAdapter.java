package com.example.mdpsmartmarket.Classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mdpsmartmarket.Activities.ProceedToPaymentActivity;
import com.example.mdpsmartmarket.R;

import java.util.ArrayList;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Cart> carts;
    private LayoutInflater mInflater;
    private Cart currentCart;
    private ItemClickListener mClickListener;

    public HistoryRecyclerViewAdapter(Context context, ArrayList<Cart> carts) {
        this.mInflater = LayoutInflater.from(context);
        this.carts = carts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        currentCart = carts.get(position);
        holder.date.setText("Cart Date");
        holder.noOfItems.setText("No of Items: " + currentCart.getItems().size());
        double cartCost = 0.0;
        for(int i = 0; i < currentCart.getItems().size(); i++){
            cartCost += currentCart.getItems().get(i).getQuantity() * currentCart.getItems().get(i).getUnitPrice();
        }
        holder.cost.setText("Cost: " + cartCost);

        holder.refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mInflater.getContext(), ProceedToPaymentActivity.class);
                intent.putExtra("cartID", carts.get(position).getID());
                mInflater.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date;
        TextView cost;
        TextView noOfItems;
        Button refund;

        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_historyrecord_date);
            cost = itemView.findViewById(R.id.txt_historyrecord_cost);
            noOfItems = itemView.findViewById(R.id.txt_historyrecord_noofitems);
            refund = itemView.findViewById(R.id.btn_historyrecord_refund);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Cart getItem(int id) {
        return carts.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
