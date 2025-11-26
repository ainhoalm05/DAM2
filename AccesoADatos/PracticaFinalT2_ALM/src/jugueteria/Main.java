package jugueteria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	static Scanner entrada = new Scanner(System.in);

	public static  Connection ConnectionDB() {
		String url = "jdbc:mysql://localhost:3306/practica_final_t2";
		String usuario = "root";
		String password = "cfgs";
		
		Connection connection=null;
		
		try {
			// Cargar driver de BDD
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//Crear conexion
			connection = DriverManager.getConnection(url,usuario,password);
			System.out.println("Nos hemos conectado a la BDD");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return connection;
	}		
	
	// -------------JUGUETES------------//
	
	private static void mostrarTodosLosJuguetes(Connection con) {
		String sql = "SELECT idJuguete, Nombre, Descripcion, Precio, Stock, Categorida FROM juguete;"; 
		try (Statement st = con.createStatement();
			 ResultSet rs = st.executeQuery(sql)) {
			
			System.out.println("\n=======================================================");
			System.out.println("           LISTADO COMPLETO DE JUGUETES");
			System.out.println("=======================================================");
			System.out.printf("%-10s %-20s %-30s %-10s %-10s\n", "ID", "NOMBRE", "PRECIO", "STOCK", "CATEGORIA");
			System.out.println("-------------------------------------------------------------------------------------------------");

			boolean encontrado = false;
			while (rs.next()) {
				encontrado = true;
				System.out.printf("%-5d %-20s %-40s %-10.2f %-10d %-15s\n",
					rs.getInt("idJuguete"), 
					rs.getString("Nombre"),
					rs.getString("Descripcion"),
					rs.getFloat("Precio"), 
					rs.getInt("Stock"), 
					rs.getString("Categorida")
				);
			}
			
			if (!encontrado) {
				System.out.println("No hay juguetes registrados en la base de datos.");
			}
			System.out.println("=======================================================");
			
		} catch (SQLException e) {
			System.err.println("Error SQL al listar juguetes: " + e.getMessage());
		}
	}
	
	private static void registrarJuguete() {
		Connection con = ConnectionDB();
		if (con == null) {
			return;
		}
		try {
			System.out.print("Nombre: ");
			String nombre = entrada.nextLine();
			System.out.print("Descripcion: ");
			String descripcion = entrada.nextLine();
			System.out.print("Precio: ");
			float precio = Float.parseFloat(entrada.nextLine());
			System.out.print("Stock Inicial: ");
			int stock = Integer.parseInt(entrada.nextLine());
			System.out.print("Categoría (Pelotas, Muñecas, Vehiculos): ");
			String categoriaStr = entrada.nextLine();
			
			String consulta = "INSERT INTO juguete (Nombre, Descripcion, Precio, Stock, Categorida)VALUES (?, ?, ?, ?, ?);" ;
						
			PreparedStatement sentencia = con.prepareStatement(consulta);
			sentencia.setString(1, nombre);
			sentencia.setString(2, descripcion);
			sentencia.setFloat(3, precio);
			sentencia.setInt(4, stock);
			sentencia.setString(5, categoriaStr.valueOf(categoriaStr).toString());
		
			int filasAfectadas = sentencia.executeUpdate();
			
			if(filasAfectadas > 0) {
				System.out.println("Se ha añadido el juguete: " + nombre);
				
				// 2. MOSTRAR LA TABLA COMPLETA DE JUGUETES
				mostrarTodosLosJuguetes(con);
			} else {
				System.out.println("No se pudo añadir el juguete.");
			}
			
			sentencia.close();
			
		}catch (Exception e) {
			e.getStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
	ConnectionDB();
	
	 
	 String opcion;
	 
	 do {
         System.out.println("\n===== GESTIÓN JUGUETERÍA (MySQL) =====");
         System.out.println("--- JUGUETES ---");
         System.out.println("1) Registrar juguete");
         System.out.println("2) Modificar juguete");
         System.out.println("3) Eliminar juguete");
         System.out.println("--- EMPLEADOS ---");
         System.out.println("4) Registrar empleado");
         System.out.println("5) Modificar empleado");
         System.out.println("6) Eliminar empleado");
         System.out.println("--- VENTAS Y CAMBIOS ---");
         System.out.println("7) Realizar Venta");
         System.out.println("8) Realizar Devolución");
         System.out.println("--- REPORTES (Consultas) ---");
         System.out.println("9) Juguetes en Stand específico");
         System.out.println("10) Ventas por Mes");
         System.out.println("11) Ventas por Empleado en un Mes");
         System.out.println("12) Productos más vendidos (Top 5)");
         System.out.println("13) Empleados que más venden (Top 5)");
         System.out.println("14) Reporte de Cambios y Motivos");
         System.out.println("15) Lista de Productos por Precio");
         System.out.println("16) Cerrar Aplicación");
         System.out.print("Opción: ");
         opcion = entrada.nextLine().trim();
	 
	 switch(opcion) {
	 case "1":
		 registrarJuguete();
		 break;
	 case "2":
			 
		break;
	 case "3":
		 
		 break;
	 case "4":
		 
		 break;
	 case "5":
		 
		 break;
	 case "6":
		 
		 break;
	 case "7":
		 
		 break;
	 case "8":
		 
		 break;
	 case "9":
		 
		 break;
	 case "10":
		 
		 break;
	 case "11":
		 
		 break;
	 case "12":
		 
		 break;
	 case "13":
		 
		 break;
	 case "14":
		 
		 break;
	 case "15":
		 
		 break;
	 case "16":
		 System.out.println("Cerrando aplicacion....");
		 break;
	 default: 
		 System.out.println("Opción inválida.");
		 
	 }
	 }while(!opcion.equals("0"));
	 
	 
}
}
