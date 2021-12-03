package si.evinjete.prekrski;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@RequestScoped
public class SlikaService {
    //@PersistenceContext(unitName = "kumuluzee-samples-jpa")
    @PersistenceContext(unitName = "evinjete-prekrski")
    private EntityManager em;

    public Slika getSlika(String slikaId) {
        return em.find(Slika.class, slikaId);
    }

    public List<Slika> getPrekrski() {
        List<Slika> slike = em
                .createNamedQuery("Slika.findSlike", Slika.class)
                .getResultList();

        return slike;
    }

    @Transactional
    public void saveSlika(Slika slike) {
        if (slike != null) {
            em.persist(slike);
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteSlika(String slikaId) {
        Slika slika = em.find(Slika.class, slikaId);
        if (slika != null) {
            em.remove(slika);
        }
    }

    @Transactional
    public Slika addNewSlika(Slika slika) {
        if (slika != null) {
            em.persist(slika);
        }
        return slika;
    }
}
