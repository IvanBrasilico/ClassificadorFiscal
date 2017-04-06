package org.classifica.gui;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.Document;

import org.classifica.util.VetorizadorTEC;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;


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
	private JTextArea txtPontuacao;

	private JScrollPane scrollPane3;

	private JCheckBox rdbTexto;

	private JCheckBox rdbBigrama;

	private JCheckBox rdbStem;



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
		setBounds(100, 100, 872, 594);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 118, 797,386);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(tabbedPane);

		panelA = new JPanel();
		panelA.setBounds(10, 0, 846, 118);
		contentPane.add(panelA);
		panelA.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 33, 814, 59);
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
		btnPesquisaPalavrasNCM.setBounds(334, 5, 255, 23);
		panelA.add(btnPesquisaPalavrasNCM);

		btnImportarDeLaudo = new JButton("Importar palavras de arquivo PDF...");
		btnImportarDeLaudo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				importaPalavrasdePDF();
			}
		});
		btnImportarDeLaudo.setBounds(590, 5, 246, 23);
		panelA.add(btnImportarDeLaudo);

		chkPonderado = new JCheckBox("Utilizar pesos ponderados TF-IDF");
		chkPonderado.setSelected(true);
		chkPonderado.setBounds(23, 95, 275, 23);
		panelA.add(chkPonderado);

		lblAguarde = new JLabel("Aguarde!!! Carregando dados...");
		lblAguarde.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblAguarde.setBounds(23, 3, 401, 23);
		panelA.add(lblAguarde);

		rdbStem = new JCheckBox("Stemiza\u00E7\u00E3o");
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
		btnParametros.setBounds(676, 95, 160, 23);
		panelA.add(btnParametros);
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
				txtPontuacao.setText(vetorizador.pontuaTexto(txtPesquisa.getText(), chkPonderado.isSelected()));
				Integer index = vetorizador.getTECsPontos().size();
				if (index>50){
					index = 50;
				}
				ArrayList<String[]> linhas = new ArrayList<String[]>();
				for (int r=0;r<index;r++){
					String[] linha = {String.format("%.4f", vetorizador.getTECsPontos().get(r)[0]), vetorizador.getListaTECResumo().get(vetorizador.getTECsPontos().get(r)[1].intValue())}; 
					linhas.add(linha);
				}
				ModelSugestoes model = (ModelSugestoes) tblSugestoes.getModel();
				model.limpar();
				model.addLista(linhas);
			}
		});


		scrollPane_1 = new JScrollPane();
		tabbedPane.addTab("Sugestões", null, scrollPane_1, null);

		tblSugestoes = new JTable(){
			private static final long serialVersionUID = 1L;
			public String getToolTipText(MouseEvent e) {
				String tip = null;
				String htmltip = "<html>";
				java.awt.Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				int colIndex = columnAtPoint(p);
				try {
					tip = getValueAt(rowIndex, colIndex).toString();
					int numRows = (tip.length() % 150) + 1;
					for (int r=0; r<=numRows; r+=1){
						int beginIndex = r * 150;
						int endIndex = (r+1) * 150;
						if (endIndex > tip.length()){
							endIndex = tip.length();
						}
						htmltip = htmltip + tip.substring(beginIndex, endIndex) + "<br>";
					}
					htmltip = htmltip + "</html>";

				} catch (RuntimeException e1) {
					//e1.printStackTrace();
					//catch null pointer exception if mouse is over an empty line
				}
//				System.out.println(htmltip);

				return htmltip;
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



		splitPane3 = new JSplitPane();
		splitPane3.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabbedPane.addTab("Explicação - ver pontuação palavras", null, splitPane3, null);

		lblNewLabel_1 = new JLabel("Pontua\u00E7\u00E3o das palavras pesquisadas");
		splitPane3.setLeftComponent(lblNewLabel_1);


		scrollPane3= new JScrollPane();
		splitPane3.setRightComponent(scrollPane3);


		txtPontuacao = new JTextArea();
		txtPontuacao.setEditable(false);
		txtPontuacao.setLineWrap(true);
		txtPontuacao.setWrapStyleWord(true);
		scrollPane3.setViewportView(txtPontuacao);


		contentPane.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e) {
				panelA.setSize(contentPane.getWidth()-10, 118);
				tabbedPane.setSize(contentPane.getWidth()-10, contentPane.getHeight()-120);
				scrollPane_1.setSize(contentPane.getWidth()-12, contentPane.getHeight()-140);
			}
		});
		/////////////////////////////////////
		///InicializaListasTEC....
		//////////////////////////////////////////////////////
		//carregaTECCompleta();

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
			txtPesquisa.setText(getPalavrasPDF(fileInput));
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
		System.out.println(filtro);
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

	public String getPalavrasPDF(String arquivo) {
		PdfReader reader;
		try {
			reader = new PdfReader(arquivo);
			String texto = "";
			for (int r=1;r<=reader.getNumberOfPages();r++){
				texto = texto + " " + PdfTextExtractor.getTextFromPage(reader, r);
			}
			reader.close();
			return vetorizador.getPalavrasnoVocabulario(texto);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
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

	}
	private void ajustaParametros() {
		ParametrosBM25Form frm = new ParametrosBM25Form();
		frm.setVetorizador(vetorizador);
		frm.setVisible(true);
	}

}



