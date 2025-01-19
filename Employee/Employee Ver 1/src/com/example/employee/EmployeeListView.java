package com.example.employee;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class EmployeeListView extends ListActivity {
	
    private ReviewAdapter reviewAdapter;
    private List<Review> reviews;
    
	private IEmployee EmployeeInterface;
	private boolean bound;

    private ServiceConnection connection = new ServiceConnection() {
    	@Override
    	public void onServiceConnected(ComponentName name, IBinder service) {
    		EmployeeInterface = IEmployee.Stub.asInterface(service);
    		Toast.makeText(EmployeeListView.this, "Connected to Service",
    		Toast.LENGTH_SHORT).show();
    		bound = true;
    		loadReviews();
    	}
    	@Override
    	public void onServiceDisconnected
    	(ComponentName name) {
    		EmployeeInterface = null;
    		Toast.makeText(EmployeeListView.this, "Disconnected from Service",
    		Toast.LENGTH_SHORT).show();
    		bound = false;
    	}
    };
    
    @Override
    public void onStart() {
    	super.onStart();
    	if (!this.bound) {
    		bindService(new Intent (IEmployee.class.getName()),this.connection,Context.BIND_AUTO_CREATE);
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
    protected void onResume() {
        super.onResume();
    }
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list_employee);
        
        final ListView listView = getListView();
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
            	Intent intent = new Intent(this,AddEmployee.class);
                startActivity(intent);
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
	
	private void loadReviews() {
        new Thread() {
            @Override
            public void run() {
            	try {
					reviews = EmployeeInterface.getEmployees();
				}
            	catch (RemoteException e) {
            		showError(e.getMessage());
				}
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
	
	private void showError(String Messege) {
		new AlertDialog.Builder(this).setTitle("Warning").setMessage(Messege).setPositiveButton("Continue",
				new android.content.DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int arg1) {	}
				}
		).show();
	}
	
	private final Handler handler = new Handler() {
    	@Override
        public void handleMessage(final Message msg) {
            if ((reviews == null) || (reviews.size() == 0)) {
            	Toast.makeText(EmployeeListView.this, "No Data Found",Toast.LENGTH_SHORT).show();            			
            } 
            else {
                reviewAdapter = new ReviewAdapter(EmployeeListView.this, reviews);
                setListAdapter(reviewAdapter);
            }
        }
    };   

}
