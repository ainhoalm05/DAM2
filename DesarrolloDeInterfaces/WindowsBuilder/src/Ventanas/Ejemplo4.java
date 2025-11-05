package Ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Ejemplo4 {
	private JPanel morado;
	private JPanel verde;
	private JPanel rosa;
	
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ejemplo4 window = new Ejemplo4();
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
	public Ejemplo4() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 544, 360);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);
		layeredPane.setLayout(null);
		
		rosa = new JPanel();
		rosa.setBounds(0, 0, 528, 321);
		rosa.setBackground(new Color(255, 128, 128));
		layeredPane.add(rosa);
		rosa.setLayout(null);
		rosa.setVisible(false); // -> EVITA LOS BOTONES FANTASMA, poniendolo en todos los paneles menos el primero
		
		JButton btnNewButton_1 = new JButton("Ir al VERDE");
		btnNewButton_1.setForeground(new Color(128, 255, 128));
		btnNewButton_1.setActionCommand("Ir al VERDE");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rosa.setVisible(false);
				verde.setVisible(true);
			}
		});
		btnNewButton_1.setBounds(96, 138, 128, 23);
		rosa.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Ir al MORADO");
		btnNewButton_2.setForeground(new Color(128, 128, 255));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rosa.setVisible(false);
				morado.setVisible(true);
			}
		});
		btnNewButton_2.setBounds(305, 138, 128, 23);
		rosa.add(btnNewButton_2);
		
		verde = new JPanel();
		layeredPane.setLayer(verde, 2);
		verde.setBounds(0, 0, 528, 321);
		verde.setBackground(new Color(128, 255, 128));
		layeredPane.add(verde);
		verde.setLayout(null);
		
		JButton btnNewButton_4 = new JButton("Ir al MORADO");
		btnNewButton_4.setForeground(new Color(128, 128, 255));
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						verde.setVisible(false);
						morado.setVisible(true);
					}
		});
		btnNewButton_4.setBounds(100, 148, 125, 23);
		verde.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("Ir al ROSA");
		btnNewButton_5.setForeground(new Color(255, 128, 128));
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verde.setVisible(false);
				rosa.setVisible(true);
			}
		});
		btnNewButton_5.setBounds(298, 148, 107, 23);
		verde.add(btnNewButton_5);
		
		morado = new JPanel();
		layeredPane.setLayer(morado, 1);
		morado.setBounds(0, 0, 528, 323);
		morado.setBackground(new Color(128, 128, 255));
		layeredPane.add(morado);
		morado.setLayout(null);
		morado.setVisible(false);
		
		JButton btnNewButton = new JButton("Ir al VERDE");
		btnNewButton.setForeground(new Color(128, 255, 128));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				morado.setVisible(false);
				verde.setVisible(true);
			}
		});
		btnNewButton.setBounds(102, 154, 136, 23);
		morado.add(btnNewButton);
		
		JButton btnNewButton_3 = new JButton("Ir al ROSA");
		btnNewButton_3.setForeground(new Color(255, 128, 128));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				morado.setVisible(false);
				rosa.setVisible(true);
			}
		});
		btnNewButton_3.setBounds(290, 154, 115, 23);
		morado.add(btnNewButton_3);
	}
}
