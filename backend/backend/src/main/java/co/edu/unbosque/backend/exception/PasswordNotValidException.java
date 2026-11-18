package co.edu.unbosque.backend.exception;

public class PasswordNotValidException extends Exception {

	private static final long serialVersionUID = -1290628531913444044L;

	public PasswordNotValidException() {
		super("La contraseña no cumple el estandar (minimo 8 caracteres, al menos una minuscula, "
				+ "una mayuscula, un numero y un simbolo).");
	}

}
