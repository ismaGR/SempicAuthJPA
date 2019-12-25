/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.util;

import com.sun.faces.renderkit.html_basic.FileRenderer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "javax.faces.File")
public class MultipleFileRenderer extends FileRenderer {
    


    @Override
    public void decode(FacesContext context, UIComponent component) {
        rendererParamsNotNull(context, component);

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

        if (clientId == null) {
            clientId = component.getClientId(context);
        }

        assert (clientId != null);
        ExternalContext externalContext = context.getExternalContext();
        Map<String, String> requestMap = externalContext.getRequestParameterMap();
        if (requestMap.containsKey(clientId)) {
            setSubmittedValue(component, requestMap.get(clientId));
        }

        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            List<Part> partsList = new ArrayList<>();
            Collection<Part> parts = request.getParts();
            for (Part cur : parts) {
                if (clientId.equals(cur.getName())) {
                    component.setTransient(true);
                    partsList.add(cur);
                }
            }
            setSubmittedValue(component, partsList);
        } catch (IOException | ServletException ioe) {
            throw new FacesException(ioe);
        }

    }

}
    
    //--------------------//
/*
    @Override
    public void decode(FacesContext context, UIComponent component) {
        rendererParamsNotNull(context, component);

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = decodeBehaviors(context, component);

        if (clientId == null) {
            clientId = component.getClientId(context);
        }

        assert (clientId != null);
        ExternalContext externalContext = context.getExternalContext();
        Map<String, String> requestMap = externalContext.getRequestParameterMap();
        if (requestMap.containsKey(clientId)) {
            setSubmittedValue(component, requestMap.get(clientId));
        }

        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            List<Part> partsList = new ArrayList<>();
            Collection<Part> parts = request.getParts();
            for (Part cur : parts) {
                if (clientId.equals(cur.getName())) {
                    component.setTransient(true);
                    partsList.add(cur);
                }
            }
            setSubmittedValue(component, partsList);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        } catch (ServletException se) {
            throw new FacesException(se);
        }

    }
*/
