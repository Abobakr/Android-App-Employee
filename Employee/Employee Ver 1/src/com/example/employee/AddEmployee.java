package com.example.employee;

import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEmployee extends Activity {
	
	private IEmployee EmployeeInterface;
	private boolean bound;
	
	private EditText Name;
	private EditText Surname;
	private Button addEmpButton;
	
	private ServiceConnection connection =	new ServiceConnection() {
		@Override
		public void onServiceConnected (ComponentName name, IBinder service) {
			EmployeeInterface = IEmployee.Stub.asInterface(service);
			Toast.makeText(AddEmployee.this, "Connected to Service",Toast.LENGTH_SHORT).show();
			bound = true;
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			EmployeeInterface = null;
			Toast.makeText(AddEmployee.this, "Disconnected from Service",Toast.LENGTH_SHORT).show();
			bound = false;
		}
	};
	
	@Override
	public void onStart() {
		super.onStart();
		if (!this.bound) {
			bindService(new Intent(IEmployee.class.getName()),this.connection,Context.BIND_AUTO_CREATE);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (this.bound){
			bound = false;
			unbindService(connection);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_employee);
		
		this.Name =(EditText) findViewById(R.id.employeeName);
		this.Surname =(EditText) findViewById(R.id.employeeSurname);
		this.addEmpButton =(Button) findViewById(R.id.addEmpBottun);
		
		this.addEmpButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View view){
				saveEmployee();
			}
		});
	}
	
	public void saveEmployee() {
		if (!validate()) {
            return;
        }		
		final String EmpName = Name.getText().toString();
		final String EmpSurname = Surname.getText().toString();
		//new Thread() {
			//public void run() {
				try {
					EmployeeInterface.addEmployee(EmpName, EmpSurname);					
				}
				catch (DeadObjectException e) {
					showError(e.getMessage());
				}
				catch (RemoteException e) {
					showError(e.getMessage());
				}
				catch (Exception e) {
					showError(e.getMessage());						
				}					
			//}
		//}.start();
	}

	private boolean validate() {
		boolean valid = true;
        StringBuilder validationText = new StringBuilder();
        if ((this.Name.getText() == null) || this.Name.getText().toString().equals("")) {
            validationText.append("Name is not supplied");
            valid = false;
        }
        if ((this.Surname.getText() == null) || this.Surname.getText().toString().equals("")) {
            validationText.append("Sur Name is not Supplied");
            valid = false;
        }
        if (!valid) {
            showError(validationText.toString());
            validationText = null;
        }
        return valid;
	}
	private void showError(String Messege) {
		new AlertDialog.Builder(this).setTitle("Warning").setMessage(Messege).setPositiveButton("Continue",
				new android.content.DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1) {	}
				}
		).show();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, Menu.FIRST, 0, "Show Employee List");
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
            	Intent intent = new Intent("com.example.employee.VIEW_EMPLOYEE_LIST");
                startActivity(intent);
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}