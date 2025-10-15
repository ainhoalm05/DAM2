package ficherosBinarios;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ficherosAleatorios {

	public static void main(String[] args) {
		int numeroLista;
		String nombre = "Pedro";
		double nota;
		
		
		try {
			
			//Creamos el fichero binario
			File fichero = new File("datos.dat");
			fichero.createNewFile();
			
			RandomAccessFile raf = new RandomAccessFile(fichero,"rw");//El segundo parametro indicamos los permisos de lectura r, escritura w o ambos wr
		
			raf.writeInt(1);
			raf.writeChars(nombre);
			raf.writeDouble(6.5);
			
			//Imprimimos la direccion del puntero del archivo
			System.out.println(raf.getFilePointer());
			
			//raf.seek(4); // nos posicionamos en la posicion 4 de memoria del archivo binario
			
			//Lee los primeros bytes de memoria del archivo binario
			//System.out.println(raf.readInt());
			
			String cadena = "";
			raf.seek(0);
			
			while(raf.getFilePointer()<raf.length()) {
				System.out.println(raf.readInt());
				for(int i=0;i<nombre.length();i++) {	
					//System.out.print(raf.readChar());
					cadena+=raf.readChar();//Esto es igual a lo anterior pero imprimiendo fuera del bucle
				}
				
				System.out.println(cadena);
								
				System.out.println(raf.readDouble());
			}
			
			raf.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}


	}

}
