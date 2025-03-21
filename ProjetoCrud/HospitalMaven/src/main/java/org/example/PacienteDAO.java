package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO extends PessoaDAO<Paciente> {

    public PacienteDAO() {
        super("paciente");
    }

    // Inserindo na base de dados - INSERT
    public void inserirCompleto(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente (nome, telefone, email, sexo, endereco) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getTelefone());
            ps.setString(3, paciente.getEmail());
            ps.setString(4, paciente.getSexo());
            ps.setString(5, paciente.getEndereco());
            ps.executeUpdate();
        }
    }

    // Exibindo toda a tabela - SELECT
    public List<Paciente> listarTodos() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente paciente = new Paciente(
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getInt("id_paciente"),
                        rs.getString("sexo"),
                        rs.getString("endereco")
                );
                pacientes.add(paciente);
            }
        }
        return pacientes;
    }

    // Buscando uma linha específica - SELECT * FROM... WHERE...
    public Paciente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM paciente WHERE id_paciente = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("email"),
                            rs.getInt("id_paciente"),
                            rs.getString("sexo"),
                            rs.getString("endereco")
                    );
                }
            }
        }
        return null;
    }

    // Atualizando a base de dados - UPDATE
    public void atualizar(Paciente paciente) throws SQLException {
        String sql = "UPDATE paciente SET nome = ?, telefone = ?, email = ?, sexo = ?, endereco = ? WHERE id_paciente = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getTelefone());
            ps.setString(3, paciente.getEmail());
            ps.setString(4, paciente.getSexo());
            ps.setString(5, paciente.getEndereco());
            ps.setInt(6, paciente.getId_paciente());
            ps.executeUpdate();
        }
    }

    // Removendo da base de dados - DELETE
    public void deletar(int idPaciente) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id_paciente = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erro ao excluir o paciente", e);
        }
    }
}
