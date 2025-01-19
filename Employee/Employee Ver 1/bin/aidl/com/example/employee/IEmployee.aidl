package com.example.employee;
import com.example.employee.Review;

interface IEmployee {	
	void addEmployee(in String Name,in String Surname);
	List<Review> getEmployees();
}
