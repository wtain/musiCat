package ru.rz.musicat.data.dto;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "file")
public class FileDTO implements Serializable {
    private Integer id;
    private String fileName;
    private TrackDTO track;

    public FileDTO() {

    }

    public FileDTO(String fileName) {
        this(0, fileName);
    }

    public FileDTO(Integer id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "fileName", unique = true)
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
