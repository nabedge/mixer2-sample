package org.mixer2.sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.mixer2.jaxb.xhtml.Body;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.xhtml.TagCreator;

@Path("hello")
public class HelloResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Html say(@QueryParam("name") String name) {
        Html html = TagCreator.html();
        Body body = TagCreator.body();
        body.getContent().add("Hello, " + name + "!!");
        html.setBody(body);
        return html;
    }
}
