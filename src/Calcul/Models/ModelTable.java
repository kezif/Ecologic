package Calcul.Models;

import Calcul.Calculations.Element;
import Calcul.Calculations.XY;

import javax.swing.table.AbstractTableModel;
import java.util.LinkedList;
import java.util.Vector;

public class ModelTable extends AbstractTableModel{

	 private LinkedList<Element> elem;
	 private XY xy;
	

	private Vector<String> columnNames;

	public ModelTable(LinkedList<Element> elem, XY xy) {
		
		this.elem=elem;
		this.xy=xy;
		    columnNames = new Vector<String>();
		    columnNames.add("Код элемента" );
			columnNames.add("C ("+ xy +") [г/с\u00B3]" );
			columnNames.add("Cp ("+ xy +") [г/с\u00B3]");
		    columnNames.add("Ccc [г/с\u00B3]");
		
			}

	@Override
	public int getColumnCount() {
		return 4;
	}
		
	public LinkedList<Element> getElem() {
		return elem;
	}

	public void setElem(LinkedList<Element> elem) {
		this.elem = elem;
	}

	public XY getXy() {
		return xy;
	}

	public void setXy(XY xy) {
		this.xy = xy;
	}

	@Override
	public String getColumnName(int column)
    {
          return columnNames.get(column);
    }

	@Override
	public int getRowCount() {
		return elem.size();
	}

	@Override
	public Object getValueAt(int i, int j) {
		String value;
		if(j == 0)
			value = elem.get(i).getCode();
		else if (j==1) {
				value = String.valueOf(elem.get(i).getC());
			} else if(j==2){
				    value = String.valueOf(elem.get(i).getCp());
			}else{
				 value = String.valueOf(elem.get(i).getCcc());
			}
	
		return value;
	}

}
