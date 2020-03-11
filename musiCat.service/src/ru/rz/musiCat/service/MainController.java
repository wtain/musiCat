package ru.rz.musiCat.service;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ru.rz.musiCat.controllers.HostController;
import ru.rz.musiCat.data.entities.Host;
import ru.rz.musiCat.helpers.StringHelpers;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class MainController {
	
	@Autowired
	DataService dataService;
	@Autowired
	HostController hostController;
	
//	@RequestMapping("/")
//	public String index() {
//		return "Greetings from Spring Boot!<BR>" + 
//					dataService.getTick() + "<BR>" + 
//					String.join("<BR>", dataService.getItems());
//	}
	
	@RequestMapping("/")
	public RedirectView index() {
		return new RedirectView("/home", true);
	}
	
	@RequestMapping("/home")
	public ModelAndView home() {
	    	ModelAndView modelAndView = new ModelAndView();
		    modelAndView.setViewName("index.html");
		    modelAndView.addObject("tick", dataService.getTick());
		    modelAndView.addObject("items", dataService.getItems());
		    return modelAndView;
	}
	
	@RequestMapping("/get")
	public ModelAndView get(
			@RequestParam(value="item")
			String item) {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("item.html");
	    modelAndView.addObject("fileName", item);
	    modelAndView.addObject("size", StringHelpers.fileSizeToString(dataService.getItemSize(item)));
	    modelAndView.addObject("type", dataService.getItemType(item));
	    modelAndView.addObject("imgSrc", dataService.getItemImage(item));
	    return modelAndView;
	}
}