package com.dummy.myerp.business.impl.manager;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/testContext.xml" })
public class ComptabiliteManagerImplITTest extends BusinessTestCase {

        private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

        @Test
        public void getListCompteComptable() {
                System.out.println("\n le test est lancé ");
                int somme = 2;
                assertTrue(somme > 1);
        }
}
