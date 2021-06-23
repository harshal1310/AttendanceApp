package com.example.takeit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    classadapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<classlist> classItems = new ArrayList<>();
    Toolbar toolbar;
    DbHelper db;
    EditText classname,subname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  dbHelper = new Dataofclass(this);
        db=new DbHelper(this);
        fab = findViewById(R.id.floatingadd);
        fab.setOnClickListener(v -> showDialog());

        loadData();

        recyclerView = findViewById(R.id.rview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        classAdapter = new classadapter(classItems, this);
        recyclerView.setAdapter(classAdapter);
//        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));
        classAdapter.setOnItemClickListener(new classadapter.OnItemClickListener() {
            @Override
            public void onclick(int position) {
                Intent intent=new Intent(MainActivity.this,secact.class);
                intent.putExtra("classname",classItems.get(position).getClassname());
                intent.putExtra("subname",classItems.get(position).getSubname());
                intent.putExtra("position",position);
                intent.putExtra("cid", classItems.get(position).getCid());
                startActivity(intent);
            }

            @Override
            public void delete(int position) {
                db.deleteClass(classItems.get(position).getCid());
                classItems.remove(position);
                classAdapter.notifyItemRemoved(position);
            }
        });
    }




    private void showDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog dialog=  builder.create();
        dialog.show();
        classname=view.findViewById(R.id.class_edt);
        subname=view.findViewById(R.id.section_edt);
        Button cancel=view.findViewById(R.id.cancel);
        Button add=view.findViewById(R.id.add);
        cancel.setOnClickListener(v->dialog.dismiss());
        add.setOnClickListener(v->{
            addclas();
            dialog.dismiss();

        });


    }

    private void addclas() {
        String className,subjectName;
        className=classname.getText().toString();
        subjectName=subname.getText().toString();
        long cid = db.addClass(className,subjectName);
        classlist classItem = new classlist(cid,className, subjectName);
        classItems.add(classItem);
        classAdapter.notifyDataSetChanged();
    }

    private void loadData() {
        Cursor cursor = db.getClassTable();

        classItems.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DbHelper.C_ID));
            String className = cursor.getString(cursor.getColumnIndex(DbHelper.CLASS_NAME_KEY));
            String subjectName = cursor.getString(cursor.getColumnIndex(DbHelper.SUBJECT_NAME_KEY));

            classItems.add(new classlist(id,className,subjectName));
        }
    }


}