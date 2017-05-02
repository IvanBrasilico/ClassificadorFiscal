package org.classifica.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.classifica.util.VetorizadorTEC;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.awt.event.ActionEvent;

public class FormPDFView extends JFrame implements ChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private int numpage = 1;
	private JSpinner spinner;
	private VetorizadorTEC vetorizador;
	private JTextArea txtPalavras;
	private JPanel pnlImg;
	private JLabel lblImg;
	private String arquivo;


	public FormPDFView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 821, 631);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.NORTH);
		
		
		JScrollPane scrollPane = new JScrollPane();
		txtPalavras = new JTextArea();
		txtPalavras.setLineWrap(true);
		txtPalavras.setWrapStyleWord(true);
		txtPalavras.setRows(5);
		scrollPane.setViewportView(txtPalavras);
		splitPane.setRightComponent(scrollPane);

		panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1, 0, 0));
		splitPane.setLeftComponent(panel);
		JButton btnAdicionarTexto = new JButton("Adicionar texto da p\u00E1gina");
		panel.add(btnAdicionarTexto);
		
		btnLimpar = new JButton("Limpar");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtPalavras.setText("");
			}
		});
		panel.add(btnLimpar);
		
		btnFechar = new JButton("Fechar");
		btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeForm();
			}
		});
		panel.add(btnFechar);
		btnAdicionarTexto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPalavrasPDF();
			}
		});

		JSplitPane splitPane_1 = new JSplitPane();
		contentPane.add(splitPane_1, BorderLayout.CENTER);

		pnlImg = new JPanel();
		splitPane_1.setRightComponent(pnlImg);

		lblImg = new JLabel("");
		pnlImg.add(lblImg);

		JPanel panel_1 = new JPanel();
		splitPane_1.setLeftComponent(panel_1);
		panel_1.setLayout(new GridLayout(10, 1, 0, 0));

		JLabel lblNewLabel = new JLabel("   Selecione a p\u00E1gina   ");
		panel_1.add(lblNewLabel);

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 1, 1));
		panel_1.add(spinner);
		spinner.addChangeListener(this);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				try{
					reader.close();
					textoRetorno.setText(txtPalavras.getText());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	private void closeForm() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	private PdfReader reader;
	private JPanel panel;
	private JButton btnLimpar;

	public void setPDF(String arquivo) throws IOException{
		try {
			reader = new PdfReader(arquivo);
			this.arquivo = arquivo;
			SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
			model.setMaximum(reader.getNumberOfPages());
			mostraPagina();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}


	@Override
	public void stateChanged(ChangeEvent e) {
		int newpage = (int) spinner.getValue();
		System.out.println(numpage);
		if (newpage!=numpage){
			numpage = newpage;
			mostraPagina();
		}
	}

	private void mostraPagina() {

		RandomAccessFile raf;
		try {
			File file = new File(arquivo);
			raf = new RandomAccessFile(file, "r");
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			PDFFile pdf = new PDFFile(buf);
			PDFPage page = pdf.getPage(numpage);

			// create the image
			Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(),
					(int) page.getBBox().getHeight());

			Image image = page.getImage(rect.width, rect.height,    // width & height
					rect,                       // clip rect
					null,                       // null for the ImageObserver
					true,                       // fill background with white
					true                        // block until drawing is done
					);

			lblImg.setIcon(new ImageIcon(image));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void getPalavrasPDF() {
		String texto;
		try {
			texto = PdfTextExtractor.getTextFromPage(reader, numpage);
			txtPalavras.append(vetorizador.getPalavrasnoVocabulario(texto));
		} catch (IOException e) {
			e.printStackTrace();
			texto = "";
		}
	}


	public void setVetorizador(VetorizadorTEC pvetorizador) {
		vetorizador = pvetorizador;
	}

    private JTextArea textoRetorno; 
    private JButton btnFechar;
	public void setTextoRetorno(JTextArea textArea) {
		textoRetorno = textArea;
	}

}
