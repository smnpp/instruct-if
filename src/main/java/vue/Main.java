package vue;

import metier.service.Service;
import dao.JpaUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sperret
 */
import metier.modele.Eleve;
import metier.modele.Matiere;
import metier.modele.Soutien;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean run = true;
        int choix;

        while (run) {
            JpaUtil.creerFabriquePersistance();
            System.out.println("Choisissez un scénario :");
            System.out.println("1. Scénario simple de demande de soutien");
            System.out.println("2. Scénario de plusieurs demandes de soutien");
            System.out.println("3. Scénario de plusieurs demandes de soutien avec echec d'affiliation d'intervenant");
            System.out.println("0. Quitter");

            System.out.print("Votre choix : ");
            choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    scenarioSimple();
                    break;

                case 2:
                    scenarioComplexe();
                    break;

                case 3:
                    scenarioConflit1();
                    break;
                case 0:
                    run = false;
                    break;
                default:
                    System.out.println("Choix incorrect");
            }

            JpaUtil.fermerFabriquePersistance();

        }
        scanner.close();
    }

    public static void scenarioSimple() throws InterruptedException {
        Service service = new Service();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Scanner scanner = new Scanner(System.in);
        try {
            //initialisation des intervenant et des matieres.
            service.initialisation();
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

            //Authentification d'élèves
            Eleve eleveLogIn = service.authentifierEleveMail("alice.pascal@free.fr", "123456");
            if (eleveLogIn != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            Soutien soutien1 = service.demanderSoutien(eleveLogIn, service.consulterListeMatieres().get(1), "Je n'ai pas compris le cours");

            System.out.print("numéro de téléphone: ");
            String telephone = scanner.nextLine();
            if (service.authentifierIntervenantTelephone(telephone, "mdp") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            System.out.println(
                    service.lancerVisio(soutien1)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien1"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien1 (peut-être qu'il n'existe pas)"
            );
            Thread.sleep(10000);

            //Fin des visios pour les soutiens existant
            System.out.println(
                    service.terminerVisio(soutien1)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien1"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien1 (peut-être qu'il n'existe pas)"
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

            System.out.println("\u001b[31m" + "======================");

            System.out.println("\u001b[33m" + "=====HISTORIQUE ELEVE=====");

            List<Soutien> historique1 = service.trouverHistoriqueEleve(eleve1);
            System.out.println("\u001b[33m" + "Historique eleve1 : " + historique1);
            StringBuilder sb1 = new StringBuilder();
            for (Soutien soutien : historique1) {
                sb1.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }

            System.out.println("\u001b[33m" + "Historique eleve1 : " + sb1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void scenarioComplexe() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Service service = new Service();

        try {
            //initialisation des intervenant et des matieres.
            service.initialisation();
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

            //Authentification d'élèves
            if (service.authentifierEleveMail("alice.pascal@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            if (service.authentifierEleveMail("jeanfrancois@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            if (service.authentifierEleveMail("nmathilde@free.fr", "123456") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            //Authentification intervenant
            if (service.authentifierIntervenantTelephone("0655447788", "mdp") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            if (service.authentifierIntervenantTelephone("0633221144", "mdp") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            if (service.authentifierIntervenantTelephone("0788559944", "mdp") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            if (service.authentifierIntervenantTelephone("0722447744", "mdp") != null) {
                System.out.println("\u001b[34m" + "Authentification réussie");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            List<Matiere> listeMatieres = service.consulterListeMatieres();
            System.out.println("\u001b[34m" + "Liste des matières pour lesquelles vous pouvez demander un soutien :");
            for (Matiere matiere : listeMatieres) {
                System.out.println("\u001b[34m" + matiere.getNom());
            }

            //Persistance de Soutien (et au passage, de la matière du soutien)
            //En utilisant le nouveau service CreerSoutiens
            //Ce service permet de gérer la concurrence
            String descriptif = "J'ai besoin d'aide pour réviser avant mon DS ";

            Soutien soutien1 = service.demanderSoutien(eleve1, listeMatieres.get(1), descriptif);
            Soutien soutien2 = service.demanderSoutien(eleve2, listeMatieres.get(2), descriptif);
            Soutien soutien3 = service.demanderSoutien(eleve3, listeMatieres.get(3), descriptif);
            Soutien soutien4 = service.demanderSoutien(eleve4, listeMatieres.get(4), descriptif);

            //Remarque : Deux possibilités pour persister les soutiens en fonction de la vision qu'on veut adopter 
            //Possibilité 1 : Plusieurs élèves peuvent demander un soutien en même temps
            //Dans ce cas, on enregistre la demande, et on appelle creerSoutiens 
            //on regarde s'il y a assez d'intervenant
            //Car dans le cas contraire, on choisit d'accorder le soutiens aux élèves des classes supérieures 
            //(codées ici comme les classes inférieures : 0,1,2...)
            //Et on retourne la liste des soutiens créée
            //Puis on extrait les soutiens de cette liste
            //C'est l'hypothèse adoptée ci-dessus --> gestion de la concurrence
            //Possibilité 2 : 
            //Pas de concurrence possible : dès qu'un élève demande un soutien, son soutien est créé
            //s'il y a un intervenant disponible
            //Dans ce cas, il suffit de créer le soutien en appelant creerSoutien
            //Ce cas sera démontré plus loin
            //Lancement des visios pour les soutien existant
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

            System.out.println("\u001b[31m" + "======================");

            System.out.println("\u001b[33m" + "=====TABLEAU DE BORD ELEVE=====");

            List<Soutien> historique1 = service.trouverHistoriqueEleve(eleve1);
            System.out.println("\u001b[33m" + "Historique eleve1 : " + historique1);
            StringBuilder sb1 = new StringBuilder();
            for (Soutien soutien : historique1) {
                sb1.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }

            List<Soutien> historique2 = service.trouverHistoriqueEleve(eleve2);
            System.out.println("\u001b[33m" + "Historique eleve1 : " + historique2);
            StringBuilder sb2 = new StringBuilder();
            for (Soutien soutien : historique2) {
                sb2.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }

            List<Soutien> historique3 = service.trouverHistoriqueEleve(eleve3);
            System.out.println("\u001b[33m" + "Historique eleve1 : " + historique3);
            StringBuilder sb3 = new StringBuilder();
            for (Soutien soutien : historique3) {
                sb3.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }

            List<Soutien> historique4 = service.trouverHistoriqueEleve(eleve4);
            System.out.println("\u001b[33m" + "Historique eleve1 : " + historique4);
            StringBuilder sb4 = new StringBuilder();
            for (Soutien soutien : historique4) {
                sb4.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }

            System.out.println("\u001b[33m" + "Historique eleve1 : " + sb1);
            System.out.println("\u001b[33m" + "Historique eleve2 : " + sb2);
            System.out.println("\u001b[33m" + "Historique eleve3 : " + sb3);
            System.out.println("\u001b[33m" + "Historique eleve4 : " + sb4);

            //Ajout d'un soutien pour l'élève3 avec creerSoutien
            Soutien soutien6 = service.demanderSoutien(eleve3, listeMatieres.get(2), descriptif);

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

            System.out.println("\u001b[31m" + "======================");

            System.out.println("\u001b[33m" + "=====Historique ELEVE=====");

            List<Soutien> historique5 = service.trouverHistoriqueEleve(eleve1);
            System.out.println("\u001b[33m" + "Historique eleve3 : " + historique5);
            StringBuilder sb5 = new StringBuilder();
            for (Soutien soutien : historique1) {
                sb5.append(String.format("Date: %s, Intervenant: %s %s, Matière: %s, Bilan: %s ; ",
                        soutien.getDate(),
                        soutien.getIntervenant().getPrenom(),
                        soutien.getIntervenant().getNom(),
                        soutien.getMatiere().getNom(),
                        soutien.getBilanIntervenant()));
            }
            System.out.println("\u001b[35m" + "Historique intervenant : " + sb5);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void scenarioConflit1() throws InterruptedException {
        Service service = new Service();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Scanner scanner = new Scanner(System.in);
        try {
            //initialisation des intervenant et des matieres.
            service.initialisation();
            //Inscription d'élèves
            Date date1 = sdf.parse("27/03/2011");
            Eleve eleve1 = new Eleve("Pascal", "Alice", "alice.pascal@free.fr", "123456", date1, 6);
            service.inscrireEleve(eleve1, "0691664J");

            Date date2 = sdf.parse("27/03/2008");
            Eleve eleve2 = new Eleve("Jean", "François", "jeanfrancois@free.fr", "987654", date2, 0);
            service.inscrireEleve(eleve2, "0691664J");

            Date date3 = sdf.parse("28/03/2010");
            Eleve eleve3 = new Eleve("Paul", "Paul", "ppaul@free.fr", "123456", date3, 0);
            service.inscrireEleve(eleve3, "0691478G");

            Date date4 = sdf.parse("28/03/2010");
            Eleve eleve4 = new Eleve("Michel", "Arthur", "marthur@free.fr", "123456", date4, 4);
            service.inscrireEleve(eleve4, "0691478G");

            Date date5 = sdf.parse("28/03/2010");
            Eleve eleve5 = new Eleve("Ninon", "Mathilde", "nmathilde@free.fr", "123456", date5, 0);
            service.inscrireEleve(eleve5, "0691478G");

            //Authentification d'élèves
            Eleve eleveLogIn1 = service.authentifierEleveMail("alice.pascal@free.fr", "123456");
            if (eleveLogIn1 != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            Eleve eleveLogIn2 = service.authentifierEleveMail("jeanfrancois@free.fr", "987654");
            if (eleveLogIn2 != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            Eleve eleveLogIn3 = service.authentifierEleveMail("ppaul@free.fr", "123456");
            if (eleveLogIn3 != null) {
                System.out.println("\u001b[34m" + "Authentification réussie"
                        + ". Accès a la page d'accueil (Demander un Soutien).");
            } else {
                System.out.println("\u001b[34m" + "Erreur d'authentification");
            }

            Soutien soutien1 = service.demanderSoutien(eleveLogIn1, service.consulterListeMatieres().get(1), "Je n'ai pas compris le cours");
            Soutien soutien2 = service.demanderSoutien(eleveLogIn2, service.consulterListeMatieres().get(4), "J'ai quelques difficultés");
            Soutien soutien3 = service.demanderSoutien(eleveLogIn3, service.consulterListeMatieres().get(3), "Aidez moi !");

            System.out.println(
                    service.lancerVisio(soutien1)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien1"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien1 (peut-être qu'il n'existe pas)"
            );
            System.out.println(
                    service.lancerVisio(soutien2)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien2"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien1 (peut-être qu'il n'existe pas)"
            );
            System.out.println(
                    service.lancerVisio(soutien3)
                    ? "\u001b[32m" + "Lancement de la visio pour le soutien3"
                    : "\u001b[31m" + "La visio ne peut être lancée pour le soutien1 (peut-être qu'il n'existe pas)"
            );
            Thread.sleep(10000);

            //Fin des visios pour les soutiens existant
            System.out.println(
                    service.terminerVisio(soutien1)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien2"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien1 (peut-être qu'il n'existe pas)"
            );

            System.out.println(
                    service.terminerVisio(soutien2)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien2"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien1 (peut-être qu'il n'existe pas)"
            );
            System.out.println(
                    service.terminerVisio(soutien2)
                    ? "\u001b[32m" + "Fin de la visio pour le soutien2"
                    : "\u001b[31m" + "La visio ne peut être arrêtée pour le soutien1 (peut-être qu'il n'existe pas)"
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

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
