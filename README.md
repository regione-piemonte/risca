# Prodotto

RISCA : Riscossione Canoni

# Descrizione del prodotto
Il prodotto intende proporre una soluzione multiutente per la gestione della riscossione canoni condivisibile tra diversi ambiti di applicazione, collegata al Back Office (Scrivania del Funzionario) e al Front Office (Scrivania del richiedente) nel caso di procedimenti di tipo concessorio. 
Nello specifico gli ambiti tematici interessati sono AMBIENTE, ENERGIA E TERRITORIO, OPERE PUBBLICHE e  Polizia mineraria, cave e miniere.

L’ esigenza principale è quella di fornire adeguato supporto alla gestione dei canoni: 
•	delle utenze di acque pubbliche e delle acque minerali
•	per l’utilizzo e l’occupazione di aree del demanio idrico
•	delle concessioni di escavazione e ricerca mineraria

L’obiettivo è invece quello di fornire adeguato supporto al Gestore canoni ed ai concessionari attraverso una soluzione multiutente che consenta di fare fattor comune delle esigenze di diversi fruitori.


Complessivamente l’applicativo prevede moduli di front-end web che interagiscono, tramite API, con moduli di back-end dove risiede la logica di business e che accedono al DB.

Il prodotto segue quindi il paradigma “SPA – Single Page Application” : la componente di interfaccia Angular ha una corrispondente componente di “BackEnd”, realizzata nel linguaggio Java, e che espone API REST per la componente Angular; il back-end accede al DB.

Il prodotto è strutturato nelle seguenti componenti specifiche:
- [riscadb]( https://github.com/regione-piemonte/risca/tree/main/riscadb ) : script per la creazione del DB (istanza DBMS Postgresql);
- [riscabowcl]( https://github.com/regione-piemonte/risca/tree/main/riscabowcl ) : Client Web (Angular 9.1.12), front-end applicativo;
- [riscaboweb]( https://github.com/regione-piemonte/risca/tree/main/riscaboweb ) : Web application di back office del prodotto				;
- [riscabesrv]( https://github.com/regione-piemonte/risca/tree/main/riscabesrv ) : Servizi di backend del prodotto risca.


In ciascuna di queste cartelle di componente si trovano ulteriori informazioni specifiche, incluso il BOM della componente di prodotto.

Nella directory [csi-lib]( https://github.com/regione-piemonte/ ) si trovano le librerie sviluppate da CSI-Piemonte con licenza OSS, come indicato nei BOM delle singole componenti, ed usate trasversalmente nel prodotto.

## Architettura Tecnologica

Le tecnologie adottate sono conformi agli attuali standard adottati da CSI per lo sviluppo del Sistema infermativo di Regione Piemonte, ed in particolare sono orientate alla possibilità di installare il prodotto sw su infrastruttura “a container”, orientata alle moderne architetture a “mini/microservizi”, prediligendo sostanzialmente gli strumenti open-source consolidati a livello internazionale (Linux, Java, Apache…); nel dettaglio tali pile prevedono:

- JDK 11
- WildFly - 17.0
- WS Apache 2.4
- DBMS Postgresql 12.4
- S.O. Linux CentOS 7
- Angular 9 (lato client web).

## Linguaggi di programmazione utilizzati

I principali linguaggi utilizzati sono:
•	Java v. 11
•	HTML5
•	Typescript/Javascript
•	XML/JSON
•	SQL

## DB di riferimento

A seguito di valutazione sull’utilizzo di DBMS open-source si è ritenuto che Postgresql garantisca adeguata robustezza e affidabilità tendo conto delle dimensioni previste per il DB e dei volumi annui gestiti.
La versione scelta è la 12.4, con possibilità di aggiornamento alla versione 15.

Per quanto riguarda la struttura del DB, si è optato per un “partizionamento” fra entità di carattere trasversale ed entità specifiche per ciascun adempimento. Tale strutturazione è coerente con la progettazione di piattaforme sw orientate ai "micro-servizi".

## Tecnologie framework e standard individuati
Le tecnologie individuate sono Open Source e lo "stack applicativo" utilizzato rispetta gli standard del SIRe Regione Piemonte. Si basa quindi sull’utilizzo di:

- JDK 11
- Angular 9.x
- librerie sviluppate da CSI e mantenute trasversalmente per la cooperazione applicativa


# Prerequisiti di sistema

Una istanza DBMS Postgresql (consigliata la verione 15) con utenza avente privilegi per la creazione tabelle ed altri oggetti DB  ed una ulteriore utenza separata non proprietaria dello schama, per l'esecuzione di istruzioni DML di Insert, Read, Update e Delete sui dati.

Una istanza di web server, consigliato apache web server ( https://httpd.apache.org/ ).\
Per il build è previsto l'uso di apache Ant (https://ant.apache.org/)

Infine, anche per quanto concerne l'autenticazione e la profilazione degli utenti del sistema, RISCA è integrato con servizi trasversali del sistema informativo regionale ("Shibboleth", "IRIDE"), di conseguenza per un utilizzo in un altro contesto occorre avere a disposizione servizi analoghi o integrare moduli opportuni che svolgano analoghe funzionalità.
 

# Installazione

Creare lo schema del DB, tramite gli script della componente riscadb.
 
Configurare il datasource nel file application.properties 

Configurare i web server e definire gli opportuni Virtual Host e "location" - per utilizzare il protocollo https occorre munirsi di adeguati certificati SSL.

Nel caso si vogliano sfruttare le funzionalità di invio mail, occorre anche configurare un mail-server.


# Deployment

Dopo aver seguito le indicazioni del paragrafo relativo all'installazione, si può procedere al build dei pacchetti ed al deploy sull'infrastruttura prescelta.


# Versioning
Per la gestione del codice sorgente viene utilizzato Git, ma non vi sono vincoli per l'utilizzo di altri strumenti analoghi.\
Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).


# Copyrights
© Copyright Regione Piemonte – 2025\


# License

SPDX-License-Identifier: EUPL-1.2-or-later .\
Questo software è distribuito con licenza EUPL-1.2 .\
Consultare il file LICENSE.txt per i dettagli sulla licenza.