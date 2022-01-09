package si.evinjete.prekrski;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;

@Named
@RequestScoped
public class SlikaBean implements Serializable {

    @Inject
    SlikaService slikaBean;

    public SlikaBean(){}

    public SlikaBean(Integer id, String numberPlate, String location, Date timestamp, byte[] content){
        this.id = id;
        this.numberPlate = numberPlate;
        this.location = location;
        this.timestamp = timestamp;
        this.content = content;
    }

    private Integer id;
    private String numberPlate;
    private String location;
    private Date timestamp;
    private byte[] content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getLocation() {
        return location;
    }

    public void  setLocation(String location) {
        this.location = location;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentString(Integer id) {
        Slika slika = slikaBean.getSlika(Integer.toString(id));
        String imageString= new String(Base64.getEncoder().encodeToString(slika.getContent()));
        return imageString;
    }
}
