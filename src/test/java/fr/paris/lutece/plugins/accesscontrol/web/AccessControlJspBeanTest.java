/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES LOSS OF USE, DATA, OR PROFITS OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.accesscontrol.web;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import java.util.List;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.LocalVariables;
import java.io.IOException;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlHome;
import java.sql.Date;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.portal.web.l10n.LocaleService;
/**
 * This is the business class test for the object AccessControl
 */
public class AccessControlJspBeanTest extends LuteceTestCase
{
    private static final String NAME1 = "Name1";
    private static final String NAME2 = "Name2";
    private static final String DESCRIPTION1 = "Description1";
    private static final String DESCRIPTION2 = "Description2";
	private static final Date CREATIONDATE1 = new Date( 1000000l );
    private static final Date CREATIONDATE2 = new Date( 2000000l );
	private static final boolean ISENABLED1 = true;
    private static final boolean ISENABLED2 = false;
    private static final String WORKGROUPKEY1 = "WorkgroupKey1";
    private static final String WORKGROUPKEY2 = "WorkgroupKey2";

public void testJspBeans(  ) throws AccessDeniedException
	{	
     	MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockServletConfig config = new MockServletConfig();

		//display admin AccessControl management JSP
		AccessControlJspBean jspbean = new AccessControlJspBean();
		String html = jspbean.getManageAccessControls( request );
		assertNotNull(html);

		//display admin AccessControl creation JSP
		html = jspbean.getCreateAccessControl( request );
		assertNotNull(html);

		//action create AccessControl
		request = new MockHttpServletRequest();

        request.addParameter( "name" , NAME1 );
        request.addParameter( "description" , DESCRIPTION1 );
        request.addParameter( "creation_date" , DateUtil.getDateString( CREATIONDATE1, LocaleService.getDefault( ) ) );
        request.addParameter( "is_enabled" , String.valueOf( ISENABLED1) );
        request.addParameter( "workgroup_key" , WORKGROUPKEY1 );
		request.addParameter("action","createAccessControl");
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createAccessControl" ));
		request.setMethod( "POST" );
		response = new MockHttpServletResponse( );
		AdminUser adminUser = new AdminUser( );
		adminUser.setAccessCode( "admin" );

		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response ); 
			
			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}

		//display modify AccessControl JSP
		request = new MockHttpServletRequest();
        request.addParameter( "name" , NAME1 );
        request.addParameter( "description" , DESCRIPTION1 );
        request.addParameter( "creation_date" , DateUtil.getDateString( CREATIONDATE1, LocaleService.getDefault( ) ) );
        request.addParameter( "is_enabled" , String.valueOf( ISENABLED1) );
        request.addParameter( "workgroup_key" , WORKGROUPKEY1 );
		List<Integer> listIds = AccessControlHome.getIdAccessControlsList();
        assertTrue( !listIds.isEmpty( ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		jspbean = new AccessControlJspBean();

		assertNotNull( jspbean.getModifyAccessControl( request ) );

		//action modify AccessControl
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
        request.addParameter( "name" , NAME2 );
        request.addParameter( "description" , DESCRIPTION2 );
        request.addParameter( "creation_date" , DateUtil.getDateString( CREATIONDATE2, LocaleService.getDefault( ) ) );
        request.addParameter( "is_enabled" , String.valueOf( ISENABLED2) );
        request.addParameter( "workgroup_key" , WORKGROUPKEY2 );
		request.setRequestURI("jsp/admin/plugins/example/ManageAccessControls.jsp");
		//important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createAccessControl, qui est l'action par défaut
		request.addParameter("action","modifyAccessControl");
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyAccessControl" ));
		adminUser = new AdminUser();
		adminUser.setAccessCode("admin");

		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response ); 

			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}
		
		//get remove AccessControl
		request = new MockHttpServletRequest();
        //request.setRequestURI("jsp/admin/plugins/example/ManageAccessControls.jsp");
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		jspbean = new AccessControlJspBean();
		request.addParameter("action","confirmRemoveAccessControl");
		assertNotNull( jspbean.getModifyAccessControl( request ) );
				
		//do remove AccessControl
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setRequestURI("jsp/admin/plugins/example/ManageAccessControlts.jsp");
		//important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createAccessControl, qui est l'action par défaut
		request.addParameter("action","removeAccessControl");
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeAccessControl" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		request.setMethod("POST");
		adminUser = new AdminUser();
		adminUser.setAccessCode("admin");

		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response ); 

			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}	
     
     }
}
