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
    private Date date;
    private Long duree; // en minutes
    private Integer evaluationEleve;
    private String bilanIntervenant;
    @ManyToOne
    private Matiere matiere ;
    @ManyToOne
    private Eleve eleve ;
    @ManyToOne
    private Intervenant intervenant;
    

    public Soutien() {
        // Par défaut, un soutien est en attente et a pour date la date actuelle
        this.etat = EtatSoutien.EN_ATTENTE;
        this.date = new Date();
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

    public void setId(Long id) {
        this.id = id;
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

    public Integer getEvaluationEleve() {
        return evaluationEleve;
    }

    public void setEvaluationEleve(Integer evaluationEleve) {
        this.evaluationEleve = evaluationEleve;
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
    
    

    // Méthode toString pour l'affichage
    @Override
    public String toString() {
        return "Soutien{" +
                "id=" + id +
                ", etat=" + etat +
                ", descriptif='" + descriptif + '\'' +
                ", date=" + date +
                ", duree=" + duree +
                ", evaluation eleve=" + evaluationEleve +
                ", bilan intervenant='" + bilanIntervenant + '\'' +
                '}';
    }
}
