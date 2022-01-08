package si.evinjete.prekrski;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("prekrski")
public class PrekrsekResource {

    @Inject
    @DiscoverService(value = "anpr", version = "1.0.x", environment = "dev")
    private WebTarget anprTarget;

    @Inject
    private PrekrsekService prekrsekBean;
    @Inject
    private SlikaService slikaService;
    @Inject
    private PrekrskiProperties properties;

    @PersistenceContext
    private EntityManager em;

    @GET
    public Response getAllPrekrski() {
        List<Prekrsek> prekrski = prekrsekBean.getPrekrski();
        return Response.ok(prekrski).build();
    }

    @GET
    @Path("/config")
    public Response testConfig() {
        System.out.println("Recieved testConfig GET request.");
        Optional<String> test = ConfigurationUtil.getInstance().get("test");
        if(test.isPresent()){
            System.out.println("Read value test: " + test);
        }
        else{
            System.out.println("Couldnt read value. :(");
        }

        return Response.ok().build();

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

        WebTarget service = anprTarget.path("v1/upload/slika");
        System.out.println(service);
        String response = service.request(MediaType.APPLICATION_JSON).post(Entity.json(slika), String.class);

        slika.setNumberPlate(response);
        slikaService.addNewSlika(slika);
        System.out.println(response);

        return Response.status(200).build();
    }

    @DELETE
    @Path("{prekrsekId}")
    public Response deletePrekrsek(@PathParam("prekrsekId") String prekrsekId) {
        prekrsekBean.deletePrekrsek(prekrsekId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/slike/izbrisi")
    public Response deleteAllSlike() {
        List<Slika> rows = slikaService.deleteAllSlika(properties.getSlikeAgeProperty());
        return Response.ok(rows).build();
    }
}
