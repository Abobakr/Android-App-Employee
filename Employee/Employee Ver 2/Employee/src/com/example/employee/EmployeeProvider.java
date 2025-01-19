package com.example.employee;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class EmployeeProvider extends ContentProvider {

	private static final String CLASSNAME = EmployeeProvider.class
			.getSimpleName();
	private static final int EMPLOYEES_CODE = 1;
	private static final int EMPLOYEE_CODE = 2;

	public static final String DB_NAME = "employees_db";
	public static final String DB_TABLE = "employee";
	public static final int DB_VERSION = 3;

	private static final String LOGTAG = "Employees Provider Tag";

	private static HashMap<String, String> PROJECTION_MAP;

	public static SQLiteDatabase db;
	private static UriMatcher URI_MATCHER = null;

	static {
		EmployeeProvider.URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		EmployeeProvider.URI_MATCHER.addURI(Employee.AUTHORITY,
				Employee.PATH_MULTIPLE, EmployeeProvider.EMPLOYEES_CODE);
		EmployeeProvider.URI_MATCHER.addURI(Employee.AUTHORITY,
				Employee.PATH_SINGLE, EmployeeProvider.EMPLOYEE_CODE);

		EmployeeProvider.PROJECTION_MAP = new HashMap<String, String>();
		EmployeeProvider.PROJECTION_MAP.put(BaseColumns._ID, "_id");
		EmployeeProvider.PROJECTION_MAP.put(Employee.NAME, "Name");
		EmployeeProvider.PROJECTION_MAP.put(Employee.SURNAME, "Surname");
	}

	private static class DBOpenHelper extends SQLiteOpenHelper {

		private static final String DB_CREATE = "CREATE TABLE "
				+ EmployeeProvider.DB_TABLE + " (_id INTEGER PRIMARY KEY, "
				+ Employee.NAME + " TEXT NOT NULL, " + Employee.SURNAME
				+ " TEXT);";

		// private static final String DB_UPDATE = "";
		public DBOpenHelper(final Context context) {
			super(context, EmployeeProvider.DB_NAME, null,
					EmployeeProvider.DB_VERSION);
		}

		@Override
		public void onCreate(final SQLiteDatabase db) {
			Log.v(LOGTAG, EmployeeProvider.CLASSNAME
					+ " OpenHelper Creating database");
			try {
				db.execSQL(DBOpenHelper.DB_CREATE);
			} catch (SQLException e) {
				Log.e(LOGTAG, EmployeeProvider.CLASSNAME, e);
			}
		}

		@Override
		public void onOpen(final SQLiteDatabase db) {
			super.onOpen(db);
			Log.v(LOGTAG, EmployeeProvider.CLASSNAME
					+ " OpenHelper Opening database");
		}

		@Override
		public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
				final int newVersion) {
			Log.w(LOGTAG, EmployeeProvider.CLASSNAME
					+ " OpenHelper Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ " all data will be clobbered");
			db.execSQL("DROP TABLE IF EXISTS " + EmployeeProvider.DB_TABLE);
			this.onCreate(db);
		}
	}

	@Override
	public boolean onCreate() {
		DBOpenHelper dbHelper = new DBOpenHelper(getContext());
		db = dbHelper.getWritableDatabase();
		if (db == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		switch (EmployeeProvider.URI_MATCHER.match(uri)) {
		case EMPLOYEES_CODE:
			return Employee.MIME_TYPE_MULTIPLE;
		case EMPLOYEE_CODE:
			return Employee.MIME_TYPE_SINGLE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		String orderBy = null;

		switch (EmployeeProvider.URI_MATCHER.match(uri)) {
		case EMPLOYEES_CODE:
			queryBuilder.setTables(EmployeeProvider.DB_TABLE);
			queryBuilder.setProjectionMap(EmployeeProvider.PROJECTION_MAP);
			break;
		case EMPLOYEE_CODE:
			queryBuilder.setTables(EmployeeProvider.DB_TABLE);
			queryBuilder.appendWhere("_id=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = Employee.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		Cursor c = queryBuilder.query(db, projection, selection, selectionArgs,
				null, null, orderBy);
		return c;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		return 0;
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues initialValues) {
		long rowId = 0L;
		ContentValues values = null;

		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		if (EmployeeProvider.URI_MATCHER.match(uri) != EmployeeProvider.EMPLOYEES_CODE) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// default fields if not set
		if (!values.containsKey(Employee.NAME)) {
			values.put(Employee.NAME, "NA");
		}
		if (!values.containsKey(Employee.SURNAME)) {
			values.put(Employee.SURNAME, "NA");
		}

		rowId = db.insert(EmployeeProvider.DB_TABLE, "Employee_hack", values);

		if (rowId > -1) {// If not error.
			Uri result = ContentUris
					.withAppendedId(Employee.CONTENT_URI, rowId);
			return result;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}
}
