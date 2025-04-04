package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Config.ConfigSQLite;
import entrada.Teclado;

public class probatina {
	public static void insertarSocio() {
        String dni = Teclado.leerCadena("Ingrese el DNI: ");
        String nombre = Teclado.leerCadena("Ingrese el nombre: ");
        String domicilio = Teclado.leerCadena("Ingrese el domicilio: ");
        String telefono = Teclado.leerCadena("Ingrese el teléfono: ");
        String correo = Teclado.leerCadena("Ingrese el correo: ");

        String sql = "INSERT INTO socio (dni, nombre, domicilio, telefono, correo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConfigSQLite.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dni);
            pstmt.setString(2, nombre);
            pstmt.setString(3, domicilio);
            pstmt.setString(4, telefono);
            pstmt.setString(5, correo);
            pstmt.executeUpdate();
            System.out.println("Se ha insertado un socio en la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al insertar el socio: " + e.getMessage());
        }
    }
	public static void eliminarSocio() {
        int codigo = Teclado.leerEntero("Ingrese el código del socio a eliminar: ");
        
        String consultaSocio = "SELECT codigo FROM socio WHERE codigo = ?";
        String eliminarSocio = "DELETE FROM socio WHERE codigo = ?";
        
        try (Connection conn = ConfigSQLite.abrirConexion(); 
             PreparedStatement pstmtConsulta = conn.prepareStatement(consultaSocio)) {
            
            pstmtConsulta.setInt(1, codigo);
            try (ResultSet rs = pstmtConsulta.executeQuery()) {
                if (rs.next()) {
                    // Socio encontrado, proceder a eliminar
                    try (PreparedStatement pstmtEliminar = conn.prepareStatement(eliminarSocio)) {
                        pstmtEliminar.setInt(1, codigo);
                        int filasAfectadas = pstmtEliminar.executeUpdate();
                        if (filasAfectadas > 0) {
                            System.out.println("Se ha eliminado un socio de la base de datos.");
                        }
                    }
                } else {
                    System.out.println("No existe ningún socio con ese código en la base de datos.");
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key")) {
                System.out.println("El socio está referenciado en un préstamo de la base de datos.");
            } else {
                System.out.println("Error al eliminar el socio: " + e.getMessage());
            }
        }
    }
	
	
	public static void consultarSocios() {
        String sql = "SELECT * FROM socio";
        try (Connection conn = ConfigSQLite.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            List<String> listaSocios = new ArrayList<>();
            while (rs.next()) {
                int codigo = rs.getInt("codigo");
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                listaSocios.add(String.format("Socio [Código = %d, DNI = %s, Nombre = %s, Domicilio = %s, Teléfono = %s, Correo = %s]", codigo, dni, nombre, domicilio, telefono, correo));
            }
            if (listaSocios.isEmpty()) {
                System.out.println("No se ha encontrado ningún socio en la base de datos.");
            } else {
                for (String socio : listaSocios) {
                    System.out.println(socio);
                }
                System.out.println("Se han consultado " + listaSocios.size() + " socios de la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los socios: " + e.getMessage());
        }
    }
	public static void consultarSociosPorLocalidad() {
        String localidad = Teclado.leerCadena("Ingrese la localidad de los socios a consultar: ");
        
        String sql = "SELECT * FROM socio WHERE domicilio LIKE ? ORDER BY nombre ASC";
        try (Connection conn = ConfigSQLite.abrirConexion(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + localidad + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                List<String> listaSocios = new ArrayList<>();
                while (rs.next()) {
                    int codigo = rs.getInt("codigo");
                    String dni = rs.getString("dni");
                    String nombre = rs.getString("nombre");
                    String domicilio = rs.getString("domicilio");
                    String telefono = rs.getString("telefono");
                    String correo = rs.getString("correo");
                    listaSocios.add(String.format("Socio [Código = %d, DNI = %s, Nombre = %s, Domicilio = %s, Teléfono = %s, Correo = %s]", codigo, dni, nombre, domicilio, telefono, correo));
                }
                if (listaSocios.isEmpty()) {
                    System.out.println("No existe ningún socio con esa localidad en la base de datos.");
                } else {
                    for (String socio : listaSocios) {
                        System.out.println(socio);
                    }
                    System.out.println("Se han consultado " + listaSocios.size() + " socios de la base de datos.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los socios por localidad: " + e.getMessage());
        }
    }
	public static void consultarSociosSinPrestamos() {
        String sql = "SELECT * FROM socio WHERE codigo NOT IN (SELECT codigo_socio FROM prestamo)";
        
        try (Connection conn = ConfigSQLite.abrirConexion(); 
             PreparedStatement pstmt = conn.prepareStatement(sql); 
             ResultSet rs = pstmt.executeQuery()) {
            
            List<String> listaSocios = new ArrayList<>();
            while (rs.next()) {
                int codigo = rs.getInt("codigo");
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                listaSocios.add(String.format("Socio [Código = %d, DNI = %s, Nombre = %s, Domicilio = %s, Teléfono = %s, Correo = %s]", codigo, dni, nombre, domicilio, telefono, correo));
            }
            if (listaSocios.isEmpty()) {
                System.out.println("No existe ningún socio sin préstamos en la base de datos.");
            } else {
                for (String socio : listaSocios) {
                    System.out.println(socio);
                }
                System.out.println("Se han consultado " + listaSocios.size() + " socios sin préstamos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los socios sin préstamos: " + e.getMessage());
        }
    }
	public static void consultarSociosConPrestamosFecha() {
        String fecha = Teclado.leerCadena("Ingrese la fecha de los préstamos (YYYY-MM-DD): ");
        
        String sql = "SELECT * FROM socio WHERE codigo IN (SELECT codigo_socio FROM prestamo WHERE fecha_inicio = ?)";
        try (Connection conn = ConfigSQLite.abrirConexion(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fecha);
            try (ResultSet rs = pstmt.executeQuery()) {
                List<String> listaSocios = new ArrayList<>();
                while (rs.next()) {
                    int codigo = rs.getInt("codigo");
                    String dni = rs.getString("dni");
                    String nombre = rs.getString("nombre");
                    String domicilio = rs.getString("domicilio");
                    String telefono = rs.getString("telefono");
                    String correo = rs.getString("correo");
                    listaSocios.add(String.format("Socio [Código = %d, DNI = %s, Nombre = %s, Domicilio = %s, Teléfono = %s, Correo = %s]", codigo, dni, nombre, domicilio, telefono, correo));
                }
                if (listaSocios.isEmpty()) {
                    System.out.println("No existe ningún socio con préstamos en esa fecha.");
                } else {
                    for (String socio : listaSocios) {
                        System.out.println(socio);
                    }
                    System.out.println("Se han consultado " + listaSocios.size() + " socios con préstamos en esa fecha.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los socios con préstamos: " + e.getMessage());
        }
    }
	public static void main(String[] args) {
        int opcion = 0;
        
        do {
            System.out.println("\n--- MENÚ DE OPCIONES ---");
            System.out.println("1. Insertar socio");
            System.out.println("2. Eliminar socio");
            System.out.println("3. Consultar todos los socios");
            System.out.println("4. Consultar socios por localidad");
            System.out.println("5. Consultar socios sin préstamos");
            System.out.println("6. Consultar socios con préstamos en una fecha");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");
            
            try {
                opcion = Teclado.leerEntero("");
                
                switch (opcion) {
                    case 1:
                        insertarSocio();
                        break;
                    case 2:
                        eliminarSocio();
                        break;
                    case 3:
                        consultarSocios();
                        break;
                    case 4:
                        consultarSociosPorLocalidad();
                        break;
                    case 5:
                        consultarSociosSinPrestamos();
                        break;
                    case 6:
                        consultarSociosConPrestamosFecha();
                        break;
                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("La opción de menú debe estar comprendida entre 0 y 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            }
        } while (opcion != 0);
    }
}
