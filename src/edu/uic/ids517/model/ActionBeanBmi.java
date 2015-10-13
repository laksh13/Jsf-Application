package edu.uic.ids517.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActionBeanBmi {

	private DbaseAccessBean dbaseAccessBean;
	//private DbaseBean dbaseBean;
	private BmiBean bmiBean;
	//maintains list of previous runs
	private List <BmiBean> bmiBeanList;
	private boolean renderList;
	
	public ActionBeanBmi() {
			
		renderList = false;
		
		setBmiBeanList(new ArrayList <BmiBean> ());
	
	}

	public String computeBMI() 
		throws CloneNotSupportedException {
		String status = bmiBean.computeBMI();
		BmiBean x = bmiBean.clone();
		
		if(bmiBean.getErrorCode()==0)
		{
			bmiBeanList.add(x);
			renderList = true;
		}
	
		return status;
	}
	
	//public String updateDatabase() {
		//String sqlQuery = "INSERT INTO bmiTable () Values(";
		//dbaseBean.setQueryType("INSERT");
		//BmiBean bmi = null;
		//for(int i=0; i < bmiBeanList.size(); i++) {
			//bmi = bmiBeanList.get(i);
			//dbaseBean.executeSQL(sqlQuery +
				//"null, " + i + ", " +
					//bmi.getWeight() + "," +
					//bmi.getHeight() + "," +
					//bmi.getBmi() + "," +
					//bmi.getBmiPrime() + ", \'" +
					//bmi.getBmiCateogry() + "\') "	);
		
				
		//}
	//}
	
	public String reset() {
		bmiBeanList.clear();
		renderList = false;
		bmiBean.setWeight(" ");
		bmiBean.setHeight(" ");
		bmiBean.setCategory(" ");
		bmiBean.setBmi(0);
		bmiBean.setBmiPrime(0);
		bmiBean.setErrorMessage("");
		return "SUCCESS";
	}
	
	public BmiBean getBmiBean() {
		return bmiBean;
	}

	public void setBmiBean(BmiBean bmiBean) {
		this.bmiBean = bmiBean;
	}

	public List<BmiBean> getBmiBeanList() {
		return bmiBeanList;
	}

	public void setBmiBeanList(List<BmiBean> bmiBeanList) {
		this.bmiBeanList = bmiBeanList;
	}

	public boolean isRenderList() {
		return renderList;
	}

	public void setRenderList(boolean renderList) {
		this.renderList = renderList;
	}
	
	public void save() {
		Iterator bmiIterator = bmiBeanList.iterator();
		while (bmiIterator.hasNext()) {
			BmiBean bmi = (BmiBean) bmiIterator.next();
				
			// save to database	
			String sqlQuery = "INSERT INTO bmiTable (bmiUser, units, weight, height, bmi, bmiPrime, bmiCategory) values ('" 
					+ bmi.getBmiUser() + "', '"+ bmi.getUnits() + "', " + bmi.getWeight() + ", " + bmi.getHeight() + ", " + bmi.getBmi() 
					+ ", " + bmi.getBmiPrime() + ", '" + bmi.getCategory() + "')"; 
			dbaseAccessBean.setQueryType("INSERT");
			dbaseAccessBean.executeSQL(sqlQuery);
		}
	}

	public DbaseAccessBean getDbaseAccessBean() {
		return dbaseAccessBean;
	}

	public void setDbaseAccessBean(DbaseAccessBean dbaseAccessBean) {
		this.dbaseAccessBean = dbaseAccessBean;
	}

}
