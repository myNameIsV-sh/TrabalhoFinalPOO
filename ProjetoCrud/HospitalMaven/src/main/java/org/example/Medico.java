package org.example;

public class Medico extends Pessoa {
    private int id_medico;
    private String especialidade;
    private String crm;

    public Medico(String nome, String telefone, String email, int id_medico, String especialidade, String crm) {
        super(nome, telefone, email);
        this.id_medico = id_medico;
        this.especialidade = especialidade;
        this.crm = crm;
    }

    public int getId_medico() {
        return id_medico;
    }

    public void setId_medico(int id_medico) {
        this.id_medico = id_medico;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    @Override
    public String toString() {
        return "Médico [ID: " + id_medico + ", Especialidade: " + especialidade +
                ", CRM: " + crm + ", " + super.toString() + "]";
    }
}
