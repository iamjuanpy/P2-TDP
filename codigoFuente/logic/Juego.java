package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Logica principal del sudoku, modela el tablero y chequea la validez de la solución.
 * 
 */
public class Juego {

	private Celda [][] tablero; // Tablero con las casillas
	private String tableroCompleto = "bin/res/tablero2.txt"; // Archivo para llenar el tablero
	
	private int tamanoCTablero = 9; // Cant de filas y columnas del tablero
	private int tamañoCCuadrado = 3; // Cant de filas y columnas de los subcuadros
	
	private boolean [] problemaF; // Arrays para marcar filas, columnas y 3x3 donde haya nums repetidos
	private boolean [] problemaC;
	private boolean [] problemaCuad;
	
	private int num = 81; // casillas para ganar
	private boolean gane;
	
	//Inicializa un tablero de sudoku, cargando los numeros desde un archivo e 
	//inicializando variables auxiliares para verificar la solucion.
	public Juego() throws ProblemWithFilesException {
		tablero = new Celda [tamanoCTablero][tamanoCTablero];
		
		for (int i=0; i < tamanoCTablero ;i++)
			for (int j=0; j < tamanoCTablero ; j++) {
				tablero[i][j] = new Celda(0);
			}
		
		gane = false;
		problemaF = new boolean[tamanoCTablero];
		problemaC = new boolean[tamanoCTablero];
		problemaCuad = new boolean[tamanoCTablero];
		num = 81;

		cargarArchivo();
	}
	
	// Getter de Celda
	public Celda getCelda(int i, int j){
		return tablero[i][j];
	}
	
	// Getter del alto y ancho del tablero
	public int getTamanoTablero() {return tamanoCTablero;}
	
	// Getter del alto y ancho de los subcuadrados.
	public int getTamanoCuadrado() {return tamañoCCuadrado;}
	
	// Getter de gane
	public boolean hasWon() throws ProblemWithFilesException {
		if (gane == true) // Si la partida esta ganada, fija todo el tablero
			for (int i=0; i < tamanoCTablero ;i++)
				for (int j=0; j < tamanoCTablero ; j++) {
					tablero[i][j].fijar();
				}
		return gane; // Devuelve si la partida esta ganada.
	}

	// Prepara la partida
	@SuppressWarnings("unchecked")
	private void cargarArchivo() throws ProblemWithFilesException {
		
		int i,j, numero, cuadr;
		File archivo; 
		Scanner scanner;
		String[] numeros;
		String fila;
		Map<Integer,Integer>[] filas = new HashMap[tamanoCTablero];
		Map<Integer,Integer>[] columnas = new HashMap[tamanoCTablero];
		Map<Integer,Integer>[] cuadrados3x3 = new HashMap[tamanoCTablero];
		
		// Inicializo los 3 arrays de mapas
		for (int c = 0; c < tamanoCTablero; c++) {
			filas[c] = new HashMap<Integer,Integer>();
			columnas[c] = new HashMap<Integer,Integer>();
			cuadrados3x3[c] = new HashMap<Integer,Integer>();
		}
		
		archivo = new File(tableroCompleto);
		try {
			scanner = new Scanner(archivo);		
			i=0;
			while (scanner.hasNextLine()) { // Lee el archivo por filas.
				if (i >= tamanoCTablero) // Si el archivo tiene filas de mas que las que corresponden, no es valido
					throw new ProblemWithFilesException("El archivo no tiene "+tamanoCTablero+" filas");
				
				fila = scanner.nextLine();
				numeros = fila.split(" ");
				
				// Si el archivo tiene columnas de mas o de menos que las que corresponden, no es valido
				if (numeros.length > tamanoCTablero || numeros.length < tamanoCTablero)
					throw new ProblemWithFilesException("El archivo no tiene "+tamanoCTablero+" columnas");
					
				for (j=0; j<tamanoCTablero; j++) {
					
					numero=Integer.parseInt(numeros[j]);
					
					cuadr = getPosCuadrado(i,j); // Obtengo el cuadrado donde está (i,j)
					
					if (numero > tamanoCTablero || numero < 1) // Si el numero no esta entre 1 y 9, el arc no es valido
						throw new ProblemWithFilesException("El archivo tiene un numero invalido");
					
					
					// Si el numero ya estaba en el mapeo de la fila, columna o bloque 3x3 el archivo no es valido.
					if (filas[i].get(numero) != null || columnas[j].get(numero) != null || cuadrados3x3[cuadr].get(numero) != null )
						throw new ProblemWithFilesException("El archivo tiene un numero repetido en una fila/columna/subcuadrado");
					
					// Pongo el numero en los mapeos correspondientes para chequear la validez del archivo.
					filas[i].put(numero,numero);
					columnas[j].put(numero,numero);
					cuadrados3x3[cuadr].put(numero,numero);
					

					// Randomiza que numeros  se van a mostrar y cuales no.
					// Cambiar el 8 por un numero mas bajo para una partida mas facil.
					if (new Random().nextInt(10) >= 8) {
						tablero[i][j].setNumero(numero);
						tablero[i][j].fijar(); // Los numeros ya puestos por la maquina no pueden ser modificados por el jugador.
						num--; // Por cada numero ya puesto, resto 1 al contador total.
					}
				}
				i++;
			}
			
			if (i < tamanoCTablero)
				throw new ProblemWithFilesException("El archivo no tiene "+tamanoCTablero+" filas");
			
		} catch (FileNotFoundException | NumberFormatException e) {
			throw new ProblemWithFilesException(""); // Si el archivo no existe o tiene algun caracter que no sea numero, no es valido.	
		}

	}
	
