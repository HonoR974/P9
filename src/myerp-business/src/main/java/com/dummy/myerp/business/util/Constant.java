package com.dummy.myerp.business.util;

public final class Constant {

    public static final String ECRITURE_COMPTABLE_MANAGEMENT_RULE_ERRORMSG = "L'écriture comptable ne respecte pas les règles de gestion.";
    public static final String ECRITURE_COMPTABLE_VALIDATION_CONSTRAINT_ERRORMSG = "L'écriture comptable ne respecte pas les contraintes de validation.";
    public static final String RG_COMPTA_2_VIOLATION_ERRORMSG = "RG_Compta_2 : L'écriture comptable n'est pas équilibrée. la somme des montants au crédit des lignes d'écriture doit être égale à la somme des montants au débit.";
    public static final String RG_COMPTA_3_VIOLATION_ERRORMSG = "RG_Compta_3 : L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.";
    public static final String RG_COMPTA_5_VIOLATION_ERRORMSG = "RG_Compta 5 : La référence ne concorde pas avec l'année d'écriture ou le code du journal comptable. La référence d'une écriture comptable est composée du code du journal dans lequel figure l'écriture suivi de l'année et d'un numéro de séquence (propre à chaque journal) sur 5 chiffres incrémenté automatiquement à chaque écriture.";
    public static final String RG_COMPTA_6_VIOLATION_ERRORMSG = "RG_Compta_6 : La référence spécifié est déjà utilisée. La référence d'une écriture comptable doit être unique, il n'est pas possible de créer plusieurs écritures ayant la même référence.";
    public static final String ECRITURE_COMPTABLE_DATE_NULL_FOR_ADD_REFERENCE = "La référence ne peut pas être construite si la date de l'écriture comptable n'est pas définie";
    public static final String ECRITURE_COMPTABLE_JOURNAL_NULL_FOR_ADD_REFERENCE = "La référence ne peut pas être construite si le journal de l'écriture comptable n'est pas défini";
    public static final String BUSINESS_PROXY_NOT_INTIALIZED = "La classe BusinessProxyImpl n'a pas été initialisée.";
}
