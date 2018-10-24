package br.edu.uni7.ia.id3;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import javax.swing.JComboBox;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;

public class CreditProfileFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static final String[] creditHistoryValues = { "ruim", "desconhecido", "bom" };
	public static final String[] debtValues = { "alta", "baixa" };
	public static final String[] garanteeValues = { "nenhuma", "adequada" };
	public static final String[] incomeValues = { "$0 a $15k", "$15 a $35k", "acima de $35k" };

	private JComboBox<String> creditHistoryComboBox;
	private JComboBox<String> debtComboBox;
	private JComboBox<String> garanteeComboBox;
	private JComboBox<String> incomeComboBox;
	private JButton analyzeButton;
	
	private CreditProfileFinder profileFinder;

	public CreditProfileFrame(CreditProfileFinder profileFinder) {
		this.profileFinder = profileFinder;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 3.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_creditHistoryLabel = new GridBagConstraints();
		gbc_creditHistoryLabel.gridx = 0;
		gbc_creditHistoryLabel.gridy = 0;
		getContentPane().add(new JLabel("Histórico de Crédito"), gbc_creditHistoryLabel);

		GridBagConstraints gbc_creditHistoryComboBox = new GridBagConstraints();
		gbc_creditHistoryComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_creditHistoryComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_creditHistoryComboBox.gridx = 1;
		gbc_creditHistoryComboBox.gridy = 0;
		getContentPane().add(getCreditHistoryComboBox(), gbc_creditHistoryComboBox);

		GridBagConstraints gbc_debtLabel = new GridBagConstraints();
		gbc_debtLabel.gridx = 0;
		gbc_debtLabel.gridy = 1;
		getContentPane().add(new JLabel("Dívida"), gbc_debtLabel);
		
		GridBagConstraints gbc_debtComboBox = new GridBagConstraints();
		gbc_debtComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_debtComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_debtComboBox.gridx = 1;
		gbc_debtComboBox.gridy = 1;
		getContentPane().add(getDebtComboBox(), gbc_debtComboBox);
		
		GridBagConstraints gbc_garanteeeLabel = new GridBagConstraints();
		gbc_garanteeeLabel.gridx = 0;
		gbc_garanteeeLabel.gridy = 2;
		getContentPane().add(new JLabel("Garantia"), gbc_garanteeeLabel);

		GridBagConstraints gbc_garanteeComboBox = new GridBagConstraints();
		gbc_garanteeComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_garanteeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_garanteeComboBox.gridx = 1;
		gbc_garanteeComboBox.gridy = 2;
		getContentPane().add(getGaranteeComboBox(), gbc_garanteeComboBox);
		
		GridBagConstraints gbc_incomeLabel = new GridBagConstraints();
		gbc_incomeLabel.gridx = 0;
		gbc_incomeLabel.gridy = 3;
		getContentPane().add(new JLabel("Renda"), gbc_incomeLabel);

		GridBagConstraints gbc_incomeComboBox = new GridBagConstraints();
		gbc_incomeComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_incomeComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_incomeComboBox.gridx = 1;
		gbc_incomeComboBox.gridy = 3;
		getContentPane().add(getIncomeComboBox(), gbc_incomeComboBox);

		GridBagConstraints gbc_analyzeButton = new GridBagConstraints();
		gbc_analyzeButton.gridx = 0;
		gbc_analyzeButton.gridy = 4;
		gbc_analyzeButton.gridwidth = 2;
		getContentPane().add(getAnalyzeButton(), gbc_analyzeButton);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(480, 250);
	}

	private JComboBox<String> getCreditHistoryComboBox() {
		if (creditHistoryComboBox == null) {
			creditHistoryComboBox = new JComboBox<>(creditHistoryValues);
		}
		return creditHistoryComboBox;
	}

	private JComboBox<String> getDebtComboBox() {
		if (debtComboBox == null) {
			debtComboBox = new JComboBox<>(debtValues);
		}
		return debtComboBox;
	}

	private JComboBox<String> getGaranteeComboBox() {
		if (garanteeComboBox == null) {
			garanteeComboBox = new JComboBox<>(garanteeValues);
		}
		return garanteeComboBox;
	}

	private JComboBox<String> getIncomeComboBox() {
		if (incomeComboBox == null) {
			incomeComboBox = new JComboBox<>(incomeValues);
		}
		return incomeComboBox;
	}

	private Component getAnalyzeButton() {
		if (analyzeButton == null) {
			analyzeButton = new JButton("Analisar");
			
			analyzeButton.addActionListener((event) -> {
				String creditHistoryValue = (String) getCreditHistoryComboBox().getSelectedItem();
				String debtValue = (String) getDebtComboBox().getSelectedItem();
				String garanteeValue = (String) getGaranteeComboBox().getSelectedItem();
				String incomeValue = (String) getIncomeComboBox().getSelectedItem();
				
//				System.out.println("creditHistoryValue: " + creditHistoryValue);
//				System.out.println("debtValue: " + debtValue);
//				System.out.println("garanteeValue: " + garanteeValue);
//				System.out.println("incomeValue: " + incomeValue);
				
				String risk = profileFinder.findCreditProfile(new PerfilDeCredito(creditHistoryValue, debtValue, garanteeValue, incomeValue));
				
		        JOptionPane.showMessageDialog(CreditProfileFrame.this, "Este perfil é de risco " + risk, "Resultado", JOptionPane.INFORMATION_MESSAGE);
			}); 
		}
		return analyzeButton;
	}
}
