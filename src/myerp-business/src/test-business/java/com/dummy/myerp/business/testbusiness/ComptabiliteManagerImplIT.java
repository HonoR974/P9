package com.dummy.myerp.business.testbusiness;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/com/dummy/myerp/business/testbusiness/testContext.xml" })
@WebAppConfiguration
public class ComptabiliteManagerImplIT  {

        private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
      

        @Test
        public void addReference() throws NotFoundException {

        }

        @Test
        public void getListCompteComptable() {
                System.out.println("\n le test est lancé ");
                assertTrue(manager.getListCompteComptable() != null);
        }
}