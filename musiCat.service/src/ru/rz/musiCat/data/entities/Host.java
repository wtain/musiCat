package ru.rz.musiCat.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hosts")
public class Host {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Getter
    //@Setter
    private Long id;
    
    public Host() {}
    
    public Host(String physicalId, String address) {
    	this.physicalId = physicalId;
    	this.networkAddress = address;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
//    @Getter
//    @Setter
    @NotEmpty
    private String networkAddress;
    
    public String getNetworkAddress() { return networkAddress; }
    public void setNetworkAddress(String newNetworkAddress) { networkAddress = newNetworkAddress; }    
    
//    @Getter
//    @Setter
    @NotEmpty
    private String name;
    
    public String getName() { return name; }
    public void setName(String newName) { name = newName; }
    
//    @Getter
//    @Setter
    @NotEmpty
    @Column(unique=true)    
    private String physicalId;
    
    public String getPhysicalId() { return physicalId; }
    public void setPhysicalId(String newPhysicalId) { physicalId = newPhysicalId; }
    
//    @Getter
//    @Setter
    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastUpdated;
    
    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
}
