package jugueteria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/*
 * TO DO:
 * - Quitar todos los emojis
 * - Revisar funcion de modificar y borrar juguete
 * - Poner en la BDD el borrado en cascada en juguete 
 */

public class Main {
	static Scanner entrada = new Scanner(System.in);

	public static Connection ConnectionDB() {
		String url = "jdbc:mysql://localhost:3306/practica_final_t2";
		String usuario = "root";
		String password = "cfgs";
		
		Connection connection=null;
		
		try {
			// Cargar driver de BDD (Asegúrate de tener el JAR de MySQL Connector/J en el classpath de tu proyecto)
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			
			//Crear conexion
			connection = DriverManager.getConnection(url,usuario,password);
			System.out.println("Nos hemos conectado a la BDD");
		}catch(Exception e) {
			System.err.println("ERROR DE CONEXIÓN: No se ha podido conectar a la BDD.");
			e.printStackTrace();
		}
		return connection;
	}		
	
	// ------------INICIALIZAR------------ //
	
	public static void inicializarZonasYStands() {
		Connection con = ConnectionDB();
		if (con == null) return;
		
		try {
			// Comprobar si ya hay zonas
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM zona");
			rs.next();
			if (rs.getInt(1) > 0) {
				return;
			}
			
			System.out.println("⚙️ Inicializando Zonas y Stands...");
			 // Iniciar transacción
			
			// 1. Insertar Zonas
			PreparedStatement consultaZona = con.prepareStatement("INSERT INTO zona (idzona, Nombre, Descripcion) VALUES (?, ?, ?)");
			consultaZona.setInt(1, 1); consultaZona.setString(2, "Zona Pelotas"); consultaZona.setString(3, "Zona de juegos con pelotas"); consultaZona.executeUpdate();
			consultaZona.setInt(1, 2); consultaZona.setString(2, "Zona Muñecas"); consultaZona.setString(3, "Área de exposición de muñecas"); consultaZona.executeUpdate();
			consultaZona.setInt(1, 3); consultaZona.setString(2, "Zona Vehículos"); consultaZona.setString(3, "Pista de pruebas de vehículos"); consultaZona.executeUpdate();
			consultaZona.close();
			
			// 2. Insertar Stands (vinculados a sus zonas, uno por zona)
			PreparedStatement consultaStand = con.prepareStatement("INSERT INTO stand (idStand, ZONA_idzona, Nombre, Descripcion) VALUES (?, ?, ?, ?)");
			consultaStand.setInt(1, 1); consultaStand.setInt(2, 1); consultaStand.setString(3, "Stand Principal P"); consultaStand.setString(4, "Estantería de entrada"); consultaStand.executeUpdate();
			consultaStand.setInt(1, 2); consultaStand.setInt(2, 2); consultaStand.setString(3, "Stand Vitrina M"); consultaStand.setString(4, "Vitrina de cristal"); consultaStand.executeUpdate();
			consultaStand.setInt(1, 3); consultaStand.setInt(2, 3); consultaStand.setString(3, "Stand Central V"); consultaStand.setString(4, "Expositor central"); consultaStand.executeUpdate();
			consultaStand.close();
			
			System.out.println("✅ Zonas y Stands inicializados correctamente.");
			
		} catch (SQLException e) {
			System.err.println("❌ Error SQL al inicializar Zonas y Stands: " + e.getMessage());
			// Deshacer si falla
		}
		}
	
	
	// ----------------- LISTAR ----------------- //
	
	/**
	 * Muestra los Stands y las Zonas disponibles al usuario.
	 * @param con Conexión a la base de datos.
	 */
	private static void listarZonasYStands(Connection con) {
		System.out.println("\n--- UBICACIONES DISPONIBLES ---");
		
		try {
			// Listar Zonas
			Statement stZona = con.createStatement();
			ResultSet rsZona = stZona.executeQuery("SELECT idzona, Nombre FROM zona");
			System.out.println("\nZONAS (id | Nombre):");
			while (rsZona.next()) {
				System.out.printf("  - %d | %s\n", rsZona.getInt("idzona"), rsZona.getString("Nombre"));
			}
			rsZona.close();
			stZona.close();
			
			// Listar Stands
			Statement stStand = con.createStatement();
			ResultSet rsStand = stStand.executeQuery("SELECT idStand, ZONA_idzona, Nombre FROM stand");
			System.out.println("\nSTANDS (id | Zona ID | Nombre):");
			while (rsStand.next()) {
				System.out.printf("  - %d | %d | %s\n", rsStand.getInt("idStand"), rsStand.getInt("ZONA_idzona"), rsStand.getString("Nombre"));
			}
			rsStand.close();
			stStand.close();
			
		} catch (SQLException e) {
			System.err.println("❌ Error al listar zonas/stands: " + e.getMessage());
		}
	}
	
