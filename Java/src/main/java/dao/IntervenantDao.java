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

    //On pourra ajouter ici d'autres méthodes pour gérer les intervenants (find, update, delete...)
    public static Intervenant trouverParTelephone(String telephone) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Intervenant> query = em.createQuery("SELECT c FROM Intervenant c WHERE c.telephone = :telephone", Intervenant.class);
        query.setParameter("telephone", telephone);
        List<Intervenant> intervenants = query.getResultList();
        Intervenant result = null;
        if (!intervenants.isEmpty()) {
            // on prend le tuple de la première ligne et cela marche car le mail est unique
            result = intervenants.get(0);
        }
        return result;
    }
    
    public static Intervenant trouverParId(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Intervenant> query = em.createQuery("SELECT c FROM Intervenant c WHERE c.id = :id", Intervenant.class);
        query.setParameter("id", id);
        List<Intervenant> intervenants = query.getResultList();
        Intervenant result = null;
        if (!intervenants.isEmpty()) {
            // on prend le tuple de la première ligne et cela marche car le mail est unique
            result = intervenants.get(0);
        }
        return result;
    }
    
    public List<Intervenant> getAllIntervenants() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Intervenant> query = em.createQuery("SELECT c FROM Intervenant c", Intervenant.class);
        return query.getResultList();
    }
}