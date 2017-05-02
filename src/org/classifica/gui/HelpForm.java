package org.classifica.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;


import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;

public class HelpForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private String arquivoRaiz="P:/PUBLICO/GRALT/Laudo/help/index.html";
	private String arquivoAtual="";
	private JEditorPane edpHTML;


	/**
	 * Create the frame.
	 */
	public HelpForm() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(HelpForm.class.getResource("/javax/swing/plaf/metal/icons/ocean/question.png")));
		setTitle("Ajuda do Sistema Laudo");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 617, 443);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.EAST);
		
		JButton btnIndice = new JButton("\u00CDndice");
		btnIndice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadArquivo("");
			}
		});
		
		JButton btnFechar = new JButton("Fechar");
		btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnIndice, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnFechar, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(21, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(5)
					.addComponent(btnIndice)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnFechar)
					.addContainerGap(338, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		edpHTML = new JEditorPane();
		edpHTML.setEditable(false);
		edpHTML.setContentType("text/html");
		DefaultCaret caret = (DefaultCaret)edpHTML.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		JScrollPane scrollPane = new JScrollPane(edpHTML);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		edpHTML.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent ev) {
				try {
					if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						edpHTML.setPage(ev.getURL());
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
	}


	public void loadArquivo(String arquivo) {
		if (arquivo.isEmpty()) {
			arquivoAtual = arquivoRaiz;
		} else {
			arquivoAtual = arquivo;
		}
		String helpContent = "";
		try {
			File file=new File(arquivoAtual);
			if (file.exists()) {
				byte[] encoded = Files.readAllBytes(Paths.get(arquivo));
				helpContent =  new String(encoded, Charset.defaultCharset());
			} else {  
				JOptionPane.showMessageDialog(this, "File Does not Exist \n" + file.getPath() + "\n" + file.toString());
			}
		} catch (Exception ex) {
		//	ex.printStackTrace();
		}
		if(helpContent!=""){
			HTMLDocument doc = (HTMLDocument)edpHTML.getDocument();
			doc.putProperty("IgnoreCharsetDirective", new Boolean(true));
			HTMLEditorKit editorKit = (HTMLEditorKit)edpHTML.getEditorKit();
			try {
				editorKit.insertHTML(doc, doc.getLength(), helpContent, 0, 0, null);
			} catch (BadLocationException | IOException e) {
			//	e.printStackTrace();
			}
		   edpHTML.setText(helpContent);
		}
	}


	public void setRaiz(String string) {
		arquivoRaiz = string;
		
	}

}
