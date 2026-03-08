package no.nav.saksbehandler;

/**
 * Saksbehandler representerer en saksbehandler som behandler saker.
 * Holder på informasjon som navn og spesialisering for en saksbehandler.
 * 
 * @author Maria Agnetha Andreassen
 */
public class Saksbehandler {

  private final String navn;
  private final Spesialisering spesialisering;

  public Saksbehandler(String navn, Spesialisering spesialisering) {
    this.navn = navn;
    this.spesialisering = spesialisering;
  }

  /**
   * Henter navn for en saksbehandler.
   * @return navn for en saksbehandler.
   */
  public String hentNavn() {
    return navn;
  }

  /**
   * Henter spesialisering for en saksbehandler.
   * @return spesialisering for en saksbehandler.
   */
  public Spesialisering hentSpesialisering() {
    return spesialisering;
  }
}
