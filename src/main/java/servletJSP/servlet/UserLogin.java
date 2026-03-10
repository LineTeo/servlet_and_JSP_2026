package servletJSP.servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import servletJSP.model.LoginLogic;
import servletJSP.model.RegisterUserLogic;
import servletJSP.model.User;

@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {
	  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    // フォワード先
		    String forwardPath = null;

		    // サーブレットクラスの動作を決定する「action」の値を
		    // リクエストパラメータから取得
		    String action = request.getParameter("action");

		    // 「ログイン画面」をリクエストされたときの処理
		    if (action == null) {
		      // フォワード先を設定
		      forwardPath = "WEB-INF/jsp/loginForm.jsp";  //ログイン画面
		    }
		    // 登録確認画面から「登録実行」をリクエストされたときの処理
		    else if (action.equals("done")) {
		      // セッションスコープに保存された登録ユーザ
		      HttpSession session = request.getSession();
		      User loginUser = (User) session.getAttribute("loginUser");

		      // 登録処理の呼び出し
		      RegisterUserLogic logic = new RegisterUserLogic();
		      logic.execute(loginUser,getServletContext());

		      // 不要となったセッションスコープ内のインスタンスを削除
		      session.removeAttribute("registerUser");

		      // 登録後のフォワード先を設定
		      forwardPath = "WEB-INF/jsp/registerDone.jsp";
		    }

		    // 設定されたフォワード先にフォワード
		    RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
		    dispatcher.forward(request, response);
	}
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String pass = request.getParameter("pass");

        // 入力情報からUserインスタンスを生成（名前は空でもOK）
        User user = new User(id, "", pass, "");
        
        // ログイン判定
        LoginLogic logic = new LoginLogic();
        User loginUser = logic.execute(user, getServletContext());

        if (loginUser != null) {
            // ログイン成功：セッションにユーザー情報を保存してメイン画面へ
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", loginUser);
		    RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/main.jsp");
		    dispatcher.forward(request, response);
        } else {
            // ログイン失敗：ログイン画面へリダイレクト
		    RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/loginForm.jsp");
		    dispatcher.forward(request, response);
        }
    }
}
