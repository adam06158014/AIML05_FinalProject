
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.API_for_robot;

@WebServlet("/History")
public class History extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String department_id = request.getParameter("department_id");
		// 使用ArrayList來存儲結果
        ArrayList<String[]> historyResult = new ArrayList<>();
        try {
            if (department_id != null && !department_id.isEmpty()) {
                historyResult = API_for_robot.findHistoryBySendingDepartmentName(department_id);
            } else {
                String[] departments = {"A", "B", "C", "D"};
                for (String dept : departments) {
                    historyResult.addAll(API_for_robot.findHistoryBySendingDepartmentName(dept));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        // 打印結果
        for(String[] record: historyResult) {
	        System.out.println(record[0]);
	        System.out.println(record[1]);
	        System.out.println(record[2]);
        }

        // 將結果轉換為JSON格式字符串
        String jsonResponse = convertToJSON(historyResult);

        // 設置響應類型和編碼
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 返回JSON數據
        response.getWriter().write(jsonResponse);
    }

    // 將歷史記錄數組轉換為JSON格式字符串
    public String convertToJSON(ArrayList<String[]> historyResult) {
        StringBuilder jsonResult = new StringBuilder();
        jsonResult.append("[");
        for (int i = 0; i < historyResult.size(); i++) {
            String[] record = historyResult.get(i);
            if (record != null) {
                if (i > 0) {
                    jsonResult.append(",");
                }
                jsonResult.append("{");
                jsonResult.append("\"sending_department_name\":\"").append(filterToUpperCase(record[0])).append("\",");
                jsonResult.append("\"receiving_department_name\":\"").append(filterToUpperCase(record[1])).append("\",");
                jsonResult.append("\"sending_time\":\"").append(record[2]).append("\"");
                jsonResult.append("}");
            }
        }
        jsonResult.append("]");
        return jsonResult.toString();
    }

    // 過濾非大寫字母的字符
    private String filterToUpperCase(String input) {
        if (input == null) {
            return "";
        }
        return input.replaceAll("[^A-Z]", "");
    }
}