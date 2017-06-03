package ajax.photo;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		urlPatterns={"/pages/photo.view"},
		initParams = {
				@WebInitParam(name="defaultFile", value="/img/x.png"),
				@WebInitParam(name="tempdir", value="/img")
		}
)
public class PhotoServlet extends HttpServlet {
	private DetailService detailService;
	@Override
	public void init() throws ServletException {
		ServletContext application = this.getServletContext();

		String tempdirRealPath = application.getRealPath(getInitParameter("tempdir"));
		File tempdir = new File(tempdirRealPath);
		
		String defaultPhotoRealPath = application.getRealPath(getInitParameter("defaultFile"));
		File defaultPhotoFile = new File(defaultPhotoRealPath);
		this.detailService = new DetailService(tempdir, defaultPhotoFile);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String photoid = request.getParameter("photoid");
		byte[] photo = detailService.getPhotoByteArray(photoid);
		System.out.println("file path="+detailService.getPhotoPath(photoid));
		
		response.setContentType("image/png");
		OutputStream out = response.getOutputStream();
		out.write(photo);
		out.close();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
