/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import java.util.Scanner;
import util.Saisie;
import metier.service.Service;
import dao.JpaUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import metier.modele.Eleve;
import metier.modele.Matiere;

/**
 *
 * @author sperret
 */
public class MainBis {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JpaUtil.creerFabriquePersistance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Service service = new Service();
        
        int choix;

        do {
            // Affichage du menu
            System.out.println("Menu :\n"
                    + "\n"
                    + "\t1: Se connecter\n"
                    + "\t2: Créer un compte\n"
                    + "\t3: Passer en mode intervenant\n"
                    + "\t0: Quitter\n");

            System.out.print(" > ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 0:
                    break;

                case 1:
                    service.authentifierEleveMail(Saisie.lireChaine("Enter email : "), Saisie.lireChaine("Enter password : "));
                    break;

                case 2:
                    // Code pour la recherche avancée
                    break;

                case 3:
                    // Code pour ajouter un trajet au catalogue
                    break;

                case 4:
                    // Code pour afficher le catalogue
                    break;

                default:
                    System.out.println("Choix incorrect");
            }
        } while (choix != 0);

        scanner.close();

        System.out.println("Au revoir");
    }

}
