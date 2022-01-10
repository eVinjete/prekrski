package si.evinjete.prekrski;


import java.io.Serializable;
import java.util.Date;

class Kamera implements Serializable {
    public Integer id;
    public String password;
    public String location;
    public Date timestamp;
    public String direction;
}