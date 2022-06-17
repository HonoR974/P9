package com.dummy.myerp.business;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "classpath:/com/dummy/myerp/business/businessBootstrapContext.xml" })
public class InitSpringContextTest {

    @Autowired
    ApplicationContext applicationContext;

    /*
     * @Test
     * 
     * @DisplayName("Beans should be initialised")
     * void initTest() {
     * Assertions.assertThat((BusinessProxy)
     * applicationContext.getBean("businessProxy")).isNotNull();
     * Assertions.assertThat((TransactionManager)
     * applicationContext.getBean("transactionManager")).isNotNull();
     * }
     */
}
