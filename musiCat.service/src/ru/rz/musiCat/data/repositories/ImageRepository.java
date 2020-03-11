package ru.rz.musiCat.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.rz.musiCat.data.entities.Album;
import ru.rz.musiCat.data.entities.Image;
import ru.rz.musiCat.data.entities.RootFolder;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findAllByFolder(RootFolder folder);
	List<Image> findAllByAlbum(Album album);
	long deleteByFolder(RootFolder folder);
	//List<Image> findAllByFolderAndRemoved(RootFolder folder, Boolean removed);
	
	@Query("SELECT i FROM Image i WHERE i.folder=?1 AND (i.removed is null or i.removed is false)") //  
	List<Image> findAllNotRemoved(RootFolder folder);
}