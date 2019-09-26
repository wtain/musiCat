package ru.rz.musicat;

import com.google.common.io.Files;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.TagException;
import org.hibernate.exception.LockAcquisitionException;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MCApplication {

    EAMusicStore store = new EAMusicStore();

    int total = 0;

    private FeedbackConsumer consumer;
    private ProgressReporter progressReporter;

    private DataAccessFacade dataFacade;

    public MCApplication(FeedbackConsumer consumer, ProgressReporter progressReporter){
        this.consumer = consumer;
        this.progressReporter = progressReporter;

        this.dataFacade = new DataAccessFacade();
    }

    void Run(String path, HashSet<String> exts, boolean recursive) {

        try {
            consumer.print(String.format("Processing folder '%s'", path));
            processPath(new File(path), exts, recursive);

            reportSummary();

            saveTracks();
        }
        finally {
            this.dataFacade.Close();
        }
    }

    private void processPath(File folder, HashSet<String> exts, boolean recursive) {

        if (recursive) {
            File[] folders = folder.listFiles(pathname -> pathname.isDirectory());
            for (File subfolder: folders)
                processPath(subfolder, exts, true);
        }

        File[] files = folder.listFiles(pathname -> {
            String ext = Files.getFileExtension(pathname.getName()).toLowerCase();
            return exts.contains(ext);
        });

        processFiles(files);
    }

    private void processFile(DataChangeSession tx, File file) {
        try {
            tx.Save(new FileDTO(file.getAbsolutePath()));
            EAMusicFile musicFile = new EAMusicFile(file);
            store.AddMusicFile(musicFile);
        } catch (InvalidAudioFrameException | IOException | CannotReadException | TagException e) {
            e.printStackTrace();
        }
    }

    private static int SleepDelay = 300;
    private static int RetryAttempts = 10;

    private static void TryDataChange(Runnable operation, int attempts, int delay) throws InterruptedException {
        Boolean success = false;
        int attempt = 0;
        while (!success) {
            try {
                ++attempt;
                operation.run();
                success = true;
            } catch (LockAcquisitionException e) {
                e.printStackTrace();
                System.out.println("Database is locked");
                if (attempt == attempts)
                    throw e;
                System.out.println("Retrying");
                Thread.sleep(delay);
            }
        }
    }

    private void processFiles(File[] files) {
        int processed = 0;
        int total = files.length;
        progressReporter.resetProgress();
        DataChangeSession tx = dataFacade.BeginTransaction();
        try {
            for (File file : files)
                try {
                    TryDataChange(() -> processFile(tx, file), RetryAttempts, SleepDelay);
                } finally {
                    ++processed;
                    double perc = 100 * (double) processed / total;
                    progressReporter.reportProgress(perc);
                }
            tx.Commit();
        }
        catch (Exception he) {
            he.printStackTrace();
        }
        finally {
            if (null != tx)
                tx.CleanUp();
        }
    }

    private void reportSummary(){
        for (EAArtist artist: store.getArtists()) {
            consumer.print(artist.getName());
            for (EAAlbum album: artist.getAlbums())
                consumer.print(String.format("\t%s", album.getName()));
        }
    }

    private void saveTracks() {
        List<TrackDTO> tracksToSave = new ArrayList<TrackDTO>();

        DataChangeSession tx = null;
        try {
            tx = dataFacade.BeginTransaction();
            for (MusicFile t : store.getAll()) {
                FileDTO fileDto = tx.GetFile(t.getFileNameFull());

                ArtistDTO artistDto = new ArtistDTO(0, t.getArtist());
                tx.Save(artistDto);

                AlbumDTO albumDto = new AlbumDTO(0, t.getAlbum(), t.getYear(), artistDto);
                tx.Save(albumDto);
                tracksToSave.add(new TrackDTO(0, t.getTitle(), fileDto, albumDto));
            }
            tx.Commit();
        }
        finally {
            tx.CleanUp();
        }

        DataChangeSession tx1 = dataFacade.BeginTransaction();
        try {
            for (TrackDTO t : tracksToSave) {
                TryDataChange(() -> tx1.Save(t),
                        RetryAttempts, SleepDelay);
            }
            tx1.Commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (null != tx1)
                tx1.CleanUp();
        }
    }

}