	// Actualiza el tablero cuando ocurre un movimiento. Llamado cuando clickean en un casillero.
	public void actualizarTablero(int i, int j) throws ProblemWithFilesException {
		
		Map<Integer,Integer> fila = new HashMap<Integer,Integer>();
		Map<Integer,Integer> columna = new HashMap<Integer,Integer>();
		Map<Integer,Integer> cuadrado3x3 = new HashMap<Integer,Integer>();
		int numero = 0 ; int auxiliar; int ax, ay;
		int k = getPosCuadrado(i,j);
		boolean fin = false;
		
		if (tablero[i][j].getNum() == 0) 
			actualizarContador(tablero[i][j]); // Si el numero era 0, lo resto de los espacios vacios
		
		tablero[i][j].actualizarCelda(); // Actualizo la celda con el numero siguiente.
		
		// Si la celda no era fijo, veo si hay problemas.
		
		if (tablero[i][j].getFijo() != true) {
			
			/**
			 * Uso esta variable auxiliar para agregar los espacios vacios al mapeo y 
			 * que estos sumen a la cuenta del mismo, ya que no quiero que marque 
	         * que hay problema si todavia no puse numeros
			 */
			auxiliar = 10; 
			
			// detecta problemas en la fila del elemento actualizado, y si hay lo registra en el array.
			for (int x = 0; x < tamanoCTablero; x++) {
				numero = tablero[i][x].getNum(); // Voy leyendo todos los numeros de la fila
				
				if (fila.get(numero)!= null) // Si estaba repetido, hay un problema
					problemaF[i] = true;
				
				if (numero == 0) // Si el numero es 0, agrega un num distinto para que el mapeo no lo detecte como problema
					numero = numero+auxiliar;
				
				auxiliar++;
				fila.put(numero,numero); // Agrego el numero al mapeo para despues chequear si hay problema
			}
			
			if (fila.size() == tamanoCTablero) // Si el mapeo no tiene repetidos
				problemaF[i] = false; // La fila no tiene problemas
			
			// idem al bloque anterior pero con la columna
			
			auxiliar = 10;
			for (int x = 0; x < tamanoCTablero; x++) {
				numero = tablero[x][j].getNum();
				
				if (columna.get(numero)!= null)
					problemaC[j] = true;
				
				if (numero == 0)
					numero = numero+auxiliar;
				
				auxiliar++;
				columna.put(numero,numero);
			}
			
			if (columna.size() == tamanoCTablero)
				problemaC[j] = false;
			
			//idem pero con un 3x3
			
			auxiliar = 10;
			ax = i - i % tamañoCCuadrado; // Obtengo donde arrancan los subcuadrados
			ay = j - j % tamañoCCuadrado;
			for (int x = ax; x < ax+tamañoCCuadrado; x++) {
				for (int y = ay ; y < ay+tamañoCCuadrado; y++) {
					numero = tablero[x][y].getNum();
					
					if (cuadrado3x3.get(numero) != null)
						problemaCuad[k] = true;
					
					if (numero == 0)
						numero = numero+auxiliar;
					
					auxiliar++;
					cuadrado3x3.put(numero,numero);
				}
			}
			
			if (cuadrado3x3.size() == tamanoCTablero)
				problemaCuad[k] = false;
			
			mostrarProblemas(); // Llamada al metodo que va a cambiar los casilleros de color si es que hay problemas
			
		}
		
		if (num == 0) { // Si se llenaron todas las casillas
			fin = true;
			for (int x =0; x < tamanoCTablero && fin; x++) { // Chequea si no hay problemas
				fin = !(problemaF[x] || problemaC[x] || problemaCuad[x]); 
			}	
		}
		
		if (num == 0 && fin) { // Si todas las casillas tienen numeros y no hay problemas, entonces ganas la partida.
			gane = true;
		}
			
	}
	

