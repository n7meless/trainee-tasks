package dev.n7meless.model;

public class Airport {
    private String name;
    private String row;
    public Airport(String name, String row){
        this.name = name;
        this.row = row;
    }

    public String getName() {
        return name;
    }

    public String getRow() {
        return row;
    }
}
