<jsp:useBean id="manageaccesscontrolAccessControl" scope="session" class="fr.paris.lutece.plugins.accesscontrol.web.AccessControlJspBean" />
<% String strContent = manageaccesscontrolAccessControl.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
