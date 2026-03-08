package no.nav;

import java.util.List;

import no.nav.dagpenger.DagpengerKalkulator;
import no.nav.grunnbeløp.GrunnbeløpVerktøy;
import no.nav.saksbehandler.BehandlingsStatus;
import no.nav.saksbehandler.Sak;
import no.nav.saksbehandler.Saksbehandler;
import no.nav.saksbehandler.SaksbehandlerService;
import no.nav.saksbehandler.Spesialisering;
import no.nav.årslønn.Årslønn;

public class Main {
    public static void main(String[] args) {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        SaksbehandlerService saksbehandlerService = new SaksbehandlerService();

        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 500000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2022, 450000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2021, 400000));

        Sak sak = saksbehandlerService.lagSak(dagpengerKalkulator, "Sak1");
        Saksbehandler bob = new Saksbehandler("Bob", Spesialisering.INNVILGET);
        
        System.out.println("---🤖 Kalkulerer dagsats... 🤖---");
        System.out.println("Personen har rett på følgende dagsats: " + sak.hentDagsats());
        System.out.println("---🤖 Dagsats ferdig kalkulert 🤖---");

        System.out.println("---🤖 Bob behandler saker... 🤖---");
        List<Sak> bobsSaker = saksbehandlerService.hentUbehandledeSaker(bob);
        System.out.println("Bob har " + bobsSaker.size() + " saker å behandle.");
        for (Sak bobSinSak : bobsSaker) {
          saksbehandlerService.behandleSak(bob, bobSinSak, BehandlingsStatus.GODKJENT);
          System.out.println("Bob har behandlet sakId: " + bobSinSak.hentSakId() + " med behandlingsstatus: " + bobSinSak.hentBehandlingsStatus());
        };
        System.out.println("---🤖 Bob er ferdig med å behandle saker 🤖---");
    }
}