package no.nav.saksbehandler;

/**
 * BehandlingsStatus representerer statusen for behandlingen av en sak. 
 * UBEHANDLET betyr at saken ikke har blitt behandlet ennå.
 * GODKJENT betyr at saken har blitt godkjent.
 * AVSLÅTT betyr at saken har blitt avslått.
 * 
 * @author Maria Agnetha Andreassen
 */
public enum BehandlingsStatus {
  UBEHANDLET,
  GODKJENT,
  AVSLÅTT;
}
