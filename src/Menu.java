import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Menu{
	//Colección usada para almacenar los clientes en el sistema
	private HashMap< Integer, Cliente> mapaClientes;
	
	//Colección usada para exportar los clientes existentes a un archivo csv
	private ArrayList< Integer > rutsClientes;
	
	//Colección usada para almacenar los empleados en el sistema
	private HashMap< Integer, Empleado> mapaEmpleados;
	
	//Colección usada para exportar los empleados o mostrarlos por pantalla
	private ArrayList< Integer > rutsEmpleados;
	
	//Colección usada para importar los repuestos a cada ordene de trabajo por su n° identificador
	private HashMap < Integer, OrdenDeTrabajo> ordenesActivas;
	
	//Colección usada para exportar todas las ordenes de trabajo activas a un archivo csv
	private ArrayList< Integer > idOrdenesActivas;
	
	//Colección usada para almacenar una lista con todos los repuestos que puedan existir, *NO la cantidad de estos*
	private ArrayList< String > repuestosTotales;
	
	//Colección usada para almacenar la cantidad de repuestos por su nombre
	private HashMap< String, Repuesto> repuestosDisponibles;
	
	//Variables auxiliares para asignar al empleado y cliente de una misma orden y utilizarlos en distintos menúes
	private Empleado empleadoActual;
	private Cliente clienteActual;
	
	//Variable única y autoincrementable para identificar una orden de trabajo
	private int idOrdenes; 
	
	public Menu( ) {
		//Se inicializa cada colección
		mapaClientes = new HashMap< Integer, Cliente>();
		rutsClientes = new ArrayList< Integer >();
		mapaEmpleados = new HashMap< Integer, Empleado>();
		rutsEmpleados = new ArrayList< Integer >();
		ordenesActivas = new HashMap< Integer, OrdenDeTrabajo>();
		idOrdenesActivas = new ArrayList< Integer >();
		repuestosTotales = new ArrayList< String >();
		repuestosDisponibles = new HashMap< String, Repuesto>();
		
		//El n° identificador de órdenes comenzará en 0 y aumentará cuando
		//se importe una nueva orden o cuando un cliente agregue una orden
		idOrdenes = 0;
	}
	
	/*Función que importa todos los datos del sistema desde varios archivos csv
	 * Se importan: Clientes; Empleados; Ordenes de trabajo; Repuestos del almacen; 
	 * Repuestos utilizados por cada orden; Repuestos faltantes por cada orden*/
	public void importarData() throws IOException {
		//Se abre cada flujo de lectura para los distintos archivos
		BufferedReader csvClientes = new BufferedReader(new FileReader("ArchivosCSV\\Clientes.csv"));
		BufferedReader csvEmpleados = new BufferedReader(new FileReader("ArchivosCSV\\Empleados.csv"));
		BufferedReader csvRepuestos = new BufferedReader(new FileReader("ArchivosCSV\\AlmacenDeRepuestos.csv"));
		BufferedReader csvOrdenes = new BufferedReader(new FileReader("ArchivosCSV\\OrdenesDeTrabajo.csv"));
		BufferedReader csvRepFaltantes = new BufferedReader(new FileReader("ArchivosCSV\\RepuestosFaltantes.csv"));
		BufferedReader csvRepUtilizados = new BufferedReader(new FileReader("ArchivosCSV\\RepuestosUtilizados.csv"));
		//Se declaran variables auxiliares que se utilizarán
		OrdenDeTrabajo ordenActual;
		Repuesto repuestoActual;
		String lineaTexto;
		String[] valoresCsv;
		
		//Se importan los clientes desde un csv
		//Nombre,Rut,Dirección,Número
		lineaTexto = csvClientes.readLine(); //HEADER del archivo
		while((lineaTexto = csvClientes.readLine()) != null) {
			valoresCsv = lineaTexto.split(",");
			clienteActual = new Cliente(valoresCsv[0], Integer.parseInt(valoresCsv[1]), valoresCsv[2], valoresCsv[3]);
			mapaClientes.put(clienteActual.getRut(), clienteActual);
			rutsClientes.add(clienteActual.getRut());
		}
		csvClientes.close();
		
		//Se importan los empleados desde un csv
		//Nombre,Rut,Contraseña
		lineaTexto = csvEmpleados.readLine(); //HEADER del archivo
		while((lineaTexto = csvEmpleados.readLine()) != null) {
			valoresCsv = lineaTexto.split(",");
			empleadoActual = new Empleado(valoresCsv[0], Integer.parseInt(valoresCsv[1]), valoresCsv[2]);
			mapaEmpleados.put(empleadoActual.getRut(), empleadoActual);
			rutsEmpleados.add(empleadoActual.getRut());
		}
		csvEmpleados.close();
		
		//Se importan los repuestos del almacen desde un csv
		//Nombre,Precio,Cantidad
		lineaTexto = csvRepuestos.readLine(); //HEADER del archivo
		while((lineaTexto = csvRepuestos.readLine()) != null) {
			valoresCsv = lineaTexto.split(",");
			repuestoActual = new Repuesto(valoresCsv[0], Integer.parseInt(valoresCsv[1]), Integer.parseInt(valoresCsv[2]));
			repuestosDisponibles.put(repuestoActual.getNombre(), repuestoActual);
			repuestosTotales.add(repuestoActual.getNombre());
		}
		csvRepuestos.close();
		
		//Se importan las órdenes desde un csv
		//Nombre,Descripción,Diagnóstico,RutCliente,RutEmpleado,Total,Id
		lineaTexto = csvOrdenes.readLine(); //HEADER del archivo
		while((lineaTexto = csvOrdenes.readLine()) != null) {
			valoresCsv = lineaTexto.split(",");
			clienteActual = mapaClientes.get(Integer.parseInt(valoresCsv[3]));
			empleadoActual = mapaEmpleados.get(Integer.parseInt(valoresCsv[4]));
			ordenActual = new OrdenDeTrabajo(valoresCsv[0], valoresCsv[1], valoresCsv[2], clienteActual, empleadoActual, Integer.parseInt(valoresCsv[5]), Integer.parseInt(valoresCsv[6]));
			ordenesActivas.put(ordenActual.getId(), ordenActual);
			idOrdenesActivas.add(ordenActual.getId());
			clienteActual.agregarNuevaOrden(ordenActual);
			empleadoActual.agregarNuevaOrden(ordenActual);
			//if (idOrdenes < Integer.parseInt(valoresCsv[6]))
			idOrdenes = Integer.parseInt(valoresCsv[6]);
		}
		csvOrdenes.close();
		
		//Se importan los repuestos faltantes de cada orden desde un csv
		//Nombre,Precio,Cantidad,IdOrden
		lineaTexto = csvRepFaltantes.readLine(); //HEADER del archivo
		while((lineaTexto = csvRepFaltantes.readLine()) != null) {
			valoresCsv = lineaTexto.split(",");
			repuestoActual = new Repuesto(valoresCsv[0], Integer.parseInt(valoresCsv[1]), Integer.parseInt(valoresCsv[2]));
			ordenActual = ordenesActivas.get(Integer.parseInt(valoresCsv[3]));
			ordenActual.agregarRepuestoFaltante(repuestoActual);
		}
		csvRepFaltantes.close();
		
		//Se importan los repuestos utilizados de cada orden desde un csv
		//Nombre,Precio,Cantidad,IdOrden
		lineaTexto = csvRepUtilizados.readLine(); //HEADER del archivo
		while((lineaTexto = csvRepUtilizados.readLine()) != null) {
			valoresCsv = lineaTexto.split(",");
			repuestoActual = new Repuesto(valoresCsv[0], Integer.parseInt(valoresCsv[1]), Integer.parseInt(valoresCsv[2]));
			ordenActual = ordenesActivas.get(Integer.parseInt(valoresCsv[3]));
			ordenActual.agregarRepuestoUtilizado(repuestoActual);
		}
		csvRepUtilizados.close();	
	}
	
	public void hardcodearDatos() {
		Empleado empleado = new Empleado( "Jaimito", 21324095, "123");
		mapaEmpleados.put(21324095, empleado);
		rutsEmpleados.add(21324095);
		
		empleado = new Empleado( "Manuel", 20111222, "123");
		mapaEmpleados.put(20111222, empleado);
		rutsEmpleados.add(20111222);
		
		empleado = new Empleado( "Rodrigo", 21111222, "123");
		mapaEmpleados.put(21111222, empleado);
		rutsEmpleados.add(21111222);
		
		empleado = new Empleado( "Martín", 22111222, "123");
		mapaEmpleados.put(22111222, empleado);
		rutsEmpleados.add(22111222);
		
		empleado = new Empleado( "Carlos", 23111222, "123");
		mapaEmpleados.put(23111222, empleado);
		rutsEmpleados.add(23111222);
		
		Cliente cliente = new Cliente( "Joaquín", 21000111, "Valparaíso", "987654321" );
		mapaClientes.put(21000111, cliente);
		rutsClientes.add(21000111);
		
		cliente = new Cliente( "Francisco", 22000111, "Viña del Mar", "987654321");
		mapaClientes.put(22000111, cliente);
		rutsClientes.add(22000111);
		
		cliente = new Cliente( "Enrique", 23000111, "Santiago", "987654321");
		mapaClientes.put(23000111, cliente);
		rutsClientes.add(23000111);

		cliente = new Cliente( "Valentina", 24000111, "San Antonio", "987654321");
		mapaClientes.put(24000111, cliente);
		rutsClientes.add(24000111);
		
		cliente = new Cliente( "María", 25000111, "Algarrobo", "987654321");
		mapaClientes.put(25000111, cliente);
		rutsClientes.add(25000111);
		
		Repuesto repuesto = new Repuesto("Monitor",7500,1);
		repuestosTotales.add(repuesto.getNombre());
		repuestosDisponibles.put(repuesto.getNombre(), repuesto);
		
		repuesto = new Repuesto("Placa principal",20000,1);
		repuestosTotales.add(repuesto.getNombre());
		repuestosDisponibles.put(repuesto.getNombre(), repuesto);
		
		repuesto = new Repuesto("Microprocesador (CPU)",39000,32);
		repuestosTotales.add(repuesto.getNombre());
		repuestosDisponibles.put(repuesto.getNombre(), repuesto);
		
		repuesto = new Repuesto("Módulo de RAM 8gb",16000,50);
		repuestosTotales.add(repuesto.getNombre());
		repuestosDisponibles.put(repuesto.getNombre(), repuesto);
		
		repuesto = new Repuesto("Batería",12500,23);
		repuestosTotales.add(repuesto.getNombre());
		repuestosDisponibles.put(repuesto.getNombre(), repuesto);
		
		repuesto = new Repuesto("Unidad de disco óptico (CD; DVD; BD)",8000,36);
		repuestosTotales.add(repuesto.getNombre());
		repuestosDisponibles.put(repuesto.getNombre(), repuesto);
		
		repuesto = new Repuesto("Disco duro portátil",15000,42);
		repuestosTotales.add(repuesto.getNombre());
		repuestosDisponibles.put(repuesto.getNombre(), repuesto);
	}
	
	/* Primer menú del sistema, es llamado desde el archivo principal del programa
	 * En él los usuarios deciden si entrarán en un perfil de cliente o empleado */
	public void menuPrincipal()throws IOException{
		BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
		int opcion;
		
		//hardcodearDatos(); //Función solo para avance, no para entrega final
		importarData(); //Función que importa los datos del sistema que anteriormente fueron exportados
		
		while(true) {
			System.out.println("Menu Principal");
			System.out.println("1.- Perfil Empleado");
			System.out.println("2.- Perfil Cliente");
			System.out.println("3.- Guardar y salir");
			opcion = Integer.parseInt(leer.readLine());
		
			switch(opcion) {
			case 1: 
				menuEmpleado();
				break;
			
			case 2:
				menuCliente();
				break;
				
			case 3:
				exportarData();
				System.out.println("Saliendo del programa.");
				return;
				
			default:
				System.out.println("Ingrese una opción válida.\n");
				break;
			}
		}
		
	}
	
// ---------------------------------------------------------------EMPLEADO-----------------------------------------------------------------------------
	/* Función para validar el ingreso del empleado.
	 * Recibe un rut y valida si existe tal empleado y si la contraseña ingresada es la correcta
	 * NO será posible registrarse como nuevo empleado en el sistema (por seguridad del programa)
	 * Retorna True si fue posible ingresar y False si es lo contrario */
	public boolean ingresarCuentaEmpleado()throws IOException{
		BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
		int rutEmpleado;
		
		System.out.println("\nIngrese su rut sin dígito verificador: ");
		rutEmpleado = Integer.parseInt(leer.readLine()); //Recibo el rut por el usuario
		
		empleadoActual = mapaEmpleados.get( rutEmpleado ); //Obtengo al empleado del mapa
		if( empleadoActual != null ){ //Si existe se valida la contraseña y se ingresa al menú
			System.out.println("Ingrese su contraseña: ");
			if(!empleadoActual.validarContraseña(leer.readLine())) {
				System.out.println("Contraseña incorrecta");
				System.out.println("Volviendo al menú principal.");
				return false;
			}
			System.out.println("\nIngresó correctamente");
			System.out.println("Nombre empleado: " + empleadoActual.getNombre() );
			return true;
		}
		else {//Si no existe se regresa al menú anterior
			System.out.println("No se ha encontrado el usuario.");
			System.out.println("Volviendo al menú principal.");
			return false;
		}
		
	}
	
	public void menuEmpleado()throws IOException{
		BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
		Repuesto repuestoAux;
		OrdenDeTrabajo ordenAux;
		String nombreRepuestoAux;
		int precioRepuesto;
		int cantRepuestos;
		String respuesta;
		int idOrdenActual;
		int opcion;
		
		if( !ingresarCuentaEmpleado() ) {
			return;
		}
		
		while( true ) {
			
			System.out.println("Menu Empleado");
			System.out.println("1.- Listar órdenes de trabajo.");
			System.out.println("2.- Modificar una orden.");
			System.out.println("3.- Cancelar una orden.");
			System.out.println("4.- Completar una orden.");
			System.out.println("5.- Actualizar órdenes.");
			System.out.println("6.- Agregar repuestos al almacén.");
			System.out.println("7.- Salir.");
			opcion = Integer.parseInt(leer.readLine());
			
			switch(opcion) {
			case 1: 
				if(empleadoActual.getCantOrdenes() == 0) {
					System.out.println("No se registra ninguna orden en nuestra base de datos.");
					System.out.println("Volviendo al menú\n");
					break;
				}
				listarOrdenes(empleadoActual);
				break;
			
			case 2:
				if(empleadoActual.getCantOrdenes() == 0) {
					System.out.println("No se registra ninguna orden en nuestra base de datos.");
					System.out.println("Volviendo al menú\n");
					break;
				}
				System.out.println("\nIngrese el id de la orden que desea modificar: ");
				idOrdenActual = Integer.parseInt(leer.readLine());
				ordenAux = empleadoActual.buscarOrden(idOrdenActual);
				if(ordenAux == null) {
					System.out.println("No se ha encontrado ninguna orden con ese id\n");
					break;
				}
				mostrarOrdenEmpleado(ordenAux);
				menuModificarOrden(ordenAux);
				break;
			
			case 3:
				if(empleadoActual.getCantOrdenes() == 0) {
					System.out.println("No se registra ninguna orden en nuestra base de datos.");
					System.out.println("Volviendo al menú\n");
					break;
				}
				System.out.println("\nIngrese el id de la orden que desea cancelar: ");
				idOrdenActual = Integer.parseInt(leer.readLine());
				ordenAux = empleadoActual.buscarOrden(idOrdenActual);
				if(ordenAux == null) {
					System.out.println("No se ha encontrado ninguna orden con ese id\n");
					break;
				}
				System.out.println("Cancelar una orden es irreversible.");
				System.out.println("¿Desea continuar de igual forma? (Ingrese Si o No).");
				respuesta = leer.readLine();
				if(respuesta.equals("Si")) {
					cancelarOrden(ordenAux);
					regresarRepuestos(ordenAux);
				}
				break;
			
			case 4:
				if(empleadoActual.getCantOrdenes() == 0) {
					System.out.println("No se registra ninguna orden en nuestra base de datos.");
					System.out.println("Volviendo al menú\n");
					break;
				}
				System.out.println("\nIngrese el id de la orden que desea completar: ");
				idOrdenActual = Integer.parseInt(leer.readLine());
				ordenAux = empleadoActual.buscarOrden(idOrdenActual);
				if(ordenAux == null) {
					System.out.println("No se ha encontrado ninguna orden con ese id\n");
					break;
				}
				completarOrden(ordenAux);
				break;
			
			case 5:
				if(empleadoActual.getCantOrdenes() == 0) {
					System.out.println("No se registra ninguna orden en nuestra base de datos.");
					System.out.println("Volviendo al menú\n");
					break;
				}
				System.out.println("\nActualizar órdenes eliminará, de la lista de ordenes, a aquellas que estén canceladas o realizadas.");
				actualizarOrdenes(empleadoActual);
				break;
				
			case 6:
				System.out.println("\nIngrese el nombre del repuesto: ");
				nombreRepuestoAux = leer.readLine();
				repuestoAux = repuestosDisponibles.get(nombreRepuestoAux);
				if(repuestoAux != null) {
					System.out.println("Ingrese la cantidad que agregará: ");
					cantRepuestos = Integer.parseInt(leer.readLine());
					repuestoAux.añadirRepuesto(cantRepuestos);
				}
				else {
					System.out.println("Ingrese el precio del repuesto: ");
					precioRepuesto = Integer.parseInt(leer.readLine());
					System.out.println("Ingrese la cantidad que agregará: ");
					cantRepuestos = Integer.parseInt(leer.readLine());
					repuestoAux = new Repuesto(nombreRepuestoAux, precioRepuesto, cantRepuestos);
					repuestosTotales.add(nombreRepuestoAux);
					repuestosDisponibles.put(nombreRepuestoAux, repuestoAux);
				}
				break;
				
			case 7:
				return;
				
			default:
				System.out.println("Ingrese una opción válida.\n");
				break;
			}
		}
	}
	
	public void completarOrden(OrdenDeTrabajo ordenAux) {
		if(ordenAux.getDiagnostico().equals("EN ESPERA DE UN DIGNÓSTICO TÉCNICO")) {
			System.out.println("No se podrá completar la orden porque sigue en espera de un diagnóstico técnico.");
			return;
		}
		if(!ordenAux.getCancelada() && !ordenAux.getCompletada()) {
			if(ordenAux.getCantRepuestosFaltantes() != 0) {
				System.out.println("No se podrá completar la orden hasta que se consigan los repuestos faltantes.");
			}
			else {
				ordenAux.setCompletada(true);
			}
		}
		mostrarOrdenEmpleado(ordenAux);
	}
	
	public void mostrarOrdenEmpleado(OrdenDeTrabajo ordenActual){
		clienteActual = ordenActual.getCliente();
		System.out.println("\nORDEN DE TRABAJO N°: " + ordenActual.getId());
		System.out.println("	NOMBRE DEL PRODUCTO A REPARAR: " + ordenActual.getNombreProducto());
		System.out.println("	DESCRIPCIÓN DEL PROBLEMA: " + ordenActual.getDescripcion());
		System.out.println("	DIAGNÓSTICO TÉCNICO: " + ordenActual.getDiagnostico());
		System.out.println("\nEMPLEADO: " + empleadoActual.getNombre());
		System.out.println("	RUT N°: " + empleadoActual.getRut());
		mostrarRepuestosUtilizados(ordenActual);
		mostrarRepuestosFaltantes(ordenActual);
		System.out.println("\nCOSTOS POR REPUESTOS: $" + (ordenActual.getTotal()-15000));
		//System.out.println("	APLAZAMIENTO DE FECHA POR REPUESTOS: ");
		//System.out.println("	FECHA DE ENTREGA: ");
		System.out.println("\nCOSTO BASE: $" + 15000);
		System.out.println("\nTOTAL: $" + ordenActual.getTotal());
		if(ordenActual.getCancelada()) {
			System.out.println("----ORDEN CANCELADA----");
		}
		if(ordenActual.getCompletada()) {
			System.out.println("----ORDEN COMPLETADA----");
		}
		System.out.println("--------------------------------------------------------");
	}
	
	public void mostrarRepuestosUtilizados(OrdenDeTrabajo ordenActual) {
		if (ordenActual.getCantRepuestosUtilizados() == 0)
			return;
		Repuesto repuestoActual;
		System.out.println("\nREPUESTOS DISPONIBLES: ");
		for(int i = 0; i < ordenActual.getCantRepuestosUtilizados(); i++) {
			repuestoActual = ordenActual.getRepuestoUtilizado(i);
			System.out.println("	NOMBRE DEL REPUESTO: " + repuestoActual.getNombre());
			System.out.println("	PRECIO DEL REPUESTO: " + repuestoActual.getPrecio());
			System.out.println("	CANTIDAD: " + repuestoActual.getCantidad());
		}
	}
	
	public void mostrarRepuestosFaltantes(OrdenDeTrabajo ordenActual) {
		if (ordenActual.getCantRepuestosFaltantes() == 0)
			return;
		Repuesto repuestoActual;
		System.out.println("\nREPUESTOS FALTANTES: ");
		for(int i = 0; i < ordenActual.getCantRepuestosFaltantes(); i++) {
			repuestoActual = ordenActual.getRepuestoFaltante(i);
			System.out.println("	NOMBRE DEL REPUESTO: " + repuestoActual.getNombre());
			System.out.println("	PRECIO DEL REPUESTO: " + repuestoActual.getPrecio());
			System.out.println("	CANTIDAD: " + repuestoActual.getCantidad());
		}
	}
	
	public void menuModificarOrden(OrdenDeTrabajo ordenAuxiliar) throws IOException {
		BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
		Repuesto repuestoAux, repuestoFaltante;
		String nombreRepuestoAux;
		int cantRepuestoAux = 0, cantRepuestoDisponible = 0, cantRepuestoFaltante = 0;
		int opcion;
		
		while( true ) {
			
			System.out.println("Menu para modificar una orden");
			System.out.println("1.- Cambiar diagnóstico técnico.");
			System.out.println("2.- Agregar repuesto.");
			System.out.println("3.- Eliminar repuesto.");
			System.out.println("4.- Actualizar repuestos faltantes.");
			System.out.println("5.- Salir.");
			opcion = Integer.parseInt(leer.readLine());
			
			switch(opcion) {
			case 1: 
				System.out.println("Ingrese un nuevo diagnóstico para el equipo.");
				ordenAuxiliar.setDiagnostico(leer.readLine());
				break;
			
			case 2:
				listarRepuestos();
				do {
					System.out.println("Ingrese el nombre del repuesto que utilizará.");
					nombreRepuestoAux = leer.readLine();
					repuestoAux = repuestosDisponibles.get(nombreRepuestoAux);
					if(repuestoAux == null) {
						System.out.println("Debe ingresar un nombre de repuesto válido");
					}
					else 
						break;
				}while(true);
				System.out.println("Ingrese la cantidad.");
				cantRepuestoAux = Integer.parseInt(leer.readLine());
				
				ordenAuxiliar.sumarTotalRepuestos(repuestoAux.getPrecio() * cantRepuestoAux);
				
				if(cantRepuestoAux > repuestoAux.getCantidad()) {
					cantRepuestoFaltante = cantRepuestoAux - repuestoAux.getCantidad();
					cantRepuestoDisponible = repuestoAux.getCantidad();
					repuestoAux.reservarRepuesto(cantRepuestoDisponible);
				}
				else {
					cantRepuestoFaltante = 0;
					repuestoAux.reservarRepuesto(cantRepuestoAux);
					cantRepuestoDisponible = cantRepuestoAux;
				}
				if(cantRepuestoFaltante != 0) {
					ordenAuxiliar.agregarRepuestoFaltante(repuestoAux, cantRepuestoFaltante);
				}
				if(cantRepuestoDisponible != 0) {
					ordenAuxiliar.agregarRepuestoUtilizado(repuestoAux, cantRepuestoDisponible);
				}
				
				break;
			
			case 3:
				if(ordenAuxiliar.getCantRepuestos() != 0) {
					mostrarRepuestosUtilizados(ordenAuxiliar);
					mostrarRepuestosFaltantes(ordenAuxiliar);
					System.out.println("Ingrese el nombre del repuesto que eliminará.");
					nombreRepuestoAux = leer.readLine();
					repuestoAux = ordenAuxiliar.buscarRepuestoFaltante(nombreRepuestoAux);
					if(repuestoAux != null) {
						repuestoAux.reservarRepuesto();
						ordenAuxiliar.restarTotalRepuestos(repuestoAux.getPrecio());
						if(repuestoAux.getCantidad() == 0) {
							ordenAuxiliar.eliminarRepuestoFaltante(nombreRepuestoAux);
							break;
						}
					}
					else {
						repuestoAux = ordenAuxiliar.buscarRepuestoUtilizado(nombreRepuestoAux);
						if(repuestoAux != null) {
							repuestoAux.reservarRepuesto();
							ordenAuxiliar.restarTotalRepuestos(repuestoAux.getPrecio());
							if(repuestoAux.getCantidad() == 0) {
								ordenAuxiliar.eliminarRepuestoUtilizado(nombreRepuestoAux);
							}
							repuestoAux = repuestosDisponibles.get(nombreRepuestoAux);
							repuestoAux.añadirRepuesto();
							break;
						}
						System.out.println("No se encontró tal repuesto.");
					}
					
				}
				else {
					System.out.println("No hay repuestos asignados a tal orden.");
				}
				break;
				
			case 4:
				for(int i = 0; i < ordenAuxiliar.getCantRepuestosFaltantes(); i++) {
					repuestoFaltante = ordenAuxiliar.getRepuestoFaltante(i);
					repuestoAux = repuestosDisponibles.get(repuestoFaltante.getNombre());
					if(repuestoAux.getCantidad() > 0) {
						if(repuestoFaltante.getCantidad() >= repuestoAux.getCantidad()) {
							repuestoAux.reservarRepuesto(repuestoAux.getCantidad());
							repuestoFaltante.reservarRepuesto(repuestoAux.getCantidad());
							ordenAuxiliar.agregarRepuestoUtilizado(repuestoAux, repuestoAux.getCantidad());
						}
						else {
							repuestoAux.reservarRepuesto(repuestoFaltante.getCantidad());
							repuestoFaltante.reservarRepuesto(repuestoFaltante.getCantidad());
							ordenAuxiliar.agregarRepuestoUtilizado(repuestoAux, repuestoFaltante.getCantidad());
						}
						
						if(repuestoFaltante.getCantidad() == 0) {
							ordenAuxiliar.eliminarRepuestoFaltante(repuestoFaltante.getNombre());
						}
					}
				}
				break;
				
			case 5:
				return;
				
			default:
				System.out.println("Ingrese una opción válida\n");
				break;
			}
		}
	}
	
	public void listarRepuestos(){
		Repuesto repuestoAux;
		String nombreRepuestoAux;
		System.out.println("\nListado de todos los repuestos existentes:");
		for(int i = 0; i < repuestosTotales.size(); i++) {
			nombreRepuestoAux = repuestosTotales.get(i);
			repuestoAux = repuestosDisponibles.get(nombreRepuestoAux);
			System.out.println("	NOMBRE DEL REPUESTO: " + repuestoAux.getNombre());
			System.out.println("	PRECIO DEL REPUESTO: " + repuestoAux.getPrecio());
			System.out.println("	CANTIDAD: " + repuestoAux.getCantidad() + "\n");
		}
	}
	
// -----------------------------------------------------------------CLIENTE-----------------------------------------------------------------------------
	public boolean ingresarCuentaCliente()throws IOException{
		BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
		int rutCliente;
		String respuesta, nombreCliente, direccionCliente, numeroCliente;
		
		System.out.println("\nIngrese su rut sin dígito verificador: ");
		rutCliente = Integer.parseInt(leer.readLine());
		
		clienteActual = mapaClientes.get( rutCliente );
		if( clienteActual != null ){
			System.out.println("\nIngreso correctamente");
			System.out.println("Nombre cliente: " + clienteActual.getNombre() );
			return true;
		}
		else {
			System.out.println("No se ha encontrado el usuario.");
			System.out.println("Desea registrarse (Ingrese Si o No)");
			respuesta = leer.readLine();
			if(respuesta.equals("Si")) {
				System.out.println("Ingrese su nombre");
				nombreCliente = leer.readLine();
				System.out.println("Ingrese su dirección");
				direccionCliente = leer.readLine();
				System.out.println("Ingrese su número");
				numeroCliente = leer.readLine();
				clienteActual = new Cliente( nombreCliente, rutCliente, direccionCliente, numeroCliente);
				mapaClientes.put(rutCliente, clienteActual);
				rutsClientes.add(rutCliente);
				System.out.println("\nIngreso correctamente");
				System.out.println("Nombre cliente: " + clienteActual.getNombre() );
				return true;
			}
			else {
				System.out.println("Volviendo al menú principal.");
				return false;
			}
		}
	}
	
	
	public void menuCliente()throws IOException{
		BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
		OrdenDeTrabajo ordenAux;
		String respuesta;
		int idOrdenActual;
		int opcion;
		
		if( !ingresarCuentaCliente() ) {
			return;
		}
		
		while( true ) {
			
			System.out.println("Menu Cliente");
			System.out.println("1.- Agregar nueva orden.");
			System.out.println("2.- Listar mis órdenes.");
			System.out.println("3.- Buscar una orden.");
			System.out.println("4.- Cancelar una orden.");
			System.out.println("5.- Actualizar órdenes.");
			System.out.println("6.- Salir.");
			opcion = Integer.parseInt(leer.readLine());
			
			switch(opcion) {
			case 1: 
				ingresarUnaOrden(); //Mensaje de orden ingresada con número verificador: 1
				break;
				
			case 2: 
				if(clienteActual.getCantOrdenes() == 0) {
					System.out.println("No se registra ninguna orden en nuestra base de datos.");
					System.out.println("Volviendo al menú\n");
					break;
				}
				listarOrdenes(clienteActual);
				break;
			
			case 3:
				if(clienteActual.getCantOrdenes() == 0) {
					System.out.println("No se registra ninguna orden en nuestra base de datos.");
					System.out.println("Volviendo al menú\n");
					break;
				}
				System.out.println("\nIngrese el id de la orden que busca: ");
				idOrdenActual = Integer.parseInt(leer.readLine());
				ordenAux = clienteActual.buscarOrden(idOrdenActual);
				if(ordenAux == null) {
					System.out.println("No se ha encontrado ninguna orden con ese id\n");
					break;
				}
				mostrarOrden(ordenAux);
				break;
			
			case 4:
				if(clienteActual.getCantOrdenes() == 0) {
					System.out.println("No se registra ninguna orden en nuestra base de datos.");
					System.out.println("Volviendo al menú\n");
					break;
				}
				System.out.println("\nIngrese el id de la orden que desea cancelar: ");
				idOrdenActual = Integer.parseInt(leer.readLine());
				ordenAux = clienteActual.buscarOrden(idOrdenActual);
				if(ordenAux == null) {
					System.out.println("No se ha encontrado ninguna orden con ese id\n");
					break;
				}
				System.out.println("Cancelar una orden es irreversible");
				System.out.println("¿Desea continuar de igual forma? (Ingrese Si o No)");
				respuesta = leer.readLine();
				if(respuesta.equals("Si")) {
					cancelarOrden(ordenAux);
					regresarRepuestos(ordenAux);
				}
				break;
				
			case 5:
				if(clienteActual.getCantOrdenes() == 0) {
					System.out.println("No se registra ninguna orden en nuestra base de datos.");
					System.out.println("Volviendo al menú\n");
					break;
				}
				System.out.println("\nActualizar órdenes eliminará, de su lista de ordenes, a aquellas que estén canceladas o realizadas.");
				actualizarOrdenes(clienteActual);
				break;
			
			case 6:
				return;
			
			default:
				System.out.println("Ingrese una opción válida\n");
				break;
				
			}
		}
	}
	
	public void mostrarOrden(OrdenDeTrabajo ordenActual) {
		empleadoActual = ordenActual.getEmpleado();
		clienteActual = ordenActual.getCliente();
		System.out.println("\nORDEN DE TRABAJO N°: " + ordenActual.getId());
		System.out.println("	DATOS DEL EQUIPO: " + ordenActual.getNombreProducto());
		System.out.println("	DESCRIPCIÓN DEL PROBLEMA: " + ordenActual.getDescripcion());
		System.out.println("	DIAGNÓSTICO TÉCNICO: " + ordenActual.getDiagnostico());
		System.out.println("\nEMPLEADO: " + empleadoActual.getNombre());
		System.out.println("	RUT N°: " + empleadoActual.getRut());
		System.out.println("\nCLIENTE: " + clienteActual.getNombre());
		System.out.println("	RUT N°: " + clienteActual.getRut());
		System.out.println("	DIRECCIÓN: " + clienteActual.getDireccion());
		System.out.println("	FONO: " + clienteActual.getNumero());
		//System.out.println("	FECHA DE ENTREGA: ");
		System.out.println("\nCOSTO BASE: $" + 15000);
		if(ordenActual.getDiagnostico().equals("EN ESPERA DE UN DIAGNÓSTICO TÉCNICO")) {
			System.out.println("\nTOTAL EN ESPERA DE UN DIAGNÓSTICO TÉCNICO");
		}
		else {
			System.out.println("\nTOTAL: $" + ordenActual.getTotal());
		}
		if(ordenActual.getCancelada()) {
			System.out.println("----ORDEN CANCELADA----");
		}
		if(ordenActual.getCompletada()) {
			System.out.println("----ORDEN COMPLETADA----");
		}
		System.out.println("--------------------------------------------------------");
	}
	
	public void listarOrdenes(Persona personaActual){
		OrdenDeTrabajo ordenActual;
		for(int i = 0; i < personaActual.getCantOrdenes(); i++) {
			ordenActual = personaActual.getOrden(i);
			mostrarOrden(ordenActual);
		}
	}
	
	public void ingresarUnaOrden()throws IOException {
		BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
		String nombreProducto;
		String descripcion;
		int total = 15000; //Precio base por realizar una orden
		int opcion;
		
		System.out.println("\nIngrese el modelo del computador a reparar:");
		nombreProducto = leer.readLine();
		
		System.out.println("Ingrese la descripción del problema: ");
		descripcion = leer.readLine();
		
		System.out.println("");
		
		System.out.println("Si desea escoger algún empleado para asignarle esta orden, ingrese 1.");
		System.out.println("Si ingresa cualquier otro número, el sistema escogerá automáticamente a un empleado para asignar esta orden");
		opcion = Integer.parseInt(leer.readLine());
		switch (opcion){
		case 1:
			escogerEmpleado(); //El usuario escoge un empleado 
			break;
		default:
			obtenerEmpleado(); //El sistema escoge al empleado con menos ordenes
			break;
		}
		
		idOrdenes += 1;
		OrdenDeTrabajo nuevaOrden = new OrdenDeTrabajo(nombreProducto, descripcion, clienteActual, empleadoActual, total, idOrdenes);
		clienteActual.agregarNuevaOrden(nuevaOrden);
		empleadoActual.agregarNuevaOrden(nuevaOrden);
		ordenesActivas.put(idOrdenes, nuevaOrden);
		idOrdenesActivas.add(idOrdenes);
		System.out.println("Orden agregada exitosamente");
	}
	
	public void escogerEmpleado() throws IOException {
		BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
		Persona empleadoAuxiliar;
		int rutActual;
		
		for(int i = 0; i < rutsEmpleados.size(); i++) {
			rutActual = rutsEmpleados.get(i);
			empleadoAuxiliar = mapaEmpleados.get( rutActual );
			System.out.println("Nombre empleado " + (i+1) + " : " + empleadoAuxiliar.getNombre());
			System.out.println("Rut empleado: " + empleadoAuxiliar.getRut());
			System.out.println("Cantidad de órdenes vigentes: " + empleadoAuxiliar.getCantOrdenes());
			System.out.println();
		}
		
		do {
			System.out.println("Ingrese el rut de un empleado de la lista: ");
			rutActual = Integer.parseInt(leer.readLine());
			empleadoActual = mapaEmpleados.get( rutActual );
			if(empleadoActual != null) {
				break;
			}
			System.out.println("Debe ser un rut válido");
		} while(true);
	}
	
	public void obtenerEmpleado(){
		
		int rutActual = rutsEmpleados.get(0);
		Empleado empleadoAuxiliar = mapaEmpleados.get( rutActual );
		int menorCantidadOrdenes = empleadoAuxiliar.getCantOrdenes();
		empleadoActual = empleadoAuxiliar;
		
		for(int i = 0; i < rutsEmpleados.size(); i++) {
			rutActual = rutsEmpleados.get(i);
			empleadoAuxiliar = mapaEmpleados.get( rutActual );
			if(empleadoAuxiliar.getCantOrdenes() < menorCantidadOrdenes) {
				menorCantidadOrdenes = empleadoAuxiliar.getCantOrdenes();
				empleadoActual = empleadoAuxiliar;
			}
		}
		
	}
	
	public void actualizarOrdenes(Persona personaActual) {
		OrdenDeTrabajo ordenAux;
		for(int i = 0; i < personaActual.getCantOrdenes(); i++) {
			ordenAux = personaActual.getOrden(i);
			
			if(ordenAux.getCancelada() || ordenAux.getCompletada()) {
				mostrarOrden(ordenAux);
				personaActual.removerOrden(i);
			}
		}
	}
	
	public void cancelarOrden(OrdenDeTrabajo ordenAux) {
		if(!ordenAux.getCancelada() && !ordenAux.getCompletada()) {
			ordenAux.setCancelada(true);
		}
		mostrarOrden(ordenAux);
	}
	
	public void regresarRepuestos(OrdenDeTrabajo ordenAux) {
		Repuesto repuestoUtilizado, repuestoAux;
		for(int i = 0; i < ordenAux.getCantRepuestosUtilizados(); i++) {
			repuestoUtilizado = ordenAux.getRepuestoUtilizado(i);
			repuestoAux = repuestosDisponibles.get(repuestoUtilizado.getNombre());
			repuestoAux.añadirRepuesto(repuestoUtilizado.getCantidad());
		}
	}
	
	public void exportarData() throws IOException {
		String sep = ",";
		String lF = "\n";
		FileWriter csvClientes = new FileWriter("ArchivosCSV\\Clientes.csv");
		FileWriter csvEmpleados = new FileWriter("ArchivosCSV\\Empleados.csv");
		FileWriter csvRepuestos = new FileWriter("ArchivosCSV\\AlmacenDeRepuestos.csv");
		FileWriter csvOrdenes = new FileWriter("ArchivosCSV\\OrdenesDeTrabajo.csv");
		FileWriter csvRepFaltantes = new FileWriter("ArchivosCSV\\RepuestosFaltantes.csv");
		FileWriter csvRepUtilizados = new FileWriter("ArchivosCSV\\RepuestosUtilizados.csv");
		
		csvClientes.append("Nombre,Rut,Dirección,Número\n");
		for(int i = 0; i < rutsClientes.size(); i++) {
			clienteActual = mapaClientes.get(rutsClientes.get(i));
			csvClientes.append(clienteActual.getNombre());
			csvClientes.append(sep);
			csvClientes.append(String.valueOf(clienteActual.getRut()));
			csvClientes.append(sep);
			csvClientes.append(clienteActual.getDireccion());
			csvClientes.append(sep);
			csvClientes.append(clienteActual.getNumero());
			csvClientes.append(lF);
		}
		csvClientes.close();
		
		csvEmpleados.append("Nombre,Rut,Contraseña\n");
		for(int i = 0; i < rutsEmpleados.size(); i++) {
			empleadoActual = mapaEmpleados.get(rutsEmpleados.get(i));
			csvEmpleados.append(empleadoActual.getNombre());
			csvEmpleados.append(sep);
			csvEmpleados.append(String.valueOf(empleadoActual.getRut()));
			csvEmpleados.append(sep);
			csvEmpleados.append(empleadoActual.getContraseña());
			csvEmpleados.append(lF);
		}
		csvEmpleados.close();
		
		Repuesto repuestoActual;
		csvRepuestos.append("Nombre,Precio,Cantidad\n");
		for(int i = 0; i < repuestosTotales.size(); i++) {
			repuestoActual = repuestosDisponibles.get(repuestosTotales.get(i));
			csvRepuestos.append(repuestoActual.getNombre());
			csvRepuestos.append(sep);
			csvRepuestos.append(String.valueOf(repuestoActual.getPrecio()));
			csvRepuestos.append(sep);
			csvRepuestos.append(String.valueOf(repuestoActual.getCantidad()));
			csvRepuestos.append(lF);
		}
		csvRepuestos.close();
		
		csvOrdenes.append("Nombre,Descripción,Diagnóstico,RutCliente,RutEmpleado,Total,Id\n");
		csvRepFaltantes.append("Nombre,Precio,Cantidad,IdOrden\n");
		csvRepUtilizados.append("Nombre,Precio,Cantidad,IdOrden\n");
		OrdenDeTrabajo ordenActual;
		for(int i = 0; i < idOrdenesActivas.size(); i++) {
			ordenActual = ordenesActivas.get(idOrdenesActivas.get(i));
			if(!ordenActual.getCancelada() && !ordenActual.getCompletada()) {
				empleadoActual = ordenActual.getEmpleado();
				clienteActual = ordenActual.getCliente();
				csvOrdenes.append(ordenActual.getNombreProducto());
				csvOrdenes.append(sep);
				csvOrdenes.append(ordenActual.getDescripcion());
				csvOrdenes.append(sep);
				csvOrdenes.append(ordenActual.getDiagnostico());
				csvOrdenes.append(sep);
				csvOrdenes.append(String.valueOf(clienteActual.getRut()));
				csvOrdenes.append(sep);
				csvOrdenes.append(String.valueOf(empleadoActual.getRut()));
				csvOrdenes.append(sep);
				csvOrdenes.append(String.valueOf(ordenActual.getTotal()));
				csvOrdenes.append(sep);
				csvOrdenes.append(String.valueOf(ordenActual.getId()));
				csvOrdenes.append(lF);
				
				for(int j = 0; j < ordenActual.getCantRepuestosFaltantes(); j++) {
					repuestoActual = ordenActual.getRepuestoFaltante(j);
					csvRepFaltantes.append(repuestoActual.getNombre());
					csvRepFaltantes.append(sep);
					csvRepFaltantes.append(String.valueOf(repuestoActual.getPrecio()));
					csvRepFaltantes.append(sep);
					csvRepFaltantes.append(String.valueOf(repuestoActual.getCantidad()));
					csvRepFaltantes.append(sep);
					csvRepFaltantes.append(String.valueOf(ordenActual.getId()));
					csvRepFaltantes.append(lF);
				}
				
				for(int j = 0; j < ordenActual.getCantRepuestosUtilizados(); j++) {
					repuestoActual = ordenActual.getRepuestoUtilizado(j);
					csvRepUtilizados.append(repuestoActual.getNombre());
					csvRepUtilizados.append(sep);
					csvRepUtilizados.append(String.valueOf(repuestoActual.getPrecio()));
					csvRepUtilizados.append(sep);
					csvRepUtilizados.append(String.valueOf(repuestoActual.getCantidad()));
					csvRepUtilizados.append(sep);
					csvRepUtilizados.append(String.valueOf(ordenActual.getId()));
					csvRepUtilizados.append(lF);
				}
				
			}
			
		}
		csvOrdenes.close();
		csvRepFaltantes.close();
		csvRepUtilizados.close();
		System.out.println("\nSe ha exportado exitosamente.");
		
	}
}