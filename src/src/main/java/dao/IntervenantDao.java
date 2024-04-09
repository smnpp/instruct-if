/*
 La classe IntervenantDao va encapsuler les opérations CRUD pour les entités Intervenant
 */
package dao;

/**
 *
 * @author sperret
 */
import metier.modele.Intervenant;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class IntervenantDao {

    public void create(Intervenant intervenant) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(intervenant);
    }

    public static Intervenant getParTelephone(String telephone) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Intervenant> query = em.createQuery("SELECT c FROM Intervenant c WHERE c.telephone = :telephone", Intervenant.class);
        query.setParameter("telephone", telephone);
        List<Intervenant> intervenants = query.getResultList();
        Intervenant result = null;
        if (!intervenants.isEmpty()) {
            // on prend le tuple de la première ligne et cela marche car le tel est unique
            result = intervenants.get(0);
        }
        return result;
    }

    public static Intervenant getParId(Long id) {
        return JpaUtil.obtenirContextePersistance().find(Intervenant.class, id);
    }

    public void delete(Intervenant intervenant) {
        JpaUtil.obtenirContextePersistance().remove(intervenant);
    }

    public Intervenant update(Intervenant intervenant) {
        return JpaUtil.obtenirContextePersistance().merge(intervenant);
    }

    public Long getNbIntervActif() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(DISTINCT i) FROM Intervenant i WHERE i.nbIntervention > 0", Long.class);
        return query.getSingleResult();

    }
    
    public Long getNbIntervDisponibles() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(DISTINCT i) FROM Intervenant i WHERE i.disponibilite > 0", Long.class);
        return query.getSingleResult();

    }
    

    public List<Intervenant> getAllIntervenants() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Intervenant> query = em.createQuery("SELECT c FROM Intervenant c", Intervenant.class);
        return query.getResultList();
    }


}
