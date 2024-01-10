package program;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.Consultas;
import entities.Pacientes;

public class Main {

    private static List<Pacientes> pacientes = new ArrayList<>();
    private static List<Consultas> consultas = new ArrayList<>();
    private static final String PACIENTES_FILE = "pacientes.txt";

    public static void main(String[] args) {
        carregarPacientes();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Cadastrar um paciente");
            System.out.println("2. Marcações de consultas");
            System.out.println("3. Cancelamento de consultas");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");

            int escolha = sc.nextInt();
            sc.nextLine();

            switch (escolha) {
                case 1:
                    cadastrarPaciente(sc);
                    break;
                case 2:
                    marcarConsulta(sc);
                    break;
                case 3:
                    cancelarConsulta(sc);
                    break;
                case 4:
                    salvarPacientes();
                    System.out.println("Sistema encerrado.");
                    System.exit(0);
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void carregarPacientes() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PACIENTES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Pacientes paciente = new Pacientes(parts[0], parts[1]);
                pacientes.add(paciente);
            }
        } catch (IOException e) {
            // Arquivo ainda não existe (primeira execução), ou ocorreu algum erro na leitura.
        }
    }

    private static void salvarPacientes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PACIENTES_FILE))) {
            for (Pacientes paciente : pacientes) {
                writer.write(paciente.getNome() + "," + paciente.getTelefone());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar pacientes: " + e.getMessage());
        }
    }

    private static void cadastrarPaciente(Scanner scanner) {
        System.out.print("Nome do paciente: ");
        String nome = scanner.nextLine();

        System.out.print("Telefone do paciente: ");
        String telefone = scanner.nextLine();

        if (isPacienteCadastrado(telefone)) {
            System.out.println("Paciente já cadastrado!");
            return;
        }

        Pacientes paciente = new Pacientes(nome, telefone);
        pacientes.add(paciente);

        System.out.println("Paciente cadastrado com sucesso!");
    }

    private static boolean isPacienteCadastrado(String telefone) {
        for (Pacientes paciente : pacientes) {
            if (paciente.getTelefone().equals(telefone)) {
                return true;
            }
        }
        return false;
    }

    private static void marcarConsulta(Scanner scanner) {
        if (pacientes.isEmpty()) {
            System.out.println("Não há pacientes cadastrados. Cadastre um paciente primeiro.");
            return;
        }

        System.out.println("Lista de Pacientes Cadastrados:");
        for (int i = 0; i < pacientes.size(); i++) {
            System.out.println((i + 1) + ". " + pacientes.get(i).getNome());
        }

        System.out.print("Escolha o número correspondente ao paciente: ");
        int numeroPaciente = scanner.nextInt();
        scanner.nextLine();

        if (numeroPaciente < 1 || numeroPaciente > pacientes.size()) {
            System.out.println("Número de paciente inválido. Tente novamente.");
            return;
        }

        Pacientes paciente = pacientes.get(numeroPaciente - 1);

        System.out.print("Dia da consulta (formato dd/MM/yyyy): ");
        String dia = scanner.nextLine();

        if (!isDataFutura(dia)) {
            System.out.println("A consulta só pode ser agendada para datas futuras. Tente novamente.");
            return;
        }

        System.out.print("Hora da consulta: ");
        String hora = scanner.nextLine();

        System.out.print("Especialidade da consulta: ");
        String especialidade = scanner.nextLine();

        if (!isConsultaDisponivel(dia, hora)) {
            System.out.println("Horário já ocupado. Escolha outro horário.");
            return;
        }

        Consultas consulta = new Consultas(paciente, dia, hora, especialidade);
        consultas.add(consulta);

        System.out.println("Consulta agendada com sucesso!");
    }

    private static boolean isDataFutura(String data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataConsulta = LocalDate.parse(data, formatter);

            LocalDate dataAtual = LocalDate.now();

            return !dataConsulta.isBefore(dataAtual);
        } catch (Exception e) {
            System.out.println("Formato de data inválido. Certifique-se de usar o formato dd/MM/yyyy.");
            return false;
        }
    }
    
    
    private static boolean isConsultaDisponivel(String dia, String hora) {
        for (Consultas consulta : consultas) {
            if (consulta.getData().equals(dia) && consulta.getHora().equals(hora)) {
                return false;
            }
        }
        return true;
    }

    private static void cancelarConsulta(Scanner scanner) {
        if (consultas.isEmpty()) {
            System.out.println("Não há consultas agendadas.");
            return;
        }

        System.out.println("Lista de Consultas Agendadas:");
        for (int i = 0; i < consultas.size(); i++) {
            Consultas consulta = consultas.get(i);
            System.out.println((i + 1) + ". " + consulta.getPaciente().getNome() + " - " + consulta.getData() + " " + consulta.getHora() + " - " + consulta.getEspecialidade());
        }

        System.out.print("Escolha o número correspondente à consulta que deseja cancelar: ");
        int numeroConsulta = scanner.nextInt();
        scanner.nextLine(); 

        if (numeroConsulta < 1 || numeroConsulta > consultas.size()) {
            System.out.println("Número de consulta inválido. Tente novamente.");
            return;
        }

        Consultas consultaCancelada = consultas.remove(numeroConsulta - 1);
        System.out.println("Consulta cancelada: " + consultaCancelada.getData() + " " + consultaCancelada.getHora() + " - " + consultaCancelada.getEspecialidade());
    }
}
