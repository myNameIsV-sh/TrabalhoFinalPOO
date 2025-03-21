// ConsultaDAO.java
package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    // Inserindo na base de dados - INSERT
    public void inserir(Consulta consulta) throws SQLException {
        String sql = "INSERT INTO consulta (id_paciente, id_medico, dataHora, motivo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, consulta.getId_paciente());
            ps.setInt(2, consulta.getId_medico());
            ps.setString(3, consulta.getDataHora());
            ps.setString(4, consulta.getMotivo());
            ps.executeUpdate();
        }
    }


    // Exibindo toda a tabela - SELECT
    public List<Consulta> listarTodos() throws SQLException {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT * FROM consulta";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Consulta consulta = new Consulta(
                        rs.getInt("id_consulta"),
                        new PacienteDAO().buscarPorId(rs.getInt("id_paciente")),
                        new MedicoDAO().buscarPorId(rs.getInt("id_medico")),
                        rs.getString("dataHora"),
                        rs.getString("motivo")
                );
                consultas.add(consulta);
            }
        }
        return consultas;
    }

    // Buscando uma linha específica - SELECT * FROM... WHERE...
    public Consulta buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM consulta WHERE id_consulta = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Consulta(
                        rs.getInt("id_consulta"),
                        new PacienteDAO().buscarPorId(rs.getInt("id_paciente")),
                        new MedicoDAO().buscarPorId(rs.getInt("id_medico")),
                        rs.getString("dataHora"),
                        rs.getString("motivo")
                );
            }
        }
        return null;
    }

    // Atualizando a base de dados - UPDATE
    public void atualizar(Consulta consulta) throws SQLException {
        String sql = "UPDATE consulta SET id_paciente = ?, id_medico = ?, dataHora = ?, motivo = ? WHERE id_consulta = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, consulta.getId_paciente());
            ps.setInt(2, consulta.getId_medico());
            ps.setString(3, consulta.getDataHora());
            ps.setString(4, consulta.getMotivo());
            ps.setInt(5, consulta.getId_consulta());
            ps.executeUpdate();
        }
    }

    // Removendo da base de dados - DELETE
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM consulta WHERE id_consulta = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
