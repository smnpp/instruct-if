/*
 La classe MatiereDao va encapsuler les opérations CRUD pour les entités Matiere
 */
package dao;

/**
 *
 * @author selghissas
 */
import metier.modele.Matiere;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class MatiereDao {

    public void create(Matiere matiere) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(matiere);
    }

    //On pourra ajouter ici d'autres méthodes pour gérer les matieres (find, update, delete...)
    public static Matiere trouverParLibelle(String libelle) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Matiere> query = em.createQuery("SELECT c FROM Matiere c WHERE c.libelle = :libelle", Matiere.class);
        query.setParameter("libelle", libelle);
        List<Matiere> matieres = query.getResultList();
        Matiere result = null;
        if (!matieres.isEmpty()) {
            // on prend le tuple de la première ligne et cela marche car le mail est unique
            result = matieres.get(0);
        }
        return result;
    }
    
    public List<Matiere> getAllMatieres() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Matiere> query = em.createQuery("SELECT c FROM Matiere c", Matiere.class);
        return query.getResultList();
    }
}

