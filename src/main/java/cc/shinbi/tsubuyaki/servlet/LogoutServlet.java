package cc.shinbi.tsubuyaki.servlet;

import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cc.shinbi.tsubuyaki.model.Const;
import cc.shinbi.tsubuyaki.model.entity.User;

@WebServlet("/logout")
public class LogoutServlet extends JspServlet {

	private static final long serialVersionUID = 1L;

	public LogoutServlet() {
		super(true);
	}

	@Override
	protected String view(HttpServletRequest request, HttpServletResponse response, Connection connection,
			User loginUser) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute(Const.LOGIN_USER_KEY);

		String jsp = "/WEB-INF/jsp/login.jsp";
		return jsp;
	}
}
