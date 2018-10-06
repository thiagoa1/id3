package br.edu.uni7.ia.id3;

public class PerfilDeCredito {

	public static final String[] PROPRIEDADES = { "historicoDeCredito", "divida", "garantia", "renda" };

	// Classe
	public final String risco;
	// Propriedades
	public final String historicoDeCredito;
	public final String divida;
	public final String garantia;
	public final String renda;
	
	private final String[] stringData;
	
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
			// TODO tratar erro quando propriedade não está no array 
			propertyIndex++;
		}
		
		return stringData[propertyIndex + 1];
	}
	
	@Override
	public String toString() {
		
		return "[" + String.join(",", stringData) + "]";
	}
}
