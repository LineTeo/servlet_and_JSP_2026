<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8" %>
<%@ page import="servletJSP.model.User" %>
<%
User registerUser = (User) session.getAttribute("registerUser");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー情報</title>
</head>
<body>
<p><%= registerUser.getName() %>さんの登録情報</p>
<p>
ログインID：<%= registerUser.getId() %><br>
パスワード：************<br>
自己紹介文：<%= registerUser.getProfile() %><br>
</p></p>
<a href="UserLogin?action=change">登録情報変更</a>
<a href="UserLogin?action=done">ログアウト</a>
</body>
</html>