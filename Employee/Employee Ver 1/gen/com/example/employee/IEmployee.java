/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\Abubakr\\workspace\\Employee\\src\\com\\example\\employee\\IEmployee.aidl
 */
package com.example.employee;
public interface IEmployee extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.employee.IEmployee
{
private static final java.lang.String DESCRIPTOR = "com.example.employee.IEmployee";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.employee.IEmployee interface,
 * generating a proxy if needed.
 */
public static com.example.employee.IEmployee asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.employee.IEmployee))) {
return ((com.example.employee.IEmployee)iin);
}
return new com.example.employee.IEmployee.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_addEmployee:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.addEmployee(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getEmployees:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.example.employee.Review> _result = this.getEmployees();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.employee.IEmployee
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void addEmployee(java.lang.String Name, java.lang.String Surname) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(Name);
_data.writeString(Surname);
mRemote.transact(Stub.TRANSACTION_addEmployee, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.util.List<com.example.employee.Review> getEmployees() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.example.employee.Review> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEmployees, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.example.employee.Review.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_addEmployee = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getEmployees = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void addEmployee(java.lang.String Name, java.lang.String Surname) throws android.os.RemoteException;
public java.util.List<com.example.employee.Review> getEmployees() throws android.os.RemoteException;
}
