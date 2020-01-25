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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author denisbolshakov
 */

@Entity
@NamedQueries({
    @NamedQuery(
            name = "getByReceiver",
            query = "SELECT DISTINCT a FROM SharedAlbum a WHERE a.receiver=:receiver"
    ),    
    @NamedQuery(
            name = "getByAlbum",
            query = "SELECT DISTINCT a FROM SharedAlbum a WHERE a.album=:album"
    ),
    @NamedQuery(
        name = "getById",
        query = "SELECT DISTINCT a FROM SharedAlbum a WHERE a.id=:id"
)
})
@Table
public class SharedAlbum implements Serializable{
  
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private Long receiverId;
    
    private Long albumId;
    
    @NotNull
    @ManyToOne
    private SempicUser receiver;
    
    @NotNull
    @ManyToOne
    private Album album;
    
    public SharedAlbum ( ){
       
    }
    
    public SharedAlbum (Album album, SempicUser receiver ){
        this.album = album;
        this.receiver = receiver;
        this.receiverId = receiver.getId();
        this.albumId = album.getAlbumId();
    }
    
    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
    
    public SempicUser getReceiver() {
        return receiver;
    }

    public void setReceiver(SempicUser receiver) {
        this.receiver = receiver;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
    
    
}
