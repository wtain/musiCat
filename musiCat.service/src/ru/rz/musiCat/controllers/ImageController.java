package ru.rz.musiCat.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import ru.rz.musiCat.data.entities.Album;
import ru.rz.musiCat.data.entities.Image;
//import ru.rz.musiCat.data.entities.RootFolder;
import ru.rz.musiCat.data.repositories.AlbumRepository;
import ru.rz.musiCat.data.repositories.ImageRepository;
import ru.rz.musiCat.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/image")
public class ImageController {
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	AlbumRepository albumRepository;
	
	@GetMapping("/get/{id}")
	public void getImageById(@PathVariable(value = "id") Long imageId, 
			HttpServletResponse response) throws IOException {
	    Image img = imageRepository.findById(imageId)
	            .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
	    
	    
	    File f = new File(FilenameUtils.concat(img.getFolder().getPath(), img.getFileName()));
	    InputStream inputStream = new BufferedInputStream(new FileInputStream(f));
	    IOUtils.copy(inputStream, response.getOutputStream());
	}
	
	@GetMapping("/remove/{id}")
	public void removeImageById(@PathVariable(value = "id") Long imageId, 
			HttpServletResponse response) throws IOException {
	    Image img = imageRepository.findById(imageId)
	            .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
	    
	    img.setIsRemoved(Boolean.TRUE);
	    imageRepository.save(img);
	}
	
	@PostMapping("/update/{id}")
	public RedirectView Update(@PathVariable(value = "id") Long imageId, 
			HttpServletRequest request) {
		String description = request.getParameter("description");
		String folderIdString = request.getParameter("folder_id");
		Long albumId = Long.parseLong(request.getParameter("album"));
		Album album = 0 != albumId ? albumRepository.findById(albumId)
			.orElseThrow(() -> new ResourceNotFoundException("album", "id", albumId))
			: null;
	    Image img = imageRepository.findById(imageId)
	            .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
	    img.setDescription(description);
	    img.setAlbum(album);
	    imageRepository.save(img);
	    return new RedirectView("/root/view/" + folderIdString, true);
	}
}
