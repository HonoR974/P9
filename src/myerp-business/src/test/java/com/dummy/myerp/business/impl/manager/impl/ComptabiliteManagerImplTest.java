package com.dummy.myerp.business.impl.manager.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import java.util.Date;

import com.dummy.myerp.business.config.BusinessContextBeans;
import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.business.util.Constant;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { BusinessContextBeans.class })
public class ComptabiliteManagerImplTest {

        @Autowired
        private BusinessProxy businessProxy;

        @Autowired
        private DaoProxy daoProxy;

        @Autowired
        private TransactionManager transactionManager;

        @Autowired
        private ComptabiliteDao comptabiliteDao;

        private ComptabiliteManagerImpl objectToTest;

        private EcritureComptable sampleEcritureComptable;

        @BeforeEach
        void init() {

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

        @AfterEach
        void reset() {
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

        @Test
        void addReference() {
                /*-_shouldcreateNewSequenceInDB_whenSequenceNotFound 
                 * shouldThrowFunctionalException_whenEcritureComptableDateIsNull
                 * shouldThrowFunctionalException_whenEcritureComptableJournalIsNull
                 * 
                 * shouldcreateNewSequenceInDBAndConstructRef_whenSequenceNotFound
                 * shouldThrowFunctionalException_whenEcritureComptableJournalCodeIsNull
                 */
        }

        @Test
        void addReference_shouldcreateNewSequenceInDB_whenSequenceNotFound()
                        throws NotFoundException, FunctionalException {
                //
                LocalDate ecritureDate = sampleEcritureComptable.getDate().toInstant().atZone(ZoneId.systemDefault())
                                .toLocalDate();

                System.out.println("\n ecritudeDate" + ecritureDate.getChronology());

                String expectedRef = sampleEcritureComptable.getReference();
                System.out.println("\n exceptected ref " + expectedRef);

                sampleEcritureComptable.setReference("");

                Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
                Mockito.when(comptabiliteDao.getSequenceByYearAndJournalCode(ecritureDate.getYear(),
                                sampleEcritureComptable.getJournal().getCode())).thenThrow(NotFoundException.class);

                //
                System.out.println("\n sample ecriture " + sampleEcritureComptable.toString());
                objectToTest.addReference(sampleEcritureComptable);

                //
                Mockito.verify(comptabiliteDao).insertNewSequence(Mockito.any(SequenceEcritureComptable.class));
                Assertions.assertThat(sampleEcritureComptable.getReference()).isEqualTo(expectedRef);
        }

        /**
         * @throws Exception
         */
        @Test
        public void checkEcritureComptableUnit() throws Exception {
                EcritureComptable vEcritureComptable;
                vEcritureComptable = new EcritureComptable();
                vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
                vEcritureComptable.setDate(new Date());
                vEcritureComptable.setLibelle("Libelle");
                vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                null, new BigDecimal(123),
                                null));
                vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                null, null,
                                new BigDecimal(123)));
                System.out.println("\n test");

        }

        /**
         * @throws Exception
         */
        @Test
        public void checkEcritureComptableUnitViolation() throws Exception {
                EcritureComptable vEcritureComptable;
                vEcritureComptable = new EcritureComptable();

        }

        /**
         * @throws Exception
         */
        @Test
        public void checkEcritureComptableUnitRG2() throws Exception {
                EcritureComptable vEcritureComptable;
                vEcritureComptable = new EcritureComptable();
                vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
                vEcritureComptable.setDate(new Date());
                vEcritureComptable.setLibelle("Libelle");
                vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                null, new BigDecimal(123),
                                null));
                vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                null, null,
                                new BigDecimal(1234)));

        }

        /**
         * @throws Exception
         */
        @Test
        public void checkEcritureComptableUnitRG3() throws Exception {
                EcritureComptable vEcritureComptable;
                vEcritureComptable = new EcritureComptable();
                vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
                vEcritureComptable.setDate(new Date());
                vEcritureComptable.setLibelle("Libelle");
                vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                null, new BigDecimal(123),
                                null));
                vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                null, new BigDecimal(123),
                                null));

        }

}
