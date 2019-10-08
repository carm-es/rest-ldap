package es.carm.openldap;

public final class WriteError extends Exception {

  private static final long serialVersionUID = 8218380130198970934L;

  private static final String msg = "ERROR AL MODIFICAR";

  public WriteError() {
    super(msg);
  }

  public WriteError(Throwable e) {
    super(msg, e);
  }
}
