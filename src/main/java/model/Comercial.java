package model;

public class Comercial {
    private Integer id;
    private String nombre;
    private String zona;
    private String telefono;

    public Comercial() {
    }

    public Comercial(Integer id, String nombre, String zona, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.zona = zona;
        this.telefono = telefono;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getZona() {
        return zona;
    }
    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "%d - %s - %s - %s"
                .formatted(id, nombre, zona, telefono);
    }
}
