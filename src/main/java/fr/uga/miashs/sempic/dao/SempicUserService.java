/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.dao;

import fr.uga.miashs.sempic.SempicException;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


/**
 *
 * @author inhoa
 */
//@ApplicationScoped
@Stateless //@Singleton
public class SempicUserService {
    
    // private Map<String, SempicUser> users;
    
    @Inject
    private SempicUserFacade userDao;
    
   /* @PostConstruct
    public void init() {
        users = new HashMap<>();
    } */
    
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<SempicUser> listAll(){
        
                List<SempicUser> res = userDao.findAll();
                return res;
    }
    
    @RolesAllowed("ADMIN")
    public List<SempicUser> findAll() {
      //  return new ArrayList<>(users.values());
         return new ArrayList<>(userDao.findAll());
    }
    
    
      @Path("/users/{id}")
      @Produces(MediaType.APPLICATION_XML)
      @GET
      public SempicUser get(@PathParam("id") long id){
          SempicUser su =(SempicUser)userDao.read(id);
          //obligé a charger car on est en lazy
          su.getGroups().size();su.getMemberOf().size();
          return su;
      }
      
      @Path("/users}")
      @Consumes(MediaType.APPLICATION_JSON)
      @Produces(MediaType.APPLICATION_XML)
      @POST
      public Response create(SempicUser su, @Context UriInfo uriInfo) throws Exception{
         
          long id = userDao.create(su);
          URI location = uriInfo.getRequestUriBuilder()
                  .path(String.valueOf(id))
                  .build();
                  return Response.created(location).entity(su).build();
      
    }
      
    /*   public void create(SempicUser u) throws SempicException {
        if (users.containsKey(u.getEmail())) {
            throw new SempicException("The users with email "+u.getEmail()+ " already exists");
        }
        users.put(u.getEmail(), u);
    }*/
      
    
}
