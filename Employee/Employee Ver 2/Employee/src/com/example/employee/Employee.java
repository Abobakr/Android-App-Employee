package com.example.employee;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Employee implements BaseColumns {
	
	public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
    public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
    public static final String MIME_ITEM = "vnd.msi.employee";
    public static final String MIME_TYPE_SINGLE = Employee.MIME_ITEM_PREFIX + "/" + Employee.MIME_ITEM;
    public static final String MIME_TYPE_MULTIPLE = Employee.MIME_DIR_PREFIX + "/" + Employee.MIME_ITEM;

	
	public static final String AUTHORITY = "com.example.employee";
    
	public static final String PATH_MULTIPLE = "employees";
	public static final String PATH_SINGLE = "employees/#";
    
    
    public static final Uri CONTENT_URI = Uri.parse("content://" + Employee.AUTHORITY + "/" + Employee.PATH_MULTIPLE);

    public static final String DEFAULT_SORT_ORDER = "_id DESC";
    
    public static final String NAME = "Name";//
    public static final String SURNAME = "Surname";   
}