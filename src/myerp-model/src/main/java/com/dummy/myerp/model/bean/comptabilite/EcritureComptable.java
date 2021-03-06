package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Bean représentant une Écriture Comptable
 */
public class EcritureComptable {

    // ==================== Attributs ====================
    /** The Id. */
    private Integer id;
    /** Journal comptable */
    @NotNull
    private JournalComptable journal;
    /** The Reference. */
    @Pattern(regexp = "^[A-Za-z]{2}-\\d{4}/\\d{5}$")
    private String reference;
    /** The Date. */
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /** The Libelle. */
    @NotNull
    @Size(min = 1, max = 200)
    private String libelle;

    /** La liste des lignes d'écriture comptable. */
    @Valid
    @Size(min = 2)
    private final List<LigneEcritureComptable> listLigneEcriture = new ArrayList<>();

    /**
     * @return Integer
     */
    // ==================== Getters/Setters ====================
    public Integer getId() {
        return id;
    }

    /**
     * @param pId
     */
    public void setId(Integer pId) {
        id = pId;
    }

    /**
     * @return JournalComptable
     */
    public JournalComptable getJournal() {
        return journal;
    }

    /**
     * @param pJournal
     */
    public void setJournal(JournalComptable pJournal) {
        journal = pJournal;
    }

    /**
     * @return String
     */
    public String getReference() {
        return reference;
    }

    /**
     * @param pReference
     */
    public void setReference(String pReference) {
        reference = pReference;
    }

    /**
     * @return Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param pDate
     */
    public void setDate(Date pDate) {
        date = pDate;
    }

    /**
     * @return String
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @param pLibelle
     */
    public void setLibelle(String pLibelle) {
        libelle = pLibelle;
    }

    /**
     * @return List<LigneEcritureComptable>
     */
    public List<LigneEcritureComptable> getListLigneEcriture() {
        return listLigneEcriture;
    }

    /**
     * Calcul et renvoie le total des montants au débit
     * des lignes d'écriture
     *
     * @return {@link BigDecimal}, {@link BigDecimal#ZERO} si aucun montant au débit
     */
    // TODO à tester
    public BigDecimal getTotalDebit() {
        BigDecimal vRetour = BigDecimal.ZERO;
        for (LigneEcritureComptable vLigneEcritureComptable : listLigneEcriture) {
            if (vLigneEcritureComptable.getDebit() != null) {
                vRetour = vRetour.add(vLigneEcritureComptable.getDebit());
            }
        }
        return vRetour;
    }

    /**
     * Calcul et renvoie le total des montants au crédit
     * des lignes d'écriture
     *
     * @return {@link BigDecimal}, {@link BigDecimal#ZERO} si aucun montant au
     *         crédit
     */
    public BigDecimal getTotalCredit() {
        BigDecimal vRetour = BigDecimal.ZERO;
        for (LigneEcritureComptable vLigneEcritureComptable : listLigneEcriture) {
            if (vLigneEcritureComptable.getCredit() != null) {
                vRetour = vRetour.add(vLigneEcritureComptable.getCredit());
            }

        }
        return vRetour;
    }

    /**
     * Renvoie si l'écriture est équilibrée (TotalDebit = TotalCrédit)
     * 
     * @return boolean
     */
    public boolean isEquilibree() {

        return getTotalDebit().equals(getTotalCredit());
    }

    /**
     * @return String
     */
    // ==================== Méthodes ====================
    @Override
    public String toString() {
        final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
        final String vSEP = ", ";
        vStB.append("{")
                .append("id=").append(id)
                .append(vSEP).append("journal=").append(journal)
                .append(vSEP).append("reference='").append(reference).append('\'')
                .append(vSEP).append("date=").append(date)
                .append(vSEP).append("libelle='").append(libelle).append('\'')
                .append(vSEP).append("totalDebit=").append(this.getTotalDebit().toPlainString())
                .append(vSEP).append("totalCredit=").append(this.getTotalCredit().toPlainString())
                .append(vSEP).append("listLigneEcriture=[\n")
                .append(StringUtils.join(listLigneEcriture, "\n")).append("\n]")
                .append("}");
        return vStB.toString();
    }
}
