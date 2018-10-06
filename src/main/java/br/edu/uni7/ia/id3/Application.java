package br.edu.uni7.ia.id3;

import java.awt.BorderLayout;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.opencsv.CSVReader;

import br.edu.uni7.util.Branch;
import br.edu.uni7.util.Node;

public class Application {

	private String dataCsv = "dados.csv";

	public Application() {
		List<PerfilDeCredito> data = loadData();
		List<String> properties = new ArrayList<>();

		for (String property : PerfilDeCredito.PROPRIEDADES) {
			properties.add(property);
		}

//		System.out.println(getClassFrequency(data));
//		System.out.println(propertiesInformation(properties, data));
//		System.out.println(getPropertyFrequency(data, "renda"));
//		System.out.println(propertyGainExpectation(data, "renda"));
//		System.out.println(selectProperty2(properties, data));

		Node<String> rootNode = induceTree(data, properties);

		mxGraph graph = getGraphfromNode(rootNode);
		graph.getModel().endUpdate();

		JFrame frame = new JFrame("Arvere");
		mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
		layout.execute(graph.getDefaultParent());
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(graphComponent, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1024, 600);
		frame.setVisible(true);
	}

	private mxGraph getGraphfromNode(Node<String> node) {
		mxGraph g = new mxGraph();

		Object parent = g.getDefaultParent();
		mxICell pCell = (mxICell) g.insertVertex(parent, null, node.getData(), 0, 0, 110, 30);

		return getGraphfromNode(g, node, pCell);
	}

	private mxGraph getGraphfromNode(mxGraph g, Node<String> node, mxICell pCell) {
		for (Branch<String> branch : node.getBranchs()) {
			mxICell cCell = (mxICell) g.insertVertex(pCell.getParent(), null, branch.getNode().getData(), 0, 0, 110, 30);
			g.insertEdge(pCell.getParent(), null, branch.getName(), pCell, cCell);

			if (branch.getNode().getBranchs() != null && !branch.getNode().getBranchs().isEmpty()) {
				getGraphfromNode(g, branch.getNode(), cCell);
			}
		}

		return g;
	}

