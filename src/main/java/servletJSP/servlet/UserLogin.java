package servletJSP.servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import servletJSP.model.User;
import servletJSP.model.UserDAO;

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

		      // 不要となったセッションスコープ内のインスタンスを削除
		      session.removeAttribute("registerUser");
		      session.removeAttribute("errResponse");

		      // ログイン画面へ
		      forwardPath = "WEB-INF/jsp/loginForm.jsp";
		    }

		    // 設定されたフォワード先にフォワード
		    RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
		    dispatcher.forward(request, response);
	}
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String pass = request.getParameter("pass");
                
        // ログイン判定
//        LoginLogic logic = new LoginLogic();
        UserDAO dao = new UserDAO(getServletContext());
//        User loginUser = logic.execute(user, getServletContext());
        User loginUser = dao.login(id, pass);
        HttpSession session = request.getSession();
        
        if (loginUser != null) {
            // ログイン成功：セッションにユーザー情報を保存してメイン画面へ
            session.setAttribute("loginUser", loginUser);
		    request.getRequestDispatcher("WEB-INF/jsp/main.jsp").forward(request, response);
        } else {
            // ログイン失敗：ログイン画面へリダイレクト
            session.setAttribute("errResponse", 1);
		    request.getRequestDispatcher("WEB-INF/jsp/loginForm.jsp").forward(request, response);
        }
    }
}
