package si.evinjete.prekrski;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "prekrsek")
@NamedQueries({
        @NamedQuery(
                name = "Prekrsek.findPrekrski",
                query = "SELECT p " +
                        "FROM Prekrsek p"
        )
})
@NamedNativeQueries(
        {
                @NamedNativeQuery(
                        name = "Prekrsek.findPrekrskiFromTablica",
                        query = "SELECT * FROM prekrsek WHERE number_plate = :tablica", resultClass = Prekrsek.class
                )
        }
)
public class Prekrsek implements Serializable {

    @Column(nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "number_plate", nullable = false, updatable = false)
    private String numberPlate;
    @Column(name = "location", nullable = false, updatable = false)
    private String location;
    @Column(name = "timestamp", nullable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date timestamp;
    @Column(name = "imageId", nullable = false, updatable = false)
    private Integer imageId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

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

    public Integer getImageId() { return imageId; }

    public void setImageId(Integer imageId) { this.imageId = imageId; }
}
