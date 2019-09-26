package ru.rz.musicat.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "artist")
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Getter
    @Setter
    int id;

    @Getter
    @Setter
    String name;
}
