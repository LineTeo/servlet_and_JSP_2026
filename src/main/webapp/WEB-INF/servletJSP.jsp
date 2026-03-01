<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="servlet.DataBean" %>
<%
    // サーブレットから渡されたデータを取得
    DataBean curData = (DataBean) request.getAttribute("curData");
    Boolean searched = (Boolean) request.getAttribute("searched");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>献立検索</title>
</head>
<body>
	<p>2026/9/5～2026/2/20までの献立</p>
	<form method='post' action='/example/Yattemiru'>
		日付: <input type="date" name="date">
		<button type='submit'>検索</button>
    </form>
<hr>

    <% if (searched != null && searched) { %>
        <% if (curData == null) { %>
            <p>該当するデータが見つかりませんでした。</p>
        <% } else { %>
            <p><%= curData.getDate() %>(<%= curData.getDayName() %>) の献立</p>
            <ul>
                <li>朝食：<%= curData.getItem1() %></li>
                <li>昼食：<%= curData.getItem2() %></li>
                <li>夕食：<%= curData.getItem3() %></li>
            </ul>
        <% } %>
    <% } %>
    </body>
</html>



