package no.nav.dagpenger;

import no.nav.grunnbeløp.GrunnbeløpVerktøy;
import no.nav.årslønn.Årslønn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Kalkulator for å beregne hvor mye dagpenger en person har rett på i Norge basert på dagens grunnbeløp (1G).
 * For at en person skal ha rett på dagpenger, må en av de to følgene kravene være møtt:
 *      De siste 3 årene må gjennomsnitslønnen være høyere enn 3G.
 *      Tjent mer det siste året enn 1.5G.
 * Hvis en person har rett på dagpenger, må følgende ting vurderes for å kalkulere dagsatsen:
 *      Hva er størst av gjennomsnittlig årslønn de 3 siste årene og siste årslønn.
 *      Hvis siste årslønn er størst, er årslønnen høyere enn 6G.
 * Antall årlige arbeidsdager i Norge er satt til å være 260, så ved beregning av dagsats må 260 dager
 * brukes og ikke 365.
 *
 * @author Emil Elton Nilsen
 * @version 1.0
 */
public class DagpengerKalkulator {

    private static final int ARBEIDSDAGER_I_ÅRET = 260;
    private final GrunnbeløpVerktøy grunnbeløpVerktøy;
    private final List<Årslønn> årslønner;

    public DagpengerKalkulator(GrunnbeløpVerktøy grunnbeløpVerktøy) { 
        this.grunnbeløpVerktøy = grunnbeløpVerktøy;
        this.årslønner = new ArrayList<>();
    }

    public enum BeregningsMetode {
        SISTE_ÅRSLØNN, GJENNOMSNITTET_AV_TRE_ÅR, MAKS_ÅRLIG_DAGPENGERGRUNNLAG
    }

    /**
     * Hvis en person har rett på dagpenger, vil den kalkulere dagsatsen en person har rett på.
     * Hvis ikke en person har rett på dagpenger, vil metoden returnere 0kr som dagsats, som en antagelse på at det
     * er det samme som å ikke ha rett på dagpenger.
     * @return dagsatsen en person har rett på.
     */
    public double kalkulerDagsats() {
        if (!harRettigheterTilDagpenger()) {
          return 0.0;
        }
        BeregningsMetode beregningsMetode = velgBeregningsMetode(); // Endret unødvending mange kall til velgBeregningsMetode()

        return switch (beregningsMetode) {
          case SISTE_ÅRSLØNN ->
            Math.ceil(hentSisteÅrslønn() / ARBEIDSDAGER_I_ÅRET);
          case GJENNOMSNITTET_AV_TRE_ÅR ->
            Math.ceil(beregnGjennomsnittAvTreÅrslønner() / ARBEIDSDAGER_I_ÅRET);
          case MAKS_ÅRLIG_DAGPENGERGRUNNLAG ->
            Math.ceil(grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag() / ARBEIDSDAGER_I_ÅRET);
        };
    }

    /**
     * Sjekker om en person har rettighet til dagpenger eller ikke.
     * @return om personen har rett på dagpenger.
     */
    public boolean harRettigheterTilDagpenger() {
        if (this.årslønner.isEmpty()) {
          return false;
        }
        int antallTilgjengeligeÅrslønner = Math.min(3, årslønner.size());
        return summerNyligeÅrslønner(antallTilgjengeligeÅrslønner) >= grunnbeløpVerktøy.hentTotaltGrunnbeløpForGittAntallÅr(antallTilgjengeligeÅrslønner)
          || hentSisteÅrslønn() >= grunnbeløpVerktøy.hentMinimumÅrslønnForRettPåDagpenger();
    }

    /**
     * Velger hva som skal være beregnings metode for dagsats ut ifra en person sine årslønner.
     * @return beregnings metode for dagsats.
     */
    public BeregningsMetode velgBeregningsMetode() {
        double årslønn= hentSisteÅrslønn();
        double gjennomsnittAvTreÅrslønner = beregnGjennomsnittAvTreÅrslønner();
        double maksÅrligDagpengerGrunnlag = grunnbeløpVerktøy.hentMaksÅrligDagpengegrunnlag();

        if (årslønn > (gjennomsnittAvTreÅrslønner)) {
           if (årslønn > maksÅrligDagpengerGrunnlag ) {
               return BeregningsMetode.MAKS_ÅRLIG_DAGPENGERGRUNNLAG;
           }
           return BeregningsMetode.SISTE_ÅRSLØNN;
        }
        return BeregningsMetode.GJENNOMSNITTET_AV_TRE_ÅR;
    }

    public void leggTilÅrslønn(Årslønn årslønn) {
        this.årslønner.add(årslønn);
        this.sorterÅrslønnerBasertPåNyesteÅrslønn();
    }

    private double hentSisteÅrslønn() {
        return hentÅrslønnVedIndeks(0).map(Årslønn::hentÅrslønn).orElse(0.0); 
    }

    /**
     * Beregner gjennomsnittet av tre årslønner.
     * 
     * Antagelse: hvis man har mindre enn tre årslønner skal det beregnes gjennomsnitt av antall tilgjengelige årslønner.
     * Kan endres til å dele på 3 uansett eller kaste exception
     * @return gjennomsnittet av nylige årslønn
     */
    private double beregnGjennomsnittAvTreÅrslønner() {
        int antallTilgjengeligeÅrslønner = Math.min(3, årslønner.size());
        if (antallTilgjengeligeÅrslønner == 0) {
          return 0.0;
        }
        return summerNyligeÅrslønner(antallTilgjengeligeÅrslønner) / antallTilgjengeligeÅrslønner;
    }

    /**
     * Henter årslønnen i registeret basert på dens posisjon i registeret ved gitt indeks.
     * @param indeks posisjonen til årslønnen.
     * @return årslønnen ved gitt indeks.
     */
    public Optional<Årslønn> hentÅrslønnVedIndeks(int indeks) {
        if (indeks < 0 || indeks >= this.årslønner.size()) {
          return Optional.empty();
      }
        return Optional.of(årslønner.get(indeks));
    }

    /**
     * Summerer sammen antall årslønner basert på gitt parameter.
     * @param antallÅrÅSummere antall år med årslønner vi vil summere.
     * @return summen av årslønner.
     */
    public double summerNyligeÅrslønner(int antallÅrÅSummere) {
        if (antallÅrÅSummere <= 0 || this.årslønner.isEmpty()) {
          throw new IllegalArgumentException("Antall år å summere må være større enn 0 og årslønner kan ikke være tomt.");
        }
        if (antallÅrÅSummere > this.årslønner.size()) {
          throw new IllegalArgumentException("Kan ikke summere flere år enn antall årslønner i listen.");
        }
        return this.årslønner.stream().limit(antallÅrÅSummere)
        .mapToDouble(Årslønn::hentÅrslønn).sum();
    }

    /**
     * Sorterer registeret slik at den nyligste årslønnen er det først elementet i registeret.
     * Først blir årslønnene i registeret sortert ut at den eldstre årslønnen skal først i registeret,
     * deretter blir registeret reversert.
     */
    public void sorterÅrslønnerBasertPåNyesteÅrslønn() {
        this.årslønner.sort(Comparator.comparingInt(Årslønn::hentÅretForLønn).reversed());
    }
}
