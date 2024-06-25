import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.API_for_robot;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { // doPost一定要有HTML File

		// 設定回應型別與請求的字元編碼,並設定內容為html
		request.setCharacterEncoding("UTF-8"); // user輸入中文後呈現時不會亂碼
		response.setContentType("text/html;charset=UTF-8");

		// 取得輸出物件
		PrintWriter out = response.getWriter();

		// 取得user id & passwd
		String id = request.getParameter("id");
		String password = request.getParameter("password");

		// 調用外部API進行用戶驗證
		API_for_robot db = new API_for_robot();
		boolean isValidUser = false; // 設置初始值為false,默認假設user帳密無效

		try {
			String correct_ps = API_for_robot.findPasswordByEmployeeId(Integer.parseInt(id));

			if (correct_ps != null && correct_ps.equals(password)) {
				isValidUser = true;

			}
		} catch (Exception e) {

			e.printStackTrace(); // 捕捉並處理異常
		}

		// 判斷依據
		// if((rightid.equals(id) && rightpasswd.equals(password))) {
		if (isValidUser) {
			System.out.println("Login Successful");
			System.out.println("userid:" + id); // server
			System.out.println("userpasswd:" + password); // server

			// 調派請求,共享資料
			request.setAttribute("userid", id); // 將正確的id存入
			request.getRequestDispatcher("RobotController.jsp").forward(request, response); // 用jsp檔案的畫面呈現
			//out.write("<h3>Login Successful!<br> Redirecting to the other page...</h3>");
			//out.write("<meta http-equiv='refresh' content='2;URL=RobotController.jsp'>");
			// // 2s後跳轉至RobotController

		}

		else {
			System.out.println("Login Failed");
			System.out.println("userid:" + id); // server
			System.out.println("userpasswd:" + password); // server

			out.write("<h3>Please login again!</h3>"); // for user跳轉頁面
			out.write("<a href='index.jsp'>back to login page</a>"); // user能重回登入頁面
		}

		out.close();
	}
}

//音檔0415下午 From 00:40 (file:testresponse.java) 
//參考老師的file:TestLogin.java

//先手動設定正確的id & passwd--->到時要連資料庫核對
//	String rightid = "aaa";
//	String rightpasswd = "1234";