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
			<a href="#"><i class="fa-solid fa-user"></i>登入</a>
		</div>
	</header>
	<h1>Proposal On-Call Delivery送貨機器人控制介面</h1>
	<hr>
	<!-- 收件人部門選擇 -->
	<label for="department">收件人部門:</label>
	<select id="department" name="department" onchange="loadRecipients()">
		<option value="">請選擇部門</option>
		<!-- 部門選項由Servlet填充 -->
		<c:forEach items="${departments}" var="department">
			<option value="${department.id}">${department.name}</option>
		</c:forEach>
	</select>
	<br>
	<br>
	<!-- 收件人姓名選擇 -->
	<label for="recipient">收件人姓名:</label>
	<select id="recipient" name="recipient">
		<option value="">請選擇姓名</option>
		<!-- 收件人選項將由JavaScript生成 -->
	</select>
	<br>
	<br>
	<!-- 呼叫按鈕 -->
	<button onclick="callBackend()">呼叫</button>
	<!-- 機器人控制按鈕 -->
	<button onclick="sendRobotCommand('forward')">Forward</button>
	<button onclick="sendRobotCommand('stop')">Stop</button>
	<script>
        // 當部門選擇改變時，加載相應的收件人
        function loadRecipients() {
            const department = document.getElementById('department').value;
            fetch(`/api/recipients?department=${department}`)
                .then(response => response.json())
                .then(data => {
                    const recipientSelect = document.getElementById('recipient');
                    recipientSelect.innerHTML = '';
                    data.forEach(recipient => {
                        const option = document.createElement('option');
                        option.value = recipient.id;
                        option.text = recipient.name;
                        recipientSelect.add(option);
                    });
                })
                .catch(error => console.error('Error fetching recipients:', error));
        }
        // 呼叫後端API，發送部門和收件人數據
        function callBackend() {
            const department = document.getElementById('department').value;
            const recipient = document.getElementById('recipient').value;
            fetch('請填入API連結', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ department, recipient })
            })
            .then(response => response.json())
            .then(data => {
                alert('成功呼叫後端: ' + data.message);
            })
            .catch(error => console.error('Error calling backend:', error));
        }
        // 發送機器人控制命令           
            function sendRobotCommand(robotCommand){

                fetch('./RobotController', {
                    method: "POST",
                    headers: new Headers({
                        "Content-Type": "application/json",
                    }),
                    body: robotCommand,
                })
                .then()
                .catch((error) => console.error("Error:", error))
                .then((response) => console.log("Success:", response));
            }

        </script>
</body>



</html>
