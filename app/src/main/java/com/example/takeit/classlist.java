package com.example.takeit;

public class classlist {String classname,subname;
    long cid;
    public classlist(long cid,String classname, String subname) {
        this.cid=cid;
        this.classname = classname;
        this.subname = subname;
    }

    public String getClassname() {
        return classname;
    }

    public String getSubname() {
        return subname;
    }

    public long getCid() {
        return cid;
    }

}
