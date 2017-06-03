package ajax.product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import model.ProductBean;
import model.ProductService;

@WebServlet(
		urlPatterns={"/pages/product.view"}
)
public class ProductIdServlet extends HttpServlet {
	private ProductService productService;
	@Override
	public void init() throws ServletException {
		ServletContext application = this.getServletContext();
		ApplicationContext context = 
				WebApplicationContextUtils.getWebApplicationContext(application);
		productService = (ProductService) context.getBean("productService");
	}
	private static final long serialVersionUID = 1L;
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String method = request.getMethod();
		System.out.println("method="+method+", "+request.getRequestURI());
		
		String action = request.getParameter("action");
		System.out.println("action="+action);
		
		if("GET".equals(method)) {
			if("textText".equals(action)) {
				this.textText(request, response);
			} else if("textXml".equals(action)) {
				this.textXml(request, response);
			} else if("textJson".equals(action)) {
				this.textJson(request, response);
			} else {
				throw new ServletException("使用GET呼叫必須輸入action參數值：textText, textXml, textJson");				
			}
		} else if("POST".equals(method)) {
			if("textText".equals(action)) {
				this.textText(request, response);
			} else if("textXml".equals(action)) {
				this.textXml(request, response);
			} else if("textJson".equals(action)) {
				this.textJson(request, response);
			} else {
				this.jsonJson(request, response);				
			}
		}
	}
	private void textText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		StringBuilder output = new StringBuilder();
		String temp = request.getParameter("id");
		int id = 0;
		if(temp==null || temp.trim().length()==0) {
			output.append("ID是必要欄位");
		} else {
			try {
				id = Integer.parseInt(temp);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				output.append("ID必須是數字");
			}
		}
		if(output!=null && output.length()!=0) {
			out.print(output);
			out.close();
			return;
		}
		ProductBean bean = new ProductBean();
		bean.setId(id);
		List<ProductBean> result = productService.select(bean);
		if(result==null || result.isEmpty()) {
			output.append("ID不存在");
		} else {
			output.append("ID存在:");
			ProductBean data = result.get(0);
			output.append(data.getId()+",");
			output.append(data.getName()+",");
			output.append(data.getPrice()+",");
			output.append(data.getMake()+",");
			output.append(data.getExpire());
		}
		out.print(output);
		out.close();
		return;
	}
	private void textXml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml; charset=UTF-8");
		PrintWriter out = response.getWriter();

		StringBuilder output = new StringBuilder();
		output.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		output.append("<result>");
		
		String temp = request.getParameter("id");
		int id = 0;
		boolean flag = false;
		if(temp==null || temp.trim().length()==0) {
			output.append("<text>ID是必要欄位</text>");
			flag = true;
		} else {
			try {
				id = Integer.parseInt(temp);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				output.append("<text>ID必需是數字</text>");
				flag = true;
			}
		}
		if(flag==true) {
			output.append("<hasMoreData>false</hasMoreData>");
			output.append("</result>");
			
			out.print(output);
			out.close();
			return;
		}
		
		ProductBean bean = new ProductBean();
		bean.setId(id);
		List<ProductBean> result = productService.select(bean);

		if(result==null || result.isEmpty()) {
			output.append("<text>ID不存在</text>");
			output.append("<hasMoreData>false</hasMoreData>");
		} else {
			output.append("<text>ID存在</text>");
			output.append("<hasMoreData>true</hasMoreData>");
			
			ProductBean data = result.get(0);
			output.append("<id>"+data.getId()+"</id>");
			output.append("<name>"+data.getName()+"</name>");
			output.append("<price>"+data.getPrice()+"</price>");
			output.append("<make>"+data.getMake()+"</make>");
			output.append("<expire>"+data.getExpire()+"</expire>");
		}
		output.append("</result>");
		
		out.print(output);
		out.close();
		return;
	}
	private void textJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String temp = request.getParameter("id");
		this.writeJsonOutput(temp, response);
	}
	private void jsonJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = request.getInputStream();
		int b = is.read();
		while(b!=-1) {
			baos.write(b);
			b = is.read();
		}
		is.close();
		byte[] messageBody = baos.toByteArray();
		baos.close();
		
		String jsonString = new String(messageBody);
		JSONObject jsonInput = new JSONObject(jsonString);

		String action = jsonInput.getString("action");
		System.out.println("action="+action);
		if(action==null || !action.equals("jsonJson")) {
			throw new ServletException("使用POST呼叫必須輸入action參數值：textText, textXml, textJson, jsonJson");
		}
		String temp = jsonInput.getString("id");
		this.writeJsonOutput(temp, response);
	}
	private void writeJsonOutput(String temp, HttpServletResponse response) throws IOException{
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		StringBuilder output = new StringBuilder();
		int id = 0;
		if(temp==null || temp.trim().length()==0) {
			output.append("ID是必要欄位");
		} else {
			try {
				id = Integer.parseInt(temp);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				output.append("ID必需是數字");
			}
		}

		if(output!=null && output.length()!=0) {
			JSONObject obj = new JSONObject();
			obj.put("text", output.toString());
			obj.put("hasMoreData", false);
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(obj);
			out.write(jsonArray.toString());
			out.close();
			return;
		}
		
		ProductBean bean = new ProductBean();
		bean.setId(id);
		List<ProductBean> result = productService.select(bean);
		
		JSONArray jsonArray = new JSONArray();
		if(result==null || result.isEmpty()) {
			JSONObject obj = new JSONObject();
			obj.put("text", "ID不存在");
			obj.put("hasMoreData", false);
			jsonArray.put(obj);
		} else {
			JSONObject obj1 = new JSONObject();
			obj1.put("text", "ID存在");
			obj1.put("hasMoreData", true);
			jsonArray.put(obj1);
			
			ProductBean data = result.get(0);
			JSONObject obj2 = new JSONObject();
			obj2.put("id", data.getId());
			obj2.put("name", data.getName());
			obj2.put("price", data.getPrice());
			obj2.put("make", data.getMake().toString());
			obj2.put("expire", data.getExpire());
			jsonArray.put(obj2);
		}
		out.write(jsonArray.toString());
		out.close();
		return;
	}
}
