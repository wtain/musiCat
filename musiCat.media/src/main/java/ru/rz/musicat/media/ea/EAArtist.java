package ru.rz.musicat.media.ea;

import ru.rz.musicat.media.entities.EntityWIthChildren;

public class EAArtist extends EntityWIthChildren<EAAlbum> {

    public EAArtist() {}

    public EAArtist(String name) {
        super(name);
    }

    public EAAlbum getAlbum(String albumName) {
        return getChild(albumName, EAAlbum::new);
    }

    public Iterable<EAAlbum> getAlbums() {
        return getChildren();
    }

    public void addSong(EAMusicFile file) {
        getAlbum(file.getAlbum()).addSong(file);
    }
}
