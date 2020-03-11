package ru.rz.musiCat.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.rz.musiCat.data.entities.Image;
import ru.rz.musiCat.data.entities.RootFolder;
import ru.rz.musiCat.data.repositories.HostRepository;
import ru.rz.musiCat.data.repositories.ImageRepository;
import ru.rz.musiCat.data.repositories.RootRepository;
import ru.rz.musiCat.helpers.HostHelpers;

@Service
@ConfigurationProperties(prefix = "musicat.folderdiscoveryservice")
public class FolderDiscoveryService {
	@Autowired
	RootRepository rootRepository;
	@Autowired
	HostRepository hostRepository;
	@Autowired
	ImageRepository imageRepository;
	
	@Value("#{'${musicat.folderdiscoveryservice.extensions}'.split(',')}") 
	private List<String> extensions;
	
	long traversalInterval = 10 * 60 * 1000; // 10 minutes by default
	
//	@Autowired
//	public FolderDiscoveryService(@Value("${SVN_URL}") String svnUrl) {
//		
//	}
	
	@Scheduled (fixedRateString="${musicat.folderdiscoveryservice.frequency}")
	public void service() {
		System.out.println("Running folder discovery");
		//for (RootFolder folder: rootRepository.findAll()) {
		for (RootFolder folder: rootRepository.findAllByHost(hostRepository.getCurrentHost())) {
			//Period
			//if (new Date() - folder.getLastTraversed())
//			if (!hostRepository.isCurrentHost(folder.getHostId()))
//				continue;
			
			if (folder.isProblem() == Boolean.TRUE) {
				System.out.println(String.format("Skipping problem folder %s", folder.getPath()));
				continue;
			}
			
			Date now = new Date();
			Date lastTraversed = folder.getLastTraversed();
			long elapsed = now.getTime() - (null != lastTraversed ? lastTraversed.getTime() : 0l);
			if (elapsed >= traversalInterval) {
				// Do traversal
				System.out.println(String.format("Traversing folder %s", folder.getPath()));
				
				try (Stream<Path> walk = 
						Files.walk(Paths.get(folder.getPath()))) {

					/* List<String> */Stream<String> files = walk
							.filter(Files::isRegularFile)
							.filter(f -> extensions.contains(FilenameUtils.getExtension(f.getFileName().toString())))
							//.map(x -> x.toString()).collect(Collectors.toList());
							.map(x -> x.getFileName().toString())/* .collect(Collectors.toList()) */;
					
					//files.forEach(f -> System.out.println(f));
					
					Map<String, Image> images = imageRepository.findAllByFolder(folder)
							.stream()
							.collect(Collectors.toMap(Image::getFileName, Function.identity()));
					
					//files.forEach(f -> imageRepository.save(new Image(f, folder)));
					imageRepository.saveAll(files
							.map(f -> images.containsKey(f) ? images.get(f) : new Image(f, folder) 
									
								//new Image(f, folder)
							)::iterator);
					// Merge!
					
				} catch (IOException e) {
					System.out.println(String.format("Found a problem with folder %s: %s", 
							folder.getPath(), e.toString()));
					folder.setProblem(true);
				}
				
				folder.setLastTraversed(now);
				rootRepository.save(folder);
			}
		}
	}
}
