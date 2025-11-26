package jugueteria;
import java.util.Date;

public class Empleado {
	     int idEmpleado;
	     String nombre;
	     enum cargos{
	    	 Empleado,
	    	 Vendedor
	     }
	     cargos cargo;
	     Date fechaIngreso;
	    	   	    
	    public Empleado(int idEmpleado, String nombre, cargos cargo, Date fechaIngreso) {
			super();
			this.idEmpleado = idEmpleado;
			this.nombre = nombre;
			this.cargo = cargo;
			this.fechaIngreso = fechaIngreso;
		}

		public int getIdEmpleado() {
			return idEmpleado;
		}

		public void setIdEmpleado(int idEmpleado) {
			this.idEmpleado = idEmpleado;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public cargos getCargo() {
			return cargo;
		}

		public void setCargo(cargos cargo) {
			this.cargo = cargo;
		}

		public Date getFechaIngreso() {
			return fechaIngreso;
		}

		public void setFechaIngreso(Date fechaIngreso) {
			this.fechaIngreso = fechaIngreso;
		}
	    
		
	}
