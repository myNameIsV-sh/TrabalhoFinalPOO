public class Paciente extends Pessoa {
    private int id_paciente;
    private String sexo;
    private String endereco;

    public Paciente(String nome, String telefone, String email, int id_paciente, String sexo, String endereco) {
        super(nome, telefone, email);
        this.id_paciente = id_paciente;
        this.sexo = sexo;
        this.endereco = endereco;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(int id_paciente) {
        this.id_paciente = id_paciente;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Paciente [ID: " + id_paciente + ", Sexo: " + sexo +
                ", Endereço: " + endereco + ", " + super.toString() + "]";
    }
}
