package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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

    // get EcritureComptable by ID
    @Override
    public EcritureComptable getEcritureComptableById(int id) throws NotFoundException {
        EcritureComptable ec = null;
        try {
            ec = getDaoProxy().getComptabiliteDao().getEcritureComptable(id);
        } catch (NotFoundException nfe) {
            throw new NotFoundException();
        }

        return ec;
    }

    /**
     * {@inheritDoc}
     * * Le principe :
     * 1. Remonter depuis la persitance la derni??re valeur de la s??quence du journal
     * pour l'ann??e de l'??criture
     * (table sequence_ecriture_comptable)
     * 2. * S'il n'y a aucun enregistrement pour le journal pour l'ann??e concern??e :
     * 1. Utiliser le num??ro 1.
     * Sinon :
     * 1. Utiliser la derni??re valeur + 1
     * 3. Mettre ?? jour la r??f??rence de l'??criture avec la r??f??rence calcul??e
     * (RG_Compta_5)
     * 4. Enregistrer (insert/update) la valeur de la s??quence en persitance
     * (table sequence_ecriture_comptable)
     * 
     * @throws FunctionalException
     */
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable)
            throws FunctionalException, NotFoundException {

        System.out.println("\n refto add " + pEcritureComptable.toString());
        // verifie la date et le journal
        if (pEcritureComptable.getDate() == null) {
            throw new FunctionalException(Constant.ECRITURE_COMPTABLE_DATE_NULL_FOR_ADD_REFERENCE);
        }
        if (pEcritureComptable.getJournal() == null || pEcritureComptable.getJournal().getCode() == null) {
            throw new FunctionalException(Constant.ECRITURE_COMPTABLE_JOURNAL_NULL_FOR_ADD_REFERENCE);
        }

        // Remonter depuis la persitance la derni??re valeur de la s??quence du journal
        // pour l'ann??e de l'??criture
        LocalDate ecritureDate = pEcritureComptable.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 2
        // verifie si la sequence existe
        /// verifie la sequence d'ecriture entre le journal
        // et l'ann??e de l'ecriture comptable
        // sequence ( (Integer year, String code )

        SequenceEcritureComptable sequenceEcritureComptable;
        boolean isSequenceAlreadyExist = true;

        try {

            // il y a une sequence d'ecriture de cette ann??e par ce journal
            sequenceEcritureComptable = getDaoProxy().getComptabiliteDao()
                    .getSequenceByYearAndJournalCode(ecritureDate.getYear(), pEcritureComptable.getJournal().getCode());

            // ce journal n'a pas de sequence d'ecriture cette ann??e
        } catch (NotFoundException e) {

            // new sequence
            sequenceEcritureComptable = new SequenceEcritureComptable(ecritureDate.getYear(), 0,
                    pEcritureComptable.getJournal().getCode());
            isSequenceAlreadyExist = false;
        }

        sequenceEcritureComptable.setDerniereValeur(sequenceEcritureComptable.getDerniereValeur() + 1);

        // 3 Mettre ?? jour la r??f??rence de l'??criture avec la r??f??rence calcul??e
        // La r??f??rence d'une ??criture comptable est compos??e du code du journal
        // dans lequel figure l'??criture suivi de l'ann??e et d'un num??ro de
        // s??quence(propre ?? chaque journal )
        // (RG_Compta_5)

        // numero de sequence
        StringBuilder formattedSequenceNumberBuilder = new StringBuilder(
                sequenceEcritureComptable.getDerniereValeur().toString());

        while (formattedSequenceNumberBuilder.length() < 5) {
            // insert ( index , contenu )
            formattedSequenceNumberBuilder.insert(0, "0");
        }

        String formattedSequenceNumber = formattedSequenceNumberBuilder.toString();

        //
        String reference = sequenceEcritureComptable.getCode() + "-"
                + sequenceEcritureComptable.getAnnee().toString() + "/" + formattedSequenceNumber;
        pEcritureComptable.setReference(reference);

        // 4. Enregistrer (insert/update) la valeur de la s??quence en persitance
        // (table sequence_ecriture_comptable)

        if (isSequenceAlreadyExist) {

            getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(sequenceEcritureComptable);
        } else {
            getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(sequenceEcritureComptable);
        }

        pEcritureComptable.setReference(reference);

    }

    /**
     * {@inheritDoc}
     */
    // TODO ?? tester
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }

    /**
     * V??rifie que l'Ecriture comptable respecte les r??gles de gestion unitaires,
     * c'est ?? dire ind??pendemment du contexte (unicit?? de la r??f??rence, exercie
     * comptable non clotur??...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
     *                             r??gles de gestion
     */
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable)
            throws FunctionalException {
        // ===== V??rification des contraintes unitaires sur les attributs de l'??criture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);

        if (!vViolations.isEmpty()) {
            throw new FunctionalException(Constant.ECRITURE_COMPTABLE_MANAGEMENT_RULE,
                    new ConstraintViolationException(Constant.ECRITURE_COMPTABLE_VALIDATION_CONSTRAINT,
                            vViolations));
        }

        // ===== RG_Compta_3 : une ??criture comptable doit avoir au moins 2 lignes
        // d'??criture (1 au d??bit, 1 au cr??dit)
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
        // On test le nombre de lignes car si l'??criture ?? une seule ligne
        // avec un montant au d??bit et un montant au cr??dit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
                || vNbrCredit < 1
                || vNbrDebit < 1) {
            throw new FunctionalException(Constant.RG_COMPTA_3_VIOLATION);
        }

        // ===== RG_Compta_2 : Pour qu'une ??criture comptable soit valide, elle doit
        // ??tre ??quilibr??e

        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException(Constant.RG_COMPTA_2_VIOLATION);
        }

        // TODO ===== RG_Compta_5 : Format et contenu de la r??f??rence
        // v??rifier que l'ann??e dans la r??f??rence correspond bien ?? la date de
        // l'??criture, idem pour le code journal...
        String refJournalCode = pEcritureComptable.getReference().substring(0, 2);
        String refDateYear = pEcritureComptable.getReference().substring(3, 7);

        LocalDate ecritureDate = pEcritureComptable.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        int ecritureDateYear = ecritureDate.getYear();

        if (!refJournalCode.equals(pEcritureComptable.getJournal().getCode())
                || !refDateYear.equals(Integer.toString(ecritureDateYear))) {
            throw new FunctionalException(Constant.RG_COMPTA_5_VIOLATION);
        }
    }

    /**
     * V??rifie que l'Ecriture comptable respecte les r??gles de gestion li??es au
     * contexte
     * (unicit?? de la r??f??rence, ann??e comptable non clotur??...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
     *                             r??gles de gestion
     */
    public void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {

        System.out.println("\n ecriture recu " + pEcritureComptable.toString());
        // ===== RG_Compta_6 : La r??f??rence d'une ??criture comptable doit ??tre unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une ??criture ayant la m??me r??f??rence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao()
                        .getEcritureComptableByRef(
                                pEcritureComptable.getReference());

                // Si l'??criture ?? v??rifier est une nouvelle ??criture (id == null),
                // ou si elle ne correspond pas ?? l'??criture trouv??e (id != idECRef),
                // c'est qu'il y a d??j?? une autre ??criture avec la m??me r??f??rence
                if (pEcritureComptable.getId() == null
                        || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException(Constant.RG_COMPTA_6_VIOLATION);
                }

            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ??a veut dire qu'on n'a aucune autre ??criture avec la
                // m??me r??f??rence.
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
