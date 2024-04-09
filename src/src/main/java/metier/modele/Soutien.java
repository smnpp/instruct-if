package metier.modele;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import javax.persistence.ManyToOne;

@Entity
public class Soutien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private EtatSoutien etat;
    private String descriptif;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date; // date de lancement de la visio
    private Long duree; // en minutes
    private Double autoevaluationEleve;
    private String bilanIntervenant;
    @ManyToOne
    private Matiere matiere;
    @ManyToOne
    private Eleve eleve;
    @ManyToOne
    private Intervenant intervenant;

    public Soutien() {
        // Par défaut, un soutien est en attente et a pour date la date actuelle
        this.etat = EtatSoutien.EN_ATTENTE;
    }

    // Enumération des différents états possibles d'un soutien
    public enum EtatSoutien {
        EN_ATTENTE,
        EN_VISIO,
        TERMINE
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public EtatSoutien getEtat() {
        return etat;
    }

    public void setEtat(EtatSoutien etat) {
        this.etat = etat;
    }

    public String getDescriptif() {
        return descriptif;
    }

    public void setDescriptif(String descriptif) {
        this.descriptif = descriptif;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getDuree() {
        return duree;
    }

    public void setDuree(Long duree) {
        this.duree = duree;
    }

    public Double getAutoevaluationEleve() {
        return autoevaluationEleve;
    }

    public void setAutoevaluationEleve(Double autoevaluationEleve) {
        this.autoevaluationEleve = autoevaluationEleve;
    }

    public String getBilanIntervenant() {
        return bilanIntervenant;
    }

    public void setBilanIntervenant(String bilanIntervenant) {
        this.bilanIntervenant = bilanIntervenant;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Intervenant getIntervenant() {
        return intervenant;
    }

    public void setIntervenant(Intervenant intervenant) {
        this.intervenant = intervenant;
    }

    @Override
    public String toString() {
        return "Soutien{" + "Date : " + date + ", Intervenant=" + intervenant.getPrenom() + " "
                + intervenant.getNom() + ", Matiere : " + matiere.getNom() + ", Bilan : "
                + bilanIntervenant + '}';
    }

}
