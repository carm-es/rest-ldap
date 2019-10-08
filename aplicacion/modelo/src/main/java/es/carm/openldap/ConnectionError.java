package es.carm.openldap;

public final class ConnectionError extends Exception {

    private static final long serialVersionUID = 7504319756260921957L;

    private static final String msg = "ERROR EN LA CONEXION";

    public ConnectionError() {
        super(msg);
    }

    public ConnectionError(Throwable e) {
        super(msg, e);
    }
}
