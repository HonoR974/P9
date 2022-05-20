package com.dummy.myerp.technical.util.spring;

import org.springframework.beans.factory.FactoryBean;


/**
 * {@link FactoryBean} permettant de déclarer des Beans d'une classe spécifique et valant <code>null</code>.
 *
 * @param <T> Classe de l'objet construit
 */
public class NullFactoryBean<T> implements FactoryBean<T> {

    /** Classe de l'objet construit */
    private final Class<T> objectType;

    /**
     * Constructeur.
     *
     * @param pObjectType -
     */
    public NullFactoryBean(Class<T> pObjectType) {
        this.objectType = pObjectType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getObject() throws Exception {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getObjectType() {
        return objectType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return false;
    }
}
