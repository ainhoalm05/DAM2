package PracticaFinalT1_ALM;

// Importaciones de clases necesarias para manejo de ficheros, colecciones, y utilidades.
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.time.LocalDateTime; // Clase importada automáticamente por su uso en generarTicket
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// Declaración de la clase principal.
public class Main {

    // --- 1. CONSTANTES Y VARIABLES ESTÁTICAS ---
    
    // Expresión regular para validar nombres de plantas o personas.
    static String REGEX_NOMBRE = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";
    // Expresión regular para validar descripciones con letras, espacios, puntos y comas.
    static String REGEX_DECRIP = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ.,\\s]+$";
    // Expresión regular para validar nombres de archivos de fotos.
    static String REGEX_FOTO = "^[a-zA-Z0-9_\\-.]+$";
    // Expresión regular para validar precios (número positivo, opcionalmente con 1 o 2 decimales).
    static String REGEX_PRECIO = "^[1-9][0-9]*([.,][0-9]{1,2})?$";
    // Expresión regular para validar stock (número entero positivo).
    static String REGEX_STOCK = "^[1-9][0-9]*$";
    // Expresión regular para validar ID o código (número entero positivo).
    static String REGEX_ID = "^[0-9]+$"; 
    // Expresión regular compleja para validar contraseñas seguras (al menos 8 caracteres, mayús, minús, número, símbolo).
    static String REGEX_CONTRASEGNA = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\{}\\[\\]|:;\"'<>,.?/]).{8,}$";
    // Expresión regular para validar el cargo (gestor o vendedor).
    static String REGEX_CARGO = "^([g]estor|[v]endedor)$";
    
    // Constantes para las rutas de los directorios y archivos principales.
    private static final String DIR_PLANTAS = "PLANTAS";
    private static final String FICHERO_DAT = "PLANTAS/plantas.dat";
    private static final String FICHERO_BAJA_DAT = "PLANTAS/plantasbaja.dat";
    private static final String DIR_EMPLEADOS = "EMPLEADOS";
    private static final String FICHERO_EMPLEADOS = "EMPLEADOS/empleado.dat";
    private static final String FICHERO_EMPLEADOS_BAJA = "EMPLEADOS/BAJA/empleadosBaja.dat";
    private static final String DIR_TICKETS = "TICKETS";
    private static final String DIR_DEVOLUCIONES = "DEVOLUCIONES";
    
    // Objeto Scanner estático para la entrada de datos por consola.
    static Scanner entrada = new Scanner(System.in);

    // Listas estáticas para almacenar los datos en memoria.
    static List<Plantas> plantasActivas = new ArrayList<Plantas>(); // Almacena plantas activas.
    static List<Plantas> plantasInactivas = new ArrayList<Plantas>(); // Almacena plantas dadas de baja (inactivas).
    static List<Empleado> empleadosActivos = new ArrayList<Empleado>(); // Almacena empleados activos.
    static List<Empleado> empleadosInactivos = new ArrayList<Empleado>(); // Almacena empleados dados de baja (inactivos).
    static List<Ticket> listaCesta = new ArrayList<Ticket>(); // Almacena los ítems de la compra actual (la cesta).


    // --- 2. FUNCIONES UTILITARIAS Y DE BÚSQUEDA ---

    // Busca una planta por su código en la lista de plantas activas.
    public static Plantas buscarPlanta(int codigo) {
        // Itera sobre la lista de plantas activas.
        for (Plantas p : plantasActivas) {
            // Si el código de la planta coincide con el código buscado.
            if (p.getCodigo() == codigo) {
                return p; // Retorna el objeto Planta.
            }
        }
        return null; // Retorna null si no se encuentra la planta.
    }

    // Busca una planta por su código en la lista de plantas inactivas (bajas).
    public static Plantas buscarPlantabaja(int codigo) {
        // Itera sobre la lista de plantas inactivas.
        for (Plantas p : plantasInactivas) {
            // Si el código coincide.
            if (p.getCodigo() == codigo) {
                return p; // Retorna el objeto Planta.
            }
        }
        return null; // Retorna null si no se encuentra.
    }

    // Busca un empleado por su código/ID en la lista de empleados activos.
    public static Empleado buscarEmpleado(int codigo) {
        // Itera sobre la lista de empleados activos.
        for (Empleado e : empleadosActivos) {
            // Si el ID del empleado coincide con el código buscado.
            if (e.getId() == codigo) {
                return e; // Retorna el objeto Empleado.
            }
        }
        return null; // Retorna null si no se encuentra.
    }

    // Busca un empleado por su código/ID en la lista de empleados inactivas (bajas).
    public static Empleado buscarEmpleadobaja(int codigo) {
        // Itera sobre la lista de empleados inactivas.
        for (Empleado e : empleadosInactivos) {
            // Si el ID coincide.
            if (e.getId() == codigo) {
                return e; // Retorna el objeto Empleado.
            }
        }
        return null; // Retorna null si no se encuentra.
    }

    // Genera un ID de empleado único de 4 dígitos (1000-9999).
    public static int generarIdUnico() {
        Random random = new Random(); // Crea un generador de números aleatorios.
        int idNuevo; // Variable para el nuevo ID.
        boolean existe; // Flag para verificar unicidad.

        do {
            idNuevo = 1000 + random.nextInt(9000); // Genera un número entre 1000 y 9999.
            existe = false; // Asume que el ID es único al inicio del bucle.

            // 1. Comprueba si el ID existe en empleados activos.
            for (Empleado e : empleadosActivos) {
                if (e.getId() == idNuevo) {
                    existe = true;
                }
            }
            // 2. Comprueba si el ID existe en empleados dados de baja.
            if (!existe) {
                for (Empleado e : empleadosInactivos) {
                    if (e.getId() == idNuevo) {
                        existe = true;
                    }
                }
            }
        } while (existe); // Repite si el ID generado ya existe.

        return idNuevo; // Retorna el ID único.
    }
    
    // Muestra un resumen de los productos en la cesta de la compra.
    private static void mostrarResumen() {
        System.out.println("\n--- RESUMEN DE COMPRA ---"); // Encabezado.
        float total = 0; // Inicializa el total de la compra.

        // Itera sobre cada ítem (Ticket) en la cesta.
        for (Ticket item : listaCesta) {
            // Busca la planta activa para obtener el nombre.
            Plantas p = buscarPlanta(item.getCodigoProducto());
            // Imprime el nombre, cantidad y subtotal del ítem.
            System.out.printf("%s x%d - %.2f€\n", p.getNombre(), item.getUnidades(), item.getSubtotal());
            total += item.getSubtotal(); // Acumula el subtotal.
        }

        System.out.printf("TOTAL: %.2f€\n", total); // Imprime el total final.
    }

    // --- 3. GESTIÓN DE FICHEROS: LECTURA Y ESCRITURA ---

    // Verifica que la estructura de carpetas y archivos principales exista.
    public static void verificarEstructuraCarpetas() {
        boolean ok = true; // Flag de estado.
        // Array de directorios necesarios.
        String[] directorios = {"PLANTAS", "EMPLEADOS", "EMPLEADOS/BAJA", "TICKETS", "DEVOLUCIONES"};
        // Array de archivos principales cuya existencia se verifica.
        String[] archivos = {"PLANTAS/plantas.xml", "PLANTAS/plantas.dat", "EMPLEADOS/empleado.dat"};

        // Itera sobre los directorios.
        for (String dir : directorios) {
            File carpeta = new File(dir); // Crea objeto File.
            // Si la carpeta no existe.
            if (!carpeta.exists()) {
                System.out.println("Alerta: El directorio '" + dir + "' no existe.");
                ok = false;
            }
        }
        // Itera sobre los archivos.
        for (String file : archivos) {
            File archivo = new File(file); // Crea objeto File.
            // Si el archivo no existe.
            if (!archivo.exists()) {
                System.out.println("Alerta: El archivo '" + file + "' no existe.");
                ok = false;
            }
        }
        // Mensaje de resumen.
        if (ok) {
            System.out.println("Estructura de archivos OK.");
        } else {
            System.err.println("\nEstructura de archivos incompleta. El programa podría fallar.\n");
        }
    }
    
    // Carga los datos de las plantas activas desde plantas.xml y obtiene precio/stock de plantas.dat.
    public static void cargarPlantas() {
        try {
            File ficheroXML = new File("PLANTAS/plantas.xml"); // Referencia al archivo XML.
            File ficheroDat = new File(FICHERO_DAT); // Referencia al archivo de acceso directo.

            // Si los archivos principales no existen, termina la carga.
            if (!ficheroXML.exists() || !ficheroDat.exists()) {
            	return;
            }

            // Configuración y creación del parser DOM.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docB = dbf.newDocumentBuilder();
            Document doc = docB.parse(ficheroXML); // Parseo del XML.
            doc.getDocumentElement().normalize(); // Normaliza el documento.

            NodeList lista = doc.getElementsByTagName("planta"); // Obtiene todos los nodos <planta>.
            
            // Itera sobre la lista de nodos.
            for (int i = 0; i < lista.getLength(); i++) {
                Node nodo = lista.item(i); // Obtiene el nodo actual.
                // Verifica que el nodo sea un elemento.
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element plantas = (Element) nodo; // Casteo a Element.
                    // Obtiene el contenido de cada etiqueta.
                    String codigo = plantas.getElementsByTagName("codigo").item(0).getTextContent();
                    String nombre = plantas.getElementsByTagName("nombre").item(0).getTextContent();
                    String foto = plantas.getElementsByTagName("foto").item(0).getTextContent();
                    String descripcion = plantas.getElementsByTagName("descripcion").item(0).getTextContent();

                    // Crea el objeto Planta.
                    Plantas resultado = new Plantas(Integer.valueOf(codigo), nombre, foto, descripcion);
                    // Obtiene precio y stock accediendo a la posición correspondiente en plantas.dat.
                    resultado.getPrecio(Integer.valueOf(codigo), ficheroDat);
                    resultado.getStock(Integer.valueOf(codigo), ficheroDat);
                    plantasActivas.add(resultado); // Añade a la lista de activas.
                }
            }
            System.out.println("Éxito: Plantas activas cargadas.");
        } catch (SAXException | ParserConfigurationException | IOException e) {
            // Captura errores de parseo o I/O.
            System.err.println("Error: No se pudo leer plantas.xml o plantas.dat.");
            e.printStackTrace();
        }
    }

    // Carga los empleados activos desde empleado.dat (fichero binario serializado).
    public static void cargarEmpleados() {
        ArrayList<Empleado> listaEmpleados = new ArrayList<>(); // Lista temporal.
        File archivo = new File(FICHERO_EMPLEADOS); // Referencia al archivo.

        // Verifica que el archivo exista, sea un archivo y no esté vacío.
        if (archivo.exists() && archivo.isFile() && archivo.length() > 0) {
            // Usa try-with-resources para asegurar el cierre de flujos.
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                // Lee el objeto serializado (el ArrayList completo).
                listaEmpleados = (ArrayList<Empleado>) ois.readObject();
                // Copia los empleados a la lista estática.
                for (Empleado empleado : listaEmpleados) {
                    empleadosActivos.add(new Empleado(empleado.getId(), empleado.getNombre(), empleado.getPassword(), empleado.getCargo()));
                }
                System.out.println("Éxito: Empleados activos cargados.");
            } catch (IOException e) {
                System.err.println("Error: No se pudo leer empleado.dat.");
            } catch (ClassNotFoundException e) {
                System.err.println("Error: Formato de empleado.dat incompatible.");
            }
        } else {
            System.out.println("Alerta: empleado.dat no encontrado o vacío.");
        }
    }

    // Carga los empleados dados de baja desde empleadosBaja.dat.
    public static void leerEmpleadosBaja() {
        ArrayList<Empleado> listaEmpleados = new ArrayList<>(); // Lista temporal.
        File archivo = new File(FICHERO_EMPLEADOS_BAJA); // Referencia al archivo de bajas.

        // Verifica que el archivo exista y tenga contenido.
        if (archivo.exists() && archivo.isFile() && archivo.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                // Lee el ArrayList serializado.
                listaEmpleados = (ArrayList<Empleado>) ois.readObject();
                // Copia los empleados a la lista estática de bajas.
                for (Empleado empleado : listaEmpleados) {
                    empleadosInactivos.add(new Empleado(empleado.getId(), empleado.getNombre(), empleado.getPassword(), empleado.getCargo()));
                }
                System.out.println("Éxito: Empleados de baja cargados.");
            } catch (IOException | ClassNotFoundException e) {
                // Manejo silencioso de errores si el archivo de bajas es nuevo o corrupto.
            }
        }
    }

    // Carga los datos de las plantas dadas de baja desde plantasbaja.xml.
    public static void cargarPlantasBajaXML() {
        plantasInactivas.clear(); // Limpia la lista antes de cargar.
        try {
            File ficheroXML = new File("PLANTAS/plantasbaja.xml"); // Referencia al archivo XML de bajas.
            // Si el archivo no existe o está vacío, termina.
            if (!ficheroXML.exists() || ficheroXML.length() == 0) {
                return;
            }

            // Configuración y creación del parser DOM.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docB = dbf.newDocumentBuilder();
            Document doc = docB.parse(ficheroXML); // Parseo del XML.
            doc.getDocumentElement().normalize();
            NodeList lista = doc.getElementsByTagName("planta"); // Obtiene todos los nodos <planta>.

            // Itera sobre la lista de nodos.
            for (int i = 0; i < lista.getLength(); i++) {
                Node nodo = lista.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element plantas = (Element) nodo;
                    // Se asegura de que los nodos críticos (código, precio, stock) existan.
                    Node codigoNode = plantas.getElementsByTagName("codigo").item(0);
                    Node precioNode = plantas.getElementsByTagName("precio").item(0);
                    Node stockNode = plantas.getElementsByTagName("stock").item(0);
                    
                    if (codigoNode == null || precioNode == null || stockNode == null) continue; // Salta si faltan datos críticos.

                    try {
                        // Obtiene y convierte los datos.
                        int codigo = Integer.parseInt(codigoNode.getTextContent());
                        String nombre = plantas.getElementsByTagName("nombre").item(0).getTextContent();
                        String foto = plantas.getElementsByTagName("foto").item(0).getTextContent();
                        String descripcion = plantas.getElementsByTagName("descripcion").item(0).getTextContent();
                        float precio = Float.parseFloat(precioNode.getTextContent());
                        int stock = Integer.parseInt(stockNode.getTextContent());
        
                        // Crea el objeto y asigna precio/stock (variables de instancia).
                        Plantas p = new Plantas(codigo, nombre, foto, descripcion);
                        p.precio = precio;
                        p.stock = stock;
                        plantasInactivas.add(p); // Añade a la lista de inactivas.
                    } catch (NumberFormatException e) {
                        System.err.println("Error: Formato numérico en plantasbaja.xml, registro " + (i + 1) + ".");
                    }
                }
            }
            System.out.println("Éxito: Plantas de baja cargadas.");
        } catch (Exception e) {
            System.err.println("Error grave al parsear plantasbaja.xml.");
        }
    }
    
    // Guarda (serializa) el ArrayList de empleados activos sobrescribiendo el archivo.
    public static void guardarEmpleados() {
        // Usa try-with-resources para gestionar los flujos.
        try (FileOutputStream fos = new FileOutputStream(FICHERO_EMPLEADOS);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(empleadosActivos); // Escribe el ArrayList completo.
        } catch (IOException e) {
            System.err.println("Error al guardar empleados activos.");
        }
    }

    // Guarda (serializa) el ArrayList de empleados dados de baja sobrescribiendo el archivo.
    public static void guardarEmpleadosBaja() {
        // Usa try-with-resources para gestionar los flujos.
        try (FileOutputStream fos = new FileOutputStream(FICHERO_EMPLEADOS_BAJA);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(empleadosInactivos); // Escribe el ArrayList completo.
        } catch (IOException e) {
            System.err.println("Error al guardar empleados de baja.");
        }
    }

    // Guarda los datos de las plantas activas en el archivo plantas.xml.
    public static void guardarPlantasXML() {
        // Usa try-with-resources para gestionar el escritor de caracteres.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("PLANTAS/plantas.xml"))) {
            // Escribe el encabezado XML.
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.write("<plantas>");
            bw.newLine();

            // Itera sobre la lista de plantas activas.
            for (Plantas p : plantasActivas) {
                // Escribe las etiquetas de cada planta.
                bw.write("  <planta>"); bw.newLine();
                bw.write("    <codigo>" + p.getCodigo() + "</codigo>"); bw.newLine();
                bw.write("    <nombre>" + p.getNombre() + "</nombre>"); bw.newLine();
                bw.write("    <foto>" + p.getFoto() + "</foto>"); bw.newLine();
                bw.write("    <descripcion>" + p.getDescripcion() + "</descripcion>"); bw.newLine();
                bw.write("  </planta>"); bw.newLine();
            }

            bw.write("</plantas>"); // Cierra la etiqueta raíz.
            System.out.println("Éxito: Archivo plantas.xml actualizado.");
        } catch (IOException e) {
            System.err.println("Error al escribir plantas.xml.");
        }
    }
    
    // Guarda los datos de las plantas dadas de baja en el archivo plantasbaja.xml.
    public static void guardarPlantasBajaXML() {
        // Usa try-with-resources.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("PLANTAS/plantasbaja.xml"))) {
            // Escribe el encabezado y la etiqueta raíz.
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.write("<plantasBaja>");
            bw.newLine();

            // Itera sobre la lista de plantas inactivas.
            for (Plantas p : plantasInactivas) {
                // Escribe los datos, incluyendo precio y stock (que solo están en bajas/dat).
                bw.write("  <planta>"); bw.newLine();
                bw.write("    <codigo>" + p.getCodigo() + "</codigo>"); bw.newLine();
                bw.write("    <nombre>" + p.getNombre() + "</nombre>"); bw.newLine();
                bw.write("    <foto>" + p.getFoto() + "</foto>"); bw.newLine();
                bw.write("    <descripcion>" + p.getDescripcion() + "</descripcion>"); bw.newLine();
                bw.write("    <precio>" + p.precio + "</precio>"); bw.newLine();
                bw.write("    <stock>" + p.stock + "</stock>"); bw.newLine();
                bw.write("  </planta>"); bw.newLine();
            }

            bw.write("</plantasBaja>"); // Cierra la etiqueta raíz.
            System.out.println("Éxito: Archivo plantasbaja.xml actualizado.");

        } catch (IOException e) {
            System.err.println("Error al escribir plantasbaja.xml.");
        }
    }

    // Obtiene el siguiente número de ticket consecutivo desde contador.dat.
    public static int obtenerSiguienteNumeroTicket() {
        int numero = 1; // Número por defecto.
        File fichero = new File("TICKETS/contador.dat"); // Referencia al archivo contador binario.
        // Si el archivo existe.
        if (fichero.exists()) {
            // Usa DataInputStream para leer un entero binario.
            try (DataInputStream dis = new DataInputStream(new FileInputStream(fichero))) {
                numero = dis.readInt(); // Lee el número.
            } catch (IOException e) {
                System.err.println("Error: No se pudo leer contador.dat. Usando número 1.");
            }
        }
        return numero; // Retorna el número de ticket.
    }

    // Guarda el número consecutivo del siguiente ticket en contador.dat.
    public static void guardarNumeroTicket(int numero) {
        // Usa DataOutputStream para escribir un entero binario.
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("TICKETS/contador.dat"))) {
            dos.writeInt(numero); // Escribe el nuevo número (siguiente a usar).
        } catch (IOException e) {
            System.err.println("Error al guardar contador.dat.");
        }
    }


    // --- 4. LOGIN Y MENÚS PRINCIPALES ---

    // Maneja el proceso de inicio de sesión de empleados.
    public static void login(List<Empleado> empleados) {

        int intentos = 3; // Límite de intentos.
        
        System.out.println("\n=== INICIAR SESIÓN ===");

        while (intentos > 0) { // Bucle para los intentos.
            
            System.out.print("ID: ");
            String idIngresadoStr = entrada.nextLine().trim(); // Lee el ID como string (renombrado).
            
            int idNumerico = -1; // ID en formato numérico (renombrado).
            boolean idValido = true; // Flag de validación.
            
            try {
                idNumerico = Integer.parseInt(idIngresadoStr); // Intenta convertir a entero (renombrado).
            } catch (NumberFormatException e) {
                System.out.println("Error: ID debe ser numérico.");
                idValido = false; // Marca como inválido.
            }
            
            if (idValido) {
                
                System.out.print("Contraseña: ");
                String passwordIngresada = entrada.nextLine(); // Lee la contraseña (renombrado).
                
                // Itera sobre la lista de empleados activos.
                for (Empleado e : empleados) {
                    
                    // Verifica ID y contraseña.
                    if (e.getId() == idNumerico && passwordIngresada.equals(e.getPassword())) {
                        System.out.println("Login correcto. Bienvenido, " + e.getNombre());
                        
                        // OBTENER Y ESTANDARIZAR CARGO: Convierte el cargo a mayúsculas y quita espacios.
                        String cargoEstandarizado = e.getCargo().trim().toUpperCase(); // (Renombrado).
                        
                        System.out.println(">>> DEBUG: Cargo para el switch: [" + cargoEstandarizado + "]"); 
                        
                        // Control de flujo según el cargo estandarizado.
                        switch (cargoEstandarizado) {
                            case "GESTOR": 
                            case "GESTOR PRINCIPAL": 
                            case "ADMIN": 
                                menuGestor(e); // Llama al menú de gestor.
                                break;
                            case "VENDEDOR":
                                menuVendedor(e); // Llama al menú de vendedor.
                                break;
                            default:
                                System.out.println("Error: Cargo no reconocido (" + e.getCargo().trim() + ").");
                                break;
                        }
                        
                        return; // Sale de la función login tras el acceso. 
                    }
                }
                System.out.println("Error: Credenciales incorrectas.");
            } 
          
            intentos--; // Decrementa intentos.
            System.out.println("Intentos restantes: " + intentos);
        }
        
        System.out.println("Acceso denegado. Máximo de intentos alcanzado.");
    }
    
    // Menú principal para el usuario con cargo de Gestor.
    public static void menuGestor(Empleado empleado) { // Recibe el objeto Empleado (Firma corregida).

        int opcion; // Variable para almacenar la opción seleccionada.

        do {
            // Muestra el encabezado del menú.
            System.out.println("\n=== MENÚ GESTOR (" + empleado.getNombre() + ") ===");
            System.out.println("1) Alta planta");
            System.out.println("2) Baja planta");
            System.out.println("3) Modificar planta");
            System.out.println("4) Recuperar planta");
            System.out.println("5) Alta empleado");
            System.out.println("6) Baja empleado");
            System.out.println("7) Recuperar empleado");
            System.out.println("8) Listar empleados");
            System.out.println("9) Listar catálogo");
            System.out.println("10) Estadísticas");
            System.out.println("0) Salir");
            System.out.print("Opción: ");

            // Bucle de validación para asegurar que la entrada sea un número.
            while (!entrada.hasNextInt()) {
                System.out.println("Error: Introduce un número válido.");
                System.out.print("Opción: ");
                entrada.next(); // Limpia la entrada inválida. 
            }
            opcion = entrada.nextInt(); // Lee la opción.
            entrada.nextLine(); // Consume el salto de línea pendiente.

            boolean errorValidacion = false; // Flag para errores de validación (renombrado).

            switch (opcion) { // Ejecuta la acción según la opción.
            case 1:
                altaPlanta();
                break;
            case 2:
                bajaPlanta();
                break;
            case 3:
                String codigoPlantaStr; // Variable para el código de planta en string (renombrado).

                do {
                    System.out.print("Código de planta a modificar: ");
                    codigoPlantaStr = entrada.next(); // Lee el código.
                    // Valida el formato del código.
                    errorValidacion = !codigoPlantaStr.matches(REGEX_ID);
                    if (errorValidacion) System.out.println("Error: Formato de código incorrecto.");
                } while (errorValidacion);
                
                modificarPlanta(Integer.parseInt(codigoPlantaStr)); // Llama a la función de modificación.
                entrada.nextLine(); // Consumir salto de línea si la función no lo hizo.
                break;
            case 4:
                recuperarPlanta();
                break;
            case 5:
                altaEmpleados(empleadosActivos);
                break;
            case 6:
                bajaEmpleado();
                break;
            case 7:
                recuperarEmpleado();
                break;
            case 8:
                mostrarListaEmpleados();
                break;
            case 9:
                listarCatalogo();
                break;
            case 10:
                estadisticas();
                break;
            case 0:
                System.out.println("Cerrando sesión...");
                break;
            default:
                System.out.println("Error: Opción inválida.");
            }
        } while (opcion != 0); // Repite hasta que la opción sea 0.
    }
    
    // Menú principal para el usuario con cargo de Vendedor.
    public static void menuVendedor(Empleado empleado) {

		int opcion; // Variable para almacenar la opción.
		 do {
	        System.out.println("\n=== MENÚ VENDEDOR (" + empleado.getNombre() + ") ==="); // Muestra el encabezado.
	        System.out.println("1) Listar catálogo");
	        System.out.println("2) Realizar venta");
	        System.out.println("3) Devolución");
	        System.out.println("4) Buscar ticket");
	        System.out.println("0) Cerrar sesión");
	        System.out.print("Opción: ");
	        
            // Bucle de validación para asegurar que la entrada sea un número (CORREGIDO).
	        while (!entrada.hasNextInt()) {
	            System.out.println("Error: Introduce un número válido.");
	            System.out.print("Opción: ");
	            entrada.next(); // Limpia la entrada inválida.
	        }
	        opcion = entrada.nextInt(); // Lee la opción válida.
	        entrada.nextLine(); // Consume el salto de línea pendiente.

			switch (opcion) {
			case 1:
				listarCatalogo();
				break;
			case 2:
				generarVenta(empleado);
				break;
			case 3:
				generarDevolucion();
				break;
			case 4:
				buscarTicket();
				break;
			case 0:
				System.out.println("Cerrando sesión...");
				break;
			default:
				System.out.println("Error: Opción inválida.");
			}
		} while (opcion != 0); // Repite hasta que la opción sea 0.
	}


    // --- 5. GESTIÓN DE PLANTAS ---

    // Muestra por consola las plantas activas e inactivas.
    public static void listarCatalogo() {
        // Muestra plantas activas.
        if (plantasActivas.isEmpty()) {
            System.out.println("No hay plantas activas en el catálogo.");
        } else {
            System.out.println("\n--- PLANTAS ACTIVAS ---");
            for (Plantas p : plantasActivas) {
                System.out.println(p.toString());
            }
        }

        // Muestra plantas dadas de baja.
        if (!plantasInactivas.isEmpty()) {
            System.out.println("\n--- PLANTAS DADAS DE BAJA ---");
            for (Plantas p : plantasInactivas) {
                System.out.println(p.toString());
            }
        }
    }
    
    // Proceso de alta de una nueva planta.
    public static void altaPlanta() {
        boolean errorValidacion = false; // Flag para control de bucles (renombrado).
        String nombreNuevo, descripcionNueva, fotoNueva, precioNuevoStr, stockNuevoStr; // Variables renombradas.

        // 1. Entrada y validación del Nombre.
        do {
            System.out.print("Nombre: ");
            nombreNuevo = entrada.nextLine();
            if (!nombreNuevo.matches(REGEX_NOMBRE)) {
                System.out.println("Error: Formato de nombre incorrecto.");
                errorValidacion = true;
            } else {
                errorValidacion = false;
            }
        } while (errorValidacion);

        // 2. Entrada y validación de la Descripción.
        do {
            System.out.print("Descripción: ");
            descripcionNueva = entrada.nextLine(); // Renombrada.
            if (!descripcionNueva.matches(REGEX_DECRIP)) {
                System.out.println("Error: Formato de descripción incorrecto.");
                errorValidacion = true;
            } else {
                errorValidacion = false;
            }
        } while (errorValidacion);

        // 3. Entrada y validación de la Foto (opcional).
        do {
            System.out.print("Foto (nombre.ext, opcional): ");
            fotoNueva = entrada.nextLine(); // Renombrada.
            // Si no está vacío, valida el formato.
            if (!fotoNueva.isEmpty() && !fotoNueva.matches(REGEX_FOTO)) {
                System.out.println("Alerta: Formato de foto incorrecto. Se guardará sin foto.");
                fotoNueva = ""; // Se fuerza a vacío si el formato es malo.
            }
            errorValidacion = false;
        } while (errorValidacion);

        // 4. Entrada y validación del Precio.
        do {
            System.out.print("Precio: ");
            precioNuevoStr = entrada.nextLine().replace(',', '.'); // Lee y normaliza la coma a punto (renombrado).
            if (!precioNuevoStr.matches(REGEX_PRECIO)) {
                System.out.println("Error: Formato de precio incorrecto.");
                errorValidacion = true;
            } else {
                errorValidacion = false;
            }
        } while (errorValidacion);

        // 5. Entrada y validación del Stock.
        do {
            System.out.print("Stock: ");
            stockNuevoStr = entrada.nextLine(); // Renombrado.
            if (!stockNuevoStr.matches(REGEX_STOCK)) {
                System.out.println("Error: Formato de stock incorrecto.");
                errorValidacion = true;
            } else {
                errorValidacion = false;
            }
        } while (errorValidacion);

        // 6. Asignación de código consecutivo.
        int siguienteCodigo = 1; // Inicializa el código (renombrado).
        if (!plantasActivas.isEmpty()) {
            for (Plantas p : plantasActivas) {
                if (p.getCodigo() >= siguienteCodigo) {
                    siguienteCodigo = p.getCodigo() + 1; // Encuentra el siguiente código disponible.
                }
            }
        }

        // 7. Crear objeto, añadir a la lista y guardar XML.
        Plantas nuevaPlanta = new Plantas(siguienteCodigo, nombreNuevo, fotoNueva, descripcionNueva);
        plantasActivas.add(nuevaPlanta);
        guardarPlantasXML();

        // 8. Guardar precio y stock en plantas.dat (acceso directo).
        File ficheroDat = new File(FICHERO_DAT);
        nuevaPlanta.setPrecio(siguienteCodigo, ficheroDat, Float.parseFloat(precioNuevoStr));
        nuevaPlanta.setStock(siguienteCodigo, ficheroDat, Integer.parseInt(stockNuevoStr));

        System.out.println("\nÉxito: Planta dada de alta. Código asignado: " + siguienteCodigo);
    }
    
    // Menú y lógica para modificar los atributos de una planta existente.
    public static void modificarPlanta(int codigo) {

        File ficheroDat = new File(FICHERO_DAT);
        Plantas plantaModificar = buscarPlanta(codigo);

        // Verifica si la planta existe.
        if (plantaModificar == null) {
            System.out.println("Error: Planta con código " + codigo + " no encontrada.");
            return;
        }

        int opcion;
        do {
            // Muestra el menú de modificación.
            System.out.println("\n=== MODIFICAR PLANTA: " + plantaModificar.getNombre() + " ===");
            System.out.println("1) Nombre");
            System.out.println("2) Descripción");
            System.out.println("3) Foto");
            System.out.println("4) Precio");
            System.out.println("5) Stock");
            System.out.println("0) Salir");
            System.out.print("Opción: ");

            // Valida la entrada numérica.
            while (!entrada.hasNextInt()) {
                System.out.println("Error: Introduce un número válido.");
                System.out.print("Opción: ");
                entrada.next(); 
            }
            opcion = entrada.nextInt();
            entrada.nextLine();

            boolean errorValidacion = false; // Flag para control de bucles (renombrado).

            switch (opcion) {
            case 1:
                String nombreNuevo;
                do {
                    System.out.print("Nuevo nombre: ");
                    nombreNuevo = entrada.nextLine();
                    errorValidacion = !nombreNuevo.matches(REGEX_NOMBRE);
                    if (errorValidacion) System.out.println("Error: Formato incorrecto.");
                } while (errorValidacion);
                plantaModificar.setNombre(nombreNuevo);
                guardarPlantasXML(); // Actualiza el XML tras modificar un campo XML.
                break;

            case 2:
                String descripcionNueva; // Renombrada.
                do {
                    System.out.print("Nueva descripción: ");
                    descripcionNueva = entrada.nextLine();
                    errorValidacion = !descripcionNueva.matches(REGEX_DECRIP);
                    if (errorValidacion) System.out.println("Error: Formato incorrecto.");
                } while (errorValidacion);
                plantaModificar.setDescripcion(descripcionNueva);
                guardarPlantasXML(); // Actualiza el XML tras modificar un campo XML.
                break;

            case 3:
                String fotoNueva; // Renombrada.
                do {
                    System.out.print("Nueva foto (nombre.ext): ");
                    fotoNueva = entrada.nextLine();
                    errorValidacion = !fotoNueva.matches(REGEX_FOTO);
                    if (errorValidacion) System.out.println("Error: Formato incorrecto.");
                } while (errorValidacion);
                plantaModificar.setFoto(fotoNueva);
                guardarPlantasXML(); // Actualiza el XML tras modificar un campo XML.
                break;

            case 4:
                String precioNuevoStr; // Renombrada.
                do {
                    System.out.print("Nuevo precio: ");
                    precioNuevoStr = entrada.nextLine().replace(',', '.');
                    errorValidacion = !precioNuevoStr.matches(REGEX_PRECIO);
                    if (errorValidacion) System.out.println("Error: Formato incorrecto.");
                } while (errorValidacion);
                // Llama al método que modifica el registro directamente en plantas.dat.
                plantaModificar.setPrecio(codigo, ficheroDat, Float.parseFloat(precioNuevoStr));
                System.out.println("Éxito: Precio actualizado.");
                break;

            case 5:
                String stockNuevoStr; // Renombrada.
                do {
                    System.out.print("Nuevo stock: ");
                    stockNuevoStr = entrada.nextLine();
                    errorValidacion = !stockNuevoStr.matches(REGEX_STOCK);
                    if (errorValidacion) System.out.println("Error: Formato incorrecto.");
                } while (errorValidacion);

                int stock = Integer.parseInt(stockNuevoStr);
                // Si el stock es 0, avisa y solo actualiza el DAT. La baja debe ser manual.
                if (stock == 0) {
                    System.out.println("Alerta: Stock cero. Realice la baja desde el menú principal de gestor para reestructurar archivos.");
                    plantaModificar.setStock(codigo, ficheroDat, stock);
                } else {
                    // Actualiza el stock en plantas.dat.
                    plantaModificar.setStock(codigo, ficheroDat, stock);
                    System.out.println("Éxito: Stock actualizado.");
                }
                break;

            case 0:
                System.out.println("Saliendo de modificación.");
                break;
            default:
                System.out.println("Error: Opción inválida.");
            }
        } while (opcion != 0);

    }
    
    // Proceso de baja de una planta (movimiento a inactiva y reestructuración de códigos).
    public static void bajaPlanta() {
        System.out.println("\n=== BAJA DE PLANTA ===");

        boolean errorValidacion = false; // Renombrada.
        String codigo;

        do {
            System.out.print("Código de planta a dar de baja: ");
            codigo = entrada.next();
            errorValidacion = !codigo.matches(REGEX_ID);
            if (errorValidacion) System.out.println("Error: Formato de código incorrecto.");
        } while (errorValidacion);
        entrada.nextLine();

        Plantas plantaBaja = buscarPlanta(Integer.parseInt(codigo));

        if (plantaBaja == null) {
            System.out.println("Error: Planta no encontrada.");
            return;
        }

        // Obtiene el precio y stock actuales del fichero DAT antes de dar de baja.
        File ficheroDat = new File(FICHERO_DAT);
        float precioActual = plantaBaja.getPrecio(Integer.parseInt(codigo), ficheroDat);
        int stockActual = plantaBaja.getStock(Integer.parseInt(codigo), ficheroDat);

        if (precioActual == 0 && stockActual == 0) {
            System.out.println("Alerta: Esta planta ya está dada de baja.");
            return;
        }

        // 1. Guardar en lista de bajas (Copia la planta con el código original).
        Plantas plantaBajaCopia = new Plantas(Integer.parseInt(codigo), plantaBaja.getNombre(), plantaBaja.getFoto(),
                plantaBaja.getDescripcion());
        plantaBajaCopia.precio = precioActual;
        plantaBajaCopia.stock = stockActual;

        plantasInactivas.add(plantaBajaCopia); // Añade a la lista de inactivas (renombrado).
        guardarPlantasBajaXML(); // Guarda el XML de bajas.

        // 2. Guardar en plantasbaja.dat (Fichero binario de bajas).
        File ficheroBaja = new File(FICHERO_BAJA_DAT);
        // Usa RandomAccessFile para añadir al final del fichero.
        try (RandomAccessFile raf = new RandomAccessFile(ficheroBaja, "rw")) {
            raf.seek(raf.length()); // Posiciona el puntero al final.
            raf.writeInt(Integer.parseInt(codigo)); // Escribe código original.
            raf.writeFloat(precioActual); // Escribe precio original.
            raf.writeInt(stockActual); // Escribe stock original.
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo de bajas (plantasbaja.dat).");
            return;
        }

        // 3. Eliminar de la lista activa y reasignar códigos.
        plantasActivas.remove(plantaBaja); // Elimina de la lista activa (renombrado).

        int siguienteCodigo = 1; // Contador para la reasignación (renombrado).
        for (Plantas p : plantasActivas) {
            p.setCodigo(siguienteCodigo); // Reasigna el código.
            siguienteCodigo++;
        }

        guardarPlantasXML(); // Sobrescribe el XML de plantas activas con los nuevos códigos.

        // 4. Reescribir plantas.dat con códigos reasignados.
        try (RandomAccessFile raf = new RandomAccessFile(ficheroDat, "rw")) {
            raf.setLength(0); // Trunca el fichero a longitud cero (lo borra).
            // Itera sobre la lista activa ya reasignada.
            for (Plantas p : plantasActivas) {
                // Escribe el nuevo registro binario.
                raf.writeInt(p.getCodigo());
                raf.writeFloat(p.precio);
                raf.writeInt(p.stock);
            }
        } catch (IOException e) {
            System.err.println("Error al reescribir plantas.dat.");
        }

        System.out.println("\nÉxito: Planta dada de baja correctamente.");
    }
    
    // Proceso para recuperar una planta dada de baja.
    public static void recuperarPlanta() {
        System.out.println("\n=== RECUPERAR PLANTA ===");

        if (plantasInactivas.isEmpty()) { // Verifica si hay plantas de baja (renombrado).
            System.out.println("Alerta: No hay plantas dadas de baja.");
            return;
        }

        boolean errorValidacion = false; // Renombrada.
        String codigo;

        do {
            System.out.print("Código de planta a recuperar: ");
            codigo = entrada.next();
            errorValidacion = !codigo.matches(REGEX_ID);
            if (errorValidacion) System.out.println("Error: Formato de código incorrecto.");
        } while (errorValidacion);
        entrada.nextLine();

        int codigoOriginal = Integer.parseInt(codigo);
        Plantas plantaRecuperar = buscarPlantabaja(codigoOriginal); // Busca en la lista de bajas.

        if (plantaRecuperar == null) {
            System.out.println("Error: Planta no encontrada en bajas.");
            return;
        }

        // Calcula el nuevo código consecutivo más alto.
        int siguienteCodigo = 1; // Renombrada.
        if (!plantasActivas.isEmpty()) {
            for (Plantas p : plantasActivas) {
                if (p.getCodigo() >= siguienteCodigo) {
                    siguienteCodigo = p.getCodigo() + 1;
                }
            }
        }

        System.out.println("Código original: " + codigoOriginal + " → Nuevo código: " + siguienteCodigo);
        System.out.println("Precio recuperado: " + plantaRecuperar.precio + "€");
        System.out.println("Stock recuperado: " + plantaRecuperar.stock);

        System.out.print("¿Confirmar recuperación? (s/n): ");
        String confirmar = entrada.nextLine();

        if (!confirmar.equalsIgnoreCase("s")) {
            System.out.println("Recuperación cancelada.");
            return;
        }

        // 1. Crear planta activa con nuevo código y datos.
        Plantas plantaNueva = new Plantas(siguienteCodigo, plantaRecuperar.getNombre(), plantaRecuperar.getFoto(),
                plantaRecuperar.getDescripcion());
        plantaNueva.precio = plantaRecuperar.precio;
        plantaNueva.stock = plantaRecuperar.stock;

        // 2. Agregar a plantasActivas y guardar XML (ya que los XML se reescriben).
        plantasActivas.add(plantaNueva);
        guardarPlantasXML();

        // 3. Guardar precio y stock en plantas.dat.
        File ficheroDat = new File(FICHERO_DAT);
        // Usa el método para escribir el nuevo registro en la posición del nuevo código.
        plantaNueva.setPrecio(siguienteCodigo, ficheroDat, plantaRecuperar.precio);
        plantaNueva.setStock(siguienteCodigo, ficheroDat, plantaRecuperar.stock);

        // 4. Eliminar de bajas y guardar archivos de bajas (XML y DAT).
        plantasInactivas.remove(plantaRecuperar);
        guardarPlantasBajaXML();

        File ficheroBaja = new File(FICHERO_BAJA_DAT);
        // Reescribe plantasbaja.dat sin el registro recuperado.
        try (RandomAccessFile raf = new RandomAccessFile(ficheroBaja, "rw")) {
            raf.setLength(0); // Trunca el fichero.
            for (Plantas p : plantasInactivas) {
                // Reescribe los registros restantes.
                raf.writeInt(p.getCodigo());
                raf.writeFloat(p.precio);
                raf.writeInt(p.stock);
            }
        } catch (IOException e) {
            System.err.println("Error al reescribir plantasbaja.dat.");
        }

        System.out.println("Éxito: Planta recuperada correctamente. Ahora está activa.");
    }


    // --- 6. GESTIÓN DE EMPLEADOS ---

    // Muestra por consola las listas de empleados activos e inactivos.
    public static void mostrarListaEmpleados() {
        // Muestra activos.
        if (empleadosActivos.isEmpty()) {
            System.out.println("No hay empleados activos en la lista.");
        } else {
            System.out.println("\n--- EMPLEADOS ACTIVOS ---");
            for (Empleado e : empleadosActivos) {
                System.out.println(e.toString());
            }
        }

        // Muestra bajas.
        if (!empleadosInactivos.isEmpty()) {
            System.out.println("\n--- EMPLEADOS DADOS DE BAJA ---");
            for (Empleado e : empleadosInactivos) {
                System.out.println(e.toString());
            }
        }
    }
    
    // Proceso de alta de un nuevo empleado.
    public static void altaEmpleados(List<Empleado> empleados) {
        try {
            System.out.println("\n=== ALTA NUEVO EMPLEADO ===");
            
            // Genera ID único.
            int id = generarIdUnico(); 
            System.out.println("ID generado automáticamente: " + id);

            System.out.print("Nombre: ");
            String nombre = entrada.nextLine().trim();
            System.out.print("Password: ");
            String pass = entrada.nextLine().trim();
            System.out.print("Cargo (vendedor/gestor): ");
            String cargo = entrada.nextLine().trim();

            // Validación de cargo.
            if (!cargo.equalsIgnoreCase("vendedor") && !cargo.equalsIgnoreCase("gestor")) {
                System.out.println("Error: Cargo inválido. Debe ser 'vendedor' o 'gestor'.");
                return;
            }
            // Validación de campos no vacíos.
            if (nombre.isEmpty() || pass.isEmpty()) {
                System.out.println("Error: Nombre y password no pueden estar vacíos.");
                return;
            }

            // Añade el empleado a la lista activa (guarda el cargo en minúsculas).
            empleados.add(new Empleado(id, nombre, pass, cargo.toLowerCase()));
            
            guardarEmpleados(); // Serializa la lista actualizada en empleado.dat.
            System.out.println("Éxito: Empleado " + nombre + " añadido.");

        } catch (NumberFormatException e) {
            System.out.println("Error: ID no es válido (Debería ser autogenerado).");
        }
    }

    // Proceso para dar de baja un empleado.
    public static void bajaEmpleado() {
        System.out.println("\n=== BAJA DE EMPLEADO ===");

        String id;
        boolean errorValidacion; // Renombrada.

        do {
            System.out.print("ID del empleado a dar de baja: ");
            id = entrada.next();
            errorValidacion = !id.matches(REGEX_ID);
            if (errorValidacion) System.out.println("Error: Formato de ID incorrecto.");
        } while (errorValidacion);
        entrada.nextLine();

        Empleado empBaja = buscarEmpleado(Integer.parseInt(id)); // Busca en activos.

        if (empBaja == null) {
            System.out.println("Error: Empleado no encontrado en la lista activa.");
            return;
        }

        // Mueve el empleado de la lista activa a la lista inactiva.
        empleadosInactivos.add(empBaja); // Añade a inactivas (renombrado).
        empleadosActivos.remove(empBaja); // Elimina de activas (renombrado).

        // Guarda ambas listas serializadas.
        guardarEmpleados();
        guardarEmpleadosBaja();

        System.out.println("\nÉxito: Empleado dado de baja correctamente.");
    }

    // Proceso para recuperar un empleado dado de baja.
    public static void recuperarEmpleado() {
        System.out.println("\n=== RECUPERAR EMPLEADO ===");

        if (empleadosInactivos.isEmpty()) { // Verifica lista de inactivas (renombrado).
            System.out.println("Alerta: No hay empleados dados de baja.");
            return;
        }

        System.out.println("\n--- EMPLEADOS EN BAJA ---");
        for (Empleado e : empleadosInactivos) {
            System.out.println(e.toString());
        }

        String id;
        boolean errorValidacion; // Renombrada.

        do {
            System.out.print("\nID del empleado a recuperar: ");
            id = entrada.next();
            errorValidacion = !id.matches(REGEX_ID);
            if (errorValidacion) System.out.println("Error: Formato de ID incorrecto.");
        } while (errorValidacion);
        entrada.nextLine();

        Empleado empRecuperar = buscarEmpleadobaja(Integer.parseInt(id)); // Busca en inactivas.

        if (empRecuperar == null) {
            System.out.println("Error: Empleado no encontrado en bajas.");
            return;
        }

        // Mueve el empleado de la lista inactiva a la lista activa.
        empleadosInactivos.remove(empRecuperar);
        empleadosActivos.add(empRecuperar);

        // Guarda ambas listas serializadas.
        guardarEmpleados();
        guardarEmpleadosBaja();

        System.out.println("\nÉxito: Empleado recuperado y activo.");
    }


    // --- 7. VENTAS, TICKETS Y ESTADÍSTICAS ---

    // Proceso interactivo para registrar una venta.
    public static void generarVenta(Empleado empleado) {
        System.out.println("\n=== GENERAR VENTA ===");

        listaCesta.clear(); // Limpia la lista de la cesta para una nueva venta (renombrado).
        String continuar = null;

        do { // Bucle para añadir productos.
            String codigo, cantidad;
            boolean errorValidacion; // Renombrada.

            // 1. Pide y valida el código.
            do {
                System.out.print("Código de planta: ");
                codigo = entrada.next();
                errorValidacion = !codigo.matches(REGEX_ID);
                if (errorValidacion) System.out.println("Error: Código incorrecto.");
            } while (errorValidacion);

            // 2. Pide y valida la cantidad.
            do {
                System.out.print("Cantidad: ");
                cantidad = entrada.next();
                errorValidacion = !cantidad.matches(REGEX_STOCK);
                if (errorValidacion) System.out.println("Error: Cantidad incorrecta.");
            } while (errorValidacion);
            
            entrada.nextLine(); // Consumir salto de línea.

            Plantas p = buscarPlanta(Integer.parseInt(codigo)); // Busca la planta.

            if (p == null) {
                System.out.println("Error: Planta no encontrada en el catálogo activo.");
                System.out.print("¿Intentar con otro producto? (s/n): ");
                continuar = entrada.nextLine();
                continue; // Vuelve al inicio del bucle Do-While.
            }

            File ficheroDat = new File(FICHERO_DAT);
            int stockActual = p.getStock(Integer.parseInt(codigo), ficheroDat); // Obtiene el stock actual.
            float precioActual = p.getPrecio(Integer.parseInt(codigo), ficheroDat); // Obtiene el precio actual.
            int cantidadInt = Integer.parseInt(cantidad);

            // 3. Verifica stock.
            if (stockActual < cantidadInt) {
                System.out.println("Error: Stock insuficiente. Disponible: " + stockActual);
                System.out.print("¿Intentar con otro producto? (s/n): ");
                continuar = entrada.nextLine();
                continue;
            }

            // 4. Verifica precio (que no esté dada de baja por precio 0).
            if (precioActual == 0) {
                System.out.println("Error: Planta no disponible para venta (precio cero).");
                System.out.print("¿Intentar con otro producto? (s/n): ");
                continuar = entrada.nextLine();
                continue;
            }

            // Añade el ítem a la lista de la cesta.
            listaCesta.add(new Ticket(Integer.parseInt(codigo), cantidadInt, precioActual));
            System.out.println("Éxito: Producto agregado a la cesta.");

            System.out.print("¿Agregar más productos? (s/n): ");
            continuar = entrada.nextLine();

        } while (continuar != null && continuar.equalsIgnoreCase("s"));

        if (listaCesta.isEmpty()) {
            System.out.println("Alerta: Venta cancelada sin productos.");
            return;
        }

        mostrarResumen(); // Muestra el total a pagar.

        System.out.print("\n¿Confirmar venta? (s/n): ");
        String confirmar = entrada.nextLine();

        if (confirmar.equalsIgnoreCase("s")) {
            int numeroTicket = generarTicket(empleado); // Genera y guarda el ticket.

            // Actualizar stock en plantas.dat.
            File ficheroDat = new File(FICHERO_DAT);
            for (Ticket item : listaCesta) {
                Plantas p = buscarPlanta(item.getCodigoProducto());
                int stockActual = p.getStock(item.getCodigoProducto(), ficheroDat);
                int nuevoStock = stockActual - item.getUnidades();

                // Actualiza el stock.
                p.setStock(item.getCodigoProducto(), ficheroDat, nuevoStock);
                
                if (nuevoStock <= 0) {
                    System.out.println("Alerta: Stock agotado para " + p.getNombre() + ". Requiere baja manual.");
                }
            }

            System.out.println("Éxito: Venta realizada. Ticket: " + numeroTicket);
            listaCesta.clear(); // Limpia la cesta.
        } else {
            System.out.println("Venta cancelada.");
            listaCesta.clear();
        }
    }

    // Crea el archivo de texto del ticket y actualiza el contador.
    public static int generarTicket(Empleado empleado) {
        int numeroTicket = obtenerSiguienteNumeroTicket(); // Obtiene el número consecutivo.

        // Usa BufferedWriter para escribir el archivo de texto.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DIR_TICKETS + "/" + numeroTicket + ".txt"))) {

            bw.write("Número Ticket: " + numeroTicket); bw.newLine();
            bw.write("————————————————————————————————"); bw.newLine();
            bw.write("Empleado ID: " + empleado.getId()); bw.newLine();
            bw.write("Nombre: " + empleado.getNombre()); bw.newLine();
            bw.write("Fecha: " + LocalDateTime.now()); bw.newLine(); // Fecha y hora actual.
            bw.write("CodigoProducto Cantidad PrecioUnitario"); bw.newLine();

            float total = 0;
            // Itera sobre la cesta para escribir los ítems.
            for (Ticket item : listaCesta) {
                bw.write(item.getCodigoProducto() + " " + item.getUnidades() + " " + item.getPrecioUnitario());
                bw.newLine();
                total += item.getSubtotal();
            }

            bw.write("————————————————————————————————"); bw.newLine();
            bw.write(String.format("Total: %.2f €", total)); bw.newLine(); // Escribe el total.

            guardarNumeroTicket(numeroTicket + 1); // Incrementa y guarda el contador.

        } catch (IOException e) {
            System.err.println("Error al generar ticket.");
        }

        return numeroTicket; // Retorna el número de ticket generado.
    }
    
    // Proceso para registrar una devolución.
    public static void generarDevolucion() {
        System.out.println("\n=== GENERAR DEVOLUCIÓN ===");

        boolean errorValidacion; // Renombrada.
        String numeroStr;

        // 1. Pide y valida el número de ticket.
        do {
            System.out.print("Número de ticket a devolver: ");
            numeroStr = entrada.next();
            entrada.nextLine();

            errorValidacion = !numeroStr.matches(REGEX_ID);
            if (errorValidacion) System.out.println("Error: Formato de número de ticket incorrecto.");
        } while (errorValidacion);

        int numero = Integer.parseInt(numeroStr);
        File archivoTicket = new File(DIR_TICKETS + "/" + numero + ".txt");

        if (!archivoTicket.exists()) {
            System.out.println("Error: Ticket no encontrado o ya devuelto.");
            return;
        }

        ArrayList<Ticket> items = new ArrayList<>(); // Lista temporal para guardar los ítems del ticket.
        boolean continuar = true;

        // 2. Leer items del ticket.
        try (BufferedReader br = new BufferedReader(new FileReader(archivoTicket))) {
            String linea;
            boolean leyendoItems = false;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("CodigoProducto")) {
                    leyendoItems = true;
                } else if (leyendoItems && linea.startsWith("——")) {
                    leyendoItems = false; 
                } else if (leyendoItems) {
                    // Parsea los datos del ítem (código, cantidad, precio).
                    String[] partes = linea.trim().split("\\s+");
                    if (partes.length == 3) {
                        items.add(new Ticket(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]), Float.parseFloat(partes[2])));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al leer contenido del ticket.");
            continuar = false;
        }

        if (!continuar || items.isEmpty()) {
            System.out.println("Error: No se pudieron procesar los items del ticket.");
            return;
        }

        // 3. Restaurar stock en plantas.dat.
        File ficheroDat = new File(FICHERO_DAT);
        for (Ticket item : items) {
            Plantas p = buscarPlanta(item.getCodigoProducto());
            if (p != null) {
                int stockActual = p.getStock(item.getCodigoProducto(), ficheroDat);
                int nuevoStock = stockActual + item.getUnidades(); // Suma las unidades devueltas.
                p.setStock(item.getCodigoProducto(), ficheroDat, nuevoStock);
            }
        }

        // 4. Mover ticket a DEVOLUCIONES.
        // Usa BufferedReader para leer el original y BufferedWriter para escribir el nuevo.
        try (BufferedReader br = new BufferedReader(new FileReader(archivoTicket));
             BufferedWriter bw = new BufferedWriter(new FileWriter(DIR_DEVOLUCIONES + "/" + numero + ".txt"))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                bw.write(linea); // Copia el contenido.
                bw.newLine();
            }

            bw.write("\n*** DEVUELTO: STOCK RESTAURADO ***"); // Marca de devolución.
            bw.newLine();

            archivoTicket.delete(); // Borra el ticket original de TICKETS.

            System.out.println("\nÉxito: Devolución procesada. Stock restaurado.");

        } catch (IOException e) {
            System.err.println("Error al mover el ticket de devolución.");
        }
    }

    // Proceso para buscar y mostrar un ticket (vendido o devuelto).
    public static void buscarTicket() {
        System.out.println("\n=== BUSCAR TICKET ===");

        boolean errorValidacion = false; // Renombrada.
        String numeroStr;

        // Pide y valida el número de ticket.
        do {
            System.out.print("Número de ticket: ");
            numeroStr = entrada.next();
            entrada.nextLine();

            errorValidacion = !numeroStr.matches(REGEX_ID);
            if (errorValidacion) System.out.println("Error: Formato de número de ticket incorrecto.");
        } while (errorValidacion);

        int numero = Integer.parseInt(numeroStr);
        File archivoTicket = new File(DIR_TICKETS + "/" + numero + ".txt");
        boolean esDevuelto = false;

        // 1. Busca en TICKETS.
        if (!archivoTicket.exists()) {
            // 2. Si no está en TICKETS, busca en DEVOLUCIONES.
            archivoTicket = new File(DIR_DEVOLUCIONES + "/" + numero + ".txt");
            if (!archivoTicket.exists()) {
                System.out.println("Error: Ticket " + numero + " no encontrado.");
                return;
            }
            esDevuelto = true;
        }

        // Muestra el encabezado e intenta leer el archivo.
        System.out.println("\n" + "=".repeat(40));
        System.out.println(esDevuelto ? "TICKET EN DEVOLUCIONES:" : "TICKET EN TICKETS:");
        System.out.println("=".repeat(40));
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivoTicket))) {
            String linea;
            // Imprime el contenido línea por línea.
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo del ticket.");
        }
        System.out.println("=".repeat(40));
    }
    
    // Calcula y muestra las estadísticas de venta (recaudación y planta más vendida).
    public static void estadisticas() {
        System.out.println("\n=== ESTADÍSTICAS DE VENTA ===");

        File carpetaTickets = new File(DIR_TICKETS);
        // Lista todos los archivos .txt en la carpeta TICKETS.
        File[] tickets = carpetaTickets.listFiles((dir, name) -> name.endsWith(".txt"));

        if (tickets == null || tickets.length == 0) {
            System.out.println("Alerta: No hay tickets para procesar.");
            return;
        }

        float totalRecaudado = 0;
        // Array para contar las unidades vendidas por código de planta (asume códigos < 1000).
        int[] contadorPlantas = new int[1000]; 

        // Itera sobre cada archivo de ticket.
        for (File ticket : tickets) {
            try (BufferedReader br = new BufferedReader(new FileReader(ticket))) {
                String linea;
                boolean leyendoItems = false;

                while ((linea = br.readLine()) != null) {
                    // Detecta el inicio de la sección de ítems.
                    if (linea.startsWith("CodigoProducto")) {
                        leyendoItems = true;
                        continue;
                    }
                    // Detecta el final de la sección de ítems.
                    if (leyendoItems && linea.startsWith("——")) {
                        leyendoItems = false;
                        continue;
                    }

                    if (leyendoItems) {
                        // Procesa la línea del ítem.
                        String[] partes = linea.trim().split("\\s+");
                        if (partes.length == 3) {
                            int codigo = Integer.parseInt(partes[0]);
                            int unidades = Integer.parseInt(partes[1]);
                            float precio = Float.parseFloat(partes[2]);

                            // Suma unidades al contador por código.
                            if (codigo < contadorPlantas.length) {
                                contadorPlantas[codigo] += unidades;
                            }
                            totalRecaudado += unidades * precio; // Suma al total.
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                System.err.println("Error: Problema al leer datos de un ticket.");
            }
        }

        System.out.println("\n--- RESULTADOS ---");
        System.out.printf("Total recaudado (Neto): %.2f €\n", totalRecaudado);
        System.out.println("Total de tickets procesados: " + tickets.length);

        int codigoMasVendido = 0;
        int maxVentas = 0;

        // Busca la planta con más ventas en el array contador.
        for (int i = 0; i < contadorPlantas.length; i++) {
            if (contadorPlantas[i] > maxVentas) {
                maxVentas = contadorPlantas[i];
                codigoMasVendido = i;
            }
        }

        // Muestra la planta más vendida.
        if (maxVentas > 0) {
            Plantas p = buscarPlanta(codigoMasVendido);
            System.out.println("\nPlanta más vendida:");
            System.out.println("  - Código: " + codigoMasVendido);
            if (p != null) {
                System.out.println("  - Nombre: " + p.getNombre());
            } else {
                 System.out.println("  - Nombre: (No activa)");
            }
            System.out.println("  - Unidades: " + maxVentas);
        } else {
            System.out.println("No se registraron ventas válidas de plantas.");
        }

        System.out.println("=".repeat(40));
    }


    // --- 8. MAIN ---

    // Método principal de la aplicación.
    public static void main(String[] args) {

        System.out.println("--- INICIANDO SISTEMA DE VIVERO ---");
        
        // 1. Verificación y Carga de datos inicial.
        verificarEstructuraCarpetas();
        cargarPlantas();
        cargarEmpleados();
        leerEmpleadosBaja();
        cargarPlantasBajaXML();

        // Muestra inicial de empleados (solo para fines de depuración/prueba).
        System.out.println("\n--- EMPLEADOS CARGADOS EN MEMORIA (PRUEBAS) ---");
        for (Empleado e : empleadosActivos) {
            System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombre() + " | Cargo: " + e.getCargo());
        }
        System.out.println("----------------------------------------------");

        // 2. Inicia el proceso de login.
        login(empleadosActivos); // Pasa la lista de empleados activos.

    }

}