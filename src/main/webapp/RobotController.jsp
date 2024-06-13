<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Proposal On-Call Delivery送貨機器人控制介面</title>
<link rel="stylesheet" href="./css/RobotController.css" />
</head>

<body>

	<header>
		<nav>
			<ul>
				<li>送貨申請</li>
				<li>異常回報</li>
			</ul>
		</nav>
		<div>
			<a href="index.jsp"><i class="fa-solid fa-user"></i>登出</a>
		</div>
	</header>
	<h1>Proposal On-Call Delivery送貨機器人控制介面</h1>
	<hr>

	<!-- 收件人部門選擇 -->
	<form action="RobotController" id="robotForm" method="POST">
		<!-- 寄件人部門選擇 -->
		<label for="senderDepartment">寄件人部門:</label> <select
			id="senderDepartment" name="senderDepartment">
			<option value="">請選擇部門</option>
			<option value="101">finance</option>
			<option value="102">Human resource</option>
			<option value="103">Research and development</option>
			<option value="104">Sale</option>
		</select><br>
		<br>

		<!-- 收件人部門選擇 -->
		<label for="recipientDepartment">收件人部門:</label> <select
			id="recipientDepartment" name="recipientDepartment">
			<option value="">請選擇部門</option>
			<option value="101">finance</option>
			<option value="102">Human resource</option>
			<option value="103">Research and development</option>
			<option value="104">Sale</option>
		</select><br>
		<br>

		<!-- 呼叫按鈕 -->
		<button type="button" class="submit-btn" onclick="sendRobotCommand()">呼叫</button>
	</form>

	<!-- 顯示寄件人及收件人信息的表格 -->
	<h2>配送歷史記錄</h2>
	<table border="1">
		<thead>
			<tr>
				<th>寄件人部門</th>
				<th>收件人部門</th>
				<th>時間</th>
			</tr>
		</thead>
		<tbody id="deliveryInfo">
			<!-- 這裡將填充配送信息 -->
		</tbody>
	</table>

	<script>
        // 呼叫後端API，發送部門數據         
        function sendRobotCommand() {
            // 獲取寄件人部門和收件人部門的值
            const senderDepartment = document.getElementById('senderDepartment').value;
            const recipientDepartment = document.getElementById('recipientDepartment').value;

            // 檢查是否選擇了寄件人部門和收件人部門
            if (!senderDepartment || !recipientDepartment) {
                alert('請選擇寄件人部門和收件人部門');
                return;
            }
            
            // 發送HTTP請求到後端的RobotController
            fetch('./RobotController', {
                method: "POST", // 使用POST方法
                headers: new Headers({
                    "Content-Type": "application/json", // 設置請求頭，表示發送JSON數據
                }),
                body: JSON.stringify({ // 將寄件人部門和收件人部門轉換為JSON字符串
                    senderDepartment,
                    recipientDepartment
                })
            })
            .then(response => response.json()) // 將響應轉換為JSON
            .then(data => {
                alert('成功呼叫後端: ' + data.message); // 顯示後端返回的消息
                loadDeliveryHistory(); // 加載歷史記錄
            })
            .catch(error => console.error('Error calling backend:', error)); // 錯誤處理
        }

        // 從後端加載歷史記錄
        function loadDeliveryHistory() {
            fetch('./GetDeliveryHistory', {
                method: "GET", // 使用GET方法獲取歷史記錄
                headers: new Headers({
                    "Content-Type": "application/json", // 設置請求頭
                })
            })
            .then(response => response.json()) // 將響應轉換為JSON
            .then(data => {
                const tbody = document.getElementById('deliveryInfo');
                tbody.innerHTML = ''; // 清空表格

                // 將每條歷史記錄添加到表格中
                data.forEach(record => {
                    const row = `
                        <tr>
                            <td>${record.senderDepartment}</td>
                            <td>${record.recipientDepartment}</td>
                            <td>${record.timestamp}</td>
                        </tr>
                    `;
                    tbody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error fetching delivery history:', error)); // 錯誤處理
        }

        // 每5秒自動更新表格內容
        setInterval(loadDeliveryHistory, 5000);
        
        // 初始加載歷史記錄
        window.onload = loadDeliveryHistory;
    </script>
</body>
</html>
