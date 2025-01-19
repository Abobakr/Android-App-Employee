package com.example.employee;

import java.util.ArrayList;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

public class EmployeeService extends Service { 
	
	public static final String EMPLOYEE_LIST_BR = EmployeeService.class.getName() + ".employeeListBR";
	public static final String ADD_EMP_BR = EmployeeService.class.getName() + ".addEmpBR";
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Bundle b = intent.getExtras();
		int Choice = b.getInt("Choice");
		if (Choice == 0) {
			String Name = b.getString("Name");
			String Surname = b.getString("Surname");
			addEmployee(Name, Surname);
			//sendBroadcast(myIntent); is inside AddEmployee method
		} else if (Choice == 1) {
			ArrayList<Review> reviews = getEmployees();
			Intent myIntent = new Intent(EMPLOYEE_LIST_BR);
			myIntent.putParcelableArrayListExtra("reviews", reviews);
			sendBroadcast(myIntent);
		}
		return START_STICKY;
	}

	private void addEmployee(String Name, String Surname) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("Name", Name);
		initialValues.put("Surname", Surname);
		
		Uri UriResult = getContentResolver().insert(Employee.CONTENT_URI, initialValues);

		int rowID = Integer.parseInt(UriResult.getLastPathSegment());
		Bundle bundle = new Bundle();
		bundle.putInt("rowID", rowID);
		Intent myIntent = new Intent(ADD_EMP_BR);
		myIntent.putExtras(bundle);
		sendBroadcast(myIntent);
	}

	private ArrayList<Review> getEmployees() {
		Cursor cursor = getContentResolver().query(Employee.CONTENT_URI, null, null, null, null);
		ArrayList<Review> reviews = new ArrayList<Review>();
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex("_id"));
				String name = cursor.getString(cursor.getColumnIndex("Name"));
				String surname = cursor.getString(cursor
						.getColumnIndex("Surname"));
				reviews.add(new Review(id, name, surname));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return reviews;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
	}	
}
