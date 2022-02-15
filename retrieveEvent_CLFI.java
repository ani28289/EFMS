package CLFI_step4.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.ResponseEntity;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.JsonArray;
import oracle.jdbc.oracore.Util;

public class retrieveEvent_CLFI {

	List<List<String>> tableData;
	String CLFI;
	int facilityeventid = 0;
	String LastJeopCode;
	List <String> jsondata = new ArrayList<String>(); 
	List <String> jeopdata = new ArrayList<String>(); 
	List <String> dbbase = new ArrayList<String>(); 
	List <String> dbjeop = new ArrayList<String>(); 
	java.sql.Date CompleteDate;
	java.sql.Date ScheduledDate;
	java.sql.Date expecteddate;
	java.sql.Date statusupdateddate;
	java.sql.Date createddate;

	@Given("^Delete Event from FacilityEvent and corresponding Jeopardies for <salesOrderNumber>$")
	public void Delete_event_from_Facility_event_and_corresponding_jeopardies_for_salesOrderNumber(DataTable Table_1) throws ClassNotFoundException  {
		System.out.println("\n");
		System.out.println("Scenario 1 :");
		System.out.println("\n");
		System.out.println("Deletion Inpogress");

		System.out.println("\n");

		try{

			DB_Details_ST1_mSDB obj = new DB_Details_ST1_mSDB();
			Connection con = obj.dbDetails();
			PreparedStatement pstmtDelete = null;
			List<List<String>> data1 = Table_1.raw();
			System.out.println("\n");
			System.out.println("Deletion of records related to SalesOrderNumber :"+data1);
			System.out.println("\n");

			for(int r = 1;r<=5;r++)
			{
				String SalesOrderNumber=data1.get(r).get(0);
				System.out.println("SalesOrderNumber : "+SalesOrderNumber);
				pstmtDelete = con.prepareStatement("delete FACILITY_MILESTONE_JEOPARDY where FACILITY_MILESTONE_ID in (select FACILITY_MILESTONE_ID from FACILITY_MILESTONE where PARENT_ORDER_NUMBER=?)");
				pstmtDelete.setString(1,SalesOrderNumber); 
				pstmtDelete.executeUpdate();
				System.out.println("\n");

				System.out.println("Records are Deleted for the SalesOrderNumber in Jeop : "+SalesOrderNumber);

				System.out.println("\n");

				pstmtDelete = con.prepareStatement("delete FACILITY_MILESTONE where PARENT_ORDER_NUMBER=?");
				pstmtDelete.setString(1,SalesOrderNumber); 
				pstmtDelete.executeUpdate();
				System.out.println("Records are Deleted for the SalesOrderNumber in Facility Event : "+SalesOrderNumber);

				System.out.println("\n");

			}

		}
		catch(SQLException e)
		{
			System.out.println("Exception in Deletion: "+e);
			System.out.println("\n");
		}


	}

