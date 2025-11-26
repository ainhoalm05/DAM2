package jugueteria;

import java.sql.Date;

public class Venta {
	int idVenta, idJuguete, idEmpleado, idStand, idZona;
	Date Fecha;
	float precioTotal;
	enum tipoPagos{
		Efectivo,
		Tarjeta
	}
	tipoPagos tipoPagp;
	public Venta(int idVenta, int idJuguete, int idEmpleado, int idStand, int idZona, Date fecha, float precioTotal,
			tipoPagos tipoPagp) {
		super();
		this.idVenta = idVenta;
		this.idJuguete = idJuguete;
		this.idEmpleado = idEmpleado;
		this.idStand = idStand;
		this.idZona = idZona;
		Fecha = fecha;
		this.precioTotal = precioTotal;
		this.tipoPagp = tipoPagp;
	}
	
	public int getIdVenta() {
		return idVenta;
	}
	public void setIdVenta(int idVenta) {
		this.idVenta = idVenta;
	}
	public int getIdJuguete() {
		return idJuguete;
	}
	public void setIdJuguete(int idJuguete) {
		this.idJuguete = idJuguete;
	}
	public int getIdEmpleado() {
		return idEmpleado;
	}
	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	public int getIdStand() {
		return idStand;
	}
	public void setIdStand(int idStand) {
		this.idStand = idStand;
	}
	public int getIdZona() {
		return idZona;
	}
	public void setIdZona(int idZona) {
		this.idZona = idZona;
	}
	public Date getFecha() {
		return Fecha;
	}
	public void setFecha(Date fecha) {
		Fecha = fecha;
	}
	public float getPrecioTotal() {
		return precioTotal;
	}
	public void setPrecioTotal(float precioTotal) {
		this.precioTotal = precioTotal;
	}
	public tipoPagos getTipoPagp() {
		return tipoPagp;
	}
	public void setTipoPagp(tipoPagos tipoPagp) {
		this.tipoPagp = tipoPagp;
	}
	
	
}
