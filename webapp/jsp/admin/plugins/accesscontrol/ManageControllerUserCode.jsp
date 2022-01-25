<%@page import="fr.paris.lutece.plugins.accesscontrol.web.ControllerUserCodeJspBean"%>
<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="manageusercodes" scope="session" class="fr.paris.lutece.plugins.accesscontrol.web.ControllerUserCodeJspBean" />
<%
    manageusercodes.init( request, ControllerUserCodeJspBean.RIGHT_MANAGE_USER_CODES ); 
	response.sendRedirect( manageusercodes.getManageUserCode(request) );
%>
