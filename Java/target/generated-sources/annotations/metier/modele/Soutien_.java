package metier.modele;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import metier.modele.Eleve;
import metier.modele.Intervenant;
import metier.modele.Matiere;
import metier.modele.Soutien.EtatSoutien;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-03-31T16:53:08")
@StaticMetamodel(Soutien.class)
public class Soutien_ { 

    public static volatile SingularAttribute<Soutien, Date> date;
    public static volatile SingularAttribute<Soutien, Integer> evaluationEleve;
    public static volatile SingularAttribute<Soutien, String> bilanIntervenant;
    public static volatile SingularAttribute<Soutien, Long> duree;
    public static volatile SingularAttribute<Soutien, Long> id;
    public static volatile SingularAttribute<Soutien, Eleve> eleve;
    public static volatile SingularAttribute<Soutien, EtatSoutien> etat;
    public static volatile SingularAttribute<Soutien, String> descriptif;
    public static volatile SingularAttribute<Soutien, Intervenant> intervenant;
    public static volatile SingularAttribute<Soutien, Matiere> matiere;

}