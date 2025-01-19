package com.example.employee;

import java.util.List;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;


public class EmployeeService extends Service{
	
	EmployeeProvider employeeProvider;
	
	private final class EmployeeDBServer extends IEmployee.Stub {
		
		public void addEmployee(String Name, String Surname) throws RemoteException {			
		
			ContentValues initialValues=new ContentValues();
			initialValues.put("Name", Name);
			initialValues.put("Surname", Surname);
			
			Uri CONTENT_URI = Uri.parse("content://" + Employee.AUTHORITY + "/" + Employee.PATH_SINGLE);
			Uri UriResult= employeeProvider.insert(CONTENT_URI, initialValues);			
			
			String rowID= UriResult.getLastPathSegment();
			Message message = Message.obtain();
			Bundle bundle = new Bundle();
			bundle.putString("ROWID", rowID);
			message.setData(bundle);
			handler.sendMessage(message);
		}

		public List<Review> getEmployees() throws RemoteException {	
			Uri CONTENT_URI = Uri.parse("content://" + Employee.AUTHORITY + "/" + Employee.PATH_MULTIPLE);
			return employeeProvider.queryEmployees(CONTENT_URI, null, null, null, null);
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return new EmployeeDBServer();
	}
	
	 @Override
	    public void onCreate() {
		 employeeProvider = new EmployeeProvider();
	 }
	 
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            notifyFromHandler((String) msg.getData().get("ROWID"));
        }
    };
	
    private void notifyFromHandler(String rowID) {
    	Context context = getApplicationContext();
    	CharSequence text = "Employee No: "+rowID+" was added successfully.";
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    }  
}
