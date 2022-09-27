
public class Empleado extends Persona{
	private String contraseña;
	
	public Empleado(String nombre, int rut, String contraseña) {
		super(nombre, rut );
		this.contraseña = contraseña;
	}
	
	public boolean validarContraseña(String contraseña) {
		return contraseña.equals(this.contraseña);
	}
	
	public void setContraseña(String contraseñaAnterior, String nuevaContraseña ) {
		if(contraseñaAnterior.equals(contraseña)) {
			contraseña = nuevaContraseña;
		}
	}
	
	public String getContraseña() {
		return contraseña;
	}
	
}
