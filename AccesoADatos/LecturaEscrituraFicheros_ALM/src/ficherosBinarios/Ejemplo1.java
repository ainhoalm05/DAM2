package ficherosBinarios;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Ejemplo1 {

	public static void LecturaDatos() {
		try {
			DataInputStream dis = new DataInputStream(new FileInputStream("datos.bin"));
			int entero = dis.readInt();
			double numero = dis.readDouble();
			boolean booleano = dis.readBoolean();
			String frase = dis.readUTF();
			
			System.out.println("Entero: "+entero+"Double: "+numero+"Booleano: "+ booleano+"Frase: "+frase);
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void EscrituraPersonas (ArrayList <Ejemplo1Persona> personas) {
		String fichero = "personas.dat";
		File ficheroEscritura=new File(fichero);
		
		if(!ficheroEscritura.exists()) {
			try {
				ficheroEscritura.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}else {
			if(ficheroEscritura.isFile()) {
				try {
				//Lo abro para escritura y escribo un flujo de datos
				FileOutputStream fos =new FileOutputStream(ficheroEscritura);
				//El tipo de dato al que lo tiene que transformar
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				
				oos.writeObject(personas);
				oos.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("Fichero es directorio y no se puede escribir");
			}
		}
	}
	
	public static void EscrituraDatos() {
		File ficheroDatos = new File("datos.bin");
		
		try {
			if(!ficheroDatos.exists()) {
				ficheroDatos.createNewFile();
			}
			
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(ficheroDatos));
			dos.writeInt(2);
			dos.writeDouble(1.2);
			dos.writeBoolean(false);
			dos.writeUTF("Hola como vas?");
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void LecturaPersonas() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("personas.dat"));
			ArrayList <Ejemplo1Persona> personas = (ArrayList <Ejemplo1Persona>) ois.readObject();
			for(Ejemplo1Persona p:personas) {
				System.out.println(p);
			}
		}catch(IOException|ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ArrayList <Ejemplo1Persona> personas= new ArrayList<>();
		personas.add(new Ejemplo1Persona("Ana",23));
		personas.add(new Ejemplo1Persona("Luis",28));
		personas.add(new Ejemplo1Persona("Maria",35));

		EscrituraPersonas (personas);
		LecturaPersonas();
		EscrituraDatos();
		LecturaDatos();
	}

}
