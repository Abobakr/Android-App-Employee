package com.example.employee;


import android.net.Uri;
import android.provider.BaseColumns;

public final class Employee implements BaseColumns {
	
	public static final String AUTHORITY = "com.example.employee";
    
	public static final String PATH_MULTIPLE = "employees_all";
	public static final String PATH_SINGLE = "employee_one";
    
    
    public static final Uri CONTENT_URI = Uri.parse("content://" + Employee.AUTHORITY + "/" + PATH_SINGLE);

    public static final String DEFAULT_SORT_ORDER = "_id DESC";
    
    public static final String NAME = "Name";//
    public static final String SURNAME = "Surname";   
}
