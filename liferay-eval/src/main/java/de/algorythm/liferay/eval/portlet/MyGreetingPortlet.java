package de.algorythm.liferay.eval.portlet;
import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;

import com.liferay.util.bridges.mvc.MVCPortlet;


public class MyGreetingPortlet extends MVCPortlet {

	@Override
    public void processAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
        throws IOException, PortletException {
		
        final String greeting = actionRequest.getParameter("greeting");

        if (greeting != null) {
        	final PortletPreferences prefs = actionRequest.getPreferences();
            
        	prefs.setValue("greeting", greeting);
            prefs.store();
        }

        super.processAction(actionRequest, actionResponse);
    }
}