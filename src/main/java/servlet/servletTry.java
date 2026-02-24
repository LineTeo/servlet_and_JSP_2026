package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Yattemiru")
public class servletTry extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private List<DataBean> dataList = new ArrayList<>();

    @Override
    public void init() throws ServletException {  //サーブレットのインスタンスが生成されたときに1回だけ実施する親クラスのメソッド　ここでオーバーライド
//        InputStream is = getClass().getClassLoader().getResourceAsStream("data.csv");
    	InputStream is = getServletContext().getResourceAsStream("/WEB-INF/data.csv");
    	
        //デバッグメッセージ
//        System.out.println("InputStream: " + is);
        
        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length == 5) {
                        int dayNum = Integer.parseInt(values[1].trim());
                        DataBean bean = new DataBean(
                            values[0], dayNum, values[2], values[3], values[4]
                        );
                        dataList.add(bean);
                    }
                }
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GETリクエスト時はフォームを表示
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<p>2026/9/5～2026/2/20までの献立</p>");
        out.println("<form method='post' action='/example/Yattemiru'>");
        out.println("日付: <input type=\"date\" name=\"date\">"); 
//        out.println("日付: <input type='text' name='date' placeholder='例: 1月1日'>"); 
        out.println("<button type='submit'>検索</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String menuDate = request.getParameter("date");
//		デバッグ用        	
    	System.out.println(menuDate); 

        // datalistから日付にマッチするDataBeanを検索
        DataBean curData = null;
        for (DataBean bean : dataList) {
        	

//			デバッグ用        	
//        	System.out.println(bean); 
        	
        	if (bean.getDate().equals(menuDate)) {
                curData = bean;
                break;
            }
        }

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<p>2026/9/5～2026/2/20までの献立</p>");
        out.println("<form method='post' action='/example/Yattemiru'>");
        out.println("日付: <input type=\"date\" name=\"date\">"); 
        out.println("<button type='submit'>検索</button>");
        out.println("</form>");

        if (curData == null) {
            out.println("<p>該当するデータが見つかりませんでした。</p>");
        } else {
            out.println("<p>" + curData.getDate() + "(" + curData.getDayName() + ") の献立<br>");
            out.println("朝食　" + curData.getItem1() + "<br>");
            out.println("昼食　" + curData.getItem2() + "<br>"); 
            out.println("夕食　" + curData.getItem3() + "</p>"); 
        }

        out.println("</body></html>");
    }
}