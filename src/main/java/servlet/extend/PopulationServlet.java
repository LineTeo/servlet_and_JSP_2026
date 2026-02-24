package servlet.extend;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/population")
public class PopulationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /** 市区町村名 → areaコード のマップ（init()で一度だけ初期化） */
    private Map<String, String> cityCodeMap;

    /** e-Stat APIクライアント */
    private EStatApiClient apiClient;

    // -----------------------------------------------------------------------
    // 初期化
    // -----------------------------------------------------------------------
    @Override
    public void init() throws ServletException {
        cityCodeMap = CityCodeLoader.load(getServletContext());
        apiClient   = new EStatApiClient();

        if (cityCodeMap.isEmpty()) {
            // CSV読み込み失敗でもサーブレット自体は起動させる（検索時にエラー表示）
            System.err.println("[PopulationServlet] cityCodeMap が空です。citycode.csv を確認してください。");
        } else {
            System.out.println("[PopulationServlet] cityCodeMap 読み込み完了: " + cityCodeMap.size() + " 件");
        }
    }

    // -----------------------------------------------------------------------
    // GET: 検索フォームを表示
    // -----------------------------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='ja'><head><meta charset='UTF-8'>");
        out.println("<title>市区町村 人口検索</title></head><body>");
        out.println("<h2>市区町村 人口検索</h2>");
        out.println("<p>市区町村名を入力してください（例: 札幌市、那覇市）</p>");
        out.println("<form method='post' action='population'>");
        out.println("  <input type='text' name='cityName' size='20' placeholder='例: 札幌市'>");
        out.println("  <button type='submit'>検索</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    // -----------------------------------------------------------------------
    // POST: コード変換 → API問い合わせ → 人口表示
    // -----------------------------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String cityName = request.getParameter("cityName");

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='ja'><head><meta charset='UTF-8'>");
        out.println("<title>市区町村 人口検索 - 結果</title></head><body>");
        out.println("<h2>市区町村 人口検索</h2>");

        // --- 入力チェック ---
        if (cityName == null || cityName.trim().isEmpty()) {
            out.println("<p style='color:red;'>市区町村名を入力してください。</p>");
            printBackLink(out);
            out.println("</body></html>");
            return;
        }

        cityName = cityName.trim();

        // --- CSVマップからareaコードを取得 ---
        String areaCode = cityCodeMap.get(cityName);
        if (areaCode == null) {
            out.println("<p style='color:red;'>「" + escapeHtml(cityName) + "」に該当する市区町村が見つかりませんでした。</p>");
            out.println("<p>市区町村名は正式名称で入力してください（例: 札幌市、中央区 など）</p>");
            printBackLink(out);
            out.println("</body></html>");
            return;
        }

        // --- e-Stat API から人口を取得 ---
        Optional<Long> population = apiClient.fetchPopulation(areaCode);

        if (population.isEmpty()) {
            out.println("<p style='color:red;'>データの取得に失敗しました。しばらく後に再試行してください。</p>");
            printBackLink(out);
            out.println("</body></html>");
            return;
        }

        // --- 結果表示 ---
        // 人口を3桁カンマ区切りにフォーマット
        String formattedPop = NumberFormat.getNumberInstance(Locale.JAPAN).format(population.get());

        out.println("<h3>【" + escapeHtml(cityName) + "】の人口（令和２年国勢調査）</h3>");
        out.println("<p>総人口：<strong>" + formattedPop + " 人</strong></p>");
        printBackLink(out);
        out.println("</body></html>");
    }

    // -----------------------------------------------------------------------
    // ヘルパーメソッド
    // -----------------------------------------------------------------------

    /** 戻るリンクを出力する */
    private void printBackLink(PrintWriter out) {
        out.println("<br><a href='population'>← 検索に戻る</a>");
    }

    /** HTMLエスケープ（XSS対策） */
    private String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}