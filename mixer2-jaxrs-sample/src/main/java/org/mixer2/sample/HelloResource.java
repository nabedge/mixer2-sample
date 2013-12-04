package org.mixer2.sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("hello")
public class HelloResource {

    @GET
    public String say(@QueryParam("name") String name) {
        return "Hello, " + name + "!";
    }
}
