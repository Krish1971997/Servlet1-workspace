package com.stu.todoapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.stu.todoapp.dto.DataTranfer_login;
import com.stu.todoapp.dto.DataTransfer;
import com.stu.todoapp.servlet.LogInUser;

public class DatabaseOperations {
	final static String DB_URl="jdbc:mysql://localhost:3306/java?user=root&password=root";
	final static String DRIVER_CLASS="com.mysql.cj.jdbc.Driver";
	static int rowNumber;
	
	public boolean insertUser(DataTranfer_login dt) {
		Connection con=null;
		PreparedStatement ps=null;
		try {
			Class.forName(DRIVER_CLASS);
			con=DriverManager.getConnection(DB_URl);
			String query="insert into todo_login values(?,?,?)";
			
			ps=con.prepareStatement(query);
			ps.setString(1, dt.getEmailID());
			ps.setString(2, dt.getPassword());
			ps.setString(3, dt.getName());
			
			int rowsAffected=ps.executeUpdate();
			if(rowsAffected!=0)
				return true;
			else
				return false;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
				try {
					if(con!=null)
					con.close();
					if(ps!=null)
						ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return false;
	}

	public boolean loginUser(String email, String password) {
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			Class.forName(DRIVER_CLASS);
			con=DriverManager.getConnection(DB_URl);
			String query="select *from todo_login where email_id=? and password=? ";
			ps=con.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
			rs=ps.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(con!=null)
				con.close();
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
		return false;
	}

	public ArrayList<DataTransfer> listOfTasks() {
		
		ArrayList<DataTransfer> al=new ArrayList<>();
		Connection con=null;
		Statement st=null;
		ResultSet rs=null;
		try {
			Class.forName(DRIVER_CLASS);
			con=DriverManager.getConnection(DB_URl);
			String query="select *from todoapp;";
			st=con.createStatement();
			rs=st.executeQuery(query);
			while(rs.next()) {
				int id=rs.getInt(1);
				String name=rs.getString(2);
				String task=rs.getString(3);
				String task_desc=rs.getString(4);
				String date=rs.getString(5);
				
				DataTransfer dt=new DataTransfer();
				dt.setID(id);
				dt.setName(name);
				dt.setTask(task);
				dt.setTask_discription(task_desc);
				dt.setDate(date);
				al.add(dt);
			}
				
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(con!=null)
				con.close();
				if(st!=null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
		return al;
	}

	public static int maxNumber() {
		Connection con=null;
		Statement st=null;
		ResultSet rs=null;
				try {
					Class.forName(DRIVER_CLASS);
					con=DriverManager.getConnection(DB_URl);
					String query="select max(id) from todoapp";
					st=con.createStatement();
					rs=st.executeQuery(query);
					if(rs.next())
						rowNumber=rs.getInt(1);
					
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				finally {
					try {
						if(con!=null)
						con.close();
						if(st!=null)
							st.close();
						if(rs!=null)
							rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return rowNumber;
	}
	
	public boolean insertNewTask(DataTransfer dt) {
		Connection con=null;
		PreparedStatement ps=null;
			try {
				Class.forName(DRIVER_CLASS);
				con=DriverManager.getConnection(DB_URl);
				String query="insert into todoapp values(?,?,?,?,?)";
				
				DateFormat dform = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
				Date obj = new Date();
				rowNumber=maxNumber()+1;
				
				ps=con.prepareStatement(query);
				ps.setInt(1, rowNumber);
				ps.setString(2, dt.getName());
				ps.setString(3, dt.getTask());
				ps.setString(4, dt.getTask_discription());
				ps.setString(5, dform.format(obj));
				
				int rowAffected = ps.executeUpdate();
				if (rowAffected != 0)
					return true;
				else
					return false;
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (con != null)
						con.close();
					if (ps != null) {
						ps.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		return false;
	} 
}
