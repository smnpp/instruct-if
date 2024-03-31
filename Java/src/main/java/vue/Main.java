/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;


import metier.service.Service;
import dao.JpaUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 *
 * @author selghissas
 */
import metier.modele.Eleve;
import metier.modele.Matiere;


public class Main {

    public static void main(String[] args) {

        JpaUtil.creerFabriquePersistance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Service service = new Service();

        try {
            //initialisation des intervenant et des matieres.
            service.initialisation();
            //Inscription d'élèves
            Date date1 = sdf.parse("27/03/2011");
            Eleve eleve1 = new Eleve("Pascal", "Alice", "alice.pascal@free.fr", "123456", date1, 5);
            service.inscrireEleve(eleve1, "0691664J");

            Date date2 = sdf.parse("27/03/2011");
            Eleve eleve2 = new Eleve("Jean", "François", "jeanfrancois@free.fr", "987654", date2, 2);
            service.inscrireEleve(eleve2, "0691664J");

            Date date3 = sdf.parse("28/03/2011");
            Eleve eleve3 = new Eleve("Paul", "Paul", "ppaul@free.fr", "123456", date3, 4);
            service.inscrireEleve(eleve3, "0691478G");
            
            
            //Authentification d'élèves
            if (service.authentifierEleveMail("alice.pascal@free.fr", "123456") != null) {
            System.out.println("Authentification réussie"
            + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("Erreur d'authentification");
            }
            
            if (service.authentifierEleveMail("jeanfrancois@free.fr", "123456") != null) {
            System.out.println("Authentification réussie"
            + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("Erreur d'authentification");
            }
            
            //Authentification intervenant
            if (service.authentifierIntervenantTelephone("0655447788", "tutu") != null) {
            System.out.println("Authentification réussie");
            } else {
                System.out.println("Erreur d'authentification");
            }
            if (service.authentifierIntervenantTelephone("0655447788", "tata") != null) {
            System.out.println("Authentification réussie");
            } else {
                System.out.println("Erreur d'authentification");
            }

            
            List<Matiere> listeMatieres = service.consulterListeMatieres();
            System.out.println("Liste des matières pour lesquelles vous pouvez demander un soutien :");
            for (Matiere matiere : listeMatieres) {
            System.out.println(matiere.getNom());
            }
            
            //Persistance de Soutien (et au passage, de la matière du soutien
            //Matiere matiere = new Matiere("Histoire-Géographie") ; 
            String descriptif = "Je n'ai pas très bien compris le chapitre sur le Moyen-Age, "
                    + "j'aimerais un petit peut d'aide avant de commencer mon devoir" ;
            //System.out.println( service.creerSoutien(eleve1, mathematiques, descriptif) ? 
                    //"Soutien créé avec succès" : "Le soutien n'a pu être créé");
            service.creerSoutien(eleve1, listeMatieres.get(0), "Je n'ai pas très bien compris le chapitre sur le Moyen-Age, j'aimerais un petit peut d'aide avant de commencer mon devoir");
            service.creerSoutien(eleve1, listeMatieres.get(2), "deuxieme soutien eleve 1");
            service.creerSoutien(eleve1, listeMatieres.get(5), "troisieme soutien eleve 1");
            service.creerSoutien(eleve3, listeMatieres.get(4), "premier soutien eleve 3");
            
            System.out.println(service.obtenirHistoriqueEleve(eleve1));
            
            } catch (ParseException e) {
                e.printStackTrace();
            }

        JpaUtil.fermerFabriquePersistance();
    }
}
