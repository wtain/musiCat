package ru.rz.musiCat.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.rz.musiCat.data.entities.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
	
}