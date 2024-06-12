<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>送貨機器人控制介面</title>
<link rel="stylesheet" href="./css/index.css" />
</head>
<body>
	<div class="container">
		<h1>送貨機器人控制介面</h1>
		<!-- 表單開始 -->
		<form action="LoginServlet" id="robotForm" method="POST">
			<div class="form-group">
				<label for="id">工號</label> <input type="text" id="id" name="id"
					required>
				<!-- 工號輸入框 -->
			</div>
			<div class="form-group">
				<label for="password">密碼</label> <input type="password"
					id="password" name="password" required>
				<!-- 密碼輸入框 -->
			</div>
			<div class="buttons">
				<button type="submit" class="submit-btn">送出</button>
				<!-- 提交按鈕 -->
				<button type="button" class="reset-btn" onclick="resetForm()">清除</button>
				<!-- 重置按鈕 -->
			</div>
		</form>
		<div class="output" id="output"></div>
		<!-- 顯示輸出結果的區域 -->
	</div>
	<script>
		function resetForm() {
			document.getElementById('robotForm').reset(); // 重置表單
			document.getElementById('output').innerHTML = ''; // 清空顯示結果
		}
	</script>
</body>
</html>