	private static void mostrarTodosLosJuguetes(Connection con) {
		// Usamos Stock y Categorida según la tabla del usuario
		String consulta = "SELECT idJuguete, Nombre, Descripcion, Precio, Stock, Categorida FROM juguete ORDER BY idJuguete DESC"; 
		try (Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(consulta)) {
			
			System.out.println("\n======================================================================================================================");
			System.out.println("                                      LISTADO COMPLETO DE JUGUETES");
			System.out.println("======================================================================================================================");
			// Añadida DESCRIPCIÓN al encabezado
			System.out.printf("%-5s %-20s %-40s %-10s %-10s %-15s\n", "ID", "NOMBRE", "DESCRIPCIÓN", "PRECIO", "STOCK", "CATEGORIA");
			System.out.println("----------------------------------------------------------------------------------------------------------------------");

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
			System.out.println("======================================================================================================================");
			
		} catch (SQLException e) {
			System.err.println("❌ Error SQL al listar juguetes: " + e.getMessage());
		}
	}
	
	private static void mostrarTodosLosEmpleados(Connection con) {
	    String consulta = "SELECT idEmpleado, Nombre, Cargo, FechaIngreso, activo FROM empleado ORDER BY idEmpleado DESC"; 
	    try (Statement st = con.createStatement();
	            ResultSet rs = st.executeQuery(consulta)) {
	        
	        System.out.println("\n=======================================================================================");
	        System.out.println("                                LISTADO COMPLETO DE EMPLEADOS");
	        System.out.println("=======================================================================================");
	        System.out.printf("%-5s %-25s %-15s %-15s %-10s\n", "ID", "NOMBRE", "CARGO", "INGRESO", "ACTIVO");
	        System.out.println("---------------------------------------------------------------------------------------");

	        boolean encontrado = false;
	        while (rs.next()) {
	            encontrado = true;
	            System.out.printf("%-5d %-25s %-15s %-15s %-10s\n",
	                    rs.getInt("idEmpleado"), 
	                    rs.getString("Nombre"),
	                    rs.getString("Cargo"),
	                    rs.getDate("FechaIngreso"),
	                    rs.getBoolean("activo") ? "Sí" : "No"
	                    );
	        }
	        
	        if (!encontrado) {
	            System.out.println("No hay empleados registrados en la base de datos.");
	        }
	        System.out.println("=======================================================================================");
	        
	    } catch (SQLException e) {
	        System.err.println("Error SQL al listar empleados: " + e.getMessage());
	    }
	}
	// -------------JUGUETES------------ //
		