	@Given("^following FacilityEvent Exists and scheduled and some completed <eventName>, <salesOrderNumber>, <usoNumber>, <clfi>, <facilityOrderNumber>, <ecd>, <scheduledDate>, <completedDate> , <lastJeopCode>$")
	public void following_facility_Events_exists_and_scheduled_and_some_completed_eventName_salesOrderNumber_usoNumber_clfi_facilityOrderNumber_ecd_schedDate_completedDate_lastJeopCode(DataTable Table_2) throws ClassNotFoundException, ParseException  {

		List<List<String>> data2 = Table_2.raw();
		tableData = Table_2.raw();
		int r;

		java.sql.Date ECD, scheduleDate, CompletedDate;
		SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd") ;

		try

		{
			DB_Details_ST1_mSDB obj = new DB_Details_ST1_mSDB();
			Connection con = obj.dbDetails();
			PreparedStatement pstmtInsert = null;
			System.out.println("\n");
			System.out.println("Inserting Records:"+data2);

			int recCount=1;

			for(r=1;r<=16;r++)
			{


				String EventName =data2.get(r).get(0);
				String SalesOrderNumber= data2.get(r).get(1);
				String UsoNumber=data2.get(r).get(2);
				String Clfi=data2.get(r).get(3);
				String FacilityOrderNumber=data2.get(r).get(4);
				String ecd=data2.get(r).get(5);
				if(ecd.equals(""))
				{
					ECD = null;

				}
				else
				{
					java.util.Date date = a.parse(ecd);
					ECD = new java.sql.Date(date.getTime());

				}


				String ScheduleDate=data2.get(r).get(6);

				if(ScheduleDate.equals(""))
				{
					scheduleDate = null;
				}
				else
				{
					java.util.Date date1 = a.parse(ScheduleDate);
					scheduleDate = new java.sql.Date(date1.getTime());			
				}


				String completedDate=data2.get(r).get(7);

				if(completedDate.equals(""))
				{
					CompletedDate = null;
				}
				else
				{
					java.util.Date date2 = a.parse(completedDate);
					CompletedDate = new java.sql.Date(date2.getTime());			
				}

				String lastjeopcode=data2.get(r).get(8);

				if(lastjeopcode.equals(""))
				{
					LastJeopCode = null;
				}
				else
				{
					LastJeopCode = lastjeopcode;
				}

				//	System.out.println("Insertion inprogress");

				if (data2.get(r).get(0).equalsIgnoreCase("FACRID"))
				{
					int EventType=92;
					//	System.out.println("Insertion values : "+EventName+""+SalesOrderNumber+""+UsoNumber+""+Clfi+""+FacilityOrderNumber+""+ecd+""+ScheduleDate+""+completedDate);
					pstmtInsert = con.prepareStatement("insert into FACILITY_MILESTONE (FACILITY_MILESTONE_ID,PARENT_ORDER_NUMBER,ORDER_NUMBER,ORDER_NUMBER_TYPE,MILESTONE_NAME,MILESTONE_TYPE,INITIAL_SCHED_DATE,INITIAL_SCHED_STAMP,CURRENT_SCHED_DATE,CURRENT_SCHED_STAMP,EXPECTED_COMP_DATE,EXPECTED_COMP_STAMP,COMPLETED_DATE,COMPLETED_DATE_STAMP,CALLING_SYSTEM,UPDATED_BY,COMPLETED_BY,CREATED_BY,MILESTONE_NOTES,CLFI,FACILITY_ORDER_NUMBER,COMPLETED_BY_ID_TYPE,NISE_ORDER_NUMBER,ORDER_SOURCE_SYSTEM,LAST_JEOP_CODE) values (FACILITY_MILESTONE_ID.nextval,?,?,'USO',?,?,?,sysdate,?,sysdate,?,sysdate,?,sysdate,'Other',null,null,'tb750b','Testing',?,?,null,null,'System',?)");
					pstmtInsert.setString(1,SalesOrderNumber);
					pstmtInsert.setString(2,UsoNumber);
					pstmtInsert.setString(3,EventName);
					pstmtInsert.setInt(4,EventType);


					if(scheduleDate==null){

						pstmtInsert.setNull(5,java.sql.Types.DATE);
						pstmtInsert.setNull(6,java.sql.Types.DATE);
					}
					else
					{
						pstmtInsert.setDate(5,scheduleDate);
						pstmtInsert.setDate(6, scheduleDate);
					}



					if(ECD==null){

						pstmtInsert.setNull(7,java.sql.Types.DATE);

					}
					else
					{
						pstmtInsert.setDate(7,ECD); 
					}



					if(CompletedDate==null){

						pstmtInsert.setNull(8,java.sql.Types.DATE);

					}
					else
					{
						pstmtInsert.setDate(8,CompletedDate); 
					}

					pstmtInsert.setString(9,Clfi); 

					pstmtInsert.setString(10,FacilityOrderNumber); 

					pstmtInsert.setString(11,LastJeopCode); 

					pstmtInsert.executeUpdate();

					//	System.out.println(recCount);

					recCount++;

					//	System.out.println("FACRID Event Inserted");

				}

				if (data2.get(r).get(0).equalsIgnoreCase("FACIE"))
				{

					int EventType=162;
					//System.out.println("Insertion values : "+EventName+""+SalesOrderNumber+""+UsoNumber+""+Clfi+""+FacilityOrderNumber+""+ecd+""+ScheduleDate+""+completedDate);

					pstmtInsert = con.prepareStatement("insert into FACILITY_MILESTONE (FACILITY_MILESTONE_ID,PARENT_ORDER_NUMBER,ORDER_NUMBER,ORDER_NUMBER_TYPE,MILESTONE_NAME,MILESTONE_TYPE,INITIAL_SCHED_DATE,INITIAL_SCHED_STAMP,CURRENT_SCHED_DATE,CURRENT_SCHED_STAMP,EXPECTED_COMP_DATE,EXPECTED_COMP_STAMP,COMPLETED_DATE,COMPLETED_DATE_STAMP,CALLING_SYSTEM,UPDATED_BY,COMPLETED_BY,CREATED_BY,MILESTONE_NOTES,CLFI,FACILITY_ORDER_NUMBER,COMPLETED_BY_ID_TYPE,NISE_ORDER_NUMBER,ORDER_SOURCE_SYSTEM,LAST_JEOP_CODE) values (FACILITY_MILESTONE_ID.nextval,?,?,'USO',?,?,?,sysdate,?,sysdate,?,sysdate,?,sysdate,'Other',null,null,'tb750b','Testing',?,?,null,null,'System',?)");
					pstmtInsert.setString(1,SalesOrderNumber);
					pstmtInsert.setString(2,UsoNumber);
					pstmtInsert.setString(3,EventName);
					pstmtInsert.setInt(4,EventType);


					if(scheduleDate==null){

						pstmtInsert.setNull(5,java.sql.Types.DATE);
						pstmtInsert.setNull(6,java.sql.Types.DATE);
					}
					else
					{
						pstmtInsert.setDate(5,scheduleDate);
						pstmtInsert.setDate(6, scheduleDate);
					}



					if(ECD==null){

						pstmtInsert.setNull(7,java.sql.Types.DATE);

					}
					else
					{
						pstmtInsert.setDate(7,ECD); 
					}



					if(CompletedDate==null){

						pstmtInsert.setNull(8,java.sql.Types.DATE);

					}
					else
					{
						pstmtInsert.setDate(8,CompletedDate); 
					}

					pstmtInsert.setString(9,Clfi); 

					pstmtInsert.setString(10,FacilityOrderNumber); 

					pstmtInsert.setString(11,LastJeopCode); 

					pstmtInsert.executeUpdate();

					//	System.out.println(recCount);

					recCount++;

				}



			}
			System.out.println("\n");
			System.out.println("Insertion is done");
			System.out.println("\n");

		}

		catch(SQLException e)
		{

			System.out.println("Exception in insertion: "+e);

		}

	}



