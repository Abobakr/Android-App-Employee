package com.example.employee;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class EmployeeListView extends ListActivity {

	private ReviewAdapter reviewAdapter;
	private ArrayList<Review> reviews;
	
	private BroadcastReceiver employeeListBR = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) { 
			
			reviews = intent.getParcelableArrayListExtra("reviews");
			if (reviews != null && reviews.size() > 0) {
				reviewAdapter = new ReviewAdapter(EmployeeListView.this, reviews);
				EmployeeListView.this.setListAdapter(reviewAdapter);
			} else { 
				Toast.makeText(EmployeeListView.this, "No Data Found",
						       Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_list_employee);
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}
	
	@Override
	protected void onPause() { 
		
		unregisterReceiver(employeeListBR);
		super.onPause();
	}
	
	@Override
	protected void onResume() { 
		
		super.onResume();
		IntentFilter inf = new IntentFilter(EmployeeService.EMPLOYEE_LIST_BR);
		registerReceiver(employeeListBR, inf);
		
		Intent intent = new Intent(this, EmployeeService.class);
		Bundle b = new Bundle();
		b.putInt("Choice", 1);
		intent.putExtras(b);
		startService(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu.FIRST, 0, "Add New Employee");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			Intent intent = new Intent(this, AddEmployee.class);
			startActivity(intent);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
