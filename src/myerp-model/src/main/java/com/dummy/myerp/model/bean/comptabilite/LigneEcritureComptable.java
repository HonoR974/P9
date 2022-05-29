package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.dummy.myerp.model.validation.constraint.MontantComptable;

/**
 * Bean représentant une Ligne d'écriture comptable.
 */
public class LigneEcritureComptable {

    // ==================== Attributs ====================
    /** Compte Comptable */
    @NotNull
    private CompteComptable compteComptable;

    /** The Libelle. */
    @Size(max = 200)
    private String libelle;

    /** The Debit. */
    @MontantComptable
    private BigDecimal debit;

    /** The Credit. */
    @MontantComptable
    private BigDecimal credit;

    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Ligne ecriture comptable.
     */
    public LigneEcritureComptable() {
    }

    /**
     * Instantiates a new Ligne ecriture comptable.
     *
     * @param pCompteComptable the Compte Comptable
     * @param pLibelle         the libelle
     * @param pDebit           the debit
     * @param pCredit          the credit
     */
    public LigneEcritureComptable(CompteComptable pCompteComptable, String pLibelle,
            BigDecimal pDebit, BigDecimal pCredit) {
        compteComptable = pCompteComptable;
        libelle = pLibelle;
        debit = pDebit;
        credit = pCredit;
    }

    /**
     * @return CompteComptable
     */
    // ==================== Getters/Setters ====================
    public CompteComptable getCompteComptable() {
        return compteComptable;
    }

    /**
     * @param pCompteComptable
     */
    public void setCompteComptable(CompteComptable pCompteComptable) {
        compteComptable = pCompteComptable;
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
     * @return BigDecimal
     */
    public BigDecimal getDebit() {
        return debit;
    }

    /**
     * @param pDebit
     */
    public void setDebit(BigDecimal pDebit) {
        debit = pDebit;
    }

    /**
     * @return BigDecimal
     */
    public BigDecimal getCredit() {
        return credit;
    }

    /**
     * @param pCredit
     */
    public void setCredit(BigDecimal pCredit) {
        credit = pCredit;
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
                .append("compteComptable=").append(compteComptable)
                .append(vSEP).append("libelle='").append(libelle).append('\'')
                .append(vSEP).append("debit=").append(debit)
                .append(vSEP).append("credit=").append(credit)
                .append("}");
        return vStB.toString();
    }
}
