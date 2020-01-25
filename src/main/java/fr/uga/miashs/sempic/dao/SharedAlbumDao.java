/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import fr.uga.miashs.sempic.entities.Album;
import fr.uga.miashs.sempic.entities.SharedAlbum;
import fr.uga.miashs.sempic.entities.Photo;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.util.List;
import javax.ejb.Stateless;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;



@Stateless
public class SharedAlbumDao extends AbstractJpaFacade<Long,SharedAlbum>{
    
    
     public SharedAlbumDao(){
        super(SharedAlbum.class);
    }
    
    
    public List<SharedAlbum> getByReceiver(SempicUser receiver ) throws NoResultException{
        Query q;
        try{ 
         q = getEntityManager().createNamedQuery("getByReceiver").setParameter("receiver", receiver);
               return q.getResultList() ;
        }  catch (NoResultException e) {
            return null;
        }
    }
    
    public List<SharedAlbum> getByAlbum(Album album)throws NoResultException{
        Query q;
        try{
            q = getEntityManager().createNamedQuery("getByAlbum").setParameter("album", album);
             return  q.getResultList();
        }  catch (NoResultException e) {
            return null;
        }
    }
    
    public SharedAlbum getById(Long id)throws NoResultException{
        Query q;
        try{            
            q= getEntityManager().createNamedQuery("getById").setParameter("id", id);
            return (SharedAlbum)  q.getSingleResult();
        }  catch (NoResultException e) {
            return null;
        }
    }
    
}
