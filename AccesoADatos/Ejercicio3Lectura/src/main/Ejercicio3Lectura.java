package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Ejercicio3Lectura {

	public static void main(String[] args) {
	    // Ruta del archivo CSV
        String fichero = "restaurants.csv";
        // Leer el archivo y mostrar los valores en el formato campo:valor
        try  {
        	BufferedReader br = new BufferedReader(new FileReader(fichero));
            String linea;
            boolean esPrimeraLinea = true; // Para detectar la primera línea de encabezados

            while ((linea = br.readLine()) != null) {
                // Separar la línea por comas
                String[] campos = linea.split(",");

                if (esPrimeraLinea) {
                    // En la primera línea, los valores son los encabezados
                    for (String campo : campos) {
                        System.out.println(campo + ":");
                    }
                    esPrimeraLinea = false; // Cambiar a false después de procesar la primera línea
                } else {
                    // En las líneas siguientes, los valores son los datos
                    int i = 0;
                    for (String campo : campos) {
                        System.out.println("Campo" + (i + 1) + ":" + campo);
	                        i++;
	                    }
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	}

}
