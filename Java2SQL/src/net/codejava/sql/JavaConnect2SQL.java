package net.codejava.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JavaConnect2SQL {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String url ="jdbc:sqlserver://cmsc495team03.eastus.cloudapp.azure.com;databaseName=Pulse";

		//String url ="jdbc:sqlserver://localhost;databaseName=Pulse";  //integratedSecurity=true
		String user = "sa";
		String password = "1234567890";
		try {
		Connection connection = DriverManager.getConnection(url,user,password);
				System.out.println("Connect to MS SQL Server on Azure. Good Job Dude.");
		} catch (SQLException e) {
			System.out.println("Oops, there's an error connecting to Azure");
			e.printStackTrace();}
		//test for local access

		String Localurl ="jdbc:sqlserver://localhost;databaseName=Pulse";  //integratedSecurity=true
		String Localuser = "sa";
		String Localpassword = "1234567890";
		try {
		Connection connection = DriverManager.getConnection(Localurl,Localuser,Localpassword);
				System.out.println("Connect to MS SQL Server on Local Host. Good Job Dude.");
		} catch (SQLException e) {
			System.out.println("Oops, there's an error connecting to the LocalHost");
			e.printStackTrace();}
	}

}
