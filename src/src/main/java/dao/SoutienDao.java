/*
 La classe SoutienDao va encapsuler les opérations CRUD pour les entités Soutien
 */
package dao;

/**
 *
 * @author selghissas
 */
import java.util.ArrayList;
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

    public static Soutien trouverParId(Long id) {
        return JpaUtil.obtenirContextePersistance().find(Soutien.class, id);
    }

    public void delete(Soutien soutien) {
        JpaUtil.obtenirContextePersistance().remove(soutien);
    }

    public Soutien update(Soutien soutien) {
        return JpaUtil.obtenirContextePersistance().merge(soutien);
    }
    
    public Soutien getSoutienEnAttenteParEleveId(Long eleveId) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Soutien> query = em.createQuery(
                "SELECT s FROM Soutien s WHERE s.eleve.id = :eleveId AND s.etat = metier.modele.Soutien$EtatSoutien.EN_ATTENTE",
                Soutien.class);
        query.setParameter("eleveId", eleveId);
        List<Soutien> resultats = query.getResultList();
        Soutien resultat = null ;
        if (!resultats.isEmpty()) {
            // retourne le premier soutien trouvé
            resultat = resultats.get(0);
        }
        return resultat; 
    }
    
    public Soutien getSoutienEnAttenteParIntervenantId(Long intervenantId) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Soutien> query = em.createQuery(
                "SELECT s FROM Soutien s WHERE s.intervenant.id = :intervenantId AND s.etat = metier.modele.Soutien$EtatSoutien.EN_ATTENTE",
                Soutien.class);
        query.setParameter("intervenantId", intervenantId);
        List<Soutien> resultats = query.getResultList();
        Soutien resultat = null ;
        if (!resultats.isEmpty()) {
            // retourne le premier soutien trouvé
            resultat = resultats.get(0);
        }
        return resultat; 
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

    public List<List<Object>> getQuantiteSoutienParCoordonnees() {
        EntityManager em = JpaUtil.obtenirContextePersistance();

        // Requête JPQL pour compter la quantité de soutien par coordonnées géographiques
        TypedQuery<Object[]> query = em.createQuery(
                "SELECT e.longitude, e.latitude, COUNT(s) "
                + "FROM Soutien s "
                + "JOIN s.eleve eleve "
                + "JOIN eleve.etablissement e "
                + "GROUP BY e.longitude, e.latitude",
                Object[].class
        );

        // Récupération des résultats et création de la liste de listes
        List<Object[]> resultList = query.getResultList();
        List<List<Object>> resultats = new ArrayList<>();
        for (Object[] row : resultList) {
            List<Object> coordonneesEtNombre = new ArrayList<>();
            coordonneesEtNombre.add(Double.valueOf(row[0].toString())); 
            coordonneesEtNombre.add(Double.valueOf(row[1].toString()));
            coordonneesEtNombre.add(((Long) row[2]).intValue()); // Quantité de soutien, already Integer
            resultats.add(coordonneesEtNombre);
        }

        return resultats;
    }

    public static List<Soutien> getHistoriqueParEleve(Eleve eleve) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Soutien> query = em.createQuery("SELECT s FROM Soutien s WHERE s.eleve = :eleve", Soutien.class);
        query.setParameter("eleve", eleve);
        return query.getResultList();
    }

    public static List<Soutien> getHistoriqueParIntervenant(Intervenant intervenant) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Soutien> query = em.createQuery("SELECT s FROM Soutien s WHERE s.intervenant = :intervenant", Soutien.class);
        query.setParameter("intervenant", intervenant);
        return query.getResultList();
    }
}
