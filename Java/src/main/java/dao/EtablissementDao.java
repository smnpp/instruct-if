package dao;

/**
 *
 * @author selghissas
 */
import metier.modele.Etablissement;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EtablissementDao {

    public void create(Etablissement etablissement) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(etablissement);
    }
    
    public static Etablissement trouverParId(String uai) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Etablissement> query = em.createQuery("SELECT c FROM Etablissement c WHERE c.uai = :uai", Etablissement.class);
        query.setParameter("uai", uai);
        List<Etablissement> etablissements = query.getResultList();
        Etablissement result = null;
        if (!etablissements.isEmpty()) {
            // on prend le tuple de la première ligne et cela marche car l'uai est unique
            result = etablissements.get(0);
        }
        return result;
    }
    
    public List<Etablissement> getAllEtablissements() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Etablissement> query = em.createQuery("SELECT c FROM Etablissement c", Etablissement.class);
        return query.getResultList();
    }
}