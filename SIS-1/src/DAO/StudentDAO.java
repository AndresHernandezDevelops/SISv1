package DAO;
import bean.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class StudentDAO {

	DataSource ds;
	StudentBean sb;
	//Statement stmn;
	
	public StudentDAO() throws ClassNotFoundException{
		
		try{
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
			//con = ds.getConnection();
			//con.createStatement();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public Map<String, StudentBean> retrieveStudent(String namePrefix, int credit_taken) throws SQLException{
		String query = "select * from students where surname like '%" + namePrefix + "%' and credit_taken >= " + credit_taken;
		Map<String, StudentBean> rv = new HashMap<String, StudentBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while(r.next())
		{
			String name = r.getString("GIVENNAME") + ", " + r.getString("SURNAME");
			String sid = r.getString("SID");
			int min_credit = r.getInt("CREDIT_TAKEN");
			int credit_graduate = r.getInt("CREDIT_GRADUATE");
			rv.put(sid,new StudentBean(sid, name, min_credit, credit_graduate));
		}
		r.close();
		p.close();
		con.close();
		return rv;
		
	}
}
