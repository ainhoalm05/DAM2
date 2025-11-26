package Ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class EjerciciosT2_Parte1 {
	
	static Scanner entrada = new Scanner(System.in);
	
	static void Ejercicio1() {
		String url = "jdbc:mysql://localhost:3306/nba";
		String usuario = "root";
		String passwd = "cfgs";
		
		
		try { 
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conexion = DriverManager.getConnection(url,usuario,passwd);
			
			System.out.println("Que letra quieres buscar?");
			String letra = entrada.nextLine()+'%';
			
			String consulta = "select*from jugadores where Nombre like ?";
			PreparedStatement sentencia = conexion.prepareStatement(consulta);
			sentencia.setString(1, letra);
			
			ResultSet resultado = sentencia.executeQuery();
			
			while (resultado.next()) {
				int codigo = resultado.getInt("codigo");
				String nombre = resultado.getString("nombre");
				String procedencia = resultado.getString("procedencia");
				String altura = resultado.getString("altura");
				int peso = resultado.getInt("peso");
				String posicion = resultado.getString("posicion");
				String equipo = resultado.getString("Nombre_equipo");
				
				System.out.println("-"+codigo+" Nombre: "+nombre+" Procedencia: "+procedencia+" Altura: "+altura+" Peso: "+peso+" Posicion: "+posicion+" Equipo: "+equipo);
			}
			
			
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}
	
	static void Ejercicio2() {
		String url = "jdbc:mysql://localhost:3306/nba";
		String usuario = "root";
		String passwd = "cfgs";
		
		try { 
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conexion = DriverManager.getConnection(url,usuario,passwd);
			
						
			String consulta = "select avg(peso) peso_general FROM jugadores;";
			PreparedStatement sentencia = conexion.prepareStatement(consulta);
		
			
			ResultSet resultado = sentencia.executeQuery();
			
			while (resultado.next()) {
				
				double peso = resultado.getInt("peso_general");
			;
				
				System.out.println("- La media de los pesos de todos los jugadores es: "+peso);
			}
			
			
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	
	}
	
public static void Ejercicio3() {
		
		String url = "jdbc:mysql://localhost:3306/nba";
		
		String usuario="root";
		String contr="cfgs";
		
		ArrayList <String> nombres = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conexion = DriverManager.getConnection(url, usuario, contr);
			int codigo=0;
			String consultacodigo = "select nombre from equipos";
			PreparedStatement sentencia = conexion.prepareStatement(consultacodigo);
			ResultSet resultado = sentencia.executeQuery(); // Cuidado usar libreria Prepares Statement
			while (resultado.next()) {
				String nombre = resultado.getString("Nombre");
				System.out.println(codigo+": "+nombre);
				nombres.add(nombre);
				codigo++;
			}
			
			System.out.println("Introducir el numero del equipo:");
			int numequipo = entrada.nextInt();
			
			String consultaagregarjugador = "select nombre from jugadores where Nombre_equipo=?";
			PreparedStatement sentencia2 = conexion.prepareStatement(consultaagregarjugador);
			sentencia2.setString(1,nombres.get(numequipo));
			
			resultado = sentencia2.executeQuery(); //Cuidado usar libreria Prepares Statement
			while(resultado.next()) {
				String nombre = resultado.getString("Nombre");
				System.out.printf("%s\n",nombre);
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	
public static void Ejercicio4() {
	String url = "jdbc:mysql://localhost:3306/nba";
	
	String usuario="root";
	String contr="cfgs";
	
	try {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection conexion = DriverManager.getConnection(url,usuario,contr);
		int codigo = 1;
		
		String consultacodigo = "select codigo from jugadores order by codigo desc limit 1";
		PreparedStatement sentencia = conexion.prepareCall(consultacodigo);
		ResultSet resultado = sentencia.executeQuery(); //Cuidado usar libreria Prepares Statement
		
		while(resultado.next()) {
			codigo = resultado.getInt("codigo");
		}
		
		System.out.println("Introducir el nombre del nuevo jugador:");
		String nombre = entrada.next();
		
		String consultaagregarjugador = "insert into jugadores (codigo,nombre) values (?,?)";
		PreparedStatement sentencia2 = conexion.prepareCall(consultaagregarjugador);
		ResultSet resulta2 = sentencia.executeQuery(); //Cuidado usar libreria Prepares Statement
		sentencia2.setString(1,(codigo+1));
		sentencia2.setString(2, nombre);
		
		while(resultado.next()) {
			codigo = resultado.getInt("codigo");
			nombre = resultado.getString("Nombre");
			
			System.out.printf("codigo: %d, nombre: %s\n",codigo,nombre);
		}
	}catch(ClassNotFoundException | SQLException e) {
		e.printStackTrace();
	}
	
}

	
	static void Ejercicio5() {
		String url = "jdbc:mysql://localhost:3306/nba";
		String usuario = "root";
		String passwd = "cfgs";
		
		
		try { 
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conexion = DriverManager.getConnection(url,usuario,passwd);
			
			System.out.println("Introduce el codigo del jugador que quieres borrar");
			int codigo = entrada.nextInt();
			
			String consultaBorrado = "delete from jugadores where codigo=?";
			PreparedStatement sentencia = conexion.prepareStatement(consultaBorrado);
			int lineas = sentencia.executeUpdate();
			
			if(lineas>0) {
				System.out.println("Se han borrado las lineas"+lineas);
			}
			
			
		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		String op;
        
        do {
            System.out.println("\n--- EJERCICIOS T2 (PARTE 1) ---");
            System.out.println("1) Buscar por letra");
            System.out.println("2) Calcular peso medio de los jugadores");
            System.out.println("3) Calcular peso medio de los jugadores");
            System.out.println("4) Encontrar el ultimo codigo");
            System.out.println("0) Cerrar sesión");
            System.out.print("Opción: ");
            op = entrada.nextLine().trim(); 
            
            // 'switch' para ejecutar la acción correspondiente
            switch (op) {
                case "1":
                	Ejercicio1(); // Buscar los Jugadores por letra
                   
                    break;
                case "2":
                	Ejercicio2(); //Calcular la media de los pesos de los jugadores
                   
                    break;
                case "3":
                	Ejercicio3();
                	
                    break;
                case "4":
                	Ejercicio4();
                	
                    break;
               
                case "0":
                    System.out.println("Cerrando sesión...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!op.equals("0")); // El bucle se repite mientras la opción NO sea "0"
        
        do {
            System.out.println("\n--- EJERCICIOS T2 (PARTE 2) ---");
            System.out.println("1) Borrar un jugador");
            System.out.println("2) Calcular peso medio de los jugadores");
            System.out.println("3) Calcular peso medio de los jugadores");
            System.out.println("4) Encontrar el ultimo codigo");
            System.out.println("0) Cerrar sesión");
            System.out.print("Opción: ");
            op = entrada.nextLine().trim(); 
            
            // 'switch' para ejecutar la acción correspondiente
            switch (op) {
                case "1":
                	Ejercicio5(); // Buscar los Jugadores por letra
                   
                    break;
                case "2":
                	//Ejercicio6(); //Calcular la media de los pesos de los jugadores
                   
                    break;
                case "3":
                	//Ejercicio7();
                	
                    break;
                case "4":
                	//Ejercicio4();
                	
                    break;
               
                case "0":
                    System.out.println("Cerrando sesión...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!op.equals("0")); // El bucle se repite mientras la opción NO sea "0"
	}

}
