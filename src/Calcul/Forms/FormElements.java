package Calcul.Forms;

import Calcul.Calculations.Company;
import Calcul.Calculations.Element;
import Calcul.Calculations.XY;
import Utils.DataFromExcel.CatalogOfElements;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.LinkedList;


public class FormElements extends JFrame {
	 private static String LABEL_H="Высота источника выброса H [м]: ";
	 private static String LABEL_D="Диаметр источника выброса D: [м]: ";
	 private static String LABEL_V1="Объемный расход выбрасываемой смеси V1 [м\u00B3/с]: ";
	 private static String LABEL_TG="Температура газовоздушной смеси Tг [\u00B0С]: ";
	 private static String LABEL_N3="n3 [%]: ";
	 private static String LABEL_TV="Температура воздуха Тв [\u00B0С]: ";
	 private static String LABEL_UM="Максимальная скорость ветра Uм [м/с]: ";
	 private static String LABEL_U="Среднегодовая скорость ветра U [м/с]: ";

	private static  String H="50";
    private static  String D="40";
    private static  String V1="400";
    private static  String TG="120";
    private static  String N3="0.80";
    private static  String TV="24";
    private static  String UM="24";
    private static  String U="2";
    
	private Company c;
    private CatalogOfElements catalog = CatalogOfElements.getInstance();
    private JPanel p1;
    private JPanel p2;
    
	public FormElements(Company c){
		
		this.c=c;
		setTitle("Перечень элементов "+c.getName()+ " ");
		initComponents();	
	    
	}
	
	 private void initComponents() {
            LinkedList<Element> elements=c.getElements();
            Iterator iterator = elements.iterator();
            setSize(1050, 650);
	        JLabel l;
	        JTextField t;
	        
	        setLayout(null);
	        p1=new JPanel();
	        p1.setBounds(10, 10, 400, 600);
	        p1.setLayout(new GridLayout(0,2));
	        
	        while(iterator.hasNext()){
	           Element el=(Element) iterator.next();
	           String name="";
	            //System.out.println("Element :" + el.getCode()+" Concentration " +el.getMass());
	            l= new JLabel();
	            Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
	            l.setBorder(border);
	            name=el.getName() +" [г/с]";
	            if(el.isHot()){
	            	name=name + " *";
	            }
	            l.setText(name);
	            p1.add(l);
	            t= new JTextField();
	            t.setBorder(border);
	            t.setText(el.getMass().toString());
	            p1.add(t);
	            
	           
	        }
	        
	        p2 = panel2();
	        //System.out.println(getListofElements(p1,elements).toString());
	        getContentPane().add(p1);
	        getContentPane().add(p2);
	        
	        
	       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	       
	 }
	 
	 	 
	 
	 private JPanel panel2(){
		 JPanel p =new JPanel();
		 p.setBounds(415, 10, 620, 250);
		 p.setLayout(new GridLayout(0,2));
		// p.setSize(new Dimension(100, 200));
		
		 JLabel lh =new JLabel(LABEL_H);
		 JTextField h =new JTextField(); h.setText(H);
		 p.add(lh); p.add(h); 
		 JLabel ld =new JLabel(LABEL_D);
		 JTextField d =new JTextField(); d.setText(D);
		 p.add(ld); p.add(d);
		 JLabel lv1 =new JLabel(LABEL_V1);
		 JTextField v1 =new JTextField(); v1.setText(V1);
		 
		 p.add(lv1); p.add(v1);
		 JLabel ltg =new JLabel(LABEL_TG);
		 JTextField tg =new JTextField(); tg.setText(TG);
		 p.add(ltg); p.add(tg);
		 JLabel ln3 =new JLabel(LABEL_N3);
		 JTextField n3 =new JTextField(); n3.setText(N3);
		 p.add(ln3); p.add(n3);
		 JLabel larea =new JLabel("Область : ");
		 String[] areas = { "Брестская", "Витебская", "Гродненская", "Гомельская", "Минская", "Могилевская" };
         p.add(larea); 
	
		 JComboBox areasList = new JComboBox(areas);
		 areasList.setSelectedIndex(0);
		 p.add(areasList);
		 JLabel lTv =new JLabel(LABEL_TV);
		 JTextField tv =new JTextField(); tv.setText(TV);
		 tv.setEditable(false);
		 p.add(lTv); p.add(tv);
		 JLabel lUm =new JLabel(LABEL_UM);
		 JTextField um =new JTextField(); um.setText(UM);
		 um.setEditable(false);
		 p.add(lUm); p.add(um);
		 JLabel lU =new JLabel(LABEL_U);
		 JTextField u =new JTextField(); u.setText(U);
		 u.setEditable(false);
		 p.add(lU); p.add(u);
		 
		 areasList.addItemListener(new ItemListener() {
		       
				@Override
				public void itemStateChanged(ItemEvent arg0) {
				  int area=areasList.getSelectedIndex();
				  
				  switch (area) {
				case 0:
					tv.setText("24");
					um.setText("22");
					u.setText("2.5");
					
					break;
                case 1:
                	tv.setText("23");
					um.setText("23");
					u.setText("2");
					
					break;
                case 2:
                	tv.setText("24");
					um.setText("24");
					u.setText("2.4");
					
					break;
                case 3:
                	tv.setText("25");
					um.setText("17");
					u.setText("1.9");
					
					break;
                case 4:
                	tv.setText("23");
					um.setText("24");
					u.setText("2.9");
					
					break;
                case 5:
                	tv.setText("24");
					um.setText("24");
					u.setText("2.6");
					
					break;


					

				default:
					
					break;
				} 
					
				}
		    });
		 
		JButton set =new JButton("Рассчитать");
		set.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("Click");
				c.setElements(getListofElements(p1, c.getElements()));
				getParameters(p);
				calculateConcentration();
               //System.out.println("uz= "+c.getElementByCode("0301").getUz());

				FormReport fr  = new FormReport(c); 
		        SwingUtilities.invokeLater(new Runnable() {
		           @Override
				public void run() {
		              fr.setVisible(true);
		           }
		        });	
				
				
			}
		});
		 
		p.add(set);
		
         return p;
		 	 
	 }
	 
