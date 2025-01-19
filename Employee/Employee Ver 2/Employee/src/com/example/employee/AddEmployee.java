package com.example.employee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEmployee extends Activity {

	private EditText Name;
	private EditText Surname;
	private Button addEmpButton;
	
	private BroadcastReceiver AddEmpBR=new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle res = intent.getExtras();
			int rowID = res.getInt("rowID");
			CharSequence text;
			if (rowID > 0) {
				text = "Employee No: " + rowID + " was added successfully.";
			} else {
				text = "Employee " + Name + " " + Surname + " was not added successfully.";
			}
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
		}
	};
	
	@Override protected void onResume() {
		super.onResume();
		IntentFilter MyIntentFilter=new IntentFilter(EmployeeService.ADD_EMP_BR);
		registerReceiver(AddEmpBR, MyIntentFilter);
	};
	
	@Override protected void onPause() {
		unregisterReceiver(AddEmpBR);
		super.onPause();		
	};
	
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
	
	private void saveEmployee() {
		if (!validate()) {
			return;
		}
		final String EmpName = Name.getText().toString();
		final String EmpSurname = Surname.getText().toString();

		Intent intent = new Intent(this, EmployeeService.class);
		Bundle b = new Bundle();
		b.putInt("Choice", 0);
		b.putString("Name", EmpName);
		b.putString("Surname", EmpSurname);
		intent.putExtras(b);
		startService(intent);		
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