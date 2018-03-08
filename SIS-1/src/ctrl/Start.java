package ctrl;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.SIS;

/**
 * Servlet implementation class Start
 */
@WebServlet("/Start")
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	private SIS sis;
	
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String CREDITSTAKEN= "creditsTaken";
	private static final String CREDITSTOGRADUATE = "creditsToGraduate";
	private static final String CREDITSENDOFTERM = "creditsEndOfTerm";
	private static final String EMESSAGE = "errorMessage";
	
	private String id;
	private String name;
	private int creditsTaken;
	private int creditsToGraduate;
	private int creditsEndOfTerm;
	
	private String namePrefix;
	private String minimumCreditTaken;
	
	private String errorMessage = "";
	private boolean error = false;
	
	private boolean firstTime = true;
	
	public void init() throws ServletException{
		try {
			this.sis = new SIS();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /** 
     * Default constructor. 
     * @throws ServletException 
     */
    public Start() throws ServletException {
        // TODO Auto-generated constructor stub
    	this.init();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    private void serveJSP(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	String target = "/Form.jspx";
		String reportParameter = request.getParameter("report");
		
		if (reportParameter != null && reportParameter.equals("Report")) {
			firstTime = false;
			System.out.println("Just hit the report button");
			this.errorMessage = "";
			errorJSP();
			//write code to show the table and populate with results, using the firstTime variable to calculate results.
		}
		else
		{
			//the result table will stay hidden, might not need this else statement if the firstTime is used in the "queryTable" method 
		}
		request.getRequestDispatcher(target).forward(request, response);
    	
    }
    
    private void queryTables(HttpServletRequest request) {
    	String input_namePrefix = request.getParameter("namePrefix");
		String input_minimumCreditTaken = request.getParameter("minimumCreditTaken");
    	
		this.namePrefix = input_namePrefix;
		this.minimumCreditTaken = input_minimumCreditTaken;
		
		//int mct = Integer.parseInt(minimumCreditTaken); //not needed since SIS retrieveStudent will take care of the parsing
		if(firstTime==false){ //show the table
			try{
				//this section should be modified to get the set of results from retrieve methods (maps) and copy them onto the servlet context variavles (this.sid, etc)
				this.id = sis.retriveStudent(namePrefix, input_minimumCreditTaken).get("cse67895").getSid(); // just for testing purposes i want to return only a single element (any) from the map result set
				this.name = sis.retriveStudent(namePrefix, input_minimumCreditTaken).get("cse67895").getName();
				this.creditsTaken = sis.retriveStudent(namePrefix, input_minimumCreditTaken).get("cse67895").getCredit_taken();
				this.creditsToGraduate = sis.retriveStudent(namePrefix, input_minimumCreditTaken).get("cse67895").getCredit_graduate();
				this.creditsEndOfTerm = 0; //just for testing, need to find out how to calculate this
				
			}catch (Exception e){
				this.errorMessage = "Invalid Parameters";
				this.error = true;
				errorJSP();
			}
		}
		else
		{
			//testing purposes, return dummy strings and variables. 
			this.id = "null";
			this.name = "null";
			this.creditsTaken = 0;
			this.creditsToGraduate = 0;
			this.creditsEndOfTerm = 0; //just for testing,	
			//maintain the result table hidden
		}
    	
    }
    
    private void errorJSP() {
    	//this.getServletContext().setAttribute(EMESSAGE, this.errorMessage);
	}
    
    private void updateJSP(HttpServletRequest request) {
    	this.getServletContext().setAttribute(ID, this.id);
		this.getServletContext().setAttribute(NAME, this.name);
		this.getServletContext().setAttribute(CREDITSTAKEN, this.creditsTaken);
		this.getServletContext().setAttribute(CREDITSTOGRADUATE, this.creditsToGraduate);
		this.getServletContext().setAttribute(CREDITSENDOFTERM, this.creditsEndOfTerm);
    }

    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		//request.getRequestDispatcher("/Form.jspx").forward(request, response);
		
		queryTables(request);
		updateJSP(request);
		serveJSP(request, response);
		firstTime = false;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
