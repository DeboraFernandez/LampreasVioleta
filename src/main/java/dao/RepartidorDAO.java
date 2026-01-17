package dao;

import db.Db;
import model.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Repartidor.

 * Proporciona las operaciones CRUD básicas .
 */
public class RepartidorDAO {



    // Inserta un nuevo repartidor.

    private static final String INSERT_SQL =
            "INSERT INTO repartidor (id, nombre, vehiculo, turno) VALUES (?, ?, ?, ?)";

    // Consulta un repartidor por su id.

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nombre, vehiculo, turno FROM repartidor WHERE id = ?";

    // Lista todos los repartidores

    private static final String SELECT_ALL_SQL =
            "SELECT id, nombre, vehiculo, turno FROM repartidor ORDER BY id";

    //Actualiza los datos de un repartidor

    private static final String UPDATE_SQL =
            "UPDATE repartidor SET nombre = ?, vehiculo = ?, turno = ? WHERE id = ?";

    // Elimina un repartidor por su id
    private static final String DELETE_SQL =
            "DELETE FROM repartidor WHERE id = ?";

    // ===============================
    //  MÉTODOS CRUD BÁSICOS
    // ===============================

    // Inserta un repartidor nuevo.

    public void insert(Repartidor r) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(INSERT_SQL)) {

            pst.setInt(1, r.getId());
            pst.setString(2, r.getNombre());
            pst.setString(3, r.getVehiculo());
            pst.setString(4, r.getTurno());

            pst.executeUpdate();
        }
    }

    // Devuelve un repartidor por su id o null si no existe.

    public Repartidor findById(int id) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(SELECT_BY_ID_SQL)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    // Devuelve una lista con todos los repartidores.

    public List<Repartidor> findAll() throws SQLException {
        List<Repartidor> out = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                out.add(mapRow(rs));
            }
        }

        return out;
    }

    // Actualiza un repartidor existente. Devuelve el número de filas afectadas.

    public int update(Repartidor r) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(UPDATE_SQL)) {

            pst.setString(1, r.getNombre());
            pst.setString(2, r.getVehiculo());
            pst.setString(3, r.getTurno());
            pst.setInt(4, r.getId());

            return pst.executeUpdate();
        }
    }

    // Elimina un repartidor por su id. Devuelve el número de filas afectadas.

    public int deleteById(int id) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(DELETE_SQL)) {

            pst.setInt(1, id);
            return pst.executeUpdate();
        }
    }

    //  MAPEO ResultSet → Repartidor


    // Convierte una fila del ResultSet en un objeto Repartidor. Este sigo sin entenderlo

    private Repartidor mapRow(ResultSet rs) throws SQLException {
        return new Repartidor(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("vehiculo"),
                rs.getString("turno")
        );
    }
}
