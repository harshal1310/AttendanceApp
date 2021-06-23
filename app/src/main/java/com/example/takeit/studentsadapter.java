package com.example.takeit;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class studentsadapter  extends RecyclerView.Adapter<studentsadapter.Myview>{
    Context con;
    ArrayList<Studentslist> slist;
    OnItemclickListener onItemclickListener;
    public interface OnItemclickListener{
        void onclick(int position);
    }

    public void setOnItemclickListener(OnItemclickListener onItemclickListener) {
        this.onItemclickListener = onItemclickListener;
    }

    public studentsadapter(Context con, ArrayList<Studentslist> slist) {
        this.con = con;
        this.slist = slist;
    }

    @NonNull
    @Override
    public studentsadapter.Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listofstudents, parent, false);

        return new studentsadapter.Myview(view,onItemclickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull studentsadapter.Myview holder, int position) {
        //holder.roll.setText(slist.get(position).getRoll());
        Studentslist s=slist.get(position);
        holder.roll.setText(s.getRoll());
        holder.name.setText(s.getName());
        holder.status.setText(slist.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }

    private int getColor(int position) {
        String status = slist.get(position).getStatus();
        if(status.equals("P"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(con,R.color.present)));
        else if (status.equals("A"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(con,R.color.absent)));

        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(con,R.color.normal)));
    }


    @Override
    public int getItemCount() {
        return slist.size();
        //    return lists.size();
    }

    public class Myview extends RecyclerView.ViewHolder
    {
        public TextView roll,name,status;
        CardView cardView;
        public Myview(@NonNull View itemView,OnItemclickListener onItemclickListener) {
            super(itemView);
            roll=itemView.findViewById(R.id.roll);
            name=itemView.findViewById(R.id.name);
            status= itemView.findViewById(R.id.status);
            cardView=itemView.findViewById(R.id.cardview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemclickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemclickListener.onclick(position);
                        }
                    }
                }
            });
        }


    }
}

