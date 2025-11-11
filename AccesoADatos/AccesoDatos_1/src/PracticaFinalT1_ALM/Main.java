package PracticaFinalT1_ALM;


import java.io.*;
import java.nio.file.*; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*; 
import java.util.concurrent.ThreadLocalRandom; 
import java.util.stream.Collectors; 
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException; 

public class Main {

  
    private static final Scanner sc = new Scanner(System.in);
    private static final int TAMANO = 12; // (int codigo:4 + float precio:4 + int stock:4 = 12)
    private static final String DIR_PLANTAS = "PLANTAS";
    private static final String DIR_EMPLEADOS = "EMPLEADOS";
    private static final String DIR_TICKETS = "TICKETS";
    
    public static void main(String[] args) {
        System.out.println("== INICIO APLICACIÓN VIVERO ==");

        // 1) Comprobación / creación de la jerarquía de carpetas y ficheros necesarios.
        if (!comprobarYCrearEstructura()) {
            System.out.println("Error en estructura de directorios.");
            return;
        }

        // 2) Carga de empleados desde EMPLEADOS/empleados.dat
        // Llama al método que usa ObjectInputStream para deserializar el ArrayList.
        // (Rúbrica 2.3, 2.4)
        List<Empleado> empleados = cargarEmpleados(Paths.get(DIR_EMPLEADOS, "empleados.dat").toFile());
        
        // Si el método devuelve null, hubo un error de lectura o formato (ej: fichero corrupto).
        if (empleados == null) {
            System.out.println("Error al cargar empleados. Saliendo.");
            return; // Termina la aplicación (Rúbrica 2.5)
        }

        // Mostrar empleados antes del login (para pruebas).
        System.out.println("\n=== EMPLEADOS EN SISTEMA ===");
        if (empleados.isEmpty()) { // Si el ArrayList está vacío
            System.out.println("No hay empleados registrados.");
        } else {
            // Recorre la lista de empleados y los muestra
            for (Empleado e : empleados) {
                System.out.println("ID: " + e.getId() +
                        " | Nombre: " + e.getNombre() +
                        " | Cargo: " + e.getCargo() +
                        " | Password (pruebas): " + e.getPassword());
            }
        }
       
        // 2.1/2.2 Carga de datos de Plantas
        // Carga la lista de plantas activas desde el XML. (Rúbrica 2.2)
        List<Plantas> plantas = cargarPlantasDesdeXML(Paths.get(DIR_PLANTAS,"plantas.xml").toFile());
        if (plantas == null) {
            System.err.println("Error cargando plantas desde XML. Saliendo.");
            return; // (Rúbrica 2.5)
        }
        
        // Carga la lista de plantas dadas de baja desde su XML.
        List<Plantas> plantasBaja = cargarPlantasDesdeXML(Paths.get(DIR_PLANTAS, "plantasBaja.xml").toFile()); //Forma mas sencilla de de acceder al archivo con Paths
        if (plantasBaja == null) { 
             System.err.println("Error cargando plantasBaja desde XML. Saliendo.");
            return; // (Rúbrica 2.5)
        }

        // Sincroniza la lista de plantas ACTIVAS con los datos de plantas.dat (precio y stock)
        // (Rúbrica 2.1)
        sincronizarPlantasDat(plantas);

        // 3) Identificación de usuario (login)
        // Llama al método de login, que devuelve el Empleado si es correcto, o null si falla.
        // (Rúbrica 3.1)
        Empleado logeado = login(empleados);
        
        if (logeado == null) { // Si el login falla (3 intentos)
            System.out.println("No se pudo iniciar sesión. Saliendo.");
            return; // Termina la aplicación (Rúbrica 3.3)
        }

        // 3.2 Mostrar menú según cargo del empleado
        // (Rúbrica 3.2)
        if (logeado.getCargo().equalsIgnoreCase("vendedor")) {
            // Si es vendedor, llama al menú de vendedor
            // Pasa la lista de bajas por si una venta provoca una baja automática
            menuVendedor(logeado, plantas, plantasBaja);
        } else if (logeado.getCargo().equalsIgnoreCase("gestor")) {
            // Si es gestor, carga también la lista de empleados de baja
            List<Empleado> empleadosBaja = cargarEmpleados(Paths.get(DIR_EMPLEADOS, "BAJA", "empleadosBaja.dat").toFile());
            // Llama al menú de gestor
            menuGestor(logeado, plantas, plantasBaja, empleados, empleadosBaja);
        } else {
            // Si el cargo no es reconocido
            System.out.println("Cargo desconocido. Contacte con administración.");
        }

        // ---- FIN DE LA EJECUCIÓN DEL MENÚ ----
        // El código llega aquí cuando el Vendedor o Gestor cierran sesión.

        // Guardar cambios finales antes de salir:
        // Guarda la lista de plantas activas (en memoria) en el fichero plantas.xml
        guardarPlantasXML(plantas, Paths.get(DIR_PLANTAS, "plantas.xml").toFile());
        // Guarda la lista de plantas de baja (en memoria) en el fichero plantasBaja.xml
        guardarPlantasXML(plantasBaja, Paths.get(DIR_PLANTAS, "plantasBaja.xml").toFile());
        
        // Guarda la lista de empleados activos (en memoria) en empleados.dat
        guardarEmpleados(empleados, Paths.get(DIR_EMPLEADOS, "empleados.dat").toFile());
        // Nota: empleadosBaja.dat se guarda al salir del menú gestor.

        System.out.println("== FIN APLICACIÓN ==");
    }

    // ------------------------ 1. Estructura y comprobaciones ------------------------

