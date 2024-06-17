<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Proposal On-Call Delivery送貨機器人控制介面</title>
<link rel="stylesheet" href="./css/RobotController.css">
</head>
<body>
<header>
    <nav>
        <ul>
            <li><a href="#">送貨申請</a></li>
            <li><a href="#">異常回報</a></li>
        </ul>
    </nav>
    <div class="logout">
        <a href="index.jsp"><i class="fa-solid fa-user"></i> 登出</a>
    </div>
</header>
<main>
    <h1>Proposal On-Call Delivery送貨機器人控制介面</h1>
    <hr>
    <form action="RobotController" id="robotForm" method="POST">
        <label for="senderDepartment">寄件人部門:</label>
        <select id="senderDepartment" name="senderDepartment">
            <option value="">請選擇部門</option>
            <option value="101">finance</option>
            <option value="102">Human resource</option>
            <option value="103">Research and development</option>
            <option value="104">Sale</option>
        </select>
        <label for="recipientDepartment">收件人部門:</label>
        <select id="recipientDepartment" name="recipientDepartment">
            <option value="">請選擇部門</option>
            <option value="101">finance</option>
            <option value="102">Human resource</option>
            <option value="103">Research and development</option>
            <option value="104">Sale</option>
        </select>
        <button type="button" class="submit-btn" onclick="sendRobotCommand()">呼叫</button>
    </form>
    <div class="control-buttons">
        <button onclick="sendRobotControl('forward')">Forward</button>
        <button onclick="sendRobotControl('stop')">Stop</button>
    </div>
    <section class="delivery-history">
        <h2>配送歷史記錄</h2>
        <div class="filter-options">
            <input type="text" id="searchInput" onkeyup="searchTable()" placeholder="搜索...">
            <select id="filterDepartment" onchange="filterTable()">
                <option value="">全部部門</option>
                <option value="finance">finance</option>
                <option value="Human resource">Human resource</option>
                <option value="Research and development">Research and development</option>
                <option value="Sale">Sale</option>
            </select>
        </div>
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
    function sendRobotCommand() {
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
            body: JSON.stringify({ senderDepartment, recipientDepartment })
        })
        .then(response => response.json())
        .then(data => {
            alert('成功呼叫後端: ' + data.message);
            loadDeliveryHistory();
        })
        .catch(error => console.error('Error calling backend:', error));
    }

    function sendRobotControl(robotControl) {
        fetch('./RobotController', {
            method: "POST",
            headers: new Headers({
                "Content-Type": "application/json",
            }),
            body: JSON.stringify({ command: robotControl })
        })
        .then()
        .catch(error => console.error("Error:", error))
        .then(response => console.log("Success:", response));
    }

    function loadDeliveryHistory() {
        fetch('./RobotController', {
            method: "GET",
            headers: new Headers({
                "Content-Type": "application/json",
            })
        })
        .then(response => response.json())
        .then(data => {
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
        .catch(error => console.error('Error fetching delivery history:', error));
    }

    function searchTable() {
        const input = document.getElementById('searchInput').value.toLowerCase();
        const table = document.getElementById('deliveryInfo');
        const rows = table.getElementsByTagName('tr');
        for (let i = 0; i < rows.length; i++) {
            const cells = rows[i].getElementsByTagName('td');
            let matched = false;
            for (let j = 0; j < cells.length; j++) {
                if (cells[j].innerText.toLowerCase().includes(input)) {
                    matched = true;
                    break;
                }
            }
            rows[i].style.display = matched ? '' : 'none';
        }
    }

    function filterTable() {
        const filter = document.getElementById('filterDepartment').value.toLowerCase();
        const table = document.getElementById('deliveryInfo');
        const rows = table.getElementsByTagName('tr');
        for (let i = 0; i < rows.length; i++) {
            const senderCell = rows[i].getElementsByTagName('td')[0];
            const recipientCell = rows[i].getElementsByTagName('td')[1];
            const senderDepartment = senderCell ? senderCell.innerText.toLowerCase() : '';
            const recipientDepartment = recipientCell ? recipientCell.innerText.toLowerCase() : '';
            if (filter === '' || senderDepartment.includes(filter) || recipientDepartment.includes(filter)) {
                rows[i].style.display = '';
            } else {
                rows[i].style.display = 'none';
            }
        }
    }

    setInterval(loadDeliveryHistory, 5000);
    window.onload = loadDeliveryHistory;
</script>
</body>
</html>
