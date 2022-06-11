package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.util.Constant;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================

    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
        super();
    }

    /**
     * @return List<CompteComptable>
     */
    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }

    /**
     * @return List<JournalComptable>
     */
    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    /**
     * {@inheritDoc}
     * * Le principe :
     * 1. Remonter depuis la persitance la dernière valeur de la séquence du journal
     * pour l'année de l'écriture
     * (table sequence_ecriture_comptable)
     * 2. * S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
     * 1. Utiliser le numéro 1.
     * Sinon :
     * 1. Utiliser la dernière valeur + 1
     * 3. Mettre à jour la référence de l'écriture avec la référence calculée
     * (RG_Compta_5)
     * 4. Enregistrer (insert/update) la valeur de la séquence en persitance
     * (table sequence_ecriture_comptable)
     * 
     * @throws FunctionalException
     */
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable)
            throws FunctionalException, NotFoundException {

        System.out.println("\n ecriture " + pEcritureComptable.toString());

        // verifie la date et le journal
        if (pEcritureComptable.getDate() == null) {
            throw new FunctionalException(Constant.ECRITURE_COMPTABLE_DATE_NULL_FOR_ADD_REFERENCE);
        }
        if (pEcritureComptable.getJournal() == null || pEcritureComptable.getJournal().getCode() == null) {
            throw new FunctionalException(Constant.ECRITURE_COMPTABLE_JOURNAL_NULL_FOR_ADD_REFERENCE);
        }

        // Remonter depuis la persitance la dernière valeur de la séquence du journal
        // pour l'année de l'écriture
        LocalDate ecritureDate = pEcritureComptable.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println("\n année du journal " + ecritureDate.toString());

        // 2
        // verifie si la sequence existe
        /// verifie la sequence d'ecriture entre le journal
        // et l'année de l'ecriture comptable
        // sequence ( (Integer year, String code )

        SequenceEcritureComptable sequenceEcritureComptable;
        boolean isSequenceAlreadyExist = true;

        try {

            // il y a une sequence d'ecriture de cette année par ce journal
            sequenceEcritureComptable = getDaoProxy().getComptabiliteDao()
                    .getSequenceByYearAndJournalCode(ecritureDate.getYear(), pEcritureComptable.getJournal().getCode());

            // ce journal n'a pas de sequence d'ecriture cette année
        } catch (NotFoundException e) {

            // new sequence
            sequenceEcritureComptable = new SequenceEcritureComptable(ecritureDate.getYear(),
                    pEcritureComptable.getJournal(), 0);
            isSequenceAlreadyExist = false;
        }

        sequenceEcritureComptable.setDerniereValeur(sequenceEcritureComptable.getDerniereValeur() + 1);

        // 3 Mettre à jour la référence de l'écriture avec la référence calculée
        // La référence d'une écriture comptable est composée du code du journal
        // dans lequel figure l'écriture suivi de l'année et d'un numéro de
        // séquence(propre à chaque journal )
        // (RG_Compta_5)

        // numero de sequence
        StringBuilder formattedSequenceNumberBuilder = new StringBuilder(
                sequenceEcritureComptable.getDerniereValeur().toString());
        System.out.println("\n while ");

        while (formattedSequenceNumberBuilder.length() < 5) {
            // insert ( index , contenu )
            formattedSequenceNumberBuilder.insert(0, "0");
            System.out.println("\n fomatedSequence " + formattedSequenceNumberBuilder.toString());
        }

        String formattedSequenceNumber = formattedSequenceNumberBuilder.toString();

        System.out.println("\n format string " + formattedSequenceNumber);

        //
        String reference = sequenceEcritureComptable.getJournal().getCode() + "-"
                + sequenceEcritureComptable.getAnnee().toString() + "/" + formattedSequenceNumber;
        pEcritureComptable.setReference(reference);

        System.out.println("\n reference ecriture comptable " + reference);

        // 4. Enregistrer (insert/update) la valeur de la séquence en persitance
        // (table sequence_ecriture_comptable)

        if (isSequenceAlreadyExist) {

            getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(sequenceEcritureComptable);
        } else {
            getDaoProxy().getComptabiliteDao().insertNewSequence(sequenceEcritureComptable);
        }

        pEcritureComptable.setReference(reference);

    }

    /**
     * {@inheritDoc}
     */
    // TODO à tester
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }

    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion unitaires,
     * c'est à dire indépendemment du contexte (unicité de la référence, exercie
     * comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
     *                             règles de gestion
     */
    // TODO tests à compléter
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable)
            throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);

        if (!vViolations.isEmpty()) {
            System.out.println("\n violation " + vViolations.toString());
            throw new FunctionalException(Constant.ECRITURE_COMPTABLE_MANAGEMENT_RULE,
                    new ConstraintViolationException(Constant.ECRITURE_COMPTABLE_VALIDATION_CONSTRAINT,
                            vViolations));
        }

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes
        // d'écriture (1 au débit, 1 au crédit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        // avec un montant au débit et un montant au crédit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
                || vNbrCredit < 1
                || vNbrDebit < 1) {
            throw new FunctionalException(Constant.RG_COMPTA_3_VIOLATION);
        }

        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit
        // être équilibrée

        System.out.println("\n pEcriture comptable desequilibré " + pEcritureComptable.toString());
        if (!pEcritureComptable.isEquilibree()) {
            System.out.println("\n desequilibré ");
            throw new FunctionalException(Constant.RG_COMPTA_2_VIOLATION);
        }

        // TODO ===== RG_Compta_5 : Format et contenu de la référence
        // vérifier que l'année dans la référence correspond bien à la date de
        // l'écriture, idem pour le code journal...
        String refJournalCode = pEcritureComptable.getReference().substring(0, 2);
        String refDateYear = pEcritureComptable.getReference().substring(3, 7);

        LocalDate ecritureDate = pEcritureComptable.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        System.out.println("\n ------------------------ ");

        System.out.println("\n ref " + pEcritureComptable.getReference());
        System.out.println("\n journal code " + pEcritureComptable.getJournal().getCode());
        System.out.println("\n ref journal " + refJournalCode);
        System.out.println("\n ref date  " + refDateYear);
        int ecritureDateYear = ecritureDate.getYear();

        if (!refJournalCode.equals(pEcritureComptable.getJournal().getCode())
                || !refDateYear.equals(Integer.toString(ecritureDateYear))) {
            throw new FunctionalException(Constant.RG_COMPTA_5_VIOLATION);
        }
    }

    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au
     * contexte
     * (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
     *                             règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {

        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                        pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                        || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException(Constant.RG_COMPTA_6_VIOLATION);
                }

            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la
                // même référence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
}
