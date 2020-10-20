package logic;

import javax.swing.ImageIcon;

/*
 * Encapsula la imagen de la celda y el path de todas las imagenes que puede tomar la misma.
 */
public class ImagenC {
	
	private ImageIcon img; // Imagen de la celda
	// Imagenes normales
	private String [] imagenes = new String[]{"/res/0.png","/res/1.png","/res/2.png","/res/3.png","/res/4.png","/res/5.png","/res/6.png","/res/7.png","/res/8.png","/res/9.png"};
	// Imagenes de conflicto
	private String [] imagenesA = new String[]{"/res/0a.png","/res/1a.png","/res/2a.png","/res/3a.png","/res/4a.png","/res/5a.png","/res/6a.png","/res/7a.png","/res/8a.png","/res/9a.png"};
	// Imagen fija
	private String [] imagenesF = new String[]{"/res/0.png","/res/1f.png","/res/2f.png","/res/3f.png","/res/4f.png","/res/5f.png","/res/6f.png","/res/7f.png","/res/8f.png","/res/9f.png"};

	
	// Crea la imagen de la celda en 0
	public ImagenC() throws ProblemWithFilesException {
		try {
			img = new ImageIcon(this.getClass().getResource(imagenes[0]));}
		catch(NullPointerException e){
			throw new ProblemWithFilesException("Faltan Archivos");// Si faltan las imagenes lanzo una excepcion.
		}
	}
	
	// Crea la imagen de la celda en i
	public ImagenC(int i) throws ProblemWithFilesException {
		try {
			img = new ImageIcon(this.getClass().getResource(this.imagenes[i]));}
		catch(NullPointerException e){
			throw new ProblemWithFilesException("Faltan Archivos");// Si faltan las imagenes lanzo una excepcion.
		}
	}
	
	// Retorna la imagen de la celda
	public ImageIcon getImg() {
		return img;
	}
	
	// Setea la imagen asociada a la celda
	public void setImg(int i, boolean a, boolean f) throws ProblemWithFilesException {
		ImageIcon aux = null;
		
		try {
			if (a) { // Si hay conflicto
				aux = new ImageIcon(this.getClass().getResource(imagenesA[i])); // Seteo la img de conflicto
			}
			else if (f) // Si no hay conflicto, veo si esta fija
					aux = new ImageIcon(this.getClass().getResource(imagenesF[i])); // Seteo la imagen fija
				 else aux = new ImageIcon(this.getClass().getResource(imagenes[i])); // Seteo la imagen default.
			}
		catch(NullPointerException e){
			throw new ProblemWithFilesException("Faltan Archivos"); // Si faltan las imagenes lanzo una excepcion.
		}
		
		img.setImage(aux.getImage());
	}
}
