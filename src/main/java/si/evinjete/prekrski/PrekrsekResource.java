package si.evinjete.prekrski;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("prekrski")
public class PrekrsekResource {

    @Inject
    private PrekrsekService prekrsekBean;

    @PersistenceContext
    private EntityManager em;

    @GET
    public Response getAllPrekrski() {
//        Prekrsek p = new Prekrsek();
//        p.setTimestamp(new Date());
//        p.setLocation("neka_lokacija");
//        p.setNumberPlate("KPVK313");
//
//        em.getTransaction().begin();
//        em.persist(p);
//        em.getTransaction().commit();

        List<Prekrsek> prekrski = prekrsekBean.getPrekrski();
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

        if (prekrsek.getNumberPlate() == null || prekrsek.getLocation() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            prekrsek = prekrsekBean.addNewPrekrsek(prekrsek);
        }

        prekrsek.setTimestamp(new Date());
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
