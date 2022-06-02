package com.dummy.myerp.business.impl.manager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dummy.myerp.business.config.BusinessContextBeans;
import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.ConsumerHelper;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;

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

        @BeforeEach
        void init() {

                objectToTest = new ComptabiliteManagerImpl();
                ComptabiliteManagerImpl.configure(businessProxy, daoProxy, transactionManager);

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
