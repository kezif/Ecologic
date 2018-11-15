package Calcul.Calculations;
import java.util.ArrayList;


public class SumElem {
	private ArrayList<String> sumelem;
	
	
	public SumElem() {
		this.sumelem = new ArrayList<String>();
	}
	
	
	public SumElem(ArrayList<String> sumelem) {
		this.sumelem = sumelem;
	}

	public ArrayList<String> getSumelem() {
		return sumelem;
	}

	public void setSumelem(ArrayList<String> sumelem) {
		this.sumelem = sumelem;
	}
    
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return sumelem.toString();
	}
}
