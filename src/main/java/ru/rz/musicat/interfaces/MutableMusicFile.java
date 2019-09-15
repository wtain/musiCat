package ru.rz.musicat.interfaces;

public interface MutableMusicFile extends MusicFile {
    void setTitle(String newTitle);
    void setAlbum(String newAlbum);
    void setArtist(String newArtist);
    void setYear(int newYear);
}
