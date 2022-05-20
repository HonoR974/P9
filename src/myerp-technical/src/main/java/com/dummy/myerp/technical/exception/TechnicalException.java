package com.dummy.myerp.technical.exception;

/**
 * Classe des Exceptions Techniques
 */
public class TechnicalException extends Exception {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;


    // ==================== Constructeurs ====================
    /**
     * Constructeur.
     *
     * @param pMessage -
     */
    public TechnicalException(String pMessage) {
        super(pMessage);
    }

    /**
     * Constructeur.
     *
     * @param pCause -
     */
    public TechnicalException(Throwable pCause) {
        super(pCause);
    }

    /**
     * Constructeur.
     *
     * @param pMessage -
     * @param pCause -
     */
    public TechnicalException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }
}
