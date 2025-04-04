package modelo;

public class probar {
	  private int codigo;        
	    private String dni;        
	    private String nombre;     
	    private String domicilio;  
	    private String telefono;   
	    private String correo;     

	    
	    public probar(int codigo, String dni, String nombre, String domicilio, String telefono, String correo) {
	        if (dni == null || dni.isEmpty()) {
	            throw new IllegalArgumentException("El DNI no puede ser nulo o vacío.");
	        }
	        if (nombre == null || nombre.isEmpty()) {
	            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
	        }
	        if (domicilio == null || domicilio.isEmpty()) {
	            throw new IllegalArgumentException("El domicilio no puede ser nulo o vacío.");
	        }
	        if (telefono == null || telefono.isEmpty()) {
	            throw new IllegalArgumentException("El teléfono no puede ser nulo o vacío.");
	        }
	        if (correo == null || correo.isEmpty()) {
	            throw new IllegalArgumentException("El correo no puede ser nulo o vacío.");
	        }

	        this.codigo = codigo;
	        this.dni = dni;
	        this.nombre = nombre;
	        this.domicilio = domicilio;
	        this.telefono = telefono;
	        this.correo = correo;
	    }

	    // Métodos Getters y Setters
	    public int getCodigo() {
	        return codigo;
	    }

	    public void setCodigo(int codigo) {
	        this.codigo = codigo;
	    }

	    public String getDni() {
	        return dni;
	    }

	    public void setDni(String dni) {
	        this.dni = dni;
	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }

	    public String getDomicilio() {
	        return domicilio;
	    }

	    public void setDomicilio(String domicilio) {
	        this.domicilio = domicilio;
	    }

	    public String getTelefono() {
	        return telefono;
	    }

	    public void setTelefono(String telefono) {
	        this.telefono = telefono;
	    }

	    public String getCorreo() {
	        return correo;
	    }

	    public void setCorreo(String correo) {
	        this.correo = correo;
	    }
}
