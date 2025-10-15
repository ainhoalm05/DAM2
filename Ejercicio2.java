package ejerciciosAccesoDirecto;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Ejercicio2 {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        String fichero = "productos.dat";

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            raf.setLength(0); // limpiar el archivo

            System.out.print("¿Cuántos productos quieres introducir? ");
            int n = entrada.nextInt();

            for (int i = 1; i <= n; i++) {
                System.out.println("Producto " + i + ":");
                System.out.print("Cantidad: ");
                int cantidad = entrada.nextInt();
                System.out.print("Precio: ");
                double precio = entrada.nextDouble();

                raf.writeInt(i);         // ID
                raf.writeInt(cantidad);  // Cantidad
                raf.writeDouble(precio); // Precio
            }
            System.out.println("\nProductos guardados correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                int id = raf.readInt();
                int cantidad = raf.readInt();
                double precio = raf.readDouble();
                System.out.printf("ID: %d  Cantidad: %d  Precio: %.2f%n", id, cantidad, precio);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        System.out.print("\nIntroduce el ID del producto que quieres ver: ");
        int idBuscar = entrada.nextInt();

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
            long posicion = (idBuscar - 1) * 16L; // 16 bytes por registro
            if (posicion >= raf.length()) {
                System.out.println("No existe ese producto.");
            } else {
                raf.seek(posicion);
                int id = raf.readInt();
                int cantidad = raf.readInt();
                double precio = raf.readDouble();
                System.out.printf("Producto encontrado -> ID: %d  Cantidad: %d  Precio: %.2f%n", id, cantidad, precio);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

       
        System.out.print("\nIntroduce el ID del producto que quiere modificar: ");
        int idMod = entrada.nextInt();

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            long posicion = (idMod - 1) * 16L; // 16 bytes por producto
            if (posicion >= raf.length()) {
                System.out.println("No existe ese producto.");
            } else {
                raf.seek(posicion + 4); // saltar el ID (4 bytes)
                System.out.print("Nueva cantidad: ");
                int nuevaCantidad = entrada.nextInt();
                System.out.print("Nuevo precio: ");
                double nuevoPrecio = entrada.nextDouble();

                raf.writeInt(nuevaCantidad);
                raf.writeDouble(nuevoPrecio);
                System.out.println("Producto modificado correctamente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        System.out.print("\nIntroduce el ID del producto que quiere borrar: ");
        int idBorrar = entrada.nextInt();

        ArrayList<Integer> cantidades = new ArrayList<>();
        ArrayList<Double> precios = new ArrayList<>();

        // Leer todos los productos y guardar los que no se borran
        try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                int id = raf.readInt();
                int cantidad = raf.readInt();
                double precio = raf.readDouble();
                if (id != idBorrar) {
                    cantidades.add(cantidad);
                    precios.add(precio);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Reescribir el archivo con IDs contiguos
        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            raf.setLength(0);
            for (int i = 0; i < cantidades.size(); i++) {
                raf.writeInt(i + 1);             // nuevo ID contiguo
                raf.writeInt(cantidades.get(i)); // cantidad
                raf.writeDouble(precios.get(i)); // precio
            }
            System.out.println("Producto borrado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mostrar lista final
        try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                int id = raf.readInt();
                int cantidad = raf.readInt();
                double precio = raf.readDouble();
                System.out.printf("ID: %d  Cantidad: %d  Precio: %.2f%n", id, cantidad, precio);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        entrada.close();
    }
}



