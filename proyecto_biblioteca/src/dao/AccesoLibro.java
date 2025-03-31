package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



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
        String nombre = Teclado.leerCadena("Ingrese el nombre: ");
        String isbn = Teclado.leerCadena("Ingrese el isbn");
        String titulo = Teclado.leerCadena("Ingrese el titulo");
        String escritor = Teclado.leerCadena("INgrese el escritor");
        int anioPublicacion = Teclado.leerEntero("Ingrese el año de publicacion");
        double puntuacion = Teclado.leerReal("Ingrese la puntuacion");
        
        String url = "jdbc:sqlite:biblioteca.db";
        String sql = "INSERT INTO libro (isbn, nombre,titulo, escritor, anio_publicacion, puntuacion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            pstmt.setString(2, nombre);
            pstmt.setString(3, titulo);
            pstmt.setString(4, escritor);
            pstmt.setInt(5, anioPublicacion);
            pstmt.setDouble(6, puntuacion);
            pstmt.executeUpdate();
            System.out.println("Se ha insertado un libro en la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al insertar el libro: " + e.getMessage());
        }
    }
	public static void borrarLibroPorCodigo() {
        
        int codigo = Teclado.leerEntero("Ingrese el codigo del libro");
        String url = "jdbc:sqlite:biblioteca.db";
        String sql = "DELETE FROM departamente WHERE codigoDpto = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, codigo);
            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                System.out.println("Departamento eliminado correctamente.");
            } else {
                System.out.println("No se encontró el departamento.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el departamento: " + e.getMessage());
        }
    } 
	public static List<Libro> consultarLibros() {
        List<Libro> listaLibros = new ArrayList<>();
        
        String sql = "SELECT codigo, nombre, ubicacion FROM departamente WHERE ubicacion = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int codigo = rs.getInt("codigo");
                String isbn = rs.getString("isbn");
                String titulo = rs.getString("titulo");
                String escritor = rs.getString("escritor");
                int anioPublicacion = rs.getInt("anioPublicacion");
                double puntuacion =rs.getDouble("puntuacion");

                
                Libro libro = new Libro(codigo, isbn, titulo, escritor, anioPublicacion,puntuacion);
                listaLibros.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el departamento: " + e.getMessage());
        }
        return listaLibros;
    }
	public static void main(String[] args) {
        List<Libro> libros = consultarLibros();
        for (Libro libro : libros) {
            System.out.println(libro);
        }
    }

}
