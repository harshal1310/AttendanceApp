package com.example.takeit;

public class Studentslist {
    String name,roll,status;

    long sid;
    public Studentslist(long sid,String roll, String name) {
        this.name = name;
        this.roll = roll;
        this.sid=sid;
        status="";
    }

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }
    public long getSid()
    {
        return sid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
