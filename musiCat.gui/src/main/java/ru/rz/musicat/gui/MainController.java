package ru.rz.musicat.gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.rz.musicat.ea.EAAlbum;
import ru.rz.musicat.ea.EAArtist;
import ru.rz.musicat.ea.EAMusicStore;
import ru.rz.musicat.interfaces.MusicFile;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController {
    @FXML private TreeView treeView;
    @FXML private ProgressBar progressBar;
    @FXML private TextField statusField;

    @FXML private Button btnRun;
    @FXML private Button btnExit;

    @FXML private TextField selectedNodeInfo;
    @FXML private TextField txtPath;
    @FXML private TextField txtExtensions;
    @FXML private CheckBox chkRecursive;

    @FXML private TextField txtTrackName;
    @FXML private TextField txtAlbumName;
    @FXML private TextField txtArtistName;
    @FXML private Button btnApply;

    @FXML
    public void initialize() {
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                treeView_SelectedItemChanged();
            }
        });

        txtPath.setText("d:\\\\_From_Phone_20190503");
        txtExtensions.setText("mp3;flac");
        chkRecursive.setSelected(true);
    }

    private Set<String> getSelectedExtensions() {
        String sExts = txtExtensions.getText();
        String[] exts = sExts.split(";");
        Set<String> result = new HashSet<String>();
        for (String ext: exts)
            result.add(ext);
        return result;
    }

    private MusicFile getSelectedMusicFile() {
        Object newValue = treeView.getSelectionModel().getSelectedItem();
        if (!(newValue instanceof TrackTreeItem))
            return null;
        TrackTreeItem titem = (TrackTreeItem) newValue;
        return titem.getTrack();
    }

    private void treeView_SelectedItemChanged() {
        selectedNodeInfo.setText("");
        MusicFile mfile = getSelectedMusicFile();
        if (null == mfile)
            return;
        selectedNodeInfo.setText(mfile.getFileName());

        txtTrackName.setText(mfile.getTitle());
        txtAlbumName.setText(mfile.getAlbum());
        txtArtistName.setText(mfile.getArtist());
        btnApply.setDisable(true);
    }

    public void setDirtyFlag() {
        btnApply.setDisable(false);
    }

    public void btnApplyHandler(ActionEvent actionEvent) {

        MusicFile current = getSelectedMusicFile();
        if (null == current)
            return;

        // current.

        btnApply.setDisable(true);
    }

    public void btnRunHandler(ActionEvent actionEvent) {
        btnRun.setDisable(true);
        ProcessTask task = new ProcessTask(txtPath.getText(), getSelectedExtensions(), chkRecursive.isSelected());
        progressBar.progressProperty().bind(task.progressProperty());
        statusField.textProperty().bind(task.messageProperty());
        task.setOnSucceeded(wse -> {
            EAMusicStore store = task.getValue();
            onScanCompleted(store);
            btnRun.setDisable(false);
        });
        task.setOnFailed(wse -> btnRun.setDisable(false));

        ExecutorService executorService
                = Executors.newFixedThreadPool(1);
        executorService.execute(task);
        executorService.shutdown();
    }

    public void btnExitHandler(ActionEvent actionEvent) {
        Platform.exit();
    }

    private void onScanCompleted(EAMusicStore store) {
        TreeItem treeroot = new TreeItem("Artists");
        treeView.setRoot(treeroot);
        treeroot.setExpanded(true);

        for (EAArtist artist : store.getArtists()) {
            TreeItem tiArtist = new TreeItem(artist.getName());
            treeroot.getChildren().add(tiArtist);
            tiArtist.setExpanded(true);
            for (EAAlbum album : store.getAlbumsByArtist(artist.getName())) {
                TreeItem tiAlbum = new TreeItem(album.getName());
                tiArtist.getChildren().add(tiAlbum);
                tiAlbum.setExpanded(true);
                for (MusicFile track : store.getSongsByAlbum(artist.getName(), album.getName())) {
                    TrackTreeItem tiTrack = new TrackTreeItem(track);
                    tiAlbum.getChildren().add(tiTrack);
                }
            }
        }
    }
}
