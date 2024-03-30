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
import metier.modele.Soutien;

public class Main {

    public static void main(String[] args) {

        JpaUtil.creerFabriquePersistance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Service service = new Service();

        try {
            //Inscription d'élèves
            Date date1 = sdf.parse("27/03/2011");
            Eleve eleve1 = new Eleve("Pascal", "Alice", "alice.pascal@free.fr", "123456", date1, 5);
            service.inscrireEleve(eleve1, "0691664J");

            Date date2 = sdf.parse("27/03/2011");
            Eleve eleve2 = new Eleve("Jean", "François", "jeanfrancois@free.fr", "987654", date2, 5);
            service.inscrireEleve(eleve2, "0691664J");

            Date date3 = sdf.parse("28/03/2011");
            Eleve eleve3 = new Eleve("Paul", "Paul", "pfrancois@free.fr", "123456", date3, 5);
            service.inscrireEleve(eleve3, "0691478G");
            
            //Authentification d'élèves
            if (service.authentifierEleveMail(eleve1.getMail(), eleve1.getMotDePasse()) != null) {
            System.out.println("L'élève "+ eleve1.getNom() + " " + eleve1.getPrenom() + " a été authentifié(e)"
            + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("Elève non inscrit sur INSTRUCT'IF");
            }
            
            //Demande de soutien de la part d'un élève
            
            //Consultation de la liste des matières par l'élève
            List<Matiere> listeMatieres = service.obtenirListeMatieres();
            System.out.println("Liste des matières pour lesquelles vous pouvez demander un soutien :");
            for (Matiere matiere : listeMatieres) {
            System.out.println(matiere.getLibelle());
            }
            
            //Persistance de Soutien (et au passage, de la matière du soutien
            Matiere matiere = new Matiere("Histoire-Géographie") ; 
            String descriptif = "Je n'ai pas très bien compris le chapitre sur le Moyen-Age, "
                    + "j'aimerais un petit peut d'aide avant de commencer mon devoir" ;
            System.out.println( service.creerSoutien(eleve1, matiere, descriptif) ? 
                    "Soutien créé avec succès" : "Le soutien n'a pu être créé");
            
            
            } catch (ParseException e) {
                e.printStackTrace();
            }

        JpaUtil.fermerFabriquePersistance();
    }
}
