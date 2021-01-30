package com.example.common;

import java.util.ArrayList;

public class Car {

    private ArrayList<String> Cars = new ArrayList<>();
    private boolean open = false;
    private String address= "";
    private String name ="";

    public Car(){
    }


    public ArrayList<String> getCars() {
        return Cars;
    }

    public boolean isOpen() {
        return open;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Car setCars(ArrayList<String> Cars) {
        this.Cars = Cars;
        return this;
    }

    public Car setOpen(boolean open) {
        this.open = open;
        return this;
    }

    public Car setAddress(String address) {
        this.address = address;
        return this;
    }

    public Car setName(String name) {
        this.name = name;
        return this;

    }
}
