package ru.rz.musicat.entities;

public class NamedEntity {
    private String name;

    public NamedEntity() {}

    public NamedEntity(String name) {
        this.name = name;
    }

    public String getName() {return name;}

    public NamedEntity setName(String name){
        this.name = name;
        return this;
    }
}
