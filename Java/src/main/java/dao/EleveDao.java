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

    //On pourra ajouter ici d'autres méthodes pour gérer les eleves (find, update, delete...)
    public static Eleve trouverParMail(String mail) {
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
    
    public static Eleve trouverParId(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Eleve> query = em.createQuery("SELECT c FROM Eleve c WHERE c.id = :id", Eleve.class);
        query.setParameter("id", id);
        List<Eleve> eleves = query.getResultList();
        Eleve result = null;
        if (!eleves.isEmpty()) {
            // on prend le tuple de la première ligne et cela marche car le mail est unique
            result = eleves.get(0);
        }
        return result;
    }
    
    public List<Eleve> getAllEleves() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Eleve> query = em.createQuery("SELECT c FROM Eleve c", Eleve.class);
        return query.getResultList();
    }
}

