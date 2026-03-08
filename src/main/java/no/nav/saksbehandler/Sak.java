package no.nav.saksbehandler;

import java.util.Objects;

/**
 * Sak representerer en sak som skal bli behandlet av en saksbehandler.
 * Holder på informasjon som dagsats, sakId, spesialisering og behandlingsstatus for en sak.
 * 
 * @author Maria Agnetha Andreassen
 */
public class Sak {

  private final double dagsats;
  private final String sakId;
  private final Spesialisering spesialisering;
  private BehandlingsStatus behandlingsStatus;
  
  public Sak(double dagsats, String sakId, Spesialisering spesialisering) {
    this.dagsats = dagsats;
    this.sakId = Objects.requireNonNull(sakId, "sakId kan ikke være null");
    this.spesialisering = Objects.requireNonNull(spesialisering, "spesialisering kan ikke være null");
    this.behandlingsStatus = BehandlingsStatus.UBEHANDLET;
  }

  /**
   * Henter dagsats for en sak.
   * @return dagsats for en sak.
   */
  public double hentDagsats() {
    return dagsats;
  }

  /**
   * Henter sakId for en sak.
   * @return sakId for en sak.
   */
  public String hentSakId() {
    return sakId;
  }

  /**
   * Henter spesialisering for en sak.
   * @return spesialisering for en sak.
   */
  public Spesialisering hentSpesialisering() {
    return spesialisering;
  }

  /**
   * Henter behandlingsstatus for en sak.
   * @return behandlingsstatus for en sak.
   */
  public BehandlingsStatus hentBehandlingsStatus() {
    return behandlingsStatus;
  }

  /**
   * Setter behandlingsstatus for en sak.
   * @param behandlingsStatus behandlingsstatusen saken skal settes til.
   */
  public void settBehandlingsStatus(BehandlingsStatus behandlingsStatus) {
    this.behandlingsStatus = behandlingsStatus;
  }
}