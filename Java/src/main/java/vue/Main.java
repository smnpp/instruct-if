package vue;

import metier.service.Service;
import dao.JpaUtil;
import static java.lang.Integer.parseInt;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author sperret
 */
import metier.modele.Eleve;
import metier.modele.Intervenant;
import metier.modele.Matiere;
import metier.modele.Soutien;

public class Main {

    public static void main(String[] args) throws InterruptedException, Exception {
        JpaUtil.creerFabriquePersistance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Scanner scanner = new Scanner(System.in);

        int choix;

        Service service = new Service();
        JpaUtil.creerFabriquePersistance();
        service.initialisation();
        System.out.println("Choisissez un scénario :");
        System.out.println("1. Simulation complète avec un seul élève (saisie manuelle)");
        System.out.println("2. Scénario complète avec plusieurs élèves qui se voient accorder ou refuser leurs inscriptions, connexions et soutiens");
        System.out.println("3. Scénario où il n'y a plus d'intervenants disponible car tous occupés ou ne prennent pas en charge la classe de l'élève");
        System.out.println("4. Scénario de connexion avec mot de passe incorrect");
        System.out.println("9. Scénario du sujet");
        System.out.println("0. Quitter");

        System.out.print("Votre choix : ");
        choix = scanner.nextInt();

        switch (choix) {
            case 1:
                scenario1(service);
                break;

            case 2:
                scenario2(service);
                break;

            case 3:
                scenario3(service);
                break;
            case 4:
                scenarioMdpIncorrect(service);
                break;
            case 9:
                scenarioSujet(service);
                break;
            case 0:
                break;
            default:
                System.out.println("Choix incorrect");
        }

        JpaUtil.fermerFabriquePersistance();
        scanner.close();
    }

