package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.business.util.Constant;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplTest {

        @Mock
        private BusinessProxy businessProxy;

        @Mock
        private DaoProxy daoProxy;

        @Mock
        private TransactionManager transactionManager;

        @Mock
        private ComptabiliteDao comptabiliteDao;

        private ComptabiliteManagerImpl objectToTest;

        private EcritureComptable sampleEcritureComptable;

        @Before
        public void init() {

                objectToTest = new ComptabiliteManagerImpl();

                ComptabiliteManagerImpl.configure(businessProxy, daoProxy,
                                transactionManager);

                sampleEcritureComptable = new EcritureComptable();
                sampleEcritureComptable.setId(1);
                sampleEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
                sampleEcritureComptable.setDate(new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime());
                sampleEcritureComptable.setLibelle("Libelle");
                sampleEcritureComptable.setReference("AC-2020/00001");
                sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                null, new BigDecimal(123),
                                null));
                sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                null, null,
                                new BigDecimal(123)));
        }

        @After
        public void reset() {
                Mockito.reset(daoProxy);
                Mockito.reset(comptabiliteDao);
                Mockito.reset(transactionManager);
        }

        @Test
        public void getListCompteComptable() {

                // given
                List<CompteComptable> compteComptables = new ArrayList<>();
                compteComptables.add(new CompteComptable(1));
                compteComptables.add(new CompteComptable(2));

                Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
                Mockito.when(comptabiliteDao.getListCompteComptable()).thenReturn(compteComptables);

                // when
                List<CompteComptable> result = objectToTest.getListCompteComptable();

                // then
                Assertions.assertThat(result).isEqualTo(compteComptables);
                Mockito.verify(comptabiliteDao).getListCompteComptable();

        }

        @Test
        public void getListJournalComptable() {
                // given
                List<JournalComptable> journalComptables = new ArrayList<>();
                journalComptables.add(new JournalComptable("1", "journal-1"));
                journalComptables.add(new JournalComptable("2", "journal-2"));

                Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
                Mockito.when(comptabiliteDao.getListJournalComptable()).thenReturn(journalComptables);

                // when
                List<JournalComptable> result = objectToTest.getListJournalComptable();

                // then
                Assertions.assertThat(result).isEqualTo(journalComptables);
                Mockito.verify(comptabiliteDao).getListJournalComptable();

        }

        @Test
        public void getListEcritureComptable() {
                // given
                List<EcritureComptable> ecritureComptables = new ArrayList<>();
                ecritureComptables.add(new EcritureComptable());
                ecritureComptables.add(new EcritureComptable());

                Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
                Mockito.when(comptabiliteDao.getListEcritureComptable()).thenReturn(ecritureComptables);

                // when
                List<EcritureComptable> result = objectToTest.getListEcritureComptable();

                // then
                Assertions.assertThat(result).isEqualTo(ecritureComptables);
                Mockito.verify(comptabiliteDao).getListEcritureComptable();
        }

        /*-_
         * shouldThrowFunctionalException_whenEcritureComptableJournalCodeIsNull
         */

        @Test
        public void addReference_shouldcreateNewSequenceInDB_whenSequenceNotFound()
                        throws NotFoundException, FunctionalException {
                //
                LocalDate ecritureDate = sampleEcritureComptable.getDate()
                                .toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate();

                String expectedRef = sampleEcritureComptable.getReference();

                sampleEcritureComptable.setReference("");

                Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
                Mockito.when(comptabiliteDao.getSequenceByYearAndJournalCode(ecritureDate.getYear(),
                                sampleEcritureComptable.getJournal().getCode())).thenThrow(NotFoundException.class);

                objectToTest.addReference(sampleEcritureComptable);

                Mockito.verify(comptabiliteDao).insertNewSequence(Mockito.any(SequenceEcritureComptable.class));
                Assertions.assertThat(sampleEcritureComptable.getReference()).isEqualTo(expectedRef);
        }

        @Test
        public void addReference_noDate_shouldFunctionalException() throws NotFoundException, FunctionalException {

                sampleEcritureComptable.setDate(null);

                Assertions.assertThatThrownBy(() -> objectToTest.addReference(sampleEcritureComptable))
                                .isInstanceOf(FunctionalException.class)
                                .hasMessageContaining(Constant.ECRITURE_COMPTABLE_DATE_NULL_FOR_ADD_REFERENCE);

        }

        @Test
        public void addReference_noJournal_shouldFunctionalException() throws NotFoundException, FunctionalException {
                sampleEcritureComptable.setJournal(null);

                Assertions.assertThatThrownBy(() -> objectToTest.addReference(sampleEcritureComptable))
                                .isInstanceOf(FunctionalException.class)
                                .hasMessageContaining(Constant.ECRITURE_COMPTABLE_JOURNAL_NULL_FOR_ADD_REFERENCE);

        }

        @Test
        public void addReference_shouldUpdateSequenceDerniereValeurInDBAndConstructRef_whenSequenceFound()
                        throws NotFoundException, FunctionalException {

                String expectedRef = "AC-2020/00006";

                SequenceEcritureComptable sequenceEcritureComptableFound = new SequenceEcritureComptable(2020,
                                new JournalComptable("AC", "Achat"),
                                5);

                LocalDate ecritureDate = sampleEcritureComptable.getDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                sampleEcritureComptable.setReference("");

                Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
                Mockito.when(comptabiliteDao.getSequenceByYearAndJournalCode(ecritureDate.getYear(),
                                sampleEcritureComptable.getJournal().getCode()))
                                .thenReturn(sequenceEcritureComptableFound);

                objectToTest.addReference(sampleEcritureComptable);

                Mockito.verify(comptabiliteDao).updateSequenceEcritureComptable(sequenceEcritureComptableFound);
                Assertions.assertThat(sampleEcritureComptable.getReference()).isEqualTo(expectedRef);
        }

        @Test
        public void addReference_shouldcreateNewSequenceInDBAndConstructRef_whenSequenceNotFound()
                        throws NotFoundException, FunctionalException {

                LocalDate ecritureDate = sampleEcritureComptable.getDate()
                                .toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate();

                String expectedRef = sampleEcritureComptable.getReference();

                sampleEcritureComptable.setReference("");

                Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
                Mockito.when(comptabiliteDao.getSequenceByYearAndJournalCode(ecritureDate.getYear(),
                                sampleEcritureComptable.getJournal().getCode())).thenThrow(NotFoundException.class);

                objectToTest.addReference(sampleEcritureComptable);

                Mockito.verify(comptabiliteDao).insertNewSequence(Mockito.any(SequenceEcritureComptable.class));
                Assertions.assertThat(sampleEcritureComptable.getReference()).isEqualTo(expectedRef);

        }

        /**
         * Verifie une ecriture comptable correcte
         * ne doit pas retourner d'expception
         * 
         * @throws Exception
         */
        @Test
        public void checkEcritureComptable_correctNewEcritureComptable_shouldNotThrowException() throws Exception {

                Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
                Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference()))
                                .thenThrow(new NotFoundException());

                Assertions.assertThatCode(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                                .doesNotThrowAnyException();

        }

        /**
         * verifie une ecritre comptable
         * qui n'a pas de date
         * retourne functionalException avec constraint message
         * 
         * @throws Exception
         */
        @Test
        public void checkEcritureComptable_badConstraint_shouldFunctionalExceptionConstraintMessage()
                        throws Exception {

                sampleEcritureComptable.setDate(null);

                Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
                Mockito.when(comptabiliteDao.getEcritureComptableByRef(sampleEcritureComptable.getReference()))
                                .thenThrow(new NotFoundException());

                Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                                .isInstanceOf(FunctionalException.class)
                                .hasMessageContaining(Constant.ECRITURE_COMPTABLE_MANAGEMENT_RULE);
        }

        /**
         * RG 2
         * verifie si l'ecriture comptable est equilibré
         * la somme des montants au crédit des lignes d'écriture doit être égale à la
         * somme des montants au débit.
         * 
         * @throws Exception
         */
        @Test
        public void checkEcritureComptable_RG2_unbalanced_shouldFunctionalExceptionRG2Message() throws Exception {

                sampleEcritureComptable.getListLigneEcriture().clear();

                sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                null, new BigDecimal(123), null));

                sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                null, null, new BigDecimal(1234)));

                Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                                .isInstanceOf(FunctionalException.class)
                                .hasMessageContaining(Constant.RG_COMPTA_2_VIOLATION);
        }

        /**
         * RG 3
         * verifie si l'ecriture comptable a au moins
         * 2 ligne d'ecriture (1 au débit, 1 au crédit)
         * 
         * @throws Exception
         */
        @Test
        public void checkEcritureComptable_RG3_noCredit_shouldFunctionalExceptionRG3Message() throws Exception {

                sampleEcritureComptable.getListLigneEcriture().clear();

                sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                null, new BigDecimal(123), null));

                sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                null, new BigDecimal(123), null));

                Assertions.assertThatThrownBy(() -> objectToTest.checkEcritureComptable(sampleEcritureComptable))
                                .isInstanceOf(FunctionalException.class)
                                .hasMessageContaining(Constant.RG_COMPTA_3_VIOLATION);

        }

        /**
         * RG 5
         * vérifier que l'année dans la référence correspond bien à la date de
         * l'écriture, idem pour le code journal...
         * 
         */
        @Test
        public void checkEcritureComptable_RG5_() {

                /*
                 * Assertions.assertThatThrownBy(() ->
                 * objectToTest.checkEcritureComptable(sampleEcritureComptable))
                 * .isInstanceOf(FunctionalException.class)
                 * .hasMessageContaining(Constant.RG_COMPTA_5_VIOLATION);
                 */

        }

}
