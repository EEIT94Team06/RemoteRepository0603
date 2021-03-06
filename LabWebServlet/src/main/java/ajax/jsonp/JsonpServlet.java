package ajax.jsonp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		urlPatterns={"/jsonp.view"}
)
public class JsonpServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String param = request.getParameter("param");
		String output = "the input is "+param;

		String callback = request.getParameter("callback");
		if(callback!=null && callback.length()!=0) {
			output = callback+"('"+output+"')";
		}
		
		System.out.println("output="+output);
		PrintWriter out = response.getWriter();
		out.write(output);
		out.close();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
