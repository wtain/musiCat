package ru.rz.musicat.media.entities;

import java.util.HashMap;
import java.util.function.Supplier;

public class EntityWIthChildren<T extends NamedEntity> extends NamedEntity {

    private HashMap<String, T> children = new HashMap<>();

    public EntityWIthChildren() {}

    public EntityWIthChildren(String name) {
        super(name);
    }

    protected T getChild(String childName, Supplier<? extends T> ctor) {
        if (children.containsKey(childName))
            return children.get(childName);
        T album = (T) ctor.get().setName(childName);
        children.put(childName, album);
        return album;
    }

    protected Iterable<T> getChildren() {
        return children.values();
    }
}
