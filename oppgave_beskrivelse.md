# Velkommen til kode-oppgave hos oss i Nav IT!

Gratuler med å bli kalt inn til intervju hos oss i Nav, nå ønsker vi å bli litt bedre kjent med hvordan
du jobber og reflekter over kode. I denne oppgaven vil du bli gitt en eksisterende kodebase for en dagpenger kalkulator som du skal
jobbe videre med. Vi anbefaler å bli bedre kjent med den eksisterende kodebasen før du hopper rett ut i kodingen 😄💻

Kodebasen for [dagpenger kalkulatoren finner du her](https://github.com/navikt/dagpenger-kalkulator).

Du kan lese mer [om hvordan kalkulatoren fungerer her](https://github.com/navikt/dagpenger-kalkulator/blob/main/README.md).

Du leverer inn din besvarelse på oppgaven ved å publisere den i et GitHub repository og legger til følgende
brukere som collaborators:
- perkynades
- sindreh-navikt
- PauliusDeveika
- Martineem
- brahabra
- murvold

**Frist for å levere besvarelsen er etter avtale**

## Oppgaven
I oppgaven du nå skal utføre er det ingen krav om å levere et brukergrensesnitt, det går derfor helt fint
at alt kalkulatoren gjør er å printe til terminalen. 

Tenk på modelleringen, og vær forberedt på å forklare hva du har tenkt på intervjuet.

Det er også ikke et krav om å klare å implementere alt i løpet av fristen til i oppgaven. Hvis du 
ikke blir ferdig kan du godt bare skrive ned hva du ville ha gjort annerledes og presentere dette på intervjuet.

### Del 1
Dagpenger kalkulatoren er laget av en kollega som liker å ta snarveier, noe som kode-kvaliteten reflekter. Første
oppgave blir da å rydde opp i koden for å enklere kunne videreutvkle den.

### Del 2
Etter å ha ryddet litt, skal du nå utvide kalkulatoren slik at kalkulasjonen kan godkjennes av en saksbehandler.
En saksbehandler har en spesialisering, og skal godkjenne eller avslå resultater. En saksbehandler skal bare 
behandle resultater innenfor sin spesialisering. 

Spesialiseringene er:
- Avslag på grunn av for lav inntekt
- Innvilget
- Innvilget med makssats

Den nye funksjonaliteten du skal implementere er altså:
1. En saksbehandler skal kunne hente ubehandlede resultater innenfor sin spesialisering.
2. En saksbehandler skal kunne godkjenne eller avslå resultatet.

_Lykke til!_
