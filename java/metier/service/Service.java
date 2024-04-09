/*
 Le service métier va utiliser EleveDao pour persister les données et GeoNetApi pour obtenir les coordonnées GPS.
 */
package metier.service;

import com.google.maps.model.LatLng;
import util.GeoNetApi;
import util.Message;
import util.EducNetApi;
import dao.EleveDao;
import dao.EtablissementDao;
import dao.JpaUtil;
import dao.MatiereDao;
import dao.SoutienDao;
import dao.IntervenantDao;
import java.util.ArrayList;
import metier.modele.Eleve;
import metier.modele.Intervenant;
import metier.modele.Etudiant;
import metier.modele.Etablissement;
import metier.modele.Matiere;
import metier.modele.AutreIntervenant;
import metier.modele.Enseignant;
import metier.modele.Soutien;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author selghissas
 */
public class Service {

    public EleveDao eleveDao = new EleveDao();
    public EtablissementDao etablissementDao = new EtablissementDao();
    public SoutienDao soutienDao = new SoutienDao();
    public MatiereDao matiereDao = new MatiereDao();
    public IntervenantDao intervenantDao = new IntervenantDao();
    private List<Object[]> demandesEnAttente = new LinkedList<>();

    public Boolean inscrireEleve(Eleve eleve, String uai) { //Inscrire un élève
        boolean success = false;
        try {
            JpaUtil.creerContextePersistance();

            EducNetApi api = new EducNetApi();
            if (api.getInformationCollege(uai) != null || api.getInformationLycee(uai) != null) {

                List<String> resultEtablissement;
                Etablissement etablissementEleve = new Etablissement();
                Etablissement etablissementTrouve = EtablissementDao.getParId(uai);
                if (etablissementTrouve == null) {

                    if (eleve.getClasse() > 2) {
                        resultEtablissement = api.getInformationCollege(uai);
                    } else {
                        resultEtablissement = api.getInformationLycee(uai);
                    }

                    if (resultEtablissement != null) {
                        etablissementEleve.setUai(resultEtablissement.get(0));
                        etablissementEleve.setNomEtablissement(resultEtablissement.get(1));
                        etablissementEleve.setSecteur(resultEtablissement.get(2));
                        etablissementEleve.setCodeCommune(resultEtablissement.get(3));
                        etablissementEleve.setNomCommune(resultEtablissement.get(4));
                        etablissementEleve.setCodeDepartement(resultEtablissement.get(5));
                        etablissementEleve.setDepartement(resultEtablissement.get(6));
                        etablissementEleve.setAcademie(resultEtablissement.get(7));
                        etablissementEleve.setIps(Double.parseDouble(resultEtablissement.get(8)));

                        LatLng latLng = GeoNetApi.getLatLng(resultEtablissement.get(1) + ", " + resultEtablissement.get(4));
                        if (!(latLng == null)) {
                            etablissementEleve.setLatitude(latLng.lat);
                            etablissementEleve.setLongitude(latLng.lng);
                        }
                    }
                } else {
                    etablissementEleve = etablissementTrouve;
                }

                JpaUtil.ouvrirTransaction();
                if (etablissementTrouve == null) {
                    etablissementDao.create(etablissementEleve);
                }
                eleve.setEtablissement(etablissementEleve);
                eleveDao.create(eleve); // Persister l'entité eleve
                JpaUtil.validerTransaction();

                success = true;

                // Envoi du mail de confirmation
                Message.envoyerMail("contact@instruct.if", eleve.getMail(), "Bienvenue sur le réseau INSTRUCT'IF", "Bonjour "
                        + eleve.getPrenom() + ", Nous te confirmons ton inscription sur le réseau INSTRUCT'IF."
                        + "Si tu as besoin d'un soutien pour tes"
                        + " leçons, ou tes devoirs, rends-toi sur notre site pour une mise en relation avec un intervenant.");
            } else {
                JpaUtil.annulerTransaction();

                // Envoi du mail d'infirmité
                Message.envoyerMail("contact@instruct.if", eleve.getMail(), "Échec de l'inscription sur le réseau INSTRUCT'IF",
                        "Bonjour "
                        + eleve.getPrenom() + " ton inscription sur le réseau INSTRUCT'IF a malencontreusement échoué... "
                        + "Merci de recommencer ultérieurement.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JpaUtil.annulerTransaction();

            // Envoi du mail d'infirmité
            Message.envoyerMail("contact@instruct.if", eleve.getMail(), "Échec de l'inscription sur le réseau INSTRUCT'IF",
                    "Bonjour "
                    + eleve.getPrenom() + " ton inscription sur le réseau INSTRUCT'IF a malencontreusement échoué... Merci de recommencer ultérieurement.");
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return success;
    }

    public Eleve authentifierEleveMail(String mail, String motDePasse) { //Authentifier un eleve
        Eleve retour = null;
        JpaUtil.creerContextePersistance();
        Eleve eleve = eleveDao.getParMail(mail);
        JpaUtil.fermerContextePersistance();
        if (eleve != null && eleve.getMotDePasse().equals(motDePasse)) {
            retour = eleve;
        }
        return retour;
    }

    public Boolean inscrireIntervenant(Intervenant intervenant) {
        Boolean success = false;
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            intervenantDao.create(intervenant);
            JpaUtil.validerTransaction();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            JpaUtil.annulerTransaction();

        } finally {
            JpaUtil.fermerContextePersistance();
            return success;
        }
    }

    public Intervenant authentifierIntervenantTelephone(String telephone, String motDePasse) { //Authentifier un eleve
        Intervenant retour = null;
        JpaUtil.creerContextePersistance();
        Intervenant intervenant = intervenantDao.getParTelephone(telephone);
        JpaUtil.fermerContextePersistance();
        if (intervenant != null && intervenant.getMotDePasse().equals(motDePasse)) {
            retour = intervenant;
        }
        return retour;
    }

    public Boolean creerMatiere(Matiere matiere) {
        Boolean success = false;
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            matiereDao.create(matiere);
            JpaUtil.validerTransaction();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            JpaUtil.annulerTransaction();

        } finally {
            JpaUtil.fermerContextePersistance();
            return success;
        }
    }

    public List<Matiere> consulterListeMatieres() {
        //Obtenir la liste des matières (utiliser ce service pour le menu déroulant 
        //de la page de demande de soutien des élèves)
        JpaUtil.creerContextePersistance();
        List<Matiere> matieres = matiereDao.getAllMatieres();
        JpaUtil.fermerContextePersistance();
        return matieres;
    }

    public Intervenant trouverIntervenantSoutien(Eleve eleve) {
        Intervenant intervenantTrouve = null;
        List<Intervenant> intervenants = intervenantDao.getAllIntervenants();
        Collections.sort(intervenants, Comparator.comparingInt(Intervenant::getNbIntervention));
        for (Intervenant intervenant : intervenants) {
            if (intervenant.getDisponibilite() == true && (intervenant.getNiveauMin() >= eleve.getClasse() && intervenant.getNiveauMax() <= eleve.getClasse())) {
                intervenantTrouve = intervenant;
                break;
            }
        }
        return intervenantTrouve;
    }

    public Soutien creerSoutien(Eleve eleve, Matiere matiere, String descriptif) throws Exception {
        Soutien soutien = new Soutien();
        try {
            JpaUtil.creerContextePersistance();

            soutien.setEleve(eleve);
            soutien.setDescriptif(descriptif);
            soutien.setMatiere(matiere);
            Intervenant intervenant = trouverIntervenantSoutien(eleve);

            if (intervenant == null) {
                // Si aucun intervenant disponible n'est trouvé, on envoie un email à l'élève.
                Message.envoyerMail("contact@instruct.if", eleve.getMail(), "Aucun intervenant disponible",
                        "Bonjour " + eleve.getPrenom() + ",\n\nNous sommes désolés, mais aucun intervenant n'est disponible pour le moment pour du soutien en "
                        + matiere.getNom() + ". Veuillez réessayer ultérieurement.\n\nCordialement,\nL'équipe INSTRUCT'IF");
                throw new Exception("Aucun intervenant disponible pour le moment.");
            }

            soutien.setIntervenant(intervenant);
            intervenant.setDisponibilite(false);
            intervenant.setNbIntervention(intervenant.getNbIntervention() + 1);

            JpaUtil.ouvrirTransaction();
            intervenantDao.update(intervenant);
            soutienDao.create(soutien);
            JpaUtil.validerTransaction();

            Message.envoyerNotification(intervenant.getTelephone(),
                    "Bonjour " + intervenant.getPrenom() + ", vous avez une nouvelle demande de soutien en "
                    + matiere.getNom() + " de la part de " + eleve.getPrenom() + ", classe de " + eleve.getClasse() + "ème.");

        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            throw e; // Relancer l'exception pour indiquer un échec dans la création du soutien.
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return soutien;
    }

    public void demanderSoutien(Eleve eleve, Matiere matiere, String descriptif) {
        demandesEnAttente.add(new Object[]{eleve, matiere, descriptif});
    }

    public List<Soutien> creerSoutiens() throws Exception {
        List<Soutien> soutiensCrees = new ArrayList<>();
        if (demandesEnAttente.isEmpty()) {
            throw new Exception("Aucune demande en attente.");
        }

        JpaUtil.creerContextePersistance();

        try {
            JpaUtil.ouvrirTransaction();

            // Trier la liste des demandes en attente par classe de l'élève (ascendant) 
            //puis par date de naissance s'ils ont la même classe
            demandesEnAttente.sort((demande1, demande2) -> {
    int classeComparison = ((Eleve) demande1[0]).getClasse().compareTo(((Eleve) demande2[0]).getClasse());
    if (classeComparison != 0) {
        return classeComparison;
    } else {
        // Pour trier par date de naissance de manière descendante, le plus jeune d'abord
        // Nous inversons l'ordre de comparaison pour les dates de naissance
        return ((Eleve) demande2[0]).getDateNaissance().compareTo(((Eleve) demande1[0]).getDateNaissance());
    }
});

            Long nombreIntervenantsDisponibles = intervenantDao.getNbIntervDisponibles();

            for (Object[] demande : demandesEnAttente) {
                Eleve eleve = (Eleve) demande[0];
                Matiere matiere = (Matiere) demande[1];
                String descriptif = (String) demande[2];

                if (nombreIntervenantsDisponibles > 0) {
                    Intervenant intervenant = trouverIntervenantSoutien(eleve);

                    if (intervenant != null) {
                        Soutien soutien = new Soutien();
                        soutien.setEleve(eleve);
                        soutien.setDescriptif(descriptif);
                        soutien.setMatiere(matiere);
                        soutien.setIntervenant(intervenant);

                        intervenant.setDisponibilite(false);
                        intervenant.setNbIntervention(intervenant.getNbIntervention() + 1);

                        soutienDao.create(soutien);
                        soutiensCrees.add(soutien);

                        nombreIntervenantsDisponibles--;

                        Message.envoyerNotification(intervenant.getTelephone(),
                                "Bonjour " + intervenant.getPrenom() + ", vous avez une nouvelle demande de soutien en "
                                + matiere.getNom() + " de la part de " + eleve.getPrenom() + ", classe de " + eleve.getClasse() + "ème.");
                    } else {
                        // Envoie un mail à l'élève pour informer de l'indisponibilité des intervenants
                        Message.envoyerMail("contact@instruct.if", eleve.getMail(), "Demande de soutien non aboutie",
                                "Bonjour " + eleve.getPrenom() + ",\n\nMalheureusement, nous n'avons pas assez d'intervenants disponibles pour répondre à toutes les demandes actuellement. Veuillez réessayer ultérieurement.\n\nCordialement,\nL'équipe INSTRUCT'IF");
                    }
                } else {
                    // Envoie un mail à l'élève pour informer de l'indisponibilité des intervenants
                    Message.envoyerMail("contact@instruct.if", eleve.getMail(), "Demande de soutien non aboutie",
                            "Bonjour " + eleve.getPrenom() + ",\n\nMalheureusement, nous n'avons pas assez d'intervenants disponibles pour répondre à toutes les demandes actuellement. Veuillez réessayer ultérieurement.\n\nCordialement,\nL'équipe INSTRUCT'IF");
                }
            }

            JpaUtil.validerTransaction();
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            throw e;
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        demandesEnAttente.clear(); // La file est maintenant vide après traitement

        return soutiensCrees; // Retourne la liste des soutiens créés
    }

    public Soutien obtenirSoutienEnAttenteParEleveId(Long eleveId) throws Exception {
        JpaUtil.creerContextePersistance();
        try {
            Soutien soutien = soutienDao.getSoutienEnAttenteParEleveId(eleveId);
            return soutien;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
    }

    public Soutien obtenirSoutienEnAttenteParIntervenantId(Long intervenantId) throws Exception {
        JpaUtil.creerContextePersistance();
        try {
            Soutien soutien = soutienDao.getSoutienEnAttenteParIntervenantId(intervenantId);
            return soutien;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
    }

    public List<Soutien> trouverHistoriqueEleve(Eleve eleve) {
        JpaUtil.creerContextePersistance();
        List<Soutien> historique = soutienDao.getHistoriqueParEleve(eleve);
        return historique;
    }

    public List<Soutien> trouverHistoriqueIntervenant(Long intervenantId) {
        JpaUtil.creerContextePersistance();
        Intervenant intervenant = intervenantDao.getParId(intervenantId) ;
        List<Soutien> historique = soutienDao.getHistoriqueParIntervenant(intervenant);
        return historique ;
    }

    public Boolean lancerVisio(Soutien soutien) {
        Boolean success = false;
        if (soutien != null) {
            try {
                JpaUtil.creerContextePersistance();
                if (soutien.getIntervenant() != null) {
                    soutien.setEtat(Soutien.EtatSoutien.EN_VISIO);
                    soutien.setDate(new Date());

                    JpaUtil.ouvrirTransaction();
                    soutienDao.update(soutien);
                    JpaUtil.validerTransaction();

                    success = true;
                } else {
                    JpaUtil.annulerTransaction();
                }
            } catch (Exception e) {
                e.printStackTrace();
                JpaUtil.annulerTransaction();

            } finally {
                JpaUtil.fermerContextePersistance();
            }
        }
        return success;
    }

    public Boolean terminerVisio(Soutien soutien) {
        Boolean success = false;
        if (soutien != null) {
            try {
                JpaUtil.creerContextePersistance();
                soutien.setEtat(Soutien.EtatSoutien.TERMINE);
                Date dateDeFin = new Date();
                Long dureeDeVisio = dateDeFin.getTime() - soutien.getDate().getTime();
                soutien.setDuree(TimeUnit.MILLISECONDS.toMinutes(dureeDeVisio));
                soutien.getIntervenant().setDisponibilite(true);

                JpaUtil.ouvrirTransaction();
                soutienDao.update(soutien);
                intervenantDao.update(soutien.getIntervenant());
                JpaUtil.validerTransaction();

                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                JpaUtil.annulerTransaction();

            } finally {
                JpaUtil.fermerContextePersistance();
            }
        }
        return success;
    }

    public Boolean faireAutoEvaluationEleve(Soutien soutien, Integer note) {
        Boolean success = false;
        if (soutien != null) {
            try {
                JpaUtil.creerContextePersistance();
                double noteCastee = note;
                soutien.setAutoevaluationEleve(noteCastee);

                JpaUtil.ouvrirTransaction();
                soutienDao.update(soutien);
                JpaUtil.validerTransaction();

                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                JpaUtil.annulerTransaction();

            } finally {
                JpaUtil.fermerContextePersistance();
            }
        }
        return success;
    }

    public Boolean redigerBilan(Soutien soutien, String bilan) {
        Boolean success = false;
        if (soutien != null) {
            try {
                JpaUtil.creerContextePersistance();
                soutien.setBilanIntervenant(bilan);

                JpaUtil.ouvrirTransaction();
                soutienDao.update(soutien);
                JpaUtil.validerTransaction();

                Message.envoyerMail("contact@instruct.if", soutien.getEleve().getMail(), "INSTRUCT'IF : ton bilan de soutien", "Bonjour "
                        + soutien.getEleve().getPrenom() + ", voici le bilan de ton soutien du " + soutien.getDate() + " en " + soutien.getMatiere().getNom() + " redigé par " + soutien.getIntervenant().getPrenom() + " :\n" + bilan + "\n A bientot sur INSTRUCT'IF !");

                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                JpaUtil.annulerTransaction();

            } finally {
                JpaUtil.fermerContextePersistance();
            }
        }
        return success;
    }

    public Long statNbTotalIntervention() {
        JpaUtil.creerContextePersistance();
        Long result = soutienDao.getCountInterventions();
        JpaUtil.fermerContextePersistance();
        return result;
    }

    public Double statDureeMoyenneIntervention() {
        JpaUtil.creerContextePersistance();
        Double result = soutienDao.getDureeMoyenneInterventions();
        JpaUtil.fermerContextePersistance();
        return result;
    }

    public Double statSatisfactionEleve() {
        JpaUtil.creerContextePersistance();
        Double result = soutienDao.getSatisfactionMoyenneEleve();
        JpaUtil.fermerContextePersistance();
        return result;
    }

    public String statIntervenantMois() {
        JpaUtil.creerContextePersistance();
        Long intervenantMoisId = soutienDao.getIntervenantMoisId();
        String result = "Pas d'intervenant ce mois";
        if (intervenantMoisId != null) {
            Intervenant intervenantMois = intervenantDao.getParId(intervenantMoisId);
            result = intervenantMois.getPrenom() + " " + intervenantMois.getNom();
        }

        JpaUtil.fermerContextePersistance();
        return result;
    }

    public Long statIntervenantActif() {
        JpaUtil.creerContextePersistance();
        Long result = intervenantDao.getNbIntervActif();
        JpaUtil.fermerContextePersistance();
        return result;
    }

    public Long statNbEleveInscrit() {
        JpaUtil.creerContextePersistance();
        Long result = eleveDao.getTotalEleveInscrit();
        JpaUtil.fermerContextePersistance();
        return result;
    }

    public Double statIpsMoyenSoutien() {
        JpaUtil.creerContextePersistance();
        Double result = soutienDao.getIPSMoyenEtablissements();
        JpaUtil.fermerContextePersistance();
        return result;
    }

    public List<List<Object>> statQuantiteSoutienParCoordonnees() {
        JpaUtil.creerContextePersistance();
        List<List<Object>> result = soutienDao.getQuantiteSoutienParCoordonnees();
        JpaUtil.fermerContextePersistance();
        return result;
    }

    public void initialisation() {

        Etudiant intervenant1 = new Etudiant("Martin", "Camille", "0655447788", "123456", 6, 5, "Sorbonne", "Langues orientales");
        inscrireIntervenant(intervenant1);

        AutreIntervenant intervenant2 = new AutreIntervenant("Zola", "Anna", "0633221144", "123456", 4, 3, "Retraite");
        inscrireIntervenant(intervenant2);

        Enseignant intervenant3 = new Enseignant("Hugo", "Emile", "0788559944", "123456", 2, 1, "collège");
        inscrireIntervenant(intervenant3);

        AutreIntervenant intervenant4 = new AutreIntervenant("Yourcenar", "Simone", "0722447744", "123456", 0, 0, "médecin");
        inscrireIntervenant(intervenant4);

        //il n'est pas possible pour un élève de creer une nouvelle matiere (menu déroulant)
        Matiere allemand = new Matiere("Allemand");
        creerMatiere(allemand);
        Matiere anglais = new Matiere("Anglais");
        creerMatiere(anglais);
        Matiere espagnol = new Matiere("Espagnol");
        creerMatiere(espagnol);
        Matiere francais = new Matiere("Français");
        creerMatiere(francais);
        Matiere histoireGeo = new Matiere("Histoire-Géographie");
        creerMatiere(histoireGeo);
        Matiere mathematiques = new Matiere("Mathématiques");
        creerMatiere(mathematiques);
        Matiere philosophie = new Matiere("Philosophie");
        creerMatiere(philosophie);
        Matiere physiqueChimie = new Matiere("Physique-Chimie");
        creerMatiere(physiqueChimie);
        Matiere svt = new Matiere("SVT");
        creerMatiere(svt);

    }
}
