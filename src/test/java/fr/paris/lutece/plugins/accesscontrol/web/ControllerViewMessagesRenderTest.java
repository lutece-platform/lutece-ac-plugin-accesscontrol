/*
 * Copyright (c) 2002-2022, City of Paris
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
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.accesscontrol.web;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.util.mvc.utils.MVCMessage;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;

/**
 * The front office controller view lists the warnings of the model. Since lutece-core LUT-30580 the front office
 * MVCApplication.addWarning stores plain strings (Set of String); older code and other callers give MVCMessage objects:
 * the template must render both.
 */
public class ControllerViewMessagesRenderTest
{
    private static final String TEMPLATE = "skin/plugins/accesscontrol/controller_view.html";
    private static final String MACROS = "accesscontrol_controller_view_test_macros.ftl";
    private static final String MACROS_SOURCE = "<#macro cTpl rest...><#nested></#macro><#macro cForm rest...><#nested></#macro>"
            + "<#macro cRow rest...><#nested></#macro><#macro cCol rest...><#nested></#macro><#macro cBtn rest...></#macro>"
            + "<#macro chList rest...><ul><#nested></ul></#macro><#macro chItem rest...><li><#nested></li></#macro>"
            + "<#macro cAlert title='' rest...><div class=\"alert\">${title}<#nested></div></#macro>";

    private static String render( Object warnings ) throws Exception
    {
        StringTemplateLoader stubs = new StringTemplateLoader( );
        stubs.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_32 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                stubs, new FileTemplateLoader( new File( "webapp/WEB-INF/templates" ) )
        } ) );
        cfg.addAutoInclude( MACROS );
        Map<String, Object> model = new HashMap<>( );
        model.put( "warnings", warnings );
        model.put( "first_controller", true );
        StringWriter writer = new StringWriter( );
        cfg.getTemplate( TEMPLATE ).process( model, writer );
        return writer.toString( );
    }

    /**
     * A warning added by the core front office MVCApplication (plain string) is shown.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testWarningAddedByTheCoreAsText( ) throws Exception
    {
        Set<String> warnings = new LinkedHashSet<>( );
        warnings.add( "Le code n'est pas valide" );
        String strHtml = render( warnings );
        assertTrue( strHtml.contains( "<li>Le code n&#39;est pas valide</li>" ), strHtml );
    }

    /**
     * A warning given as an MVCMessage is still shown.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testWarningGivenAsMessageObject( ) throws Exception
    {
        String strHtml = render( List.of( new MVCMessage( "Code expired" ) ) );
        assertTrue( strHtml.contains( "<li>Code expired</li>" ), strHtml );
    }
}
