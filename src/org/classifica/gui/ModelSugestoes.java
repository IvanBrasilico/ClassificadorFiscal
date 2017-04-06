package org.classifica.gui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;



public class ModelSugestoes extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private String[] colunas = new String[]{"Pontuação", "Descrição"};
    private ArrayList<String[]> linhas;
    public ModelSugestoes() {
        linhas = new ArrayList<String[]>();
    }
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return colunas.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return colunas[columnIndex];
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return linhas.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return linhas.get(rowIndex)[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub

		
	}
	
    public void addLista(ArrayList<String[]> plinhas) {
        // Pega o tamanho antigo da tabela.
        int tamanhoAntigo = getRowCount();
        // Adiciona os registros.
        linhas.addAll(plinhas);
        fireTableRowsInserted(tamanhoAntigo, getRowCount() - 1);
    }
    /* Remove todos os registros. */
    public void limpar() {
        linhas.clear();
        fireTableDataChanged();
    }
    /* Verifica se este table model esta vazio. */
    public boolean isEmpty() {
        return linhas.isEmpty();
    }


}
