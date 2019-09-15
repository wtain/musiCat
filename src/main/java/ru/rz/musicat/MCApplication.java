package ru.rz.musicat;

import com.google.common.io.Files;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.TagException;
import ru.rz.musicat.ea.EAAlbum;
import ru.rz.musicat.ea.EAArtist;
import ru.rz.musicat.ea.EAMusicFile;
import ru.rz.musicat.ea.EAMusicStore;
import ru.rz.musicat.interfaces.FeedbackConsumer;
import ru.rz.musicat.interfaces.ProgressReporter;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class MCApplication {

    EAMusicStore store = new EAMusicStore();

    int total = 0;

    private FeedbackConsumer consumer;
    private ProgressReporter progressReporter;

    public MCApplication(FeedbackConsumer consumer, ProgressReporter progressReporter){
        this.consumer = consumer;
        this.progressReporter = progressReporter;
    }

    void Run(String path, HashSet<String> exts, boolean recursive) {
        consumer.print(String.format("Processing folder '%s'", path));
        processPath(new File(path), exts, recursive);

        reportSummary();
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

    private void processFiles(File[] files) {
        int processed = 0;
        int total = files.length;
        progressReporter.resetProgress();
        for (File file: files)
            try {
                EAMusicFile musicFile = new EAMusicFile(file);
                store.AddMusicFile(musicFile);
            } catch (InvalidAudioFrameException | IOException | CannotReadException | TagException e) {
                e.printStackTrace();
            } finally {
                ++processed;
                double perc = 100 * (double)processed / total;
                progressReporter.reportProgress(perc);
            }
    }

    private void reportSummary(){
        for (EAArtist artist: store.getArtists()) {
            consumer.print(artist.getName());
            for (EAAlbum album: artist.getAlbums())
                consumer.print(String.format("\t%s", album.getName()));
        }
    }

}
