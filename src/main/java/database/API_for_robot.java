package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class API_for_robot {
	public  static String findInformationsByEmployeeId(int employee_id) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("select * from employees where employee_id = ?");
		preState.setInt(1,employee_id);
		ResultSet rs = preState.executeQuery();
		if(rs.next()) {
			System.out.print(rs.getString(1)+",");
			System.out.print(rs.getString(2)+",");
			System.out.print(rs.getString(3)+",");
			System.out.print(rs.getString(4)+",");
			System.out.print(rs.getString(5)+",");
			System.out.println(rs.getString(6));
			return "This employee exists.";
		}
		
		// 加個判斷式，來判斷假如輸入的name在mysql裡找不到，則System.out.println("wrong employee");
		else{
			System.out.println("wrong employee_id");
		}
		return "This employee doesn't exists.";
	}
	public  static String findInformationsByEmployeeName(String employee_name) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("select * from employees where employee_name = ?");
		preState.setString(1,employee_name);
		ResultSet rs = preState.executeQuery();
		if(rs.next()) {
			System.out.print(rs.getString(1)+",");
			System.out.print(rs.getString(2)+",");
			System.out.print(rs.getString(3)+",");
			System.out.print(rs.getString(4)+",");
			System.out.print(rs.getString(5)+",");
			System.out.println(rs.getString(6));
			return "This employee exists.";
		}
		// 加個判斷式，來判斷假如輸入的name在mysql裡找不到，則System.out.println("wrong employee");
		else{
			System.out.println("wrong employee_name");
			return "This employee doesn't exists.";
		}
	}
	public  static String findInformationsByDepartmentId(int department_id) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("select * from departments where department_id = ?");
		preState.setInt(1,department_id);
		ResultSet rs = preState.executeQuery();
		if(rs.next()) {
			System.out.print(rs.getString(1)+",");
			System.out.println(rs.getString(2));
			return "This department exists.";
		}
		// 加個判斷式，來判斷假如輸入的name在mysql裡找不到，則System.out.println("wrong employee");
		else{
			System.out.println("wrong department_id");
			return "This department doesn't exists.";
		}
	}
	public  static String findAccountByEmployeeId(int employee_id) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("select employee_account from accounts_passwords where employee_id = ?");
		preState.setInt(1,employee_id);
		ResultSet rs = preState.executeQuery();
		if(rs.next()) {
			System.out.println(rs.getString(1));
			return rs.getString(1);
		}
		// 加個判斷式，來判斷假如輸入的name在mysql裡找不到，則System.out.println("wrong employee");
		else{
			System.out.println("wrong employee_id");
			return "Wrong employee_id";
		}
	}
	public  static String findPasswordByEmployeeId(int employee_id) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("select employee_password from accounts_passwords where employee_id = ?");
		preState.setInt(1,employee_id);
		ResultSet rs = preState.executeQuery();
		if(rs.next()) {
			System.out.println(rs.getString(1));
			return rs.getString(1);
		}
		// 加個判斷式，來判斷假如輸入的name在mysql裡找不到，則System.out.println("wrong employee");
		else{
			System.out.println("wrong employee_id");
			return "Wrong employee_id";
		}
	}
	public  static void insertInformationsIntoEmployees(int employee_id,String employee_name,int department_id,String position,String mail,String phone) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("insert into employees(employee_id,employee_name,department_id,position,mail,phone)"
				+ "values(?,?,?,?,?,?)");
		preState.setInt(1,employee_id);
		preState.setString(2,employee_name);
		preState.setInt(3,department_id);
		preState.setString(4,position);
		preState.setString(5,mail);
		preState.setString(6,phone);
		int rs = preState.executeUpdate();
		System.out.println("Inserted record with employee_id: " + employee_id + ", employee_name: " + employee_name + ",department_id: " + department_id + ",position: " + position + ", mail: " + mail + ", phone: " + phone);
		preState.close();
		conn.close();
	}
	public  static void insertInformationsIntoAccountsPasswords(int employee_id,String employee_account,String employee_password) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("insert into accounts_passwords(employee_id,employee_account,employee_password)"
				+ "values(?,?,?)");
		preState.setInt(1,employee_id);
		preState.setString(2,employee_account);
		preState.setString(3,employee_password);
		int rs = preState.executeUpdate();
		System.out.println("Inserted record with employee_id: " + employee_id + ", employee_account: " + employee_account + ", employee_password: " + employee_password );
		preState.close();
		conn.close();
	}
	public  static void insertInformationsIntoDepartments(int department_id,String department_name) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("insert into departments(department_id,department_name)"
				+ "values(?,?)");
		preState.setInt(1,department_id);
		preState.setString(2,department_name);
		int rs = preState.executeUpdate();
		System.out.println("Inserted record with department_id: " + department_id + ", department_name: " + department_name);
		preState.close();
		conn.close();
	}
	public  static String employeeAccountToCheckPassword(String employee_account) throws Exception{
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project_databases","root","P@ssw0rd");
		PreparedStatement preState = conn.prepareStatement("select employee_password from accounts_passwords\r\n"
				+ "join employees\r\n"
				+ "on accounts_passwords.employee_id = employees.employee_id\r\n"
				+ "where employee_account = ?");
		preState.setString(1,employee_account);
		ResultSet rs = preState.executeQuery();
		if(rs.next()) {
			System.out.println(rs.getString(1));
			return rs.getString(1);
		}
		// 加個判斷式，來判斷假如輸入的name在mysql裡找不到，則System.out.println("wrong employee");
		else{
			System.out.println("wrong employee_account");
			return "Wrong employee_account.";
		}
	}
	public static void main(String[] args) throws Exception{  
		
	}
}
