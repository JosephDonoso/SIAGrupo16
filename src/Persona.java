import java.util.ArrayList;

public class Persona {
	private String nombre;
	private int rut;
	protected ArrayList< OrdenDeTrabajo > ordenesDeTrabajo;
	
	public Persona() {
	}
	
	public Persona(String nombre, int rut ) {
		this.nombre = nombre;
		this.rut = rut;
		ordenesDeTrabajo = new ArrayList< OrdenDeTrabajo >();
	}
	
	public OrdenDeTrabajo buscarOrden(int idOrden){
		
		OrdenDeTrabajo ordenAuxiliar = new OrdenDeTrabajo();
		for(int i = 0; i < ordenesDeTrabajo.size(); i++) {
			ordenAuxiliar = ordenesDeTrabajo.get(i);
			if(ordenAuxiliar.getId() == idOrden){
				return ordenAuxiliar;
			}
			
		}
		return null;
	}
	
	/*public void agregarNuevaOrden(OrdenDeTrabajo nuevaOrden, Empleado empleado) {
		ordenesDeTrabajo.add(nuevaOrden);
		empleado.ordenesDeTrabajo.add(nuevaOrden);
		
	}*/
	
	public void agregarNuevaOrden(OrdenDeTrabajo nuevaOrden) {
		ordenesDeTrabajo.add(nuevaOrden);
	}
	
	/*public void agregarNuevaOrden(String nombreProducto, String diagnostico, Cliente cliente, Empleado empleado, int total, int id) {
		OrdenDeTrabajo nuevaOrden = new OrdenDeTrabajo(nombreProducto, diagnostico, cliente, empleado, total, id);
		ordenesDeTrabajo.add(nuevaOrden);
		if(this == cliente) {
			empleado.ordenesDeTrabajo.add(nuevaOrden);
		}
		else {
			cliente.ordenesDeTrabajo.add(nuevaOrden);
		}
		
	}*/
	
	/*public void agregarNuevaOrden(String nombreProducto, String diagnostico, Empleado empleado, int total, int id) {
		OrdenDeTrabajo nuevaOrden = new OrdenDeTrabajo(nombreProducto, diagnostico, (Cliente)this, empleado, total, id);
		ordenesDeTrabajo.add(nuevaOrden);
		empleado.ordenesDeTrabajo.add(nuevaOrden);
		
	}*/
	
	public void removerOrden(int i){
		ordenesDeTrabajo.remove(i);
	}
	
	public OrdenDeTrabajo getOrden(int i) {
		return ordenesDeTrabajo.get(i);
	}
	
	public int getCantOrdenes() {
		return ordenesDeTrabajo.size();
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public int getRut() {
		return rut;
	}
	
	public void setRut(int rut) {
		this.rut = rut;
	}
	
	public void setRut(String rut) {
		this.rut = Integer.parseInt(rut);
	}
	
}
