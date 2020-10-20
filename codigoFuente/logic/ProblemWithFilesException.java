package logic;

/**
 * Excepcion para que el Juego lance si faltan los archivos o el archivo del tablero es invalido.
 * 
 *
 */
public class ProblemWithFilesException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public ProblemWithFilesException (String error) {
		super(error);
	}
}
