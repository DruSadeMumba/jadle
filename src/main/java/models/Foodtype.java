package models;

import java.util.Objects;

public class Foodtype {
    private String name;
    private int id;

    //getters
    public String getName() {return name;}
    public int getId() {return id;}

    //setters
    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}

    public Foodtype(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Foodtype foodtype = (Foodtype) o;
        return id == foodtype.id && name.equals(foodtype.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
