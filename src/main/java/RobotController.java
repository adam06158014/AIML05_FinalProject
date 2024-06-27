
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
import java.text.SimpleDateFormat;
import database.API_for_robot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/RobotController")
public class RobotController extends HttpServlet {
	private static final long serialVersionUID = 1L;

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

		this.postToRobot("192.168.71.31", "5000", "/data", result.toString());

		// input to db
		// 按逗號分割字串
		String[] parts = result.toString().split(",");
		if (parts.length >= 2) {

			String sending_department_name = extractValue(parts[1]);
			String receiving_department_name = extractValue(parts[2]);

			// get current time
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sending_time = sdf.format(new Date());

			// 调用API_for_robot中的insertInformationsIntoHistory方法将信息存入数据库
			String insertResult = null;
			try {
				insertResult = API_for_robot.insertInformationsIntoHistory(sending_department_name,
						receiving_department_name, sending_time);
				System.out.println(insertResult);
			} catch (Exception e) {
				e.printStackTrace();
				insertResult = "Error: " + e.getMessage();
			}

			// 返回处理结果给客户端
			response.getWriter().write(insertResult);
		} else {
			response.getWriter().write("Error: Invalid input format");
		}
	}

	

	private String extractValue(String part) {
		// 去除无关字符，仅提取冒号后面的字母部分
		return part.trim().split(":")[1].replaceAll("[^A-Z]", "");
	}

	private void postToRobot(String ip, String port, String apiRoute, String robotCommand) {

		try {
			// Specify the new URL endpoint
			URL url = new URL("http://" + ip + ":" + port + apiRoute);

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
			String jsonInputString = robotCommand;

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
				System.out.println("Response Body: " + response_to_robot.toString());
			}

			// Disconnect the connection
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}