package PracticaFinalT1_ALM;

import java.io.File;
import java.io.RandomAccessFile;

public class Plantas {
	int codigo;
	String nombre;
	String foto;
	String descripcion;
	float precio;
	int stock;
	
	public Plantas(int codigo, String nombre, String foto, String descripcion) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.foto = foto;
		this.descripcion = descripcion;
	}
	@Override
	public String toString() {
		return "--------------------------------------------------------------\n"
				+ "\tCódigo planta " + codigo + "\n\tNombre: " + nombre + "\n\tFoto: " + foto + "\n\tDescripcion: " + descripcion + "\n\tPrecio: " + precio + "\n\tStock: " + stock + "\n"
				+ "--------------------------------------------------------------\n";
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public float getPrecio(int codigo, File fichero) {
	    try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
	        raf.seek((codigo - 1) * 12 + 4);
	        precio = raf.readFloat();
	    } catch (Exception e) {
	        System.out.println("Se ha producido un error al leer el precio.");
	    }
	    return precio;
	}

	public void setPrecio(int codigo, File fichero, float precionuevo) {
	    try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
	        raf.seek((codigo - 1) * 12 + 4);
	        raf.writeFloat(precionuevo);
	        this.precio = precionuevo;
	    } catch (Exception e) {
	        System.out.println("Se ha producido un error al guardar el precio.");
	    }
	}

	public int getStock(int codigo, File fichero) {
	    try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
	        raf.seek((codigo - 1) * 12 + 8);
	        stock = raf.readInt();
	    } catch (Exception e) {
	        System.out.println("Se ha producido un error al leer el stock.");
	    }
	    return stock;
	}

	public void setStock(int codigo, File fichero, int stocknuevo) {
	    try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
	        raf.seek((codigo - 1) * 12 + 8);
	        raf.writeInt(stocknuevo);
	        this.stock = stocknuevo;
	    } catch (Exception e) {
	        System.out.println("Se ha producido un error al guardar el stock.");
	    }
	}
}