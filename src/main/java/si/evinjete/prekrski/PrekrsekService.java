package si.evinjete.prekrski;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class PrekrsekService {
    @PersistenceContext(unitName = "kumuluzee-samples-jpa")
    private EntityManager em;

    @PersistenceContext(unitName = "kumuluzee-samples-jpa-failed")
    private EntityManager emFailed;

    public Prekrsek getPrekrsek(String prekrsekId) {
        return em.find(Prekrsek.class, prekrsekId);
    }

    public List<Prekrsek> getPrekrski() {
        List<Prekrsek> prekrski = em
                .createNamedQuery("Prekrsek.findPrekrski", Prekrsek.class)
                .getResultList();

        return prekrski;
    }

    @Transactional
    public void savePrekrsek(Prekrsek prekrski) {
        if (prekrski != null) {
            em.persist(prekrski);
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deletePrekrsek(String prekrsekId) {
        Prekrsek prekrsek = em.find(Prekrsek.class, prekrsekId);
        if (prekrsek != null) {
            em.remove(prekrsek);
        }
    }

    public List<Prekrsek> getPrekrskiFailed() {
        List<Prekrsek> prekrski = emFailed
                .createNamedQuery("Prekrsek.findPrekrski", Prekrsek.class)
                .getResultList();

        return prekrski;
    }
}
