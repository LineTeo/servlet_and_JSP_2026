package servletJSP.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import servletJSP.model.User;

@WebServlet("/Login")
public class UserLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String pass = request.getParameter("pass");

        // 入力情報からUserインスタンスを生成（名前は空でもOK）
        User user = new User(id, "", pass);
        
        // ログイン判定
        LoginLogic logic = new LoginLogic();
        boolean isSuccess = logic.execute(user);

        if (isSuccess) {
            // ログイン成功：セッションにユーザー情報を保存してメイン画面へ
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);
            response.sendRedirect("WEB-INF/jsp/main.jsp");
        } else {
            // ログイン失敗：ログイン画面へリダイレクト
            response.sendRedirect("login.jsp");
        }
    }
}
