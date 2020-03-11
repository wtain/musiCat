package ru.rz.musiCat.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import ru.rz.musiCat.data.entities.Album;
import ru.rz.musiCat.data.entities.RootFolder;
import ru.rz.musiCat.data.repositories.AlbumRepository;
import ru.rz.musiCat.data.repositories.ImageRepository;
import ru.rz.musiCat.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/album")
public class AlbumController {
	@Autowired
	AlbumRepository albumRepository;
	@Autowired
	ImageRepository imageRepository;
	
	@GetMapping("/api/list")
	public List<Album> getAllAlbums() {
	    return albumRepository.findAll();
	}
	
	@RequestMapping("/list")
	public ModelAndView hosts() {
		return prepareListView(new ModelAndView(), "albums.html");
	}
	
	private ModelAndView prepareListView(ModelAndView modelAndView, String viewName) {
		modelAndView.setViewName(viewName);
	    modelAndView.addObject("albums", getAllAlbums());
	    modelAndView.addObject("album", new Album());
	    return modelAndView;
	}
	
	@RequestMapping("/updatelist")
	public ModelAndView updatelist() {
		return prepareListView(new ModelAndView("updatelist::albumList"), 
				"albums.html :: albumList");
	}
	
	@GetMapping("/remove/{id}")
	public ModelAndView removeAlbum(@PathVariable(value = "id") Long albumId) {
		albumRepository.deleteById(albumId);
	    return updatelist();
	}
	
	@PostMapping("/update")
	public ModelAndView removeAlbum(HttpServletRequest request) {
		long id = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		Album album = albumRepository.getOne(id);
		album.setName(name);
		album.setDescription(description);		
		albumRepository.save(album);
	    return updatelist();
	}
	
	@RequestMapping("/")
	public RedirectView index() {
		return new RedirectView("/album/list", true);
	}
	
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public RedirectView createRoot(HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		Album album = new Album(name, description);
		albumRepository.save(album);
	    
	    return new RedirectView("/album/list", true);
	}
	
	@GetMapping("/get/{id}")
	public Album getAlbumById(@PathVariable(value = "id") Long albumId) {
	    return albumRepository.findById(albumId)
	            .orElseThrow(() -> new ResourceNotFoundException("Album", "id", albumId));
	}
	
	@GetMapping("/view/{id}")
	public ModelAndView viewAlbum(@PathVariable(value = "id") Long albumId,
			RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("view_album.html");
	    Album album = getAlbumById(albumId);
	    modelAndView.addObject("album", album);
	    modelAndView.addObject("images", imageRepository.findAllByAlbum(album));
	    modelAndView.addObject("albums", albumRepository.findAll());
	    modelAndView.addObject("enable_edit", Boolean.FALSE);
	    return modelAndView;
	}
}
