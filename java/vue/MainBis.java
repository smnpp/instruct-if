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
            System.out.println("\u001b[34m" + "Choisissez un scénario à tester:");
            System.out.println("\u001b[34m" + "A- Erreur d'inscription d'élève à cause des formats");
            System.out.println("\u001b[34m" + "B- Erreur d'inscription d'élève car mauvais UAI");
            System.out.println("\u001b[34m" + "C- Erreur de connexion d'élève (mauvais mdp)");
            System.out.println("\u001b[34m" + "D- Erreur de connexion d'intervenant (mauvais mdp)");
            System.out.println("\u001b[34m" + "E- Soumission d'une demande de soutien avec infos invalides");
            System.out.println("\u001b[34m" + "F- Demande de soutien sans intervenant disponible");
            System.out.println("\u001b[34m" + "G- Demande de soutien simultanée avec concurrence");
            System.out.println("\u001b[34m" + "H- Quitter");

            String choix = Saisie.lireChaine("Votre choix: ").toUpperCase();
            switch (choix) {
                case "A":
                    testerErreurInscriptionFormat(service);
                    break;
                case "B":
                    testerErreurInscriptionUAI(service);
                    break;
                case "C":
                    testerErreurConnexionEleve(service);
                    break;
                case "D":
                    testerErreurConnexionIntervenant(service);
                    break;
                case "E":
                    testerDemandeSoutienInfosInvalides(service);
                    break;
                case "F":
                    testerDemandeSoutienSansIntervenant(service);
                    break;
                case "G":
                    testerDemandeSoutienConcurrence(service);
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
            Eleve eleve = new Eleve("Test", "Format", "test.format@exemple.com", "mdp", new SimpleDateFormat("dd/MM/yyyy").parse("32/13/2000"), 3);
            boolean resultat = service.inscrireEleve(eleve, "0693030A");
            System.out.println(resultat ? "Inscription réussie" : "Echec d'inscription de l'élève");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'inscription de l'élève.");
        }

    }

    private static void testerErreurInscriptionUAI(Service service) {
        System.out.println("Scénario B - Erreur d'inscription d'élève car mauvais UAI");
        // Tentative d'inscription avec un UAI incorrect
        try {
            Eleve eleve = new Eleve("Erreur", "UAI", "test.format@exemple.com", "mdp123", new SimpleDateFormat("dd/MM/yyyy").parse("15/09/2005"), 2);
            boolean resultat = service.inscrireEleve(eleve, "UAIIncorrect");
            System.out.println(resultat ? "Inscription réussie" : "\u001b[31m" + "Inscription échouée");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'inscription de l'élève");
        }
    }

    private static void testerErreurConnexionEleve(Service service) {
        System.out.println("Scénario C- Erreur de connexion d'élève (mauvais mdp)");
        // On inscrit un élève, puis ce dernier essaye de s'identifier en saisissant le mauvais mdp
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse("27/03/2011");
            Eleve eleve1 = new Eleve("Pascal", "Alice", "alice.pascal@free.fr", "123456", date1, 5);
            service.inscrireEleve(eleve1, "0691664J");

            if (service.authentifierEleveMail("alice.pascal@free.fr", "mdpincorrect") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[31m" + "\u001b[34m" + "Echec d'authentification d'élève");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'authentification.");
        }
    }

    private static void testerErreurConnexionIntervenant(Service service) {
        System.out.println("Scénario B - Erreur d'inscription d'élève car mauvais UAI");
        // essaye de connecter un intervenant déja codé en dur avec un mdp incorrect
        try {
            if (service.authentifierIntervenantTelephone("0655447788", "mdpincorrect") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demandes de Soutien).");
            } else {
                System.out.println("\u001b[31m" + "Echec d'authentification d'intervenant");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'authentification.");
        }
    }

    private static void testerDemandeSoutienInfosInvalides(Service service) {
        System.out.println("E- Soumission d'une demande de soutien avec infos invalides");
        // essaye de créer un soutien avec une matière non valide (ici, probabilités, qu'on essaie de créer au préalable)
        // ça ne va pas fonctionner : la matière doit être codée en dur lors de l'initialisation
        // Seules les matières présentes dans la liste des matières sont valides
        // Pour gagner du temps, afficher la liste comme ci-dessous, 
        // puis entrez l'objet listeMatieres.get(position_dans_liste)
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse("27/03/2011");
            Eleve eleve1 = new Eleve("Pascal", "Alice", "alice.pascal@free.fr", "123456", date1, 5);
            service.inscrireEleve(eleve1, "0691664J");

            if (service.authentifierEleveMail("alice.pascal@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[31m" + "\u001b[34m" + "Echec d'authentification d'élève");
            }

            List<Matiere> listeMatieres = service.consulterListeMatieres();
            System.out.println("\u001b[34m" + "Liste des matières pour lesquelles vous pouvez demander un soutien :");
            for (Matiere matiere : listeMatieres) {
                System.out.println("\u001b[34m" + matiere.getNom());
            }

            String descriptif = "J'ai besoin d'aide pour réviser avant mon DS ";
            Matiere matiere = new Matiere(Long.valueOf(10), "Probabilités");
            service.creerSoutien(eleve1, matiere, descriptif);
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du Soutien.");
        }
    }

    private static void testerDemandeSoutienSansIntervenant(Service service) {
        System.out.println("F- Demande de soutien sans intervenant disponible");
        // Au moment de l'initialisation, 4 intervenants sont codés en dur
        // un assure des cours de la 6eme a la 5eme
        // Un assure des cours de la 4eme a la 3eme
        // Un assure des cours de la 2nde a la 1ere
        // Un assure uniquement des cours de terminale

        //Soient deux élèves de terminale 
        try {
            //On inscrit les deux élèves
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse("27/03/2006");
            Eleve eleve1 = new Eleve("Pierre", "Aymeric", "alice.pascal@free.fr", "123456", date1, 0);
            service.inscrireEleve(eleve1, "0690132U");

            Date date2 = sdf.parse("21/04/2006");
            Eleve eleve2 = new Eleve("Jean", "Luc", "jean.luc@free.fr", "123456", date2, 0);
            service.inscrireEleve(eleve2, "0690132U");

            if (service.authentifierEleveMail("alice.pascal@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour l'élève 1"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[31m" + "\u001b[34m" + "Echec d'authentification de l'élève 1");
            }

            if (service.authentifierEleveMail("jean.luc@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour l'élève 2"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[31m" + "\u001b[34m" + "Echec d'authentification de l'élève 2");
            }

            List<Matiere> listeMatieres = service.consulterListeMatieres();
            System.out.println("\u001b[34m" + "Liste des matières pour lesquelles vous pouvez demander un soutien :");
            for (Matiere matiere : listeMatieres) {
                System.out.println("\u001b[34m" + matiere.getNom());
            }
            String descriptif = "J'ai besoin d'aide pour réviser avant mon DS ";

            service.creerSoutien(eleve1, listeMatieres.get(0), descriptif);
            service.creerSoutien(eleve2, listeMatieres.get(0), descriptif);

        } catch (Exception e) {
            System.out.println("Erreur lors de la création du Soutien.");
        }
    }

    private static void testerDemandeSoutienConcurrence(Service service) {
        System.out.println("G- Demande de soutien simultanée avec concurrence");
        // Au moment de l'initialisation, 4 intervenants sont codés en dur
        // un assure des cours de la 6eme a la 5eme
        // Un assure des cours de la 4eme a la 3eme
        // Un assure des cours de la 2nde a la 1ere
        // Un assure uniquement des cours de terminale

        //Soient deux élèves de terminale 
        try {
            //On inscrit les deux élèves
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse("27/03/2006");
            Eleve eleve1 = new Eleve("Pierre", "Aymeric", "alice.pascal@free.fr", "123456", date1, 2);
            service.inscrireEleve(eleve1, "0690132U");

            Date date2 = sdf.parse("21/04/2006");
            Eleve eleve2 = new Eleve("Jean", "Luc", "jean.luc@free.fr", "123456", date2, 1);
            service.inscrireEleve(eleve2, "0690132U");

            if (service.authentifierEleveMail("alice.pascal@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour l'élève 1"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[31m" + "\u001b[34m" + "Echec d'authentification de l'élève 1");
            }

            if (service.authentifierEleveMail("jean.luc@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour l'élève 2"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[31m" + "\u001b[34m" + "Echec d'authentification de l'élève 2");
            }

            List<Matiere> listeMatieres = service.consulterListeMatieres();
            System.out.println("\u001b[34m" + "Liste des matières pour lesquelles vous pouvez demander un soutien :");
            for (Matiere matiere : listeMatieres) {
                System.out.println("\u001b[34m" + matiere.getNom());
            }
            String descriptif = "J'ai besoin d'aide pour réviser avant mon DS ";
            
            //Les deux élèves demandent leur soutien en même temps
            //Un seul intervenant est disponible
            //C'est l'élève de 1ère qui sera priorisé par l'algorithme
            
            service.demanderSoutien(eleve1, listeMatieres.get(0), descriptif);
            service.demanderSoutien(eleve2, listeMatieres.get(0), descriptif);
            
            List<Soutien> soutiens;
            soutiens = service.creerSoutiens();


        } catch (Exception e) {
            System.out.println("Erreur lors de la création du Soutien.");
        }
    }
}
