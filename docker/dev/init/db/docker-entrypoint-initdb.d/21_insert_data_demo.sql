--- ================================================================================								
--- compte_comptable								
--- ================================================================================								
--		compte_comptable						
--		numero	libelle					
--		401	Fournisseurs					
--		411	Clients					
--		4456	Taxes sur le chiffre d'affaires déductibles					
--		4457	Taxes sur le chiffre d'affaires collectées par l'entreprise					
--		512	Banque					
--		606	Achats non stockés de matières et fournitures					
--		706	Prestations de services					
--								
--	compte_comptable							
--								
--		numero	libelle					
--		CHAR(1)	VARCHAR					
	INSERT INTO MYERP.compte_comptable (numero, libelle) VALUES (	401,	'Fournisseurs'	);				
	INSERT INTO MYERP.compte_comptable (numero, libelle) VALUES (	411,	'Clients'	);				
	INSERT INTO MYERP.compte_comptable (numero, libelle) VALUES (	4456,	'Taxes sur le chiffre d''affaires déductibles'	);				
	INSERT INTO MYERP.compte_comptable (numero, libelle) VALUES (	4457,	'Taxes sur le chiffre d''affaires collectées par l''entreprise'	);				
	INSERT INTO MYERP.compte_comptable (numero, libelle) VALUES (	512,	'Banque'	);				
	INSERT INTO MYERP.compte_comptable (numero, libelle) VALUES (	606,	'Achats non stockés de matières et fournitures'	);				
	INSERT INTO MYERP.compte_comptable (numero, libelle) VALUES (	706,	'Prestations de services'	);				
								
								
								
								
								
--- ================================================================================								
--- journal_comptable								
--- ================================================================================								
--		journal_comptable						
--		code	libelle					
--		AC	Achat					
--		VE	Vente					
--		BQ	Banque					
--		OD	Opérations Diverses					
--								
--	journal_comptable							
--								
--		code	libelle					
--		VARCHAR	VARCHAR					
	INSERT INTO MYERP.journal_comptable (code, libelle) VALUES (	'AC',	'Achat'	);				
	INSERT INTO MYERP.journal_comptable (code, libelle) VALUES (	'VE',	'Vente'	);				
	INSERT INTO MYERP.journal_comptable (code, libelle) VALUES (	'BQ',	'Banque'	);				
	INSERT INTO MYERP.journal_comptable (code, libelle) VALUES (	'OD',	'Opérations Diverses'	);				
								
								
								
								
								
--- ================================================================================								
--- sequence_ecriture_comptable								
--- ================================================================================								
--		sequence_ecriture_comptable						
--		journal_code	annee	derniere_valeur				
--		AC	2016	40				
--		VE	2016	41				
--		BQ	2016	51				
--		OD	2016	88				
--								
--	sequence_ecriture_comptable							
--								
--		journal_code	annee	derniere_valeur				
--		VARCHAR	INTEGER	INTEGER				
	INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'AC',	2016,	40	);			
	INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'VE',	2016,	41	);			
	INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'BQ',	2016,	51	);			
	INSERT INTO MYERP.sequence_ecriture_comptable (journal_code, annee, derniere_valeur) values (	'OD',	2016,	88	);			
								
								
								
								
								
--- ================================================================================								
--- ecriture_comptable								
--- ================================================================================								
--		ecriture_comptable						
--		id	journal_code	reference	date	libelle		
--		-1	AC	AC-2016/00001	31/12/16	Cartouches d’imprimante		
--		-2	VE	VE-2016/00002	30/12/16	TMA Appli Xxx		
--		-3	BQ	BQ-2016/00003	29/12/16	Paiement Facture F110001		
--		-4	VE	VE-2016/00004	28/12/16	TMA Appli Yyy		
--		-5	BQ	BQ-2016/00005	27/12/16	Paiement Facture C110002		
--								
--	ecriture_comptable							
--								
--		id	journal_code	reference	date	libelle		
--		INTEGER	VARCHAR	VARCHAR	DATE	VARCHAR		
	INSERT INTO MYERP.ecriture_comptable (id,journal_code,reference,date,libelle) VALUES (	-1,	'AC',	'AC-2016/00001',	'2016-12-31',	'Cartouches d’imprimante'	);	
	INSERT INTO MYERP.ecriture_comptable (id,journal_code,reference,date,libelle) VALUES (	-2,	'VE',	'VE-2016/00002',	'2016-12-30',	'TMA Appli Xxx'	);	
	INSERT INTO MYERP.ecriture_comptable (id,journal_code,reference,date,libelle) VALUES (	-3,	'BQ',	'BQ-2016/00003',	'2016-12-29',	'Paiement Facture F110001'	);	
	INSERT INTO MYERP.ecriture_comptable (id,journal_code,reference,date,libelle) VALUES (	-4,	'VE',	'VE-2016/00004',	'2016-12-28',	'TMA Appli Yyy'	);	
	INSERT INTO MYERP.ecriture_comptable (id,journal_code,reference,date,libelle) VALUES (	-5,	'BQ',	'BQ-2016/00005',	'2016-12-27',	'Paiement Facture C110002'	);	
								
								
								
								
								
--- ================================================================================								
--- ligne_ecriture_comptable								
--- ================================================================================								
--		ligne_ecriture_comptable						
--		ecriture_id	ligne_id	compte_comptable_numero	libelle	debit	credit	
--		-1	1	606	Cartouches d’imprimante	43,95		
--		-1	2	4456	TVA 20%	8,79		
--		-1	3	401	Facture F110001		52,74	
--		-2	1	411	Facture C110002	3000		
--		-2	2	706	TMA Appli Xxx		2500	
--		-2	3	4457	TVA 20%		500	
--		-3	1	401		52,74		
--		-3	2	512			52,74	
--		-4	1	411	Facture C110004	5700		
--		-4	2	706	TMA Appli Xxx		4750	
--		-4	3	4457	TVA 20%		950	
--		-5	1	512		3000		
--		-5	2	411			3000	
--								
--	ligne_ecriture_comptable							
--								
--		ecriture_id	ligne_id	compte_comptable_numero	libelle	debit	credit	
--		INTEGER	INTEGER	INTEGER	VARCHAR	NUMERIC(15,2)	NUMERIC(15,2)	
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-1,	1,	606,	'Cartouches d’imprimante',	43.95,	null	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-1,	2,	4456,	'TVA 20%',	8.79,	null	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-1,	3,	401,	'Facture F110001',	null,	52.74	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-2,	1,	411,	'Facture C110002',	3000,	null	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-2,	2,	706,	'TMA Appli Xxx',	null,	2500	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-2,	3,	4457,	'TVA 20%',	null,	500	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-3,	1,	401,	null,	52.74,	null	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-3,	2,	512,	null,	null,	52.74	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-4,	1,	411,	'Facture C110004',	5700,	null	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-4,	2,	706,	'TMA Appli Xxx',	null,	4750	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-4,	3,	4457,	'TVA 20%',	null,	950	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-5,	1,	512,	null,	3000,	null	);
	INSERT INTO MYERP.ligne_ecriture_comptable (ecriture_id,ligne_id,compte_comptable_numero,libelle,debit,credit) VALUES (	-5,	2,	411,	null,	null,	3000	);
