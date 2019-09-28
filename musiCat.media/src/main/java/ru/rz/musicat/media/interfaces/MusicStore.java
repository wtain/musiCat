package ru.rz.musicat.media.interfaces;

public interface MusicStore {
    Iterable<? extends MusicFile> getAll();
    Iterable<? extends MusicFile> getSongsByArtist(String artist);
    //Iterable<? extends MusicFile> getAlbumsByArtist(String artist);
    Iterable<? extends MusicFile> getSongsByAlbum(String artist, String album);
}
