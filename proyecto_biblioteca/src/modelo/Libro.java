package modelo;

public class Libro {
    private int codigo;
    private String isbn;
    private String titulo;
    private String escritor;
    private int añoPublicacion;
    private double puntuacion;

    // Constructor
    public Libro(int codigo, String isbn, String titulo, String escritor, int añoPublicacion, double puntuacion) {
        this.codigo = codigo;
        this.isbn = isbn;
        this.titulo = titulo;
        this.escritor = escritor;
        this.añoPublicacion = añoPublicacion;
        this.puntuacion = puntuacion;
    }

    // Getters y Setters
    public int getCodigo() { 
    	return codigo; 
    	}
    
    public void setCodigo(int codigo) { 
    	this.codigo = codigo; 
    	}
    
    public String getIsbn() { 
    	return isbn; 
    	}
    
    public void setIsbn(String isbn) { 
    	this.isbn = isbn; 
    	}
    
    public String getTitulo() { 
    	return titulo; 
    	}
    public void setTitulo(String titulo) { 
    	this.titulo = titulo; 
    	}
    
    public String getEscritor() { 
    	return escritor;
    	}
    
    public void setEscritor(String escritor) { 
    	this.escritor = escritor; 
    }
    public int getAnioPublicacion() { 
    	return añoPublicacion; 
    }
    public void setAñoPublicacion(int añoPublicacion) { 
    	this.añoPublicacion = añoPublicacion; 
    }
    public double getPuntuacion() { 
    	return puntuacion; 
    }
    public void setPuntuacion(double puntuacion) { 
    	this.puntuacion = puntuacion; 
    }

    // Método toString
    @Override
    public String toString() {
        return "Libro{" +
                "codigo=" + codigo +
                ", isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", escritor='" + escritor + '\'' +
                ", anioPublicacion=" + añoPublicacion +
                ", puntuacion=" + puntuacion +
                '}';
    }
}