	// Reduce el contador de numeros puestos, solo es llamado cada vez que se cambie una casilla que tenia el numero 0.
	private void actualizarContador(Celda c) {
			num--;
	}
	
	
	// Submetodo de actualizarTablero que se encarga de marcar las casillas que tienen problemas y las que no.
	private void mostrarProblemas() throws ProblemWithFilesException{
		
		// Limpio de conflictos todo el tablero
		for (int i= 0; i < tamanoCTablero; i++) {
			for (int j= 0; j < tamanoCTablero; j++){
				tablero[i][j].changeConflicto(false);
			}
		}
		
		// Si hay problemas en la fila, los informo.
		for (int x= 0; x < tamanoCTablero; x++) {
			if (problemaF[x] == true) // Si hay problemas en x fila
				for (int j=0; j < tamanoCTablero; j++) { 
					tablero[x][j].changeConflicto(true); // Pinta todos los numeros de la fila.
				}
		}
		// Idem para las columnas
		for (int x= 0; x < tamanoCTablero; x++) {
			if (problemaC[x] == true)
				for (int i=0; i < tamanoCTablero; i++) {
					tablero[i][x].changeConflicto(true);
				}
		}
		// Idem para los subcuadrados
		for (int x= 0; x<tamanoCTablero; x++) {
			if (problemaCuad[x] == true) {
				int ax = getICuadrado(x);
				int ay = getJCuadrado(x);
				for (int i = ax; i < ax +tamañoCCuadrado; i++)
					for (int j= ay; j < ay +tamañoCCuadrado; j++)
						tablero[i][j].changeConflicto(true);
			}
		}
		
		
	}
	
	// Metodo que obtiene el cuadrado3X3 X que contiene a (I,J)
	private int getPosCuadrado(int i, int j) {
		int auxi, ret;

		if (i<=2)
			auxi = 0;
		else if (i<=5)
			auxi =3;
		else auxi=6;
		
		
		if (j <= 2)
			ret = 0 + auxi;
		else if (j <= 5)
			ret = 1 + auxi;
		else ret = 2 + auxi;
		
		return ret;
	}
	
	
	// Metodo que obtiene la I inicial de un cuadrado3x3 X
	/* 
	 *  Estas serian las posiciones X
	 *  0   1   2
	 *  3   4   5
   	 *  6   7   8
   	 *  
	 * */
	private int getICuadrado(int x) {
		int ret = 0;
		
		if (x == 0 || x == 1 || x == 2)
			ret = 0;
			else if (x == 3 || x == 4 || x == 5)
					ret = 3;
				else ret = 6;
	
		
		return ret;
	}
	
	// Metodo que obtiene la J inicial de un cuadrado3x3 X
	/* 
	 *  Estas serian las posiciones X
	 *  0   1   2
	 *  3   4   5
   	 *  6   7   8
   	 *  
	 * */
	private int getJCuadrado(int x) {
		int ret = 0;
				
		if (x == 0 || x == 3 || x == 6)
			ret = 0;
			else if (x == 1 || x == 4 || x == 7)
					ret = 3;
				else ret = 6;
		
		return ret;
	}
	
	
}
