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

    public static void main(String[] args) {
        while (true) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Insertar libro");
            System.out.println("2. Eliminar libro");
            System.out.println("3. Consultar libros");
            System.out.println("4. Consultar libros por escritor");
            System.out.println("5. Salir");
            int opcion = Teclado.leerEntero("Ingrese su opción: ");
            switch (opcion) {
                case 1:
                    insertarLibro();
                    break;
                case 2:
                    eliminarLibro();
                    break;
                case 3:
                    consultarLibros();
                    break;
                case 4:
                	consultarLibrosPorEscritor();
                	break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    System.exit(0);
                default:
                    System.out.println("Opción inválida, intente nuevamente.");
            }
        }
    }
}
