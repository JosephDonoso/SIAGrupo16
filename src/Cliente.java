
public class Cliente extends Persona {
	private String direccion;
	private String numero;
	
	public Cliente(String nombre, int rut, String direccion, String numero) {
		super(nombre, rut);
		this.direccion = direccion;
		this.numero = numero;
	}
	
	public String getDireccion() {
		return direccion;
	}
	
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public void setNumero(int numero) {
		this.numero = String.valueOf(numero);
	}
}
