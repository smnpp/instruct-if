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

    public static Etablissement getParId(String uai) {
        return JpaUtil.obtenirContextePersistance().find(Etablissement.class, uai);
    }

    public void delete(Etablissement etablissement) {
        JpaUtil.obtenirContextePersistance().remove(etablissement);
    }

    public Etablissement update(Etablissement etablissement) {
        return JpaUtil.obtenirContextePersistance().merge(etablissement);
    }

    public List<Etablissement> getAllEtablissements() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Etablissement> query = em.createQuery("SELECT c FROM Etablissement c", Etablissement.class);
        return query.getResultList();
    }
}
