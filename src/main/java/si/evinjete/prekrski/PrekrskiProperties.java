package si.evinjete.prekrski;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("slike-config")
public class PrekrskiProperties {

    @ConfigValue(watch = true)
    // Max age of photos
    private Integer slikeAgeProperty;

    public Integer getSlikeAgeProperty() {
        return slikeAgeProperty;
    }

    public void setSlikeAgeProperty(Integer slikeAgeProperty) {
        this.slikeAgeProperty = slikeAgeProperty;
    }
}