    public static void scenarioMdpIncorrect(Service service) {
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

    private static void scenario1(Service service) throws ParseException, Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Scanner scanner = new Scanner(System.in);
        try {
            //Inscription d'un élève
            System.out.println("\u001b[34m" + "Page de création de compte élève");
            System.out.println("\u001b[34m" + "Entrez dans l'ordre : Nom, Prenom, date (format dd/MM/yyyy), mail, mdp, code établissement, classe");
            //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            String nom_eleve1 = scanner.nextLine(); //Pascal
            String prenom_eleve1 = scanner.nextLine(); //Alice
            Date date_eleve1 = sdf.parse(scanner.nextLine());
            String mail_eleve1 = scanner.nextLine(); //alice.pascal@free.fr
            String mdp_eleve1 = scanner.nextLine(); //123456
            String uai_eleve1 = scanner.nextLine(); //0691664J
            Integer classe_eleve1 = parseInt(scanner.nextLine()); //5

            //L'élève clique sur le bouton de création de compte
            Eleve eleve1 = new Eleve(nom_eleve1, prenom_eleve1, mail_eleve1, mdp_eleve1, date_eleve1, classe_eleve1);
            service.inscrireEleve(eleve1, uai_eleve1);

            //L'élève va sur la page de connexion et s'authentifie
            System.out.println("\u001b[34m" + "Page de connexion élève");
            System.out.println("\u001b[34m" + "Entrez dans l'ordre : mail, mdp");
            Eleve eleveLogIn = service.authentifierEleveMail(scanner.nextLine(), scanner.nextLine());
            if (eleveLogIn != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour "
                        + eleveLogIn.getNom() + " " + eleveLogIn.getPrenom()
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification de l'élève");
            }

            //L'élève accède à la page de demande de Soutien
            System.out.println("\u001b[34m" + "Page de demande de Soutien élève");
            System.out.println("\u001b[34m" + "Voici la liste des soutiens matière disponible");
            List<Matiere> listeMatieres = service.consulterListeMatieres();
            for (Matiere matiere : listeMatieres) {
                System.out.println("\u001b[34m" + matiere.getNom());
            }
            System.out.println("\u001b[34m" + "Sélectionnez une matière (nombre entier)");
            System.out.println("\u001b[34m" + "(par exemple, pour la première matière qui s'affiche, entrez le numéro 1)");
            System.out.println("\u001b[34m" + "puis saisissez le descriptif de votre demande de soutien");

            Integer numero_matiere_soutien1 = parseInt(scanner.nextLine());
            String descriptif_soutien1 = scanner.nextLine();
            Soutien soutien_eleve1 = service.demanderSoutien(eleveLogIn, listeMatieres.get(numero_matiere_soutien1), descriptif_soutien1);
            if (soutien_eleve1 != null) {
                System.out.println("\u001b[34m" + "Votre demande de soutien a été traitée avec succès. Veuillez Attendre le lancement de votre visio");
            } else {
                System.out.println("\u001b[34m" + "Echec lors de la création de votre soutien");
            }

            //L'intervenant a reçu la notification de demande de Soutien. Il se connecte sur son espace
            System.out.println("\u001b[34m" + "Page de connexion Intervenant");
            System.out.println("\u001b[34m" + "Veuillez saisir votre téléphone et mdp");
            String tel_intervenant1 = scanner.nextLine();
            String mdp_intervenant1 = scanner.nextLine();
            Intervenant intervenant1 = service.authentifierIntervenantTelephone(tel_intervenant1, mdp_intervenant1);
            if (intervenant1 != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour " + intervenant1.getNom() + " " + intervenant1.getPrenom());
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification de l'intervenant");
            }

            //L'intervenant accède à l'espace de consultation des soutiens
            System.out.println("\u001b[34m" + "Page de consultation des soutiens Intervenant");
            System.out.println("\u001b[34m" + "Informations du soutien demandé : ");
            Soutien soutienIntervenant = service.obtenirSoutienEnAttenteParIntervenantId(intervenant1.getId());
            System.out.println("\u001b[34m" + "Matiere : " + soutienIntervenant.getMatiere().getNom());
            System.out.println("\u001b[34m" + "Eleve : " + soutienIntervenant.getEleve().getNom() + " " + soutienIntervenant.getEleve().getPrenom());
            System.out.println("\u001b[34m" + "Etablissement : " + soutienIntervenant.getEleve().getEtablissement().getNomEtablissement());
            System.out.println("\u001b[34m" + "Classe : " + soutienIntervenant.getEleve().getClasse());
            System.out.println("\u001b[34m" + "Description : " + soutienIntervenant.getDescriptif());

            //L'intervenant clique sur lancer la visio
            System.out.println(
                    service.lancerVisio(soutienIntervenant)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien1"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            //La visio se lance chez l'intervenant et chez l'élève
            //Remarquons que soutien_eleve1 pointe vers un objet soutien qui est toujours EN_ATTENTE
            // --> Il faut donc le mettre a jour 
            soutien_eleve1 = service.refreshSoutien(soutien_eleve1);

            Thread.sleep(2000);

            System.out.println(
                    service.terminerVisio(soutien_eleve1) //On aurait pu appeler soutienIntervenant : les deux peuvent mettre fin a la visio
                    ? "\u001b[32m" + "Fin de la visio pour le soutien1"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien1 (peut-être qu'il n'existe pas)"
            );
            soutienIntervenant = service.refreshSoutien(soutienIntervenant); //Comme tout a l'heure, on rafraichit le soutien

            //L'élève accède a la page de rédaction de l'autoévaluation
            System.out.println("\u001b[34m" + "Page d'autoévaluation du soutien par l'élève");
            System.out.println("\u001b[34m" + "Veuillez évaluer votre soutien (entre un nombre entier)");
            System.out.println("\u001b[34m" + "1 - Pas du tout compris");
            System.out.println("\u001b[34m" + "2 - J'ai encore du mal");
            System.out.println("\u001b[34m" + "3 - J'ai moyennement compris");
            System.out.println("\u001b[34m" + "4 - J'ai acquis l'essentiel");
            System.out.println("\u001b[34m" + "5 - J'ai tout compris");
            String auto_evaluation_eleve_soutien1 = scanner.nextLine();

            System.out.println(
                    service.faireAutoEvaluationEleve(soutien_eleve1, parseInt(auto_evaluation_eleve_soutien1))
                    ? "\u001b[32m" + "Autoévaluation de l'élève faite pour le soutien1"
                    : "\u001b[31m" + "L'autoévaluation de l'élève ne peut être faite pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            //L'intervenant accède a la page de rédaction du bilan
            System.out.println("\u001b[34m" + "Page de soumission du bilan du soutien par l'intervenant");
            System.out.println("\u001b[34m" + "Veuillez saisir le bilan du soutien");
            String bilan_intervenant_soutien1 = scanner.nextLine();

            System.out.println(
                    service.redigerBilan(soutien_eleve1, bilan_intervenant_soutien1)
                    ? "\u001b[32m" + "Bilan de l'intervenant fait pour le soutien1"
                    : "\u001b[31m" + "Le bilan de l'intervenant ne peut être faite pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            //L'élève accède a l'historique de ses soutiens
            System.out.println("\u001b[33m" + "=====HISTORIQUE ELEVE=====");

            List<Soutien> historique_eleve1 = service.trouverHistoriqueEleve(eleve1);
            StringBuilder sb_eleve1 = new StringBuilder();
            for (Soutien soutien : historique_eleve1) {
                sb_eleve1.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique eleve1 : " + sb_eleve1);

            //L'intervenant accède a l'historique de ses interventions
            System.out.println("\u001b[33m" + "=====HISTORIQUE INTERVENANT=====");

            List<Soutien> historique_intervenant1 = service.trouverHistoriqueIntervenant(intervenant1.getId());
            StringBuilder sb_intervenant1 = new StringBuilder();
            for (Soutien soutien : historique_intervenant1) {
                sb_intervenant1.append(String.format("Date: %s, Eleve: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getEleve().getPrenom(),
                        soutien.getEleve().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique intervenant1 : " + sb_intervenant1);

            //L'intervenant accède a son tableau de bord
            System.out.println("\u001b[31m" + "======================");
            System.out.println("\u001b[31m" + "=====TABLEAU DE BORD INTERVENANT=====");
            System.out.println("\u001b[31m" + "Nombre total d'interventions : " + service.statNbTotalIntervention());
            System.out.println("\u001b[31m" + "Duree moyenne d'une intervention : " + service.statDureeMoyenneIntervention());
            System.out.println("\u001b[31m" + "Nombre d'élèves inscrits : " + service.statNbEleveInscrit());
            System.out.println("\u001b[31m" + "Nombre d'intervenants actifs : " + service.statIntervenantActif());
            System.out.println("\u001b[31m" + "Satisfaction moyenne des élèves : " + service.statSatisfactionEleve() + "/5");
            System.out.println("\u001b[31m" + "Intervenant du mois : " + service.statIntervenantMois());
            System.out.println("\u001b[31m" + "IPS moyen des établissement : " + service.statIpsMoyenSoutien());
            System.out.println("\u001b[31m" + "Quantite d'intervention par coordonnées : " + service.statQuantiteSoutienParCoordonnees());
            System.out.println("\u001b[31m" + "======================");
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    private static void scenario2(Service service) throws ParseException, Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Scanner scanner = new Scanner(System.in);
        try {
            //Inscription d'élèves
            Date date1 = sdf.parse("27/03/2011");
            Eleve eleve1 = new Eleve("Pascal", "Alice", "alice.pascal@free.fr", "123456", date1, 5);
            service.inscrireEleve(eleve1, "0691664J");

            Date date2 = sdf.parse("27/03/2008");
            Eleve eleve2 = new Eleve("Jean", "François", "jeanfrancois@free.fr", "987654", date2, 2);
            service.inscrireEleve(eleve2, "0691664J");

            Date date3 = sdf.parse("28/03/2010");
            Eleve eleve3 = new Eleve("Paul", "Paul", "ppaul@free.fr", "123456", date3, 3);
            service.inscrireEleve(eleve3, "0691478G");

            Date date4 = sdf.parse("28/03/2010");
            Eleve eleve4 = new Eleve("Michel", "Arthur", "marthur@free.fr", "123456", date4, 3);
            service.inscrireEleve(eleve4, "0691478G");

            Date date5 = sdf.parse("28/03/2010");
            Eleve eleve5 = new Eleve("Ninon", "Mathilde", "nmathilde@free.fr", "123456", date5, 0);
            service.inscrireEleve(eleve5, "0691478G");

            Date date6 = sdf.parse("12/03/2006");
            Eleve eleve6 = new Eleve("Marc", "Poireau", "mp@free.fr", "123456", date6, 0);
            service.inscrireEleve(eleve6, "0691478");
            System.out.println("\u001b[31m" + "Echec de l'inscription pour cause d'un établissement non valide");

            //Les élèves vont sur la page de connexion et s'authentifient
            if (service.authentifierEleveMail("alice.pascal@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Alice Pascal"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Alice Pascal");
            }

            if (service.authentifierEleveMail("jeanfrancois@free.fr", "987654") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Jean François"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Jean François");
            }

            if (service.authentifierEleveMail("ppaul@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Paul Paul"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Paul Paul");
            }

            if (service.authentifierEleveMail("marthur@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Michel Arthur"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Michel Arthur");
            }

            if (service.authentifierEleveMail("mail_non_valide", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour cause de mail non valide");
            }

            if (service.authentifierEleveMail("nmathilde@free.fr", "mdp_non_valide") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Ninon Mathilde cause de mdp non valide");
            }

            //Les élèves accèdent à la page de demande de Soutien
            List<Matiere> listeMatieres = service.consulterListeMatieres();
            System.out.println("\u001b[34m" + "Liste des matières pour lesquelles vous pouvez demander un soutien :");
            for (Matiere matiere : listeMatieres) {
                System.out.println("\u001b[34m" + matiere.getNom());
            }

            String descriptif = "J'ai besoin d'aide pour réviser avant mon DS ";

            Soutien soutien1 = service.demanderSoutien(eleve1, listeMatieres.get(1), descriptif);
            Soutien soutien2 = service.demanderSoutien(eleve2, listeMatieres.get(2), descriptif);
            Soutien soutien3 = service.demanderSoutien(eleve3, listeMatieres.get(3), descriptif);
            Soutien soutien4 = service.demanderSoutien(eleve4, listeMatieres.get(4), descriptif);

            Intervenant intervenant1 = soutien1.getIntervenant();
            Intervenant intervenant2 = soutien2.getIntervenant();
            Intervenant intervenant3 = soutien3.getIntervenant();
            Intervenant intervenant4 = soutien4.getIntervenant();

            //Les intervenants ont reçu des notifications de demande de Soutien. Ils se connectent sur leurs espaces
            if (service.authentifierIntervenantTelephone("0655447788", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Camille Martin");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Camille Martin");
            }

            if (service.authentifierIntervenantTelephone("0633221144", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Anna Zola");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Anna Zola");
            }

            if (service.authentifierIntervenantTelephone("0788559944", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Emile Hugo");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Emile Hugo");
            }

            if (service.authentifierIntervenantTelephone("0722447744", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Simone Yourcenar");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Simone Yourcenar");
            }

            System.out.println(
                    service.lancerVisio(soutien1)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien1"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.lancerVisio(soutien2)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien2"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien2 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.lancerVisio(soutien3)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien3"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien3 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.lancerVisio(soutien4)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien4"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien4 (peut-être qu'il n'existe pas)"
            );

            //Fin des visios pour les soutiens existant
            System.out.println(
                    service.terminerVisio(soutien1)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien1"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.terminerVisio(soutien2)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien2"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien2 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.terminerVisio(soutien3)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien3"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien3 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.terminerVisio(soutien4)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien4"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien4 (peut-être qu'il n'existe pas)"
            );

            //Rédaction des Autoévaluations Elèves et Bilans Intervenants
            String bilanIntervenant = "Bonne séance";

            System.out.println(
                    service.faireAutoEvaluationEleve(soutien1, 4)
                    ? "\u001b[32m" + "Autoévaluation de l'élève faite pour le soutien1"
                    : "\u001b[31m" + "L'autoévaluation de l'élève ne peut être faite pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.redigerBilan(soutien1, bilanIntervenant)
                    ? "\u001b[32m" + "Bilan de l'intervenant fait pour le soutien1"
                    : "\u001b[31m" + "Le bilan de l'intervenant ne peut être faite pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.faireAutoEvaluationEleve(soutien2, 3)
                    ? "\u001b[32m" + "Autoévaluation de l'élève faite pour le soutien2"
                    : "\u001b[31m" + "L'autoévaluation de l'élève ne peut être faite pour le soutien2 (peut-être qu'il n'existe pas)"
            );
            System.out.println(
                    service.redigerBilan(soutien2, bilanIntervenant)
                    ? "\u001b[32m" + "Bilan de l'intervenant fait pour le soutien2"
                    : "\u001b[31m" + "Le bilan de l'intervenant ne peut être faite pour le soutien2 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.faireAutoEvaluationEleve(soutien3, 2)
                    ? "\u001b[32m" + "Autoévaluation de l'élève faite pour le soutien3"
                    : "\u001b[31m" + "L'autoévaluation de l'élève ne peut être faite pour le soutien3 (peut-être qu'il n'existe pas)"
            );
            System.out.println(
                    service.redigerBilan(soutien3, bilanIntervenant)
                    ? "\u001b[32m" + "Bilan de l'intervenant fait pour le soutien3"
                    : "\u001b[31m" + "Le bilan de l'intervenant ne peut être faite pour le soutien3 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.faireAutoEvaluationEleve(soutien4, 5)
                    ? "\u001b[32m" + "Autoévaluation de l'élève faite pour le soutien4"
                    : "\u001b[31m" + "L'autoévaluation de l'élève ne peut être faite pour le soutien4 (peut-être qu'il n'existe pas)"
            );
            System.out.println(
                    service.redigerBilan(soutien4, bilanIntervenant)
                    ? "\u001b[32m" + "Bilan de l'intervenant fait pour le soutien4"
                    : "\u001b[31m" + "Le bilan de l'intervenant ne peut être faite pour le soutien4 (peut-être qu'il n'existe pas)"
            );

            //Les élèves accèdent a leur historique de soutiens
            System.out.println("\u001b[33m" + "=====HISTORIQUE ELEVES=====");

            List<Soutien> historique_eleve1 = service.trouverHistoriqueEleve(eleve1);
            StringBuilder sb_eleve1 = new StringBuilder();
            for (Soutien soutien : historique_eleve1) {
                sb_eleve1.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique Alice : " + sb_eleve1);

            List<Soutien> historique_eleve2 = service.trouverHistoriqueEleve(eleve2);
            StringBuilder sb_eleve2 = new StringBuilder();
            for (Soutien soutien : historique_eleve2) {
                sb_eleve2.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique François : " + sb_eleve2);

            List<Soutien> historique_eleve3 = service.trouverHistoriqueEleve(eleve3);
            StringBuilder sb_eleve3 = new StringBuilder();
            for (Soutien soutien : historique_eleve3) {
                sb_eleve3.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique Paul : " + sb_eleve3);

            List<Soutien> historique_eleve4 = service.trouverHistoriqueEleve(eleve4);
            StringBuilder sb_eleve4 = new StringBuilder();
            for (Soutien soutien : historique_eleve4) {
                sb_eleve4.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique Arthur : " + sb_eleve4);

            //Les intervenants accèdent a leurs historiques d'interventions
            System.out.println("\u001b[33m" + "=====HISTORIQUE INTERVENANTS=====");

            List<Soutien> historique_intervenant1 = service.trouverHistoriqueIntervenant(intervenant1.getId());
            StringBuilder sb_intervenant1 = new StringBuilder();
            for (Soutien soutien : historique_intervenant1) {
                sb_intervenant1.append(String.format("Date: %s, Eleve: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getEleve().getPrenom(),
                        soutien.getEleve().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique " + intervenant1.getNom() + " " + intervenant1.getPrenom() + " " + sb_intervenant1);

            List<Soutien> historique_intervenant2 = service.trouverHistoriqueIntervenant(intervenant2.getId());
            StringBuilder sb_intervenant2 = new StringBuilder();
            for (Soutien soutien : historique_intervenant1) {
                sb_intervenant2.append(String.format("Date: %s, Eleve: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getEleve().getPrenom(),
                        soutien.getEleve().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique " + intervenant2.getNom() + " " + intervenant2.getPrenom() + " " + sb_intervenant2);

            List<Soutien> historique_intervenant3 = service.trouverHistoriqueIntervenant(intervenant3.getId());
            StringBuilder sb_intervenant3 = new StringBuilder();
            for (Soutien soutien : historique_intervenant1) {
                sb_intervenant3.append(String.format("Date: %s, Eleve: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getEleve().getPrenom(),
                        soutien.getEleve().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique " + intervenant3.getNom() + " " + intervenant3.getPrenom() + " " + sb_intervenant3);

            List<Soutien> historique_intervenant4 = service.trouverHistoriqueIntervenant(intervenant4.getId());
            StringBuilder sb_intervenant4 = new StringBuilder();
            for (Soutien soutien : historique_intervenant1) {
                sb_intervenant4.append(String.format("Date: %s, Eleve: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getEleve().getPrenom(),
                        soutien.getEleve().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique " + intervenant4.getNom() + " " + intervenant4.getPrenom() + " " + sb_intervenant4);

            //Tableau bord des intervenants
            System.out.println("\u001b[31m" + "======================");
            System.out.println("\u001b[31m" + "=====TABLEAU DE BORD INTERVENANT=====");
            System.out.println("\u001b[31m" + "Nombre total d'interventions : " + service.statNbTotalIntervention());
            System.out.println("\u001b[31m" + "Duree moyenne d'une intervention : " + service.statDureeMoyenneIntervention());
            System.out.println("\u001b[31m" + "Nombre d'élèves inscrits : " + service.statNbEleveInscrit());
            System.out.println("\u001b[31m" + "Nombre d'intervenants actifs : " + service.statIntervenantActif());
            System.out.println("\u001b[31m" + "Satisfaction moyenne des élèves : " + service.statSatisfactionEleve() + "/5");
            System.out.println("\u001b[31m" + "Intervenant du mois : " + service.statIntervenantMois());
            System.out.println("\u001b[31m" + "IPS moyen des établissement : " + service.statIpsMoyenSoutien());
            System.out.println("\u001b[31m" + "Quantite d'intervention par coordonnées : " + service.statQuantiteSoutienParCoordonnees());
            System.out.println("\u001b[31m" + "======================");

            //Ajout d'un soutien pour l'élève3
            Soutien soutien6 = service.demanderSoutien(eleve3, listeMatieres.get(2), descriptif);
            Intervenant intervenant6 = soutien6.getIntervenant();

            System.out.println(
                    service.lancerVisio(soutien6)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien6"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien6 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.terminerVisio(soutien6)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien6"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien6 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.faireAutoEvaluationEleve(soutien6, 4)
                    ? "\u001b[32m" + "Autoévaluation de l'élève faite pour le soutien6"
                    : "\u001b[31m" + "L'autoévaluation de l'élève ne peut être faite pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.redigerBilan(soutien6, bilanIntervenant)
                    ? "\u001b[32m" + "Bilan de l'intervenant fait pour le soutien5"
                    : "\u001b[31m" + "Le bilan de l'intervenant ne peut être faite pour le soutien5 (peut-être qu'il n'existe pas)"
            );

            //On vérifie que les historiques et statistiques se sont bien mis a jour
            historique_eleve3 = service.trouverHistoriqueEleve(eleve3);
            StringBuilder sb_eleve6 = new StringBuilder();
            for (Soutien soutien : historique_eleve3) {
                sb_eleve6.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique Paul : " + sb_eleve6);

            List<Soutien> historique_intervenant6 = service.trouverHistoriqueIntervenant(intervenant6.getId());
            StringBuilder sb_intervenant6 = new StringBuilder();
            for (Soutien soutien : historique_intervenant1) {
                sb_intervenant6.append(String.format("Date: %s, Eleve: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getEleve().getPrenom(),
                        soutien.getEleve().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[33m" + "Historique " + intervenant6.getNom() + " " + intervenant6.getPrenom() + " " + sb_intervenant4);

            //Tableau bord des intervenants
            System.out.println("\u001b[31m" + "======================");
            System.out.println("\u001b[31m" + "=====TABLEAU DE BORD INTERVENANT=====");
            System.out.println("\u001b[31m" + "Nombre total d'interventions : " + service.statNbTotalIntervention());
            System.out.println("\u001b[31m" + "Duree moyenne d'une intervention : " + service.statDureeMoyenneIntervention());
            System.out.println("\u001b[31m" + "Nombre d'élèves inscrits : " + service.statNbEleveInscrit());
            System.out.println("\u001b[31m" + "Nombre d'intervenants actifs : " + service.statIntervenantActif());
            System.out.println("\u001b[31m" + "Satisfaction moyenne des élèves : " + service.statSatisfactionEleve() + "/5");
            System.out.println("\u001b[31m" + "Intervenant du mois : " + service.statIntervenantMois());
            System.out.println("\u001b[31m" + "IPS moyen des établissement : " + service.statIpsMoyenSoutien());
            System.out.println("\u001b[31m" + "Quantite d'intervention par coordonnées : " + service.statQuantiteSoutienParCoordonnees());
            System.out.println("\u001b[31m" + "======================");

        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    private static void scenario3(Service service) throws ParseException, Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Scanner scanner = new Scanner(System.in);
        try {
            //Inscription d'élèves
            Date date1 = sdf.parse("27/03/2011");
            Eleve eleve1 = new Eleve("Pascal", "Alice", "alice.pascal@free.fr", "123456", date1, 5);
            service.inscrireEleve(eleve1, "0691664J");

            Date date2 = sdf.parse("27/03/2008");
            Eleve eleve2 = new Eleve("Jean", "François", "jeanfrancois@free.fr", "987654", date2, 2);
            service.inscrireEleve(eleve2, "0691664J");

            Date date3 = sdf.parse("28/03/2010");
            Eleve eleve3 = new Eleve("Paul", "Paul", "ppaul@free.fr", "123456", date3, 3);
            service.inscrireEleve(eleve3, "0691478G");

            Date date4 = sdf.parse("28/03/2010");
            Eleve eleve4 = new Eleve("Michel", "Arthur", "marthur@free.fr", "123456", date4, 3);
            service.inscrireEleve(eleve4, "0691478G");

            Date date5 = sdf.parse("28/03/2010");
            Eleve eleve5 = new Eleve("Ninon", "Mathilde", "nmathilde@free.fr", "123456", date5, 0);
            service.inscrireEleve(eleve5, "0691478G");

            Date date6 = sdf.parse("28/03/2010");
            Eleve eleve6 = new Eleve("Brigitte", "Dupres", "bd@free.fr", "123456", date6, 0);
            service.inscrireEleve(eleve6, "0691478G");

            //Les élèves vont sur la page de connexion et s'authentifient
            if (service.authentifierEleveMail("alice.pascal@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Alice Pascal"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Alice Pascal");
            }

            if (service.authentifierEleveMail("jeanfrancois@free.fr", "987654") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Jean François"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Jean François");
            }

            if (service.authentifierEleveMail("ppaul@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Paul Paul"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Paul Paul");
            }

            if (service.authentifierEleveMail("marthur@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Michel Arthur"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Michel Arthur");
            }

            if (service.authentifierEleveMail("nmathilde@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Ninon Mathilde cause de mdp non valide");
            }

            //Les élèves accèdent à la page de demande de Soutien
            List<Matiere> listeMatieres = service.consulterListeMatieres();
            System.out.println("\u001b[34m" + "Liste des matières pour lesquelles vous pouvez demander un soutien :");
            for (Matiere matiere : listeMatieres) {
                System.out.println("\u001b[34m" + matiere.getNom());
            }

            String descriptif = "J'ai besoin d'aide pour réviser avant mon DS ";

            Soutien soutien1 = service.demanderSoutien(eleve1, listeMatieres.get(1), descriptif);
            Soutien soutien2 = service.demanderSoutien(eleve2, listeMatieres.get(2), descriptif);
            Soutien soutien3 = service.demanderSoutien(eleve3, listeMatieres.get(3), descriptif);
            Soutien soutien4 = service.demanderSoutien(eleve4, listeMatieres.get(4), descriptif);
            Soutien soutien5 = service.demanderSoutien(eleve5, listeMatieres.get(5), descriptif);
            Soutien soutien6 = service.demanderSoutien(eleve6, listeMatieres.get(6), descriptif);
            //Les soutiens 5 et 6 sont refusés par manque d'intervenants

            Intervenant intervenant1 = soutien1.getIntervenant();
            Intervenant intervenant2 = soutien2.getIntervenant();
            Intervenant intervenant3 = soutien3.getIntervenant();
            Intervenant intervenant4 = soutien4.getIntervenant();

            //Les intervenants ont reçu des notifications de demande de Soutien. 
            //Ils se connectent sur leurs espaces et lancent leurs soutiens
            if (service.authentifierIntervenantTelephone("0655447788", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Camille Martin");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Camille Martin");
            }

            if (service.authentifierIntervenantTelephone("0633221144", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Anna Zola");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Anna Zola");
            }

            if (service.authentifierIntervenantTelephone("0788559944", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Emile Hugo");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Emile Hugo");
            }

            if (service.authentifierIntervenantTelephone("0722447744", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Simone Yourcenar");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Simone Yourcenar");
            }

            System.out.println(
                    service.lancerVisio(soutien1)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien1"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.lancerVisio(soutien2)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien2"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien2 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.lancerVisio(soutien3)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien3"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien3 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.lancerVisio(soutien4)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien4"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien4 (peut-être qu'il n'existe pas)"
            );

            //Fin des visios pour les soutiens existant
            System.out.println(
                    service.terminerVisio(soutien1)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien1"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.terminerVisio(soutien2)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien2"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien2 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.terminerVisio(soutien3)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien3"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien3 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.terminerVisio(soutien4)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien4"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien4 (peut-être qu'il n'existe pas)"
            );

            //Rédaction des Autoévaluations Elèves et Bilans Intervenants
            String bilanIntervenant = "Bonne séance";

            System.out.println(
                    service.faireAutoEvaluationEleve(soutien1, 4)
                    ? "\u001b[32m" + "Autoévaluation de l'élève faite pour le soutien1"
                    : "\u001b[31m" + "L'autoévaluation de l'élève ne peut être faite pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.redigerBilan(soutien1, bilanIntervenant)
                    ? "\u001b[32m" + "Bilan de l'intervenant fait pour le soutien1"
                    : "\u001b[31m" + "Le bilan de l'intervenant ne peut être faite pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.faireAutoEvaluationEleve(soutien2, 3)
                    ? "\u001b[32m" + "Autoévaluation de l'élève faite pour le soutien2"
                    : "\u001b[31m" + "L'autoévaluation de l'élève ne peut être faite pour le soutien2 (peut-être qu'il n'existe pas)"
            );
            System.out.println(
                    service.redigerBilan(soutien2, bilanIntervenant)
                    ? "\u001b[32m" + "Bilan de l'intervenant fait pour le soutien2"
                    : "\u001b[31m" + "Le bilan de l'intervenant ne peut être faite pour le soutien2 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.faireAutoEvaluationEleve(soutien3, 2)
                    ? "\u001b[32m" + "Autoévaluation de l'élève faite pour le soutien3"
                    : "\u001b[31m" + "L'autoévaluation de l'élève ne peut être faite pour le soutien3 (peut-être qu'il n'existe pas)"
            );
            System.out.println(
                    service.redigerBilan(soutien3, bilanIntervenant)
                    ? "\u001b[32m" + "Bilan de l'intervenant fait pour le soutien3"
                    : "\u001b[31m" + "Le bilan de l'intervenant ne peut être faite pour le soutien3 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.faireAutoEvaluationEleve(soutien4, 5)
                    ? "\u001b[32m" + "Autoévaluation de l'élève faite pour le soutien4"
                    : "\u001b[31m" + "L'autoévaluation de l'élève ne peut être faite pour le soutien4 (peut-être qu'il n'existe pas)"
            );
            System.out.println(
                    service.redigerBilan(soutien4, bilanIntervenant)
                    ? "\u001b[32m" + "Bilan de l'intervenant fait pour le soutien4"
                    : "\u001b[31m" + "Le bilan de l'intervenant ne peut être faite pour le soutien4 (peut-être qu'il n'existe pas)"
            );

            //L'élève 5 redemande un soutien : cette fois, il lui est accordé car des intervenants sont disponibles
            soutien5 = service.demanderSoutien(eleve5, listeMatieres.get(5), descriptif);

            //L'élève 6 redemande un soutien : il est refusé car aucun intervenant disponible ne prend en charge son niveau scolaire
            soutien6 = service.demanderSoutien(eleve5, listeMatieres.get(6), descriptif);

            service.lancerVisio(soutien5);
            service.terminerVisio(soutien5);
            service.redigerBilan(soutien5, descriptif);
            service.faireAutoEvaluationEleve(soutien5, 5);

            //L'élève 6 redemande un soutien : cette fois-ci, il est accepté
            soutien6 = service.demanderSoutien(eleve6, listeMatieres.get(6), descriptif);
            service.lancerVisio(soutien6);
            service.terminerVisio(soutien6);
            service.redigerBilan(soutien6, descriptif);
            service.faireAutoEvaluationEleve(soutien6, 4);

        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    private static void scenarioSujet(Service service) throws ParseException, Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Scanner scanner = new Scanner(System.in);
        try {
            Date date1 = sdf.parse("27/03/2011");
            Eleve eleve1 = new Eleve("Pascal", "Alice", "alice.pascal@free.fr", "123456", date1, 5);
            service.inscrireEleve(eleve1, "0691664J");

            Eleve alice = new Eleve();
            alice = service.authentifierEleveMail("alice.pascal@free.fr", "123456");
            if (alice != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Alice Pascal"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Alice Pascal");
            }

            List<Matiere> listeMatieres = service.consulterListeMatieres();
            System.out.println("\u001b[34m" + "Liste des matières pour lesquelles vous pouvez demander un soutien :");
            for (Matiere matiere : listeMatieres) {
                System.out.println("\u001b[34m" + matiere.getNom());
            }

            Soutien soutien = service.demanderSoutien(alice, listeMatieres.get(5), "Je n'ai pas tout compris dans le chapitre sur le Moyen-Age, et je suis bloquée pour commencer mon devoir");

            Intervenant camille = soutien.getIntervenant();

            if (service.authentifierIntervenantTelephone("0655447788", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie pour Camille Martin");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification pour Camille Martin");
            }

            System.out.println(
                    service.lancerVisio(soutien)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien4 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.terminerVisio(soutien)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.faireAutoEvaluationEleve(soutien, 5)
                    ? "\u001b[32m" + "Autoévaluation de l'élève faite pour le soutien"
                    : "\u001b[31m" + "L'autoévaluation de l'élève ne peut être faite pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.redigerBilan(soutien, "Bonne séance")
                    ? "\u001b[32m" + "Bilan de l'intervenant fait pour le soutien"
                    : "\u001b[31m" + "Le bilan de l'intervenant ne peut être faite pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println("\u001b[31m" + "======================");
            System.out.println("\u001b[31m" + "=====TABLEAU DE BORD INTERVENANT=====");
            System.out.println("\u001b[31m" + "Nombre total d'interventions : " + service.statNbTotalIntervention());
            System.out.println("\u001b[31m" + "Duree moyenne d'une intervention : " + service.statDureeMoyenneIntervention());
            System.out.println("\u001b[31m" + "Nombre d'élèves inscrits : " + service.statNbEleveInscrit());
            System.out.println("\u001b[31m" + "Nombre d'intervenants actifs : " + service.statIntervenantActif());
            System.out.println("\u001b[31m" + "Satisfaction moyenne des élèves : " + service.statSatisfactionEleve() + "/5");
            System.out.println("\u001b[31m" + "Intervenant du mois : " + service.statIntervenantMois());
            System.out.println("\u001b[31m" + "IPS moyen des établissement : " + service.statIpsMoyenSoutien());
            System.out.println("\u001b[31m" + "Quantite d'intervention par coordonnées : " + service.statQuantiteSoutienParCoordonnees());
            System.out.println("\u001b[31m" + "======================");

        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }
}
