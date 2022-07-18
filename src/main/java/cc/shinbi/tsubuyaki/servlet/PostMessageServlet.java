package cc.shinbi.tsubuyaki.servlet;

import java.sql.Connection;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import cc.shinbi.tsubuyaki.model.Pair;
import cc.shinbi.tsubuyaki.model.dao.MessageDAO;
import cc.shinbi.tsubuyaki.model.entity.Message;
import cc.shinbi.tsubuyaki.model.entity.User;

@WebServlet("/postMessage")
@MultipartConfig
public class PostMessageServlet extends JspServlet {

	private static final long serialVersionUID = 1L;

	public PostMessageServlet() {
		super(true);
	}

	@Override
	protected String view(
	HttpServletRequest request,
	HttpServletResponse response,
	Connection connection,
	User loginUser

	) throws Exception {
	MessageDAO dao = new MessageDAO(connection);
	String error = "";
	int messageId = 0;
	try {
	messageId = Integer.parseInt(request.getParameter("id"));
	}
	catch(Exception e) {
	}
	String text = request.getParameter("text");
	boolean isPublic = Boolean.parseBoolean(request.getParameter("is_public"));
	if(text == null || text.isEmpty()) {
	error = "テキストを入力してください。";
	}
	if(error.isEmpty()) {
	Message message = null;
	if(messageId > 0) {
	message = dao.updateMessage(messageId, text, isPublic);
	}
	else {
	message = dao.addMessage(loginUser.getId(), text, isPublic);
	}
	this.setImage(request, dao, message);
	}
	List<Pair> pairs = dao.findVisible(loginUser.getId());
	request.setAttribute("pairs", pairs);
	request.setAttribute("error", error);
	return "/WEB-INF/jsp/top.jsp";
	}

	private void setImage(HttpServletRequest request, MessageDAO dao, Message entity) throws Exception {
		Part part = request.getPart("image_file");
		String fileName = part.getSubmittedFileName();
		boolean deleteFlag = Boolean.parseBoolean(request.getParameter("delete_image_flag"));
		if (fileName == null || fileName.isEmpty()) {
			if (deleteFlag) {
				dao.removeImage(entity.getId());
			}
		} else {
			dao.setImage(entity.getId(), fileName, part.getInputStream());
		}
	}
}
