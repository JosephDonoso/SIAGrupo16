import java.util.ArrayList;

public class OrdenDeTrabajo {
	private String nombreProducto;
	private String descripcion;
	private String diagnostico;
	private Cliente cliente;
	private Empleado empleado;
	private int total;
	private int id;
	private boolean ordenCancelada = false;
	private boolean ordenCompletada = false; 
	//private String fechaEntrega; DD/MM/AAAA
	//private diasAplazamiento;
	private ArrayList< Repuesto > repuestosUtilizados = new ArrayList< Repuesto >();
	private ArrayList< Repuesto > repuestosFaltantes = new ArrayList< Repuesto >();
	
	public OrdenDeTrabajo(){
		
	}
	
	public OrdenDeTrabajo(String nombreProducto, String descripcion, Cliente cliente, Empleado empleado, int total, int id){
		this.nombreProducto = nombreProducto;
		this.descripcion = descripcion;
		diagnostico = "EN ESPERA DE UN DIAGNÓSTICO TÉCNICO";
		this.cliente = cliente;
		this.empleado = empleado;
		this.total = total;
		this.id = id;
	}
	
	public OrdenDeTrabajo(String nombreProducto, String descripcion, String diagnostico, Cliente cliente, Empleado empleado, int total, int id){
		this.nombreProducto = nombreProducto;
		this.descripcion = descripcion;
		this.diagnostico = diagnostico;
		this.cliente = cliente;
		this.empleado = empleado;
		this.total = total;
		this.id = id;
	}
	
	public void setListasDeRepuestos(ArrayList< Repuesto > repuestosUtilizados, ArrayList< Repuesto > repuestosFaltantes) {
		this.repuestosUtilizados = repuestosUtilizados;
		this.repuestosFaltantes = repuestosFaltantes;
	}
	
	
	public void sumarTotalRepuestos(int totalRepuestos) {
		total += totalRepuestos;
	}
	
	public void restarTotalRepuestos(int totalRepuestos) {
		total -= totalRepuestos;
	}
	
	public void agregarRepuestoFaltante(Repuesto nuevoRepuesto, int cantidad) {
		Repuesto repuestoAux;
		for(int i = 0; i < repuestosFaltantes.size(); i++) {
			repuestoAux = repuestosFaltantes.get(i);
			if( nuevoRepuesto.getNombre().equals(repuestoAux.getNombre()) ) {
				repuestoAux.añadirRepuesto(cantidad);
				return;
			}
		}
		repuestoAux = new Repuesto(nuevoRepuesto.getNombre(), nuevoRepuesto.getPrecio(), cantidad);
		repuestosFaltantes.add(repuestoAux);
	}
	
	public void agregarRepuestoFaltante(Repuesto nuevoRepuesto) {
		repuestosFaltantes.add(nuevoRepuesto);
	}
	
	public void agregarRepuestoUtilizado(Repuesto nuevoRepuesto, int cantidad) {
		Repuesto repuestoAux;
		for(int i = 0; i < repuestosUtilizados.size(); i++) {
			repuestoAux = repuestosUtilizados.get(i);
			if( nuevoRepuesto.getNombre().equals(repuestoAux.getNombre()) ) {
				repuestoAux.añadirRepuesto(cantidad);
				return;
			}
		}
		repuestoAux = new Repuesto(nuevoRepuesto.getNombre(), nuevoRepuesto.getPrecio(), cantidad);
		repuestosUtilizados.add(repuestoAux);
	}
	
	public void agregarRepuestoUtilizado(Repuesto nuevoRepuesto) {
		repuestosUtilizados.add(nuevoRepuesto);
	}
	
	public Repuesto buscarRepuestoFaltante(String nombreRepuesto) {
		Repuesto repuestoAux;
		for(int i = 0; i < repuestosFaltantes.size(); i++) {
			repuestoAux = repuestosFaltantes.get(i);
			if(nombreRepuesto.equals(repuestoAux.getNombre())) {
				return repuestoAux;
			}
		}
		return null;
	}
	
	public Repuesto buscarRepuestoUtilizado(String nombreRepuesto) {
		Repuesto repuestoAux;
		for(int i = 0; i < repuestosUtilizados.size(); i++) {
			repuestoAux = repuestosUtilizados.get(i);
			if(nombreRepuesto.equals(repuestoAux.getNombre())) {
				return repuestoAux;
			}
		}
		return null;
	}
	
	public void eliminarRepuestoFaltante(String nombreRepuesto) {
		Repuesto repuestoAux;
		for(int i = 0; i < repuestosFaltantes.size(); i++) {
			repuestoAux = repuestosFaltantes.get(i);
			if(nombreRepuesto.equals(repuestoAux.getNombre())) {
				repuestosFaltantes.remove(i);
				return;
			}
		}
	}
	
	public void eliminarRepuestoUtilizado(String nombreRepuesto) {
		Repuesto repuestoAux;
		for(int i = 0; i < repuestosUtilizados.size(); i++) {
			repuestoAux = repuestosUtilizados.get(i);
			if(nombreRepuesto.equals(repuestoAux.getNombre())) {
				repuestosUtilizados.remove(i);
				return;
			}
		}
	}
	
	public String getNombreProducto() {
		return nombreProducto;
	}
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}
	public String getDiagnostico() {
		return diagnostico;
	}
	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Empleado getEmpleado() {
		return empleado;
	}
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public void setTotal(long total) {
		this.total = (int)total;
	}
	public void setTotal(double total) {
		this.total = (int)total;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public void setId(long id) {
		this.id = (int) id;
	}
	
	public void setId(double id) {
		this.id = (int) id;
	}
	
	public boolean getCancelada() {
		return ordenCancelada;
	}
	
	public void setCancelada(boolean ordenCancelada) {
		this.ordenCancelada = ordenCancelada;
	}
	
	public boolean getCompletada() {
		return ordenCompletada;
	}
	
	public void setCompletada(boolean ordenCompletada) {
		this.ordenCompletada = ordenCompletada;
	}
	
	public Repuesto getRepuestoFaltante(int indice) {
		return repuestosFaltantes.get(indice);
	}
	
	public Repuesto getRepuestoUtilizado(int indice) {
		return repuestosUtilizados.get(indice);
	}
	
	public int getCantRepuestosFaltantes() {
		return repuestosFaltantes.size();
	}
	
	public int getCantRepuestosUtilizados() {
		return repuestosUtilizados.size();
	}
	
	public int getCantRepuestos() {
		return repuestosUtilizados.size() + repuestosFaltantes.size();
	}
}
