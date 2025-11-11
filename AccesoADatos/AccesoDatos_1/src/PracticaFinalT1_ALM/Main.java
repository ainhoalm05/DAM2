package PracticaFinalT1_ALM;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Main {

    // ---------------------- CONSTANTES ----------------------
    private static final Scanner sc = new Scanner(System.in);
    private static final int TAMANO = 12; // bytes por registro en plantas.dat (int+float+int)
    private static final String DIR_PLANTAS = "PLANTAS"; //Carpetas de las plantas
    private static final String DIR_EMPLEADOS = "EMPLEADOS"; //Carpeta de los empleados
    private static final String DIR_TICKETS = "TICKETS"; //Carpeta de los tickets

    // ---------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("== INICIO APLICACIÓN VIVERO ==");

        //1 Comprobación / creación de árbol de directorios y ficheros básicos
        if (!comprobarYCrearEstructura()) {
        	System.out.println("Error en estructura de directorios.");
            return;
        }

        List<Empleado> empleados = cargarEmpleados(Paths.get(DIR_EMPLEADOS, "empleados.dat").toFile());
        if (empleados == null) {
        	System.out.println("Error al cargar empleados. Saliendo.");
            return;
        }

        // Mostrar empleados antes del login (útil para ver credenciales en pruebas)
        System.out.println("\n=== EMPLEADOS EN SISTEMA ===");
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            
        } else {
            // Mostrar lista: ID, nombre, cargo y (solo en pruebas) la contraseña
            for (Empleado e : empleados) {
                System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombre() + " | Cargo: " + e.getCargo()
                        + " | Password (pruebas): " + e.getPassword());
            }
        }
        System.out.println("================================\n");

        // 2.1 / 2.2 -> cargar plantas desde XML (campos descriptivos)
        List<Plantas> plantas = cargarPlantasDesdeXML(Paths.get(DIR_PLANTAS, "plantas.xml").toFile());
        if (plantas == null) {
            System.err.println("Error cargando plantas desde XML. Saliendo.");
            return;
        }
        if (plantas.isEmpty()) {
            System.out.println("El catálogo de plantas está vacío. Puedes dar de alta plantas más tarde desde gestor.");
            // No salimos: el gestor podrá añadir.
        }

        // 2.1 Sincronizar precios/stock con plantas.dat (crea registros si faltan)
        sincronizarPlantasDat(plantas);

        // 3) Identificación de usuario (login)
        Empleado logged = login(empleados);
        if (logged == null) {
            System.out.println("No se pudo iniciar sesión. Saliendo.");
            return;
        }

        // 3.2 Mostrar menú según cargo
        if (logged.getCargo().equalsIgnoreCase("vendedor")) {
            menuVendedor(logged, plantas);
        } else if (logged.getCargo().equalsIgnoreCase("gestor")) {
            // cargar ficheros de baja para gestor
            List<Plantas> plantasBaja = cargarPlantasDesdeXML(Paths.get(DIR_PLANTAS, "plantasBaja.xml").toFile());
            List<Empleado> empleadosBaja = cargarEmpleados(Paths.get(DIR_EMPLEADOS, "BAJA", "empleadosBaja.dat").toFile());
            menuGestor(logged, plantas, plantasBaja, empleados, empleadosBaja);
        } else {
            System.out.println("Cargo desconocido. Contacte con administración.");
        }

        // Guardar cambios finales en XML y empleados
        guardarPlantasXML(plantas, Paths.get(DIR_PLANTAS, "plantas.xml").toFile());
        guardarEmpleados(empleados, Paths.get(DIR_EMPLEADOS, "empleados.dat").toFile());

        System.out.println("== FIN APLICACIÓN ==");
    }

    // ------------------------ 1. Estructura y comprobaciones ------------------------

    /**
     * Comprueba y crea la jerarquía de carpetas y ficheros necesarios:
     * PLANTAS, EMPLEADOS, EMPLEADOS/BAJA, TICKETS, DEVOLUCIONES y ficheros básicos.
     */
    private static boolean comprobarYCrearEstructura() {
        try {
            // Carpeta base
            Files.createDirectories(Path.of(DIR_PLANTAS));
            Files.createDirectories(Path.of(DIR_EMPLEADOS));
            Files.createDirectories(Path.of(DIR_EMPLEADOS, "BAJA"));
            Files.createDirectories(Path.of(DIR_TICKETS));
            Files.createDirectories(Path.of("DEVOLUCIONES"));

            // Ficheros que deben existir
            crearFicheroSiNoExiste(Path.of(DIR_PLANTAS, "plantas.xml"));
            crearFicheroSiNoExiste(Path.of(DIR_PLANTAS, "plantasBaja.xml"));
            crearFicheroSiNoExiste(Path.of(DIR_PLANTAS, "plantas.dat"));
            crearFicheroSiNoExiste(Path.of(DIR_PLANTAS, "plantasbaja.dat"));
            crearFicheroSiNoExiste(Path.of(DIR_EMPLEADOS, "empleados.dat"));
            crearFicheroSiNoExiste(Path.of(DIR_EMPLEADOS, "BAJA", "empleadosBaja.dat"));
            // tickets y devoluciones son directorios

            return true;
        } catch (IOException e) {
            System.err.println("Error creando estructura: " + e.getMessage());
            return false;
        }
    }

    private static void crearFicheroSiNoExiste(Path p) throws IOException {
        File f = p.toFile();
        if (!f.exists()) {
            // Si el fichero no existe, lo creamos vacío
            f.createNewFile();
        }
    }

    // ------------------------ 2. Carga de datos ------------------------

    /**
     * Lee empleados desde empleados.dat (serializado ArrayList<Empleado>).
     * Devuelve null en caso de error.
     */
   
    private static ArrayList<Empleado> cargarEmpleados(File f) {
        if (!f.exists() || f.length() == 0) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object o = ois.readObject();
            if (o instanceof ArrayList) {
                return (ArrayList<Empleado>) o;
            } else {
                System.err.println("Formato de empleados.dat no válido.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error leyendo empleados.dat: " + e.getMessage());
            return null;
        }
    }

    /**
     * Sobreescribe empleados.dat con la lista dada.
     */
    private static void guardarEmpleados(List<Empleado> lista, File f) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(new ArrayList<>(lista));
        } catch (IOException e) {
            System.err.println("Error guardando empleados: " + e.getMessage());
        }
    }

    /**
     * Carga plantas desde el XML (solo campos descriptivos).
     * Devuelve null si hay error.
     */
    private static ArrayList<Plantas> cargarPlantasDesdeXML(File xml) {
        ArrayList<Plantas> salida = new ArrayList<>();
        if (!xml.exists() || xml.length() == 0) return salida; // lista vacía, no error fatal

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();
            NodeList nl = doc.getElementsByTagName("planta");
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) node;
                    int codigo = Integer.parseInt(getTagText(el, "codigo", "0"));
                    String nombre = getTagText(el, "nombre", "");
                    String foto = getTagText(el, "foto", "");
                    String descripcion = getTagText(el, "descripcion", "");
                    salida.add(new Plantas(codigo, nombre, foto, descripcion, 0f, 0));
                }
            }
            return salida;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("Error leyendo plantas.xml: " + e.getMessage());
            return null;
        }
    }

    private static String getTagText(Element parent, String tag, String def) {
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl.getLength() == 0) return def;
        return nl.item(0).getTextContent();
    }

    /**
     * Guarda la lista de plantas en el XML (reescribe).
     */
    private static void guardarPlantasXML(List<Plantas> lista, File xml) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.newDocument();
            Element root = doc.createElement("plantas");
            doc.appendChild(root);
            for (Plantas p : lista) {
                Element e = doc.createElement("planta");
                e.appendChild(crearElemento(doc, "codigo", String.valueOf(p.getCodigo())));
                e.appendChild(crearElemento(doc, "nombre", p.getNombre()));
                e.appendChild(crearElemento(doc, "foto", p.getFoto()));
                e.appendChild(crearElemento(doc, "descripcion", p.getDescripcion()));
                root.appendChild(e);
            }
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(doc), new StreamResult(xml));
        } catch (Exception ex) {
            System.err.println("Error guardando plantas.xml: " + ex.getMessage());
        }
    }

    private static Element crearElemento(Document doc, String tag, String valor) {
        Element e = doc.createElement(tag);
        e.appendChild(doc.createTextNode(valor));
        return e;
    }

    // ------------------------ 2.1 Leer/Escribir plantas.dat (acceso directo) ------------------------

    /**
     * Sincroniza cada planta del ArrayList con plantas.dat. Si no existe registro
     * para un código, lo crea con valores por defecto (precio aleatorio y stock).
     */
    private static void sincronizarPlantasDat(List<Plantas> lista) {
        File dat = Paths.get(DIR_PLANTAS, "plantas.dat").toFile();
        try (RandomAccessFile raf = new RandomAccessFile(dat, "rw")) {
            for (Plantas p : lista) {
                long pos = (long) (p.getCodigo() - 1) * TAMANO;
                if (pos + TAMANO <= raf.length()) {
                    raf.seek(pos);
                    int codigo = raf.readInt();
                    float precio = raf.readFloat();
                    int stock = raf.readInt();
                    if (codigo == p.getCodigo()) {
                        p.setPrecio(precio);
                        p.setStock(stock);
                    } else {
                        // inconsistencia: reescribimos
                        raf.seek(pos);
                        float precioDef = preciosPorDefecto(p.getCodigo());
                        int stockDef = stockPorDefecto();
                        raf.writeInt(p.getCodigo());
                        raf.writeFloat(precioDef);
                        raf.writeInt(stockDef);
                        p.setPrecio(precioDef);
                        p.setStock(stockDef);
                    }
                } else {
                    // añadir registro
                    float precioDef = preciosPorDefecto(p.getCodigo());
                    int stockDef = stockPorDefecto();
                    raf.seek(pos);
                    raf.writeInt(p.getCodigo());
                    raf.writeFloat(precioDef);
                    raf.writeInt(stockDef);
                    p.setPrecio(precioDef);
                    p.setStock(stockDef);
                }
            }
        } catch (IOException e) {
            System.err.println("Error accediendo a plantas.dat: " + e.getMessage());
        }
    }

    private static float preciosPorDefecto(int codigo) {
        float n = 5.0f + (codigo % 10) * 1.5f;
        return Math.round(n * 100f) / 100f;
    }

    private static int stockPorDefecto() {
        return 10;
    }

    /**
     * Lee el precio de una planta en plantas.dat (acceso directo). Devuelve 0 si error.
     */
    private static float leerPrecioDat(int codigo) {
        File dat = Paths.get(DIR_PLANTAS, "plantas.dat").toFile();
        try (RandomAccessFile raf = new RandomAccessFile(dat, "r")) {
            raf.seek((long) (codigo - 1) * TAMANO + 4);
            return raf.readFloat();
        } catch (Exception e) {
            return 0f;
        }
    }

    /**
     * Lee el stock de una planta en plantas.dat (acceso directo). Devuelve 0 si error.
     */
    private static int leerStockDat(int codigo) {
        File dat = Paths.get(DIR_PLANTAS, "plantas.dat").toFile();
        try (RandomAccessFile raf = new RandomAccessFile(dat, "r")) {
            raf.seek((long) (codigo - 1) * TAMANO + 8);
            return raf.readInt();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Escribe precio y stock en el registro de plantas.dat para el código indicado.
     */
    private static void escribirRegistroDat(int codigo, float precio, int stock) throws IOException {
        File dat = Paths.get(DIR_PLANTAS, "plantas.dat").toFile();
        try (RandomAccessFile raf = new RandomAccessFile(dat, "rw")) {
            long pos = (long) (codigo - 1) * TAMANO;
            raf.seek(pos);
            raf.writeInt(codigo);
            raf.writeFloat(precio);
            raf.writeInt(stock);
        }
    }

    // ------------------------ 3. Login e identificación ------------------------

    /**
     * Login que controla ID y contraseña. Devuelve empleado logueado o null.
     * Incluye control de errores y mensajes.
     */
    private static Empleado login(List<Empleado> empleados) {
        int intentos = 3;
        while (intentos > 0) {
            System.out.println("\n--- INICIAR SESIÓN ---");
            System.out.print("ID: ");
            String idS = sc.nextLine().trim();
            if (!idS.matches("\\d+")) {
                System.out.println("ID debe ser numérico.");
                intentos--;
                continue;
            }
            int id = Integer.parseInt(idS);
            System.out.print("Contraseña: ");
            String pw = sc.nextLine();
            for (Empleado e : empleados) {
                if (e.getId() == id && e.getPassword().equals(pw)) {
                    return e;
                }
            }
            intentos--;
            System.out.println("Credenciales incorrectas. Intentos restantes: " + intentos);
        }
        return null;
    }

    // ------------------------ 4. Menú Vendedor ------------------------

    private static void menuVendedor(Empleado emp, List<Plantas> listaPlantas) {
        String op;
        do {
            System.out.println("\n--- MENÚ VENDEDOR ---");
            System.out.println("1) Listar catálogo");
            System.out.println("2) Realizar venta");
            System.out.println("3) Devolución");
            System.out.println("4) Buscar ticket");
            System.out.println("0) Cerrar sesión");
            System.out.print("Opción: ");
            op = sc.nextLine().trim();
            switch (op) {
                case "1":
                    listarCatalogo(listaPlantas);
                    break;
                case "2":
                    realizarVenta(emp, listaPlantas);
                    break;
                case "3":
                    procesarDevolucion(emp, listaPlantas);
                    break;
                case "4":
                    buscarTicketPorTeclado();
                    break;
                case "0":
                    System.out.println("Cerrando sesión...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!op.equals("0"));
    }

    /**
     * 4.1 Listar catálogo: une datos xml (listaPlantas) con datos del acceso directo (precio/stock)
     *  - muestra códigos rellenados con ceros a la izquierda (format 3 dígitos por ejemplo)
     *  - redirige a venta si se selecciona el código
     */
    private static void listarCatalogo(List<Plantas> listaPlantas) {
        System.out.println("\n--- CATÁLOGO ---");
        for (Plantas p : listaPlantas) {
            float precio = leerPrecioDat(p.getCodigo());
            int stock = leerStockDat(p.getCodigo());
            // rellenar código con ceros a la izquierda (3 dígitos mínimo)
            String codigoFmt = String.format("%03d", p.getCodigo());
            System.out.printf("Código: %s | Nombre: %s | Precio: %.2f € | Stock: %d%n",
                    codigoFmt, p.getNombre(), precio, stock);
        }
        System.out.print("Introduzca código para ver/ir a venta (0 para volver): ");
        String cod = sc.nextLine().trim();
        if (cod.matches("\\d+")) {
            int codigo = Integer.parseInt(cod);
            if (codigo == 0) return;
            Optional<Plantas> opt = listaPlantas.stream().filter(pl -> pl.getCodigo() == codigo).findFirst();
            if (opt.isPresent()) {
                mostrarFichaVenta(opt.get());
                // preguntar al usuario si quiere vender esa planta
                System.out.print("¿Vender esta planta? (si/no): ");
                if (sc.nextLine().equalsIgnoreCase("si")) {
                    realizarVentaSimple(codigo, listaPlantas);
                }
            } else {
                System.out.println("Código no encontrado.");
            }
        } else {
            System.out.println("Código no válido.");
        }
    }

    private static void mostrarFichaVenta(Plantas p) {
        System.out.println("---- FICHA PLANTA ----");
        System.out.println("Código: " + String.format("%03d", p.getCodigo()));
        System.out.println("Nombre: " + p.getNombre());
        System.out.println("Descripción: " + p.getDescripcion());
        System.out.println("Precio: " + leerPrecioDat(p.getCodigo()) + " €");
        System.out.println("Stock: " + leerStockDat(p.getCodigo()));
    }

    // ------------------------ 5. Realizar venta ------------------------

    /**
     * Realizar venta (modo interactivo con cesta múltiple)
     * Reglas:
     * - permitir repetir la acción hasta que cliente quiera
     * - validar entradas numéricas
     * - comprobar stock en plantas.dat
     * - previsualización ticket
     * - actualizar stock y escribir en plantas.dat
     * - generar ticket secuencial en TICKETS/
     */
    private static void realizarVenta(Empleado emp, List<Plantas> listaPlantas) {
        boolean continuar = true;
        while (continuar) {
            // cesta: codigo -> cantidad
            Map<Integer, Integer> cesta = new LinkedHashMap<>();
            while (true) {
                System.out.print("Introduce código planta (0 para terminar selección): ");
                String cs = sc.nextLine().trim();
                if (!cs.matches("\\d+")) {
                    System.out.println("Código inválido.");
                    continue;
                }
                int codigo = Integer.parseInt(cs);
                if (codigo == 0) break;
                Optional<Plantas> op = listaPlantas.stream().filter(p -> p.getCodigo() == codigo).findFirst();
                if (!op.isPresent()) {
                    System.out.println("Código no encontrado.");
                    continue;
                }
                int stock = leerStockDat(codigo);
                if (stock <= 0) {
                    System.out.println("Stock 0. No disponible.");
                    continue;
                }
                System.out.print("Cantidad: ");
                String cantS = sc.nextLine().trim();
                if (!cantS.matches("\\d+")) {
                    System.out.println("Cantidad inválida.");
                    continue;
                }
                int cantidad = Integer.parseInt(cantS);
                if (cantidad <= 0) {
                    System.out.println("Cantidad debe ser mayor que cero.");
                    continue;
                }
                if (cantidad > stock) {
                    System.out.println("No hay suficientes unidades. Stock: " + stock);
                    continue;
                }
                cesta.put(codigo, cesta.getOrDefault(codigo, 0) + cantidad);
                System.out.print("¿Añadir otra planta? (si/no): ");
                if (!sc.nextLine().equalsIgnoreCase("si")) break;
            }

            if (cesta.isEmpty()) {
                System.out.println("No se seleccionó ningún artículo. Venta cancelada.");
                return;
            }

            // 5.3 Mostrar resumen compra (previsualización ticket)
            System.out.println("\n--- RESUMEN COMPRA ---");
            float total = 0f;
            List<String> lineas = new ArrayList<>();
            for (Map.Entry<Integer, Integer> e : cesta.entrySet()) {
                int cod = e.getKey();
                int cant = e.getValue();
                float precio = leerPrecioDat(cod);
                float subtotal = precio * cant;
                total += subtotal;
                System.out.printf("Código %03d  Cantidad %d  Precio unit: %.2f  Subtotal: %.2f%n", cod, cant, precio, subtotal);
                lineas.add(String.format("%03d %d %.2f", cod, cant, precio));
            }
            System.out.printf("TOTAL A PAGAR: %.2f €%n", total);

            // 5.4 Confirmar venta y actualizar stock
            System.out.print("Confirmar venta? (Aceptar para confirmar): ");
            String conf = sc.nextLine();
            if (conf.equalsIgnoreCase("Aceptar")) {
                // Modify stock in dat
                for (Map.Entry<Integer, Integer> e : cesta.entrySet()) {
                    int cod = e.getKey();
                    int cant = e.getValue();
                    int stockActual = leerStockDat(cod);
                    int nuevo = stockActual - cant;
                    try {
                        escribirRegistroDat(cod, leerPrecioDat(cod), nuevo);
                    } catch (IOException ex) {
                        System.err.println("Error actualizando stock para código " + cod);
                    }
                    // if stock reaches 0, write to bajas
                    if (nuevo <= 0) {
                        darDeBajaPlanta(cod);
                    }
                }

                // 5.5 Generar ticket (secuencial)
                int num = siguienteNumeroTicket();
                Ticket ticket = new Ticket(num);
                ArrayList<String> contenido = new ArrayList<>();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                contenido.add("TICKET Nº " + num);
                contenido.add("Fecha: " + LocalDateTime.now().format(fmt));
                contenido.add("Empleado ID: " + emp.getId() + " Nombre: " + emp.getNombre());
                contenido.add("---------------------------------");
                for (String l : lineas) contenido.add(l);
                contenido.add("---------------------------------");
                contenido.add(String.format("TOTAL: %.2f €", total));
                // escribir fichero
                if (ticket.crearTicket(contenido)) {
                    System.out.println("Ticket generado: " + num);
                } else {
                    System.err.println("Error generando ticket.");
                }
            } else {
                System.out.println("Venta cancelada.");
            }

            System.out.print("¿Realizar otra venta? (si/no): ");
            if (!sc.nextLine().equalsIgnoreCase("si")) continuar = false;
        }
    }

    // realizar venta rápida para redirección desde catálogo (una planta)
    private static void realizarVentaSimple(int codigo, List<Plantas> listaPlantas) {
        Optional<Plantas> opt = listaPlantas.stream().filter(p -> p.getCodigo() == codigo).findFirst();
        if (!opt.isPresent()) {
            System.out.println("No existe la planta.");
            return;
        }
        Plantas p = opt.get();
        System.out.print("Cantidad: ");
        String s = sc.nextLine().trim();
        if (!s.matches("\\d+")) {
            System.out.println("Cantidad inválida.");
            return;
        }
        int cantidad = Integer.parseInt(s);
        int stock = leerStockDat(codigo);
        if (cantidad > stock) {
            System.out.println("Stock insuficiente.");
            return;
        }
        // crear un ticket sencillo con empleado ficticio (0)
        Ticket t = new Ticket(siguienteNumeroTicket());
        ArrayList<String> contenido = new ArrayList<>();
        contenido.add("TICKET RAPIDO");
        contenido.add("Planta: " + p.getNombre());
        contenido.add("Cantidad: " + cantidad);
        contenido.add("Total: " + (leerPrecioDat(codigo) * cantidad));
        t.crearTicket(contenido);
        try {
            escribirRegistroDat(codigo, leerPrecioDat(codigo), stock - cantidad);
        } catch (IOException ex) {
            System.err.println("Error actualizando stock.");
        }
        System.out.println("Venta rápida completada.");
    }

    // ------------------------ 6. Devolución ------------------------

    /**
     * 6) Devolución: buscar ticket, escribir línea "-- DEVOLUCIÓN --", invertir total,
     * modificar stock y mover ticket a DEVOLUCIONES/
     */
    private static void procesarDevolucion(Empleado emp, List<Plantas> listaPlantas) {
        System.out.print("Número de ticket a devolver: ");
        String numS = sc.nextLine().trim();
        if (!numS.matches("\\d+")) {
            System.out.println("Número inválido.");
            return;
        }
        int num = Integer.parseInt(numS);
        File ticketFile = Paths.get(DIR_TICKETS, num + ".txt").toFile();
        if (!ticketFile.exists()) {
            System.out.println("Ticket no encontrado en TICKETS.");
            return;
        }
        try {
            List<String> lines = Files.readAllLines(ticketFile.toPath());
            // buscar total
            float total = 0f;
            for (String l : lines) {
                if (l.trim().toUpperCase().startsWith("TOTAL")) {
                    // formato "TOTAL: 12.34 €" o "TOTAL 12.34 €"
                    String numStr = l.replaceAll("[^0-9,\\.\\-]", "").replace(",", ".");
                    try { total = Float.parseFloat(numStr); } catch (Exception ex) { total = 0f; }
                    break;
                }
            }
            // añadir linea devolución y convertir total a negativo
            lines.add("-- DEVOLUCIÓN --");
            for (int i = 0; i < lines.size(); i++) {
                String l = lines.get(i);
                if (l.trim().toUpperCase().startsWith("TOTAL")) {
                    lines.set(i, String.format("TOTAL: -%.2f €", Math.abs(total)));
                    break;
                }
            }
            // parsear líneas con artículos para recuperar stock (esperamos formato "003 2 5.00" o "Nombre x Cant =")
            // intentaremos encontrar líneas con "codigo cantidad precio" (se añadieron así en crear ticket)
            for (String l : lines) {
                String s = l.trim();
                if (s.matches("^\\d{1,} \\d+ .*")) {
                    String[] parts = s.split("\\s+");
                    int codigo = Integer.parseInt(parts[0]);
                    int cantidad = Integer.parseInt(parts[1]);
                    // sumar stock
                    int stock = leerStockDat(codigo);
                    try {
                        escribirRegistroDat(codigo, leerPrecioDat(codigo), stock + cantidad);
                    } catch (IOException ex) {
                        System.err.println("Error devolviendo stock para código " + codigo);
                    }
                }
            }
            // mover fichero a DEVOLUCIONES y sobrescribir con contenido modificado
            Path destino = Paths.get("DEVOLUCIONES", num + ".txt");
            Files.write(destino, lines);
            Files.delete(ticketFile.toPath());
            System.out.println("Devolución procesada y ticket movido a DEVOLUCIONES.");
        } catch (IOException ex) {
            System.err.println("Error procesando devolución: " + ex.getMessage());
        }
    }

    // ------------------------ 7. Búsqueda de tickets ------------------------

    private static void buscarTicketPorTeclado() {
        System.out.print("Introduce número de ticket a buscar: ");
        String s = sc.nextLine().trim();
        if (!s.matches("\\d+")) {
            System.out.println("Número inválido.");
            return;
        }
        buscarTicket(Integer.parseInt(s));
    }

    private static void buscarTicket(int numero) {
        File t = Paths.get(DIR_TICKETS, numero + ".txt").toFile();
        File d = Paths.get("DEVOLUCIONES", numero + ".txt").toFile();
        try {
            if (t.exists()) {
                System.out.println("--- Contenido ticket " + numero + " ---");
                Files.lines(t.toPath()).forEach(System.out::println);
            } else if (d.exists()) {
                System.out.println("--- Contenido devuelto " + numero + " ---");
                Files.lines(d.toPath()).forEach(System.out::println);
            } else {
                System.out.println("Ticket no encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Error leyendo ticket: " + e.getMessage());
        }
    }

    // ------------------------ 8. Menú Gestor (Plantas y Empleados) ------------------------

    private static void menuGestor(Empleado gestor, List<Plantas> plantas, List<Plantas> plantasBaja,
                                  List<Empleado> empleados, List<Empleado> empleadosBaja) {
        String op;
        do {
            System.out.println("\n--- MENÚ GESTOR ---");
            System.out.println("1) Gestionar Plantas");
            System.out.println("2) Gestionar Empleados");
            System.out.println("3) Estadísticas");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            op = sc.nextLine().trim();
            switch (op) {
                case "1":
                    gestionarPlantas((ArrayList<Plantas>) plantas, (ArrayList<Plantas>) plantasBaja);
                    break;
                case "2":
                    gestionarEmpleados((ArrayList<Empleado>) empleados, (ArrayList<Empleado>) empleadosBaja);
                    break;
                case "3":
                    mostrarEstadisticas();
                    break;
                case "0":
                    System.out.println("Saliendo de gestor...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!op.equals("0"));
    }

    // 8.1 Gestión de plantas
    private static void gestionarPlantas(ArrayList<Plantas> plantas, ArrayList<Plantas> plantasBaja) {
        String op;
        do {
            System.out.println("\n--- GESTIÓN PLANTAS ---");
            System.out.println("1) Alta planta");
            System.out.println("2) Baja planta");
            System.out.println("3) Rescatar planta (dar stock)");
            System.out.println("4) Listar plantas");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            op = sc.nextLine().trim();
            switch (op) {
                case "1":
                    altaPlanta(plantas);
                    break;
                case "2":
                    bajaPlanta(plantas, plantasBaja);
                    break;
                case "3":
                    rescatarPlanta(plantas, plantasBaja);
                    break;
                case "4":
                    listarCatalogo((ArrayList<Plantas>) plantas);
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!op.equals("0"));
    }

    /**
     * 8.1.1 Alta planta:
     * - añadir al array
     * - escribir registro en plantas.dat
     * - sobrescribir XML al finalizar (guardado inmediato aquí)
     */
    private static void altaPlanta(List<Plantas> plantas) {
        System.out.print("Código nueva planta: ");
        String cs = sc.nextLine().trim();
        if (!cs.matches("\\d+")) {
            System.out.println("Código inválido.");
            return;
        }
        int codigo = Integer.parseInt(cs);
        if (plantas.stream().anyMatch(p -> p.getCodigo() == codigo)) {
            System.out.println("Código ya existente.");
            return;
        }
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Foto: ");
        String foto = sc.nextLine().trim();
        System.out.print("Descripción: ");
        String desc = sc.nextLine().trim();
        System.out.print("Precio: ");
        String sp = sc.nextLine().trim();
        if (!sp.matches("\\d+(\\.\\d+)?")) {
            System.out.println("Precio inválido.");
            return;
        }
        float precio = Float.parseFloat(sp);
        System.out.print("Stock: ");
        String ss = sc.nextLine().trim();
        if (!ss.matches("\\d+")) {
            System.out.println("Stock inválido.");
            return;
        }
        int stock = Integer.parseInt(ss);
        Plantas p = new Plantas(codigo, nombre, foto, desc, precio, stock);
        plantas.add(p);
        // escribir en dat
        try {
            escribirRegistroDat(codigo, precio, stock);
        } catch (IOException e) {
            System.err.println("Error escribiendo plantas.dat: " + e.getMessage());
        }
        // guardar xml
        guardarPlantasXML(plantas, Paths.get(DIR_PLANTAS, "plantas.xml").toFile());
        System.out.println("Planta dada de alta correctamente.");
    }

    /**
     * 8.1.2 Baja planta:
     * - comprobar stock y poner precio=0 stock=0 en dat
     * - escribir linea en plantasbaja.dat con codigo y precio anterior
     * - añadir a plantasBaja.xml
     */
    private static void bajaPlanta(List<Plantas> plantas, List<Plantas> plantasBaja) {
        System.out.print("Código planta a dar de baja: ");
        String cs = sc.nextLine().trim();
        if (!cs.matches("\\d+")) { System.out.println("Código inválido."); return; }
        int codigo = Integer.parseInt(cs);
        Optional<Plantas> opt = plantas.stream().filter(p -> p.getCodigo() == codigo).findFirst();
        if (!opt.isPresent()) { System.out.println("Planta no encontrada."); return; }
        int stock = leerStockDat(codigo);
        float precio = leerPrecioDat(codigo);
        // escribir en bajas dat
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(
                new FileOutputStream(Paths.get(DIR_PLANTAS, "plantasbaja.dat").toFile(), true)))) {
            dos.writeInt(codigo);
            dos.writeFloat(precio);
            dos.writeInt(stock);
        } catch (IOException e) {
            System.err.println("Error escribiendo plantasbaja.dat: " + e.getMessage());
        }
        // poner a 0 en dat
        try {
            escribirRegistroDat(codigo, 0f, 0);
        } catch (IOException e) {
            System.err.println("Error actualizando registro: " + e.getMessage());
        }
        // añadir a lista de bajas y guardar XML de bajas
        Plantas p = opt.get();
        plantasBaja.add(p);
        guardarPlantasXML(plantasBaja, Paths.get(DIR_PLANTAS, "plantasBaja.xml").toFile());
        // eliminar de lista principal
        plantas.remove(p);
        guardarPlantasXML(plantas, Paths.get(DIR_PLANTAS, "plantas.xml").toFile());
        System.out.println("Planta dada de baja correctamente.");
    }

    /**
     * 8.1.3 Rescatar planta (dar stock) desde plantasbaja.dat y plantasBaja.xml
     * - el usuario introduce código
     * - comprobación numérica
     * - leer precio del fichero de bajas (binario) y volver a escribir en dat
     * - eliminar de plantasBaja.xml la planta rescatada
     */
    private static void rescatarPlanta(List<Plantas> plantas, List<Plantas> plantasBaja) {
        System.out.print("Código planta a rescatar: ");
        String cs = sc.nextLine().trim();
        if (!cs.matches("\\d+")) { System.out.println("Código inválido."); return; }
        int codigo = Integer.parseInt(cs);
        // leer plantasbaja.dat para buscar precio
        File f = Paths.get(DIR_PLANTAS, "plantasbaja.dat").toFile();
        boolean encontrado = false;
        float precioEncontrado = 0f;
        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
            long len = raf.length();
            long puntero = 0;
            while (puntero + 12 <= len) {
                raf.seek(puntero);
                int cod = raf.readInt();
                float precio = raf.readFloat();
                int stock = raf.readInt();
                if (cod == codigo) {
                    precioEncontrado = precio;
                    encontrado = true;
                    break;
                }
                puntero += 12;
            }
        } catch (IOException e) {
            System.err.println("Error leyendo plantasbaja.dat");
            return;
        }
        if (!encontrado) {
            System.out.println("Código no encontrado en fichero de bajas.");
            return;
        }
        // pedir nuevo stock
        System.out.print("Introducir stock a restaurar: ");
        String ss = sc.nextLine().trim();
        if (!ss.matches("\\d+")) { System.out.println("Stock inválido."); return; }
        int stockNuevo = Integer.parseInt(ss);
        // escribir en plantas.dat
        try {
            escribirRegistroDat(codigo, precioEncontrado, stockNuevo);
            // añadir a listado principal (si no existía) y eliminar de plantasBaja.xml
            Optional<Plantas> op = plantas.stream().filter(pl -> pl.getCodigo() == codigo).findFirst();
            if (!op.isPresent()) {
                // intentar recuperar desde plantasBaja.xml
                List<Plantas> bajasXml = cargarPlantasDesdeXML(Paths.get(DIR_PLANTAS, "plantasBaja.xml").toFile());
                Optional<Plantas> rec = bajasXml.stream().filter(pl -> pl.getCodigo() == codigo).findFirst();
                if (rec.isPresent()) {
                    Plantas p = rec.get();
                    p.setPrecio(precioEncontrado);
                    p.setStock(stockNuevo);
                    plantas.add(p);
                    // eliminar de bajasXml
                    bajasXml.remove(p);
                    guardarPlantasXML(bajasXml, Paths.get(DIR_PLANTAS, "plantasBaja.xml").toFile());
                } else {
                    // crear nuevo registro mínimo
                    Plantas p = new Plantas(codigo, "PLANTA_" + codigo, "", "", precioEncontrado, stockNuevo);
                    plantas.add(p);
                }
            } else {
                op.get().setPrecio(precioEncontrado);
                op.get().setStock(stockNuevo);
            }
            guardarPlantasXML(plantas, Paths.get(DIR_PLANTAS, "plantas.xml").toFile());
            System.out.println("Planta rescatada y stock actualizado.");
        } catch (IOException e) {
            System.err.println("Error escribiendo plantas.dat: " + e.getMessage());
        }
    }

    // 8.2 Gestión empleados
    private static void gestionarEmpleados(ArrayList<Empleado> empleados, ArrayList<Empleado> empleadosBaja) {
        String op;
        do {
            System.out.println("\n--- GESTIÓN EMPLEADOS ---");
            System.out.println("1) Alta empleado");
            System.out.println("2) Baja empleado");
            System.out.println("3) Recuperar empleado");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            op = sc.nextLine().trim();
            switch (op) {
                case "1":
                    altaEmpleado(empleados);
                    break;
                case "2":
                    bajaEmpleado(empleados, empleadosBaja);
                    break;
                case "3":
                    recuperarEmpleado(empleados, empleadosBaja);
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (!op.equals("0"));
    }

    /**
     * 8.2.1 Alta empleado: validación de datos y escritura en empleados.dat
     */
    private static void altaEmpleado(List<Empleado> empleados) {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Password (5-7 chars): ");
        String pw = sc.nextLine().trim();
        if (pw.length() < 5 || pw.length() > 7) {
            System.out.println("Password no válida.");
            return;
        }
        System.out.print("Cargo (vendedor/gestor): ");
        String cargo = sc.nextLine().trim().toLowerCase();
        if (!cargo.equals("vendedor") && !cargo.equals("gestor")) {
            System.out.println("Cargo inválido.");
            return;
        }
        int id = generarIdEmpleado(empleados);
        Empleado nuevo = new Empleado(id, nombre, pw, cargo);
        empleados.add(nuevo);
        guardarEmpleados(empleados, Paths.get(DIR_EMPLEADOS, "empleados.dat").toFile());
        System.out.println("Empleado creado con ID: " + id);
    }

 // Genera un ID único para un nuevo empleado
    private static int generarIdEmpleado(List<Empleado> lista) {

        // 1. Si la lista está vacía o es nula,
        // devolvemos 1000 como primer ID disponible.
        if (lista == null || lista.isEmpty()) return 1000;

        // 2. Creamos un objeto Random para generar números aleatorios
        Random r = new Random();

        // 3. Variable donde almacenaremos el id generado
        int id;

        // 4. Variable boolean para saber si el id ya existe
        boolean repetido;

        // 5. Entramos en un bucle que repetirá el proceso
        // hasta generar un id NO repetido
        do {

            // 5.1 Generamos un número entre 1000 y 9999
            id = 1000 + r.nextInt(9000);

            // 5.2 Asumimos inicialmente que NO está repetido
            repetido = false;

            // 5.3 Recorremos toda la lista de empleados
            // para verificar si el ID ya está siendo usado
            for (Empleado e : lista) {

                // 5.4 Si encontramos un empleado con ese ID
                if (e.getId() == id) {
                    repetido = true;  // lo marcamos como repetido
                    break;            // salimos del bucle interno
                }
            }

            // 6. El bucle se repetirá mientras haya repetido == true
        } while (repetido);

        // 7. Si llegamos aquí significa que el id es único,
        // así que lo devolvemos
        return id;
    }


    /**
     * 8.2.2 Baja empleado: eliminar de lista alta y añadir a fichero bajas
     */
    private static void bajaEmpleado(List<Empleado> empleados, List<Empleado> empleadosBaja) {
        System.out.print("ID empleado a dar de baja: ");
        String s = sc.nextLine().trim();
        if (!s.matches("\\d+")) { System.out.println("ID inválido."); return; }
        int id = Integer.parseInt(s);
        Optional<Empleado> opt = empleados.stream().filter(e -> e.getId() == id).findFirst();
        if (!opt.isPresent()) { System.out.println("Empleado no encontrado."); return; }
        Empleado e = opt.get();
        empleados.remove(e);
        empleadosBaja.add(e);
        // sobrescribir fichero bajas
        guardarEmpleados(empleadosBaja, Paths.get(DIR_EMPLEADOS, "BAJA", "empleadosBaja.dat").toFile());
        // sobrescribir fichero altas
        guardarEmpleados(empleados, Paths.get(DIR_EMPLEADOS, "empleados.dat").toFile());
        System.out.println("Empleado dado de baja correctamente.");
    }

    /**
     * 8.2.3 Recuperar empleado: leer fichero bajas, obtener empleado, eliminarlo de bajas y añadirlo a altas.
     */
    private static void recuperarEmpleado(List<Empleado> empleados, List<Empleado> empleadosBaja) {
        System.out.print("ID empleado a recuperar: ");
        String s = sc.nextLine().trim();
        if (!s.matches("\\d+")) { System.out.println("ID inválido."); return; }
        int id = Integer.parseInt(s);
        Optional<Empleado> opt = empleadosBaja.stream().filter(e -> e.getId() == id).findFirst();
        if (!opt.isPresent()) { System.out.println("Empleado no encontrado en bajas."); return; }
        Empleado e = opt.get();
        empleadosBaja.remove(e);
        empleados.add(e);
        guardarEmpleados(empleadosBaja, Paths.get(DIR_EMPLEADOS, "BAJA", "empleadosBaja.dat").toFile());
        guardarEmpleados(empleados, Paths.get(DIR_EMPLEADOS, "empleados.dat").toFile());
        System.out.println("Empleado recuperado correctamente.");
    }

    // ------------------------ 8.3 Estadísticas ------------------------

    /**
     * 8.3.1 Total recaudado y 8.3.2 plantas más vendidas
     * - lee todos los tickets en TICKETS/, suma totales y cuenta cantidades por planta
     */
    private static void mostrarEstadisticas() {
        File carpeta = Paths.get(DIR_TICKETS).toFile();
        if (!carpeta.exists()) { System.out.println("No hay tickets."); return; }
        File[] files = carpeta.listFiles((d, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) { System.out.println("No hay tickets."); return; }

        double totalRecaudado = 0.0;
        Map<Integer, Integer> vendidosPorPlanta = new HashMap<>();

        for (File f : files) {
            try {
                List<String> lines = Files.readAllLines(f.toPath());
                for (String line : lines) {
                    String l = line.trim();
                    // detectar líneas de tipo "003 2 5.00"
                    if (l.matches("^\\d+\\s+\\d+\\s+.*")) {
                        String[] parts = l.split("\\s+");
                        int codigo = Integer.parseInt(parts[0]);
                        int cantidad = Integer.parseInt(parts[1]);
                        vendidosPorPlanta.put(codigo, vendidosPorPlanta.getOrDefault(codigo, 0) + cantidad);
                    } else if (l.toUpperCase().startsWith("TOTAL")) {
                        String numStr = l.replaceAll("[^0-9,\\.\\-]", "").replace(",", ".");
                        try { totalRecaudado += Double.parseDouble(numStr); } catch (Exception ex) {}
                    }
                }
            } catch (IOException e) {
                System.err.println("Error leyendo ticket " + f.getName());
            }
        }

        System.out.printf("Total recaudado: %.2f €%n", totalRecaudado);
        // planta más vendida
        List<Map.Entry<Integer,Integer>> orden = vendidosPorPlanta.entrySet().stream()
                .sorted((a,b) -> b.getValue() - a.getValue())
                .collect(Collectors.toList());
        if (!orden.isEmpty()) {
            Map.Entry<Integer,Integer> top = orden.get(0);
            System.out.println("Planta más vendida: Código " + top.getKey() + " - Unidades: " + top.getValue());
        } else {
            System.out.println("No hay ventas registradas aún.");
        }
    }

    // ------------------------ utilidades ------------------------

    private static int siguienteNumeroTicket() {
        try {
            long t = Files.list(Path.of(DIR_TICKETS)).count();
            long d = Files.list(Path.of("DEVOLUCIONES")).count();
            return (int) (t + d + 1);
        } catch (IOException e) {
            return 1;
        }
    }

    private static int numeroTicket() {
        try {
            long t = Files.list(Path.of(DIR_TICKETS)).count();
            long d = Files.list(Path.of("DEVOLUCIONES")).count();
            return (int) (t + d);
        } catch (IOException e) {
            return 0;
        }
    }

    // Dar de baja una planta por código (escribe en plantasbaja.dat y en plantasBaja.xml)
    private static void darDeBajaPlanta(int codigo) {
        // leer precio actual
        float precio = leerPrecioDat(codigo);
        // escribir en plantasbaja.dat (append)
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(
                new FileOutputStream(Paths.get(DIR_PLANTAS, "plantasbaja.dat").toFile(), true)))) {
            dos.writeInt(codigo);
            dos.writeFloat(precio);
            dos.writeInt(0);
        } catch (IOException e) {
            System.err.println("Error registrando planta en bajas: " + e.getMessage());
        }
        // opcional: añadir a plantasBaja.xml (hemos implementado rescatarPlanta que lo lee)
    }
    
}
