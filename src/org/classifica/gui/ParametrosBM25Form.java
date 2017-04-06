package org.classifica.gui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.classifica.util.VetorizadorTEC;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;

public class ParametrosBM25Form extends JFrame {

	private static final long serialVersionUID = 1L;
	private JSpinner spinK;
	private JSpinner spinB;
	private JSpinner spinD;
	private VetorizadorTEC vetorizador;

	public ParametrosBM25Form() {
		setBounds(100, 100, 480, 311);
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 229, 387, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Integer k = (Integer) spinK.getValue(); 
						Integer b = (Integer) spinB.getValue(); 
						Integer d = (Integer) spinD.getValue(); 
						vetorizador._k = k / 10.0;
						vetorizador._b = b / 100.0;
						vetorizador._delta = d / 10.0;
						dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		
		JLabel lblNewLabel = new JLabel("k");
		lblNewLabel.setBounds(33, 24, 94, 14);
		getContentPane().add(lblNewLabel);
		
		spinK = new JSpinner();
		spinK.setModel(new SpinnerNumberModel(12, 10, 30, 1));
		spinK.setBounds(229, 21, 46, 20);
		getContentPane().add(spinK);
		
		spinB = new JSpinner();
		spinB.setModel(new SpinnerNumberModel(75, 0, 100, 5));
		spinB.setBounds(229, 82, 46, 20);
		getContentPane().add(spinB);
		
		spinD = new JSpinner();
		spinD.setModel(new SpinnerNumberModel(10, 0, 30, 1));
		spinD.setBounds(229, 146, 46, 20);
		getContentPane().add(spinD);
		
		JLabel lblNewLabel_1 = new JLabel("b");
		lblNewLabel_1.setBounds(33, 85, 46, 14);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("delta");
		lblNewLabel_2.setBounds(33, 149, 46, 14);
		getContentPane().add(lblNewLabel_2);
		
		JLabel lblKTeto = new JLabel("<html>k = teto do peso da frequ\u00EAncia da palavra. Mais pontos a <br>\r\nrepeti\u00E7\u00E3o de uma palavra na mesma linha obter\u00E1 (Padr\u00E3o: 1.2 a 3)</html>");
		lblKTeto.setBounds(33, 49, 421, 25);
		getContentPane().add(lblKTeto);
		
		JLabel lblbPercentual = new JLabel("<html>b = percentual multiplicador da frequ\u00EAncia inversa de documento<br>\r\npalavras \"comuns\" s\u00E3o penalizadas (Padr\u00E3o: 0.5 a 1.0)</html>");
		lblbPercentual.setBounds(33, 110, 399, 25);
		getContentPane().add(lblbPercentual);
		
		JLabel lbldeltaValor = new JLabel("<html>delta = valor somado no final. Evita penaliza\u00E7\u00E3o excessiva de linhas<br>\r\nda TEC que possuam poucas palavras (Padr\u00E3o: 1.0)</html>");
		lbldeltaValor.setBounds(33, 177, 399, 25);
		getContentPane().add(lbldeltaValor);
		
		JLabel lblNewLabel_3 = new JLabel("/10");
		lblNewLabel_3.setBounds(285, 149, 46, 14);
		getContentPane().add(lblNewLabel_3);
		
		JLabel label = new JLabel("/10");
		label.setBounds(285, 24, 46, 14);
		getContentPane().add(label);
		
		JLabel lblNewLabel_4 = new JLabel("%");
		lblNewLabel_4.setBounds(285, 85, 46, 14);
		getContentPane().add(lblNewLabel_4);
	}

	public void setVetorizador(VetorizadorTEC vetorizador) {
		this.vetorizador = vetorizador;
		Integer k = (int) (vetorizador._k*10); 
		Integer b = (int) (vetorizador._b*100); 
		Integer d = (int) (vetorizador._delta*10); 
		spinK.setValue(k);
		spinB.setValue(b);
		spinD.setValue(d);
	}
}
