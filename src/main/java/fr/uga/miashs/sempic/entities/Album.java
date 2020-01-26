/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author inhoa
 */



@Entity
@NamedQueries({
@NamedQuery(
        name = "findUserAlbums",
        query = "SELECT DISTINCT a FROM Album a WHERE a.owner=:owner"
)
//,
//    
//@NamedQuery(
//        name = "findAllAlbums",
//        query = "SELECT DISTINCT a FROM Album a WHERE a.owner=:owner"
//)
})
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "UniqueAlbumNameForUser", columnNames = {"title","owner_id"})
})
public class Album implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long albumId;
    
    //private Set<CreatePhoto> album;
   

    @NotNull
    @ManyToOne
    private SempicUser owner;

    @NotBlank
    private String title="title";
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy="album")
    private Set<Photo> photos;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<SempicGroup> groups;


    public Album() {}
    
    public Album(SempicUser u) {
        owner = u;
    }

    public Long getAlbumId(){
        return albumId;
    }

    public void setPhotos(Set<Photo> photos){
        this.setPhotos(photos);
    }
    public void delPhoto(Photo photo){
        this.photos.remove(photo);
    }
    public void setAlbumId(Long id){
        this.albumId=id;
    }

    public SempicUser getOwner() {
        return owner;
    }
    public void setOwner(SempicUser owner) {
        this.owner= owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<SempicGroup> getGroups() {
        return groups;
    }
    public void addGroup(SempicGroup group){
        if (groups==null) {
            groups = new HashSet<>();
        }        
        groups.add(group);
    }
    public void setGroups(Set<SempicGroup> groups){
        this.groups=groups;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }
    public void addPhoto(Photo photo){
        this.photos.add(photo);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (int) (this.albumId ^ (this.albumId >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Album other = (Album) obj;
        if (this.albumId != other.albumId) {
            return false;
        }
        return true;
    }
    
}
