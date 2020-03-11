package ru.rz.musiCat.data.entities;

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
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "rootfolders")
public class RootFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotEmpty
    private String path;
    
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTraversed;
    
    //@NotEmpty
    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private Host host;
    
    private Boolean problem;
    
    public Long getHostId() { return host.getId(); }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public Date getLastTraversed() { return lastTraversed; }
    public void setLastTraversed(Date lastTraversed) { this.lastTraversed = lastTraversed; }
    
    public Host getHost() { return host; }
    public void setHost(Host host) { this.host = host; }
    
    public Boolean isProblem() { return problem; }
    public void setProblem(Boolean problem) { this.problem = problem; }
}
