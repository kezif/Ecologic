package Calcul.Forms;

import Calcul.Calculations.Company;
import Calcul.Calculations.Element;
import Calcul.Calculations.SumElem;
import Calcul.Calculations.XY;
import Utils.DataFromExcel.CatalogOfElements;
import Calcul.Models.ModelTable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

public class FormReport extends JFrame {
	private Company c;
	private Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
	private ArrayList<SumElem> sumelem = CatalogOfElements.getInstance().sumelem;
	private HashMap<XY, LinkedList<Element>> map;
	private LinkedList<XY> lst = new LinkedList();
	private JTable table;
	private ModelTable model;

	public FormReport(Company c) throws HeadlessException {
		this.c = c;
		HashMap<XY, LinkedList<Element>> map = c.getMap();
		Iterator<Entry<XY, LinkedList<Element>>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<XY, LinkedList<Element>> entry = iter.next();
			lst.add(entry.getKey());
		}
		initFrame();
	}

	private void initFrame() {
		setLayout(new FlowLayout());
		setSize(1050, 900);
		JPanel p1 = new JPanel();
		p1.setBounds(10, 10, 1000, 600);
		JPanel p2 = new JPanel();
		createTable(p1);
		p2.setBounds(10, 620, 350, 100);
		createTableUz(p2, getStringsUz(sumelem));
		JPanel p4 = new JPanel();
		p4.setBounds(10, 750, 700, 300);
		p4.setLayout(new GridLayout(0, 1));
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(10, 720, 200, 30);
		comboBox.setModel(new DefaultComboBoxModel(lst.toArray()));
		comboBox.setSelectedIndex(0);

		model = new ModelTable(c.getMap().get(lst.get(0)), lst.get(0));
		table = new JTable();
		table.setModel(model);
		table.setVisible(true);
		comboBox.addActionListener(new ActionListener() {///////////////// choose function
			@Override
			public void actionPerformed(ActionEvent arg0) {
				XY n = (XY) comboBox.getSelectedItem();

				model = new ModelTable(c.getMap().get(n), n);
				table.setModel(model);
			}
		});

		JPanel p5 = new JPanel();
		p5.setBounds(10, 720, 200, 30);
		p5.setLayout(new FlowLayout());
		p5.add(comboBox);
		p4.add(table);
		JScrollPane js = new JScrollPane(table);
		js.setBounds(10, 750, 400, 400);
		js.setVisible(true);
		p4.add(js);
		JPanel p3 = new JPanel();
		p3.setBounds(380, 620, 350, 100);
		createTableX(p3);
		JPanel p0 = new JPanel();
		p0.setPreferredSize(new Dimension(1000, 2000));
		p0.setBounds(0, 0, 980, 1000);
		p0.setLayout(null);
		p0.add(p1);
		p0.add(p2);
		p0.add(p3);
		p0.add(p4);
		p0.add(p5);

		JScrollPane scrollBar = new JScrollPane(p0, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollBar.setPreferredSize(new Dimension(1000, 700));
		add(scrollBar);

	}

	public ArrayList<String> getStringsUz(ArrayList<SumElem> sumel) {
		ArrayList<String> lst = new ArrayList<String>();
		for (SumElem el : sumel) {
			if (c.checkElem(el.getSumelem()) != null) {
				lst.add(getUz(el.getSumelem()));
			}
		}
		return lst;

	}

	public String getUz(ArrayList<String> names) {
		String str = "";
		String str2 = "Узсумм ( ";
		String str0 = "";
		double sumuz = 0;
		if (!names.isEmpty()) {

			ArrayList<String> lst = names;
			for (String code : lst) {
				Element e = this.c.getElementByCode(code);

				sumuz += e.getUz();
				str2 += e.getCode() + " ";
			}
			if (sumuz > 1)
				str0 = " >1";
			str += str2 + " ) = " + sumuz + str0 + "\n";

		} else {
			str = "";
		}
		return str;
	}

	public void createTableUz(JPanel p, ArrayList<String> s) {
		boolean b = false;
		int i = 0;

		for (String str : s) {
			if (!str.equals(""))
				b = true;
			break;
		}

		if (b) {
			p.add(new JLabel("Вещества обладающие эффектом суммации:"));
			p.setBackground(Color.WHITE);
			i = 1;
		}
		for (String str : s) {
			if (str.equals("")) {

			} else {
				i++;
				JLabel lb = new JLabel(str, SwingConstants.CENTER);
				lb.setBorder(border);
				p.setLayout(new GridLayout(i, 1));
				p.add(lb);
			}
		}

	}

	public void createTableX(JPanel p) {
		p.setBackground(Color.WHITE);
		p.setLayout(new GridLayout(0, 2));
		JLabel lh = new JLabel("Горячие выбросы [м]");
		lh.setBorder(border);
		JLabel lc = new JLabel("Холодные выбросы [м]");
		lc.setBorder(border);
		p.add(lh);
		p.add(lc);
		JLabel xmh = new JLabel("Xм = " + c.getXmhot());
		xmh.setBorder(border);
		JLabel xmc = new JLabel("Хм = " + c.getXmcold());
		xmc.setBorder(border);
		p.add(xmh);
		p.add(xmc);
		JLabel xmih = new JLabel("Хми = " + c.getXmihot());
		xmih.setBorder(border);
		JLabel xmic = new JLabel("Хми = " + c.getXmicold());
		xmic.setBorder(border);
		p.add(xmih);
		p.add(xmic);

	}

	public void createTable(JPanel p1) {
		LinkedList<Element> elements = c.getElements();
		Iterator iterator = elements.iterator();
		p1.setBackground(Color.WHITE);
		JLabel l;
		JLabel cm;
		JLabel cmi;
		JLabel uz;
		JLabel mpc;
		JLabel code;
		p1.setLayout(new GridLayout(0, 6));
		p1.add(new JLabel("Код"));
		p1.add(new JLabel("Элемент "));
		p1.add(new JLabel("Концентрация [г/м\u00B3]"));
		p1.add(new JLabel("Сми [г/м\u00B3]"));
		p1.add(new JLabel("ПДК [г/м\u00B3]"));
		p1.add(new JLabel("Уровень загрязнения "));
		while (iterator.hasNext()) {
			Element el = (Element) iterator.next();

			String name = "";
			l = new JLabel();

			code = new JLabel(el.getCode());
			code.setBorder(border);
			p1.add(code);
			l.setBorder(border);
			name = el.getName();
			if (el.isHot()) {
				name = name + " *";
			}
			l.setText(name);
			p1.add(l);
			cm = new JLabel();
			cm.setBorder(border);
			cm.setText(Double.toString(el.getCm()));
			p1.add(cm);
			cmi = new JLabel();
			cmi.setBorder(border);
			cmi.setText(Double.toString(el.getCmi()));
			p1.add(cmi);
			mpc = new JLabel();
			mpc.setBorder(border);
			mpc.setText(Double.toString(el.getMPC()));
			p1.add(mpc);
			uz = new JLabel();
			uz.setBorder(border);
			String uzresult = Double.toString(el.getUz());
			uz.setOpaque(true);
			if (el.getUz() > 1) {
				uzresult = uzresult + " >1";
				uz.setBackground(Color.RED);
				uz.setText(uzresult);
			} else {
				uz.setText(uzresult);
				uz.setBackground(null);
			}
			p1.add(uz);

		}

	}

}
