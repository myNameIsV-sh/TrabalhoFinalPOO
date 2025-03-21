package org.example;
import java.sql.SQLException;

public class Principal {
    public static void main(String[] args) {
        try {
            // Criando objetos
            Medico medico1 = new Medico(
                    "Dr. Pedro Lima",
                    "11912345678",
                    "pedro@email.com",
                    1,
                    "Ortopedia",
                    "CRM789456"
            );

            Paciente paciente1 = new Paciente(
                    "Carlos Souza",
                    "11987654321",
                    "carlos@email.com",
                    1,
                    "Masculino",
                    "Rua B, 456"
            );

            // Instanciando DAOs
            MedicoDAO medicoDAO = new MedicoDAO();
            PacienteDAO pacienteDAO = new PacienteDAO();
            ConsultaDAO consultaDAO = new ConsultaDAO();

            // Persistindo no banco
            medicoDAO.inserirCompleto(medico1);
            pacienteDAO.inserirCompleto(paciente1);

            Consulta consulta1 = new Consulta(
                    1,
                    paciente1,
                    medico1,
                    "2023-10-25 14:30",
                    "Check-up anual"
            );

            consultaDAO.inserir(consulta1);

            // Criando e persistindo uma consulta


            // Listando registros
            System.out.println("\nMédicos cadastrados:");
            medicoDAO.listarTodos().forEach(System.out::println);

            System.out.println("\nPacientes cadastrados:");
            pacienteDAO.listarTodos().forEach(System.out::println);

            System.out.println("\nConsultas agendadas:");
            consultaDAO.listarTodos().forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("Erro de banco de dados: " + e.getMessage());
        }
    }
}