	@Given("^Jeopardy Exists for <eventName> and <facilityOrderNumber> For <jeopCode>, <description>, <note>, <status>, <statusUpdatedDate>, <CreatedDate>, <source> and <lastUpdatedBy>$")
	public void Jeopardy_Exists_for_eventName_and_facilityOrderNumber_for_jeopCode_description_note_status_statusUpdatedDate_CreatedDate_source_LastUpdatedBy(DataTable Table_3) throws ClassNotFoundException, ParseException  {

		List<List<String>> data3 = Table_3.raw();
		tableData = Table_3.raw();
		int r;

		java.sql.Date statusUpdatedDate,CreatedDate;
		SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd") ;

		try

		{
			DB_Details_ST1_mSDB obj = new DB_Details_ST1_mSDB();
			Connection con = obj.dbDetails();
			PreparedStatement pstmtInsert = null;
			PreparedStatement pstmtSelect = null;
			System.out.println("\n");
			System.out.println("Inserting Records:"+data3);

			int recCount=1;

			for(r=1;r<=3;r++)
			{

				String EventName =data3.get(r).get(0);
				String facilityOrderNumber= data3.get(r).get(1);
				String jeopCode=data3.get(r).get(2);
				String note=data3.get(r).get(4);

				String JeopNote;

				if(note.equals(""))
				{
					JeopNote = null;
				}
				else
				{
					JeopNote = note;
				}

				String Status=data3.get(r).get(5);

				String StatusUpdatedDate = data3.get(r).get(6);

				java.util.Date date = a.parse(StatusUpdatedDate);

				statusupdateddate = new java.sql.Date(date.getTime());


				String CreateDate = data3.get(r).get(7);

				java.util.Date date1 = a.parse(CreateDate);

				createddate = new java.sql.Date(date1.getTime());

				String source=data3.get(r).get(8);
				String lastUpdatedBy = data3.get(r).get(9);



				pstmtSelect = con.prepareStatement("select FACILITY_MILESTONE_ID from FACILITY_MILESTONE where FACILITY_ORDER_NUMBER = ? and  MILESTONE_NAME = ? and LAST_JEOP_CODE =?");

				pstmtSelect.setString(1,facilityOrderNumber);
				pstmtSelect.setString(2,EventName);
				pstmtSelect.setString(3,jeopCode);

				ResultSet rs1=pstmtSelect.executeQuery();

				while(rs1.next())
				{

					facilityeventid = rs1.getInt("FACILITY_MILESTONE_ID");	

				}

				pstmtInsert = con.prepareStatement("insert into FACILITY_MILESTONE_JEOPARDY (FACILITY_MILESTONE_JEOPARDY_ID,FACILITY_MILESTONE_ID,JEOPARDY_CODE,STATUS,STATUS_UPDATED_DATE,CREATED_DATE,SOURCE_SYSTEM,LAST_UPDATED_BY_SYSTEM,JEOPARDY_NOTES) values (FACILITY_MILESTONE_JEOPARDY_ID.nextval,?,?,?,?,?,?,?,?)");


				pstmtInsert.setInt(1,facilityeventid);

				pstmtInsert.setString(2,jeopCode);

				pstmtInsert.setString(3,Status);
				pstmtInsert.setDate(4,statusupdateddate);

				pstmtInsert.setDate(5,createddate);

				pstmtInsert.setString(6,source); 

				pstmtInsert.setString(7,lastUpdatedBy); 

				pstmtInsert.setString(8,JeopNote);



				pstmtInsert.executeUpdate();

				//	System.out.println(recCount);

				recCount++;

				//	System.out.println("FACRID Event Inserted");

			}

			System.out.println("\n");
			System.out.println("Insertion is done");
			System.out.println("\n");

		}

		catch(SQLException e)
		{

			System.out.println("Exception in insertion: "+e);

		}

	}


