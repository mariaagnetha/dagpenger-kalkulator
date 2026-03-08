package no.nav.saksbehandler;

import java.util.ArrayList;
import java.util.List;
import no.nav.dagpenger.DagpengerKalkulator;

/**
 * SaksbehandlerService representerer en tjeneste som håndterer saker for
 * saksbehandlere.
 * Håndterer logikken for å lage saker, behandle saker og hente ubehandlede saker for saksbehandlere.
 * 
 * @author Maria Agnetha Andreassen
 */
public class SaksbehandlerService {

    private final List<Sak> saker = new ArrayList<>();

    /**
     * Lager en sak basert på dagsats og beregnet spesialisering.
     * 
     * @param dagpengerKalkulator kalkulatoren som har kalkulert dagsatsen.
     * @param sakId id til saken.
     * @return ny sak.
     */
    public Sak lagSak(DagpengerKalkulator dagpengerKalkulator, String sakId) {
      double dagsats = dagpengerKalkulator.kalkulerDagsats();
      Spesialisering spesialisering = dagpengerKalkulator.beregnSpesialisering(dagsats, dagpengerKalkulator);
      Sak nySak = new Sak(dagsats, sakId, spesialisering);
      leggTilSak(nySak);
      return nySak;
    }

    /**
     * Behandler en sak ved å sette behandlingsstatus for en sak.
     * En saksbehandler skal kun kunne behandle saker innenfor sin spesialisering.
     * 
     * @param saksbehandler saksbehandleren som skal behandle saken.
     * @param sak saken som skal behandles.
     * @param behandlingsStatus den behandlingsstatusen som saken skal settes til etter behandling.
     */
    public void behandleSak(Saksbehandler saksbehandler, Sak sak, BehandlingsStatus behandlingsStatus) {
      if (behandlingsStatus == BehandlingsStatus.UBEHANDLET) {
        throw new IllegalArgumentException("Kan ikke sette behandlingsstatus til UBEHANDLET.");
      }
      if (saksbehandler.hentSpesialisering() != sak.hentSpesialisering()) {
        throw new IllegalArgumentException(
            "Saksbehandler kan ikke behandle resultatet, da det ikke er riktig spesialisering.");
      }
      if (sak.hentBehandlingsStatus() != BehandlingsStatus.UBEHANDLET) {
        throw new IllegalStateException("Saken er behandlet fra før.");
      }
      sak.settBehandlingsStatus(behandlingsStatus);
    }

    /**
     * Legger til en sak i registeret.
     */
    private void leggTilSak(Sak sak) {
      this.saker.add(sak);
    }

    /**
     * Henter ubehandlere saker for en saksbehandler, basert på saksbehandler sin spesialisering.
     * En saksbehandler skal kun kunne hente ut ubehandlede saker innenfor sin spesialisering.
     * 
     * @param saksbehandler saksbehandleren som skal hente ut ubehandlede saker.
     * @return en liste av ubehandlede saker innenfor saksbehandler sin spesialisering.
     */
    public List<Sak> hentUbehandledeSaker(Saksbehandler saksbehandler) {
      return saker.stream()
          .filter(sak -> sak.hentBehandlingsStatus() == BehandlingsStatus.UBEHANDLET)
          .filter(sak -> sak.hentSpesialisering() == saksbehandler.hentSpesialisering())
          .toList();
    }
}
