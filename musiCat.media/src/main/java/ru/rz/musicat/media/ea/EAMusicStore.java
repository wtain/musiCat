package ru.rz.musicat.media.ea;

import ru.rz.musicat.media.entities.EntityWIthChildren;
import ru.rz.musicat.media.interfaces.MusicFile;
import ru.rz.musicat.media.interfaces.MusicStore;

import java.util.LinkedList;

public class EAMusicStore extends EntityWIthChildren<EAArtist> implements MusicStore {

    private LinkedList<EAMusicFile> musicFiles = new LinkedList<>();

    public EAMusicStore() {
        super("Store");
    }

    @Override
    public Iterable<? extends MusicFile> getAll() {
        return musicFiles;
    }

    @Override
    public Iterable<? extends MusicFile> getSongsByArtist(String artistName) {
        LinkedList<EAMusicFile> result = new LinkedList<>();
        for (EAAlbum album: getAlbumsByArtist(artistName))
            result.addAll(album.getSongs());
        return result;
    }

    //@Override
    public Iterable<EAAlbum> getAlbumsByArtist(String artistName) {
        EAArtist artist = getArtist(artistName);
        return artist.getAlbums();
    }

    @Override
    public Iterable<? extends MusicFile> getSongsByAlbum(String artistName, String albumName) {
        return getArtist(artistName).getAlbum(albumName).getSongs();
    }

    public EAArtist getArtist(String artistName) {
        return getChild(artistName, EAArtist::new);
    }

    public Iterable<EAArtist> getArtists() {
        return getChildren();
    }

    public void AddMusicFile(EAMusicFile file) {
        musicFiles.add(file);
        getArtist(file.getArtist()).addSong(file);
    }
}
