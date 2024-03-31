/*
 La classe SoutienDao va encapsuler les opérations CRUD pour les entités Soutien
 */
package dao;

/**
 *
 * @author selghissas
 */
import metier.modele.Soutien;
import metier.modele.Eleve;
import metier.modele.Intervenant;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class SoutienDao {

    public void create(Soutien soutien) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(soutien);
    }

    //On pourra ajouter ici d'autres méthodes pour gérer les soutiens (find, update, delete...)
    public static Soutien trouverParId(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Soutien> query = em.createQuery("SELECT c FROM Soutien c WHERE c.id = :id", Soutien.class);
        query.setParameter("id", id);
        List<Soutien> soutiens = query.getResultList();
        Soutien result = null;
        if (!soutiens.isEmpty()) {
            // on prend le tuple de la première ligne et cela marche car le mail est unique
            result = soutiens.get(0);
        }
        return result;
    }
    
    public List<Soutien> getAllSoutiens() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Soutien> query = em.createQuery("SELECT c FROM Soutien c", Soutien.class);
        return query.getResultList();
    }
    
    public static List<Soutien> trouverHistoriqueParEleve(Eleve eleve) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Soutien> query = em.createQuery("SELECT s FROM Soutien s WHERE s.eleve = :eleve", Soutien.class);
        query.setParameter("eleve", eleve);
        return query.getResultList();
    }
    
    public static List<Soutien> trouverHistoriqueParIntervenant(Intervenant intervenant) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Soutien> query = em.createQuery("SELECT s FROM Soutien s WHERE s.intervenant = :intervenant", Soutien.class);
        query.setParameter("intervenant", intervenant);
        return query.getResultList();
    }
}

