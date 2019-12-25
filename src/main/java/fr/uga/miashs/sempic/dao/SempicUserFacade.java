/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.dao;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.util.List;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */

@Stateless
public class SempicUserFacade extends AbstractJpaFacade<Long,SempicUser> {

    @Inject
    private transient Pbkdf2PasswordHash hashAlgo;
    
    public SempicUserFacade() {
        super(SempicUser.class);
    }
    
      
    @Override
    public Long create(SempicUser user) throws SempicModelException {
        if (user.getPassword()!=null) {
            user.setPasswordHash(hashAlgo.generate(user.getPassword().toCharArray()));
        }
        return super.create(user);
    }
    
    public SempicUser readEager (long id){
        EntityGraph entityGraph = this.getEntityManager().getEntityGraph("graph.SempicUser.groups-memberOf");
        return (SempicUser) getEntityManager().createQuery("SELECT u FROM SempicUser u WHERE u.id=:id")
                .setParameter("id", id)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getSingleResult();
    }
    
    @Override
    public List<SempicUser> findAll() {
        EntityGraph entityGraph = this.getEntityManager().getEntityGraph("graph.SempicUser.groups-memberOf");
        return getEntityManager().createQuery(this.findAllQuery())
            .setHint("javax.persistence.fetchgraph", entityGraph)
            .getResultList();
    }
    
    /*public SempicUser login(String email, String password) {
        Query q = getEntityManager().createNamedQuery("login");
        q.setParameter("email", email);
        q.setParameter("password", password);
        return (SempicUser) q.getSingleResult();
    }*/

    
    
    public SempicUser login(String email, String password) throws SempicModelException {
        Query q = getEntityManager().createNamedQuery("query.SempicUser.readByEmail");
        q.setParameter("email", email);
        SempicUser u =  (SempicUser) q.getSingleResult();
        if (hashAlgo.verify(password.toCharArray(), u.getPasswordHash())) {
            return u;
        }
        throw new SempicModelException("login failed");
    }

    public SempicUser readByEmail(String email) {
        Query q = getEntityManager().createNamedQuery("query.SempicUser.readByEmail");
        q.setParameter("email", email);
        return (SempicUser) q.getSingleResult();
    }
    
     
    
    
   
    
    
}
