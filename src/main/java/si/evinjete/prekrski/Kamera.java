package si.evinjete.prekrski;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Kamera implements Serializable {
    @Column(nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @Column(name = "password", nullable = false, updatable = false)
    public String password;
    @Column(name = "location", nullable = false, updatable = false)
    public String location;
    @Column(name = "timestamp", nullable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Date timestamp;
    @Column(name = "direction", nullable = false, updatable = false)
    public String direction;
}