	@When("^retrieveEvent for <clfi>$")
	public void retrieveEvent_for_USO(DataTable Table_4)  {
		List<List<String>> data4 = Table_4.raw();

		System.out.println("Data used to retrieveEvent"+data4);
		System.out.println("\n");
		int r=1;
		CLFI= data4.get(r).get(0);
		System.out.println("CLFI is : "+CLFI);
		System.out.println("\n");

		try{
			String CurrentUrl="http://zlt16163.vci.att.com:30973/restservices/enterpriseOrderFacilityMilestoneTracker/v1/facilityMilestone?clfi="+CLFI;

			System.out.println(CurrentUrl);
			System.out.println("\n");
			RestGetMethod d = new RestGetMethod(); 

			ResponseEntity<?> response = d.restGetRequest(CurrentUrl);

			String Response = response.getBody().toString();


			System.out.println("GET response:"+Response); 
			System.out.println("\n"); 

			//JSONArray jarry = new JSONArray((String) response.getBody());

			JSONArray jarry = new JSONArray(Response);

			int i = jarry.length(); 

			JSONObject obj2=null; 
			JSONObject obj3=null; 
			JSONObject obj4=null; 
			JSONObject obj5=null; 
			JSONObject obj6=null; 
			JSONObject obj7=null; 
			JSONObject obj8=null; 
			JSONObject obj9=null; 
			JSONObject obj10=null; 
			JSONObject obj11=null; 
			JSONObject obj12=null; 
			JSONObject obj13=null; 
			JSONObject obj14=null; 
			JSONObject obj15=null; 
			JSONObject obj16=null; 
			JSONObject obj17=null; 
			JSONObject obj18=null; 
			JSONObject obj19=null; 
			JSONObject obj20=null; 
			JSONObject obj21=null; 

			String id2 =null; 
			String id3 =null; 
			String id4 =null; 
			String id5 =null; 
			String id6 =null; 
			String id7 =null; 
			String id8 =null; 
			String id9 =null; 
			String id10 =null; 
			String id11 =null; 
			String id12 =null; 
			String id13 =null; 
			String id14 =null; 
			String id15 =null; 
			String id16 =null; 
			String id17 =null; 
			String id18 =null; 
			String id19 =null; 
			String id20 =null; 
			String id21 =null; 

			for (int j=0;j<i ;j++) 
			{ 


				obj4 =(JSONObject)jarry.get(j); 
				String value4=obj4.getString("parentOrderNumber"); 
				id4=""+value4; 
				jsondata.add(id4); 

				obj5 =(JSONObject)jarry.get(j); 
				String value5=obj5.getString("orderNumber"); 
				id5=""+value5; 
				jsondata.add(id5); 

				obj6 =(JSONObject)jarry.get(j); 
				String value6=obj6.getString("milestoneName"); 
				id6=""+value6; 
				jsondata.add(id6); 

				/*	obj6 = (JSONObject)jarry.get(j); 
			String value6 = obj6.getString("initialScheduleDate"); 
			id6 =""+value6; 
			jsondata.add(id6); 
				 */ 

				try{ 
					obj7 =(JSONObject)jarry.get(j); 
					String value7=obj7.getString("clfi"); 
					id7=""+value7; 
					jsondata.add(id7); 
				} 
				catch(Exception e){ 

					jsondata.add("null"); 

				} 

				try{ 
					obj8 =(JSONObject)jarry.get(j); 
					String value8=obj8.getString("facilityOrderNumber"); 
					id8=""+value8; 
					jsondata.add(id8); 

				} 

				catch(Exception e){ 

					jsondata.add("null"); 

				} 

				try{ 
					obj10 =(JSONObject)jarry.get(j); 
					String value10=obj10.getString("lastJeopCode"); 
					id10=""+value10; 
					jsondata.add(id10); 

				} 

				catch(Exception e){ 

					jsondata.add("null"); 

				} 

				try{ 
					obj9 =(JSONObject)jarry.get(j); 
					String value9=obj9.getString("expectedCompletionDate"); 
					id9=""+value9; 
					jsondata.add(id9); 
				} 

				catch(Exception e){ 

					jsondata.add("null"); 

				} 
				try{ 
					obj15 =(JSONObject)jarry.get(j); 
					String value15=obj5.getString("currentScheduleDate"); 
					id15=""+value15; 
					jsondata.add(id15); 

				} 


				catch(Exception e){ 

					jsondata.add("null"); 

				} 
				try{ 
					obj16 = (JSONObject)jarry.get(j); 
					String value16 = obj16.getString("completedDate"); 
					id16 =""+value16; 
					jsondata.add(id16); 
				} 
				catch(Exception e){ 

					jsondata.add("null"); 

				} 


				obj17 = (JSONObject)jarry.get(j);


				if (obj17.has("jeopardy"))
				{

					JSONArray ja = obj17.getJSONArray("jeopardy");			

					int z=ja.length();

					for(int x=0;x<z;++x){

						JSONObject je = ja.getJSONObject(x);
						//

						try{ 

							String value11=je.getString("jeopardyCode"); 
							id11=""+value11; 
							jeopdata.add(id11); 

						} 

						catch(Exception e){ 


							jeopdata.add("null"); 

						} 

						try{ 

							String value12=je.getString("status"); 
							id12=""+value12; 
							jeopdata.add(id12); 

						} 

						catch(Exception e){ 

							jeopdata.add("null"); 

						} 

						try{ 

							String value13=je.getString("sourceSystem"); 
							id13=""+value13; 
							jeopdata.add(id13); 

						} 

						catch(Exception e){ 

							jeopdata.add("null"); 

						} 


						try{ 


							String value14=je.getString("lastUpdatedBySystem"); 
							id14=""+value14; 
							jeopdata.add(id14); 

						} 

						catch(Exception e){ 

							jeopdata.add("null"); 

						} 

						try{ 

							String value17=je.getString("jeopardyNotes"); 
							id17=""+value17; 
							jeopdata.add(id17); 

						} 

						catch(Exception e){ 

							jeopdata.add("null"); 

						} 	


						String value19=je.getString("createdDate"); 

						String year4= value19.substring(0,4);
						String month4= value19.substring(5,7);
						String date4= value19.substring(8,10);
						value19 = year4+"-"+month4+"-"+date4;
					//	System.out.println("createdDate without TimeStamp : "+value19);
						jeopdata.add(value19);	


						String value20=je.getString("statusUpdatedDate"); 
						String year1= value20.substring(0,4);
						String month1= value20.substring(5,7);
						String date1= value20.substring(8,10);
						value20 = year1+"-"+month1+"-"+date1;
						//System.out.println("createdDate without TimeStamp : "+value20);
						jeopdata.add(value20);	
					} 

				}

			}
		}

		catch(Exception e)

		{ 
			System.out.println(e); 
		}
	}
	@Then("^following FacilityEvents returned <eventName>, <salesOrderNumber>, <usoNumber>, <facilityOrderNumber>$")

