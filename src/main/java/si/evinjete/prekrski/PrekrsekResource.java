package si.evinjete.prekrski;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("prekrski")
public class PrekrsekResource {

    private WebTarget wb;

    @Inject
    @DiscoverService(value = "anpr", version = "1.0.x", environment = "dev")
    private WebTarget anprTarget;

    @Inject
    @DiscoverService(value = "uporabniki", version = "1.0.x", environment = "dev")
    private WebTarget uporabnikiTarget;

    @Inject
    private PrekrsekService prekrsekBean;
    @Inject
    private SlikaService slikaBean;
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
    @Path("{prekrsekId}")
    public Response getPrekrsek(@PathParam("prekrsekId") String prekrsekId) {
        Prekrsek prekrsek = prekrsekBean.getPrekrsek(prekrsekId);
        return prekrsek != null
                ? Response.ok(prekrsek).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/tablica/{tablica}")
    public Response getPrekrskiFromTablica(@PathParam("tablica") String tablica) {
        List<Prekrsek> prekrski = prekrsekBean.getPrekrskiFromTablica(tablica);
        final GenericEntity<List<Prekrsek>> entity
                = new GenericEntity<List<Prekrsek>>(prekrski) {};

        return prekrski != null
                ? Response.ok(entity).build()
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
    public Response detectPrekrsek(@QueryParam("kameraid") String kameraid, @QueryParam("password") String password, InputStream uploadedInputStream) throws IOException {
        Client client = ClientBuilder.newClient();
        wb = client.target("http://kamere-service.default.svc.cluster.local:8080/v1/kamere/"+kameraid);
        Response responseKamere = wb.request().get();

        if(responseKamere.getStatus() == Response.Status.NOT_FOUND.getStatusCode()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Kamera k = responseKamere.readEntity(Kamera.class);
        if(!k.password.equals(password)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
        slika.setLocation(k.location);

//        WebTarget service = anprTarget.path("v1/upload/slika");
//        System.out.println(service);
//        String response = service.request(MediaType.APPLICATION_JSON).post(Entity.json(slika), String.class);

        wb = client.target("http://anpr-service.default.svc.cluster.local:8080/v1/upload/slika");
        String response = wb.request(MediaType.APPLICATION_JSON).post(Entity.json(slika), String.class);

        if(response.equals("")){
            System.out.println("INFO -- Recieved image, did not detect any number plate. ");
            return Response.status(200).build();
        }

        slika.setNumberPlate(response);
        slikaBean.addNewSlika(slika);


        //v vinjete servisu preveri ali obstaja veljavna vinjeta za zaznano registrsko tablico in ??e ne obstaja potem shrani prekr??ek
        wb = client.target("http://vinjete-service.default.svc.cluster.local:8080/v1/vinjete/tablica/"+response);
        Response responseVinjeta = wb.request().get();
        if(responseVinjeta.getStatus() != 200){ // za podano registrsko tablico vinjeta ne obstaja
            System.out.println("INFO -- New prekersek detected for tablica: " + response);
            Prekrsek prekrsek = new Prekrsek();
            prekrsek.setNumberPlate(response);
            prekrsek.setLocation(k.location);
            prekrsek.setTimestamp(new Date());
            prekrsek.setImageId(slika.getId());
            prekrsekBean.addNewPrekrsek(prekrsek);
        }
        else{
            System.out.println("INFO -- tablica " + response + " found in database. ");
        }

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
        List<Slika> rows = slikaBean.deleteAllSlika(properties.getSlikeAgeProperty());
        return Response.ok(rows).build();
    }

    @GET
    @Path("/slike/{slikaId}")
    public Response getSlika(@PathParam("slikaId") String slikaId) {
        Slika slika = slikaBean.getSlika(Integer.parseInt(slikaId));
        return slika != null
                ? Response.ok(slika).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}

