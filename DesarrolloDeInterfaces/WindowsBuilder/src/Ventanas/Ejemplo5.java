package Ventanas;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ejemplo5 {

	private JFrame frame;
	//private BufferedImage fondo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ejemplo5 window = new Ejemplo5();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Ejemplo5() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icono_gato.png")));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.getContentPane().setLayout(null);

		frame.getContentPane().add(buscarImagen());
	
	}
	


	private Component buscarImagen() {
		
		BufferedImage fondo = null;

        // Cargar la imagen
        try {
            fondo = ImageIO.read(new File("src/icono_gato.png")); // asegúrate de que el archivo exista
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Image foto = fondo;
        
        // Crear un JPanel con imagen de fondo
        JPanel panelConFondo = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(foto, 0, 0, 450, 300, null);
            }
        };

        return panelConFondo;
	}
}
