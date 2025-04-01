package dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Config.ConfigSQLite;
import entrada.Teclado;
import modelo.Libro;

/*
 *  git status
 *  
 *  SUBIR CAMBIOS 
 *  git add -A
 *  git commit -m "mensaje info"
 *  git push
 *  
 *  BAJAR CAMBIOS
 *  git pull
 */
public class AccesoLibro {
	public static void insertarLibro() {
        String isbn = Teclado.leerCadena("Ingrese el ISBN: ");
        String titulo = Teclado.leerCadena("Ingrese el título: ");
        String escritor = Teclado.leerCadena("Ingrese el escritor: ");
        int añoPublicacion = Teclado.leerEntero("Ingrese el año de publicación: ");
        double puntuacion = Teclado.leerReal("Ingrese la puntuación: ");

        String sql = "INSERT INTO libro (isbn, titulo, escritor, año_publicacion, puntuacion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConfigSQLite.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            pstmt.setString(2, titulo);
            pstmt.setString(3, escritor);
            pstmt.setInt(4, añoPublicacion);
            pstmt.setDouble(5, puntuacion);
            pstmt.executeUpdate();
            System.out.println("Se ha insertado un libro en la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al insertar el libro: " + e.getMessage());
        }
    }

    // Eliminar un libro por código
    public static void eliminarLibro() {
        int codigo = Teclado.leerEntero("Ingrese el código del libro a eliminar: ");

        String sql = "DELETE FROM libro WHERE codigo = ?";
        try (Connection conn = ConfigSQLite.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Se ha eliminado un libro de la base de datos.");
            } else {
                System.out.println("No existe ningún libro con ese código en la base de datos.");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key")) {
                System.out.println("El libro está referenciado en un préstamo de la base de datos.");
            } else {
                System.out.println("Error al eliminar el libro: " + e.getMessage());
            }
        }
    }

    // Consultar todos los libros de la base de datos
    public static void consultarLibros() {
        String sql = "SELECT * FROM libro";
        try (Connection conn = ConfigSQLite.abrirConexion(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            List<Libro> listaLibros = new ArrayList<>();
            while (rs.next()) {
                int codigo = rs.getInt("codigo");
                String isbn = rs.getString("isbn");
                String titulo = rs.getString("titulo");
                String escritor = rs.getString("escritor");
                int añoPublicacion = rs.getInt("año_publicacion");
                double puntuacion = rs.getDouble("puntuacion");
                listaLibros.add(new Libro(codigo, isbn, titulo, escritor, añoPublicacion, puntuacion));
            }
            if (listaLibros.isEmpty()) {
                System.out.println("No se ha encontrado ningún libro en la base de datos.");
            } else {
                for (Libro libro : listaLibros) {
                    System.out.println(libro);
                }
                System.out.println("Número total de libros: " + listaLibros.size());
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los libros: " + e.getMessage());
        }
    }
    public static void consultarLibrosPorEscritor() {
        String escritor = Teclado.leerCadena("Ingrese el nombre del escritor: ");
        
        String sql = "SELECT * FROM libro WHERE escritor = ? ORDER BY puntuacion DESC";
        
        try (Connection conn = ConfigSQLite.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, escritor);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Libro> listaLibros = new ArrayList<>();
                
                while (rs.next()) {
                    int codigo = rs.getInt("codigo");
                    String isbn = rs.getString("isbn");
                    String titulo = rs.getString("titulo");
                    int añoPublicacion = rs.getInt("año_publicacion");
                    double puntuacion = rs.getDouble("puntuacion");
                    listaLibros.add(new Libro(codigo, isbn, titulo, escritor, añoPublicacion, puntuacion));
                }
                
                if (listaLibros.isEmpty()) {
                    System.out.println("No existe ningún libro con ese escritor en la base de datos.");
                } else {
                    for (Libro libro : listaLibros) {
                        System.out.println(libro);
                    }
                    System.out.println("Se han consultado " + listaLibros.size() + " libros de la base de datos.");
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error al consultar los libros: " + e.getMessage());
        }
    }
    public static void consultarLibrosNoPrestados() {
        String sql = "SELECT * FROM libro WHERE codigo NOT IN (SELECT libro_codigo FROM prestamo)";
        
        try (Connection conn = ConfigSQLite.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            List<Libro> listaLibros = new ArrayList<>();
            
            while (rs.next()) {
                int codigo = rs.getInt("codigo");
                String isbn = rs.getString("isbn");
                String titulo = rs.getString("titulo");
                String escritor = rs.getString("escritor");
                int añoPublicacion = rs.getInt("año_publicacion");
                double puntuacion = rs.getDouble("puntuacion");
                listaLibros.add(new Libro(codigo, isbn, titulo, escritor, añoPublicacion, puntuacion));
            }
            
            if (listaLibros.isEmpty()) {
                System.out.println("No existe ningún libro no prestado en la base de datos.");
            } else {
                for (Libro libro : listaLibros) {
                    System.out.println(libro);
                }
                System.out.println("Número total de libros no prestados: " + listaLibros.size());
            }
            
        } catch (SQLException e) {
            System.out.println("Error al consultar los libros no prestados: " + e.getMessage());
        }
    }
    public static void consultarLibrosDevueltosPorFecha() {
        String fechaDevolucion = Teclado.leerCadena("Ingrese la fecha de devolución (YYYY-MM-DD): ");
        String sql = "SELECT libro.* FROM libro INNER JOIN prestamo ON libro.codigo = prestamo.libro_codigo WHERE prestamo.fecha_devolucion = ?";
        
        try (Connection conn = ConfigSQLite.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fechaDevolucion);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Libro> listaLibros = new ArrayList<>();
                
                while (rs.next()) {
                    int codigo = rs.getInt("codigo");
                    String isbn = rs.getString("isbn");
                    String titulo = rs.getString("titulo");
                    String escritor = rs.getString("escritor");
                    int añoPublicacion = rs.getInt("año_publicacion");
                    double puntuacion = rs.getDouble("puntuacion");
                    listaLibros.add(new Libro(codigo, isbn, titulo, escritor, añoPublicacion, puntuacion));
                }
                
                if (listaLibros.isEmpty()) {
                    System.out.println("No existe ningún libro devuelto en esa fecha en la base de datos.");
                } else {
                    for (Libro libro : listaLibros) {
                        System.out.println(libro);
                    }
                    System.out.println("Número total de libros devueltos en esa fecha: " + listaLibros.size());
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error al consultar los libros devueltos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
       
        int opcion;
        
        do {
            System.out.println("\n--- MENÚ DE OPCIONES ---");
            System.out.println("1. Insertar libro");
            System.out.println("2. Eliminar libro");
            System.out.println("3. Consultar todos los libros");
            System.out.println("4. Consultar libros por escritor");
            System.out.println("5. Consultar libros no prestados");
            System.out.println("6. Consultar libros devueltos por fecha");
            System.out.println("7. Salir");
            System.out.print("Ingrese una opción: ");
            
            try {
                opcion = Teclado.leerEntero("Elije una opcion");
                
                switch (opcion) {
                    case 1:
                        try {
                            insertarLibro();
                        } catch (Exception e) {
                            System.out.println("Error al insertar libro: " + e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            eliminarLibro();
                        } catch (Exception e) {
                            System.out.println("Error al eliminar libro: " + e.getMessage());
                        }
                        break;
                    case 3:
                        try {
                            consultarLibros();
                        } catch (Exception e) {
                            System.out.println("Error al consultar libros: " + e.getMessage());
                        }
                        break;
                    case 4:
                        try {
                            consultarLibrosPorEscritor();
                        } catch (Exception e) {
                            System.out.println("Error al consultar libros por escritor: " + e.getMessage());
                        }
                        break;
                    case 5:
                        try {
                            consultarLibrosNoPrestados();
                        } catch (Exception e) {
                            System.out.println("Error al consultar libros no prestados: " + e.getMessage());
                        }
                        break;
                    case 6:
                        try {
                            consultarLibrosDevueltosPorFecha();
                        } catch (Exception e) {
                            System.out.println("Error al consultar libros devueltos por fecha: " + e.getMessage());
                        }
                        break;
                    case 7:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
                opcion = 0; // Para evitar que termine el bucle inesperadamente
            }
        } while (opcion != 7);
        
       
    }
}
