package entities;

public class Consultas {
	private Pacientes paciente;
	private String data;
	private String hora;
	private String especialidade;
	
	public Consultas() {

	}
	
	public Consultas(Pacientes paciente, String data, String hora, String especialidade) {
		this.paciente = paciente;
		this.data = data;
		this.hora = hora;
		this.especialidade = especialidade;
	}

	public Pacientes getPaciente() {
		return paciente;
	}

	public void setPaciente(Pacientes paciente) {
		this.paciente = paciente;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}
	

}
