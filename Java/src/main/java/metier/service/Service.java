/*
 Le service métier va utiliser EleveDao pour persister les données et GeoNetApi pour obtenir les coordonnées GPS.
 */
package metier.service;

import dao.EleveDao;
import dao.EtablissementDao;
import dao.JpaUtil;
import dao.MatiereDao;
import dao.SoutienDao;
import dao.IntervenantDao;
import java.util.Arrays;
import metier.modele.Eleve;
import metier.modele.Intervenant;
import metier.modele.Etudiant;
import metier.modele.Etablissement;
import metier.modele.Matiere;
import util.Message;
import util.EducNetApi;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import metier.modele.Soutien;

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

    public Boolean inscrireEleve(Eleve eleve, String uai) { //Inscrire un élève
        boolean success = false;
        try {
            JpaUtil.creerContextePersistance();

            EducNetApi api = new EducNetApi();
            if (api.getInformationCollege(uai) != null || api.getInformationLycee(uai) != null) {

                List<String> resultEtablissement;
                Etablissement etablissementEleve = new Etablissement();
                Etablissement etablissementTrouve = EtablissementDao.trouverParId(uai);
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
                        etablissementEleve.setIps(resultEtablissement.get(8));
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
        Eleve eleve = eleveDao.trouverParMail(mail);
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
        Intervenant intervenant = intervenantDao.trouverParTelephone(telephone);
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
            if (intervenant.getDisponibilite() == true && intervenant.getNiveau().contains(eleve.getClasse())) {
                intervenantTrouve = intervenant;
                break;
            }
        }
        return intervenantTrouve;
    }

    public Soutien creerSoutien(Eleve eleve, Matiere matiere, String descriptif) {
        Soutien soutien = new Soutien();
        try {
            JpaUtil.creerContextePersistance();

            soutien.setEleve(eleve);
            soutien.setDescriptif(descriptif);
            soutien.setMatiere(matiere);
            Intervenant intervenant = trouverIntervenantSoutien(eleve);
            soutien.setIntervenant(intervenant);
            intervenant.setDisponibilite(false);
            intervenant.setNbIntervention(intervenant.getNbIntervention() + 1);

            JpaUtil.ouvrirTransaction();
            intervenantDao.update(intervenant);
            soutienDao.create(soutien);
            JpaUtil.validerTransaction();

            Message.envoyerNotification(intervenant.getTelephone(),
                    "Bonjour "
                    + intervenant.getPrenom() + ", Merci de prendre en charge la demande de soutien en "
                    + matiere.getNom() + " demandée par " + eleve.getPrenom() + " en classe de " + eleve.getClasse());

        } catch (Exception e) {
            e.printStackTrace();
            JpaUtil.annulerTransaction();
            Message.envoyerMail("contact@instruct.if", eleve.getMail(), "Annulation de demande de soutien",
                    "Bonjour "
                    + eleve.getPrenom() + " ta demande sur le réseau INSTRUCT'IF a malencontreusement échoué... "
                    + "Merci de recommencer ultérieurement.");

        } finally {
            JpaUtil.fermerContextePersistance();
            return soutien;
        }

    }

    public List<Soutien> obtenirHistoriqueEleve(Eleve eleve) {
        JpaUtil.creerContextePersistance();
        List<Soutien> historique = soutienDao.trouverHistoriqueParEleve(eleve);
        JpaUtil.fermerContextePersistance();
        return historique;
    }

    public List<Soutien> obtenirHistoriqueIntervenant(Intervenant intervenant) {
        JpaUtil.creerContextePersistance();
        List<Soutien> historique = soutienDao.trouverHistoriqueParIntervenant(intervenant);
        JpaUtil.fermerContextePersistance();
        return historique;
    }

    public Boolean lancerVisio(Soutien soutien) {
        Boolean success = false;
        try {
            JpaUtil.creerContextePersistance();
            soutien.setEtat(Soutien.EtatSoutien.EN_VISIO);
            soutien.setDate(new Date());

            JpaUtil.ouvrirTransaction();
            soutienDao.update(soutien);
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

    public Boolean terminerVisio(Soutien soutien) {
        Boolean success = false;
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
            return success;
        }
    }

    public Boolean faireAutoEvaluationEleve(Soutien soutien, Integer niveau) {
        Boolean success = false;
        try {
            JpaUtil.creerContextePersistance();
            soutien.setAutoevaluationEleve(niveau);

            JpaUtil.ouvrirTransaction();
            soutienDao.update(soutien);
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

    public Boolean redigerBilan(Soutien soutien, String bilan) {
        Boolean success = false;
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
            return success;
        }
    }

    public Integer statNbTotalIntervention() {
        JpaUtil.creerContextePersistance();
        Integer result =  soutienDao.getCountInterventions();
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
        return soutienDao.getSatisfactionMoyenneEleve();
    }
    
    public String statIntervenantMois() {
        return soutienDao.getIntervenantMois().getPrenom() + " " + soutienDao.getIntervenantMois().getNom();
    }
    
    public Integer statIntervenantActif() {
        return intervenantDao.getNbIntervActif();
    }
    
    public Integer statNbEleveInscrit() {
        return eleveDao.getTotalEleveInscrit();
    }

    public void initialisation() {
        Etudiant intervenant1 = new Etudiant("Martin", "Camille", "0655447788", "tutu", Arrays.asList(0, 1, 2, 3, 4, 5, 6), "Sorbonne", "Langues orientales");
        //Intervenant intervenant1 = new Intervenant("Martin", "Camille", "0655447788", "tutu", Arrays.asList(3, 4, 5, 6));
        inscrireIntervenant(intervenant1);
        Intervenant intervenant2 = new Intervenant("Zola", "Anna", "0633221144", "tata", Arrays.asList(0, 1, 2, 3, 4, 5, 6));
        inscrireIntervenant(intervenant2);
        Intervenant intervenant3 = new Intervenant("Hugo", "Emile", "0788559944", "toto", Arrays.asList(3));
        inscrireIntervenant(intervenant3);
        Intervenant intervenant4 = new Intervenant("Yourcenar", "Simone", "0722447744", "titi", Arrays.asList(1, 2, 3, 4, 5));
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
