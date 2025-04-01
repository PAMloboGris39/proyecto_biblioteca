package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Config.ConfigSQLite;
import exceptions.BDException;
import modelo.Socio;

public class AccesoSocio {
	
	public static void insertarSocio(Socio socioNuevo)throws BDException {
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
			// Conexi�n a la bd
			conexion = ConfigSQLite.abrirConexion();
			String query = "DELETE FROM socio WHERE codigo = ?";

			ps = conexion.prepareStatement(query);
			ps.setFloat(1, codigo);
			resultados = ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
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
			String query = "SELECT socio.codigo, socio.dni, socio.nombre, "
					+ "socio.domicilio, socio.telefono, socio.correo";

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

}
