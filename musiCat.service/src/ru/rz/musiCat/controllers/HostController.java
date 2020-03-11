package ru.rz.musiCat.controllers;

import java.util.List;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import ru.rz.musiCat.data.entities.Host;
import ru.rz.musiCat.data.repositories.HostRepository;
import ru.rz.musiCat.exceptions.ResourceNotFoundException;
import ru.rz.musiCat.helpers.HostHelpers;

//@Slf4j
@RestController
@RequestMapping("/host")
public class HostController {
	@Autowired
	HostRepository hostRepository;
	
	@GetMapping("/api/list")
	public Iterable<Host> getAllHosts() {
	    return hostRepository.findAll();
	}
	
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public RedirectView createHost(@ModelAttribute Host host, 
			//@ModelAttribute Boolean existing,
			Model model,
			RedirectAttributes redirectAttributes) {
		host.setLastUpdated(new Date());
		//Boolean existing = (Boolean)model.getAttribute("existing");
		//if (existing)
		if (host.getId() != null)
			updateHost(host.getId(), host);
		else
			hostRepository.save(host);
	    
	    redirectAttributes.addFlashAttribute("message", "Success");
	    redirectAttributes.addFlashAttribute("alertClass", "alert-success");
	    return new RedirectView("/host/list", true);
	}
	
	@GetMapping("/get/{id}")
	public Host getHostById(@PathVariable(value = "id") Long hostId) {
	    return hostRepository.findById(hostId)
	            .orElseThrow(() -> new ResourceNotFoundException("Host", "id", hostId));
	}
	
	@GetMapping("/remove/{id}")
	public RedirectView removeHost(@PathVariable(value = "id") Long hostId,
			RedirectAttributes redirectAttributes) {
	    hostRepository.deleteById(hostId);
	    redirectAttributes.addFlashAttribute("message", "Success");
	    redirectAttributes.addFlashAttribute("alertClass", "alert-success");
	    return new RedirectView("/host/list", true);
	}
	
	@PutMapping("/update/{id}")
	public Host updateHost(@PathVariable(value = "id") Long hostId,
	                                        @Valid @RequestBody Host hostDetails) {
		Host host = getHostById(hostId);

		host.setNetworkAddress(hostDetails.getNetworkAddress());
		host.setPhysicalId(hostDetails.getPhysicalId());
		host.setName(hostDetails.getName());
		host.setLastUpdated(new Date());

		Host updatedHost = hostRepository.save(host);
	    return updatedHost;
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView editHost(@PathVariable(value = "id") Long hostId,
			RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("hosts.html");
	    modelAndView.addObject("hosts", getAllHosts());
	    Host host = getHostById(hostId);
	    modelAndView.addObject("host", host);
	    modelAndView.addObject("existing", Boolean.TRUE);
	    return modelAndView;
	}
	
	@RequestMapping("/")
	public RedirectView index() {
		return new RedirectView("/host/list", true);
	}
	
	@RequestMapping("/list")
	public ModelAndView hosts() {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("hosts.html");
	    modelAndView.addObject("hosts", getAllHosts());
	    modelAndView.addObject("host", HostHelpers.getCurrentHost());
	    modelAndView.addObject("existing", Boolean.FALSE);
	    return modelAndView;
	}
}
