package ru.rz.musicat.media.ea;

import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagException;
import ru.rz.musicat.media.interfaces.MusicFile;

import java.io.File;
import java.io.IOException;

public class EAMusicFile implements MusicFile {

    AudioFile audioFile;

    public EAMusicFile(String fileName) throws TagException, CannotReadException, InvalidAudioFrameException, IOException {
        this(new File(fileName));
    }

    public EAMusicFile(File inputFile) throws TagException, CannotReadException, InvalidAudioFrameException, IOException {
        audioFile = AudioFileIO.read(inputFile);
    }

    @Override
    public String getFileName() {
        return audioFile.getFile().getName();
    }

    @Override
    public String getFileNameFull() {
        return audioFile.getFile().getAbsolutePath();
    }

    private String getTag(FieldKey fieldKey) {
        return audioFile.getTag().or(NullTag.INSTANCE).getValue(fieldKey).or("").trim();
    }

    @Override
    public String getTitle() {
        return getTag(FieldKey.TITLE);
    }

    @Override
    public String getAlbum() {
        return getTag(FieldKey.ALBUM);
    }

    @Override
    public String getArtist() {
        return getTag(FieldKey.ARTIST);
    }

    @Override
    public String getYear() {
        return getTag(FieldKey.YEAR);
    }
}
