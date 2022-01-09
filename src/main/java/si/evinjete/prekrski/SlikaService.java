package si.evinjete.prekrski;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequestScoped
public class SlikaService {
    @PersistenceContext(unitName = "evinjete-prekrski")
    private EntityManager em;

    @Inject
    PrekrsekService prekrsekBean;

    public Slika getSlika(Integer slikaId) {
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

    @Transactional
    public List<Slika> deleteAllSlika(Integer age) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR, -age);
        Date x = cal.getTime();

        List<Slika> listSlika = em.createQuery(
                     "SELECT s " +
                        "FROM Slika s " +
                        "WHERE s.timestamp BETWEEN :start AND :end")
                .setParameter("start", x, TemporalType.DATE)
                .setParameter("end", now, TemporalType.DATE)
                .getResultList();

        List<Prekrsek> listPrekrsek = prekrsekBean.getPrekrski();
        List<Integer> slikaIds = new ArrayList<Integer>();

        for (Prekrsek prekrsek: listPrekrsek) {
            slikaIds.add(prekrsek.getImageId());
        }

        for (Slika slika: listSlika) {
            if (!slikaIds.contains(slika.getId())) {
                em.remove(slika);
            }
        }

        return listSlika;
    }
}
