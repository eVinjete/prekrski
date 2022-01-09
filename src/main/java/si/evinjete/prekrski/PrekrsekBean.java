package si.evinjete.prekrski;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Named
@RequestScoped
public class PrekrsekBean implements Serializable {

    @Inject
    PrekrsekService prekrsekBean;

    public PrekrsekBean(){}

    public PrekrsekBean(Integer id, String numberPlate, String location, Date timestamp){
        this.id = id;
        this.numberPlate = numberPlate;
        this.location = location;
        this.timestamp = timestamp;
    }

    private Integer id;
    private String numberPlate;
    private String location;
    private Date timestamp;

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

    public List<Prekrsek> getPrekrski() {
        return prekrsekBean.getPrekrski();
    }
}
