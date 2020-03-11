package ru.rz.musiCat.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import ru.rz.musiCat.data.entities.Host;
import ru.rz.musiCat.data.entities.Image;
import ru.rz.musiCat.data.entities.RootFolder;
import ru.rz.musiCat.data.repositories.AlbumRepository;
import ru.rz.musiCat.data.repositories.HostRepository;
import ru.rz.musiCat.data.repositories.ImageRepository;
import ru.rz.musiCat.data.repositories.RootRepository;
import ru.rz.musiCat.exceptions.ResourceNotFoundException;
import ru.rz.musiCat.helpers.HostHelpers;

//@Slf4j
@RestController
@RequestMapping("/root")
public class RootController {
	@Autowired
	RootRepository rootRepository;
	@Autowired
	HostRepository hostRepository;
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	AlbumRepository albumRepository;
	
	@GetMapping("/api/list")
	public List<RootFolder> getAllRoots() {
	    return rootRepository.findAll();
	}
	
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public RedirectView createRoot(@ModelAttribute RootFolder root, 
			Model model,
			RedirectAttributes redirectAttributes) {
		Host host = hostRepository.findByPhysicalId(HostHelpers.getMachineId()).get();
		root.setHost(host);
		rootRepository.save(root);
	    
	    redirectAttributes.addFlashAttribute("message", "Success");
	    redirectAttributes.addFlashAttribute("alertClass", "alert-success");
	    return new RedirectView("/root/list", true);
	}
	
	@GetMapping("/get/{id}")
	public RootFolder getRootById(@PathVariable(value = "id") Long rootId) {
	    return rootRepository.findById(rootId)
	            .orElseThrow(() -> new ResourceNotFoundException("Root", "id", rootId));
	}
	
	@GetMapping("/remove/{id}")
	public RedirectView removeRoot(@PathVariable(value = "id") Long rootId,
			RedirectAttributes redirectAttributes) {
		rootRepository.deleteById(rootId);
	    redirectAttributes.addFlashAttribute("message", "Success");
	    redirectAttributes.addFlashAttribute("alertClass", "alert-success");
	    return new RedirectView("/root/list", true);
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView editRoot(@PathVariable(value = "id") Long rootId,
			RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("roots.html");
	    modelAndView.addObject("roots", getAllRoots());
	    RootFolder root = getRootById(rootId);
	    modelAndView.addObject("root", root);
	    return modelAndView;
	}
	
	@GetMapping("/traverse/{id}")
	public RedirectView traverseRoot(@PathVariable(value = "id") Long rootId,
			RedirectAttributes redirectAttributes) {
	    RootFolder root = getRootById(rootId);
	    root.setLastTraversed(null);
	    rootRepository.save(root);
	    return new RedirectView("/root/list", true);
	}
	
	@RequestMapping("/")
	public RedirectView index() {
		return new RedirectView("/root/list", true);
	}
	
	@RequestMapping("/list")
	public ModelAndView hosts() {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("roots.html");
	    modelAndView.addObject("roots", getAllRoots());
	    modelAndView.addObject("root", new RootFolder());
	    modelAndView.addObject("host", HostHelpers.getCurrentHost());
	    return modelAndView;
	}
	
	@GetMapping("/view/{id}")
	public ModelAndView viewRoot(@PathVariable(value = "id") Long rootId,
			RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("folder.html");
	    RootFolder root = getRootById(rootId);
	    modelAndView.addObject("folder", root);
		/*
		 * List<Image> images = imageRepository.findAllByFolder(root) .stream()
		 * .filter(i -> !i.isRemoved()) .collect(Collectors.toList());
		 */
	    modelAndView.addObject("images", imageRepository.findAllNotRemoved(root));
	    modelAndView.addObject("albums", albumRepository.findAll());
	    modelAndView.addObject("enable_edit", Boolean.TRUE);
	    return modelAndView;
	}
}
