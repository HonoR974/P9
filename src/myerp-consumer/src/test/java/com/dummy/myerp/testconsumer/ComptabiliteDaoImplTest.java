package com.dummy.myerp.testconsumer;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/bootstrapContext.xml" })
public class ComptabiliteDaoImplTest {

    @Autowired
    @Qualifier(value = "DaoProxy")
    private DaoProxy daoProxy;

    private ComptabiliteDao comptabiliteDao;

    @Before
    public void setUp() throws Exception {
        comptabiliteDao = daoProxy.getComptabiliteDao();
    }

    @Test
    public void getListCompteComptable() {

        List<CompteComptable> listCompteComptables = comptabiliteDao.getListCompteComptable();

        Assertions.assertThat(listCompteComptables).isNotEmpty();

    }

}
