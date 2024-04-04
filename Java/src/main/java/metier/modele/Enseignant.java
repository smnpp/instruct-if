package metier.modele;

import javax.persistence.Entity;

@Entity
public class Enseignant extends Intervenant {

    private String typeEtablissement;

    public Enseignant() {
        // Default constructor
    }

    public Enseignant(String nom, String prenom, String telephone, String mdp, Integer niveauMin, Integer niveauMax, String typeEtablissement) {
        super(nom, prenom, telephone, mdp, niveauMin, niveauMax);
        this.typeEtablissement = typeEtablissement;
    }

    public String getTypeEtablissement() {
        return typeEtablissement;
    }

    public void setTypeEtablissement(String typeEtablissement) {
        this.typeEtablissement = typeEtablissement;
    }

    @Override
    public String toString() {
        return "Enseignant{" + super.toString() + ", typeEtablissement=" + typeEtablissement + '}';
    }

}
