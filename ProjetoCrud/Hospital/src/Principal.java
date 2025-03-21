public class Principal {
    public static void main(String[] args) {
        Medicos medico1 = new Medicos("Dr. Pedro Lima", "11912345678", "pedro@email.com", 101, "Ortopedia", "CRM789456");
        Paciente paciente1 = new Paciente("Carlos Souza", "11987654321", "carlos@email.com", 2, "Masculino", "Rua B, 456");

        System.out.println(medico1);
        System.out.println(paciente1);
    }
}