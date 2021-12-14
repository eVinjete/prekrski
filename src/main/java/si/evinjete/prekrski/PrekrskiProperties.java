package si.evinjete.prekrski;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-config")
public class PrekrskiProperties {

    @ConfigValue(watch = true)
    private String anprIp;

    public String getAnprIp() {
        return anprIp;
    }

    public void setAnprIp(String anprIp) {
        this.anprIp = anprIp;
    }
}
