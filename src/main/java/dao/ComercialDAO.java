package dao;

import db.Db;
import model.Comercial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Comercial.
 * Define las operaciones CRUD básicas.
 * Explicación detallada en Repartidor*/
public class ComercialDAO {
    private static final String INSERT_SQL =
            "INSERT INTO comercial (id, nombre, zona, telefono) VALUES (?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nombre, zona, telefono FROM comercial WHERE id = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT id, nombre, zona, telefono FROM comercial ORDER BY id";

    private static final String UPDATE_SQL =
            "UPDATE comercial SET nombre = ?, zona = ?, telefono = ? WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM comercial WHERE id = ?";

    public void insert(Comercial c) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(INSERT_SQL)) {
            pst.setInt(1, c.getId());
            pst.setString(2, c.getNombre());
            pst.setString(3, c.getZona());
            pst.setString(4, c.getTelefono());
            pst.executeUpdate();
        }
    }

    public Comercial findById(int id) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(SELECT_BY_ID_SQL)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public List<Comercial> findAll() throws SQLException {
        List<Comercial> out = new ArrayList<>();
        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) out.add(mapRow(rs));
        }
        return out;
    }

    public int update(Comercial c) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement pst = con.prepareStatement(UPDATE_SQL)) {
            pst.setString(1, c.getNombre());
            pst.setString(2, c.getZona());
            pst.setString(3, c.getTelefono());
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

    private Comercial mapRow(ResultSet rs) throws SQLException {
        return new Comercial(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("zona"),
                rs.getString("teléfono"));
    }
}
