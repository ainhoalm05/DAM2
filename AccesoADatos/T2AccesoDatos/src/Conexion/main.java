package Conexion;
import java.sql.*;
public class main {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/mydb";
		String usuario = "root";
		String password = "cfgs";
		
		try {
			// Cargar driver de BDD
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//Crear conexion
			Connection conexion = DriverManager.getConnection(url,usuario,password);
			System.out.println("Nos hemos conectado a la BDD");
			
			//Crear un Statement
			//Statement sentencia = conexion.createStatement();
			//String consulta = "select*from usuario where idUSUARIO=1 or 1=1";
			//ResultSet resultado = sentencia.executeQuery(consulta);
			
			//crear un PreparedStatment
			//String consulta = "select*from usuario where idUSUARIO=? or nombre=?";
			String consulta = "update usuario set nombre=? where idUSUARIO=?";
			PreparedStatement sentencia = conexion.prepareStatement(consulta);
			int numero = 1;
			sentencia.setInt(2, numero); //(lugar, info)
			sentencia.setString(1, "Leo");
			
			//ResultSet resultado = sentencia.executeQuery();
			int resultado = sentencia.executeUpdate();
			
			/*
			//Mostrar resultados
			while(resultado.next()) {
				int idUSUARIO = resultado.getInt("idUSUARIO");
				String nombre = resultado.getString("nombre");
				Date fecha = resultado.getDate("FechaNacimiento");
				String genero = resultado.getString("genero");
				
				System.out.println("idUSUARIO: "+ idUSUARIO+"\nNombre: "+nombre+"\nFecha: "+fecha+"\nGenero: "+genero+ "\n");
			}
			*/
			
			if(resultado > 0) {
				System.out.println("Numero de lineas afectadas:" + resultado);
			}
			
			String consultaTabla = "select*from usuario";
			PreparedStatement sentencia1 = conexion.prepareStatement(consultaTabla);
			ResultSet resultado1 = sentencia1.executeQuery();
			
			while(resultado1.next()) {
				int idUSUARIO = resultado1.getInt("idUSUARIO");
				String nombre = resultado1.getString("nombre");
				Date fecha = resultado1.getDate("FechaNacimiento");
				String genero = resultado1.getString("genero");
				
				System.out.println("idUSUARIO: "+ idUSUARIO+"\nNombre: "+nombre+"\nFecha: "+fecha+"\nGenero: "+genero+ "\n");
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
