package ru.rz.musicat.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "track")
@NoArgsConstructor
@AllArgsConstructor
public class TrackDTO {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Getter
    @Setter
    int id;

    @Getter
    @Setter
    String name;

    @JoinColumn(name = "fileId")
    @ManyToOne
    @Getter
    @Setter
    FileDTO file;

    @JoinColumn(name = "albumId")
    @ManyToOne
    @Getter
    @Setter
    AlbumDTO album;
}
