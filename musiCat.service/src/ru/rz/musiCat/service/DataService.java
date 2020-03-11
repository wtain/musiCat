package ru.rz.musiCat.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataService {
	
	private int tick = 0;
	
	Logger logger = LoggerFactory.getLogger(DataService.class);
	
	@Value("${musicat.DataService.RootPath}")
	private String rootPath;
	
	@Scheduled (fixedRateString="${musicat.DataService.TickDelay}")
	public void loop() {
		++tick;
		//logger.info("Tick {}", tick);
	}
	
	public int getTick() {
		return tick;
	}
	
	public List<String> getItems() {
		logger.info("getItems for path {}", rootPath);
		try (Stream<Path> walk = 
				Files.walk(Paths.get(rootPath))) {

			List<String> files = walk.filter(Files::isRegularFile)
					.map(x -> x.toString()).collect(Collectors.toList());

			return files;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new LinkedList<String>();
	}
	
	public long getItemSize(String item) {
		File file = new File(item);
	    return file.exists() ? file.length() : 0;
	}
	
	public String getItemType(String item) {
		int pos = item.lastIndexOf('.');
		if (-1 == pos)
			return "";
		return item.substring(pos+1).toUpperCase();
	}
	
	public String getItemImage(String item) {
		String type = getItemType(item);
		if (type.equalsIgnoreCase("TXT"))
			return "/images/TXT.png";
		else if (type.equalsIgnoreCase("ZIP"))
			return "/images/ZIP.png";
		
		StringBuffer imgPath = new StringBuffer();
		imgPath.append("/images/");
		imgPath.append(type);
		imgPath.append(".png");
		
		StringBuffer absPath = new StringBuffer();
		
		try {
			absPath.append("static");
			absPath.append(imgPath.toString());
			File file = new ClassPathResource(
					absPath.toString()).getFile();
			if (file.exists())
				return imgPath.toString();
		}
		catch (IOException e) {
			logger.error("An error occurred: {}", e.toString());
		}
		
		return "/images/OTHER.png";
	}
}
