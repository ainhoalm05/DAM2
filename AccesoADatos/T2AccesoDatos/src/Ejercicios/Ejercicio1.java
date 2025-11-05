package Ejercicios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Ejercicio1 {

	public static void main(String[] args) {
		Scanner entrada = new Scanner(System.in);
		
		String url = "jdbc:mysql://localhost:3306/nba";
		String usuario = "root";
		String password = "cfgs";
		
		try {
			// Cargar driver de BDD
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//Crear conexion
			Connection conexion = DriverManager.getConnection(url,usuario,password);
			System.out.println("Nos hemos conectado a la BDD");
			
			
			String consulta = "select*from jugadores where Nombre like ?";
			PreparedStatement sentencia = conexion.prepareStatement(consulta);
			
			System.out.println("Introduce una letra para buscar: ");
			String letra = entrada.next();
			letra +="%"; //Para que la consulta busque la letra, hay que concatenarle un %
			
			sentencia.setString(1, letra);
			
			ResultSet resultado = sentencia.executeQuery();
			
			while(resultado.next()) {
				int codigo = resultado.getInt("codigo");
				String nombre = resultado.getString("Nombre");
				String procedencia = resultado.getString("Procedencia");
				String altura = resultado.getString("Altura");
				int peso = resultado.getInt("Peso");
				String posicion = resultado.getString("Posicion");
				String nombreEquipo = resultado.getString("Nombre_equipo");



				
				System.out.println("Codigo: "+ codigo+"\nNombre: "+nombre+"\nProcedencia: "+procedencia+"\nAltura: "+altura+ "\nPeso: "+peso+"\nPosicion: "+posicion+"\nNombre del Equipo: "+nombreEquipo+"\n-------------------------------\n");
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}


