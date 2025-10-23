package Ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.CardLayout;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class Ejemplo2 {

	private JFrame frame;
	private JTextField textField;
	private int contador=0;
	private JPanel panel_1 = new JPanel();
	private JPanel panel_2 = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ejemplo2 window = new Ejemplo2();
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
	public Ejemplo2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.PINK);
		frame.getContentPane().add(panel, "name_14197492643800");
		panel.setLayout(null);
		
		JButton btnNewButton = new JButton("* CLIK *");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contador++;
				textField.setText(Integer.toString(contador));
			}
		});
		btnNewButton.setForeground(Color.MAGENTA);
		btnNewButton.setBounds(91, 116, 89, 23);
		panel.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Numero de cliks");
		lblNewLabel.setBounds(213, 68, 102, 23);
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(213, 117, 86, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Siguiente");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				panel_1.setVisible(true);
			}
		});
		btnNewButton_1.setForeground(Color.MAGENTA);
		btnNewButton_1.setBounds(335, 227, 89, 23);
		panel.add(btnNewButton_1);
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.CYAN);
		frame.getContentPane().add(panel_1, "name_14197503642800");
		panel_1.setLayout(null);
		
		JButton btnNewButton_2 = new JButton("Atras");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel_1.setVisible(false);
				panel.setVisible(true);
			}
		});
		btnNewButton_2.setForeground(Color.BLUE);
		btnNewButton_2.setBounds(10, 227, 89, 23);
		panel_1.add(btnNewButton_2);
		
		JButton btnNewButton_4 = new JButton("Siguiente");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel_1.setVisible(false);
				panel_2.setVisible(true);
			}
		});
		btnNewButton_4.setForeground(Color.BLUE);
		btnNewButton_4.setBounds(335, 227, 89, 23);
		panel_1.add(btnNewButton_4);
		
		panel_2 = new JPanel();
		panel_2.setBackground(Color.ORANGE);
		frame.getContentPane().add(panel_2, "name_14206866853000");
		panel_2.setLayout(null);
		
		JButton btnNewButton_3 = new JButton("Atras");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel_2.setVisible(false);
				panel_1.setVisible(true);
			}
		});
		btnNewButton_3.setForeground(new Color(255, 128, 0));
		btnNewButton_3.setActionCommand("Atras");
		btnNewButton_3.setBounds(10, 227, 89, 23);
		panel_2.add(btnNewButton_3);
		
		JLabel lblNewLabel_1 = new JLabel("GRACIAS");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(160, 78, 101, 65);
		panel_2.add(lblNewLabel_1);
	}
}
