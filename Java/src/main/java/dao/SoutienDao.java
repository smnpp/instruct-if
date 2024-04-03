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
        return JpaUtil.obtenirContextePersistance().find(Soutien.class, id);
    }
    public void delete(Soutien soutien){
        JpaUtil.obtenirContextePersistance().remove(soutien);
    }
    public Soutien update(Soutien soutien){
        return JpaUtil.obtenirContextePersistance().merge(soutien);
    }
    
    public List<Soutien> getAllSoutiens() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Soutien> query = em.createQuery("SELECT c FROM Soutien c", Soutien.class);
        return query.getResultList();
    }
    
    public Integer getCountInterventions() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Integer> query = em.createQuery("SELECT COUNT(s) FROM Soutien s", Integer.class);
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
    
    public Intervenant getIntervenantMois() {
        Intervenant resultat;
        EntityManager em = JpaUtil.obtenirContextePersistance();
        List<Object[]> resultats = em.createQuery("SELECT s.intervenant, AVG(s.autoevaluationEleve) as moyenneEval, COUNT(s) as nbInterventions FROM Soutien s "
                + "GROUP BY s.intervenant ORDER BY moyenneEval * nbInterventions DESC", Object[].class).setMaxResults(1).getResultList();
        if (resultats.isEmpty())
            resultat = null;
        else {
            resultat = (Intervenant)resultats.get(0)[0];
        }
        return resultat;
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

