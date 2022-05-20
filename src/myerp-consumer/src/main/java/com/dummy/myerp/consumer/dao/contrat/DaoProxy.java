package com.dummy.myerp.consumer.dao.contrat;


/**
 * Interface du Proxy d'accès à la couche DAO
 */
public interface DaoProxy {

    /**
     * Renvoie un {@link ComptabiliteDao}
     *
     * @return {@link ComptabiliteDao}
     */
    ComptabiliteDao getComptabiliteDao();

}
