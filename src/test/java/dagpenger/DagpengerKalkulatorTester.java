package dagpenger;

import no.nav.dagpenger.DagpengerKalkulator;
import no.nav.grunnbeløp.GrunnbeløpVerktøy;
import no.nav.årslønn.Årslønn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DagpengerKalkulatorTester {

    @Test
    public void testSkalHaRettigheterTilDagpengerUtifraSisteTreÅrslønner()  {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 445000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 465000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 300000));
        assertTrue(dagpengerKalkulator.harRettigheterTilDagpenger());
    }

    @Test
    public void testSkalHaRetigheterTilDagpengerSisteÅrslønn() {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 0));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 0));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 467000));
        assertTrue(dagpengerKalkulator.harRettigheterTilDagpenger());
    }

    @Test
    public void testSkalIkkeHaRettigheterTilDagpengerSisteTreÅrslønner()  {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy );
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 44000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 52000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 100000));
        assertFalse(dagpengerKalkulator.harRettigheterTilDagpenger());
    }

    @Test
    public void testSkalIkkeHaRettigheterTilDagpengerSisteÅrslønn()  {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 0));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 130000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 0));
        assertFalse(dagpengerKalkulator.harRettigheterTilDagpenger());
    }

    @Test
    public void testBeregningsMetodeBlirSattTilSisteÅrslønn() {
      GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 550000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 110000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 24000));
        assertEquals(DagpengerKalkulator.BeregningsMetode.SISTE_ÅRSLØNN, dagpengerKalkulator.velgBeregningsMetode());
    }

    @Test
    public void testBeregningsMetodeBlirSattTilMaksÅrslønnGrunnbeløp() {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 830000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 110000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 24000));
        assertEquals(DagpengerKalkulator.BeregningsMetode.MAKS_ÅRLIG_DAGPENGERGRUNNLAG, dagpengerKalkulator.velgBeregningsMetode());
    }

    @Test
    public void testBeregningsMetodeBlirSattTilGjennomsnittetAvTreÅr() {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 330000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 400000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 334000));
        assertEquals(DagpengerKalkulator.BeregningsMetode.GJENNOMSNITTET_AV_TRE_ÅR, dagpengerKalkulator.velgBeregningsMetode());
    }

    @Test
    public void testDagsatsKalkulertUtifraSisteÅrslønn() {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 550000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 110000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 24000));
        assertEquals(2116, dagpengerKalkulator.kalkulerDagsats());
    }

    @Test
    public void testDagsatsKalkulertUtifraMaksÅrligGrunnbeløp() {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 830000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 24000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 110000));
        assertEquals(3004, dagpengerKalkulator.kalkulerDagsats());
    }

    @Test
    public void testDagsatsKalkulertUtifraTreÅrsGjennomsnitt() {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 330000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 334000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 400000));
        assertEquals(1365, dagpengerKalkulator.kalkulerDagsats());
    }

    @Test
    public void testDagsatsKalkulertIkkeRettPåDagpenger() {
        GrunnbeløpVerktøy grunnbeløpVerktøy = new GrunnbeløpVerktøy();
        DagpengerKalkulator dagpengerKalkulator = new DagpengerKalkulator(grunnbeløpVerktøy);
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2025, 80000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2024, 100000));
        dagpengerKalkulator.leggTilÅrslønn(new Årslønn(2023, 70000));
        assertEquals(0, dagpengerKalkulator.kalkulerDagsats());
    }
}
