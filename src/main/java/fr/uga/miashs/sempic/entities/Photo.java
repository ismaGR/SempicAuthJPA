/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;


import java.io.InputStream;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

/**
 *
 * @author inhoa
 */

@NamedQueries({
@NamedQuery(
        name = "findPhotoAlbum",
        query = "SELECT DISTINCT p FROM Photo p WHERE p.album=:album"
)
})        
@Entity
public class Photo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long photoId;
    
    @ManyToOne
    private Album album;
    
    private String filename;
    
    

    public long getPhotoId(){
        return photoId;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return getAlbum().getAlbumId()+"/"+getPhotoId();
    }

    
    
    
    
    
}
