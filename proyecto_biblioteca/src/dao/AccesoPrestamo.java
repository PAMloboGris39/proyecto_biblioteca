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
	 public static void insertarPrestamo() {
	        int codigoLibro = Teclado.leerEntero("Ingrese el código del libro: ");
	        int codigoSocio = Teclado.leerEntero("Ingrese el código del socio: ");
	        String fechaInicio = Teclado.leerCadena("Ingrese la fecha de inicio (YYYY-MM-DD): ");
	        String fechaFin = Teclado.leerCadena("Ingrese la fecha de fin (YYYY-MM-DD): ");

	        String checkLibro = "SELECT fecha_devolucion FROM prestamo WHERE codigo_libro = ? AND fecha_devolucion IS NULL";
	        String checkSocio = "SELECT fecha_devolucion FROM prestamo WHERE codigo_socio = ? AND fecha_devolucion IS NULL";
	        String insertSQL = "INSERT INTO prestamo (codigo_libro, codigo_socio, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";

	        try (Connection conn = ConfigSQLite.abrirConexion(); 
	             PreparedStatement pstmtLibro = conn.prepareStatement(checkLibro);
	             PreparedStatement pstmtSocio = conn.prepareStatement(checkSocio)) {
	            
	            pstmtLibro.setInt(1, codigoLibro);
	            ResultSet rsLibro = pstmtLibro.executeQuery();
	            if (rsLibro.next()) {
	                System.out.println("Se ha prestado ese libro a un socio y éste aún no lo ha devuelto.");
	                return;
	            }
	            
	            pstmtSocio.setInt(1, codigoSocio);
	            ResultSet rsSocio = pstmtSocio.executeQuery();
	            if (rsSocio.next()) {
	                System.out.println("Se ha prestado un libro a ese socio y éste aún no lo ha devuelto.");
	                return;
	            }
	            
	            try (PreparedStatement pstmtInsert = conn.prepareStatement(insertSQL)) {
	                pstmtInsert.setInt(1, codigoLibro);
	                pstmtInsert.setInt(2, codigoSocio);
	                pstmtInsert.setString(3, fechaInicio);
	                pstmtInsert.setString(4, fechaFin);
	                pstmtInsert.executeUpdate();
	                System.out.println("Se ha insertado un préstamo en la base de datos.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Error al insertar el préstamo: " + e.getMessage());
	        }
	    }
	 public static void eliminarPrestamo() {
	        int codigoLibro = Teclado.leerEntero("Ingrese el código del libro: ");
	        int codigoSocio = Teclado.leerEntero("Ingrese el código del socio: ");
	        String fechaInicio = Teclado.leerCadena("Ingrese la fecha de inicio del préstamo: ");
	        
	        String sql = "DELETE FROM prestamo WHERE codigo_libro = ? AND codigo_socio = ? AND fecha_inicio = ?";
	        
	        try (Connection conn = ConfigSQLite.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, codigoLibro);
	            pstmt.setInt(2, codigoSocio);
	            pstmt.setString(3, fechaInicio);
	            
	            int filasAfectadas = pstmt.executeUpdate();
	            if (filasAfectadas > 0) {
	                System.out.println("Se ha eliminado un préstamo de la base de datos.");
	            } else {
	                System.out.println("No existe ningún préstamo con esos datos identificativos en la base de datos.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Error al eliminar el préstamo: " + e.getMessage());
	        }
	    }
	 public static void consultarPrestamos() {
	        String sql = "SELECT * FROM prestamo";
	        
	        try (Connection conn = ConfigSQLite.abrirConexion();
	             PreparedStatement pstmt = conn.prepareStatement(sql);
	             ResultSet rs = pstmt.executeQuery()) {
	            
	            List<prestamo> listaPrestamos = new ArrayList<>();
	            while (rs.next()) {
	                listaPrestamos.add(new prestamo(
	                    rs.getInt("codigo_libro"),
	                    rs.getInt("codigo_socio"),
	                    rs.getString("fecha_inicio"),
	                    rs.getString("fecha_fin"),
	                    rs.getString("fecha_devolucion")
	                ));
	            }
	            
	            if (listaPrestamos.isEmpty()) {
	                System.out.println("No se ha encontrado ningún préstamo en la base de datos.");
	            } else {
	                listaPrestamos.forEach(System.out::println);
	                System.out.println("Se han consultado " + listaPrestamos.size() + " préstamos de la base de datos.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Error al consultar los préstamos: " + e.getMessage());
	        }
	    }
	 public static void consultarPrestamosNoDevueltos() {
		    String sql = "SELECT * FROM prestamo WHERE fecha_devolucion IS NULL";

		    try (Connection conn = ConfigSQLite.abrirConexion();
		         PreparedStatement pstmt = conn.prepareStatement(sql);
		         ResultSet rs = pstmt.executeQuery()) {

		    	List<prestamo> prestamosNoDevueltos = new ArrayList<>();
	            while (rs.next()) {
	                listaPrestamos.add(new prestamo(
	                    rs.getInt("codigo_libro"),
	                    rs.getInt("codigo_socio"),
	                    rs.getString("fecha_inicio"),
	                    rs.getString("fecha_fin"),
	                    rs.getString("fecha_devolucion")
	                ));
	            }

		        if (prestamosNoDevueltos.isEmpty()) {
		            System.out.println("No existe ningún préstamo no devuelto en la base de datos.");
		        } else {
		            prestamosNoDevueltos.forEach(System.out::println);
		            System.out.println("Se han consultado " + prestamosNoDevueltos.size() + " préstamos no devueltos.");
		        }
		    } catch (SQLException e) {
		        System.out.println("Error al consultar los préstamos no devueltos: " + e.getMessage());
		    }
		}

	 public static void consultarPrestamosPorFecha() {
		    String fechaConsulta = Teclado.leerCadena("Ingrese la fecha de los préstamos a consultar (YYYY-MM-DD): ");
		    String sql = """
		        SELECT s.dni, s.nombre, l.isbn, l.titulo, p.fecha_devolucion 
		        FROM prestamo p
		        JOIN socio s ON p.codigo_socio = s.codigo
		        JOIN libro l ON p.codigo_libro = l.codigo
		        WHERE p.fecha_inicio = ?
		    """;

		    try (Connection conn = ConfigSQLite.abrirConexion();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {

		        pstmt.setString(1, fechaConsulta);
		        ResultSet rs = pstmt.executeQuery();

		        List<String> prestamosResumen = new ArrayList<>();
		        while (rs.next()) {
		            String resumen = String.format(
		                "DNI: %s, Nombre: %s, ISBN: %s, Título: %s, Fecha Devolución: %s",
		                rs.getString("dni"),
		                rs.getString("nombre"),
		                rs.getString("isbn"),
		                rs.getString("titulo"),
		                rs.getString("fecha_devolucion")
		            );
		            prestamosResumen.add(resumen);
		        }

		        if (prestamosResumen.isEmpty()) {
		            System.out.println("No existe ningún préstamo realizado en esa fecha en la base de datos.");
		        } else {
		            prestamosResumen.forEach(System.out::println);
		            System.out.println("Se han consultado " + prestamosResumen.size() + " préstamos.");
		        }
		    } catch (SQLException e) {
		        System.out.println("Error al consultar los préstamos por fecha: " + e.getMessage());
		    }
		}
	 
	
	 
	 
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
	
		
}

