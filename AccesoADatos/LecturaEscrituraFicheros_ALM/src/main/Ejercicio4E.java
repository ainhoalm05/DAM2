package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Ejercicio4E {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        // Pedimos al usuario los nombres de los dos ficheros a unir
        System.out.println("Introduce el nombre del fichero 1:");
        String fichero1 = sc.nextLine();

        System.out.println("Introduce el nombre del fichero 2:");
        String fichero2 = sc.nextLine();

        // Pedimos al usuario la ruta del directorio donde se guardará el nuevo fichero
        System.out.println("Introduce la ruta de destino (carpeta):");
        String ruta = sc.nextLine();

        File directorio = new File(ruta);

        // Comprobamos si la ruta introducida corresponde a un directorio existente
        if (directorio.isDirectory() && directorio.exists()) {
            // Creamos objetos File para los dos ficheros de entrada
            File fichero1prog = new File(fichero1);
            File fichero2prog = new File(fichero2);

            // Creamos el nombre del nuevo fichero uniendo los nombres originales
            String nombreNuevo = fichero1prog.getName() + "_" + fichero2prog.getName();
            File ficheroNuevo = new File(directorio, nombreNuevo);

            // Comprobamos que ambos ficheros existen y son archivos normales
            if (fichero1prog.exists() && fichero1prog.isFile() && 
                fichero2prog.exists() && fichero2prog.isFile()) {
                try {
                    // Si el fichero de destino no existe, lo creamos
                    if (!ficheroNuevo.exists()) {
                        ficheroNuevo.createNewFile();
                    }

                    // Creamos un BufferedWriter para escribir en el nuevo fichero
                    BufferedWriter escritor = new BufferedWriter(new FileWriter(ficheroNuevo));

                    // Copiamos el contenido del primer fichero
                    copiarContenido(fichero1prog, escritor);

                    // Copiamos el contenido del segundo fichero
                    copiarContenido(fichero2prog, escritor);

                    // Cerramos el escritor (muy importante para guardar los cambios)
                    escritor.close();

                    System.out.println("Ficheros combinados en: " + ficheroNuevo.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Los ficheros no existen o no son archivos válidos.");
            }

        } else {
            System.out.println("El directorio no existe.");
        }

        sc.close();
    }

    /**
     * Método auxiliar para copiar el contenido de un fichero en un BufferedWriter
     */
    private static void copiarContenido(File fichero, BufferedWriter escritor) {
        
		try {
			FileReader fr = new FileReader(fichero);
			BufferedReader br = new BufferedReader(fr);

			String linea;
			
			// Leemos línea a línea y las escribimos en el nuevo fichero
			while ((linea = br.readLine()) != null) {
			    escritor.write(linea);
			    escritor.newLine(); // Añadimos un salto de línea después de cada línea
			}
			fr.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

        

        
    }
}
