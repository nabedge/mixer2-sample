package org.mixer2.sample;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/m2static")
public class StaticResource {

    @Path("{path : .+}")
    @GET
    public Response getImage(@PathParam("path") String path) {
        
        // get extention link "jpg","png","gif"...
        String extension = path.substring(path.length() - 3, path.length());
        path = "m2mockup/m2static/" + path;
        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream(path);
        
        // response
        return Response.ok(is, "image/" + extension).build();
    }
}
