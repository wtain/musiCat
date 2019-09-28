package ru.rz.musicat.gui;

import javafx.scene.control.TreeItem;
import lombok.Getter;
import ru.rz.musicat.media.interfaces.MusicFile;

public class TrackTreeItem extends TreeItem {

    @Getter
    MusicFile track;

    public TrackTreeItem(MusicFile track) {
        super(track.getTitle());
        this.track = track;
    }

}
