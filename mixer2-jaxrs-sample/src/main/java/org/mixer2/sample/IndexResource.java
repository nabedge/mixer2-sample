package org.mixer2.sample;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.Div;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.xhtml.PathAjuster;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;

/**
 * http://localhost:8080/mixer2-jaxrs-sample/ (without message replace)
 * http://localhost:8080/mixer2-jaxrs-sample/?message=hello
 *
 */
@Path("/")
public class IndexResource {

    private Mixer2Engine mixer2Engine = Mixer2EngineSingleton.getInstance();

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Html index(@QueryParam(value = "message") String message)
            throws IOException, TagTypeUnmatchException {

        // loading template
        String template = "m2mockup/m2template/index.html";
        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream(template);
        Html html = mixer2Engine.loadHtmlTemplate(is);
        
        // replace content in <div id="message"> tag
        if (message != null) {
            Div div = html.getBody().getById("message", Div.class);
            div.unsetContent();
            div.getContent().add(message);
        }
        
        // replace static file path
        Pattern pattern = Pattern.compile("^\\.+/.*m2static/(.*)$");
        PathAjuster.replacePath(html, pattern, "m2static/$1");
        
        // return html object
        return html;
    }
}
