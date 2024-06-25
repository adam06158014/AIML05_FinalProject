

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check if it's a logout request
		String logout = request.getParameter("logout");
		if (logout != null && logout.equals("true")) {
			// Invalidate session if logout parameter is true
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			// Redirect to index.jsp after logout
			response.sendRedirect("index.jsp");
			return  ; // Exit doPost method after logout
		}

	}

}
