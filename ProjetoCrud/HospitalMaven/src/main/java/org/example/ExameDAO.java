package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExameDAO {

    // Inserindo na base de dados - INSERT
    public void inserir(Exame exame) throws SQLException {
        String sql = "INSERT INTO exame (id_paciente, id_medico, tipo_exame, resultado, data_exame, observacoes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, exame.getId_paciente());
            ps.setInt(2, exame.getId_medico());
            ps.setString(3, exame.getTipo_exame());
            ps.setString(4, exame.getResultado());
            ps.setString(5, exame.getData_exame());
            ps.setString(6, exame.getObservacoes());
            ps.executeUpdate();
        }
    }

    // Exibiindo toda a tabela - SELECT
    public List<Exame> listarTodos() throws SQLException {
        List<Exame> exames = new ArrayList<>();
        String sql = "SELECT * FROM exame";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            MedicoDAO medicoDAO = new MedicoDAO();
            PacienteDAO pacienteDAO = new PacienteDAO();

            while (rs.next()) {
                Exame exame = new Exame(
                        rs.getInt("id_exame"),
                        pacienteDAO.buscarPorId(rs.getInt("id_paciente")),
                        medicoDAO.buscarPorId(rs.getInt("id_medico")),
                        rs.getString("tipo_exame"),
                        rs.getString("resultado"),
                        rs.getString("data_exame"),
                        rs.getString("observacoes")
                );
                exames.add(exame);
            }
        }
        return exames;
    }

    // Buscando uma linha específica - SELECT * FROM... WHERE...
    public Exame buscarPorId(int idExame) throws SQLException {
        String sql = "SELECT * FROM exame WHERE id_exame = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idExame);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Exame(
                            rs.getInt("id_exame"),
                            new PacienteDAO().buscarPorId(rs.getInt("id_paciente")),
                            new MedicoDAO().buscarPorId(rs.getInt("id_medico")),
                            rs.getString("tipo_exame"),
                            rs.getString("resultado"),
                            rs.getString("data_exame"),
                            rs.getString("observacoes")
                    );
                }
            }
        }
        return null;
    }

    // Atualizando a base de dados - UPDATE
    public void atualizar(Exame exame) throws SQLException {
        String sql = "UPDATE exame SET id_paciente = ?, id_medico = ?, tipo_exame = ?, resultado = ?, data_exame = ?, observacoes = ? WHERE id_exame = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, exame.getId_paciente());
            ps.setInt(2, exame.getId_medico());
            ps.setString(3, exame.getTipo_exame());
            ps.setString(4, exame.getResultado());
            ps.setString(5, exame.getData_exame());
            ps.setString(6, exame.getObservacoes());
            ps.setInt(7, exame.getId_exame());
            ps.executeUpdate();
        }
    }

    // Removendo da base de dados - DELETE
    public void deletar(int idExame) throws SQLException {
        String sql = "DELETE FROM exame WHERE id_exame = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idExame);
            ps.executeUpdate();
        }
    }
}