/*
 La classe SoutienDao va encapsuler les opérations CRUD pour les entités Soutien
 */
package dao;

/**
 *
 * @author selghissas
 */
import java.util.HashMap;
import metier.modele.Soutien;
import metier.modele.Eleve;
import metier.modele.Intervenant;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

public class SoutienDao {

    public void create(Soutien soutien) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(soutien);
    }

    //On pourra ajouter ici d'autres méthodes pour gérer les soutiens (find, update, delete...)
    public static Soutien trouverParId(Long id) {
        return JpaUtil.obtenirContextePersistance().find(Soutien.class, id);
    }

    public void delete(Soutien soutien) {
        JpaUtil.obtenirContextePersistance().remove(soutien);
    }

    public Soutien update(Soutien soutien) {
        return JpaUtil.obtenirContextePersistance().merge(soutien);
    }

    public List<Soutien> getAllSoutiens() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Soutien> query = em.createQuery("SELECT c FROM Soutien c", Soutien.class);
        return query.getResultList();
    }

    public Long getCountInterventions() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(s) FROM Soutien s", Long.class);
        return query.getSingleResult();
    }

    public Double getDureeMoyenneInterventions() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.createQuery("SELECT AVG(s.duree) FROM Soutien s", Double.class).getSingleResult();
    }

    public Double getSatisfactionMoyenneEleve() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.createQuery("SELECT AVG(s.autoevaluationEleve) FROM Soutien s", Double.class).getSingleResult();
    }

    public Long getIntervenantMoisId() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        List<Long> resultats = em.createQuery(
                "SELECT s.intervenant.id "
                + "FROM Soutien s GROUP BY s.intervenant.id "
                + "ORDER BY AVG(s.autoevaluationEleve) DESC, COUNT(s) DESC",
                Long.class
        ).setMaxResults(1).getResultList();

        Long resultatId = null;
        if (!resultats.isEmpty()) {
            resultatId = (Long) resultats.get(0); // Récupère l'identifiant de l'intervenant
        }
        return resultatId;
    }

    public Double getIPSMoyenEtablissements() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Double> query = em.createQuery(
                "SELECT AVG(eleve.etablissement.ips) "
                + "FROM Soutien soutien "
                + "JOIN soutien.eleve eleve "
                + "JOIN eleve.etablissement etablissement",
                Double.class
        );

        return query.getSingleResult();
    }

    public Map<String, Long> getQuantiteSoutienParDepartement() {
        EntityManager em = JpaUtil.obtenirContextePersistance();

        // Requête JPQL pour compter la quantité de soutien par département
        TypedQuery<Object[]> query = em.createQuery(
                "SELECT e.departement, COUNT(s) "
                + "FROM Soutien s "
                + "JOIN s.eleve eleve "
                + "JOIN eleve.etablissement e "
                + "GROUP BY e.departement",
                Object[].class
        );

        // Récupération des résultats et création de la Map
        Map<String, Long> resultats = new HashMap<>();
        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            String departement = (String) row[0];
            Long nombreInterventions = (Long) row[1];
            resultats.put(departement, nombreInterventions);
        }

        return resultats;
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
