<%@page import="fr.paris.lutece.plugins.accesscontrol.web.AccessControlJspBean"%>
<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="manageaccesscontrolAccessControl" scope="session" class="fr.paris.lutece.plugins.accesscontrol.web.AccessControlJspBean" />
<% 
	manageaccesscontrolAccessControl.init( request, AccessControlJspBean.RIGHT_MANAGE_ACCESS_CONTROL ); 
	response.sendRedirect( manageaccesscontrolAccessControl.doModifyConfigController(request) );
%>
