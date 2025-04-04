package modelo;

public class Prestamo {
	 	private int codigoLibro;
	    private int codigoSocio;
	    private String fechaInicio;
	    private String fechaFin;
	    private String fechaDevolucion;

	    public Prestamo(int codigoLibro, int codigoSocio, String fechaInicio, String fechaFin, String fechaDevolucion) {
	        this.codigoLibro = codigoLibro;
	        this.codigoSocio = codigoSocio;
	        this.fechaInicio = fechaInicio;
	        this.fechaFin = fechaFin;
	        this.fechaDevolucion = fechaDevolucion;
	    }

	    public int getCodigoLibro() {
	        return codigoLibro;
	    }

	    public void setCodigoLibro(int codigoLibro) {
	        this.codigoLibro = codigoLibro;
	    }

	    public int getCodigoSocio() {
	        return codigoSocio;
	    }

	    public void setCodigoSocio(int codigoSocio) {
	        this.codigoSocio = codigoSocio;
	    }

	    public String getFechaInicio() {
	        return fechaInicio;
	    }

	    public void setFechaInicio(String fechaInicio) {
	        this.fechaInicio = fechaInicio;
	    }

	    public String getFechaFin() {
	        return fechaFin;
	    }

	    public void setFechaFin(String fechaFin) {
	        this.fechaFin = fechaFin;
	    }

	    public String getFechaDevolucion() {
	        return fechaDevolucion;
	    }

	    public void setFechaDevolucion(String fechaDevolucion) {
	        this.fechaDevolucion = fechaDevolucion;
	    }

	    @Override
	    public String toString() {
	        return "Prestamo{" +
	                "codigoLibro=" + codigoLibro +
	                ", codigoSocio=" + codigoSocio +
	                ", fechaInicio='" + fechaInicio + '\'' +
	                ", fechaFin='" + fechaFin + '\'' +
	                ", fechaDevolucion='" + fechaDevolucion + '\'' +
	                '}';
	    }
	}
