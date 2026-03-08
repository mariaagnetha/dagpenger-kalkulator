package saksbehandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.dagpenger.BeregningsMetode;
import no.nav.dagpenger.DagpengerKalkulator;
import no.nav.grunnbeløp.GrunnbeløpVerktøy;
import no.nav.saksbehandler.BehandlingsStatus;
import no.nav.saksbehandler.Sak;
import no.nav.saksbehandler.Saksbehandler;
import no.nav.årslønn.Årslønn;
import no.nav.saksbehandler.SaksbehandlerService;
import no.nav.saksbehandler.Spesialisering;

public class SaksbehandlerServiceTester {

  @Test
    public void testSakBlirLagdMedRiktigSpesialiseringVedIkkeRettTilDagpenger()  {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy );
        SaksbehandlerService saksbehandlerService = new SaksbehandlerService();
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 44000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 52000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 100000));
        Sak sak = saksbehandlerService.lagSak(dagpengerKalkulator, "Sak1");
        assertFalse(dagpengerKalkulator.harRettigheterTilDagpenger());
        assertEquals(Spesialisering.AVSLAG_PÅ_GRUNN_AV_FOR_LAV_INNTEKT, sak.hentSpesialisering());
        assertEquals(0.0, sak.hentDagsats());
    }

  @Test
    public void testSakBlirLagdMedRiktigSpesialiseringVedRettTilDagpengerMaksSats()  {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        SaksbehandlerService saksbehandlerService = new SaksbehandlerService();
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 830000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 110000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 24000));
        Sak sak = saksbehandlerService.lagSak(dagpengerKalkulator, "Sak2");
        assertEquals(BeregningsMetode.MAKS_ÅRLIG_DAGPENGERGRUNNLAG, dagpengerKalkulator.velgBeregningsMetode());
        assertEquals(Spesialisering.INNVILGET_MED_MAKSSATS, sak.hentSpesialisering());
    }

  @Test
    public void testSakBlirLagdMedRiktigSpesialiseringVedRettTilDagpenger()  {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        SaksbehandlerService saksbehandlerService = new SaksbehandlerService();
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 330000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 400000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 334000));
        Sak sak = saksbehandlerService.lagSak(dagpengerKalkulator, "Sak3");
        assertEquals(BeregningsMetode.GJENNOMSNITTET_AV_TRE_ÅR, dagpengerKalkulator.velgBeregningsMetode());
        assertEquals(Spesialisering.INNVILGET, sak.hentSpesialisering());
    }

  @Test
    public void testSaksbehandlerHenterKunSakerISinEgenSpesialisering() {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        SaksbehandlerService saksbehandlerService = new SaksbehandlerService();
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 44000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 52000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 100000));
        saksbehandlerService.lagSak(dagpengerKalkulator, "Sak4");
        Saksbehandler bob = new Saksbehandler("Bob", Spesialisering.INNVILGET);
        List<Sak> bobsSaker = saksbehandlerService.hentUbehandledeSaker(bob);
        assertTrue(bobsSaker.isEmpty());
    }

  @Test
    public void testSaksbehandlerBehandlerSakOgAvslårSak() {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        SaksbehandlerService saksbehandlerService = new SaksbehandlerService();
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 44000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 52000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 100000));
        Sak sak = saksbehandlerService.lagSak(dagpengerKalkulator, "Sak5");
        Saksbehandler bob = new Saksbehandler("Bob", Spesialisering.AVSLAG_PÅ_GRUNN_AV_FOR_LAV_INNTEKT);
        saksbehandlerService.behandleSak(bob, sak, BehandlingsStatus.AVSLÅTT);
        assertEquals(BehandlingsStatus.AVSLÅTT, sak.hentBehandlingsStatus());
    }

  @Test
    public void testSaksbehandlerBehandlerSakSkalKasteExceptionVedFeilSpesialisering() {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        SaksbehandlerService saksbehandlerService = new SaksbehandlerService();
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 44000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 52000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 100000));
        Sak sak = saksbehandlerService.lagSak(dagpengerKalkulator, "Sak6");
        Saksbehandler bob = new Saksbehandler("Bob", Spesialisering.INNVILGET);
        assertThrows(IllegalArgumentException.class, () -> {
          saksbehandlerService.behandleSak(bob, sak, BehandlingsStatus.GODKJENT);
    });
    }
  
  @Test
    public void testSaksbehandlerGodkjennerSak() {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        SaksbehandlerService saksbehandlerService = new SaksbehandlerService();
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 44000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 52000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 100000));
        Sak sak = saksbehandlerService.lagSak(dagpengerKalkulator, "Sak6");
        Saksbehandler bob = new Saksbehandler("Bob", Spesialisering.AVSLAG_PÅ_GRUNN_AV_FOR_LAV_INNTEKT);
        saksbehandlerService.behandleSak(bob, sak, BehandlingsStatus.GODKJENT);
        assertEquals(BehandlingsStatus.GODKJENT, sak.hentBehandlingsStatus());
    }
}
