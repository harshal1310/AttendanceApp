package com.example.takeit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class classadapter extends RecyclerView.Adapter<classadapter.Myclass> {

    ArrayList<classlist> lists;
    Context context;
    //Dataofclass db;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener{
        void onclick(int position);
        void delete(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (OnItemClickListener) onItemClickListener;
    }


    public classadapter(ArrayList<classlist> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public Myclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listofclass,parent,false);

        return new Myclass(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Myclass holder, int position) {
        holder.classname.setText(lists.get(position).getClassname());
        holder.subname.setText(lists.get(position).getSubname());

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public  class Myclass extends RecyclerView.ViewHolder //implements View.OnClickListener
    {//Context con;
        //MainActivity obj=new MainActivity();
        //ArrayList<classitem>rem=obj.mylist;
        //Dataofclass db=new Dataofclass(con);
        TextView classname,subname;
        Button del;
        //  MainActivity obj=new MainActivity();
        //  Dataofclass   db=new Dataofclass(obj.context);

        public Myclass(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            classname= itemView.findViewById(R.id.class_name);
            subname=itemView.findViewById(R.id.sub_name);
            del=itemView.findViewById(R.id.del);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onclick(position);
                        }
                    }
                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.delete(position);
                        }
                    }
                }
            });

        }


    }
}


