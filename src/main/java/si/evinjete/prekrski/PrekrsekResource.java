package si.evinjete.prekrski;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("prekrski")
public class PrekrsekResource {

    @Inject
    private PrekrsekService prekrsekBean;

    @GET
    public Response getAllPrekrski() {
        List<Prekrsek> prekrski = prekrsekBean.getPrekrski();
        System.out.print("jooo");
        return Response.ok(prekrski).build();
    }

    @GET
    @Path("{prekrsekId}")
    public Response getPrekrsek(@PathParam("prekrsekId") String prekrsekId) {
        Prekrsek prekrsek = prekrsekBean.getPrekrsek(prekrsekId);
        return prekrsek != null
                ? Response.ok(prekrsek).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response addNewPrekrsek(Prekrsek prekrsek) {
        prekrsekBean.savePrekrsek(prekrsek);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{prekrsekId}")
    public Response deletePrekrsek(@PathParam("prekrsekId") String prekrsekId) {
        prekrsekBean.deletePrekrsek(prekrsekId);
        return Response.noContent().build();
    }
}
