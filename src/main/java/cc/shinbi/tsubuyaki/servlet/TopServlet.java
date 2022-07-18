package cc.shinbi.tsubuyaki.servlet;

import java.sql.Connection;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cc.shinbi.tsubuyaki.model.entity.User;

@WebServlet("/top")
public class TopServlet extends JspServlet {
	
	private static final long serialVersionUID = 1L;

	public TopServlet() {
		super(true);
	}
	
	@Override
	protected String view(
			HttpServletRequest request,
			HttpServletResponse response,
			Connection connection,
			User loginUser
	) throws Exception {
		String jsp = "/WEB-INF/jsp/top.jsp";
		return jsp;
	}
}
