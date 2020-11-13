package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hanibalg.yeneservice.R;
import com.hanibalg.yeneservice.models.PaymentCard;

import java.util.List;

public class PaymentCardAdaptor extends RecyclerView.Adapter<PaymentCardAdaptor.CardPayment> {
    private Context context;
    private List<PaymentCard> mPay;

    public PaymentCardAdaptor(Context context,List<PaymentCard> mPay){
        this.context = context;
        this.mPay = mPay;
    }

    @NonNull
    @Override
    public CardPayment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.payment_card,parent,false);
        return new CardPayment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardPayment holder, int position) {
        holder.background.setCardBackgroundColor(mPay.get(position).getBackgroundColor());
        holder.name.setText(mPay.get(position).getCardName());
        holder.card.setText(mPay.get(position).getHint());
    }

    @Override
    public int getItemCount() {
        return mPay.size();
    }

    public class CardPayment extends RecyclerView.ViewHolder {
        ImageView img;
        TextView card;
        TextView name;
        CardView background;

        public CardPayment(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.simImg);
            card = itemView.findViewById(R.id.cardNumber);
            name = itemView.findViewById(R.id.name);
            background = itemView.findViewById(R.id.cardBg);
        }


    }
}
