/*
 La classe EleveDao va encapsuler les opérations CRUD pour les entités Eleve
 */
package dao;

/**
 *
 * @author selghissas
 */
import metier.modele.Eleve;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EleveDao {

    public void create(Eleve eleve) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(eleve);
    }

    public static Eleve getParMail(String mail) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Eleve> query = em.createQuery("SELECT c FROM Eleve c WHERE c.mail = :mail", Eleve.class);
        query.setParameter("mail", mail);
        List<Eleve> eleves = query.getResultList();
        Eleve result = null;
        if (!eleves.isEmpty()) {
            // on prend le tuple de la première ligne et cela marche car le mail est unique
            result = eleves.get(0);
        }
        return result;
    }

    public Long getTotalEleveInscrit() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT (DISTINCT e) FROM Eleve e", Long.class);
        return query.getSingleResult();
    }

    public static Eleve getParId(Long id) {
        return JpaUtil.obtenirContextePersistance().find(Eleve.class, id);
    }

    public void delete(Eleve eleve) {
        JpaUtil.obtenirContextePersistance().remove(eleve);
    }

    public Eleve update(Eleve eleve) {
        return JpaUtil.obtenirContextePersistance().merge(eleve);
    }

    public List<Eleve> getAllEleves() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Eleve> query = em.createQuery("SELECT c FROM Eleve c", Eleve.class);
        return query.getResultList();
    }
}