	public void following_Facility_Events_returned_eventName_salesOrderNumber_USO_FacilityOrderNumber(DataTable Table_5) throws Throwable 

	{

		String ordernum = null;


		GetCLFIDB d = new GetCLFIDB(); 

		dbbase=d.DBData(CLFI); 
	

		System.out.println("\n");
	}

	@Then("^following Jeopardies returned for <eventName>, <facilityOrderNumber> and <jeopCode>$")
	public void following_jeopardies_returned_for_FACRID_facilityOrderNumber(DataTable Table_6) throws Throwable {

		{

			String ordernum = null;


			GetCLFIDB d = new GetCLFIDB(); 

			dbjeop=d.DBJEOP(CLFI); 

			System.out.println("DB data is : "+dbbase);
			System.out.println("GET response is : "+jsondata);
			System.out.println("GET JEOP response is : "+jeopdata);
			System.out.println("DB JEOP data is : "+dbjeop);

			System.out.println("\n");

			boolean testexecution=false; 

			if(dbbase.containsAll(jsondata)) 

			{

				if(jeopdata.containsAll(dbjeop)) 

				{

					System.out.println("Scenario 1 Passed"); 

					testexecution=true; 

				} 

				else

				{
					System.out.println("Assertion Failed");

				}
			}else

			{
				System.out.println("Primary Assertion Failed");

			}




			Assert.assertTrue(testexecution); 

		}



	}

}


