package ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.StudentBean;
import model.SIS;

/**
 * Servlet implementation class Start
 */
@WebServlet(urlPatterns = { "/Start", "/Startup", "/Startup/*", "/Start/*" })
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private SIS sis;
	
	Map<String, StudentBean> studentBeanList;
	
	private String namePrefix;
	private String minimumCreditTaken;
	
	public void init() throws ServletException{
		try {
			this.sis = new SIS();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
    /** 
     * Default constructor. 
     * @throws ServletException 
     */
    public Start() throws ServletException {
    	this.init();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    private void serveJSP(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
		String reportParameter = request.getParameter("submit");
		
		if (reportParameter != null && reportParameter.equals("true")) {
			System.out.println("Just hit the submit button");
			
			PrintWriter responseWriter = response.getWriter();
			try {
				Collection<StudentBean> sbean = studentBeanList.values();
				Iterator<StudentBean> studentIterator = sbean.iterator();
				responseWriter.println("<table border='1'>");
				responseWriter.println("<tr>");
				responseWriter.println("<td>Id</td>");
				responseWriter.println("<td>Name</td>");
				responseWriter.println("<td>Credits taken</td>");
				responseWriter.println("<td>Credits to graduate</td>");
				responseWriter.println("<td>Credits end of term</td>");
				responseWriter.println("</tr>");
				while (studentIterator.hasNext())
				{
					StudentBean item = studentIterator.next();
					String id = item.getSid();
					String name = item.getName();
					int creditsTaken = item.getCredit_taken();
					int creditsToGraduate = item.getCredit_graduate();
					int creditsEndOfTerm = item.getCredit_end_of_term();
					responseWriter.println("<tr color='red'>");
					responseWriter.print("<td color='red'>" + id + "</td>");
					responseWriter.print("<td>" + name + "</td>");
					responseWriter.print("<td>" + creditsTaken + "</td>");
					responseWriter.print("<td>" + creditsToGraduate + "</td>");
					responseWriter.print("<td>" + creditsEndOfTerm + "</td>");
					responseWriter.println("</tr>");
				}
				responseWriter.println("</table");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
    
    private void queryTables(HttpServletRequest request) { 	
    	this.namePrefix = request.getParameter("namePrefix");
		this.minimumCreditTaken = request.getParameter("minCred");
		
		try{
			studentBeanList = sis.retrieveStudent(this.namePrefix, this.minimumCreditTaken);
		}catch (Exception e){
			e.printStackTrace();
		}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String target = "/Form.jspx";
		request.getRequestDispatcher(target).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		queryTables(request);
		serveJSP(request, response);
	}

}
