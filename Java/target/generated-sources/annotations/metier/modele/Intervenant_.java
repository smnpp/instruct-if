package metier.modele;

import java.util.List;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-03-31T16:53:08")
@StaticMetamodel(Intervenant.class)
public class Intervenant_ { 

    public static volatile SingularAttribute<Intervenant, String> motDePasse;
    public static volatile SingularAttribute<Intervenant, Boolean> disponibilite;
    public static volatile SingularAttribute<Intervenant, Integer> nbIntervention;
    public static volatile SingularAttribute<Intervenant, String> telephone;
    public static volatile SingularAttribute<Intervenant, Long> id;
    public static volatile SingularAttribute<Intervenant, String> nom;
    public static volatile SingularAttribute<Intervenant, String> prenom;
    public static volatile SingularAttribute<Intervenant, List> niveau;

}