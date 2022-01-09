package si.evinjete.prekrski;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class PrekrsekService {
    @PersistenceContext(unitName = "evinjete-prekrski")
    private EntityManager em;

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

    @Transactional
    public Prekrsek addNewPrekrsek(Prekrsek prekrsek) {
        if (prekrsek != null) {
            em.persist(prekrsek);
        }
        return prekrsek;
    }
}
