package org.classifica.gui;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.Document;

import org.classifica.entidades.Capitulo;
import org.classifica.entidades.Pais;
import org.classifica.util.VetorizadorTEC;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingWorker;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import javax.swing.JComboBox;



public class ClassificaForm extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private VetorizadorTEC vetorizador;


	private JTextArea txtPesquisa;

	private JButton btnPesquisaPalavrasNCM;

	private JButton btnImportarDeLaudo;

	private JTable tblSugestoes;

	private JScrollPane scrollPane_1;

	private JPanel panelA;
	private JScrollPane scrollPane;

	private JCheckBox chkPonderado;

	private JTabbedPane tabbedPane;

	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_2_1;
	private JTextArea txtTEC;
	private JSplitPane splitPane;
	private JTextField txtPesquisaTextoNCM;

	private JSplitPane splitPane2;

	private JTextField txtPesquisaCodigoNCM;

	private JTable tblNCM;

	private TableRowSorter<TableModel> sorter;

	private JButton btnPesquisaTextoNCM;

	private JButton btnPesquisaCodigoNCM;

	private JLabel lblAguarde;

	private JSplitPane splitPane3;
	private JLabel lblNewLabel_1;
	private JTextArea txtExplicacao;

	private JScrollPane scrollPane3;

	private JCheckBox rdbTexto;

	private JCheckBox rdbBigrama;

	private JCheckBox rdbStem;

	private HelpForm helpForm;

	private JSplitPane splitPane4;
	private JScrollPane scrollPane_CapNCM;
	private JComboBox<Pais> cbbPaisOrigemCapNCM;
	private JCheckBox chkUsarCapNCM;
	private JTable tblCapNCM;
	private ModelCapNCM model;

	private JSplitPane splitPane5;

	private JComboBox<Capitulo> cbbCapNCM;

	private JTextArea txtNotas;

	private JScrollPane scrollPane_1p;

	private JTable tblSugestoesp;




	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClassificaForm frame = new ClassificaForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClassificaForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1029, 654);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 118, 797,386);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(tabbedPane);

		panelA = new JPanel();
		panelA.setBounds(10, 0, 993, 118);
		contentPane.add(panelA);
		panelA.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 33, 961, 59);
		panelA.add(scrollPane);

		txtPesquisa = new JTextArea();
		scrollPane.setViewportView(txtPesquisa);
		txtPesquisa.setWrapStyleWord(true);
		txtPesquisa.setLineWrap(true);
		txtPesquisa.setColumns(50);

		JLabel lblNewLabel = new JLabel("Digite as palavras a pontuar:");
		scrollPane.setColumnHeaderView(lblNewLabel);

		btnPesquisaPalavrasNCM = new JButton("Pesquisar pontua\u00E7\u00E3o");
		btnPesquisaPalavrasNCM.setEnabled(false);
		btnPesquisaPalavrasNCM.setBounds(420, 5, 307, 23);
		panelA.add(btnPesquisaPalavrasNCM);

		btnImportarDeLaudo = new JButton("Importar palavras de arquivo PDF...");
		btnImportarDeLaudo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				importaPalavrasdePDF();
			}
		});
		btnImportarDeLaudo.setBounds(737, 5, 246, 23);
		panelA.add(btnImportarDeLaudo);

		chkPonderado = new JCheckBox("Utilizar pesos ponderados TF-IDF");
		chkPonderado.setSelected(true);
		chkPonderado.setBounds(23, 95, 246, 23);
		panelA.add(chkPonderado);

		lblAguarde = new JLabel("Aguarde!!! Carregando dados...");
		lblAguarde.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAguarde.setBounds(23, 3, 401, 23);
		panelA.add(lblAguarde);

		rdbStem = new JCheckBox("Stemiza\u00E7\u00E3o");
		rdbStem.setSelected(true);
		rdbStem.setBounds(442, 95, 109, 23);
		panelA.add(rdbStem);

		rdbBigrama = new JCheckBox(" Bigramas");
		rdbBigrama.setBounds(552, 95, 100, 23);
		panelA.add(rdbBigrama);

		rdbTexto = new JCheckBox("Texto puro");
		rdbTexto.setSelected(true);
		rdbTexto.setBounds(331, 95, 109, 23);
		panelA.add(rdbTexto);

		JButton btnParametros = new JButton("Ajustar par\u00E2metros");
		btnParametros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ajustaParametros();
			}

		});
		btnParametros.setBounds(823, 95, 160, 23);
		panelA.add(btnParametros);

		/*JButton btnNewButton = new JButton("arff");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vetorizador.exportaTECResumoARFF(System.getProperty("user.home")+"/tecresumo.arff");
			}
		});
		btnNewButton.setBounds(275, 95, 51, 23);
		panelA.add(btnNewButton);*/
		btnPesquisaPalavrasNCM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vetorizador.clearTiposAtivos();
				if (rdbTexto.isSelected()){
					vetorizador.addTipoAtivo(VetorizadorTEC.Tipo.TEXTO);
				}
				if (rdbStem.isSelected()){
					vetorizador.addTipoAtivo(VetorizadorTEC.Tipo.STEM);
				}
				if (rdbBigrama.isSelected()){
					vetorizador.addTipoAtivo(VetorizadorTEC.Tipo.BIGRAMA);
				}
				txtExplicacao.setText(vetorizador.pontuaTexto(txtPesquisa.getText(), chkPonderado.isSelected()));
				Integer index = vetorizador.getTECsPontos().size();
				if (index>30){ // Se retornou mais de 30 linhas pontuadas, define um limite, um número para corte de exibição
					double valorThreshold = vetorizador.getTECsPontos().get(0)[0] / 2.0;
					int limite = 0;
					for (int r=1;r<index;r++){
						double valorLinha = vetorizador.getTECsPontos().get(r)[0];
						if (valorLinha < valorThreshold){
							limite = r;
							break;
						}
					}
					if (limite != 0){
						index = limite;
					}
				}
				ArrayList<String[]> linhas = new ArrayList<String[]>();
				Pais pais = null;
				if (chkUsarCapNCM.isSelected()){
					pais = (Pais) cbbPaisOrigemCapNCM.getSelectedItem();
				}
				//DataAccess dataaccess = DataAccess.getDB();
				for (int r=0;r<index;r++){
					double fatorCapNCM = (float) 0.0000000001;
					double valorLinha = vetorizador.getTECsPontos().get(r)[0];
					String descricaolinha = vetorizador.getListaTECResumo().get(vetorizador.getTECsPontos().get(r)[1].intValue());
					if (pais!=null){
						String capncm = descricaolinha.substring(0, 2);
						fatorCapNCM = fatorCapNCM + model.getPorcentagemCapNCM(capncm); // Modificado para pegar do modelo em vez do Banco por desempenho 
						//dataaccess.getTotalCapNCM(pais, capncm);
						//Aplicar "Smooth function" e números arbitrariamente escolhidos na porcentagem do CapNCM
						//Caso seja 1*10e-10, retorna próximo de 1, caso seja 10% retorna 1,5, caso seja 50$ retorna 2,5 (números aproximados) 
						fatorCapNCM = 1.0 - (1.0 / Math.log(fatorCapNCM));
						valorLinha = valorLinha * fatorCapNCM;
					}
					DecimalFormat formatter = new DecimalFormat("000.0000");
					String[] linha = {formatter.format(valorLinha), descricaolinha}; 
					linhas.add(linha);
				}

				ModelSugestoes model = (ModelSugestoes) tblSugestoes.getModel();
				model.limpar();
				model.addLista(linhas);

				ArrayList<String[]> linhasp = new ArrayList<String[]>();
				for (int r=0;r<vetorizador.getTECsPontosPosicao().size();r++){
					double valorLinha = vetorizador.getTECsPontosPosicao().get(r)[0];
					String descricaolinha = vetorizador.getListaTECResumoPosicoes().get(vetorizador.getTECsPontosPosicao().get(r)[1].intValue());
					DecimalFormat formatter = new DecimalFormat("000.00");
					String[] linha = {formatter.format(valorLinha), descricaolinha}; 
					linhasp.add(linha);
				}
				ModelSugestoes modelp = (ModelSugestoes) tblSugestoesp.getModel();
				modelp.limpar();
				modelp.addLista(linhasp);

				TableRowSorter<ModelSugestoes> sorter = new TableRowSorter<ModelSugestoes>(model);
				tblSugestoes.setRowSorter(sorter);
				//tblSugestoesp.setRowSorter(sorter);
				List<RowSorter.SortKey> sortKeys = new ArrayList<>();
				sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
				sorter.setSortKeys(sortKeys);
			}
		});

		//Tabela de ranqueamento  
		scrollPane_1 = new JScrollPane();
		tabbedPane.addTab("Sugestões", null, scrollPane_1, null);

		tblSugestoes = new JTable(){
			private static final long serialVersionUID = 1L;
			public String getToolTipText(MouseEvent e) {
				return toolTipExplicacao(e, this);
			}
		};
		tblSugestoes.setModel(new ModelSugestoes());
		tblSugestoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblSugestoes.getColumnModel().getColumn(0).setPreferredWidth(20);
		tblSugestoes.getColumnModel().getColumn(0).setMinWidth(20);
		tblSugestoes.getColumnModel().getColumn(1).setPreferredWidth(500);
		tblSugestoes.getColumnModel().getColumn(1).setMinWidth(400);
		tblSugestoes.setFillsViewportHeight(true);
		scrollPane_1.setViewportView(tblSugestoes);

		//Tabela de ranqueamento - posições
		scrollPane_1p = new JScrollPane();
		tabbedPane.addTab("Sugestões Posição", null, scrollPane_1p, null);

		tblSugestoesp = new JTable(){
			private static final long serialVersionUID = 1L;
			public String getToolTipText(MouseEvent e) {
				return toolTipExplicacao(e, this);
			}
		};
		tblSugestoesp.setModel(new ModelSugestoes());
		tblSugestoesp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblSugestoesp.getColumnModel().getColumn(0).setPreferredWidth(20);
		tblSugestoesp.getColumnModel().getColumn(0).setMinWidth(20);
		tblSugestoesp.getColumnModel().getColumn(1).setPreferredWidth(500);
		tblSugestoesp.getColumnModel().getColumn(1).setMinWidth(400);
		tblSugestoesp.setFillsViewportHeight(true);
		scrollPane_1p.setViewportView(tblSugestoesp);


		//######### TEC / NCM Completa
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabbedPane.addTab("NCM Completa", null, splitPane, null);

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		splitPane.setLeftComponent(panel);

		txtPesquisaTextoNCM = new JTextField();
		txtPesquisaTextoNCM.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					pesquisaTextoNCM();
				}
			}
		});
		panel.add(txtPesquisaTextoNCM);
		txtPesquisaTextoNCM.setColumns(60);

		btnPesquisaTextoNCM = new JButton("Pesquisa Texto");
		panel.add(btnPesquisaTextoNCM);
		btnPesquisaTextoNCM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pesquisaTextoNCM();
			}
		});

		scrollPane_2 = new JScrollPane();
		txtTEC = new JTextArea();
		txtTEC.setEditable(false);
		txtTEC.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				txtPesquisaTextoNCM.requestFocusInWindow();	
			}
		});
		txtTEC.setLineWrap(true);
		txtTEC.setWrapStyleWord(true);
		scrollPane_2.setViewportView(txtTEC);
		splitPane.setRightComponent(scrollPane_2);



		//######### TEC Hierarquizada
		splitPane2 = new JSplitPane();
		splitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabbedPane.addTab("Pesquisar NCM", null, splitPane2, null);

		JPanel panelT = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panelT.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		splitPane2.setLeftComponent(panelT);

		txtPesquisaCodigoNCM = new JTextField();
		txtPesquisaCodigoNCM.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					pesquisaCodigoNCM();
				}
			}
		});
		panelT.add(txtPesquisaCodigoNCM);
		txtPesquisaCodigoNCM.setColumns(10);

		btnPesquisaCodigoNCM = new JButton("Pesquisa C\u00F3digo");
		btnPesquisaCodigoNCM.setEnabled(false);
		panelT.add(btnPesquisaCodigoNCM);
		btnPesquisaCodigoNCM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pesquisaCodigoNCM();
			}
		});

		scrollPane_2_1 = new JScrollPane();
		splitPane2.setRightComponent(scrollPane_2_1);

		tblNCM = new JTable();
		tblNCM.setFillsViewportHeight(true);
		scrollPane_2_1.setViewportView(tblNCM);
		tblNCM.setModel(new ModelNCM());
		tblNCM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblNCM.getColumnModel().getColumn(0).setPreferredWidth(20);
		tblNCM.getColumnModel().getColumn(0).setMinWidth(20);
		tblNCM.getColumnModel().getColumn(1).setPreferredWidth(500);
		tblNCM.getColumnModel().getColumn(1).setMinWidth(400);
		sorter = new TableRowSorter<TableModel>(tblNCM.getModel());
		tblNCM.setRowSorter(sorter);

		//######### Capítulos NCM
		splitPane5 = new JSplitPane();
		splitPane5.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabbedPane.addTab("Capítulos e notas", null, splitPane5, null);

		JPanel panelCapNotas = new JPanel();
		FlowLayout flowLayout_CapNotas = (FlowLayout) panelCapNotas.getLayout();
		flowLayout_CapNotas.setAlignment(FlowLayout.LEFT);
		splitPane5.setLeftComponent(panelCapNotas);

		cbbCapNCM = new JComboBox<Capitulo>();
		DefaultComboBoxModel<Capitulo> modeln = new DefaultComboBoxModel<Capitulo>();
		cbbCapNCM.setModel(modeln);
		panelCapNotas.add(cbbCapNCM);

		scrollPane_CapNCM = new JScrollPane();
		splitPane5.setRightComponent(scrollPane_CapNCM);

		txtNotas = new JTextArea();
		txtNotas.setLineWrap(true);
		txtNotas.setWrapStyleWord(true);
		scrollPane_CapNCM.setViewportView(txtNotas);


		//######### Estatística das importações por NCM - % peso líquido
		splitPane4 = new JSplitPane();
		splitPane4.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabbedPane.addTab("Estatística capítulo NCM", null, splitPane4, null);

		JPanel panelCap = new JPanel();
		FlowLayout flowLayout_Cap = (FlowLayout) panelCap.getLayout();
		flowLayout_Cap.setAlignment(FlowLayout.LEFT);
		splitPane4.setLeftComponent(panelCap);

		cbbPaisOrigemCapNCM = new JComboBox<Pais>();
		DefaultComboBoxModel<Pais> modelr = new DefaultComboBoxModel<Pais>();
		cbbPaisOrigemCapNCM.setModel(modelr);
		panelCap.add(cbbPaisOrigemCapNCM);

		chkUsarCapNCM = new JCheckBox("Usar estat\u00EDstica do Pa\u00EDs de Origem selecionado");
		panelCap.add(chkUsarCapNCM);

		scrollPane_CapNCM = new JScrollPane();
		splitPane4.setRightComponent(scrollPane_CapNCM);

		tblCapNCM = new JTable();
		tblCapNCM.setFillsViewportHeight(true);
		scrollPane_CapNCM.setViewportView(tblCapNCM);
		model = new ModelCapNCM();
		tblCapNCM.setModel(model);


		//########### Explicação da pontuação
		splitPane3 = new JSplitPane();
		splitPane3.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabbedPane.addTab("Explicação - ver pontuação palavras", null, splitPane3, null);

		lblNewLabel_1 = new JLabel("Pontua\u00E7\u00E3o das palavras pesquisadas");
		splitPane3.setLeftComponent(lblNewLabel_1);


		scrollPane3= new JScrollPane();
		splitPane3.setRightComponent(scrollPane3);


		txtExplicacao = new JTextArea();
		txtExplicacao.setEditable(false);
		txtExplicacao.setLineWrap(true);
		txtExplicacao.setWrapStyleWord(true);
		scrollPane3.setViewportView(txtExplicacao);


		contentPane.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e) {
				panelA.setSize(contentPane.getWidth()-10, 118);
				tabbedPane.setSize(contentPane.getWidth()-10, contentPane.getHeight()-120);
				scrollPane_1.setSize(contentPane.getWidth()-12, contentPane.getHeight()-140);
			}
		});
		MySelectionListener listener = new MySelectionListener();
		tblSugestoes.getSelectionModel().addListSelectionListener(listener);

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());


		/////////////////////////////////////
		///InicializaListasTEC....
		//////////////////////////////////////////////////////
		carregaTECCompleta();

		ClassificaSwingWorker sw = new ClassificaSwingWorker();
		sw.execute();
	}





	public void importaPalavrasdePDF() {
		FileFilter filter = new FileNameExtensionFilter("PDF", "pdf");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.resetChoosableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File PDFSource = fileChooser.getSelectedFile();
			String fileInput = PDFSource.getAbsolutePath();
			FormPDFView frame = new FormPDFView();
			try {
				frame.setPDF(fileInput);
				frame.setVetorizador(vetorizador);
				frame.setTextoRetorno(txtPesquisa);
				frame.setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
		} else {
			JOptionPane.showMessageDialog(this, "Arquivo não encontrado",  "Arquivo não encontrado",
					JOptionPane.OK_OPTION);
		}

	}


	public void pesquisaTextoNCM() {
		int len = txtPesquisaTextoNCM.getText().length();
		txtTEC.requestFocusInWindow();
		if ((txtTEC.getCaretPosition() + len) >= txtTEC.getText().length()){
			txtTEC.setCaretPosition(0);
			txtTEC.moveCaretPosition(0);
		}
		String texto = txtTEC.getText().substring(txtTEC.getCaretPosition());
		Integer pos = texto.indexOf(txtPesquisaTextoNCM.getText());
		if (pos>0){
			int caret = txtTEC.getCaretPosition();
			txtTEC.setCaretPosition(caret+pos);
			txtTEC.moveCaretPosition(caret+pos+len);
		}
		//txtPesquisaTextoNCM.requestFocusInWindow();
	}


	public void pesquisaCodigoNCM() {
		String filtro = txtPesquisaCodigoNCM.getText();
		if (filtro.length() >= 4){
			filtro = "("+ filtro.substring(0, 2) + "." + filtro.substring(2,4) + "|" + filtro +")";
		}
		filtro = "^"+filtro;
		//		System.out.println(filtro);
		RowFilter<? super TableModel, ? super Integer> rf = RowFilter.regexFilter(filtro, 0);
		sorter.setRowFilter(rf);
	}


	@SuppressWarnings("serial")
	public class PlaceholderTextField extends JTextField {


		private String placeholder;

		public PlaceholderTextField() {
		}

		public PlaceholderTextField(
				final Document pDoc,
				final String pText,
				final int pColumns)
		{
			super(pDoc, pText, pColumns);
		}

		public PlaceholderTextField(final int pColumns) {
			super(pColumns);
		}

		public PlaceholderTextField(final String pText) {
			super(pText);
		}

		public PlaceholderTextField(final String pText, final int pColumns) {
			super(pText, pColumns);
		}

		public String getPlaceholder() {
			return placeholder;
		}

		@Override
		protected void paintComponent(final Graphics pG) {
			super.paintComponent(pG);

			if (placeholder.length() == 0 || getText().length() > 0) {
				return;
			}

			final Graphics2D g = (Graphics2D) pG;
			g.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(getDisabledTextColor());
			g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
					.getMaxAscent() + getInsets().top);
		}

		public void setPlaceholder(final String s) {
			placeholder = s;
		}

	}

	private class ClassificaSwingWorker extends SwingWorker<Void, JTextArea>{
		@Override
		protected Void doInBackground(){
			carregaTECCompleta();
			return null;
		}

		@Override

		protected void done() {
			btnPesquisaPalavrasNCM.setEnabled(true);
			btnPesquisaTextoNCM.setEnabled(true);
			btnPesquisaCodigoNCM.setEnabled(true);
			lblAguarde.setVisible(false);
		}
	}


	public void carregaTECCompleta() {
		InputStream is = ClassificaForm.class.getResourceAsStream("/org/classifica/resources/tec.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			txtTEC.read(br, is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		vetorizador = new VetorizadorTEC(txtTEC.getText());
		vetorizador.inicializa();
		txtTEC.setCaretPosition(0);
		txtTEC.moveCaretPosition(0);
		ModelNCM model = (ModelNCM) tblNCM.getModel();
		model.limpar();
		model.addLista(vetorizador.getListaNCM());
		DefaultComboBoxModel<Capitulo> modeln = (DefaultComboBoxModel<Capitulo>) cbbCapNCM.getModel();
		modeln.addElement(null);
		for ( Capitulo capitulo : vetorizador.getListaCapitulos() ) {  
			modeln.addElement( capitulo );
		}   
		cbbCapNCM.addItemListener(new MyCapChangeListener());
		@SuppressWarnings("unchecked")
		List<Pais> paises = (List<Pais>) vetorizador.deSerialize(vetorizador.caminho+"listadepaises");
		DefaultComboBoxModel<Pais> modelr = (DefaultComboBoxModel<Pais>) cbbPaisOrigemCapNCM.getModel();
		modelr.addElement(null);
		/*DataAccess dataaccess = DataAccess.getDB();
		List<Pais> paises;
		try {
			paises = dataaccess.qetListaPaises();
			*/
			for ( Pais obj : paises ) {  
				modelr.addElement( obj );
			}   
		/*} catch (SQLException e1) {
			e1.printStackTrace();
		}*/
		cbbPaisOrigemCapNCM.addItemListener(new MyItemChangeListener());
		///vetorizador.serializeListaPaises(paises, vetorizador.caminho+"listadepaises");
	}
	private void ajustaParametros() {
		ParametrosBM25Form frm = new ParametrosBM25Form();
		frm.setVetorizador(vetorizador);
		frm.setVisible(true);
	}

	protected String selecionaExplicacaoTEC(Integer pindex ) {
		if (pindex==0){
			pindex = tblSugestoes.getSelectedRow();
		}
		String explicacao = "";
		ModelSugestoes model = (ModelSugestoes) tblSugestoes.getModel();
		if (pindex >= 0){
			String codigo = model.getValueAt(pindex, 1);
			codigo = codigo.substring(0, 11);
			ArrayList<String[]> lista = vetorizador.getTECsPontosDescricao();
			for (String[] linha:lista){
				//System.out.println(linha[1]+':'+codigo);
				if(codigo.equals(linha[1])){
					explicacao = "Linha: "+codigo+ " -- "+ linha[0];
					//	txtExplicacao.append(explicacao);
					break;
				}
			}
		}
		return explicacao;

	}

	private class MySelectionListener implements  ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			selecionaExplicacaoTEC(0);
		}
	}


	protected class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() ==  KeyEvent.KEY_PRESSED && e.getKeyCode()==KeyEvent.VK_F1 && e.getModifiers() == 0){
				showHelp();
				e.consume();
			}
			return false;
		}
	}
	public void showHelp() {
		Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
		String aUrl = "P:/SISTEMAS/help/";
		String aFrame = activeWindow.getClass().getSimpleName();
		aUrl = aUrl + aFrame +".html";
		if (helpForm==null){
			helpForm = new HelpForm();
		}
		helpForm.loadArquivo(aUrl);
		helpForm.setVisible(true);
	}

	class MyItemChangeListener implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				Pais pais = (Pais) event.getItem();
				model.limpar();
				if(pais==null){
				}else{
					model.addLista(pais.getEstatisticacapncm());
				}
			}
		}
	}

	class MyCapChangeListener implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				Capitulo capitulo = (Capitulo) event.getItem();
				if(capitulo==null){
					txtNotas.setText("");
				}else{
					txtNotas.setText(capitulo.getDescricao()+"\n------\n"+capitulo.getNotas());
				}
			}
		}
	}


	public VetorizadorTEC getVetorizador() {
		return vetorizador;
	}

	private String toolTipExplicacao(MouseEvent e, JTable t) {
		String tip = null;
		String htmltip = "<html>";
		java.awt.Point p = e.getPoint();
		int rowIndex = t.rowAtPoint(p);
		int colIndex = t.columnAtPoint(p);
		try {
			tip = t.getValueAt(rowIndex, colIndex).toString();
			tip = tip + "<br>" + selecionaExplicacaoTEC(rowIndex);
			int numRows = (tip.length() % 150) + 1;
			for (int r=0; r<=numRows; r+=1){
				int beginIndex = r * 150;
				int endIndex = (r+1) * 150;
				if (endIndex > tip.length()){
					endIndex = tip.length();
				}
				String novalinha = tip.substring(beginIndex, endIndex);
				htmltip = htmltip + novalinha + "<br>";
			}
			//htmltip = htmltip + "<br>"+ selecionaExplicacaoTEC(rowIndex);
			htmltip = htmltip + "</html>";
		} catch (RuntimeException e1) {
			//e1.printStackTrace();
			//catch null pointer exception if mouse is over an empty line
		}
		//				System.out.println(htmltip);

		return htmltip;
	}
}



