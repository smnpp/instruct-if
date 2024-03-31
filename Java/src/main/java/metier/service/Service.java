/*
 Le service métier va utiliser EleveDao pour persister les données et GeoNetApi pour obtenir les coordonnées GPS.
 */
package metier.service;

import dao.EleveDao;
import dao.EtablissementDao;
import dao.JpaUtil;
import dao.MatiereDao;
import dao.SoutienDao;
import metier.modele.Eleve;
import metier.modele.Etablissement;
import metier.modele.Matiere;
import util.Message;
import util.EducNetApi;
import java.util.List;
import java.util.Comparator;
import metier.modele.Soutien;

/**
 *
 * @author selghissas
 */
public class Service {

    public EleveDao eleveDao = new EleveDao();
    public EtablissementDao etablissementDao = new EtablissementDao();
    public SoutienDao soutienDao = new SoutienDao();
    public MatiereDao matiereDao = new MatiereDao() ;

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
                }

                else {
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
                        + eleve.getPrenom() + ", Nous te confirmons ton inscription sur le réseau INSTRUCT'IF. Si tu as besoin d'un soutien pour tes"
                        + " leçons, ou tes devoirs, rends-toi sur notre site pour une mise en relation avec un intervenant.");
            }

            else {
                JpaUtil.annulerTransaction();

                // Envoi du mail d'infirmité
                Message.envoyerMail("contact@instruct.if", eleve.getMail(), "Échec de l'inscription sur le réseau INSTRUCT'IF",
                        "Bonjour "
                        + eleve.getPrenom() + " ton inscription sur le réseau INSTRUCT'IF a malencontreusement échoué... Merci de recommencer "
                        + "ultérieurement.");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            JpaUtil.annulerTransaction();

            // Envoi du mail d'infirmité
            Message.envoyerMail("contact@instruct.if", eleve.getMail(), "Échec de l'inscription sur le réseau INSTRUCT'IF",
                "Bonjour "
                + eleve.getPrenom() + " ton inscription sur le réseau INSTRUCT'IF a malencontreusement échoué... Merci de recommencer ultérieurement.");
        }
    
        finally {
            JpaUtil.fermerContextePersistance();
        }
        return success ;
        }

    public Eleve authentifierEleveMail(String mail, String motDePasse) { //Authentifier un eleve
        JpaUtil.creerContextePersistance();
        Eleve eleve = eleveDao.trouverParMail(mail);
        JpaUtil.fermerContextePersistance();
        if (eleve != null && eleve.getMotDePasse().equals(motDePasse)) {
            return eleve;
        } else {
            return null;
        }
    }

    public Eleve trouverEleveParId(Long id) { //Trouver un élève a partir de son ID (non encore utilisée)
        JpaUtil.creerContextePersistance();
        Eleve eleve = eleveDao.trouverParId(id);
        JpaUtil.fermerContextePersistance();
        if (eleve != null && eleve.getId().equals(id)) {
            return eleve;
        } else {
            return null;
        }
    }

    public List<Eleve> consulterListeEleves() { //Consulter la liste de tous les élèves inscrits (non encore utilisée
        JpaUtil.creerContextePersistance();
        List<Eleve> eleves = eleveDao.getAllEleves();
        JpaUtil.fermerContextePersistance();
        if (eleves != null) {
            eleves.sort(Comparator.comparing(Eleve::getNom).thenComparing(Eleve::getPrenom));
        }
        return eleves;
    }
    
    public List<Matiere> obtenirListeMatieres() { 
        //Obtenir la liste des matières (utiliser ce service pour le menu déroulant 
        //de la page de demande de soutien des élèves)
        return Matiere.obtenirMatieresValides();
    }
    
    public boolean creerSoutien (Eleve eleve, Matiere matiere, String descriptif){
        boolean success = false ; 
        Soutien soutien = new Soutien() ; 
        soutien.setEleve(eleve);
        soutien.setDescriptif(descriptif);
        soutien.setMatiere(matiere);
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            if (matiereDao.trouverParLibelle(matiere.getLibelle()) == null) {
                matiereDao.create(matiere);
            }
            soutienDao.create(soutien);
            JpaUtil.validerTransaction();
            success = true ; 
        } catch (Exception e) {
            e.printStackTrace();
            JpaUtil.annulerTransaction();}
        finally {
            JpaUtil.fermerContextePersistance();
        }
        return success ;
    }
}
