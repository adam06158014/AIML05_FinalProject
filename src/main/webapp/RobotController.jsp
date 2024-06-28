<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Proposal On-Call Delivery送貨機器人控制介面</title>
<link rel="stylesheet" href="./css/RobotController.css">
</head>
<body>
	<header>
		<!-- 頁首區塊登出按鈕 -->
		<div class="logout">
			<a href="#" onclick="logout()"><i class="fa-solid fa-user"></i>
				登出</a>
		</div>
	</header>
	<main>
		<h1>Proposal On-Call Delivery送貨機器人控制介面</h1>
		<hr>
		<!-- 送貨申請表單 -->
		<form action="RobotController" id="robotForm" method="POST">
			<label for="senderDepartment">寄件人部門:</label> <select
				id="senderDepartment" name="senderDepartment">
				<option value="">請選擇部門</option>
				<option value="A">[A] Finance</option>
				<option value="B">[B] Human resource</option>
				<option value="C">[C] Research and development</option>
				<option value="D">[D] Sale</option>
			</select> <label for="recipientDepartment">收件人部門:</label> <select
				id="recipientDepartment" name="recipientDepartment">
				<option value="">請選擇部門</option>
				<option value="A">[A] Finance</option>
				<option value="B">[B] Human resource</option>
				<option value="C">[C] Research and development</option>
				<option value="D">[D] Sale</option>
			</select>
			<button type="button" class="submit-btn"
				onclick="sendRobotCommand(event)">呼叫</button>
		</form>
		<!-- 控制機器人按鈕區域 -->
		<div class="control-buttons">
			<button onclick="sendRobotControl('forward')">Forward</button>
			<button onclick="sendRobotControl('stop')">Stop</button>
		</div>
		<!-- 配送歷史記錄區域 -->
		<section class="delivery-history">
			<h2>配送歷史記錄</h2>
			<div class="filter-options" onchange="filterTable()"></div>
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
		</section>
	</main>
	<script>
	filterTable()
	
    // 呼叫送貨機器人指令函數
    function sendRobotCommand(event) {
        event.preventDefault(); // 防止表单提交
        
        
        const senderDepartment = document.getElementById('senderDepartment').value;
        const recipientDepartment = document.getElementById('recipientDepartment').value;
        
        if (!senderDepartment || !recipientDepartment) {
            alert('請選擇寄件人部門和收件人部門');
            return;
        }
        
        fetch('./RobotController', {
            method: "POST",
            headers: new Headers({
                "Content-Type": "application/json",
            }),
            body: JSON.stringify({command: 'forward' ,senderDepartment, recipientDepartment })
        })
        .then(response => response.json())
        .then(data => {
            alert('成功呼叫後端: ' + data.message);
            loadDeliveryHistory();
        })
        .catch(error => console.error('呼叫後端錯誤:', error));
    }

    // 控制機器人移動指令函數
    function sendRobotControl(robotControl) {
        fetch('./RobotController', {
            method: "POST",
            headers: new Headers({
                "Content-Type": "application/json",
            }),
            body: JSON.stringify({ command: robotControl })
        })
        .then()
        .catch(error => console.error("錯誤:", error))
        .then(response => console.log("成功:", response));
    }

    
    // 加載配送歷史記錄函數
    function loadDeliveryHistory() {
    fetch('./History', {
        method: "GET",
        headers: new Headers({
            "Content-Type": "application/json",
        })
    })
    .then(response => response.json())
    .then(data => {
        // 將數據按時間從新到舊排序
        data.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));

        const tbody = document.getElementById('deliveryInfo');
        tbody.innerHTML = '';
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
    .catch(error => console.error('獲取配送歷史記錄錯誤:', error));
}
    
    // 篩選配送歷史記錄表格函數
    function filterTable(departmentId) {
    fetch('./History?departmentId=' + departmentId) // 直接使用傳遞進來的部門ID作為參數
        .then(response => response.json()) // 解析JSON格式的響應
        .then(data => {
            // 將數據按時間從新到舊排序
            data.sort((a, b) => new Date(b.sending_time) - new Date(a.sending_time));

            const tableBody = document.getElementById('deliveryInfo');
            tableBody.innerHTML = ''; // 清空表格內容

            // 遍歷返回的每條記錄，創建新的行並插入表格
            data.forEach(record => {
                const row = document.createElement('tr');

                // 寄件人部門列
                const senderDepartmentCell = document.createElement('td');
                senderDepartmentCell.textContent = record.sending_department_name;
                row.appendChild(senderDepartmentCell);

                // 收件人部門列
                const recipientDepartmentCell = document.createElement('td');
                recipientDepartmentCell.textContent = record.receiving_department_name;
                row.appendChild(recipientDepartmentCell);

                // 時間列
                const timeCell = document.createElement('td');
                timeCell.textContent = record.sending_time;
                row.appendChild(timeCell);

                // 將新行插入表格
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
}
 // 設置定時加載配送歷史記錄
    setInterval(filterTable, 5000);
    window.onload = filterTable;
    // 新增登出功能的函數
    function logout() {
        fetch('./LogoutServlet', {
            method: 'POST',
            headers: new Headers({
                'Content-Type': 'application/json',
            })
        })
        .then(response => {
            if (response.ok) {
            	window.location.href = 'index.jsp'; // 登出成功後重定向到登錄頁面
                alert('登出成功');
            }else {
                alert('登出失敗');
            }
        })
        .catch(error => console.error('登出錯誤:', error));
    }
</script>
</body>
</html>
