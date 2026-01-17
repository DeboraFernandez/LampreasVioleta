package dao;

import db.Db;
import model.Repartidor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Comercial.
 * Define las operaciones CRUD básicas.
 */
private static final String INSERT_SQL =
        "INSERT INTO repartidor (id, nombre, vehículo, turno) VALUES (?, ?, ?, ?)";

private static final String SELECT_BY_ID_SQL =
        "SELECT id, nombre, vehículo, turno FROM repartidor WHERE id = ?";

private static final String SELECT_ALL_SQL =
        "SELECT id, nombre, vehículo, turno FROM repartidor ORDER BY id";

private static final String UPDATE_SQL =
        "UPDATE repartidor SET nombre = ?, vehículo = ?, turno = ? WHERE id = ?";

private static final String DELETE_SQL =
        "DELETE FROM repartidor WHERE id = ?";

public void insert(Repartidor c) throws SQLException {
    try (Connection con = Db.getConnection();
         PreparedStatement pst = con.prepareStatement(INSERT_SQL)) {
        pst.setInt(1, c.getId());
        pst.setString(2, c.getNombre());
        pst.setString(3, c.getVehiculo());
        pst.setString(4, c.getTurno());
        pst.executeUpdate();
    }
}

public Repartidor findById(int id) throws SQLException {
    try (Connection con = Db.getConnection();
         PreparedStatement pst = con.prepareStatement(SELECT_BY_ID_SQL)) {
        pst.setInt(1, id);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }
}

public List<Repartidor> findAll() throws SQLException {
    List<Repartidor> out = new ArrayList<>();
    try (Connection con = Db.getConnection();
         PreparedStatement pst = con.prepareStatement(SELECT_ALL_SQL);
         ResultSet rs = pst.executeQuery()) {
        while (rs.next()) out.add(mapRow(rs));
    }
    return out;
}

public int update(Repartidor c) throws SQLException {
    try (Connection con = Db.getConnection();
         PreparedStatement pst = con.prepareStatement(UPDATE_SQL)) {
        pst.setString(1, c.getNombre());
        pst.setString(2, c.getVehiculo());
        pst.setString(3, c.getTurno());
        pst.setInt(4, c.getId());
        return pst.executeUpdate();
    }
}

public int deleteById(int id) throws SQLException {
    try (Connection con = Db.getConnection();
         PreparedStatement pst = con.prepareStatement(DELETE_SQL)) {
        pst.setInt(1, id);
        return pst.executeUpdate();
    }
}

private Repartidor mapRow(ResultSet rs) throws SQLException {
    return new Repartidor(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("vehículo"),
            rs.getString("turno")
    );
}
