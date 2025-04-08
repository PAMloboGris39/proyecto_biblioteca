package dao;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Config.ConfigSQLite;
import entrada.Teclado;

public class ConsultasMultitabla {

    public static void librosMenosPrestados() {
        String sql = """
            SELECT l.isbn, l.titulo, COUNT(p.codigo_libro) AS cantidad
            FROM libro l
            LEFT JOIN prestamo p ON l.codigo = p.codigo_libro
            GROUP BY l.codigo
            HAVING cantidad >= 1
            ORDER BY cantidad ASC
            """;
        try (Connection conn = ConfigSQLite.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            List<String> librosMenosPrestados = new ArrayList<>();
            while (rs.next()) {
                String libro = String.format("ISBN: %s, Título: %s, Número de préstamos: %d",
                    rs.getString("isbn"),
                    rs.getString("titulo"),
                    rs.getInt("cantidad"));
                librosMenosPrestados.add(libro);
            }

            if (librosMenosPrestados.isEmpty()) {
                System.out.println("No se ha encontrado ningún libro que haya sido prestado al menos una vez.");
            } else {
                librosMenosPrestados.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los libros menos prestados: " + e.getMessage());
        }
    }

    public static void sociosMasPrestamos() {
        String sql = """
            SELECT s.dni, s.nombre, COUNT(p.codigo_socio) AS cantidad
            FROM socio s
            JOIN prestamo p ON s.codigo = p.codigo_socio
            GROUP BY s.codigo
            ORDER BY cantidad DESC
            LIMIT 1
            """;
        try (Connection conn = ConfigSQLite.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                System.out.println(String.format("Socio con más préstamos: DNI: %s, Nombre: %s, Préstamos: %d",
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad")));
            } else {
                System.out.println("No hay socios con préstamos registrados.");
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar el socio con más préstamos: " + e.getMessage());
        }
    }

    public static void librosPrestadosInferiorMedia() {
        String sql = """
            SELECT l.isbn, l.titulo, COUNT(p.codigo_libro) AS cantidad
            FROM libro l
            JOIN prestamo p ON l.codigo = p.codigo_libro
            GROUP BY l.codigo
            HAVING cantidad < (SELECT AVG(cantidad) FROM (SELECT COUNT(p.codigo_libro) AS cantidad FROM prestamo p GROUP BY p.codigo_libro))
            """;
        try (Connection conn = ConfigSQLite.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<String> librosInferioresMedia = new ArrayList<>();
            while (rs.next()) {
                String libro = String.format("ISBN: %s, Título: %s, Número de préstamos: %d",
                    rs.getString("isbn"),
                    rs.getString("titulo"),
                    rs.getInt("cantidad"));
                librosInferioresMedia.add(libro);
            }

            if (librosInferioresMedia.isEmpty()) {
                System.out.println("No se ha encontrado ningún libro con préstamos inferiores a la media.");
            } else {
                librosInferioresMedia.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los libros prestados inferiores a la media: " + e.getMessage());
        }
    }

    public static void sociosSuperiorMedia() {
        String sql = """
            SELECT s.dni, s.nombre, COUNT(p.codigo_socio) AS cantidad
            FROM socio s
            JOIN prestamo p ON s.codigo = p.codigo_socio
            GROUP BY s.codigo
            HAVING cantidad > (SELECT AVG(cantidad) FROM (SELECT COUNT(p.codigo_socio) AS cantidad FROM prestamo p GROUP BY p.codigo_socio))
            """;
        try (Connection conn = ConfigSQLite.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<String> sociosSuperiorMedia = new ArrayList<>();
            while (rs.next()) {
                String socio = String.format("DNI: %s, Nombre: %s, Préstamos: %d",
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getInt("cantidad"));
                sociosSuperiorMedia.add(socio);
            }

            if (sociosSuperiorMedia.isEmpty()) {
                System.out.println("No se ha encontrado ningún socio con más préstamos que la media.");
            } else {
                sociosSuperiorMedia.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los socios con más préstamos que la media: " + e.getMessage());
        }
    }

    public static void listadoLibrosPorPrestamos() {
        String sql = """
            SELECT l.isbn, l.titulo, COUNT(p.codigo_libro) AS cantidad
            FROM libro l
            JOIN prestamo p ON l.codigo = p.codigo_libro
            GROUP BY l.codigo
            ORDER BY cantidad DESC
            """;
        try (Connection conn = ConfigSQLite.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<String> librosPorPrestamos = new ArrayList<>();
            while (rs.next()) {
                String libro = String.format("ISBN: %s, Título: %s, Número de préstamos: %d",
                    rs.getString("isbn"),
                    rs.getString("titulo"),
                    rs.getInt("cantidad"));
                librosPorPrestamos.add(libro);
            }

            if (librosPorPrestamos.isEmpty()) {
                System.out.println("No se ha encontrado ningún libro prestado.");
            } else {
                librosPorPrestamos.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los libros por préstamos: " + e.getMessage());
        }
    }

    public static void listadoSociosPorPrestamos() {
        String sql = """
            SELECT s.dni, s.nombre, COUNT(p.codigo_socio) AS cantidad
            FROM socio s
            JOIN prestamo p ON s.codigo = p.codigo_socio
            GROUP BY s.codigo
            ORDER BY cantidad DESC
            """;
        try (Connection conn = ConfigSQLite.abrirConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<String> sociosPorPrestamos = new ArrayList<>();
            while (rs.next()) {
                String socio = String.format("DNI: %s, Nombre: %s, Préstamos: %d",
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getInt("cantidad"));
                sociosPorPrestamos.add(socio);
            }

            if (sociosPorPrestamos.isEmpty()) {
                System.out.println("No se ha encontrado ningún socio con préstamos.");
            } else {
                sociosPorPrestamos.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar los socios por préstamos: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        int opcion = -1;

        do {
            System.out.println("\n--- MENÚ DE CONSULTAS MULTITABLA ---");
            System.out.println("1. Libros prestados menos veces (mínimo una vez)");
            System.out.println("2. Socios que han realizado más préstamos");
            System.out.println("3. Libros prestados menos que la media");
            System.out.println("4. Socios con más préstamos que la media");
            System.out.println("5. Libros ordenados por número de préstamos (descendente)");
            System.out.println("6. Socios ordenados por número de préstamos (descendente)");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            try {
                opcion = Teclado.leerEntero("");

                switch (opcion) {
                    case 1:
                        ConsultasMultitabla.librosMenosPrestados();
                        break;
                    case 2:
                        ConsultasMultitabla.sociosMasPrestamos();
                        break;
                    case 3:
                        ConsultasMultitabla.librosPrestadosInferiorMedia();
                        break;
                    case 4:
                        ConsultasMultitabla.sociosSuperiorMedia();
                        break;
                    case 5:
                        ConsultasMultitabla.listadoLibrosPorPrestamos();
                        break;
                    case 6:
                        ConsultasMultitabla.listadoSociosPorPrestamos();
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