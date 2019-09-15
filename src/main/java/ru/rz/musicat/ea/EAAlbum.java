package ru.rz.musicat.ea;

import ru.rz.musicat.entities.NamedEntity;

import java.util.Collection;
import java.util.LinkedList;

public class EAAlbum extends NamedEntity {

    private LinkedList<EAMusicFile> songs = new LinkedList<>();

    public EAAlbum(){}

    public EAAlbum(String name) {
        super(name);
    }

    public void addSong(EAMusicFile file) {
        songs.add(file);
    }

    public Collection<EAMusicFile> getSongs() {
        return songs;
    }
}
