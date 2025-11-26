package jugueteria;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

	public static  Connection ConnectionDB() {
		String url = "jdbc:mysql://localhost:3306/mydb";
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
	
	/*
	INSERT INTO juguete (Nombre, Descripcion, Precio, Stock, Categorida) 
	VALUES ("Peluche","Blandito y adorable",5.55,2,2);
	*/
	public static void main(String[] args) {
	ConnectionDB();

}
}
