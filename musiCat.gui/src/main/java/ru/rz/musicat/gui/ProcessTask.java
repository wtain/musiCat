package ru.rz.musicat.gui;

import javafx.concurrent.Task;
import ru.rz.musicat.MCApplication;
import ru.rz.musicat.ea.EAMusicStore;
import ru.rz.musicat.interfaces.ProgressReporter;

import java.util.Set;

public class ProcessTask extends Task<EAMusicStore> implements ProgressReporter {

    String path;
    Set<String> exts;
    Boolean recursive;

    public ProcessTask(String path, Set<String> exts, Boolean recursive) {
        this.path = path;
        this.exts = exts;
        this.recursive = recursive;
    }

    @Override
    protected EAMusicStore call() throws Exception {
        MCApplication app = new MCApplication(msg -> updateMessage(msg), this);
        app.Run(path, exts, recursive);
        updateMessage("Done");

        return app.getStore();
    }

    @Override
    public void resetProgress() {
        updateProgress(0, 100);
    }

    @Override
    public void reportProgress(double progress) {
        updateProgress(progress, 100);
    }
}
