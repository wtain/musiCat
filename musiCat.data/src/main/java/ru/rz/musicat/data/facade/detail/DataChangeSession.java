package ru.rz.musicat.data.facade.detail;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.rz.musicat.data.dto.AlbumDTO;
import ru.rz.musicat.data.dto.ArtistDTO;
import ru.rz.musicat.data.dto.FileDTO;
import ru.rz.musicat.data.dto.TrackDTO;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class DataChangeSession {

    Session session = null;
    Transaction tx = null;

    Dictionary<String, FileDTO> m_files;
    Dictionary<String, TrackDTO> m_tracks;
    Dictionary<String, AlbumDTO> m_albums;
    Dictionary<String, ArtistDTO> m_artists;

    static int nSessions = 0;

    public DataChangeSession(SessionFactory sessionFactory) {
        session = sessionFactory.openSession();
        loadFiles();
        loadTracks();
        loadAlbums();
        loadArtists();

        tx = session.beginTransaction();

        ++nSessions;
        System.out.println(String.format("DataChangeSession: Open session: %d", nSessions));
    }

    public void Commit() {
        session.flush();
        tx.commit();
    }

    public void CleanUp() {
        --nSessions;
        System.out.println(String.format("DataChangeSession: Clean up: %d", nSessions));

        if(session != null) {
            session.close();
        }
    }

    public void DeleteAllFiles() {
        session.createQuery("delete from FileDTO")
                .executeUpdate();
    }

    void loadFiles() {
        List<FileDTO> files = ListFiles();
        m_files = new Hashtable<String, FileDTO>();
        for (FileDTO file: files)
            m_files.put(file.getFileName(), file);
    }

    void loadTracks() {
        List<TrackDTO> tracks = ListTracks();
        m_tracks = new Hashtable<String, TrackDTO>();
        for (TrackDTO track: tracks)
            m_tracks.put(track.getName(), track);
    }

    void loadAlbums() {
        List<AlbumDTO> albums = ListAlbums();
        m_albums = new Hashtable<String, AlbumDTO>();
        for (AlbumDTO album: albums)
            m_albums.put(album.getName(), album);
    }

    void loadArtists() {
        List<ArtistDTO> artists = ListArtists();
        m_artists = new Hashtable<String, ArtistDTO>();
        for (ArtistDTO artist: artists)
            m_artists.put(artist.getName(), artist);
    }

    public FileDTO GetFile(String fileName) {
        return m_files.get(fileName);
    }

    public TrackDTO GetTrack(String trackName) {
        return m_tracks.get(trackName);
    }

    public AlbumDTO GetAlbum(String albumName) {
        return m_albums.get(albumName);
    }

    public ArtistDTO GetArtist(String artistName) {
        return m_artists.get(artistName);
    }

    public void Save(FileDTO file) {
        FileDTO f = GetFile(file.getFileName());
        if (null != f) {
            file.setId(f.getId());
            return;
        }
        try {
            m_files.put(file.getFileName(), file);
            session.save(file);
        }
        catch (NonUniqueObjectException e) {
            e.printStackTrace();
        }
    }

    public void Save(TrackDTO track) {
        TrackDTO t = GetTrack(track.getName());
        if (null != t) {
            track.setId(t.getId());
            return;
        }
        try {
            m_tracks.put(track.getName(), track);
            session.save(track);
        }
        catch (NonUniqueObjectException e) {
            e.printStackTrace();
        }
    }

    public void Save(AlbumDTO album) {
        AlbumDTO a = GetAlbum(album.getName());
        if (null != a) {
            album.setId(a.getId());
            if (null == a.getArtist()) {
                m_albums.put(album.getName(), album);
                a.setArtist(album.getArtist());
                session.update(a);
            }
            return;
        }
        try {
            m_albums.put(album.getName(), album);
            session.save(album);
        }
        catch (NonUniqueObjectException e) {
            e.printStackTrace();
        }
    }

    public void Save(ArtistDTO artist) {
        ArtistDTO a = GetArtist(artist.getName());
        if (null != a) {
            artist.setId(a.getId());
            return;
        }
        try {
            m_artists.put(artist.getName(), artist);
            session.save(artist);
        }
        catch (NonUniqueObjectException e) {
            e.printStackTrace();
        }
    }

    public List<FileDTO> ListFiles() {
        List<FileDTO> result = session.createQuery("from FileDTO").list();

        return result;
    }

    public List<TrackDTO> ListTracks() {
        List<TrackDTO> result = session.createQuery("from TrackDTO").list();

        return result;
    }

    public List<AlbumDTO> ListAlbums() {
        List<AlbumDTO> result = session.createQuery("from AlbumDTO").list();

        return result;
    }

    public List<ArtistDTO> ListArtists() {
        List<ArtistDTO> result = session.createQuery("from ArtistDTO").list();

        return result;
    }
}