	private static void registrarJuguete() {
		Connection con = ConnectionDB();
		if (con == null) return;
		
		int idJugueteGenerado = -1; // FIX: Declaración de variable para el ID generado
		
		System.out.println("\n--- 1) REGISTRAR JUGUETE ---");

		try {
			// 1. CAPTURA DE DATOS DEL JUGUETE
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
			
			// 2. CAPTURA DE UBICACIÓN (Stand y Zona)
			listarZonasYStands(con); // Mostrar opciones
			System.out.print("ID de la Zona donde se ubicará: ");
			int idZona = Integer.parseInt(entrada.nextLine());
			System.out.print("ID del Stand donde se ubicará: ");
			int idStand = Integer.parseInt(entrada.nextLine());
			
			// INICIAR TRANSACCIÓN
			
			// --- PASO 1: INSERTAR JUGUETE (Obteniendo el ID generado) ---
			String consultaJuguete = "INSERT INTO juguete (Nombre, Descripcion, Precio, Stock, Categorida) VALUES (?, ?, ?, ?, ?)";
			// FIX: Usar Statement.RETURN_GENERATED_KEYS para obtener el ID
			PreparedStatement psJuguete = con.prepareStatement(consultaJuguete, Statement.RETURN_GENERATED_KEYS);
			
			psJuguete.setString(1, nombre);
			psJuguete.setString(2, descripcion);
			psJuguete.setFloat(3, precio);
			psJuguete.setInt(4, stock); 
			psJuguete.setString(5, categoriaStr.valueOf(categoriaStr).toString());
		
			int filasAfectadas = psJuguete.executeUpdate();
			
			if(filasAfectadas > 0) {
				// Obtener el ID generado
				ResultSet rsKeys = psJuguete.getGeneratedKeys();
				if (rsKeys.next()) {
					idJugueteGenerado = rsKeys.getInt(1);
				}
				rsKeys.close();
				psJuguete.close();
				
				// --- PASO 2: INSERTAR STOCK EN LA UBICACIÓN ESPECÍFICA ---
				String consultaStock = "INSERT INTO stock (STAND_idStand, STAND_ZONA_idzona, JUGUETE_idJuguete, CantidadDisponible) VALUES (?, ?, ?, ?)";
				PreparedStatement psStock = con.prepareStatement(consultaStock);
				
				psStock.setInt(1, idStand);
				psStock.setInt(2, idZona);
				psStock.setInt(3, idJugueteGenerado); // Usamos el ID generado
				psStock.setInt(4, stock);
				
				int filasStock = psStock.executeUpdate();
				psStock.close();
				
				if (filasStock > 0) {
					System.out.println("Juguete '" + nombre + "' (ID: " + idJugueteGenerado + ") registrado y stock asignado al Stand " + idStand + ", Zona " + idZona + ".");
					
					// 3. MOSTRAR LA TABLA COMPLETA DE JUGUETES
					mostrarTodosLosJuguetes(con);
				} else {
					con.rollback(); // Deshacer si la inserción de stock falla
					System.out.println("ERROR: Se registró el juguete, pero falló la asignación de stock. Transacción revertida.");
				}
			} else {
				System.out.println("No se pudo añadir el juguete.");
			}
					
		} catch (SQLException e) {
			System.err.println("ERROR TRANSACCIONAL (JUGUETE + STOCK): " + e.getMessage());
			System.err.println("Error al cerrar conexión: " + e.getMessage());
			
		}
	}
	
	
	private static void modificarJuguete() {
		Connection con = ConnectionDB();
		if (con == null) return;
		
		System.out.println("\n--- 2) MODIFICAR JUGUETE ---");

		try {
			// 1. Mostrar juguetes y pedir ID
			mostrarTodosLosJuguetes(con);
			System.out.print("\nIntroduce el ID del juguete a modificar: ");
			int idJuguete = Integer.parseInt(entrada.nextLine());

			// 2. Buscar datos actuales
			String selectSQL = "SELECT Nombre, Descripcion, Precio, Stock, Categorida FROM juguete WHERE idJuguete = ? and activo = TRUE";
			PreparedStatement psSelect = con.prepareStatement(selectSQL);
			psSelect.setInt(1, idJuguete);
			ResultSet rs = psSelect.executeQuery();

			if (!rs.next()) {
				System.out.println("No se encontró o no hay disponible ningún juguete con ID: " + idJuguete);
				rs.close();
				psSelect.close();
				return;
			}

			// Guardar datos actuales
			String currentNombre = rs.getString("Nombre");
			String currentDescripcion = rs.getString("Descripcion");
			float currentPrecio = rs.getFloat("Precio");
			int currentStock = rs.getInt("Stock"); 
			String currentCategoria = rs.getString("Categorida");
			rs.close();
			psSelect.close();
			
			System.out.println("\nDatos actuales del juguete ID " + idJuguete + ":");
			System.out.println(" - Nombre: " + currentNombre);
			System.out.println(" - Descripción: " + currentDescripcion);
			System.out.printf(" - Precio: %.2f\n", currentPrecio);
			System.out.println(" - Stock (No modificable directamente): " + currentStock);
			System.out.println(" - Categoría: " + currentCategoria);
			System.out.println("\nIntroduce el nuevo valor o **deja vacío** para mantener el actual.");


			// 3. Pedir nuevos datos (o dejar vacío para mantener el actual)
			
			// Nombre
			System.out.print("Nuevo Nombre [" + currentNombre + "]: ");
			String newNombre = entrada.nextLine().trim();
			if (newNombre.isEmpty()) newNombre = currentNombre;

			// Descripcion
			System.out.print("Nueva Descripción [" + currentDescripcion + "]: ");
			String newDescripcion = entrada.nextLine().trim();
			if (newDescripcion.isEmpty()) newDescripcion = currentDescripcion;
			
			// Precio
			float newPrecio = currentPrecio;
			System.out.print("Nuevo Precio [" + String.format("%.2f", currentPrecio) + "]: ");
			String precioStr = entrada.nextLine().trim();
			if (!precioStr.isEmpty()) {
				// FIX: Controlar NumberFormatException si el usuario introduce algo no numérico
				newPrecio = Float.parseFloat(precioStr.replace(',', '.')); // Soporta coma decimal
			}

			// Categoria
			System.out.print("Nueva Categoría (Pelotas, Muñecas, Vehiculos) [" + currentCategoria + "]: ");
			String newCategoria = entrada.nextLine().trim();
			if (newCategoria.isEmpty()) newCategoria = currentCategoria;


			// 4. Ejecutar UPDATE
			String updateSQL = "UPDATE juguete SET Nombre=?, Descripcion=?, Precio=?, Categorida=? WHERE idJuguete=?";
			PreparedStatement psUpdate = con.prepareStatement(updateSQL);
			
			psUpdate.setString(1, newNombre);
			psUpdate.setString(2, newDescripcion);
			psUpdate.setFloat(3, newPrecio);
			psUpdate.setString(4, newCategoria);
			psUpdate.setInt(5, idJuguete);
			
			int filasModificadas = psUpdate.executeUpdate();
			psUpdate.close();
			
			if (filasModificadas > 0) {
				System.out.println("Juguete ID " + idJuguete + " modificado correctamente.");
				mostrarTodosLosJuguetes(con);
			} else {
				System.out.println("No se pudo modificar el juguete (posiblemente no se cambió ningún campo o no existe).");
			}


		} catch (NumberFormatException e) {
			System.err.println("Error de formato: El ID o el Precio deben ser números válidos.");
		} catch (SQLException e) {
			System.err.println("Error SQL al modificar el juguete: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error inesperado: " + e.getMessage());
		} finally {
			try {
				if (con != null) con.close();
			} catch (SQLException e) {
				System.err.println("Error al cerrar conexión: " + e.getMessage());
			}
		}
	}
	
	
	private static void eliminarJuguete() {
		Connection con = ConnectionDB();
		if (con == null) return;
		
		System.out.println("\n--- 3) ELIMINAR JUGUETE ---");
		//En mi caso lo que voy a hcer es crear una columna en "juguetes" en la que se marque si esta activo o no
		//ALTER TABLE juguete ADD activo BIT NOT NULL DEFAULT 1;
		//ALTER TABLE stock ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;
		//ALTER TABLE zona ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;
		
		try {
			// 1. Mostrar juguetes y pedir ID
			mostrarTodosLosJuguetes(con);
			System.out.print("\nIntroduce el ID del juguete a eliminar: ");
			int idJuguete = Integer.parseInt(entrada.nextLine());

			// 2. Confirmación (Opcional, pero buena práctica)
			System.out.print("¿Estás seguro de que quieres eliminar el juguete ID " + idJuguete + "? (s/n): ");
			String confirmacion = entrada.nextLine().trim().toLowerCase();
			
			if (!confirmacion.equals("s")) {
				System.out.println("Eliminación cancelada.");
				return;
			}
			

			String updateJugueteSQL = "UPDATE juguete SET activo = FALSE WHERE idJuguete = ?"; 
			int filasJuguete;
			try (PreparedStatement psUpdateJuguete = con.prepareStatement(updateJugueteSQL)) {
				psUpdateJuguete.setInt(1, idJuguete);
				filasJuguete = psUpdateJuguete.executeUpdate();
			}
	        System.out.println("✅ Juguete ID " + idJuguete + " desatalogado.");

			// 2.2. Actualizar el STOCK asociado al juguete
			// NOTA: Asumo que 'stock' tiene una columna 'idJuguete' o 'Juguete_idJuguete'.
			String updateStockSQL = "UPDATE stock SET activo = FALSE WHERE Juguete_idJuguete = ?"; 
			try (PreparedStatement psUpdateStock = con.prepareStatement(updateStockSQL)) {
				psUpdateStock.setInt(1, idJuguete);
				int filasStock = psUpdateStock.executeUpdate();
				System.out.println("✅ Registros de stock asociados desatalogados: " + filasStock);
			}
			
			// 2.3. Actualizar la ZONA (si la relación es directa)
	        // NOTA: Ajusta la cláusula WHERE según cómo la tabla 'zona' se relacione con 'idJuguete'.
	        // Si la relación es indirecta (por ejemplo, a través de 'stock'), esta lógica debe cambiar.
	        // Asumo aquí una relación directa o que deseas desactivar todas las zonas si se desactiva UN juguete.
	        // Si no estás seguro de la lógica de 'zona', puedes omitir este paso inicialmente.
			String updateZonaSQL = "UPDATE zona SET activo = FALSE WHERE idZona IN "
			                     + "(SELECT idZona FROM stock WHERE Juguete_idJuguete = ?)"; 
			try (PreparedStatement psUpdateZona = con.prepareStatement(updateZonaSQL)) {
				psUpdateZona.setInt(1, idJuguete);
				int filasZona = psUpdateZona.executeUpdate();
				System.out.println("Zonas afectadas desatalogadas: " + filasZona);
			}


			// --- 3. CONFIRMACIÓN Y CIERRE ---
			if (filasJuguete > 0) {
			
				System.out.println("Anulación lógica completada para el juguete ID " + idJuguete + " y sus dependencias.");
				mostrarTodosLosJuguetes(con);
			} else {
				con.rollback(); 
				System.out.println("No se encontró ningún juguete con ID: " + idJuguete + ". Transacción deshecha.");
			}

		} catch (NumberFormatException e) {
			System.err.println("Error de formato: El ID debe ser un número válido.");
		} catch (SQLException e) {
			System.err.println("Error SQL en la transacción: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error inesperado: " + e.getMessage());
		} 

	}
	
