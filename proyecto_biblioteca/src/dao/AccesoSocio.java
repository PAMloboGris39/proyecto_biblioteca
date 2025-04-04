
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Config.ConfigSQLite;
import entrada.Teclado;
import exceptions.BDException;
import modelo.Socio;

public class AccesoSocio {

	public static void insertarSocio(Socio socioNuevo) throws BDException {
		PreparedStatement ps = null;
		Connection conexion = null;
		try {
			// Conexi�n a la bd
			conexion = ConfigSQLite.abrirConexion();

			String query = "INSERT INTO socio (codigo, dni, nombre, domicilio, telefono, correo) values (?, ?, ?, ?, ?)";
			ps = conexion.prepareStatement(query);
			ps.setInt(1, socioNuevo.getCodigo());
			ps.setString(2, socioNuevo.getDni());
			ps.setString(3, socioNuevo.getNombre());
			ps.setString(4, socioNuevo.getDomicilio());
			ps.setString(5, socioNuevo.getTelefono());
			ps.setString(6, socioNuevo.getCorreo());
			ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
	}

	public static int borrarSocio(int codigo) throws BDException {
		
		PreparedStatement ps = null;
		Connection conexion = null;
		int resultados = 0;

		try {
			String consultaSocio = "SELECT codigo FROM socio WHERE codigo = ?";
			ps = conexion.prepareStatement(consultaSocio);
			ps.setInt(1, codigo);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String eliminarSocio = "DELETE FROM socio WHERE codigo = ?";
				// Socio encontrado, proceder a eliminar
				try {
					PreparedStatement ps2 = conexion.prepareStatement(eliminarSocio);
					ps2.setInt(1, codigo);
					int filasAfectadas = ps2.executeUpdate();
					if (filasAfectadas > 0) {
						System.out.println("Se ha eliminado un socio de la base de datos.");
					}
				} catch (SQLException e) {
					System.out.println("Error al eliminar el socio: " + e.getMessage());
				}
			} else {
				System.out.println("No existe ningún socio con ese código en la base de datos.");
			}
		} catch (SQLException e) {
			if (e.getMessage().contains("foreign key")) {
				System.out.println("El socio está referenciado en un préstamo de la base de datos.");
			} else {
				System.out.println("Error al eliminar el socio: " + e.getMessage());
			}
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
		return resultados;
	}

	public static ArrayList<Socio> consultarSocios() throws BDException {

		ArrayList<Socio> listaSocios = new ArrayList<Socio>();
		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			// Conexi�n a la bd
			conexion = ConfigSQLite.abrirConexion();
			String query = "SELECT * FROM socio";
			ps = conexion.prepareStatement(query);
			ResultSet resultados = ps.executeQuery(query);

			while (resultados.next()) {
				int codigoSocio = resultados.getInt("codigo");
				String dni = resultados.getString("dni");
				String nombre = resultados.getString("nombre");
				String domicilio = resultados.getString("domicilio");
				String telefono = resultados.getString("telefono");
				String correo = resultados.getString("correo");

				Socio socio = new Socio(codigoSocio, dni, nombre, domicilio, telefono, correo);
				listaSocios.add(socio);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
		return listaSocios;
	}

	public static ArrayList<Socio> consultarSociosPorLocalidad() {

		String localidad = Teclado.leerCadena("Ingrese la localidad de los socios a consultar: ");
		String sql = "SELECT * FROM socio WHERE domicilio LIKE ? ORDER BY nombre ASC";
		PreparedStatement ps = null;
		Connection conn = null;
		ArrayList<Socio> listaSocios = new ArrayList<Socio>();

		try {
			conn = ConfigSQLite.abrirConexion();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%" + localidad + "%");
			ResultSet rs = ps.executeQuery();

			try {
				while (rs.next()) {
					int codigo = rs.getInt("codigo");
					String dni = rs.getString("dni");
					String nombre = rs.getString("nombre");
					String domicilio = rs.getString("domicilio");
					String telefono = rs.getString("telefono");
					String correo = rs.getString("correo");

					Socio socio = new Socio(codigo, dni, nombre, domicilio, telefono, correo);
					listaSocios.add(socio);
				}

			} catch (SQLException e) {
				System.out.println("Error al consultar los socios por localidad: " + e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println("Error al consultar los socios por localidad: " + e.getMessage());
		} finally {
			if (conn != null) {
				ConfigSQLite.cerrarConexion(conn);
			}
		}
		return listaSocios;
	}

	public static ArrayList<Socio> consultarSociosSinPrestamos() {

		ArrayList<Socio> listaSocios = new ArrayList<Socio>();
		PreparedStatement ps = null;
		Connection conexion = null;
		try {

			conexion = ConfigSQLite.abrirConexion();

			String sql = "SELECT * FROM socio WHERE codigo NOT IN (SELECT codigo_socio FROM prestamo)";

			ps = conexion.prepareStatement(sql);
			ResultSet resultados = ps.executeQuery();

			while (resultados.next()) {

				int codigo = resultados.getInt("codigo");
				String dni = resultados.getString("dni");
				String nombre = resultados.getString("nombre");
				String domicilio = resultados.getString("domicilio");
				String telefono = resultados.getString("telefono");
				String correo = resultados.getString("correo");

				Socio socio = new Socio(codigo, dni, nombre, domicilio, telefono, correo);
				listaSocios.add(socio);
			}
			if (listaSocios.isEmpty()) {
				System.out.println("No existe ningún socio sin préstamos en la base de datos.");
			} else {
				for (Socio socio : listaSocios) {
					System.out.println(socio);
				}
				System.out.println("Se han consultado " + listaSocios.size() + " socios sin préstamos.");
			}
		} catch (SQLException e) {
			System.out.println("Error al consultar los socios sin préstamos: " + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
		return listaSocios;
	}

	public static void consultarSociosConPrestamosFecha() {
		String fecha = Teclado.leerCadena("Ingrese la fecha de los préstamos (YYYY-MM-DD): ");

		List<Socio> listaSocios = new ArrayList<>();
		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			conexion = ConfigSQLite.abrirConexion();
			String sql = "SELECT * FROM socio WHERE codigo IN (SELECT codigo_socio FROM prestamo WHERE fecha_inicio = ?)";
			ps = conexion.prepareStatement(sql);
			ps.setString(1, fecha);
			ResultSet resultados = ps.executeQuery();

			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String dni = resultados.getString("dni");
				String nombre = resultados.getString("nombre");
				String domicilio = resultados.getString("domicilio");
				String telefono = resultados.getString("telefono");
				String correo = resultados.getString("correo");

				Socio socio = new Socio(codigo, dni, nombre, domicilio, telefono, correo);
				listaSocios.add(socio);
			}
			if (listaSocios.isEmpty()) {
				System.out.println("No existe ningún socio sin préstamos en la base de datos.");
			} else {
				for (Socio socio : listaSocios) {
					System.out.println(socio);
				}
				System.out.println("Se han consultado " + listaSocios.size() + " socios sin préstamos.");
			}
		} catch (SQLException e) {
			System.out.println("Error al consultar los socios sin préstamos: " + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
	}

}
