package com.example.takeit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class secact extends AppCompatActivity {


    Toolbar toolbar;
    TextView cname,sname;
    EditText editclass,editsub;
    studentsadapter adapter;
    String classname,subjectname;
    RecyclerView review;
    int position;
    long cidid;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton addstud,showcal,viewatt;
    DbHelper db;
    ArrayList<Studentslist> records=new ArrayList<>();
    Mycalender calendar;
    ImageButton back,save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secact);
        Intent intent=getIntent();
        classname = intent.getStringExtra("classname");
        subjectname = intent.getStringExtra("subname");
        //    Log.d("aa","aaaa");
        calendar=new Mycalender();
        db=new DbHelper(this);
        position = intent.getIntExtra("position", -1);
        cidid = intent.getLongExtra("cid", -1);

        cname=findViewById(R.id.cname);
        sname=findViewById(R.id.sname);
        addstud=findViewById(R.id.addstud);

        back=findViewById(R.id.back);
        save=findViewById(R.id.save);
        viewatt=findViewById(R.id.viewattendance);
        save.setOnClickListener(v -> saveStatus());
       //viewatt.setOnClickListener(v->openlist());
        showcal=findViewById(R.id.showcal);
        back.setOnClickListener(v -> onBackPressed());

        cname.setText(classname);
        sname.setText(subjectname+ " | " + calendar.getDate());
        loadData();
        review = findViewById(R.id.stulist);
        review.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        review.setLayoutManager(layoutManager);

        addstud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        adapter = new studentsadapter(this, records);
        review.setAdapter(adapter);
        adapter.setOnItemclickListener(new studentsadapter.OnItemclickListener() {
            @Override
            public void onclick(int position) {
                changeStatus(position);
            }
        });
        loadStatusData();
        showcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
    }
    /*
    private void openlist() {
        Intent intent=new Intent(this,attsheet.class);
        intent.putExtra("cid",cidid);
        startActivity(intent);
    }*/


    private void showDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog dialog=  builder.create();
        dialog.show();
        editclass=view.findViewById(R.id.class_edt);
        editsub=view.findViewById(R.id.section_edt);
        Button cancel=view.findViewById(R.id.cancel);
        Button add=view.findViewById(R.id.add);
        cancel.setOnClickListener(v->dialog.dismiss());
        add.setOnClickListener(v->{
            addclas();
            dialog.dismiss();

        });


    }

    private void addclas() {
        String getclassname,getsuname;

        getclassname=editclass.getText().toString();
        //int roll=Integer.parseInt(getclassname);
        getsuname=editsub.getText().toString();
        long  sidd= db.addStudent(cidid,getclassname,getsuname);
        Studentslist items=new Studentslist(sidd,getclassname,getsuname);
        records.add(items);
        adapter.notifyDataSetChanged();
    }

    private void loadData() {
        Cursor cursor = db.getStudentTable(cidid);
        Log.i("ash", "loadData: " + cidid);
        records.clear();
        while (cursor.moveToNext()) {
            long sid = cursor.getLong(cursor.getColumnIndex(DbHelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY));
            String r=  String.valueOf(roll);
            records.add(new Studentslist(sid, r, name));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = records.get(position).getStatus();

        if (status.equals("P")) status = "A";
        else status = "P";

        records.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void loadStatusData() {
        for (Studentslist studentItem : records) {
            String status = db.getStatus(studentItem.getSid(), calendar.getDate());
            if (status != null) studentItem.setStatus(status);
            else studentItem.setStatus("");
        }
        adapter.notifyDataSetChanged();
    }

    private void showCalendar() {

        calendar.show(getSupportFragmentManager(), "");
        calendar.setOnCalendarOkClickListener(this::onCalendarOkClicked);
    }

    private void onCalendarOkClicked(int year, int month, int day) {
        calendar.setDate(year, month, day);
        sname.setText(subjectname + " | " + calendar.getDate());
        loadStatusData();
    }
    private void saveStatus() {
        for (Studentslist studentItem : records) {
            String status = studentItem.getStatus();
            if (status != "P") status = "A";
            long value = db.addStatus(studentItem.getSid(), cidid, calendar.getDate(), status);

            if (value == -1)
                db.updateStatus(studentItem.getSid(), calendar.getDate(), status);
        }
    }
}