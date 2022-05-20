
CREATE TABLE MYERP.journal_comptable (
                code VARCHAR(5) NOT NULL,
                libelle VARCHAR(150) NOT NULL,
                CONSTRAINT journal_comptable_pk PRIMARY KEY (code)
);


CREATE TABLE MYERP.sequence_ecriture_comptable (
                journal_code VARCHAR(5) NOT NULL,
                annee INTEGER NOT NULL,
                derniere_valeur INTEGER NOT NULL,
                CONSTRAINT sequence_ecriture_comptable_pk PRIMARY KEY (journal_code, annee)
);


CREATE SEQUENCE MYERP.ecriture_comptable_id_seq;

CREATE TABLE MYERP.ecriture_comptable (
                id INTEGER NOT NULL DEFAULT nextval('MYERP.ecriture_comptable_id_seq'),
                journal_code VARCHAR(5) NOT NULL,
                reference VARCHAR(30),
                date TIMESTAMP NOT NULL,
                libelle VARCHAR(200) NOT NULL,
                CONSTRAINT ecriture_comptable_pk PRIMARY KEY (id)
);


ALTER SEQUENCE MYERP.ecriture_comptable_id_seq OWNED BY MYERP.ecriture_comptable.id;

CREATE TABLE MYERP.compte_comptable (
                numero INTEGER NOT NULL,
                libelle VARCHAR(150) NOT NULL,
                CONSTRAINT compte_comptable_pk PRIMARY KEY (numero)
);


CREATE TABLE MYERP.ligne_ecriture_comptable (
                ecriture_id INTEGER NOT NULL,
                ligne_id INTEGER NOT NULL,
                compte_comptable_numero INTEGER NOT NULL,
                libelle VARCHAR(200),
                debit NUMERIC(15,2),
                credit NUMERIC(15,2),
                CONSTRAINT ligne_ecriture_comptable_pk PRIMARY KEY (ecriture_id, ligne_id)
);


ALTER TABLE MYERP.sequence_ecriture_comptable ADD CONSTRAINT journal_comptable_sequence_ecriture_comptable_fk
FOREIGN KEY (journal_code)
REFERENCES MYERP.journal_comptable (code)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE MYERP.ecriture_comptable ADD CONSTRAINT journal_comptable_ecriture_comptable_fk
FOREIGN KEY (journal_code)
REFERENCES MYERP.journal_comptable (code)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE MYERP.ligne_ecriture_comptable ADD CONSTRAINT ecriture_comptable_ligne_ecriture_comptable_fk
FOREIGN KEY (ecriture_id)
REFERENCES MYERP.ecriture_comptable (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE MYERP.ligne_ecriture_comptable ADD CONSTRAINT compte_comptable_ligne_ecriture_comptable_fk
FOREIGN KEY (compte_comptable_numero)
REFERENCES MYERP.compte_comptable (numero)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
