package ejemploXLM;

import java.util.ArrayList;

public class fruta {
    private String nombre;
    private String tipo;
    private String color;
    private String origen;
    private String precio; 
    private String temporada;
    private ArrayList<String> nutrientes; // ← mejor nombre plural

    public fruta(String nombre, String tipo, String color, String origen, String precio, String temporada) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
        this.origen = origen;
        this.precio = precio;
        this.temporada = temporada;
        this.nutrientes = new ArrayList<>(); // ← inicialización obligatoria
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getPrecio() { return precio; }
    public void setPrecio(String precio) { this.precio = precio; }

    public String getTemporada() { return temporada; }
    public void setTemporada(String temporada) { this.temporada = temporada; }

    public ArrayList<String> getNutrientes() { return nutrientes; }

    public void agregarNutriente(String nutriente) {
        this.nutrientes.add(nutriente);
    }

	@Override
	public String toString() {
		return "Fruta: \n Nombre=" + nombre + "\n Tipo=" + tipo + "\n Color=" + color + "\n Origen=" + origen + "\n Precio="
				+ precio + "\n Temporada=" + temporada + "\n Nutrientes=" + nutrientes ;
	}


    
}
