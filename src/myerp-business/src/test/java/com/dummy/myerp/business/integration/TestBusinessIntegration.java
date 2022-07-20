package com.dummy.myerp.business.integration;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bootstrapContext.xml")
@Transactional(propagation = Propagation.REQUIRED)
public class TestBusinessIntegration extends BusinessTestCase {

    private EcritureComptable sampleEcritureComptable;

    public TestBusinessIntegration() {
        super();
    }

    @Test
    public void getListCompteComptable() {
        List<CompteComptable> compteComptableList = getBusinessProxy().getComptabiliteManager()
                .getListCompteComptable();

        Assert.assertEquals(7, compteComptableList.size());
    }

    @Test
    public void getListJournalComptable() {
        List<JournalComptable> journalComptableList = getBusinessProxy().getComptabiliteManager()
                .getListJournalComptable();
        Assert.assertEquals(4, journalComptableList.size());
    }

    @Test
    public void getListEcritureComptable() {
        List<EcritureComptable> ecritureComptableList = getBusinessProxy().getComptabiliteManager()
                .getListEcritureComptable();
        Assert.assertEquals(5, ecritureComptableList.size());
    }

    @Before
    public void init() {

        sampleEcritureComptable = new EcritureComptable();
        sampleEcritureComptable.setId(6);
        sampleEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        sampleEcritureComptable.setDate(new Date());
        sampleEcritureComptable.setLibelle("Libelle");
        sampleEcritureComptable.setReference("AC-2020/00001");
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        sampleEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

        System.out.println("\n before method ");
    }

    @Test
    public void addReference() throws NotFoundException, FunctionalException {

        /*
         * getBusinessProxy().getComptabiliteManager().addReference(
         * sampleEcritureComptable);
         * Assert.assertEquals("AC-2020/00001", sampleEcritureComptable.getReference());
         * 
         */
    }

}
