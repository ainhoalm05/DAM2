package ejerciciosAccesoDirecto;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Ejercicio1 {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        try {
            System.out.print("¿Cuántos números de la serie de Fibonacci quieres mostrar?: ");
            int n = entrada.nextInt();

            // Escribir los números de Fibonacci en un fichero binario
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("fibonachi.dat"))) {
                int a = 0;
                int b = 1;

                System.out.print("Serie de Fibonacci: ");
                for (int i = 0; i < n; i++) {
                    System.out.print(a + " ");
                    dos.writeInt(a); // cada número ocupa 4 bytes
                    int siguiente = a + b;
                    a = b;
                    b = siguiente;
                }
                System.out.println();
            }

            // Consultar una posición
            System.out.print("¿Qué posición deseas consultar?: ");
            int posicion = entrada.nextInt();

            // Leer el número en esa posición
            try (RandomAccessFile raf = new RandomAccessFile("fibonachi.dat", "r")) {
                long posicionByte = posicion * 4L; // cada int ocupa 4 bytes
                if (posicionByte < raf.length()) {
                    raf.seek(posicionByte);
                    int numero = raf.readInt();
                    System.out.println("Número en la posición " + posicion + " = " + (numero-1));
                } else {
                    System.out.println("Esa posición no existe en el fichero.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
