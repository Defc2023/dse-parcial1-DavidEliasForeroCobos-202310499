package co.edu.uniandes.dse.parcial1.exceptions;

public final class ErrorMessage extends Exception {
    public static final String ESTACION_NOT_FOUND = "The station with the given id was not found";
	public static final String RUTA_NOT_FOUND = "The route with the given id was not found";

    private ErrorMessage() {
		throw new IllegalStateException("Utility class");
    }
}
