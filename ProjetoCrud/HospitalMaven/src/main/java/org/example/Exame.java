package org.example;

public class Exame {
    private int id_exame;
    private int id_paciente;
    private int id_medico;
    private String tipo_exame;
    private String resultado;
    private String data_exame;
    private String observacoes;

    public Exame(int id_exame, Paciente paciente, Medico medico, String tipo_exame, String resultado, String data_exame, String observacoes) {
        this.id_exame = id_exame;
        this.id_paciente = paciente.getId_paciente();
        this.id_medico = medico.getId_medico();
        this.tipo_exame = tipo_exame;
        this.resultado = resultado;
        this.data_exame = data_exame;
        this.observacoes = observacoes;
    }

    public Exame(Paciente paciente, Medico medico, String tipo_exame, String resultado, String data_exame, String observacoes) {
        this.id_paciente = paciente.getId_paciente();
        this.id_medico = medico.getId_medico();
        this.tipo_exame = tipo_exame;
        this.resultado = resultado;
        this.data_exame = data_exame;
        this.observacoes = observacoes;
    }

    public int getId_exame() {
        return id_exame;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public int getId_medico() {
        return id_medico;
    }

    public String getTipo_exame() {
        return tipo_exame;
    }

    public String getResultado() {
        return resultado;
    }

    public String getData_exame() {
        return data_exame;
    }

    public String getObservacoes() {
        return observacoes;
    }

    @Override
    public String toString() {
        return "Exame ID: " + id_exame + " - Paciente ID: " + id_paciente +
                " - Médico ID: " + id_medico + " - Tipo: " + tipo_exame +
                " - Resultado: " + resultado + " - Data: " + data_exame +
                " - Observações: " + observacoes;
    }
}
