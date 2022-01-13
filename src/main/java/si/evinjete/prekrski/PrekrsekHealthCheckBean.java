package si.evinjete.prekrski;

import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Readiness
@ApplicationScoped
public class PrekrsekHealthCheckBean implements HealthCheck {

    private static final String url = "https://github.com/kumuluz/kumuluzee";

    private static final Logger LOG = Logger.getLogger(PrekrsekHealthCheckBean.class.getSimpleName());

    @Inject
    private PrekrsekService prekrsekBean;
    @Inject
    private SlikaService slikaBean;

    @Override
    public HealthCheckResponse call() {
        try {

            List<Prekrsek> prekrski = prekrsekBean.getPrekrski();
            List<Slika> slike = slikaBean.getSlike();
            System.out.println("prekrski size:"+prekrski.size() +", slike size:"+ slike.size());

            if (prekrski.size() > 0 && slike.size() > 0) {
                return HealthCheckResponse.named(PrekrsekHealthCheckBean.class.getSimpleName()).up().build();
            }
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
        }
        return HealthCheckResponse.named(PrekrsekHealthCheckBean.class.getSimpleName()).down().build();
    }
}
