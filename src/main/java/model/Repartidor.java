package model;

public class Repartidor {
    private Integer id;
    private String nombre;
    private String vehiculo;
    private String turno;

    public Repartidor() {
    }

    public Repartidor(Integer id, String nombre, String vehiculo, String turno) {
        this.id = id;
        this.nombre = nombre;
        this.vehiculo = vehiculo;
        this.turno = turno;
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

    public String getVehiculo() {
        return vehiculo;
    }
    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getTurno() {
        return turno;
    }
    public void setTurno(String turno) {
        this.turno = turno;
    }

    @Override
    public String toString() {
        return "%d - %s - %s - %s"
                .formatted(id, nombre, vehiculo, turno);
    }
}

