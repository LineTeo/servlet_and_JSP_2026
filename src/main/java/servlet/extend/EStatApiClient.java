package servlet.extend;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * e-Stat API (statsDataId=0003433219) に問い合わせ、
 * 指定した areaコードの総人口を返すクラス。
 */
public class EStatApiClient {

    private static final String APP_ID      = "11bdee7caad9dda40c4a69e112e5bbb14386d380";
    private static final String STATS_DATA_ID = "0003433219";
    private static final String BASE_URL    =
            "https://api.e-stat.go.jp/rest/3.0/app/getStatsData";

    /**
     * areaコードを受け取り、総人口を Optional<Long> で返す。
     * データなし・通信エラーの場合は Optional.empty()。
     *
     * @param areaCode 5桁の地域コード（例: "01100"）
     * @return 総人口
     */
    public Optional<Long> fetchPopulation(String areaCode) {
        try {
            // APIリクエストURL組み立て
            System.out.println("[EStatApiClient] リクエストするエリアコード : " + areaCode);
            
            String urlStr = BASE_URL
                    + "?appId="        + APP_ID
                    + "&statsDataId=" + STATS_DATA_ID
                    + "&metaGetFlg=N"
                    + "&cntGetFlg=N"
                    + "&cdArea="       + areaCode;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);

            int status = conn.getResponseCode();
            if (status != 200) {
                System.err.println("[EStatApiClient] HTTPエラー: " + status);
                return Optional.empty();
            }

            // XMLパース
            try (InputStream is = conn.getInputStream()) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(is);

                // <VALUE cat01="0" area="xxxxx" ...> の要素を取得
                NodeList values = doc.getElementsByTagName("VALUE");
                for (int i = 0; i < values.getLength(); i++) {
                    Element el = (Element) values.item(i);
                    // cat01="0" が総人口
                    if ("0".equals(el.getAttribute("cat01"))) {
                        String text = el.getTextContent().trim();
                        return Optional.of(Long.parseLong(text));
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("[EStatApiClient] エラー: " + e.getMessage());
        }

        return Optional.empty();
    }
}