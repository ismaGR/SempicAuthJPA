/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

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
    
    static String WEBAPI = "/webapi/picture";
    public static final String THUMB_WIDTH = "250";

    @NotNull
    @ManyToOne
    private Album album;
    
    private String filename;
    
    private String path;
    private String thumbpath;
    
    static String DIR = "/images/originals/";

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
        path =  WEBAPI+"/"+ getAlbum().getAlbumId()+"/"+getPhotoId();
        return path;
    }
    public String getThumbpath() {
        //webapi/picture/3/4?width=450
        thumbpath =  WEBAPI+"/"+ getAlbum().getAlbumId()+"/"+getPhotoId()+"?width="+THUMB_WIDTH;
        return thumbpath;
    }
    
    
    
    
}
