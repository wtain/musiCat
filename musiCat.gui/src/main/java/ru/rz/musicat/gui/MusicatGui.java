package ru.rz.musicat.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.rz.musicat.MCApplication;
import ru.rz.musicat.ea.EAAlbum;
import ru.rz.musicat.ea.EAArtist;
import ru.rz.musicat.ea.EAMusicFile;
import ru.rz.musicat.ea.EAMusicStore;
import ru.rz.musicat.interfaces.MusicFile;
import ru.rz.musicat.interfaces.ProgressReporter;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicatGui extends Application/* implements ProgressReporter*/{

    TreeView treeView;
    ProgressBar progressBar;
    TextField statusField;

    @Override
    public void start(Stage primaryStage) {

        GridPane root = new GridPane();
        treeView = new TreeView();
        progressBar = new ProgressBar();
        statusField = new TextField();
        root.add(treeView, 0, 0);
        root.add(progressBar, 0, 1);
        root.add(statusField, 0, 2);

        HashSet<String> exts = new HashSet<>();
        exts.add("mp3");
        exts.add("flac");

        ProcessTask task = new ProcessTask("d:\\\\_From_Phone_20190503", exts, true);
        progressBar.progressProperty().bind(task.progressProperty());
        statusField.textProperty().bind(task.messageProperty());
        task.setOnSucceeded(wse -> {
            EAMusicStore store = task.getValue();
            TreeItem treeroot = new TreeItem("Artists");
            treeView.setRoot(treeroot);

            for (EAArtist artist : store.getArtists()) {
                TreeItem tiArtist = new TreeItem(artist.getName());
                treeroot.getChildren().add(tiArtist);
                for (EAAlbum album : store.getAlbumsByArtist(artist.getName())) {
                    TreeItem tiAlbum = new TreeItem(album.getName());
                    tiArtist.getChildren().add(tiAlbum);
                    for (MusicFile track : store.getSongsByAlbum(artist.getName(), album.getName())) {
                        TreeItem tiTrack = new TreeItem(track.getTitle());
                        tiAlbum.getChildren().add(tiTrack);
                    }
                }
            }
        });

        ExecutorService executorService
                = Executors.newFixedThreadPool(1);
        executorService.execute(task);
        executorService.shutdown();

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("musiCat");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /*@Override
    public void resetProgress() {
        progressBar.setProgress(0);
    }

    @Override
    public void reportProgress(double progress) {
        progressBar.setProgress(progress);
    }*/
}