	public List<PerfilDeCredito> loadData() {
		List<PerfilDeCredito> data = new ArrayList<>();
		try (CSVReader reader = new CSVReader(new FileReader(dataCsv))) {
			String[] line;
			while ((line = reader.readNext()) != null) {
				data.add(new PerfilDeCredito(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private Node<String> induceTree(List<PerfilDeCredito> data, List<String> properties) {
		Node<String> currentNode;
		if (allSameClass(data)) {
			currentNode = new Node<String>(data.get(0).risco);
			System.out.println("risco " + data.get(0).risco);
		} else if (properties.isEmpty()) {
			Set<String> classes = getClassesFromData(data);
			String classesString = String.join(" | ", classes);
			currentNode = new Node<String>(classesString);
		} else {
			String selectedProperty = selectProperty2(properties, data);
			currentNode = new Node<String>(selectedProperty);
//			properties.remove(selectedProperty);

			List<String> subProperties = getListRemovingProperty(properties, selectedProperty);

			System.out.println("selectedProperty " + selectedProperty);

			Set<String> uniqueValues = uniqueValuesFromProperty(data, selectedProperty);
			for (String uniqueValue : uniqueValues) {
				Branch<String> valueBranch = new Branch<>(uniqueValue);
				currentNode.addBranch(valueBranch);

				System.out.println("uniqueValue " + uniqueValue);

				List<PerfilDeCredito> dataPartitionByValue = dataPartitionByValue(data, selectedProperty, uniqueValue);
				valueBranch.setNode(induceTree(dataPartitionByValue, subProperties));
			}
		}
		return currentNode;
	}

	private List<String> getListRemovingProperty(List<String> properties, String property) {
		List<String> newProperties = new ArrayList<>(properties);
		newProperties.remove(property);

		return newProperties;
	}

	private List<PerfilDeCredito> dataPartitionByValue(List<PerfilDeCredito> data, String property, String value) {
		List<PerfilDeCredito> partitionList = new ArrayList<>();

		for (PerfilDeCredito perfilDeCredito : data) {
			if (perfilDeCredito.getPropertyValue(property).equals(value)) {
				partitionList.add(perfilDeCredito);
			}
		}

		return partitionList;
	}

	private Set<String> uniqueValuesFromProperty(List<PerfilDeCredito> data, String property) {
		Set<String> values = new HashSet<>();
		for (PerfilDeCredito perfil : data) {
			values.add(perfil.getPropertyValue(property));
		}
		return values;
	}

	private Set<String> getClassesFromData(List<PerfilDeCredito> data) {
		Set<String> classes = new HashSet<>();
		for (PerfilDeCredito perfil : data) {
			if (!classes.contains(perfil.risco)) {
				classes.add(perfil.risco);
			}
		}

		return classes;
	}

	private static final String[] SEQUENCIA_DE_PROPRIEDADES = { "historicoDeCredito", "garantia", "divida", "renda" };

	private String selectProperty(List<String> propriedades) {
		if (propriedades == null || propriedades.isEmpty()) {
			throw new IllegalArgumentException("O argumento não pode ser vazio nem nulo.");
		}

		for (String propriedadePreferencial : SEQUENCIA_DE_PROPRIEDADES) {
			if (propriedades.contains(propriedadePreferencial)) {
				return propriedadePreferencial;
			}
		}
		System.err.println("Propriedade não listada");
		return propriedades.get(0);
	}

	private String selectProperty2(List<String> properties, List<PerfilDeCredito> data) {
		String selectedProperty = null;
		double selectedPropertyGain = Double.MIN_VALUE;
		
		double propertiesInformation = propertiesInformation(properties, data);

		for (String property : properties) {
			double propertyGain =  propertyGain(data, property, propertiesInformation);
//			System.out.println("property: " + property + " gain: "+ propertyGain);
			if (propertyGain > selectedPropertyGain) {
				selectedProperty = property;
				selectedPropertyGain = propertyGain;
			}
		}

		return selectedProperty;
	}

	// G[P]
	private double propertyGain(List<PerfilDeCredito> data, String property, double propertiesInformation) {
		return propertiesInformation - propertyGainExpectation(data, property);
	}

	// E[P]
	private double propertyGainExpectation(List<PerfilDeCredito> data, String property) {
		double e = 0.0;

		HashMap<String, Double> propertyFrequency = getPropertyFrequency(data, property);

		for (String propertyValue : propertyFrequency.keySet()) {
			double propertyFrenquency = propertyFrequency.get(propertyValue); // P(mi)
			e += propertyFrenquency * propertyValueInformation(data, property, propertyValue);
		}

		return e;
	}

	// I[CE]
	private double propertiesInformation(List<String> properties, List<PerfilDeCredito> data) {
		double i = 0.0;

		HashMap<String, Double> classFrequency = getClassFrequency(data);

		for (String clazz : classFrequency.keySet()) {
			double classFrenquency = classFrequency.get(clazz); // P(mi)
			i += -1.0 * classFrenquency * log(classFrenquency, 2);
		}

		return i;
	}

	// I[Ci]
	private double propertyValueInformation(List<PerfilDeCredito> data, String property, String propertyValue) {
		double i = 0.0;

		List<PerfilDeCredito> dataByPropertyValue = getDataByPropertyValue(data, property, propertyValue);
		HashMap<String, Double> classFrequencyByPropertyValue = getClassFrequency(dataByPropertyValue);

		for (String clazz : classFrequencyByPropertyValue.keySet()) {
			double classFrenquency = classFrequencyByPropertyValue.get(clazz); // P(mi)
			i += -1.0 * classFrenquency * log(classFrenquency, 2);
		}

		return i;
	}

	// P(mi)
	private List<PerfilDeCredito> getDataByPropertyValue(List<PerfilDeCredito> data, String property,
			String propertyValue) {
		List<PerfilDeCredito> dataByPropertyValue = new ArrayList<>();

		for (PerfilDeCredito pc : data) {
			if (pc.getPropertyValue(property).equals(propertyValue)) {
				dataByPropertyValue.add(pc);
			}
		}

		return dataByPropertyValue;
	}

	public static double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}

	private HashMap<String, Double> getPropertyFrequency(List<PerfilDeCredito> data, String property) {
		HashMap<String, Double> propertyFrequencyMap = new HashMap<>();

		for (PerfilDeCredito pc : data) {
			String propertyValue = pc.getPropertyValue(property);
			if (propertyFrequencyMap.containsKey(propertyValue)) {
				propertyFrequencyMap.put(propertyValue, propertyFrequencyMap.get(propertyValue) + 1);
			} else {
				propertyFrequencyMap.put(propertyValue, 1.0);
			}
		}

		for (String propertyValue : propertyFrequencyMap.keySet()) {
			propertyFrequencyMap.put(propertyValue, propertyFrequencyMap.get(propertyValue) / data.size());
		}

		return propertyFrequencyMap;
	}

	private HashMap<String, Double> getClassFrequency(List<PerfilDeCredito> data) {
		HashMap<String, Double> classFrequencyMap = new HashMap<>();

		for (PerfilDeCredito pc : data) {
			if (classFrequencyMap.containsKey(pc.risco)) {
				classFrequencyMap.put(pc.risco, classFrequencyMap.get(pc.risco) + 1);
			} else {
				classFrequencyMap.put(pc.risco, 1.0);
			}
		}

		for (String clazz : classFrequencyMap.keySet()) {
			classFrequencyMap.put(clazz, classFrequencyMap.get(clazz) / data.size());
		}

		return classFrequencyMap;
	}

	private boolean allSameClass(List<PerfilDeCredito> data) {
		boolean sameClass = true;
		if (data != null && !data.isEmpty()) {
			String classe = data.get(0).risco;

			for (PerfilDeCredito perfil : data) {
				if (!perfil.risco.equals(classe)) {
					sameClass = false;
					break;
				}
			}
		}

		return sameClass;
	}

	public static void main(String[] args) {
		new Application();
	}

}
