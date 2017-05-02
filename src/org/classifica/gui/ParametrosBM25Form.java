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
import javax.swing.JCheckBox;

public class ParametrosBM25Form extends JFrame {

	private static final long serialVersionUID = 1L;
	private JSpinner spinK;
	private JSpinner spinB;
	private JSpinner spinD;
	private VetorizadorTEC vetorizador;
	private JCheckBox chkNormalizar;
	private JCheckBox chkDicionario;

	public ParametrosBM25Form() {
		setBounds(100, 100, 525, 426);
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(10, 344, 387, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Integer k = (Integer) spinK.getValue(); 
						Integer b = (Integer) spinB.getValue(); 
						Integer d = (Integer) spinD.getValue(); 
						vetorizador.setK(k / 10.0);
						vetorizador.setB(b / 100.0);
						vetorizador.setDelta(d / 10.0);
						vetorizador.setNormalizado(chkNormalizar.isSelected());
						vetorizador.set_dicionarizado(chkDicionario.isSelected());
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
		
		chkNormalizar = new JCheckBox("Normalizar resultados");
		chkNormalizar.setSelected(true);
		chkNormalizar.setBounds(30, 220, 266, 23);
		getContentPane().add(chkNormalizar);
		
		JLabel lblNewLabel_5 = new JLabel("Divide todas as pontua\u00E7\u00F5es da lista pela maior pontua\u00E7\u00E3o");
		lblNewLabel_5.setBounds(34, 245, 439, 14);
		getContentPane().add(lblNewLabel_5);
		
		chkDicionario = new JCheckBox("Busca dicionarizada/expans\u00E3o da busca");
		chkDicionario.setBounds(33, 274, 263, 23);
		getContentPane().add(chkDicionario);
		
		JLabel lblNewLabel_6 = new JLabel("Expande os termos da busca com palavras similares no vocabul\u00E1rio e dicion\u00E1rio TEP2");
		lblNewLabel_6.setBounds(33, 304, 440, 14);
		getContentPane().add(lblNewLabel_6);
	}

	public void setVetorizador(VetorizadorTEC vetorizador) {
		this.vetorizador = vetorizador;
		Integer k = (int) (vetorizador.get_k()*10); 
		Integer b = (int) (vetorizador.get_b()*100); 
		Integer d = (int) (vetorizador.get_delta()*10); 
		spinK.setValue(k);
		spinB.setValue(b);
		spinD.setValue(d);
		chkNormalizar.setSelected(vetorizador.is_normalizado());
		chkDicionario.setSelected(vetorizador.is_dicionarizado());
	}
}