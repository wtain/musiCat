package ru.rz.musicat.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "album")
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Getter
    @Setter
    int id;

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    String releaseDate;

    @JoinColumn(name = "artistId")
    @ManyToOne
    @Getter
    @Setter
    ArtistDTO artist;
}
