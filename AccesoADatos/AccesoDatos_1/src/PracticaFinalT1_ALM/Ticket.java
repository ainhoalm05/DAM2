package PracticaFinalT1_ALM; // paquete del proyecto

import java.io.*; // import para IO
import java.nio.file.*; // import para manejo de Paths
import java.time.LocalDateTime; // para fecha/hora
import java.time.format.DateTimeFormatter; // formateo de fecha/hora
import java.util.*; // utilidades: List, ArrayList, etc.
import java.util.regex.Matcher; // regex auxiliar
import java.util.regex.Pattern; // regex auxiliar

// Clase que representa un ticket identificado por un número
public class Ticket {

    private int numero; // número secuencial del ticket (coincide con el nombre del archivo)

    // Constructor que fija el número del ticket
    public Ticket(int numero) {
        this.numero = numero; // asigna número
    }

    // Getter del número de ticket
    public int getNumero() {
        return numero; // devuelve el número
    }

    /**
     * Escribe el contenido del ticket a partir de las plantas vendidas, cantidades y empleado.
     * Devuelve un ArrayList de líneas que luego serán escritas por crearTicket().
     * Formato interno de líneas de artículos: "codigo cantidad precioUnit"
     */
    public ArrayList<String> escribirTicket(List<Plantas> plantasVendidas, List<Integer> cantidades, Empleado empleado) {
        ArrayList<String> lineas = new ArrayList<>(); // lista de líneas del ticket
        // Cabecera con número y fecha
        lineas.add("TICKET Nº " + numero); // línea con número
        lineas.add("Fecha: " + LocalDateTime.now()); // fecha/hora de emisión
        // Empleado que realiza la venta
        lineas.add("Empleado: " + empleado.getId() + " - " + empleado.getNombre()); // id y nombre
        lineas.add("----------------------------------------"); // separador
        // Cuerpo: lista de artículos
        float total = 0f; // acumulador del total
        for (int i = 0; i < plantasVendidas.size(); i++) { // recorrer las plantas vendidas
            Plantas p = plantasVendidas.get(i); // planta actual
            int cantidad = cantidades.get(i); // cantidad vendida
            float precioUnit = p.getPrecio(); // precio unitario (se asume sincronizado)
            float subtotal = precioUnit * cantidad; // subtotal
            // construimos línea con "codigo cantidad precioUnit subtotal" para mayor claridad
            lineas.add(String.format("%03d %d %.2f %.2f", p.getCodigo(), cantidad, precioUnit, subtotal));
            total += subtotal; // sumar subtotal al total
        }
        lineas.add("----------------------------------------"); // separador final
        lineas.add(String.format("TOTAL: %.2f €", total)); // línea con total
        return lineas; // devuelve la lista de cadenas
    }

    /**
     * Crea el fichero físico del ticket en la carpeta TICKETS (nombre: numero.txt).
     * Devuelve true si se escribió correctamente.
     */
    public boolean crearTicket(List<String> contenido) {
        // carpeta TICKETS (si no existe, se crea)
        Path dir = Paths.get("TICKETS"); // ruta relativa
        if (!Files.exists(dir)) {
            try {
                Files.createDirectories(dir); // crear carpeta si hace falta
            } catch (IOException e) {
                return false; // fallo en creación
            }
        }
        // archivo destino
        Path destino = dir.resolve("Ticket"+numero + ".txt"); // TICKETS/numero.txt
        try (BufferedWriter bw = Files.newBufferedWriter(destino)) { // abrir writer
            for (String linea : contenido) { // escribir cada línea
                bw.write(linea); // escribe la línea
                bw.newLine(); // salto de línea
            }
            bw.flush(); // asegurar escritura
            return true; // éxito
        } catch (IOException e) {
            return false; // fallo de IO
        }
    }

