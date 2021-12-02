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

    public Date getTimestamp() {
        return timestamp;
    }

    public Integer getId() {
        return id;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public String getLocation() {
        return location;
    }

}
