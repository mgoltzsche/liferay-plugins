<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="javax.portlet.PortletPreferences" %>

<portlet:defineObjects />

<p>
This is the <b>liferay-eval</b><br />.
</p>

<p>asdf
<%
PortletPreferences prefs = renderRequest.getPreferences();

String greeting = (String) prefs.getValue("greeting", "Hello! Welcome to our portal.");
%>
<%=greeting%>
<portlet:renderURL var="editGreetingURL">
    <portlet:param name="jspPage" value="/edit.jsp" />
</portlet:renderURL>
<br />
<a href="<%=editGreetingURL%>">change</a><br />
<portlet:renderURL var="jspxURL">
    <portlet:param name="jspPage" value="/test.jspx" />
</portlet:renderURL>
<a href="<%=jspxURL%>">test</a><br />
</p>
