package com.dummy.myerp.business.impl.manager;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.consumer.ConsumerHelper;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;

@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplTest {

        @Mock
        ConsumerHelper consumerHelper;

        ComptabiliteManagerImpl manager;

        @Test
        public void getListCompteComptable(@Mock ConsumerHelper consumerHelper) {

                // given
                CompteComptable compte1 = new CompteComptable();
                CompteComptable compte2 = new CompteComptable();
                CompteComptable compte3 = new CompteComptable();

                List<CompteComptable> lComptables = new ArrayList<>();
                lComptables.add(compte1);
                lComptables.add(compte2);
                lComptables.add(compte3);

                when(ConsumerHelper.getDaoProxy().getComptabiliteDao()
                                .getListCompteComptable()).thenReturn(lComptables);

                // when
                System.out.println(" \n test when ");

                lComptables = manager.getListCompteComptable();

                // then
                System.out.println(" \n test then  ");

                assertEquals(lComptables.size(), 3);

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

                manager.checkEcritureComptableUnit(vEcritureComptable);

        }

        /**
         * @throws Exception
         */
        @Test(expected = FunctionalException.class)
        public void checkEcritureComptableUnitViolation() throws Exception {
                EcritureComptable vEcritureComptable;
                vEcritureComptable = new EcritureComptable();
                manager.checkEcritureComptableUnit(vEcritureComptable);
        }

        /**
         * @throws Exception
         */
        @Test(expected = FunctionalException.class)
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
                manager.checkEcritureComptableUnit(vEcritureComptable);
        }

        /**
         * @throws Exception
         */
        @Test(expected = FunctionalException.class)
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
                manager.checkEcritureComptableUnit(vEcritureComptable);
        }

}
