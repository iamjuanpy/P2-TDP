package logic;

/**
 * 
 * Modela un casillero del sudoku.
 *
 */
public class Celda {

	private int numero; // Numero asociado a la celda
	private ImagenC img; // Imagen asociada a la celda
	private boolean conflicto; // Pertenece o no a una fila que tiene un numero repetido
	private boolean fijo; // Es o no uno de los casilleros puestos al azar por el programa
 	
	// Crea una celda con valor 0
	public Celda() throws ProblemWithFilesException {
		numero = 0;
		img = new ImagenC(); // crea la imagen de la celda
		conflicto = false;
		fijo = false;
	}
	
	// Crea una celda con valor i
	public Celda(int i) throws ProblemWithFilesException {
		numero = i;
		img = new ImagenC(i); // crea la imagen de la celda
		conflicto = false;
		fijo = false;
	}
	
	// Setea el numero de la celda
	public void setNumero(int i) throws ProblemWithFilesException {
		numero = i;
		img.setImg(i, conflicto,fijo);
	}
	
	// Cambia el valor de la celda
	public void actualizarCelda() throws ProblemWithFilesException {
		if (!fijo) {
			if (numero == 9) {
				numero = 1;
			} else numero++;
			
			img.setImg(numero,conflicto,fijo);
		}
	}

	// Setea el valor de conflicto
	public void changeConflicto(boolean a) throws ProblemWithFilesException{
		conflicto = a;
		img.setImg(numero, conflicto, fijo);
	}
	
	// Fija la celda
	public void fijar() throws ProblemWithFilesException{
		fijo = true;
		img.setImg(numero, conflicto, fijo);
	}
	

	
	// Getter del valor fijo.
	public boolean getFijo() {
		return fijo;
	}
	
	// Getter de la imagen
	public ImagenC getImgC() {
		return img;
	}
	
	// Getter del numero de la celda.
	public int getNum() {
		return numero;
	}
}
