package PracticaFinal;


	import java.io.File;
	import java.io.RandomAccessFile;
	import java.util.ArrayList;
	import java.util.Scanner;

	
	public class plantas {

	    // Cada registro tiene 12 bytes: int(4) + float(4) + int(4)
	    private static final int TAMANO = 12;

	    /**
	     * Crea el archivo plantas.dat a partir del ArrayList de plantas
	     * Se pide al usuario introducir precio y stock de cada planta
	     */
	    public static void crearArchivo(ArrayList<planta> listaPlantas) {
	        Scanner sc = new Scanner(System.in);
	       	        

	        try (RandomAccessFile raf = new RandomAccessFile("plantas.dat", "rw")) {
  
	            System.out.println("=== CREACIÓN DE PLANTAS.DAT ===");
	            for (planta p : listaPlantas) {
	                System.out.println("\nIntroduce datos para la planta: " + p.getNombre());
	                System.out.print("Precio: ");
	                float precio = sc.nextFloat();
	                System.out.print("Stock: ");
	                int stock = sc.nextInt();

	                raf.writeInt(p.getCodigo());
	                raf.writeFloat(precio);
	                raf.writeInt(stock);
	            }

	            System.out.println("\nArchivo plantas.dat creado correctamente.");

	        } catch (Exception e) {
	            System.out.println("Error al crear plantas.dat");
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Lee todos los registros del archivo plantas.dat
	     */
	    public static void leerArchivo() {
	        try (RandomAccessFile raf = new RandomAccessFile("plantas.dat", "r")) {
	            System.out.println("\n=== CONTENIDO DE PLANTAS.DAT ===");

	            while (raf.getFilePointer() < raf.length()) {
	                int codigo = raf.readInt();
	                float precio = raf.readFloat();
	                int stock = raf.readInt();

	                System.out.println("Código: " + codigo + " | Precio: " + precio + " | Stock: " + stock);
	            }

	        } catch (Exception e) {
	            System.out.println("Error al leer plantas.dat");
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Busca una planta por su código
	     */
	    public static void buscarPlanta(int codigoBuscado) {
	        try (RandomAccessFile raf = new RandomAccessFile("plantas.dat", "r")) {

	            while (raf.getFilePointer() < raf.length()) {
	                int codigo = raf.readInt();
	                float precio = raf.readFloat();
	                int stock = raf.readInt();

	                if (codigo == codigoBuscado) {
	                    System.out.println("Planta encontrada → Código: " + codigo + ", Precio: " + precio + ", Stock: " + stock);
	                    return;
	                }
	            }

	            System.out.println("No se encontró la planta con código " + codigoBuscado);

	        } catch (Exception e) {
	            System.out.println("Error al buscar la planta");
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Modifica el stock de una planta
	     */
	    public static void modificarStock(int codigoBuscado, int nuevoStock) {
	        try (RandomAccessFile raf = new RandomAccessFile("plantas.dat", "rw")) {

	            while (raf.getFilePointer() < raf.length()) {
	                long pos = raf.getFilePointer();

	                int codigo = raf.readInt();
	                float precio = raf.readFloat();
	                int stock = raf.readInt();

	                if (codigo == codigoBuscado) {
	                    raf.seek(pos + 8); // saltar codigo (4) + precio (4)
	                    raf.writeInt(nuevoStock);
	                    System.out.println("Stock actualizado para la planta " + codigo + ": " + nuevoStock);
	                    return;
	                }
	            }

	            System.out.println("No se encontró la planta con código " + codigoBuscado);

	        } catch (Exception e) {
	            System.out.println("Error al modificar el stock");
	            e.printStackTrace();
	        }
	    }

	    // --- MAIN DE PRUEBA ---
	    public static void main(String[] args) {
	        // Crear lista de plantas simulando lectura de XML
	        ArrayList<planta> lista = new ArrayList<>();
	        lista.add(new planta(1, "Captus", "X", "Ninguna"));
	        lista.add(new planta(2, "Geranio", "X", "Ninguna"));
	        lista.add(new planta(3, "Rosa", "X", "Bonita flor"));

	        // Crear el archivo .dat con precios y stock
	        crearArchivo(lista);

	        // Leer el archivo completo
	        leerArchivo();

	        // Buscar por código
	        buscarPlanta(2);

	        // Modificar stock
	        modificarStock(1, 50);

	        // Ver contenido final
	        leerArchivo();
	    
	}

}
