package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logic.ProblemWithFilesException;
import logic.Juego;
import logic.Temporizador;

import javax.swing.SwingConstants;
import javax.swing.JTextField;

/**
 * Interfaz grafica del juego.
 * 
 *
 */
public class GUIjuego extends JFrame {
	
	private int tTablero;
	private int tCuadrados;
	private Juego juego;
	private JFrame menu;
	private JPanel contentPane;
	private JPanel grilla;
	private JPanel [][] cuadrantesGrilla;
	private JPanel panelH;
	private JPanel panelT;
	private JButton [][] grillaB;
	private JTextField txtEstado;
	private JButton btnMenu;
	private JButton jugar, reanudar, salir;
	private JLabel t[];
	private Temporizador temp;
	private JLabel tit;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIjuego frame = new GUIjuego(); // Crea la ventana del juego
					frame.menu(); // Crea la ventana del menu
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
 	});
	}

	/**
	 * Create the frame.
	 */
	public GUIjuego() {
		
		try {
			juego = new Juego(); // Inicializa un juego nuevo 
			temp = new Temporizador(this); // Inicializa un nuevo temporizador
		} catch (ProblemWithFilesException e) {
			cartelError(e.getMessage());
		}
		
		tTablero = juego.getTamanoTablero(); // 9
		tCuadrados = juego.getTamanoCuadrado(); // 3
		
		// Propiedades de la ventana del juego
		setTitle("Sudoku by Juan Pablo Sumski");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 585); // 100,100,515,575 o 100,100,525,585 o 100,100,515,585
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
	
		
		//separo donde va a estar el juego
		grilla = new JPanel();
		panelH = new JPanel();
		panelT = new JPanel();
		
		//Formateo la grilla 3x3 para los subcuadrados.
		grilla.setBackground(Color.BLACK);
		grilla.setBounds(3, 3, 503, 503);
		contentPane.add(grilla);
		grilla.setLayout(new GridLayout(3, 3, 3, 3));
		
		// hago los paneles para separarar los 3x3 de botones
		cuadrantesGrilla = new JPanel[tCuadrados][tCuadrados];
		for (int i=0; i < tCuadrados; i++) 
			for (int j=0; j < tCuadrados; j++){
				cuadrantesGrilla[i][j] = new JPanel();
				cuadrantesGrilla[i][j].setLayout(new GridLayout(tCuadrados, tCuadrados, 0, 0));
				grilla.add(cuadrantesGrilla[i][j]);
			}
		
		// Panel para el contador, estado, y boton de menu
		panelH.setBackground(Color.WHITE);
		panelH.setBounds(0, 510, 509, 39);
		contentPane.add(panelH);
		panelH.setLayout(new GridLayout(1, 3, 3, 3));
		
		// Inicializo el campo donde se va a mostrar el estado
		txtEstado = new JTextField();
		txtEstado.setBackground(Color.WHITE);
		txtEstado.setForeground(Color.BLACK);
		txtEstado.setHorizontalAlignment(SwingConstants.CENTER);
		txtEstado.setText("Bienvenido");
		txtEstado.setEditable(false);
		panelH.add(txtEstado);
		txtEstado.setColumns(10);
		
		panelH.add(panelT);
		
		//Inicializo el campo donde se va a mostrar el TImer
		t = new JLabel[5];
		panelT.setLayout(new GridLayout(1,5,5,5));
		panelT.setBackground(Color.WHITE);
		for (int i =0; i< t.length; i++) {
			t[i] = new JLabel();
			
			panelT.add(t[i]);
		}
		
		inicializarBotones();
		
	}
	
	// Inicializa los botones de la ventana del juego
	public void inicializarBotones() {		
		
		// Creo el boton para Jugar/Salir
		OyenteM listenerM = new OyenteM();
		btnMenu = new JButton("Pausa");
		btnMenu.addActionListener(listenerM);
		panelH.add(btnMenu);
		
		// inicializo los botones del tablero y los agrego a su correspondiente 3x3
		grillaB = new JButton[tTablero][tTablero];
		OyenteB listener = new OyenteB();
		
		for (int i=0; i < tTablero;i++)
			for (int j=0; j<tTablero;j++) {
				grillaB[i][j] = new JButton();
				grillaB[i][j].setActionCommand(i+","+j);
				grillaB[i][j].setIcon(juego.getCelda(i, j).getImgC().getImg());
				grillaB[i][j].addActionListener(listener);
				cuadrantesGrilla[i/tCuadrados][j/tCuadrados].add(grillaB[i][j]);
			}
		
	}
	
	// Creo la ventana de menu
	protected void menu(){
		
		// Formateo el menu
		menu = new JFrame();
		menu.setUndecorated(true);
		menu.setTitle("Sudoku");
		menu.setResizable(false);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setBounds(0, 0, 360, 110);
		menu.setLocationRelativeTo(null);
		menu.setVisible(true);
		
		JPanel pane = new JPanel();
		JPanel titulo = new JPanel();
		JPanel botones = new JPanel();
		
		pane.setBackground(Color.WHITE);
		titulo.setBackground(Color.WHITE);
		botones.setBackground(Color.WHITE);
		
		pane.setLayout(new FlowLayout());
		titulo.setLayout(new FlowLayout());
		botones.setLayout(new GridLayout(1,3,2,2));

		menu.setContentPane(pane);
		pane.add(titulo);
		pane.add(botones);
		
		// Pone el logo del juego
		tit = new JLabel();
		try {
			tit.setIcon(new ImageIcon(getClass().getResource("/res/logo.png")));
		}catch(NullPointerException e){
			cartelError("Faltan Archivos");
		}
		titulo.add(tit);
		
		// Pone los botones y el respectivo oyente.
		OyenteM oy = new OyenteM();
		jugar = new JButton("Jugar");jugar.addActionListener(oy);
		reanudar = new JButton("Reanudar");reanudar.addActionListener(oy); reanudar.setEnabled(false);
		salir = new JButton("Salir");salir.addActionListener(oy);
		
		botones.add(jugar);botones.add(reanudar); botones.add(salir);

	}
	
	// Oyente para los casilleros del tablero
	public class OyenteB implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int i,j;
			String m = (String) e.getActionCommand(); // Cada boton del tablero tiene de nombre ("i,j")

			i = Integer.parseInt(Character.toString(m.charAt(0))); // Obtengo i
			j = Integer.parseInt(Character.toString(m.charAt(2))); // Obtengo j
			
			try {
			
				juego.actualizarTablero(i,j); // Cambia de numero la celda y pinta donde hay problemas en el tablero
				
				repaint(); // Actualizo la GUI para mostrar el tablero pintado como se debe
				
				// Si gana
				if (juego.hasWon()) {
					setEnabled(false); // Bloquea el tablero
					txtEstado.setText("¡¡¡ V I C T O R I A !!!"); // Muestra el cartel de victoria
					btnMenu.setText("Menu"); 
					menu.setVisible(true); // Muestra el menu
					reanudar.setEnabled(false); // Desactiva el reanudar
					temp.pausarTiempo(); // Detiene el timer
					tit.setIcon(new ImageIcon(getClass().getResource("/res/win.png")));
					
				}
				
			}
			catch (ProblemWithFilesException e1){
				cartelError(e1.getMessage());
			} catch (NullPointerException e2) {
				cartelError("Faltan Archivos");
			}
		}
		
		
	}
	
	// Oyente para el boton de Menu (ventana de juego) y los botones que están en el menú(Jugar, salir, reanudar)
	public class OyenteM implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String aux = (String) e.getActionCommand(); // Obtengo que botón se usó.
			
			try {
				if (aux.equals("Pausa") || aux.equals("Menu")) { // Si apretó pausa o menu
								
					setEnabled(false); // Se desactiva el tablero
					menu.setVisible(true); // Se muestra el menu
					
					if (aux.equals("Pausa")) { // Si se apretó pausa					
						temp.pausarTiempo(); // Se pausa el timer
						txtEstado.setText("PARTIDA PAUSADA"); // Se informa
					}
		
				}
				else if (aux.equals("Jugar") || aux.equals("Nueva Partida")) {
					
					
					setEnabled(true); // Habilita el juego 
					setVisible(true); // Muestra el juego
					tit.setIcon(new ImageIcon(getClass().getResource("/res/logo.png"))); // Saca el cartel de victoria
					menu.setVisible(false); // Cierra el menu
					reanudar.setEnabled(true); // Activa la opcion de reanudar partida del menu
					jugar.setText("Nueva Partida"); // Cambia de jugar a Nueva partida
					btnMenu.setText("Pausa"); // Habilita la pausa

					if (aux.equals("Nueva Partida")) // Si habia una partida en curso, reinicia el temporizador.
						temp.resetTiempo();
					
					// Inicia el temporizador
					temp.iniciarTiempo();
				
					juego = new Juego(); // Crea la nueva partida
					
					txtEstado.setText("PARTIDA EN CURSO"); // Muestra el estado
						
					// Actualiza los iconos.
					for (int i=0; i < tTablero;i++)
						for (int j=0; j<tTablero;j++) {
							grillaB[i][j].setIcon(juego.getCelda(i, j).getImgC().getImg());
					}
						
				}
				else if (aux.equals("Reanudar")) { // Si reanudó la partida
					 
					menu.setVisible(false); // Se cierra el menu
					
					temp.reanudarTiempo(); // El tiempo vuelve a correr
					txtEstado.setText("PARTIDA EN CURSO"); // Informa que la partida está en curso
					
					setEnabled(true); // Se habilita el juego
					setVisible(true); // Se muestra el juego
					
				}
				else System.exit(1); // Sale si el botón es Salir.

			} catch (ProblemWithFilesException e1) {
				cartelError(e1.getMessage());
			} catch (NullPointerException e2) {
				cartelError("Faltan Archivos");
			}
				
		}
		
	}
	
	// Actualiza las imagenes del temporizador
	public void actualizarTemporizador() {
		
		ImageIcon[] timerArray = temp.getArray(); // Obtiene el arreglo de imagenes
		
		for (int i =0; i< t.length; i++) {
			t[i].setIcon(timerArray[i]); // Pone cada imagen segun corresponda en la posicion del arreglo de labels.
		}
		
		repaint(); // Repinta para que se vean los cambios en pantalla.
	}
	
	// Termina la partida cuando se queda sin tiempo y abre el menu para empezar otra
	public void finTemporizador() {
		setEnabled(false); // Deshabilita el juego
		txtEstado.setText("TE QUEDASTE SIN TIEMPO"); // Informa que te quedaste sin tiempo
		btnMenu.setText("Menu");
		reanudar.setEnabled(false); // Deshabilita el reanudar porque ya perdiste la partida
		menu.setVisible(true); // Muestra el menu
	}
	
	// Muestra un cartel de error si se ejecuta la excepcion de error de archivos. 
	// El parametro es el mensaje de la excepcion.
	public void cartelError(String m) {
		// Si faltan archivos de imagenes muestra un cartel informando.
		// Si el archivo que carga el juego es inválido muestra un cartel informandolo.
		if (m == "Faltan Archivos") 
			JOptionPane.showMessageDialog(contentPane, "Faltan los sprites del juego, deberian estar en "+new File(".").getAbsolutePath()+"res","Sudoku: Error",JOptionPane.ERROR_MESSAGE);
		else JOptionPane.showMessageDialog(contentPane, "El archivo del tablero es inválido o no existe, modificarlo en "+new File(".").getAbsolutePath()+"res","Sudoku: Error",JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}
	

}
