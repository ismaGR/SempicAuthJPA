package fr.uga.miashs.sempic.restservice;


import fr.uga.miashs.sempic.dao.SempicUserService;
import fr.uga.miashs.sempic.ApplicationConfig;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author inhoa
 */
@ApplicationPath(ApplicationConfig.WEB_API)
public class RestApplication extends Application{
    public RestApplication(){
        
    }
    
    public Set<Class<?>> getClasses(){
        Set res = new HashSet();
        String[] features = { "org.glassfish.jersey.moxy.json.MoxyJsonFeature",
    };
        for (String fName : features){
            try{
                Class cls = Class.forName(fName);
                res.add(cls);
                
            }catch (ClassNotFoundException ex){
                Logger.getLogger(RestApplication.class.getName()).log(Level.WARNING,fName+"not available");
            }
        }
        res.add(MOXyJsonProvider.class);
        res.add(SempicUserService.class);
        
        
        
        return res;
    
}

}
