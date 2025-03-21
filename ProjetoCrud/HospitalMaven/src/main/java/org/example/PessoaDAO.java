package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class PessoaDAO<T extends Pessoa> {
    protected String tabela;

    public PessoaDAO(String tabela) {
        this.tabela = tabela;
    }

    public void inserir(T pessoa) throws SQLException {
        String sql = "INSERT INTO " + tabela + " (nome, telefone, email) VALUES (?, ?, ?)";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getTelefone());
            ps.setString(3, pessoa.getEmail());
            ps.executeUpdate();
        }
    }

    public void atualizar(T pessoa) throws SQLException {
        String sql = "UPDATE " + tabela + " SET nome = ?, telefone = ?, email = ? WHERE id = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getTelefone());
            ps.setString(3, pessoa.getEmail());
            ps.setInt(4, pessoa instanceof Paciente ? ((Paciente) pessoa).getId_paciente() : ((Medico) pessoa).getId_medico());
            ps.executeUpdate();
        }
    }
}