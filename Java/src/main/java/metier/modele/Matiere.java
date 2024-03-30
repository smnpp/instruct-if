package metier.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Matiere implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String libelle;

    // On garde la collection des matières valides, mais cela ne sera pas stocké en base
    // Il s'agit juste d'une aide au niveau du modèle pour vérifier les valeurs valides
    private static final Set<String> MATIERES_VALIDES;

    static {
        Set<String> aSet = new HashSet<>();
        aSet.add("Mathématiques");
        aSet.add("Physique-Chimie");
        aSet.add("SVT");
        aSet.add("Histoire-Géographie");
        aSet.add("Français");
        aSet.add("Anglais");
        aSet.add("Espagnol");
        aSet.add("Philosophie");
        MATIERES_VALIDES = Collections.unmodifiableSet(aSet);
    }

    public Matiere() {}

    public Matiere(String libelle) throws IllegalArgumentException {
        if (!MATIERES_VALIDES.contains(libelle)) {
            throw new IllegalArgumentException("Libellé de matière non valide.");
        }
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        if (!MATIERES_VALIDES.contains(libelle)) {
            throw new IllegalArgumentException("Libellé de matière non valide.");
        }
        this.libelle = libelle;
    }

    public static List<Matiere> obtenirMatieresValides() {
        List<Matiere> matieres = new ArrayList<>();
        for (String libelle : MATIERES_VALIDES) {
            matieres.add(new Matiere(libelle));
        }
        return matieres;
    }

    // Méthodes toString, equals, et hashCode si nécessaire

}