    /**
     * Comprueba y crea la jerarquía de carpetas y ficheros necesarios:
     * PLANTAS, EMPLEADOS, EMPLEADOS/BAJA, TICKETS, DEVOLUCIONES y ficheros básicos.
     * (Rúbrica 1.4)
     */
    private static boolean comprobarYCrearEstructura() {
        try {
            // Creamos (si no existen) las carpetas base necesarias.
            Files.createDirectories(Path.of(DIR_PLANTAS));
            Files.createDirectories(Path.of(DIR_EMPLEADOS));
            Files.createDirectories(Path.of(DIR_EMPLEADOS, "BAJA"));
            Files.createDirectories(Path.of(DIR_TICKETS));
            Files.createDirectories(Path.of("DEVOLUCIONES"));

            // Aseguramos que los ficheros obligatorios estén presentes (se crean vacíos si faltan).
            // (Rúbrica 1.1, 1.2, 1.3)
            crearFicheroSiNoExiste(Path.of(DIR_PLANTAS, "plantas.xml"));
            crearFicheroSiNoExiste(Path.of(DIR_PLANTAS, "plantasBaja.xml"));
            crearFicheroSiNoExiste(Path.of(DIR_PLANTAS, "plantas.dat"));
            crearFicheroSiNoExiste(Path.of(DIR_PLANTAS, "plantasbaja.dat")); // (Rúbrica 8.1.2.2)
            crearFicheroSiNoExiste(Path.of(DIR_EMPLEADOS, "empleados.dat"));
            crearFicheroSiNoExiste(Path.of(DIR_EMPLEADOS, "BAJA", "empleadosBaja.dat"));

            return true; // todo correcto
        } catch (IOException e) {
            // Control de errores si no tenemos permisos o hay problema I/O (Rúbrica 1.5)
            System.err.println("Error creando estructura: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método de utilidad para crear un fichero vacío si no existe.
     * @param p El Path (ruta) del fichero a crear.
     */
    private static void crearFicheroSiNoExiste(Path p) throws IOException {
        File f = p.toFile(); // Convierte el Path a un objeto File
        if (!f.exists()) { // Si el fichero NO existe
            f.createNewFile(); // Lo crea vacío
        }
    }

    // ------------------------ 2. Carga de datos ------------------------

    /**
     * Lee empleados desde empleados.dat (serializado ArrayList<Empleado>).
     * ESTE MÉTODO USA LA LÓGICA DE ObjectInputStream (como en Ejemplo1.LecturaPersonas).
     * (Rúbrica 2.3, 2.4)
     */
  
    private static ArrayList<Empleado> cargarEmpleados(File f) {
        // Si no existe o está vacío (longitud 0) devuelve una lista vacía nueva
        if (!f.exists() || f.length() == 0) return new ArrayList<>();
        
        // Usamos try-with-resources para asegurar que el ObjectInputStream se cierra
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            
            // Lo pasamos a Objeto para poder trabajar conel debido al tipo de funcion que tengo
            Object o = ois.readObject();
                // Hacemos el cast y lo devolvemos
                return (ArrayList<Empleado>) o;
            
        } catch (Exception e) { 
            System.err.println("Error leyendo " + f.getName() + ": " + e.getMessage());
            return null; // (Rúbrica 2.5)
        }
    }

    /**
     * Sobreescribe el fichero .dat con la lista de empleados proporcionada.
     * (Rúbrica 8.2.1.3, 8.2.2.3)
     */
    private static void guardarEmpleados(List<Empleado> lista, File f) { //Lista de la que partimos, fichero final
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {  //Hacer legible la clase Empleado        
            // Escribimos la lista completa como un solo objeto en el fichero
            oos.writeObject(new ArrayList<>(lista));
        } catch (IOException e) { // Si hay un error de escritura
            System.err.println("Error guardando " + f.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Carga plantas desde un fichero XML (solo campos descriptivos).
     * (Rúbrica 2.2)
     */
    private static ArrayList<Plantas> cargarPlantasDesdeXML(File xml) {
        ArrayList<Plantas> salida = new ArrayList<>(); // Lista de salida
        // Si el XML no existe o está vacío, devolvemos la lista vacía
        if (!xml.exists() || xml.length() == 0) {
        	return salida;
        }

        try {
            // 1. Crear un DocumentBuilder (el "parseador")
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // 2. Parsear el fichero XML y cargarlo en un objeto Document (DOM)
            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize(); // Normaliza la estructura del XML
            
            // 3. Obtener una lista de todos los nodos (elementos) con la etiqueta "planta"
            NodeList nl = doc.getElementsByTagName("planta");
            
            // 4. Recorrer la lista de nodos "planta"
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i); // Obtener el nodo actual
                
                // Asegurarse de que el nodo es de tipo Elemento (no texto, etc.)
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) node; // Convertir el Nodo a Elemento
                    
                    // 5. Leer el contenido de las sub-etiquetas (codigo, nombre, etc.)
                    // Usamos el método de utilidad getTagText
                    int codigo = Integer.parseInt(getTagText(el, "codigo", "0"));
                    String nombre = getTagText(el, "nombre", "");
                    String foto = getTagText(el, "foto", "");
                    String descripcion = getTagText(el, "descripcion", "");
                    
                    // 6. Crear el objeto Plantas (con precio/stock 0 temporalmente)
                    salida.add(new Plantas(codigo, nombre, foto, descripcion, 0f, 0));
                }
            }
            return salida; // Devolver la lista llena
        } catch (ParserConfigurationException | SAXException | IOException e) {
            // Si hay un error de parseo (XML mal formado, etc.)
            System.err.println("Error leyendo XML ("+xml.getName()+"): " + e.getMessage());
            return null; // (Rúbrica 2.5)
        }
    }

    /**
     * Método de utilidad para leer el texto de una sub-etiqueta dentro de un Elemento XML.
     * @param parent El Elemento "padre" (ej: <planta>)
     * @param tag La etiqueta "hija" (ej: <nombre>)
     * @param def Valor por defecto si la etiqueta no se encuentra.
     * @return El texto de la etiqueta hija.
     */
    private static String getTagText(Element parent, String tag, String def) {
        // Obtiene la lista de nodos con esa etiqueta (ej: <nombre>)
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl.getLength() == 0) return def; // Si no hay, devuelve el valor por defecto
        // Si existe, devuelve el contenido de texto del primer elemento
        return nl.item(0).getTextContent();
    }

    /**
     * Guarda la lista de plantas en un fichero XML (reescribe completamente el fichero).
     * @param lista La lista de plantas a guardar.
     * @param xml El fichero XML (plantas.xml o plantasBaja.xml) donde guardar.
     */
    private static void guardarPlantasXML(List<Plantas> lista, File xml) {
        try {
            // 1. Crear un DocumentBuilder
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // 2. Crear un nuevo Documento DOM (vacío)
            Document doc = db.newDocument();
            
            // 3. Crear el elemento raíz (ej: <plantas>)
            Element root = doc.createElement("plantas");
            doc.appendChild(root); // Añadir la raíz al documento
            
            // 4. Recorrer la lista de plantas en memoria
            for (Plantas p : lista) {
                // Por cada planta, crear un elemento <planta>
                Element e = doc.createElement("planta");
                
                // 5. Crear los elementos hijos (codigo, nombre, etc.) y añadirlos a <planta>
                // Usamos el método de utilidad crearElemento
                e.appendChild(crearElemento(doc, "codigo", String.valueOf(p.getCodigo())));
                e.appendChild(crearElemento(doc, "nombre", p.getNombre()));
                e.appendChild(crearElemento(doc, "foto", p.getFoto()));
                e.appendChild(crearElemento(doc, "descripcion", p.getDescripcion()));
                
                // 6. Añadir el elemento <planta> (con sus hijos) al elemento raíz <plantas>
                root.appendChild(e);
            }
            
            // 7. Configurar el "Transformador" para escribir el DOM a un fichero
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes"); // Activar indentación (para que el XML sea legible)
            
            // 8. Transformar (escribir) el Document (DOMSource) al fichero (StreamResult)
            t.transform(new DOMSource(doc), new StreamResult(xml));
            
        } catch (Exception ex) { // Captura ParserConfigurationException, TransformerException
            System.err.println("Error guardando "+xml.getName()+": " + ex.getMessage());
        }
    }

    /**
     * Método de utilidad para crear un Elemento XML simple (ej: <nombre>Geranio</nombre>).
     * @param doc El Documento DOM al que pertenece.
     * @param tag El nombre de la etiqueta (ej: "nombre").
     * @param valor El valor de texto (ej: "Geranio").
     * @return El Elemento (Nodo) creado.
     */
    private static Element crearElemento(Document doc, String tag, String valor) {
        Element e = doc.createElement(tag); // Crea la etiqueta (ej: <nombre>)
        e.appendChild(doc.createTextNode(valor)); // Crea el texto (ej: "Geranio") y lo añade
        return e; // Devuelve <nombre>Geranio</nombre>
    }

    // ------------------------ 2.1 Leer/Escribir plantas.dat (acceso directo) ------------------------

    /**
     * Sincroniza cada planta (ACTIVA) del ArrayList con plantas.dat (RandomAccessFile).
     * Lee el precio/stock del .dat y lo asigna al objeto en memoria,
     * o crea un registro por defecto si no existe.
     * (Rúbrica 2.1)
     * @param lista La lista de plantas ACTIVAS.
     */
    private static void sincronizarPlantasDat(List<Plantas> lista) {
        // Define el fichero de acceso directo
        File dat = Paths.get(DIR_PLANTAS, "plantas.dat").toFile();
        
        // Abre el fichero en modo "rw" (Lectura/Escritura)
        try (RandomAccessFile raf = new RandomAccessFile(dat, "rw")) {
            
            // Recorre la lista de plantas (cargada del XML)
            for (Plantas p : lista) {
                // Calcula la posición del registro. (Código 1 -> pos 0, Código 2 -> pos 12, etc.)
                long pos = (long) (p.getCodigo() - 1) * TAMANO;
                if (pos < 0) continue; // Ignorar códigos 0 o negativos

                // Asegurarse de que el fichero es suficientemente largo para este registro
                if (pos + TAMANO > raf.length()) {
                     raf.setLength(pos + TAMANO); // Alarga el fichero si es necesario
                }

                // Se posiciona en el inicio del registro calculado
                raf.seek(pos);
                
                // Lee los primeros 4 bytes (int) para ver si hay un código
                // Comprobamos si el fichero tiene al menos 4 bytes más desde la posición
                int codigo = (raf.length() > pos + 4) ? raf.readInt() : 0;
                
                if (codigo == p.getCodigo()) {
                    // El registro existe y el código coincide. Leemos el resto.
                    float precio = raf.readFloat(); // Lee los siguientes 4 bytes (float)
                    int stock = raf.readInt(); // Lee los últimos 4 bytes (int)
                    
                    // Actualiza el objeto en memoria con los datos del fichero
                    p.setPrecio(precio);
                    p.setStock(stock);
                } else {
                    // El registro no existe (codigo=0) o es inconsistente.
                    // Creamos/sobreescribimos un registro con valores por defecto.
                    float precioDef = preciosPorDefecto(p.getCodigo());
                    int stockDef = stockPorDefecto();
                    
                    raf.seek(pos); // Volvemos al inicio del registro
                    raf.writeInt(p.getCodigo()); // Escribe el código (4 bytes)
                    raf.writeFloat(precioDef); // Escribe el precio (4 bytes)
                    raf.writeInt(stockDef); // Escribe el stock (4 bytes)
                    
                    // Actualiza el objeto en memoria con estos valores por defecto
                    p.setPrecio(precioDef);
                    p.setStock(stockDef);
                }
            }
        } catch (IOException e) {
            System.err.println("Error accediendo a plantas.dat: " + e.getMessage());
        }
    }

