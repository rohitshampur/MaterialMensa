package de.prttstft.materialmensa.pojo;

import android.content.Context;


public class Contact {

    private int _id;
    private String name;
    private int integer;

    public Contact (int id, String name, int integer) {
        _id = id;
        name = name;
        integer = integer;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }
}
