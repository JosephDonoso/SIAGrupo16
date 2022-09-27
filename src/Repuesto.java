
public class Repuesto {
	private String nombre;
	private int precio;
	private int cantidad;
	
	public Repuesto(String nombre, int precio, int cantidad){
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantidad;
	}
	
	public Repuesto(String nombre, int precio){
		this.nombre = nombre;
		this.precio = precio;
		cantidad = 1;
	}
	
	public void reservarRepuesto(int cantidad) {
		this.cantidad -= cantidad;
	}
	
	public void reservarRepuesto() {
		cantidad -= 1;
	}
	
	public void añadirRepuesto(int cantidad) {
		this.cantidad += cantidad;
	}
	
	public void añadirRepuesto() {
		cantidad += 1;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public int getPrecio() {
		return precio;
	}
	
	public void setPrecio(int precio) {
		this.precio = precio;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}
