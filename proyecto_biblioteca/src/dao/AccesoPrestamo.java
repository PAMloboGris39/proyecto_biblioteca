package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Config.ConfigSQLite;
import entrada.Teclado;
import modelo.prestamo;
import modelo.probar;

public class AccesoPrestamo {
	 
		
	
	 
	 /* MAIN PRUEBAS: 
	 public static void actualizarPrestamo() {
	        int codigoLibro = Teclado.leerEntero("Ingrese el código del libro: ");
	        int codigoSocio = Teclado.leerEntero("Ingrese el código del socio: ");
	        String fechaInicio = Teclado.leerCadena("Ingrese la fecha de inicio del préstamo: ");
	        String nuevaFechaDevolucion = Teclado.leerCadena("Ingrese la nueva fecha de devolución (YYYY-MM-DD): ");
	        
	        String sql = "UPDATE prestamo SET fecha_devolucion = ? WHERE codigo_libro = ? AND codigo_socio = ? AND fecha_inicio = ?";
	        
	        try (Connection conn = ConfigSQLite.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, nuevaFechaDevolucion);
	            pstmt.setInt(2, codigoLibro);
	            pstmt.setInt(3, codigoSocio);
	            pstmt.setString(4, fechaInicio);
	            
	            int filasAfectadas = pstmt.executeUpdate();
	            if (filasAfectadas > 0) {
	                System.out.println("Se ha actualizado un préstamo de la base de datos.");
	            } else {
	                System.out.println("No existe ningún préstamo con esos datos identificativos en la base de datos.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Error al actualizar el préstamo: " + e.getMessage());
	        }
	    }
	 public static void main(String[] args) {
	        int opcion;
	        do {
	            System.out.println("\n=== GESTIÓN DE PRÉSTAMOS ===");
	            System.out.println("1. Insertar préstamo");
	            System.out.println("2. Eliminar préstamo");
	            System.out.println("3. Consultar todos los préstamos");
	            System.out.println("4. Consultar préstamos no devueltos");
	            System.out.println("5. Consultar préstamos por fecha");
	            System.out.println("6. Actualizar fecha de devolución");
	            System.out.println("0. Salir");

	            opcion = Teclado.leerEntero("Seleccione una opción: ");

	            switch (opcion) {
	                case 1:
	                    AccesoPrestamo.insertarPrestamo();
	                    break;
	                case 2:
	                    AccesoPrestamo.eliminarPrestamo();
	                    break;
	                case 3:
	                    AccesoPrestamo.consultarPrestamos();
	                    break;
	                case 4:
	                    AccesoPrestamo.consultarPrestamosNoDevueltos();
	                    break;
	                case 5:
	                    AccesoPrestamo.consultarPrestamosPorFecha();
	                    break;
	                case 6:
	                    AccesoPrestamo.actualizarPrestamo();
	                    break;
	                case 0:
	                    System.out.println("Saliendo del programa...");
	                    break;
	                default:
	                    System.out.println("Opción no válida. Intente nuevamente.");
	            }

	        } while (opcion != 0);
	    }
	    */
	
		
}