private LinkedList<Element> getListofElements(JPanel p, LinkedList<Element> elements){
		 
		 LinkedList<Double> mas=new LinkedList<Double>(); 
		 Component[] c=p.getComponents();
		 int size=c.length;
		 for(int i = 0; i<size;i++){
			 if(c[i] instanceof JTextField){
				 JTextField f=(JTextField) c[i];
				 mas.add( Double.parseDouble(f.getText()));	 
			 }
	 
		 }
		 Iterator iterator = elements.iterator();
		 int i=0;
		 while(iterator.hasNext() ){
			 Element el=(Element) iterator.next();
			 el.setMass(mas.get(i));			 
			 elements.set(i, el);
			 i++;

		 }
		 
		 return elements;
		 
		 
	 }

	 
	 private void getParameters(JPanel p){
			 LinkedList<Double> mas=new LinkedList<Double>(); 
			 Component[] c=p.getComponents();
			 int size=c.length;
			 for(int i = 0; i<size;i++){
				 if(c[i] instanceof JTextField){
					 JTextField f=(JTextField) c[i];
					 mas.add( Double.parseDouble(f.getText()));	 
				 }			 
			 }
			 this.c.setH(mas.get(0));
			 this.c.setD(mas.get(1));
			 this.c.setV1(mas.get(2));
			 this.c.setTg(mas.get(3));
			 this.c.setN3(mas.get(4)); 	
			 this.c.setTv(mas.get(5));
			 this.c.setUm(mas.get(6));
			 this.c.setU(mas.get(7));
		 
	 }
	 
	 //Рассчет коэффициентов
	 private void calculateConcentration(){
		    c.setW0();
			c.setDelt();
			c.setF();
			c.setVm();
			c.setVm1();
			c.setFe();
			c.setM();
			c.setkF();
			c.setXmhot();
			c.setXmcold();
			c.setUdhot();
			c.setUdcold();
			c.setRhot();
			c.setRcold();
			c.setXmihot();
			c.setXmicold();
			
			
			System.out.println("W0: " + c.getW0()); 
			System.out.println("delT: " +c.getDelt());
			System.out.println(" Vm: " +c.getVm());
			System.out.println(" fe: " + c.getFe());
			System.out.println(" m: " + c.getM());
			System.out.println(" Xm горяч: " + c.getXmhot());
			System.out.println(" Xm холодн: " + c.getXmcold());
			System.out.println(" Um горяч: " + c.getUdhot());
			System.out.println(" Um холодн: " + c.getUdcold());
			System.out.println(" r горяч: " + c.getRhot());
			System.out.println(" r холодн: " + c.getRcold());
			System.out.println(" Xми горяч: " + c.getXmihot());
			System.out.println(" Хми холодн: " + c.getXmicold());
			
			//Рассчет концентрации для всех элементов предприятия
		 LinkedList<Element> elements=c.getElements();
         Iterator iterator = elements.iterator();
         int i=0;
         while(iterator.hasNext()){
			 Element el=(Element) iterator.next();
			 double n = c.getN(el.isHot());
			 el.setCm(n, c.getM(), c.getkF(), c.getH(), c.getDelt(), c.getV1(), c.getD());
			 el.setUz();
			 el.setCmi(c.getRhot(), c.getRcold());
			 
			 
			 elements.set(i, el);
			 
		     i++;
         }
         c.setElements(elements);
         
         //Рассчет концентрации в нескольких точках 
         XY xy=new XY(30,50);
			c.setXy(xy);
			LinkedList<XY> lst = new LinkedList<>();
			lst.add(new XY(40,100));
			lst.add(new XY(100,100));
			lst.add(new XY(200,100));
			lst.add(new XY(10,100));
			lst.add(new XY(300,100));
			c.setMap(lst);

         
         
		 
	 }
	 
	 

}
