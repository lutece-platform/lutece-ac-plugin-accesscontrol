<jsp:useBean id="manageusercodes" scope="session" class="fr.paris.lutece.plugins.accesscontrol.web.ControllerUserCodeJspBean" />
<% String strContent = manageusercodes.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
