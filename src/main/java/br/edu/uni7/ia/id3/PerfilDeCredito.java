package br.edu.uni7.ia.id3;

public class PerfilDeCredito {

	public static final String[] PROPRIEDADES = { "historicoDeCredito", "divida", "garantia", "renda" };

	// Classe
	public String risco;
	// Propriedades
	public final String historicoDeCredito;
	public final String divida;
	public final String garantia;
	public final String renda;

	private String[] stringData;

	public PerfilDeCredito(String creditHistory, String debt, String garantee, String income) {
		this.historicoDeCredito = creditHistory;
		this.divida = debt;
		this.garantia = garantee;
		this.renda = income;
		this.stringData = new String[5];
		stringData[0] = "";
		stringData[1] = historicoDeCredito;
		stringData[2] = divida;
		stringData[3] = garantia;
		stringData[4] = renda;
	}

	public PerfilDeCredito(String[] stringData) {
		if (stringData == null || stringData.length != 5) {
			throw new IllegalArgumentException("Argumento nulo ou fora do formato");
		}
		this.stringData = stringData;
		this.risco = stringData[0];
		this.historicoDeCredito = stringData[1];
		this.divida = stringData[2];
		this.garantia = stringData[3];
		this.renda = stringData[4];
	}

	public String getPropertyValue(String property) {
		int propertyIndex = 0;
		for (String propriedadeListada : PROPRIEDADES) {
			if (propriedadeListada.equals(property)) {
				break;
			}
			// TODO tratar erro quando propriedade n�o est� no array
			propertyIndex++;
		}

		return stringData[propertyIndex + 1];
	}

	@Override
	public String toString() {
		return "[" + String.join(",", stringData) + "]";
	}
}
