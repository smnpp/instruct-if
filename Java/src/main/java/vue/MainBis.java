package vue;

import metier.service.Service;
import dao.JpaUtil;
import util.Saisie;
import metier.modele.Eleve;
import metier.modele.Matiere;
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

public class MainBis {

    public static void main(String[] args) {

        JpaUtil.creerFabriquePersistance();
        Service service = new Service();
        service.initialisation(); // Initialise les données nécessaires

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        boolean continuer = true;
        while (continuer) {
            System.out.println("\u001b[34m" +"Choisissez un scénario à tester:");
            System.out.println("A- Erreur d'inscription d'élève à cause des formats");
            System.out.println("B- Erreur d'inscription d'élève car mauvais UAI");
            System.out.println("C- Erreur de connexion d'élève (mauvais mdp)");
            System.out.println("D- Erreur de connexion d'intervenant (mauvais mdp)");
            System.out.println("E- Soumission d'une demande de soutien avec infos invalides");
            System.out.println("F- Demande de soutien sans intervenant disponible");
            System.out.println("G- Intervenants avec même note pour intervenant du mois");
            System.out.println("H- Quitter");

            String choix = Saisie.lireChaine("Votre choix: ").toUpperCase();
            switch (choix) {
                case "A":
                    testerErreurInscriptionFormat(service);
                    break;
                case "B":
                    testerErreurInscriptionUAI(service);
                    break;
                case "C":
                    //testerErreurConnexionEleve(service);
                    break;
                case "D":
                    //testerErreurConnexionIntervenant(service);
                    break;
                case "E":
                    //testerDemandeSoutienInfosInvalides(service);
                    break;
                case "F":
                    //testerDemandeSoutienSansIntervenant(service);
                    break;
                case "G":
                    //testerIntervenantsMemeNote(service);
                    break;
                case "H":
                    continuer = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix non reconnu, veuillez réessayer.");
            }
        }

        JpaUtil.fermerFabriquePersistance();
    }

    // Différents cas de test
    //Simulation d'une inscription avec un format de date incorrect
    private static void testerErreurInscriptionFormat(Service service) {
        System.out.println("Scénario A - Erreur d'inscription d'élève à cause des formats");
        try {
            Eleve eleve = new Eleve("Test", "Format", "test.format@exemple.com", "mdpIncorrect", new SimpleDateFormat("dd/MM/yyyy").parse("32/13/2000"), 3);
            boolean resultat = service.inscrireEleve(eleve, "0693030A");
            System.out.println(resultat ? "Inscription réussie" : "Erreur d'inscription due au format");
        } catch (Exception e) {
            System.out.println("Erreur de format lors de l'inscription de l'élève.");
        }

    }

    private static void testerErreurInscriptionUAI(Service service) {
        System.out.println("Scénario B - Erreur d'inscription d'élève car mauvais UAI");
        // Tentative d'inscription avec un UAI incorrect
        try {
            Eleve eleve = new Eleve("Erreur", "UAI", "erreur.uai@exemple.com", "mdp123", new SimpleDateFormat("dd/MM/yyyy").parse("15/09/2005"), 2);
            boolean resultat = service.inscrireEleve(eleve, "UAIIncorrect");
            System.out.println(resultat ? "Inscription réussie" : "Inscription échouée due à un UAI incorrect");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'inscription due à un UAI incorrect.");
        }
    }
}
