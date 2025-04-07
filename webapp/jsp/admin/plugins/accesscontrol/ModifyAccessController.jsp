<%@page import="fr.paris.lutece.plugins.accesscontrol.web.AccessControlJspBean"%>
<%@ page errorPage="../../ErrorPage.jsp" %>
${ accessControlJspBean.init( pageContext.request, AccessControlJspBean.RIGHT_MANAGE_ACCESS_CONTROL ) }
${ pageContext.response.sendRedirect( accessControlJspBean.doModifyConfigController( pageContext.request ) ) }
