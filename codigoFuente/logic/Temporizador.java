package logic;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import gui.GUIjuego;

/**
 * Maneja el temporizador de la partida.
 * 
 *
 */
public class Temporizador {
	
	private Timer timer;
	private int segs;
	private int mins;
	private int auxs,auxm;
	private GUIjuego gui;
	private String[] imagenes = new String[]{"/res/0t.png","/res/1t.png","/res/2t.png","/res/3t.png","/res/4t.png","/res/5t.png","/res/6t.png","/res/7t.png","/res/8t.png","/res/9t.png","/res/10t.png"};
	private ImageIcon[] timerArray;
	/*
	 * Guarda en un array, 2 iconos para los minutos, uno para dos puntos, y otros 2 para los segundos.
	 */
	
	public Temporizador(GUIjuego a) throws ProblemWithFilesException {
		gui = a;
		timerArray = new ImageIcon[5];
		try {
			timerArray[0] = new ImageIcon(this.getClass().getResource(this.imagenes[0]));
			timerArray[1] = new ImageIcon(this.getClass().getResource(this.imagenes[0]));
			timerArray[2] = new ImageIcon(this.getClass().getResource(this.imagenes[10]));
			timerArray[3] = new ImageIcon(this.getClass().getResource(this.imagenes[0]));
			timerArray[4] = new ImageIcon(this.getClass().getResource(this.imagenes[0]));
		}catch(NullPointerException e){
			throw new ProblemWithFilesException("Faltan Archivos");
		}
		segs = 0; auxs = 0;
		mins = 0; auxm = 0;
	};
	
	// Arranca a correr el tiempo
	public void iniciarTiempo(){
		timer = new Timer();
		TimerTask task = new TimerTask() {
				
			public void run() {
					
				try {
					pasarAImagen(); // Actualiza el array de las imagenes con los minutos y segundos actuales.
				} catch (ProblemWithFilesException e) { 
					gui.cartelError(e.getMessage()); // Si falta un archivo, la gui muestra un cartel de error
				} 
				gui.actualizarTemporizador(); // La gui se encarga de mostrar esas imagenes del array
					
				// Cada vez que cuenta 60 segs suma un minuto
				if (segs == 59) {
					segs = 0;
					mins++;
				}
				else segs++; // Suma un segundo
				
				if (mins == 90 && segs == 1) { // Si llega a hora y media de juego 
					resetTiempo(); // se detiene el temporizador 
					gui.finTemporizador(); // y termina la partida.
				}
					
			}
				
		};
		timer.schedule(task, 0, 1000); // Cada 1000 ms, avanza un segundo con el run()
	}
	
	// Para el timer y pone los segs y minutos en 0, 
	// no borra el array para que se siga mostrando el tiempo que tardó el jugador en terminar
	public void resetTiempo() {
		
		TimerTask task = new TimerTask() {
			
			public void run() {}
			
		};

		timer.cancel();
		timer = new Timer();
		timer.schedule(task, 1000);
		
		segs = 0;
		mins = 0;
	}
	
	// Para el timer, pero se guarda el tiempo transcurrido para luego retomarlo.
	public void pausarTiempo() {
		
		auxs = segs;
		auxm = mins;
		
		resetTiempo();

	}
	
	// Reanuda el timer
	public void reanudarTiempo() throws ProblemWithFilesException {
		
		// Vuelve a arrancar el timer, pero con los minutos y segundos guardados.
		segs = auxs;
		mins = auxm;
		
		iniciarTiempo();
	}

	// Getter del array para la GUI
	public ImageIcon[] getArray() {
		return timerArray;
	}
	
	// Obtiene cada digito de los minutos y segundos y los pone en el array de imagenes
	private void pasarAImagen() throws ProblemWithFilesException {
		
		// Obtiene los digitos
		int priD = segs %10; 
		int segD = segs /10;
		int terD = mins %10;
		int cuaD = mins /10;
		
		try {
			// Obtiene la imagen del digito
			timerArray[4] = new ImageIcon(this.getClass().getResource(this.imagenes[priD]));
			timerArray[3] = new ImageIcon(this.getClass().getResource(this.imagenes[segD]));
			timerArray[1] = new ImageIcon(this.getClass().getResource(this.imagenes[terD]));
			timerArray[0] = new ImageIcon(this.getClass().getResource(this.imagenes[cuaD]));
		}catch(NullPointerException e){
			throw new ProblemWithFilesException("Faltan Archivos");
		}

	}
	

	
}
