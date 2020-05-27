package ru.rz.musiCat.data.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "images")
//@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "lastUpdated"}, 
        allowGetters = true)
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fileName;

//    @NotBlank
//    private String folderName;
    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    private RootFolder folder;
    
    @ManyToOne
    @JoinColumn(name = "album_id", nullable = true)
    private Album album;
    
    private String description;
    
//    @Column(nullable = false)
//    @Temporal(TemporalType.TIMESTAMP)
//    @LastModifiedDate
//    private Date lastUpdated;
    // version?
    
    private String hash;
    
    private Boolean removed;
    
    public Image() {}
    
    public Image(String fileName, RootFolder folder) {
    	this.fileName = fileName;
    	this.folder = folder;
    	this.removed = Boolean.FALSE;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Boolean isRemoved() { return removed; }
    public void setIsRemoved(Boolean removed) { this.removed = removed; }
    
    public Image notRemoved() 
    {
    	setIsRemoved(Boolean.FALSE);
    	return this;
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public RootFolder getFolder() { return folder; }
    
    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }
}
