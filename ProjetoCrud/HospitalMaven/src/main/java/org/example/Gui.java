// Gui.java
package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class Gui extends JFrame {
    // DAOs para cada entidade
    private PacienteDAO pacienteDAO;
    private MedicoDAO medicoDAO;
    private ConsultaDAO consultaDAO;
    private ExameDAO exameDAO;

    // Componentes e modelos para Pacientes
    private DefaultTableModel modeloPacientes;
    private JTable tablePacientes;

    // Componentes e modelos para Médicos
    private DefaultTableModel modeloMedicos;
    private JTable tableMedicos;

    // Componentes e modelos para Consultas
    private DefaultTableModel modeloConsultas;
    private JTable tableConsultas;

    // Componentes e modelos para Exames
    private DefaultTableModel modeloExames;
    private JTable tableExames;

    private JTabbedPane abas;

    public Gui() {
        // Inicializa os DAOs (presume que as implementações já estão disponíveis)
        pacienteDAO = new PacienteDAO();
        medicoDAO = new MedicoDAO();
        consultaDAO = new ConsultaDAO();
        exameDAO = new ExameDAO();

        configurarJanela();
        criarComponentes();
        carregarDadosIniciais();
    }

    private void configurarJanela() {
        setTitle("Sistema Médico");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void criarComponentes() {
        abas = new JTabbedPane();

        // Cria uma aba para cada entidade
        abas.addTab("Pacientes", criarPainelPacientes());
        abas.addTab("Médicos", criarPainelMedicos());
        abas.addTab("Consultas", criarPainelConsultas());
        abas.addTab("Exames", criarPainelExames());

        add(abas);
    }

    /* ===============================
       Painel e Métodos para Pacientes
       =============================== */
    private JPanel criarPainelPacientes() {
        JPanel painel = new JPanel(new BorderLayout());
        String[] colunas = {"ID", "Nome", "Telefone", "Email", "Sexo", "Endereço"};
        modeloPacientes = new DefaultTableModel(colunas, 0);
        tablePacientes = new JTable(modeloPacientes);
        JScrollPane scroll = new JScrollPane(tablePacientes);

        JPanel botoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");

        btnAdicionar.addActionListener(e -> exibirDialogoPaciente(null));
        btnAtualizar.addActionListener(e -> {
            int linha = tablePacientes.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um paciente para atualizar!");
                return;
            }
            int id = (int) modeloPacientes.getValueAt(linha, 0);
            String nome = (String) modeloPacientes.getValueAt(linha, 1);
            String telefone = (String) modeloPacientes.getValueAt(linha, 2);
            String email = (String) modeloPacientes.getValueAt(linha, 3);
            String sexo = (String) modeloPacientes.getValueAt(linha, 4);
            String endereco = (String) modeloPacientes.getValueAt(linha, 5);
            Paciente paciente = new Paciente(nome, telefone, email, id, sexo, endereco);
            exibirDialogoPaciente(paciente);
        });
        btnRemover.addActionListener(e -> removerPaciente());

        botoes.add(btnAdicionar);
        botoes.add(btnAtualizar);
        botoes.add(btnRemover);

        painel.add(botoes, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private void carregarDadosPacientes() {
        modeloPacientes.setRowCount(0);
        try {
            List<Paciente> pacientes = pacienteDAO.listarTodos();
            for (Paciente p : pacientes) {
                modeloPacientes.addRow(new Object[]{
                        p.getId_paciente(), p.getNome(), p.getTelefone(),
                        p.getEmail(), p.getSexo(), p.getEndereco()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar pacientes: " + ex.getMessage());
        }
    }

    private void exibirDialogoPaciente(Paciente paciente) {
        boolean isNovo = (paciente == null);
        String titulo = isNovo ? "Novo Paciente" : "Atualizar Paciente";
        JDialog dialog = criarDialogo(titulo, new GridLayout(6, 2));
        JTextField txtNome = new JTextField();
        JTextField txtTelefone = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtSexo = new JTextField();
        JTextField txtEndereco = new JTextField();
        JButton btnSalvar = new JButton("Salvar");

        if (!isNovo) {
            txtNome.setText(paciente.getNome());
            txtTelefone.setText(paciente.getTelefone());
            txtEmail.setText(paciente.getEmail());
            txtSexo.setText(paciente.getSexo());
            txtEndereco.setText(paciente.getEndereco());
        }

        dialog.add(new JLabel("Nome:"));
        dialog.add(txtNome);
        dialog.add(new JLabel("Telefone:"));
        dialog.add(txtTelefone);
        dialog.add(new JLabel("Email:"));
        dialog.add(txtEmail);
        dialog.add(new JLabel("Sexo:"));
        dialog.add(txtSexo);
        dialog.add(new JLabel("Endereço:"));
        dialog.add(txtEndereco);
        dialog.add(new JLabel("")); // Espaço vazio
        dialog.add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            try {
                if (isNovo) {
                    Paciente novo = new Paciente(
                            txtNome.getText(),
                            txtTelefone.getText(),
                            txtEmail.getText(),
                            0, // ID será gerado pelo banco
                            txtSexo.getText(),
                            txtEndereco.getText()
                    );
                    pacienteDAO.inserirCompleto(novo);
                } else {
                    Paciente atualizado = new Paciente(
                            txtNome.getText(),
                            txtTelefone.getText(),
                            txtEmail.getText(),
                            paciente.getId_paciente(),
                            txtSexo.getText(),
                            txtEndereco.getText()
                    );
                    pacienteDAO.atualizar(atualizado);
                }
                carregarDadosPacientes();
                dialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar paciente: " + ex.getMessage());
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void removerPaciente() {
        int linha = tablePacientes.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um paciente para remover!");
            return;
        }
        int id = (int) modeloPacientes.getValueAt(linha, 0);
        try {
            pacienteDAO.deletar(id);
            carregarDadosPacientes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover paciente: " + ex.getMessage());
        }
    }

    /* ===============================
       Painel e Métodos para Médicos
       =============================== */
    private JPanel criarPainelMedicos() {
        JPanel painel = new JPanel(new BorderLayout());
        String[] colunas = {"ID", "Nome", "Telefone", "Email", "Especialidade", "CRM"};
        modeloMedicos = new DefaultTableModel(colunas, 0);
        tableMedicos = new JTable(modeloMedicos);
        JScrollPane scroll = new JScrollPane(tableMedicos);

        JPanel botoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");

        btnAdicionar.addActionListener(e -> exibirDialogoMedico(null));
        btnAtualizar.addActionListener(e -> {
            int linha = tableMedicos.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um médico para atualizar!");
                return;
            }
            int id = (int) modeloMedicos.getValueAt(linha, 0);
            String nome = (String) modeloMedicos.getValueAt(linha, 1);
            String telefone = (String) modeloMedicos.getValueAt(linha, 2);
            String email = (String) modeloMedicos.getValueAt(linha, 3);
            String especialidade = (String) modeloMedicos.getValueAt(linha, 4);
            String crm = (String) modeloMedicos.getValueAt(linha, 5);
            Medico medico = new Medico(nome, telefone, email, id, especialidade, crm);
            exibirDialogoMedico(medico);
        });
        btnRemover.addActionListener(e -> removerMedico());

        botoes.add(btnAdicionar);
        botoes.add(btnAtualizar);
        botoes.add(btnRemover);

        painel.add(botoes, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private void carregarDadosMedicos() {
        modeloMedicos.setRowCount(0);
        try {
            List<Medico> medicos = medicoDAO.listarTodos();
            for (Medico m : medicos) {
                modeloMedicos.addRow(new Object[]{
                        m.getId_medico(), m.getNome(), m.getTelefone(),
                        m.getEmail(), m.getEspecialidade(), m.getCrm()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar médicos: " + ex.getMessage());
        }
    }

    private void exibirDialogoMedico(Medico medico) {
        boolean isNovo = (medico == null);
        String titulo = isNovo ? "Novo Médico" : "Atualizar Médico";
        JDialog dialog = criarDialogo(titulo, new GridLayout(6, 2));
        JTextField txtNome = new JTextField();
        JTextField txtTelefone = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtEspecialidade = new JTextField();
        JTextField txtCRM = new JTextField();
        JButton btnSalvar = new JButton("Salvar");

        if (!isNovo) {
            txtNome.setText(medico.getNome());
            txtTelefone.setText(medico.getTelefone());
            txtEmail.setText(medico.getEmail());
            txtEspecialidade.setText(medico.getEspecialidade());
            txtCRM.setText(medico.getCrm());
        }

        dialog.add(new JLabel("Nome:"));
        dialog.add(txtNome);
        dialog.add(new JLabel("Telefone:"));
        dialog.add(txtTelefone);
        dialog.add(new JLabel("Email:"));
        dialog.add(txtEmail);
        dialog.add(new JLabel("Especialidade:"));
        dialog.add(txtEspecialidade);
        dialog.add(new JLabel("CRM:"));
        dialog.add(txtCRM);
        dialog.add(new JLabel(""));
        dialog.add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            try {
                if (isNovo) {
                    Medico novo = new Medico(
                            txtNome.getText(),
                            txtTelefone.getText(),
                            txtEmail.getText(),
                            0, // ID gerado pelo banco
                            txtEspecialidade.getText(),
                            txtCRM.getText()
                    );
                    medicoDAO.inserirCompleto(novo);
                } else {
                    Medico atualizado = new Medico(
                            txtNome.getText(),
                            txtTelefone.getText(),
                            txtEmail.getText(),
                            medico.getId_medico(),
                            txtEspecialidade.getText(),
                            txtCRM.getText()
                    );
                    medicoDAO.atualizar(atualizado);
                }
                carregarDadosMedicos();
                dialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar médico: " + ex.getMessage());
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void removerMedico() {
        int linha = tableMedicos.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um médico para remover!");
            return;
        }
        int id = (int) modeloMedicos.getValueAt(linha, 0);
        try {
            medicoDAO.deletar(id);
            carregarDadosMedicos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover médico: " + ex.getMessage());
        }
    }

    /* ===============================
       Painel e Métodos para Consultas
       =============================== */
    private JPanel criarPainelConsultas() {
        JPanel painel = new JPanel(new BorderLayout());
        String[] colunas = {"ID", "Data/Hora", "Paciente", "Médico", "Motivo"};
        modeloConsultas = new DefaultTableModel(colunas, 0);
        tableConsultas = new JTable(modeloConsultas);
        JScrollPane scroll = new JScrollPane(tableConsultas);

        JPanel botoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");

        btnAdicionar.addActionListener(e -> exibirDialogoConsulta(null));
        btnAtualizar.addActionListener(e -> {
            int linha = tableConsultas.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma consulta para atualizar!");
                return;
            }
            int id = (int) modeloConsultas.getValueAt(linha, 0);
            try {
                // Supondo que consultaDAO.buscarPorId retorne o objeto completo
                Consulta consulta = consultaDAO.buscarPorId(id);
                exibirDialogoConsulta(consulta);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar consulta: " + ex.getMessage());
            }
        });
        btnRemover.addActionListener(e -> removerConsulta());

        botoes.add(btnAdicionar);
        botoes.add(btnAtualizar);
        botoes.add(btnRemover);

        painel.add(botoes, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private void carregarDadosConsultas() {
        modeloConsultas.setRowCount(0);
        try {
            List<Consulta> consultas = consultaDAO.listarTodos();
            for (Consulta c : consultas) {
                // Para exibir, buscamos os nomes do paciente e médico
                Paciente p = pacienteDAO.buscarPorId(c.getId_paciente());
                Medico m = medicoDAO.buscarPorId(c.getId_medico());
                modeloConsultas.addRow(new Object[]{
                        c.getId_consulta(), c.getDataHora(), p.getNome(), m.getNome(), c.getMotivo()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar consultas: " + ex.getMessage());
        }
    }

    private void exibirDialogoConsulta(Consulta consulta) {
        boolean isNovo = (consulta == null);
        String titulo = isNovo ? "Nova Consulta" : "Atualizar Consulta";
        JDialog dialog = criarDialogo(titulo, new GridLayout(5, 2));
        JComboBox<Paciente> cbPacientes = new JComboBox<>();
        JComboBox<Medico> cbMedicos = new JComboBox<>();
        JTextField txtDataHora = new JTextField();
        JTextField txtMotivo = new JTextField();
        JButton btnSalvar = new JButton("Salvar");

        // Carrega os itens para os combo boxes
        new Thread(() -> {
            try {
                List<Paciente> pacientes = pacienteDAO.listarTodos();
                List<Medico> medicos = medicoDAO.listarTodos();
                SwingUtilities.invokeLater(() -> {
                    for (Paciente p : pacientes) {
                        cbPacientes.addItem(p);
                    }
                    for (Medico m : medicos) {
                        cbMedicos.addItem(m);
                    }
                    if (!isNovo) {
                        try {
                            // Seleciona os itens com base nos IDs da consulta
                            Paciente pacienteConsulta = pacienteDAO.buscarPorId(consulta.getId_paciente());
                            Medico medicoConsulta = medicoDAO.buscarPorId(consulta.getId_medico());
                            cbPacientes.setSelectedItem(pacienteConsulta);
                            cbMedicos.setSelectedItem(medicoConsulta);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        txtDataHora.setText(consulta.getDataHora());
                        txtMotivo.setText(consulta.getMotivo());
                    }
                });
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }).start();

        dialog.add(new JLabel("Paciente:"));
        dialog.add(cbPacientes);
        dialog.add(new JLabel("Médico:"));
        dialog.add(cbMedicos);
        dialog.add(new JLabel("Data/Hora (YYYY-MM-DD HH:MM):"));
        dialog.add(txtDataHora);
        dialog.add(new JLabel("Motivo:"));
        dialog.add(txtMotivo);
        dialog.add(new JLabel(""));
        dialog.add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            try {
                if (isNovo) {
                    Consulta nova = new Consulta(
                            0,
                            (Paciente) cbPacientes.getSelectedItem(),
                            (Medico) cbMedicos.getSelectedItem(),
                            txtDataHora.getText(),
                            txtMotivo.getText()
                    );
                    consultaDAO.inserir(nova);
                } else {
                    Consulta atualizada = new Consulta(
                            consulta.getId_consulta(),
                            (Paciente) cbPacientes.getSelectedItem(),
                            (Medico) cbMedicos.getSelectedItem(),
                            txtDataHora.getText(),
                            txtMotivo.getText()
                    );
                    consultaDAO.atualizar(atualizada);
                }
                carregarDadosConsultas();
                dialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar consulta: " + ex.getMessage());
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void removerConsulta() {
        int linha = tableConsultas.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma consulta para remover!");
            return;
        }
        int id = (int) modeloConsultas.getValueAt(linha, 0);
        try {
            consultaDAO.deletar(id);
            carregarDadosConsultas();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover consulta: " + ex.getMessage());
        }
    }

    /* ===============================
       Painel e Métodos para Exames
       =============================== */
    private JPanel criarPainelExames() {
        JPanel painel = new JPanel(new BorderLayout());
        String[] colunas = {"ID", "Tipo", "Paciente", "Médico", "Data", "Resultado", "Observações"};
        modeloExames = new DefaultTableModel(colunas, 0);
        tableExames = new JTable(modeloExames);
        JScrollPane scroll = new JScrollPane(tableExames);

        JPanel botoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");

        btnAdicionar.addActionListener(e -> exibirDialogoExame(null));
        btnAtualizar.addActionListener(e -> {
            int linha = tableExames.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um exame para atualizar!");
                return;
            }
            int id = (int) modeloExames.getValueAt(linha, 0);
            try {
                Exame exame = exameDAO.buscarPorId(id);
                exibirDialogoExame(exame);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar exame: " + ex.getMessage());
            }
        });
        btnRemover.addActionListener(e -> removerExame());

        botoes.add(btnAdicionar);
        botoes.add(btnAtualizar);
        botoes.add(btnRemover);

        painel.add(botoes, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private void carregarDadosExames() {
        modeloExames.setRowCount(0);
        try {
            List<Exame> exames = exameDAO.listarTodos();
            for (Exame ex : exames) {
                Paciente p = pacienteDAO.buscarPorId(ex.getId_paciente());
                Medico m = medicoDAO.buscarPorId(ex.getId_medico());
                modeloExames.addRow(new Object[]{
                        ex.getId_exame(),
                        ex.getTipo_exame(),
                        p.getNome(),
                        m.getNome(),
                        ex.getData_exame(),
                        ex.getResultado(),
                        ex.getObservacoes()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar exames: " + ex.getMessage());
        }
    }

    private void exibirDialogoExame(Exame exame) {
        boolean isNovo = (exame == null);
        String titulo = isNovo ? "Novo Exame" : "Atualizar Exame";
        JDialog dialog = criarDialogo(titulo, new GridLayout(7, 2));
        JComboBox<Paciente> cbPacientes = new JComboBox<>();
        JComboBox<Medico> cbMedicos = new JComboBox<>();
        JTextField txtTipo = new JTextField();
        JTextField txtResultado = new JTextField();
        JTextField txtData = new JTextField();
        JTextField txtObservacoes = new JTextField();
        JButton btnSalvar = new JButton("Salvar");

        // Carrega os itens para os combo boxes
        new Thread(() -> {
            try {
                List<Paciente> pacientes = pacienteDAO.listarTodos();
                List<Medico> medicos = medicoDAO.listarTodos();
                SwingUtilities.invokeLater(() -> {
                    for (Paciente p : pacientes) {
                        cbPacientes.addItem(p);
                    }
                    for (Medico m : medicos) {
                        cbMedicos.addItem(m);
                    }
                    if (!isNovo) {
                        try {
                            Paciente pacienteExame = pacienteDAO.buscarPorId(exame.getId_paciente());
                            Medico medicoExame = medicoDAO.buscarPorId(exame.getId_medico());
                            cbPacientes.setSelectedItem(pacienteExame);
                            cbMedicos.setSelectedItem(medicoExame);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        txtTipo.setText(exame.getTipo_exame());
                        txtResultado.setText(exame.getResultado());
                        txtData.setText(exame.getData_exame());
                        txtObservacoes.setText(exame.getObservacoes());
                    }
                });
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }).start();

        dialog.add(new JLabel("Paciente:"));
        dialog.add(cbPacientes);
        dialog.add(new JLabel("Médico:"));
        dialog.add(cbMedicos);
        dialog.add(new JLabel("Tipo de Exame:"));
        dialog.add(txtTipo);
        dialog.add(new JLabel("Resultado:"));
        dialog.add(txtResultado);
        dialog.add(new JLabel("Data (YYYY-MM-DD):"));
        dialog.add(txtData);
        dialog.add(new JLabel("Observações:"));
        dialog.add(txtObservacoes);
        dialog.add(new JLabel(""));
        dialog.add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            try {
                if (isNovo) {
                    Exame novo = new Exame(
                            0,
                            (Paciente) cbPacientes.getSelectedItem(),
                            (Medico) cbMedicos.getSelectedItem(),
                            txtTipo.getText(),
                            txtResultado.getText(),
                            txtData.getText(),
                            txtObservacoes.getText()
                    );
                    exameDAO.inserir(novo);
                } else {
                    Exame atualizado = new Exame(
                            exame.getId_exame(),
                            (Paciente) cbPacientes.getSelectedItem(),
                            (Medico) cbMedicos.getSelectedItem(),
                            txtTipo.getText(),
                            txtResultado.getText(),
                            txtData.getText(),
                            txtObservacoes.getText()
                    );
                    exameDAO.atualizar(atualizado);
                }
                carregarDadosExames();
                dialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Erro ao salvar exame: " + ex.getMessage());
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void removerExame() {
        int linha = tableExames.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um exame para remover!");
            return;
        }
        int id = (int) modeloExames.getValueAt(linha, 0);
        try {
            exameDAO.deletar(id);
            carregarDadosExames();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover exame: " + ex.getMessage());
        }
    }

    // Método auxiliar para criar um diálogo modal
    private JDialog criarDialogo(String titulo, LayoutManager layout) {
        JDialog dialog = new JDialog(this, titulo, true);
        dialog.setLayout(layout);
        return dialog;
    }

    // Carrega os dados de todas as abas
    private void carregarDadosIniciais() {
        carregarDadosPacientes();
        carregarDadosMedicos();
        carregarDadosConsultas();
        carregarDadosExames();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Gui().setVisible(true));
    }
}