    /**
     * Devuelve el contenido del ticket en formato texto.
     * Busca en TICKETS/ y en DEVOLUCIONES/ (si fue movido).
     */
    public String devolverContenido() {
        // buscar en TICKETS y en DEVOLUCIONES
        Path p1 = Paths.get("TICKETS", numero + ".txt"); // ruta en tickets
        Path p2 = Paths.get("DEVOLUCIONES", numero + ".txt"); // ruta en devoluciones
        Path fuente = null; // fichero a leer
        if (Files.exists(p1)) {
        	fuente = p1; // si existe en tickets
        }
        else if (Files.exists(p2)) {
        	fuente = p2; // si existe en devoluciones
        }
        else {
        	return "Ticket no encontrado."; // no existe en ninguna carpeta
        }
        // leer archivo y devolver su contenido completo
        try {
            List<String> lineas = Files.readAllLines(fuente); // leer todas las líneas
            StringBuilder sb = new StringBuilder(); // construir resultado
            for (String l : lineas) { // concatenar
                sb.append(l).append(System.lineSeparator()); // agregar salto
            }
            return sb.toString(); // devolver texto completo
        } catch (IOException e) {
            return "Error leyendo ticket."; // error de IO
        }
    }

    /**
     * Marca el ticket como devuelto: mueve el fichero a DEVOLUCIONES y actualiza el stock en plantas.dat sumando las cantidades devueltas.
     * Devuelve la lista de plantas actualizada (si procede).
     */
    public ArrayList<Plantas> marcarDevuelto(ArrayList<Plantas> plantas) {
        // ruta del ticket en TICKETS
        Path ticketPath = Paths.get("TICKETS", numero + ".txt"); // archivo actual
        Path destPath = Paths.get("DEVOLUCIONES", numero + ".txt"); // destino para devoluciones
        if (!Files.exists(ticketPath)) {
            // si no existe el ticket, intentar en devoluciones (ya devuelto)
            return plantas; // nada que hacer
        }
        try {
            // leer líneas del ticket
            List<String> lines = Files.readAllLines(ticketPath);
            // por cada línea que tenga formato "codigo cantidad ..." actualizamos stock
            Pattern p = Pattern.compile("^(\\d+)\\s+(\\d+)\\s+.*$"); // regex para codigo y cantidad
            for (String l : lines) {
                Matcher m = p.matcher(l); // aplicar regex
                if (m.matches()) {
                    int codigo = Integer.parseInt(m.group(1)); // primer grupo = codigo
                    int cantidad = Integer.parseInt(m.group(2)); // segundo grupo = cantidad
                    // leer stock actual desde PLANTAS/plantas.dat y sumarle cantidad
                    File dat = Paths.get("PLANTAS", "plantas.dat").toFile(); // ruta al dat
                    try (RandomAccessFile raf = new RandomAccessFile(dat, "rw")) {
                        long pos = (long) (codigo - 1) * 12 + 8; // offset del stock dentro del registro
                        if (pos < raf.length()) {
                            raf.seek(pos); // posicionamos en el stock
                            int stockActual = raf.readInt(); // leemos stock actual
                            raf.seek(pos); // volver a posicionar para escribe
                            raf.writeInt(stockActual + cantidad); // actualizar stock sumando devuelto
                        }
                    } catch (IOException ioe) {
                        // si hay error al actualizar stock, se continúa con siguientes líneas
                    }
                    // además, actualizar en la lista de Plantas en memoria si existe
                    for (Plantas pl : plantas) {
                        if (pl.getCodigo() == codigo) {
                            pl.setStock(pl.getStock() + cantidad); // sumar cantidad devuelta
                            break;
                        }
                    }
                }
            }
            // escribir línea adicional "-- DEVOLUCIÓN --" al final del ticket y moverlo a DEVOLUCIONES
            List<String> newLines = new ArrayList<>(lines); // copia de contenido original
            newLines.add("-- DEVOLUCIÓN --"); // añadimos indicador de devolución
            // escribir en carpeta DEVOLUCIONES
            Files.createDirectories(Paths.get("DEVOLUCIONES")); // asegurar carpeta
            Files.write(destPath, newLines); // escribir nuevo fichero en devoluciones
            // borrar fichero original en TICKETS
            Files.delete(ticketPath); // eliminar original
            return plantas; // devolver lista actualizada
        } catch (IOException e) {
            // en caso de error no rompemos la aplicación; devolvemos la lista tal cual
            return plantas;
        }
    }
}