	//----------------EMPLEADOS----------------//
	
	//Al igual que antes voy a crear una columna "activo"
	//ALTER TABLE empleado ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;
	
	private static void registrarEmpleado() {
	    Connection con = ConnectionDB();
	    if (con == null) return;
	    
	    System.out.println("\n--- 4) REGISTRAR EMPLEADO ---");
	    
	    try {
	        // 1. CAPTURA DE DATOS
	        System.out.print("Nombre del empleado: ");
	        String nombre = entrada.nextLine();
	        
	        System.out.println("Cargos disponibles: 1) Empleado, 2) Vendedor");
	        System.out.print("Selecciona el cargo (1 o 2): ");
	        String cargoStr;
	        int opcionCargo = Integer.parseInt(entrada.nextLine());
	        
	        if (opcionCargo == 1) {
	            cargoStr = "Empleado";
	        } else if (opcionCargo == 2) {
	            cargoStr = "Vendedor";
	        } else {
	            System.err.println("Opción de cargo inválida. Usando 'Empleado' por defecto.");
	            cargoStr = "Empleado";
	        }
	        
	        // La fecha de ingreso es la actual
	        java.sql.Date fechaIngreso = new java.sql.Date(System.currentTimeMillis());
	        
	        // 2. INSERTAR EMPLEADO (activo = TRUE por defecto)
	        // Usamos Statement.RETURN_GENERATED_KEYS para obtener el ID
	        String consultaEmpleado = "INSERT INTO empleado (Nombre, Cargo, FechaIngreso) VALUES (?, ?, ?)";
	        try (PreparedStatement psEmpleado = con.prepareStatement(consultaEmpleado, Statement.RETURN_GENERATED_KEYS)) {
	            
	            psEmpleado.setString(1, nombre);
	            psEmpleado.setString(2, cargoStr);
	            psEmpleado.setDate(3, fechaIngreso);
	            
	            int filasAfectadas = psEmpleado.executeUpdate();
	            
	            if(filasAfectadas > 0) {
	                int idEmpleadoGenerado = -1;
	                ResultSet rsKeys = psEmpleado.getGeneratedKeys();
	                if (rsKeys.next()) {
	                    idEmpleadoGenerado = rsKeys.getInt(1);
	                }
	                rsKeys.close();
	                
	                System.out.println("Empleado '" + nombre + "' (ID: " + idEmpleadoGenerado + ") registrado con éxito.");
	                mostrarTodosLosEmpleados(con);
	            } else {
	                System.out.println("No se pudo añadir el empleado.");
	            }
	        }
	        
	    } catch (NumberFormatException e) {
	        System.err.println("Error de formato: La opción de cargo debe ser un número válido.");
	    } catch (SQLException e) {
	        System.err.println("Error SQL al registrar empleado: " + e.getMessage());
	    } catch (Exception e) {
	        System.err.println("Error inesperado: " + e.getMessage());
	    
	    }
	}

	public static void main(String[] args) {
	
	 ConnectionDB();	
	 inicializarZonasYStands();
	 
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
		 modificarJuguete();
		 break;
	 case "3":
		 eliminarJuguete();
		 break;
	 case "4":
		 registrarEmpleado();
		 break;
	 case "5":
	 case "6":
	 case "7":
	 case "8":
	 case "9":
	 case "10":
	 case "11":
	 case "12":
	 case "13":
	 case "14":
	 case "15":
		 System.out.println("Función pendiente de implementar.");
		 break;
	 case "16":
		 System.out.println("Cerrando aplicacion....");
		 break;
	 default: 
		 System.out.println("Opción inválida.");
		 
	 }
	 }while(!opcion.equals("16"));
	 
	 
}
}