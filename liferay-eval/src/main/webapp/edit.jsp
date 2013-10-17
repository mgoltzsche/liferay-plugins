<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="javax.portlet.PortletPreferences" %>

<portlet:defineObjects />

<%
PortletPreferences prefs = renderRequest.getPreferences();

String greeting = (String)prefs.getValue(
    "greeting", "Hello! Welcome to our portal.");
%>

<portlet:actionURL var="editGreetingURL">
    <portlet:param name="jspPage" value="/edit.jsp" />
</portlet:actionURL>

<form action="<%=editGreetingURL%>" method="post">
    greeting: <input name="greeting" type="text" value="<%= greeting %>" />
    <button type="submit">change</button>
</form>

<portlet:renderURL var="viewGreetingURL">
    <portlet:param name="jspPage" value="/view.jsp" />
</portlet:renderURL>

<p><a href="<%=viewGreetingURL%>">Back</a></p>