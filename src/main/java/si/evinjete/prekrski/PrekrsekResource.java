package si.evinjete.prekrski;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("prekrski")
public class PrekrsekResource {

    private WebTarget wb;

    @Inject
    private PrekrsekService prekrsekBean;
    @Inject
    private SlikaService slikaService;

    @PersistenceContext
    private EntityManager em;

    @GET
    public Response getAllPrekrski() {
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

    @POST
    @Path("zaznaj")
    @Consumes("image/jpeg")
    public Response detectPrekrsek(@QueryParam("kraj") String location, InputStream uploadedInputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[4];

        while ((nRead = uploadedInputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] targetArray = buffer.toByteArray();

        Slika slika = new Slika();
        slika.setContent(targetArray);
        slika.setTimestamp(new Date());
        slika.setLocation(location);

        Client client = ClientBuilder.newClient();
        wb = client.target("http://localhost:8081/v1/upload/slika");
        String response = wb.request(MediaType.APPLICATION_JSON).post(Entity.json(slika), String.class);

        slika.setNumberPlate(response);
        slikaService.addNewSlika(slika);

//        return Response.noContent().build();
        return Response.status(200).build();
    }

    @DELETE
    @Path("{prekrsekId}")
    public Response deletePrekrsek(@PathParam("prekrsekId") String prekrsekId) {
        prekrsekBean.deletePrekrsek(prekrsekId);
        return Response.noContent().build();
    }
}
