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
	private int codigoProducto;
    private int unidades;
    private float precioUnitario;
	
    public Ticket(int codigoProducto, int unidades, float precioUnitario) {
		super();
		this.codigoProducto = codigoProducto;
		this.unidades = unidades;
		this.precioUnitario = precioUnitario;
	}

	public int getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(int codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public int getUnidades() {
		return unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}

	public float getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(float precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
    
    public float getSubtotal() {
        return unidades * precioUnitario;
    }
 
}