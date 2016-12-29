package cpm.com.gskmtorange.xmlGetterSetter;

import java.util.ArrayList;

public class JourneyPlanGetterSetter {
	
	String table_journey_plan;

	ArrayList<String> STORE_ID = new ArrayList<String>();
	ArrayList<String> EMP_ID = new ArrayList<String>();
	ArrayList<String> VISIT_DATE = new ArrayList<String>();
	ArrayList<String> KEYACCOUNT = new ArrayList<String>();
	ArrayList<String> STORE_NAME = new ArrayList<String>();
	ArrayList<String> CITY = new ArrayList<String>();
	ArrayList<String> STORETYPE = new ArrayList<String>();
	ArrayList<String> UPLOAD_STATUS = new ArrayList<String>();
	ArrayList<String> CHECKOUT_STATUS = new ArrayList<String>();
	ArrayList<String> GEO_TAG = new ArrayList<String>();

	ArrayList<String> ADDRESS = new ArrayList<String>();
	ArrayList<String> CLASSIFICATION = new ArrayList<String>();
	ArrayList<String> KEYACCOUNT_ID = new ArrayList<String>();
	ArrayList<String> STORETYPE_ID  = new ArrayList<String>();
	ArrayList<String> CLASS_ID  = new ArrayList<String>();
	ArrayList<String> CAMERA_ALLOW  = new ArrayList<String>();

	public ArrayList<String> getCHECKOUT_STATUS() {
		return CHECKOUT_STATUS;
	}
	public void setCHECKOUT_STATUS(String CHECKOUT_STATUS) {
		this.CHECKOUT_STATUS.add(CHECKOUT_STATUS);
	}
	public ArrayList<String> getVISIT_DATE() {
		return VISIT_DATE;
	}
	public void setVISIT_DATE(String vISIT_DATE) {
		this.VISIT_DATE.add(vISIT_DATE);
	}
	public ArrayList<String> getSTORE_ID() {
		return STORE_ID;
	}
	public void setSTORE_ID(String STORE_ID) {
		this.STORE_ID.add(STORE_ID);
	}
	public ArrayList<String> getEMP_ID() {
		return EMP_ID;
	}
	public void setEMP_ID(String EMP_ID) {
		this.EMP_ID.add(EMP_ID);
	}
	public ArrayList<String> getKEYACCOUNT() {
		return KEYACCOUNT;
	}
	public void setKEYACCOUNT(String KEYACCOUNT) {
		this.KEYACCOUNT.add(KEYACCOUNT);
	}
	public ArrayList<String> getSTORE_NAME() {
		return STORE_NAME;
	}
	public void setSTORE_NAME(String STORE_NAME) {
		this.STORE_NAME.add(STORE_NAME);
	}
	public ArrayList<String> getCITY() {
		return CITY;
	}
	public void setCITY(String CITY) {
		this.CITY.add(CITY);
	}
	public ArrayList<String> getSTORETYPE() {
		return STORETYPE;
	}
	public void setSTORETYPE(String STORETYPE) {
		this.STORETYPE.add(STORETYPE);
	}

	public ArrayList<String> getUPLOAD_STATUS() {
		return UPLOAD_STATUS;
	}
	public void setUPLOAD_STATUS(String UPLOAD_STATUS) {
		this.UPLOAD_STATUS.add(UPLOAD_STATUS);
	}
	public String getTable_journey_plan() {
		return table_journey_plan;
	}
	public void setTable_journey_plan(String table_journey_plan) {
		this.table_journey_plan = table_journey_plan;
	}

	public ArrayList<String> getCAMERA_ALLOW() {
		return CAMERA_ALLOW;
	}

	public void setCAMERA_ALLOW(String CAMERA_ALLOW) {
		this.CAMERA_ALLOW.add(CAMERA_ALLOW);
	}

	public ArrayList<String> getCLASS_ID() {
		return CLASS_ID;
	}

	public void setCLASS_ID(String CLASS_ID) {
		this.CLASS_ID.add(CLASS_ID);
	}

	public ArrayList<String> getSTORETYPE_ID() {
		return STORETYPE_ID;
	}

	public void setSTORETYPE_ID(String STORETYPE_ID) {
		this.STORETYPE_ID.add(STORETYPE_ID);
	}

	public ArrayList<String> getKEYACCOUNT_ID() {
		return KEYACCOUNT_ID;
	}

	public void setKEYACCOUNT_ID(String KEYACCOUNT_ID) {
		this.KEYACCOUNT_ID.add(KEYACCOUNT_ID);
	}

	public ArrayList<String> getCLASSIFICATION() {
		return CLASSIFICATION;
	}

	public void setCLASSIFICATION(String CLASSIFICATION) {
		this.CLASSIFICATION.add(CLASSIFICATION);
	}

	public ArrayList<String> getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String ADDRESS) {
		this.ADDRESS.add(ADDRESS);
	}

	public ArrayList<String> getGEO_TAG() {
		return GEO_TAG;
	}

	public void setGEO_TAG(String GEO_TAG) {
		this.GEO_TAG.add(GEO_TAG);
	}
}
