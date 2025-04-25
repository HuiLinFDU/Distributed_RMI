package com.example;

//import Serializable by Group11: 
import java.io.Serializable;

/**
 * A Plain Old Jave Object (POJO) representing one employee record in the
 * database
 */

//implement Serializable in EMP class  by Group11 
//public class EMP {
public class EMP implements Serializable {
        private String eno;
    private String ename;
    private String title;

    public EMP(String no, String name, String title) {
        this.eno = no;
        this.ename = name;
        this.title = title;
    }

    public String getENO() {
        return eno;
    }

    public String getName() {
        return ename;
    }

    public String getTitle() {
        return title;
    }

    public String toString() {
        return eno + " " + ename + " " + title + " ";
    }
}
