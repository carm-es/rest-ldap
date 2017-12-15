package es.carm.openldap;

public final class NoDataFound extends Exception {

	private static final long serialVersionUID = 6653811507224835535L;

	private static final String msg="NO SE ENCONTRÓ NADA";
	
	public NoDataFound() {
		super(msg);
	}
	public NoDataFound(Throwable e) {
		super(msg, e );
	}
}
