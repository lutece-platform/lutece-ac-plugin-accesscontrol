/*
 * Copyright (c) 2002-2026, City of Paris
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.template.HtmlMarkup;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Renders the plugin-accesscontrol pages that hold HTML built by a controller type or a data handler, with HTML
 * auto-escaping (LUT-33153): the front office form of a controller (controller_view.html), the configuration form of a
 * controller (modify_controller.html) and the configuration form of a data handler (data_handler_config.html), marked
 * with HtmlMarkup by plugin-accesscontrol, keep their tags; a fragment left as a String prints as text; the data around
 * them is escaped; the macro attributes are maps. The core macros the templates call are replaced by stand-ins that write
 * their arguments (escaped), their params map and their nested content: only the FreeMarker engine is needed.
 */
public class AccessControlTemplatesRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String TEMPLATE_CONTROLLER_VIEW = "skin/plugins/accesscontrol/controller_view.html";
    private static final String TEMPLATE_MODIFY_CONTROLLER = "admin/plugins/accesscontrol/modify_controller.html";
    private static final String TEMPLATE_DATA_HANDLER_CONFIG = "admin/plugins/accesscontrol/config/data_handler_config.html";
    private static final String TEMPLATE_COMMENT_CONFIG = "admin/plugins/accesscontrol/config/comment_controller_config.html";
    private static final String TEMPLATE_MANAGE_USERCODES = "admin/plugins/accesscontrol/manage_usercodes.html";

    /** Stand-ins of the core macros the templates call; the ones taking params write it as attributes, so it must be a map. */
    private static final String MACROS = "accesscontrol_test_macros.ftl";
    private static final String MACROS_SOURCE = "<#macro attributes params><#list params as key, value> ${key}<#if !value?is_boolean>=\"${value}\"</#if></#list></#macro>"
            // front office
            + "<#macro cTpl rest...><#nested></#macro><#macro cRow rest...><#nested></#macro><#macro cCol rest...><#nested></#macro>"
            + "<#macro cForm action='' rest...><form action=\"${action}\"><#nested></form></#macro>"
            + "<#macro cAlert title='' rest...><div class=\"alert\"><p>${title}</p><#nested></div></#macro>"
            + "<#macro chList rest...><ul><#nested></ul></#macro><#macro chItem rest...><li><#nested></li></#macro>"
            + "<#macro cBtn label params={} rest...><button<@attributes params />>${label}</button></#macro>"
            // back office
            + "<#macro pageContainer rest...><#nested></#macro><#macro pageColumn rest...><#nested></#macro>"
            + "<#macro pageHeader title='' rest...><h1>${title}</h1><#nested></#macro>"
            + "<#macro tform action='' enctype='' params={} rest...><form action=\"${action}\"<#if enctype?has_content> enctype=\"${enctype}\"</#if><@attributes params />><#nested></form></#macro>"
            + "<#macro fieldSet rest...><fieldset><#nested></fieldset></#macro><#macro formGroup rest...><div class=\"form-group\"><#nested></div></#macro>"
            + "<#macro row rest...><#nested></#macro><#macro columns rest...><#nested></#macro><#macro inputGroup rest...><#nested></#macro>"
            + "<#macro input name='' value='' type='' rest...><#if type == 'textarea'><textarea name=\"${name}\"><#nested></textarea>"
            + "<#else><input type=\"${type}\" name=\"${name}\" value=\"${value}\"></#if></#macro>"
            + "<#macro select name='' items=[] rest...><select name=\"${name}\"><#list items as item><option value=\"${item.code}\">${item.name}</option></#list></select></#macro>"
            + "<#macro button name='' title='' params={} rest...><button name=\"${name}\"<@attributes params />>${title}</button></#macro>"
            + "<#macro aButton rest...></#macro>";
    /** Stand-in of the core editor include of the configuration forms. */
    private static final String EDITOR = "admin/util/editor/editor.html";
    private static final String EDITOR_SOURCE = "<#macro initEditor rest...></#macro>";

    private static final String CONTROLLER_FORM = "<div class=\"row\"><p>Please enter your <strong>code</strong></p>"
            + "<label for=\"code\">Code</label><input type=\"text\" id=\"code\" name=\"code\"></div>";
    private static final String CONFIG_FORM = "<div class=\"mb-3\"><label for=\"comment\">Comment</label>"
            + "<textarea name=\"comment\">&lt;p&gt;Hello&lt;/p&gt;</textarea></div>";
    private static final String HANDLER_FORM = "<div class=\"mb-3\"><select name=\"id_form\"><option value=\"1\">Form &lt;one&gt;</option></select></div>";
    private static final String HOSTILE = "Wrong <b>x</b> \"q\"";
    private static final String HOSTILE_ESCAPED = "Wrong &lt;b&gt;x&lt;/b&gt; &quot;q&quot;";

    /**
     * Renders a template of the plugin with the core configuration of the output format.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the HTML
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, Object> model ) throws IOException, TemplateException
    {
        StringTemplateLoader stubs = new StringTemplateLoader( );
        stubs.putTemplate( MACROS, MACROS_SOURCE );
        stubs.putTemplate( EDITOR, EDITOR_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                stubs, new FileTemplateLoader( new File( TEMPLATES_ROOT ) )
        } ) );
        cfg.addAutoInclude( MACROS );
        StringWriter writer = new StringWriter( );
        cfg.getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * The model of the front office page of an access control.
     *
     * @param controllerHtml
     *            the form of the controller
     * @return the model
     */
    private static Map<String, Object> controllerViewModel( Object controllerHtml )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "controller_html", controllerHtml );
        model.put( "first_controller", false );
        model.put( "warnings", List.of( Map.of( "message", HOSTILE ) ) );
        model.put( "messageInfo", HOSTILE );

        return model;
    }

    /**
     * The front office page writes the controller form as HTML, escapes the messages and names its buttons through a
     * params map.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testControllerViewKeepsTheControllerForm( ) throws Exception
    {
        String strHtml = render( TEMPLATE_CONTROLLER_VIEW, controllerViewModel( HtmlMarkup.of( CONTROLLER_FORM ) ) );

        assertTrue( strHtml.contains( CONTROLLER_FORM ), strHtml );
        assertTrue( strHtml.contains( "<li>" + HOSTILE_ESCAPED + "</li>" ), strHtml );
        assertTrue( strHtml.contains( "<p>" + HOSTILE_ESCAPED + "</p>" ), strHtml );
        assertFalse( strHtml.contains( HOSTILE ), strHtml );
        // The back button skips the browser validation: a required field of the step must not block going back
        assertTrue( strHtml.contains( "<button name=\"action_doReturnController\" formnovalidate>" ), strHtml );
        assertTrue( strHtml.contains( "<button name=\"action_doValidateController\">" ), strHtml );
    }

    /**
     * A controller form left as a String is escaped: the reason plugin-accesscontrol marks it.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testAStringControllerFormIsEscaped( ) throws Exception
    {
        String strHtml = render( TEMPLATE_CONTROLLER_VIEW, controllerViewModel( CONTROLLER_FORM ) );

        assertFalse( strHtml.contains( CONTROLLER_FORM ), strHtml );
        assertTrue( strHtml.contains( "&lt;div class=&quot;row&quot;&gt;" ), strHtml );
    }

    /**
     * The configuration page of a controller writes the controller's configuration form as HTML, and its cancel button
     * skips the validation of the form through a params map.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testModifyControllerKeepsTheConfigForm( ) throws Exception
    {
        String strHtml = render( TEMPLATE_MODIFY_CONTROLLER, Map.of( "controller_config", HtmlMarkup.of( CONFIG_FORM ) ) );

        assertTrue( strHtml.contains( CONFIG_FORM ), strHtml );
        assertTrue( strHtml.contains( "<button name=\"action_cancelModifyConfigController\" formnovalidate>" ), strHtml );

        String strEscaped = render( TEMPLATE_MODIFY_CONTROLLER, Map.of( "controller_config", CONFIG_FORM ) );
        assertFalse( strEscaped.contains( CONFIG_FORM ), strEscaped );
        assertTrue( strEscaped.contains( "&lt;div class=&quot;mb-3&quot;&gt;" ), strEscaped );
    }

    /**
     * The configuration form of a controller writes the data handler's configuration form as HTML and escapes the names
     * of the data handlers.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testDataHandlerConfigKeepsTheHandlerForm( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "config", Map.of( "dataHandler", "forms.dataHandler" ) );
        model.put( "data_handler_list", List.of( Map.of( "code", "", "name", "-" ), Map.of( "code", "forms.dataHandler", "name", HOSTILE ) ) );
        model.put( "data_handler_config", HtmlMarkup.of( HANDLER_FORM ) );
        String strHtml = render( TEMPLATE_DATA_HANDLER_CONFIG, model );

        assertTrue( strHtml.contains( HANDLER_FORM ), strHtml );
        assertTrue( strHtml.contains( "<option value=\"forms.dataHandler\">" + HOSTILE_ESCAPED + "</option>" ), strHtml );
        assertFalse( strHtml.contains( HOSTILE ), strHtml );

        model.put( "data_handler_config", HANDLER_FORM );
        String strEscaped = render( TEMPLATE_DATA_HANDLER_CONFIG, model );
        assertFalse( strEscaped.contains( HANDLER_FORM ), strEscaped );
    }

    /**
     * The rich text comment is escaped in the textarea of the editor: a closing textarea tag in it does not end the
     * element, and the browser gives the editor the same HTML.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testConfigCommentIsTextInTheTextarea( ) throws Exception
    {
        Map<String, Object> config = new HashMap<>( );
        config.put( "idAccessController", 5 );
        config.put( "comment", "<p>Hello</p></textarea><script>alert(1)</script>" );
        String strHtml = render( TEMPLATE_COMMENT_CONFIG, Map.of( "config", config ) );

        assertTrue( strHtml.contains( "<textarea name=\"comment\">&lt;p&gt;Hello&lt;/p&gt;&lt;/textarea&gt;&lt;script&gt;alert(1)&lt;/script&gt;</textarea>" ),
                strHtml );
        assertFalse( strHtml.contains( "<script>" ), strHtml );
    }

    /**
     * The import form of the user codes sets its encoding with the enctype parameter of the macro.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testUserCodesFormIsMultipart( ) throws Exception
    {
        String strHtml = render( TEMPLATE_MANAGE_USERCODES, Map.of( "access_control_list", List.of( Map.of( "code", "3", "name", HOSTILE ) ) ) );

        assertTrue( strHtml.contains( "enctype=\"multipart/form-data\"" ), strHtml );
        assertTrue( strHtml.contains( "<option value=\"3\">" + HOSTILE_ESCAPED + "</option>" ), strHtml );
    }
}
