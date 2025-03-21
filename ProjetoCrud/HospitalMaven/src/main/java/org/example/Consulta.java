package org.example;

public class Consulta {
    private int id_consulta;
    private int id_paciente;
    private int id_medico;
    private String dataHora;
    private String motivo;

    public Consulta(int id_consulta, Paciente paciente, Medico medico, String dataHora, String motivo) {
        this.id_consulta = id_consulta;
        this.id_paciente = paciente.getId_paciente();
        this.id_medico = medico.getId_medico();
        this.dataHora = dataHora;
        this.motivo = motivo;
    }

    public int getId_consulta() {
        return id_consulta;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public int getId_medico() {
        return id_medico;
    }

    public String getDataHora() {
        return dataHora;
    }

    public String getMotivo() {
        return motivo;
    }

    @Override
    public String toString() {
        return "Consulta ID: " + id_consulta + " - Data: " + dataHora +
                " - Paciente ID: " + id_paciente + " - Médico ID: " + id_medico +
                " - Motivo: " + motivo;
    }
}
