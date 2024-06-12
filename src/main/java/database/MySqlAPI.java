package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MySqlAPI {
	public  static void find_informations_by_employee_name(String name) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("select * from employees where employee_name = ?");
		preState.setString(1,name);
		ResultSet rs = preState.executeQuery();
		if(rs.next()) {
			System.out.print(rs.getString(1)+",");
			System.out.print(rs.getString(2)+",");
			System.out.print(rs.getString(3)+",");
			System.out.print(rs.getString(4)+",");
			System.out.print(rs.getString(5)+",");
			System.out.println(rs.getString(6));
		}
		// 加個判斷式，來判斷假如輸入的name在mysql裡找不到，則System.out.println("wrong employee");
		else{
			System.out.println("wrong employee");
		}
		rs.close();
		preState.close();
		conn.close();
	}
	public  static void find_informations_by_employee_id(int id) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("select * from employees where employee_id = ?");
		preState.setInt(1,id);
		ResultSet rs = preState.executeQuery();
		if(rs.next()) {
			System.out.print(rs.getString(1)+",");
			System.out.print(rs.getString(2)+",");
			System.out.print(rs.getString(3)+",");
			System.out.print(rs.getString(4)+",");
			System.out.print(rs.getString(5)+",");
			System.out.println(rs.getString(6));
		}
		// 加個判斷式，來判斷假如輸入的name在mysql裡找不到，則System.out.println("wrong employee");
		else{
			System.out.println("wrong id");
		}
		rs.close();
		preState.close();
		conn.close();
	}
	public  static void insert_informations_into_employees(int id,String employee_name,String mail,String phone) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("insert into employees(employee_id,employee_name,mail,phone)"
				+ "values(?,?,?,?)");
		preState.setInt(1,id);
		preState.setString(2,employee_name);
		preState.setString(3,mail);
		preState.setString(4,phone);
		int rs = preState.executeUpdate();
		System.out.println("Inserted record with ID: " + id + ", Name: " + employee_name + ", Mail: " + mail + ", Phone: " + phone);
		preState.close();
		conn.close();
	}
	public  static void employee_account_to_check_password(String employee_account) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("select employee_password from accounts_passwords\r\n"
				+ "join employees\r\n"
				+ "on accounts_passwords.employee_id = employees.employee_id\r\n"
				+ "where employee_account = ?");
		preState.setString(1,employee_account);
		ResultSet rs = preState.executeQuery();
		if(rs.next()) {
			System.out.print(rs.getString(1));
		}
		// 加個判斷式，來判斷假如輸入的name在mysql裡找不到，則System.out.println("wrong employee");
		else{
			System.out.println("wrong employee_account");
		}
		rs.close();
		preState.close();
		conn.close();
	}
	public static void main(String[] args) throws Exception{ 
		
	}
}
