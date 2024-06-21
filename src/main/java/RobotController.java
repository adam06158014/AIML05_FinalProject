import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/RobotController")
public class RobotController extends HttpServlet {
	private static final long serialVersionUID = 1L;

//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 獲取前端JSON資訊
		BufferedReader br = request.getReader(); // 讀取
		StringBuilder result = new StringBuilder();
		String line; // 儲存每次從BufferedReader讀取的行數據

		while ((line = br.readLine()) != null) { // 讀取到的行不為空時會一直循環
			result.append(line); // 拼接
		}

		System.out.println(result);
		br.close();
		

		try {
			// Specify the new URL endpoint
			URL url = new URL("http://192.168.24.138:5000/data");

			// Open a connection to the URL
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// Set the connection to allow output (POST request)
			conn.setDoOutput(true);

			// Set the request method to POST
			conn.setRequestMethod("POST");

			// Set the content type to JSON
			conn.setRequestProperty("Content-Type", "application/json");

			// JSON message to be sent
//		    String jsonInputString = "{\"key1\": \"value1\", \"key2\": \"value2\"}";
			String jsonInputString = result.toString();

			// Write the JSON string as the body of the request
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			// Check the response status
			int responseCode = conn.getResponseCode();
			System.out.println("Response Code: " + responseCode);

			// Read the response from the server
			try (BufferedReader br_to_robot = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response_to_robot = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br_to_robot.readLine()) != null) {
					response_to_robot.append(responseLine.trim());
				}
				System.out.println("Response Body: " + response.toString());
			}

			// Disconnect the connection
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// received Python
		// Set the content type of the response
		response.setContentType("text/html");

		// 獲取請求參數
		String paramValue = request.getParameter("paramName");

		// Read the request body if needed
		StringBuilder requestBody = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line1;

		while ((line1 = reader.readLine()) != null) {
			requestBody.append(line1);
		}
		reader.close();

		// server端印出從Python請求傳來的訊息
		System.out.println("Received message from Python: " + requestBody.toString());

		// response.getWriter().println("Received parameter: " + paramValue);
		// response.getWriter().println("Received message: " + requestBody.toString());
		
		// response to frontend
		response.setContentType("text/html");
		response.getWriter().println("<h3>Received,the car is moving!</h3>"); 

	}

}