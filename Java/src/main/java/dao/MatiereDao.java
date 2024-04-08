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

    public static Matiere getParNom(String nom) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Matiere> query = em.createQuery("SELECT c FROM Matiere c WHERE c.nom = :nom", Matiere.class);
        query.setParameter("nom", nom);
        List<Matiere> matieres = query.getResultList();
        Matiere result = null;
        if (!matieres.isEmpty()) {
            // on prend le tuple de la première ligne et cela marche car le mail est unique
            result = matieres.get(0);
        }
        return result;
    }

    public static Matiere getParId(Long id) {
        return JpaUtil.obtenirContextePersistance().find(Matiere.class, id);
    }

    public void delete(Matiere matiere) {
        JpaUtil.obtenirContextePersistance().remove(matiere);
    }

    public Matiere update(Matiere matiere) {
        return JpaUtil.obtenirContextePersistance().merge(matiere);
    }

    public List<Matiere> getAllMatieres() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Matiere> query = em.createQuery("SELECT c FROM Matiere c", Matiere.class);
        return query.getResultList();
    }
}