    // Genera un precio por defecto en función del codigo (solo heurística)
    private static float preciosPorDefecto(int codigo) {
        float n = 5.0f + (codigo % 10) * 1.5f;
        return Math.round(n * 100f) / 100f; // Redondea a 2 decimales
    }

    // Stock por defecto sencillo
    private static int stockPorDefecto() {
        return 10;
    }

    /**
     * Lee SÓLO el precio de una planta en plantas.dat (acceso directo).
     * @param codigo El código de la planta a buscar.
     * @return El precio (float), o 0f si hay error.
     */
    private static float leerPrecioDat(int codigo) {
        File dat = Paths.get(DIR_PLANTAS, "plantas.dat").toFile();
        // Abre en modo "r" (Solo Lectura)
        try (RandomAccessFile raf = new RandomAccessFile(dat, "r")) {
             // Calcula la posición exacta del precio
             // (codigo-1) * 12 -> inicio del registro
             // + 4 -> saltar el 'int codigo'
             long pos = (long) (codigo - 1) * TAMANO + 4;
             
             // Comprobación de seguridad: si la posición está fuera del fichero
             if (pos < 0 || pos + 4 > raf.length()) return 0f; // +4 porque leemos un float
             
            raf.seek(pos); // Se posiciona en el float precio
            return raf.readFloat(); // Lee y devuelve el precio
        } catch (Exception e) {
            return 0f; // Si hay error (IOException, EOFException), devuelve 0
        }
    }

    /**
     * Lee SÓLO el stock de una planta en plantas.dat (acceso directo).
     * @param codigo El código de la planta a buscar.
     * @return El stock (int), o 0 si hay error.
     */
    private static int leerStockDat(int codigo) {
        File dat = Paths.get(DIR_PLANTAS, "plantas.dat").toFile();
        // Abre en modo "r" (Solo Lectura)
        try (RandomAccessFile raf = new RandomAccessFile(dat, "r")) {
            // Calcula la posición exacta del stock
            // (codigo-1) * 12 -> inicio del registro
            // + 8 -> saltar el 'int codigo' (4) y el 'float precio' (4)
            long pos = (long) (codigo - 1) * TAMANO + 8;
            
            // Comprobación de seguridad
            if (pos < 0 || pos + 4 > raf.length()) return 0; // +4 porque leemos un int
            
            raf.seek(pos); // Se posiciona en el int stock
            return raf.readInt(); // Lee y devuelve el stock
        } catch (Exception e) {
            return 0; // Si hay error, devuelve 0
        }
    }

    /**
     * Escribe (o sobrescribe) un registro completo (codigo, precio, stock)
     * en plantas.dat para el código indicado.
     * @param codigo El código del registro.
     * @param precio El precio a escribir.
     * @param stock El stock a escribir.
     */
    private static void escribirRegistroDat(int codigo, float precio, int stock) throws IOException {
        File dat = Paths.get(DIR_PLANTAS, "plantas.dat").toFile();
        // Abre en modo "rw" (Lectura/Escritura)
        try (RandomAccessFile raf = new RandomAccessFile(dat, "rw")) {
            // Calcula la posición del registro
            long pos = (long) (codigo - 1) * TAMANO;
             if (pos < 0) throw new IOException("Código de planta inválido");
            
            // Si el registro está más allá del final del fichero, lo alarga
            if (pos + TAMANO > raf.length()) {
                raf.setLength(pos + TAMANO);
            }
            
            raf.seek(pos); // Se posiciona al inicio del registro
            raf.writeInt(codigo); // Escribe código (4 bytes)
            raf.writeFloat(precio); // Escribe precio (4 bytes)
            raf.writeInt(stock); // Escribe stock (4 bytes)
        }
        // El 'try-with-resources' cierra el fichero automáticamente
    }

    // ------------------------ 3. Login e identificación ------------------------

    /**
     * Pide ID y contraseña, lo comprueba contra la lista de empleados.
     * Tiene 3 intentos.
     * (Rúbrica 3.1)
     * @param empleados La lista de empleados activos.
     * @return El objeto Empleado si el login es correcto, o null si fallan los 3 intentos.
     */
    private static Empleado login(List<Empleado> empleados) {
        int intentos = 3; // Contador de intentos
        
        while (intentos > 0) { // Bucle mientras queden intentos
            System.out.println("\n--- INICIAR SESIÓN ---");
            System.out.print("ID: ");
            String idS = sc.nextLine().trim(); // Lee el ID (como texto)
            
            // Validación: ID debe ser solo números (Rúbrica 3.3)
            if (!idS.matches("\\d+")) {
                System.out.println("ID debe ser numérico.");
                intentos--; // Penaliza el intento
                continue; // Vuelve al inicio del 'while'
            }
            
            int id = Integer.parseInt(idS); // Convierte el ID a número
            System.out.print("Contraseña: ");
            String pw = sc.nextLine(); // Lee la contraseña
            
            // Recorre la lista de empleados cargada en memoria
            for (Empleado e : empleados) {
                // Comprueba si el ID y la contraseña coinciden
                if (e.getId() == id && e.getPassword().equals(pw)) {
                    System.out.println("Login correcto. Bienvenido, " + e.getNombre());
                    return e; // Login exitoso: devuelve el objeto Empleado
                }
            }
            
            // Si el bucle 'for' termina, es que no hubo coincidencia
            intentos--; // Resta un intento
            System.out.println("Credenciales incorrectas. Intentos restantes: " + intentos); // (Rúbrica 3.3)
        }
        
        // Si el bucle 'while' termina (intentos=0), devuelve null
        return null;
    }

    // ------------------------ 4. Menú Vendedor ------------------------

