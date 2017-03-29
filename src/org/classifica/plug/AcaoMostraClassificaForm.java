package org.classifica.plug;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import org.classifica.gui.ClassificaForm;
import org.guga.contagil.acoes.AcaoAbstract;
import org.guga.contagil.acoes.OpenDocumentsManager;

import gov.rfb.contagil.commons.utils.GuiUtils;

public class AcaoMostraClassificaForm  extends AcaoAbstract {

	public static final String cmd = "Mostrar Janela de Ajuda em Classificação";

	public static final String btn = "Janela de Ajuda em Classificação";

	/**
	 * Construtor sem parâmetros.
	 */
	public AcaoMostraClassificaForm() {
		super(cmd, btn);
	}

	/**
	 * Construtor com indicação de um ID interno.
	 */
	public AcaoMostraClassificaForm(String nomeInterno) {
		super(cmd, btn, nomeInterno);
	}

	/**
	 * Esta é a função que é executada quando o usuário deseja executar esta ação.
	 */
	public void run(Window wnd, OpenDocumentsManager man, ActionEvent event) {
		
		if (!GuiUtils.showWindow(ClassificaForm.class)) {
			ClassificaForm frame = new ClassificaForm();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		
	}
}
