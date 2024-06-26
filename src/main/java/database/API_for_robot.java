package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class API_for_robot {
	
	static final String IP = "jdbc:mysql://localhost:3306/project_databases";
	static final String ACCOUNT = "root";
	static final String PASSWORD = "P@ssw0rd";
	
	public  static String findInformationsByEmployeeId(int employee_id) throws Exception{
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
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
			return "This employee doesn't exists.";
		}
	}
	public  static String findInformationsByEmployeeName(String employee_name) throws Exception{
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
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
	public  static String findInformationsByDepartmentId(String department_id) throws Exception{
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
		PreparedStatement preState = conn.prepareStatement("select * from departments where department_id = ?");
		preState.setString(1,department_id);
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
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
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
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
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
	public  static String insertInformationsIntoEmployees(int employee_id,String employee_name,int department_id,String position,String mail,String phone) throws Exception{
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
		PreparedStatement preState = conn.prepareStatement("insert into employees(employee_id,employee_name,department_id,position,mail,phone)"
				+ "values(?,?,?,?,?,?)");
		preState.setInt(1,employee_id);
		preState.setString(2,employee_name);
		preState.setInt(3,department_id);
		preState.setString(4,position);
		preState.setString(5,mail);
		preState.setString(6,phone);
		int rs = preState.executeUpdate();
		if(rs >0) {
			System.out.println("Inserted record with employee_id: " + employee_id + ", employee_name: " + employee_name + ",department_id: " + department_id + ",position: " + position + ", mail: " + mail + ", phone: " + phone);
			return "successfully";
		}
		else
			return "error";	
	}
	public  static String insertInformationsIntoAccountsPasswords(int employee_id,String employee_account,String employee_password) throws Exception{
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
		PreparedStatement preState = conn.prepareStatement("insert into accounts_passwords(employee_id,employee_account,employee_password)"
				+ "values(?,?,?)");
		preState.setInt(1,employee_id);
		preState.setString(2,employee_account);
		preState.setString(3,employee_password);
		int rs = preState.executeUpdate();
		if(rs >0) {
			System.out.println("Inserted record with employee_id: " + employee_id + ", employee_account: " + employee_account + ", employee_password: " + employee_password );
			return "successfully";
		}
		else
			return "error";
	}
	public  static String insertInformationsIntoDepartments(String department_id,String department_name) throws Exception{
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
		PreparedStatement preState = conn.prepareStatement("insert into departments(department_id,department_name)"
				+ "values(?,?)");
		preState.setString(1,department_id);
		preState.setString(2,department_name);
		int rs = preState.executeUpdate();
		if(rs >0) {
			System.out.println("Inserted record with department_id: " + department_id + ", department_name: " + department_name);
			return "successfully";
		}
		else
			return "error";
	}
	public  static String employeeAccountToCheckPassword(String employee_account) throws Exception{
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
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
	public  static String insertInformationsIntoHistory(String sending_department_name,String receiving_department_name,String sending_time) throws Exception{
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
		PreparedStatement preState = conn.prepareStatement("insert into history(sending_department_name,receiving_department_name,sending_time)"
				+ "values(?,?,?)");
		preState.setString(1,sending_department_name);
		preState.setString(2,receiving_department_name);
		preState.setString(3,sending_time);
		int rs = preState.executeUpdate();
		if(rs>0) {
			System.out.println("Inserted record with sending_department_name: " + sending_department_name + ", receiving_department_name: " + receiving_department_name + ", sending_time:" + sending_time );
		return "successfully";
		}
		else
			return "error";
	}
	public  static ArrayList<String[]> findHistoryBySendingDepartmentName(String department_id) throws Exception{
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
		PreparedStatement preState = conn.prepareStatement("select sending_department_name,receiving_department_name,sending_time from history\n"
				+ "join departments\n"
				+ "on history.sending_department_name = departments.department_id\n"
				+ "where department_id = ?;");
		preState.setString(1,department_id);
		ResultSet rs = preState.executeQuery();
		ArrayList<String[]> historyList = new ArrayList<>();
	    while (rs.next()) {
	        String sendingDepartmentName = rs.getString("sending_department_name");
	        String receivingDepartmentName = rs.getString("receiving_department_name");
	        String sendingTime = rs.getString("sending_time");
	        String[] record = new String[3];
	        record[0] = sendingDepartmentName;
	        record[1] = receivingDepartmentName;
	        record[2] = sendingTime;
	        historyList.add(record);
	    }
	    
	    return historyList;
	}
	public  static ArrayList<String[]> findHistoryByReceivingDepartmentName(String department_id) throws Exception{
		Connection conn = DriverManager.getConnection(IP,ACCOUNT,PASSWORD);
		PreparedStatement preState = conn.prepareStatement("select sending_department_name,receiving_department_name,sending_time from history\n"
				+ "join departments\n"
				+ "on history.receiving_department_name = departments.department_id\n"
				+ "where department_id = ?;");
		preState.setString(1,department_id);
		ResultSet rs = preState.executeQuery();
		ArrayList<String[]> historyList = new ArrayList<>();
	    while (rs.next()) {
	        String sendingDepartmentName = rs.getString("sending_department_name");
	        String receivingDepartmentName = rs.getString("receiving_department_name");
	        String sendingTime = rs.getString("sending_time");
	        String[] record = new String[3];
	        record[0] = sendingDepartmentName;
	        record[1] = receivingDepartmentName;
	        record[2] = sendingTime;
	        historyList.add(record);
	    }
	    
	    return historyList;
	}
	public static void main(String[] args) throws Exception{  
		
	}
}