    /**
     * Muestra el menú interactivo para el rol Vendedor.
     * Se mantiene en bucle hasta que el usuario elige "0) Cerrar sesión".
     * (Rúbrica 3.2)
     * @param emp El empleado que ha iniciado sesión.
     * @param listaPlantas La lista de plantas activas.
     * @param plantasBaja La lista de plantas de baja (para la venta automática).
     */
    private static void menuVendedor(Empleado emp, List<Plantas> listaPlantas, List<Plantas> plantasBaja) {
        String op; // Variable para guardar la opción del usuario
        
        // Bucle 'do-while': muestra el menú al menos una vez
        do {
            System.out.println("\n--- MENÚ VENDEDOR (" + emp.getNombre() + ") ---");
            System.out.println("1) Listar catálogo");
            System.out.println("2) Realizar venta");
            System.out.println("3) Devolución");
            System.out.println("4) Buscar ticket");
            System.out.println("0) Cerrar sesión");
            System.out.print("Opción: ");
            op = sc.nextLine().trim(); // Lee la opción
            
            // 'switch' para ejecutar la acción correspondiente
            switch (op) {
                case "1":
                    listarCatalogo(listaPlantas); // (Rúbrica 4.1)
                    break;
                case "2":
                    // Pasa ambas listas a la venta
                    realizarVenta(emp, listaPlantas, plantasBaja); // (Rúbrica 5.0)
                    break;
                case "3":
                    procesarDevolucion(emp); // (Rúbrica 6.0)
                    break;
                case "4":
                    buscarTicketPorTeclado(); // (Rúbrica 6.4)
                    break;
                case "0":
                    System.out.println("Cerrando sesión...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!op.equals("0")); // El bucle se repite mientras la opción NO sea "0"
    }

    /**
     * 4.1 Lista el catálogo de plantas ACTIVAS.
     * Para cada planta, lee su precio y stock desde plantas.dat.
     * (Rúbrica 4.1)
     * @param listaPlantas La lista de plantas activas (cargada del XML).
     */
    private static void listarCatalogo(List<Plantas> listaPlantas) {
        System.out.println("\n--- CATÁLOGO ---");
        if(listaPlantas.isEmpty()) System.out.println("No hay plantas activas en el catálogo.");
        
        // Recorre la lista de plantas (de memoria)
        for (Plantas p : listaPlantas) {
            // Por cada planta, accede a plantas.dat para leer su precio y stock actuales
            float precio = leerPrecioDat(p.getCodigo());
            int stock = leerStockDat(p.getCodigo());
            
            // Formatea el código con ceros a la izquierda (ej: 003) (Rúbrica 3.4)
            String codigoFmt = String.format("%03d", p.getCodigo());
            
            // Imprime la línea formateada
            System.out.printf("Código: %s | Nombre: %-15s | Precio: %6.2f € | Stock: %d%n",
                    codigoFmt, p.getNombre(), precio, stock);
        }
        
        // (Rúbrica 4.2)
        System.out.print("\nIntroduzca código para ver/ir a venta (0 para volver): ");
        String cod = sc.nextLine().trim();
        
        if (cod.matches("\\d+")) { // Si es un número
            int codigo = Integer.parseInt(cod);
            if (codigo == 0) return; // Si es 0, vuelve al menú
            
            // Busca la planta en la lista de activas
            Optional<Plantas> opt = listaPlantas.stream().filter(pl -> pl.getCodigo() == codigo).findFirst();
            
            if (opt.isPresent()) { // Si la encuentra
                mostrarFichaVenta(opt.get()); // Muestra la ficha detallada
                
                // Pregunta si desea iniciar una venta (Rúbrica 4.2)
                System.out.print("¿Vender esta planta? (si/no): ");
                if (sc.nextLine().equalsIgnoreCase("si")) {
                    // (Esta funcionalidad "simple" no está implementada, se redirige a la Venta (Opción 2))
                    System.out.println("Redirigiendo a venta... (Opción no implementada, use Opción 2)");
                }
            } else {
                System.out.println("Código no encontrado en plantas activas.");
            }
        } else if (!cod.isEmpty()){ // Si no está vacío y no es número
            System.out.println("Código no válido.");
        }
    }

    /**
     * Muestra la ficha detallada de una planta (nombre, desc, precio, stock).
     * @param p La planta a mostrar.
     */
    private static void mostrarFichaVenta(Plantas p) {
        System.out.println("---- FICHA PLANTA ----");
        System.out.println("Código: " + String.format("%03d", p.getCodigo())); // (Rúbrica 3.4)
        System.out.println("Nombre: " + p.getNombre());
        System.out.println("Descripción: " + p.getDescripcion());
        // Lee precio y stock del .dat
        System.out.println("Precio: " + leerPrecioDat(p.getCodigo()) + " €");
        System.out.println("Stock: " + leerStockDat(p.getCodigo()));
    }

    // ------------------------ 5. Realizar venta ------------------------

    /**
     * Lógica de venta interactiva con cesta múltiple.
     * Permite añadir varios productos, valida stock, genera ticket y
     * actualiza plantas.dat.
     * (Rúbrica 5.0)
     * @param emp El empleado que realiza la venta.
     * @param listaPlantas La lista de plantas activas.
     * @param plantasBaja La lista de plantas de baja (para añadir si stock llega a 0).
     */
    private static void realizarVenta(Empleado emp, List<Plantas> listaPlantas, List<Plantas> plantasBaja) {
        boolean continuarVenta = true; // Para el bucle de "otra venta" (Rúbrica 5.0)
        
        while (continuarVenta) {
            // Cesta de la compra: Mapa de (Código de Planta -> Cantidad)
            // LinkedHashMap mantiene el orden de inserción
            Map<Integer, Integer> cesta = new LinkedHashMap<>();
            boolean seleccionando = true; // Para el bucle de "añadir productos"

            // --- BUCLE DE SELECCIÓN DE PRODUCTOS ---
            while (seleccionando) {
                System.out.print("Introduce código planta (0 para terminar selección): ");
                String cs = sc.nextLine().trim();
                
                // (Rúbrica 5.1) Validación: debe ser numérico
                if (!cs.matches("\\d+")) {
                    System.out.println("Código inválido. Debe ser numérico.");
                    continue; // Repite el bucle
                }
                
                int codigo = Integer.parseInt(cs);
                if (codigo == 0) { // Si es 0, termina la selección
                    seleccionando = false;
                    continue; // Sale del bucle de selección
                }

                // Busca la planta en la lista de ACTIVAS
                Optional<Plantas> op = listaPlantas.stream().filter(p -> p.getCodigo() == codigo).findFirst();
                if (!op.isPresent()) {
                    System.out.println("Código no encontrado en el catálogo de plantas activas.");
                    continue;
                }

                // (Rúbrica 5.2) Comprobar el stock actual leyendo de plantas.dat
                int stock = leerStockDat(codigo);
                if (stock <= 0) {
                    System.out.println("Stock 0. No disponible.");
                    continue;
                }

                System.out.print("Cantidad: ");
                String cantS = sc.nextLine().trim();
                
                // (Rúbrica 4.3 / 5.1) Validación: cantidad debe ser numérica
                if (!cantS.matches("\\d+")) {
                    System.out.println("Cantidad inválida. Debe ser numérica.");
                    continue;
                }
                
                int cantidad = Integer.parseInt(cantS);
                if (cantidad <= 0) {
                    System.out.println("Cantidad debe ser mayor que cero.");
                    continue;
                }
                
                // (Rúbrica 5.2) Comprobar si la cantidad supera el stock
                if (cantidad > stock) {
                    System.out.println("No hay suficientes unidades. Stock: " + stock);
                    continue;
                }
                
                // Añadimos a la cesta (sumando si ya existía ese código)
                cesta.put(codigo, cesta.getOrDefault(codigo, 0) + cantidad);
                System.out.println("Añadido a la cesta.");

                System.out.print("¿Añadir otra planta? (si/no): ");
                if (!sc.nextLine().equalsIgnoreCase("si")) {
                    seleccionando = false; // Sale del bucle de selección
                }
            }
            // --- FIN BUCLE SELECCIÓN ---

            // Si la cesta está vacía, cancela la venta
            if (cesta.isEmpty()) {
                System.out.println("No se seleccionó ningún artículo. Venta cancelada.");
                return; // Sale de realizarVenta
            }

            // 5.3 Mostrar resumen compra (Rúbrica 5.3)
            System.out.println("\n--- RESUMEN COMPRA ---");
            float total = 0f;
            // Lista para guardar las líneas del ticket (formato "003 2 5.00")
            List<String> lineasTicket = new ArrayList<>();
            
            // Recorre la cesta para mostrar el resumen
            for (Map.Entry<Integer, Integer> e : cesta.entrySet()) {
                int cod = e.getKey();
                int cant = e.getValue();
                float precio = leerPrecioDat(cod); // Lee el precio del .dat
                float subtotal = precio * cant;
                total += subtotal; // Suma al total
                
                // Muestra la línea del resumen
                System.out.printf("Código %03d  Cantidad %d  Precio unit: %.2f  Subtotal: %.2f%n", cod, cant, precio, subtotal);
                // Guarda la línea para el fichero del ticket (Rúbrica 5.5)
                lineasTicket.add(String.format("%03d %d %.2f", cod, cant, precio));
            }
            System.out.printf("TOTAL A PAGAR: %.2f €%n", total);

            // 5.4 Confirmar venta
            System.out.print("Confirmar venta? (si/no): ");
            String conf = sc.nextLine();
            
            if (conf.equalsIgnoreCase("si")) {
                // Venta confirmada, actualizamos stock
                
                // (Rúbrica 5.4) Recorre la cesta de nuevo para actualizar el .dat
                for (Map.Entry<Integer, Integer> e : cesta.entrySet()) {
                    int cod = e.getKey();
                    int cant = e.getValue();
                    int stockActual = leerStockDat(cod);
                    float precioActual = leerPrecioDat(cod);
                    int nuevoStock = stockActual - cant; // Calcula el nuevo stock
                    
                    try {
                        // Escribe el nuevo stock en plantas.dat
                        escribirRegistroDat(cod, precioActual, nuevoStock);
                    } catch (IOException ex) {
                        System.err.println("Error actualizando stock para código " + cod);
                    }
                    
                    // (Rúbrica 8.1.2) Baja automática si stock llega a 0
                    if (nuevoStock <= 0) {
                        System.out.println("Stock en 0: Dando de baja automática la planta Cód " + cod);
                        // (Rúbrica 8.1.2.1)
                        moverPlantaABaja(cod, listaPlantas, plantasBaja, precioActual);
                    }
                }

                // 5.5 Generar ticket (Rúbrica 5.6, 5.7)
                int num = siguienteNumeroTicket(); // Obtiene el siguiente número secuencial
                Ticket ticket = new Ticket(num); // (Asume que existe la clase Ticket)
                ArrayList<String> contenido = new ArrayList<>(); // Contenido del fichero
                
                // Formateador de fecha y hora
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                
                // Añade la cabecera
                contenido.add("TICKET Nº " + num);
                contenido.add("Fecha: " + LocalDateTime.now().format(fmt));
                contenido.add("Empleado ID: " + emp.getId() + " Nombre: " + emp.getNombre());
                contenido.add("---------------------------------");
                // Añade las líneas de productos
                for (String l : lineasTicket) contenido.add(l);
                contenido.add("---------------------------------");
                // Añade el total
                contenido.add(String.format("TOTAL: %.2f €", total));
                
                // Llama al método de la clase Ticket para escribir el fichero
                if (ticket.crearTicket(contenido)) {
                    System.out.println("Ticket generado: " + num);
                } else {
                    System.err.println("Error generando ticket.");
                }
            } else {
                System.out.println("Venta cancelada.");
            }

            // 5.0 Permitir otra venta
            System.out.print("¿Realizar otra venta? (si/no): ");
            if (!sc.nextLine().equalsIgnoreCase("si")) {
                continuarVenta = false; // Sale del bucle de "otra venta"
            }
        }
    }


    // ------------------------ 6. Devolución ------------------------

    /**
     * Procesa la devolución de un ticket.
     * Busca el ticket, restaura el stock, y mueve el ticket
     * (modificado) a la carpeta DEVOLUCIONES.
     * (Rúbrica 6.1, 6.2, 6.3)
     */
    private static void procesarDevolucion(Empleado emp) {
        System.out.print("Número de ticket a devolver: ");
        String numS = sc.nextLine().trim();
        if (!numS.matches("\\d+")) {
            System.out.println("Número inválido.");
            return;
        }
        
        int num = Integer.parseInt(numS);
        // Busca el ticket en la carpeta TICKETS
        File ticketFile = Paths.get(DIR_TICKETS, num + ".txt").toFile();
        if (!ticketFile.exists()) {
            System.out.println("Ticket no encontrado en TICKETS.");
            return;
        }

        try {
            // Lee todas las líneas del ticket original
            List<String> lines = Files.readAllLines(ticketFile.toPath());
            // Prepara el contenido para el nuevo fichero de devolución
            List<String> devLines = new ArrayList<>();
            // Mapa para guardar (Código -> Cantidad) de los items del ticket
            Map<Integer, Integer> itemsADevolver = new HashMap<>();
            
            float total = 0f; // Para guardar el total leído
            int totalIndex = -1; // Para guardar la posición de la línea del total
            boolean inItems = false; // Flag para saber si estamos en la sección de items

            // 1. Parsear el ticket original
            for (int i = 0; i < lines.size(); i++) {
                String l = lines.get(i);
                
                // Detecta los separadores "---"
                if (l.trim().startsWith("---")) {
                    inItems = !inItems; // Invierte el flag (entra o sale de la sección)
                } 
                // Si estamos en la sección de items y la línea coincide con el formato "003 2 5.00"
                else if (inItems && l.trim().matches("^\\d{3,}\\s+\\d+\\s+[0-9.]+.*")) {
                    String[] parts = l.trim().split("\\s+"); // Divide por espacios
                    try {
                        int cod = Integer.parseInt(parts[0]); // Código
                        int cant = Integer.parseInt(parts[1]); // Cantidad
                        // Acumula en el mapa (por si el mismo código aparece varias veces)
                        itemsADevolver.put(cod, itemsADevolver.getOrDefault(cod, 0) + cant);
                    } catch (NumberFormatException e) { /* Ignorar línea mal formada */ }
                } 
                // Si la línea es la del TOTAL
                else if (l.trim().toUpperCase().startsWith("TOTAL:")) {
                    try {
                        // Extrae el número (ej: "TOTAL: 12.50 €" -> "12.50")
                        String totalStr = l.substring(l.indexOf(":") + 1).trim().replace("€","").trim();
                        total = Float.parseFloat(totalStr);
                        totalIndex = i; // Guarda la posición de esta línea
                    } catch (Exception e) { /* Ignorar error de parseo */ }
                }
            }

            // Si no encontramos un total o no encontramos items, no podemos procesar
            if (totalIndex == -1 || itemsADevolver.isEmpty()) {
                System.out.println("No se pudo procesar el ticket (formato no reconocido).");
                return;
            }

            // 2. Actualizar (devolver) el stock (Rúbrica 6.2)
            System.out.println("Restaurando stock...");
            for (Map.Entry<Integer, Integer> entry : itemsADevolver.entrySet()) {
                int cod = entry.getKey();
                int cant = entry.getValue();
                try {
                    int stockActual = leerStockDat(cod);
                    float precioActual = leerPrecioDat(cod);
                    // Si la planta está dada de baja (precio 0), no tocamos el precio
                    float precioARestaurar = (precioActual == 0f) ? 0f : precioActual;
                    
                    // Escribimos en .dat el nuevo stock (actual + devuelto)
                    escribirRegistroDat(cod, precioARestaurar, stockActual + cant);
                    System.out.printf("Stock actualizado Cód %03d: %d -> %d%n", cod, stockActual, stockActual + cant);
                } catch (IOException e) {
                    System.err.println("Error actualizando stock para " + cod + ": " + e.getMessage());
                }
            }

            // 3. Crear el contenido del nuevo fichero de devolución
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            devLines.add("-- DEVOLUCIÓN --"); // (Rúbrica 6.1)
            devLines.add("Fecha Devolución: " + LocalDateTime.now().format(fmt));
            devLines.add("Procesado por ID: " + emp.getId() + " Nombre: " + emp.getNombre());
            devLines.add("=================================");
            devLines.add("Ticket Original Nº " + num);

            // 4. Copiar líneas originales, reemplazando el total
            for (int i = 0; i < lines.size(); i++) {
                if (i == totalIndex) { // Si es la línea del total
                    // (Rúbrica 6.1) Escribimos el total en negativo
                    devLines.add(String.format("TOTAL DEVUELTO: -%.2f €", total));
                } else {
                    // Si es cualquier otra línea, la copiamos tal cual
                    devLines.add(lines.get(i));
                }
            }

            // 5. Escribir en DEVOLUCIONES/ y borrar de TICKETS/ (Rúbrica 6.3)
            File devFile = Paths.get("DEVOLUCIONES", num + ".txt").toFile();
            Files.write(devFile.toPath(), devLines); // Escribe el nuevo fichero
            ticketFile.delete(); // Elimina el ticket original

            System.out.println("Devolución procesada. Fichero movido a DEVOLUCIONES.");

        } catch (IOException e) {
            System.err.println("Error de I/O procesando devolución: " + e.getMessage());
        }
    }

    /**
     * Busca un ticket por su número, tanto en TICKETS como en DEVOLUCIONES,
     * y muestra su contenido.
     * (Rúbrica 6.4)
     */
    private static void buscarTicketPorTeclado() {
        System.out.print("Número de ticket a buscar: ");
        String numS = sc.nextLine().trim();
        if (!numS.matches("\\d+")) {
            System.out.println("Número inválido.");
            return;
        }

        // Define las rutas a los dos posibles ficheros
        File tFile = Paths.get(DIR_TICKETS, numS + ".txt").toFile();
        File dFile = Paths.get("DEVOLUCIONES", numS + ".txt").toFile();
        File toRead = null; // Fichero que finalmente leeremos

        if (tFile.exists()) { // Si existe en TICKETS
            toRead = tFile;
            System.out.println("\n--- TICKET (Venta) " + numS + " ---");
        } else if (dFile.exists()) { // Si existe en DEVOLUCIONES
            toRead = dFile;
            System.out.println("\n--- TICKET (Devolución) " + numS + " ---");
        }

        if (toRead != null) { // Si hemos encontrado el fichero
            try {
                // Lee todas las líneas y las imprime en consola
                Files.readAllLines(toRead.toPath()).forEach(System.out::println);
            } catch (IOException e) {
                System.err.println("Error leyendo el ticket: " + e.getMessage());
            }
            System.out.println("---------------------------------");
        } else { // Si no se encontró en ninguna carpeta
            System.out.println("Ticket no encontrado.");
        }
    }

    // ------------------------ 7. Menú Gestor ------------------------

    /**
     * Muestra el menú interactivo para el rol Gestor.
     * (Rúbrica 3.2)
     * @param emp El empleado gestor logueado.
     * @param plantas La lista de plantas activas (para modificarla).
     * @param plantasBaja La lista de plantas de baja (para modificarla).
     * @param empleados La lista de empleados activos (para modificarla).
     * @param empleadosBaja La lista de empleados de baja (para modificarla).
     */
    private static void menuGestor(Empleado emp, List<Plantas> plantas, List<Plantas> plantasBaja,
                                   List<Empleado> empleados, List<Empleado> empleadosBaja) {
        String op; // Opción del menú
        do {
            System.out.println("\n--- MENÚ GESTOR (" + emp.getNombre() + ") ---");
            System.out.println("1) Gestionar Plantas");
            System.out.println("2) Gestionar Empleados");
            System.out.println("3) Ver bajas (Plantas)");
            System.out.println("4) Ver bajas (Empleados)");
            System.out.println("5) Buscar ticket");
            System.out.println("0) Cerrar sesión");
            System.out.print("Opción: ");
            op = sc.nextLine().trim();
            
            switch (op) {
                case "1":
                    gestionarPlantas(plantas, plantasBaja); // Llama al submenú (Rúbrica 8.1)
                    break;
                case "2":
                    gestionarEmpleados(empleados, empleadosBaja); // Llama al submenú (Rúbrica 8.2)
                    break;
                case "3":
                    System.out.println("\n--- PLANTAS DADAS DE BAJA ---");
                    mostrarListaPlantas(plantasBaja); // Muestra la lista
                    break;
                case "4":
                    System.out.println("\n--- EMPLEADOS DADOS DE BAJA ---");
                    mostrarListaEmpleados(empleadosBaja); // Muestra la lista
                    break;
                case "5":
                    buscarTicketPorTeclado(); // Misma función que el vendedor
                    break;
                case "0":
                    System.out.println("Cerrando sesión...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!op.equals("0")); // Repite mientras no sea 0

        // Al salir del menú gestor, guardamos los cambios en las listas de bajas de empleados
        // (Rúbrica 8.2.2.2, 8.2.3.3)
        guardarEmpleados(empleadosBaja, Paths.get(DIR_EMPLEADOS, "BAJA", "empleadosBaja.dat").toFile());
        System.out.println("Datos de empleados de baja guardados.");
    }

    // ------------------------ 7.1 Gestión Plantas (Gestor) ------------------------

    /**
     * Submenú para la gestión de Plantas (Altas, Bajas, Modificaciones).
     */
    private static void gestionarPlantas(List<Plantas> activas, List<Plantas> bajas) {
        String op;
        do {
            System.out.println("\n--- GESTIÓN DE PLANTAS ---");
            System.out.println("1) Alta nueva planta");
            System.out.println("2) Modificar planta (Precio/Stock)");
            System.out.println("3) Dar de baja planta (manual)");
            System.out.println("4) Reactivar planta");
            System.out.println("0) Volver al menú gestor");
            System.out.print("Opción: ");
            op = sc.nextLine().trim();
            
            switch (op) {
                case "1":
                    altaPlanta(activas); // (Rúbrica 8.1.1)
                    break;
                case "2":
                    modificarPlanta(activas); // (Rúbrica 8.1.3 - "dar stock")
                    break;
                case "3":
                    bajaPlanta(activas, bajas); // (Rúbrica 8.1.2)
                    break;
                case "4":
                    reactivarPlanta(activas, bajas); // (Rúbrica 8.1.3)
                    break;
                case "0":
                    break; // Vuelve al menú gestor
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!op.equals("0"));
    }

    /**
     * Da de alta una nueva planta. Pide datos, la añade a la lista de activas
     * y crea su registro en plantas.dat.
     * (Rúbrica 8.1.1)
     * @param activas La lista de plantas activas (se modificará).
     */
    private static void altaPlanta(List<Plantas> activas) {
        try {
            System.out.println("\n--- ALTA NUEVA PLANTA ---");
            System.out.print("Código (numérico, debe ser único): ");
            String cs = sc.nextLine().trim();
            if (!cs.matches("\\d+") || cs.equals("0")) {
                System.out.println("Código inválido."); return;
            }
            int codigo = Integer.parseInt(cs);

            // Comprobar si ya existe (en activas o si tiene datos en .dat)
            if (activas.stream().anyMatch(p -> p.getCodigo() == codigo) || 
                leerPrecioDat(codigo) != 0f) { // Si el precio no es 0, el registro está ocupado
                System.out.println("El código " + codigo + " ya existe.");
                return;
            }

            // Pide el resto de datos
            System.out.print("Nombre: ");
            String nombre = sc.nextLine().trim();
            System.out.print("Descripción: ");
            String desc = sc.nextLine().trim();
            System.out.print("Ruta Foto (ej: /img/foto.jpg): ");
            String foto = sc.nextLine().trim();
            System.out.print("Precio (ej: 12.50): ");
            float precio = Float.parseFloat(sc.nextLine().trim().replace(",",".")); // Acepta , o .
            System.out.print("Stock inicial: ");
            int stock = Integer.parseInt(sc.nextLine().trim());

            // 1. Añadir al ArrayList (Rúbrica 8.1.1.1)
            activas.add(new Plantas(codigo, nombre, foto, desc, precio, stock));

            // 2. Escribir en .dat (Rúbrica 8.1.1.2)
            escribirRegistroDat(codigo, precio, stock);

            // 3. El guardado en XML se hace al salir del main
            System.out.println("Planta " + nombre + " añadida. Se guardará en XML al salir.");

        } catch (NumberFormatException e) {
            System.out.println("Error: El dato numérico no es válido.");
        } catch (IOException e) {
            System.err.println("Error al escribir en plantas.dat: " + e.getMessage());
        }
    }

    /**
     * Modifica precio y/o stock de una planta activa en plantas.dat.
     * (Rúbrica 8.1.3 - Modificar stock/campos)
     * @param activas La lista de plantas activas (solo para comprobar existencia).
     */
    private static void modificarPlanta(List<Plantas> activas) {
        System.out.print("Código de planta a modificar: ");
        String cs = sc.nextLine().trim();
        if (!cs.matches("\\d+")) { System.out.println("Código inválido."); return; }
        int codigo = Integer.parseInt(cs);

        // Comprueba que la planta esté en la lista de activas
        if (activas.stream().noneMatch(p -> p.getCodigo() == codigo)) {
            System.out.println("Planta no encontrada en activas.");
            return;
        }

        try {
            // Lee los datos actuales del .dat
            float precioActual = leerPrecioDat(codigo);
            int stockActual = leerStockDat(codigo);
            System.out.printf("Datos actuales: Precio=%.2f €, Stock=%d%n", precioActual, stockActual);

            // Pide los nuevos datos
            System.out.print("Nuevo precio (dejar en blanco para no cambiar): ");
            String ps = sc.nextLine().trim().replace(",",".");
            System.out.print("Nuevo stock (dejar en blanco para no cambiar): ");
            String ss = sc.nextLine().trim();

            // Lógica ternaria: si la entrada está vacía, usa el valor actual
            float nuevoPrecio = ps.isEmpty() ? precioActual : Float.parseFloat(ps);
            int nuevoStock = ss.isEmpty() ? stockActual : Integer.parseInt(ss);

            // Escribe los nuevos datos en plantas.dat
            escribirRegistroDat(codigo, nuevoPrecio, nuevoStock);
            System.out.println("Planta " + codigo + " actualizada.");

        } catch (NumberFormatException e) {
            System.out.println("Error: El dato numérico no es válido.");
        } catch (IOException e) {
            System.err.println("Error al actualizar plantas.dat: " + e.getMessage());
        }
    }

    /**
     * Da de baja manualmente una planta (la mueve a la lista de bajas).
     * (Rúbrica 8.1.2)
     * @param activas La lista de plantas activas (se modificará).
     * @param bajas La lista de plantas de baja (se modificará).
     */
    private static void bajaPlanta(List<Plantas> activas, List<Plantas> bajas) {
        System.out.print("Código de planta a dar de baja: ");
        String cs = sc.nextLine().trim();
        if (!cs.matches("\\d+")) { System.out.println("Código inválido."); return; }
        int codigo = Integer.parseInt(cs);

        // Busca la planta en la lista de activas
        Optional<Plantas> opt = activas.stream().filter(p -> p.getCodigo() == codigo).findFirst();
        if (!opt.isPresent()) {
            System.out.println("Planta no encontrada en activas.");
            return;
        }

        // Obtiene el precio *antes* de borrarlo del .dat
        float precioActual = leerPrecioDat(codigo);
        // Llama a la lógica central de baja
        moverPlantaABaja(codigo, activas, bajas, precioActual);
    }

    /**
     * Lógica central para mover una planta de 'activas' a 'bajas'.
     * Usado por bajaPlanta() y por realizarVenta() (baja automática).
     * (Rúbrica 8.1.2.1, 8.1.2.2)
     * @param codigo El código de la planta a mover.
     * @param activas Lista de activas (se elimina el item).
     * @param bajas Lista de bajas (se añade el item).
     * @param precioBaja El precio que tenía la planta (para guardarlo en bajas.dat).
     */
    private static void moverPlantaABaja(int codigo, List<Plantas> activas, List<Plantas> bajas, float precioBaja) {
        // Busca la planta en la lista de activas
        Optional<Plantas> opt = activas.stream().filter(p -> p.getCodigo() == codigo).findFirst();
        if (opt.isPresent()) {
            Plantas p = opt.get(); // El objeto planta a mover
            
            try {
                // (Rúbrica 8.1.2.2) Escribir (codigo, precio) en el fichero de bajas
                // Esto guarda un historial de precios en un fichero secuencial
                guardarPrecioEnBajasDat(codigo, precioBaja);
                
                // (Rúbrica 8.1.2.1) Modificar .dat a precio=0, stock=0
                // Esto marca el registro en plantas.dat como "vacío" o "inactivo"
                escribirRegistroDat(codigo, 0f, 0); 
                
                // Mover la planta de la lista 'activas' a 'bajas' (en memoria)
                activas.remove(p);
                bajas.add(p);
                // (Los XML se guardarán al salir del programa)
                
                System.out.println("Planta Cód " + codigo + " movida a Bajas.");
            } catch (IOException e) {
                System.err.println("Error al actualizar ficheros .dat para baja: " + e.getMessage());
                // Si falla la escritura en ficheros, no movemos la planta de lista
            }
        } else {
            System.out.println("Planta " + codigo + " no encontrada en la lista de activas.");
        }
    }

    /**
     * Escribe (en modo "append") el código y precio en plantasbaja.dat.
     * (Rúbrica 8.1.2.2)
     * @param codigo El código de la planta.
     * @param precio El precio a guardar.
     */
    private static void guardarPrecioEnBajasDat(int codigo, float precio) throws IOException {
        File datBaja = Paths.get(DIR_PLANTAS, "plantasbaja.dat").toFile();
        // Abre DataOutputStream en modo 'append' (true)
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(datBaja, true))) {
            dos.writeInt(codigo); // Escribe código
            dos.writeFloat(precio); // Escribe precio
        }
    }


    /**
     * Reactiva una planta de la lista de bajas.
     * La mueve a la lista de activas, lee el precio de plantasbaja.dat,
     * pide nuevo stock y actualiza plantas.dat.
     * (Rúbrica 8.1.3)
     * @param activas Lista de activas (se añade el item).
     * @param bajas Lista de bajas (se elimina el item).
     */
    private static void reactivarPlanta(List<Plantas> activas, List<Plantas> bajas) {
        // (Rúbrica 8.1.3.1) Pide el código
        System.out.print("Código de planta a reactivar: ");
        String cs = sc.nextLine().trim();
        if (!cs.matches("\\d+")) { System.out.println("Código inválido."); return; }
        int codigo = Integer.parseInt(cs);

        // Busca la planta en la lista de bajas (XML)
        Optional<Plantas> opt = bajas.stream().filter(p -> p.getCodigo() == codigo).findFirst();
        if (!opt.isPresent()) {
            System.out.println("Planta no encontrada en bajas (XML).");
            return;
        }
        Plantas p = opt.get(); // El objeto planta a mover
        
        try {
            // (Rúbrica 8.1.3.2) Leer fichero bajas (plantasbaja.dat) para obtener el precio
            float precio = leerPrecioDeBajasDat(codigo);
            
            if (precio == -1f) { // Si no se encontró en el .dat
                System.out.println("No se encontró precio de respaldo en plantasbaja.dat.");
                System.out.print("Introduzca precio manualmente: ");
                precio = Float.parseFloat(sc.nextLine().trim().replace(",","."));
            }

            // Pide el nuevo stock (debe ser > 0 para reactivar)
            System.out.print("Introduzca nuevo stock: ");
            String ss = sc.nextLine().trim();
            if (!ss.matches("\\d+") || Integer.parseInt(ss) <= 0) { 
                System.out.println("Stock inválido (debe ser > 0)."); return; 
            }
            int stock = Integer.parseInt(ss);

            // (Rúbrica 8.1.3.3) Modificar fichero de acceso directo (plantas.dat)
            escribirRegistroDat(codigo, precio, stock);

            // (Rúbrica 8.1.3.4) Eliminar el dato del fichero de bajas (plantasbaja.dat)
            // Esto limpia el historial de precios de esa planta
            eliminarPrecioDeBajasDat(codigo);

            // Mover de lista bajas a activas (en memoria)
            bajas.remove(p);
            activas.add(p);
            // (Los XML se guardarán al salir del main)

            System.out.printf("Planta Cód %d reactivada (Stock: %d, Precio: %.2f)%n", codigo, stock, precio);

        } catch (IOException e) {
            System.err.println("Error al reactivar la planta: " + e.getMessage());
        } catch (NumberFormatException e) {
             System.out.println("Error: El dato numérico no es válido.");
        }
    }

    /**
     * Lee secuencialmente plantasbaja.dat y devuelve el ÚLTIMO precio
     * guardado para un código.
     * (Rúbrica 8.1.3.2)
     * @param codigo El código a buscar.
     * @return El precio, o -1f si no se encuentra.
     */
    private static float leerPrecioDeBajasDat(int codigo) throws IOException {
        File datBaja = Paths.get(DIR_PLANTAS, "plantasbaja.dat").toFile();
        if (!datBaja.exists()) return -1f;
        
        float precioEncontrado = -1f; // Valor por defecto
        
        // Abre DataInputStream para leer secuencialmente
        try (DataInputStream dis = new DataInputStream(new FileInputStream(datBaja))) {
            // Mientras haya datos disponibles
            while (dis.available() > 0) {
                int cod = dis.readInt();
                float precio = dis.readFloat();
                if (cod == codigo) {
                    precioEncontrado = precio; // Sobrescribe (se queda con el último)
                }
            }
        } catch (EOFException e) {
            // Fin de fichero esperado
        }
        return precioEncontrado;
    }

    /**
     * Elimina *todas* las entradas de un código de plantasbaja.dat.
     * Lo hace copiando el fichero a uno temporal, omitiendo las entradas,
     * y luego reemplazando el original.
     * (Rúbrica 8.1.3.4)
     * @param codigo El código a eliminar.
     */
    private static void eliminarPrecioDeBajasDat(int codigo) throws IOException {
        File datBaja = Paths.get(DIR_PLANTAS, "plantasbaja.dat").toFile();
        File temp = Paths.get(DIR_PLANTAS, "plantasbaja.temp").toFile();
        if (!datBaja.exists()) return; // Si no existe, no hay nada que hacer

        // Abre el fichero original (lectura) y el temporal (escritura)
        try (DataInputStream dis = new DataInputStream(new FileInputStream(datBaja));
             DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp))) {
            
            while (dis.available() > 0) { // Lee el original
                int cod = dis.readInt();
                float precio = dis.readFloat();
                
                if (cod != codigo) { // Si NO es el código a eliminar
                    dos.writeInt(cod); // Lo escribe en el temporal
                    dos.writeFloat(precio);
                }
                // Si SÍ es el código a eliminar, simplemente no lo escribe
            }
        } catch (EOFException e) {} // Fin de fichero

        // Reemplaza el fichero original por el temporal
        datBaja.delete(); // Borra el original
        temp.renameTo(datBaja); // Renombra el temporal
    }


    // ------------------------ 7.2 Gestión Empleados (Gestor) ------------------------

    /**
     * Submenú para la gestión de Empleados (Altas, Bajas, Reactivación).
     * (Rúbrica 8.2)
     */
    private static void gestionarEmpleados(List<Empleado> empleados, List<Empleado> empleadosBaja) {
        String op;
        do {
            System.out.println("\n--- GESTIÓN DE EMPLEADOS ---");
            System.out.println("1) Alta nuevo empleado");
            System.out.println("2) Dar de baja empleado");
            System.out.println("3) Reactivar empleado");
            System.out.println("4) Listar empleados activos");
            System.out.println("0) Volver al menú gestor");
            System.out.print("Opción: ");
            op = sc.nextLine().trim();
            
            switch (op) {
                case "1":
                    altaEmpleado(empleados); // (Rúbrica 8.2.1)
                    break;
                case "2":
                    bajaEmpleado(empleados, empleadosBaja); // (Rúbrica 8.2.2)
                    break;
                case "3":
                    reactivarEmpleado(empleados, empleadosBaja); // (Rúbrica 8.2.3)
                    break;
                case "4":
                    System.out.println("\n--- EMPLEADOS ACTIVOS ---");
                    mostrarListaEmpleados(empleados);
                    break;
                case "0":
                    break; // Vuelve al menú gestor
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!op.equals("0"));
    }

    /**
     * Da de alta un nuevo empleado. Pide los datos, lo añade a la lista
     * de activos y reescribe empleados.dat.
     * (Rúbrica 8.2.1)
     * @param empleados La lista de empleados activos (se modificará).
     */
    private static void altaEmpleado(List<Empleado> empleados) {
         try {
            System.out.println("\n--- ALTA NUEVO EMPLEADO ---");
            System.out.print("ID (numérico, debe ser único): ");
            String cs = sc.nextLine().trim();
            
            // (Rúbrica 8.2.1.2) Control de errores
            if (!cs.matches("\\d+") || cs.equals("0")) {
                System.out.println("ID inválido."); return;
            }
            int id = Integer.parseInt(cs);

            // (Rúbrica 8.2.1.2) Control de unicidad
            if (empleados.stream().anyMatch(e -> e.getId() == id)) {
                System.out.println("El ID " + id + " ya existe.");
                return;
            }

            // Pide el resto de datos
            System.out.print("Nombre: ");
            String nombre = sc.nextLine().trim();
            System.out.print("Password: ");
            String pass = sc.nextLine().trim();
            System.out.print("Cargo (vendedor/gestor): ");
            String cargo = sc.nextLine().trim();

            // (Rúbrica 8.2.1.2) Control de cargo y campos vacíos
            if (!cargo.equalsIgnoreCase("vendedor") && !cargo.equalsIgnoreCase("gestor")) {
                System.out.println("Cargo inválido. Debe ser 'vendedor' o 'gestor'.");
                return;
            }
            if (nombre.isEmpty() || pass.isEmpty()) {
                 System.out.println("Nombre y password no pueden estar vacíos.");
                return;
            }

            // (Rúbrica 8.2.1.1) Añadir empleado al ArrayList (en memoria)
            empleados.add(new Empleado(id, nombre, pass, cargo));
            
            // (Rúbrica 8.2.1.3) Escribir el empleado en el fichero empleado.dat
            // (Llamamos a guardarEmpleados, que usa ObjectOutputStream)
            guardarEmpleados(empleados, Paths.get(DIR_EMPLEADOS, "empleados.dat").toFile());
            System.out.println("Empleado " + nombre + " añadido.");

        } catch (NumberFormatException e) {
            System.out.println("Error: El ID no es válido.");
        }
    }

    /**
     * Da de baja un empleado. Lo mueve de la lista de activos a la de bajas
     * y reescribe empleados.dat (activos).
     * (El fichero de bajas se reescribe al salir del menú gestor).
     * (Rúbrica 8.2.2)
     * @param empleados Lista de activos (se modifica).
     * @param empleadosBaja Lista de bajas (se modifica).
     */
    private static void bajaEmpleado(List<Empleado> empleados, List<Empleado> empleadosBaja) {
        System.out.print("ID de empleado a dar de baja: ");
        String cs = sc.nextLine().trim();
        if (!cs.matches("\\d+")) { System.out.println("ID inválido."); return; }
        int id = Integer.parseInt(cs);

        // Busca al empleado en la lista de activos
        Optional<Empleado> opt = empleados.stream().filter(e -> e.getId() == id).findFirst();
        if (!opt.isPresent()) {
            System.out.println("Empleado no encontrado en activos.");
            return;
        }

        Empleado e = opt.get(); // El empleado a mover
        
        // (Rúbrica 8.2.2.1) Mover de una lista a otra (en memoria)
        empleados.remove(e);
        empleadosBaja.add(e);
        
        // (Rúbrica 8.2.2.3) Sobreescribir el fichero empleado.dat (activos)
        guardarEmpleados(empleados, Paths.get(DIR_EMPLEADOS, "empleados.dat").toFile());
        
        // (Rúbrica 8.2.2.2) El fichero empleadosBaja.dat se guardará al salir del menú gestor
        System.out.println("Empleado " + e.getNombre() + " movido a Bajas.");
    }

    /**
     * Reactiva un empleado. Lo mueve de la lista de bajas a la de activos
     * y reescribe empleados.dat (activos).
     * (El fichero de bajas se reescribe al salir del menú gestor).
     * (Rúbrica 8.2.3)
     * @param empleados Lista de activos (se modifica).
     * @param empleadosBaja Lista de bajas (se modifica).
     */
    private static void reactivarEmpleado(List<Empleado> empleados, List<Empleado> empleadosBaja) {
        // (Rúbrica 8.2.3.1) Leer fichero (la lista 'empleadosBaja' ya está cargada)
        System.out.print("ID de empleado a reactivar: ");
        String cs = sc.nextLine().trim();
        if (!cs.matches("\\d+")) { System.out.println("ID inválido."); return; }
        int id = Integer.parseInt(cs);

        // (Rúbrica 8.2.3.2) Obtener el empleado deseado (de la lista de bajas)
        Optional<Empleado> opt = empleadosBaja.stream().filter(e -> e.getId() == id).findFirst();
        if (!opt.isPresent()) {
            System.out.println("Empleado no encontrado en bajas.");
            return;
        }

        Empleado e = opt.get(); // El empleado a mover
        
        // Control de conflicto: no puede haber dos empleados activos con el mismo ID
        if (empleados.stream().anyMatch(emp -> emp.getId() == id)) {
            System.out.println("Error: Ya existe un empleado activo con ese ID.");
            return;
        }
        
        // (Rúbrica 8.2.3.2) Mover de una lista a otra (en memoria)
        empleadosBaja.remove(e);
        empleados.add(e);
        
        // (Rúbrica 8.2.3.3) Sobreescribir fichero bajaEmpleado (se hace al salir del menú gestor)
        
        // Sobreescribir fichero empleados.dat (activos)
        guardarEmpleados(empleados, Paths.get(DIR_EMPLEADOS, "empleados.dat").toFile());
        
        System.out.println("Empleado " + e.getNombre() + " reactivado.");
    }

    // ------------------------ Utilidades ------------------------

    /**
     * Obtiene el siguiente número de ticket secuencial.
     * Busca el número más alto en TICKETS y DEVOLUCIONES y devuelve el siguiente.
     * (Rúbrica 5.6)
     * @return El siguiente número de ticket (max + 1).
     */
    private static int siguienteNumeroTicket() {
        int max = 0; // Máximo número encontrado
        
        // Busca en la carpeta TICKETS
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(DIR_TICKETS), "*.txt")) {
            for (Path p : stream) {
                // Obtiene el nombre del fichero (ej: "123.txt")
                String name = p.getFileName().toString().replace(".txt", "");
                if (name.matches("\\d+")) { // Si es un número
                    max = Math.max(max, Integer.parseInt(name)); // Compara con el máximo
                }
            }
        } catch (IOException e) { /* Ignorar error de lectura */ }
        
        // Busca también en la carpeta DEVOLUCIONES
         try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of("DEVOLUCIONES"), "*.txt")) {
            for (Path p : stream) {
                String name = p.getFileName().toString().replace(".txt", "");
                if (name.matches("\\d+")) {
                    max = Math.max(max, Integer.parseInt(name));
                }
            }
        } catch (IOException e) { /* Ignorar error de lectura */ }
        
        // Devuelve el máximo encontrado + 1
        return max + 1;
    }

    /**
     * Método de utilidad para mostrar una lista de plantas (activas o bajas).
     * @param lista La lista a mostrar.
     */
    private static void mostrarListaPlantas(List<Plantas> lista) {
        if (lista.isEmpty()) {
            System.out.println("La lista está vacía.");
            return;
        }
        for (Plantas p : lista) {
            // Leemos precio/stock del .dat (para bajas será 0/0)
            float precio = leerPrecioDat(p.getCodigo());
            int stock = leerStockDat(p.getCodigo());
             System.out.printf("Cód: %03d | Nombre: %-15s | (Precio DAT: %.2f €, Stock DAT: %d)%n",
                    p.getCodigo(), p.getNombre(), precio, stock);
        }
    }
    
    /**
     * Método de utilidad para mostrar una lista de empleados (activos o bajos).
  
     */
    private static void mostrarListaEmpleados(List<Empleado> lista) {
        if (lista.isEmpty()) {
            System.out.println("La lista está vacía.");
            return;
        }
        for (Empleado e : lista) {
             System.out.printf("ID: %d | Nombre: %-15s | Cargo: %s%n",
                    e.getId(), e.getNombre(), e.getCargo());
        }
    }
}