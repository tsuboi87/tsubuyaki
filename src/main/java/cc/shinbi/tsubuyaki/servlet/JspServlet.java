package cc.shinbi.tsubuyaki.servlet;

import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cc.shinbi.tsubuyaki.model.Const;
import cc.shinbi.tsubuyaki.model.entity.User;

public abstract class JspServlet extends DbServlet {
	private static final long serialVersionUID = 1L;
	private boolean checkLogin;
	
	public JspServlet(boolean checkLogin) {
		this.checkLogin = checkLogin;
	}
	
	@Override
	protected void action(
			HttpServletRequest request,
			HttpServletResponse response,
			Connection connection
	) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute(Const.LOGIN_USER_KEY);
		
		String jsp = null;
		if(this.checkLogin && user == null) {
			jsp = "/WEB-INF/jsp/login.jsp";
		}
		else {
			jsp = this.view(request, response, connection, user);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(jsp);
		dispatcher.forward(request, response);
	}
	
	protected abstract String view(
			HttpServletRequest request,
			HttpServletResponse response,
			Connection connection,
			User loginUser
	) throws Exception;
}