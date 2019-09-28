package ru.rz.musicat;

import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.TagException;
import ru.rz.musicat.data.dto.AlbumDTO;
import ru.rz.musicat.data.dto.ArtistDTO;
import ru.rz.musicat.data.dto.FileDTO;
import ru.rz.musicat.data.dto.TrackDTO;
import ru.rz.musicat.data.facade.DataAccessFacade;
import ru.rz.musicat.data.facade.detail.DataChangeSession;
import ru.rz.musicat.ea.EAAlbum;
import ru.rz.musicat.ea.EAArtist;
import ru.rz.musicat.ea.EAMusicFile;
import ru.rz.musicat.ea.EAMusicStore;
import ru.rz.musicat.interfaces.FeedbackConsumer;
import ru.rz.musicat.interfaces.MusicFile;
import ru.rz.musicat.interfaces.ProgressReporter;
import ru.rz.musicat.utility.FSWalker;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static ru.rz.musicat.utility.DatabaseHelper.TryDataChange;

public class MCApplication {

    EAMusicStore store = new EAMusicStore();

    private FeedbackConsumer consumer;
    private ProgressReporter progressReporter;

    private DataAccessFacade dataFacade;

    public EAMusicStore getStore() {
        return store;
    }

    public MCApplication(FeedbackConsumer consumer, ProgressReporter progressReporter){
        this.consumer = consumer;
        this.progressReporter = progressReporter;

        this.dataFacade = new DataAccessFacade();
    }

    public void Run(String path, Set<String> exts, boolean recursive) {

        consumer.print(String.format("Processing folder '%s'", path));

        FSWalker walker = new FSWalker(progressReporter, file -> processFile(file));
        walker.WalkPath(path, exts, recursive);

        reportSummary();
        saveTracks();
        dataFacade.Close();
    }

    private void processFile(File file) {
        try {
            EAMusicFile musicFile = new EAMusicFile(file);
            store.AddMusicFile(musicFile);
        } catch (InvalidAudioFrameException | IOException | CannotReadException | TagException e) {
            e.printStackTrace();
        }
    }

    private static int SleepDelay = 300;
    private static int RetryAttempts = 10;

    private void reportSummary(){
        for (EAArtist artist: store.getArtists()) {
            consumer.print(artist.getName());
            for (EAAlbum album: artist.getAlbums())
                consumer.print(String.format("\t%s", album.getName()));
        }
    }

    private void saveTracks() {
        DataChangeSession tx = dataFacade.BeginTransaction();
        try {
            for (MusicFile t : store.getAll()) {
                FileDTO fileDto = new FileDTO(t.getFileNameFull());
                TryDataChange(consumer, () -> tx.Save(fileDto),
                        RetryAttempts, SleepDelay);

                ArtistDTO artistDto = new ArtistDTO(0, t.getArtist());
                TryDataChange(consumer, () -> tx.Save(artistDto),
                        RetryAttempts, SleepDelay);

                AlbumDTO albumDto = new AlbumDTO(0, t.getAlbum(), t.getYear(), artistDto);
                TryDataChange(consumer, () -> tx.Save(albumDto),
                        RetryAttempts, SleepDelay);

                TrackDTO track = new TrackDTO(0, t.getTitle(), fileDto, albumDto);
                TryDataChange(consumer, () -> tx.Save(track),
                        RetryAttempts, SleepDelay);
            }
            tx.Commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (null != tx)
                tx.CleanUp();
        }
    }

}
