package org.classifica.plug;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

//import gov.rfb.contagil.commons.utils.GuiUtils;

import org.guga.contagil.acoes.Acao;
import org.guga.contagil.acoes.Menu;
import org.guga.contagil.acoes.OpenDocumentsManager;
import org.guga.contagil.acoes.mads.RefModeloAnaliticoDinamicoStrategy;
import org.guga.contagil.analitico.AnLeiaute;
import org.guga.contagil.plugins.ContAgilPlugInAbstract;
import org.guga.contagil.projeto.TipoProjeto;


public class PluginClassificadorFiscal extends ContAgilPlugInAbstract {

	private boolean initialized;
	
	/**
	 * Retorna o nome deste plugin.
	 */
	public String getNome() {
		return "PlugIn Classificador Fiscal";
	}

	@Override
	public void start() {
		
		
	}

	@Override
	public List<TipoProjeto> getTiposProjetos() {
		if (!initialized)
			return null;
		TipoProjeto novos_tipos[] = new TipoProjeto[] {
			new TipoProjeto("CLASSIFICA", 
					/*codigo*/TipoProjeto.getCodigoBase(getNome()) + 0,  // um código único para identificar este tipo de projeto
					CmdBotoesProjetoClassifica.class, 
					null, // Constante da enumeration para cômputo de estatísticas. Não utilizamos neste caso.  
					/*iconResourceName*/"org.guga.res.christmas_tree.png").comMenus("Classificador","Configurações","Ajuda"),
		};
		return Arrays.asList(novos_tipos);
	}

	@Override
	public List<JMenu> getMenus(OpenDocumentsManager man, Map<Acao, JMenuItem> acoes, List<RefModeloAnaliticoDinamicoStrategy> mads, Map<String, AnLeiaute> mapLeiautes) {
		List<JMenu> menus = new LinkedList<>();
		
		JMenu mClassifica = new JMenu("Classificador");
		menus.add(mClassifica);
		Menu.addAcao(mClassifica, new AcaoMostraClassificaForm(), man, acoes);
		return menus;
	}
}
