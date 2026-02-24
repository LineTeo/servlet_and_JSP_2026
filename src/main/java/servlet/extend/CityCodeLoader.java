package servlet.extend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletContext;

/**
 * citycode.csv を読み込み、市区町村名 → areaコード(5桁) の Map を生成するクラス。
 * CSVフォーマット: 市区町村名,areaコード
 */
public class CityCodeLoader {

    /**
     * クラスパス上の citycode.csv を読み込み Map を返す。
     * 読み込み失敗時は空の Map を返す。
     */
    public static Map<String, String> load(ServletContext context) {
        Map<String, String> map = new HashMap<>();
//        InputStream is = CityCodeLoader.class.getClassLoader().getResourceAsStream("citycode.csv");
        InputStream is;
        if (context != null) {
            is = context.getResourceAsStream("/WEB-INF/citycode.csv");
        } else {
            // テスト用フォールバック
            is = CityCodeLoader.class.getClassLoader()
                    .getResourceAsStream("citycode.csv");
        }
//        InputStream is = context.getResourceAsStream("/WEB-INF/citycode.csv");
        if (is == null) {
            System.err.println("[CityCodeLoader] citycode.csv が見つかりません。");
            return map;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // BOM除去（UTF-8 with BOM 対策）
                line = line.replace("\uFEFF", "");
                String[] values = line.split(",");
                if (values.length >= 2) {
                    String cityName = values[1].trim();	// csvファイルは　"コード,市町村名" となっているが、mapデータでは
                    String areaCode = values[0].trim();	// 市町村名をキーとするため、逆順で読み込んでいる
                    map.put(cityName, areaCode);
                }
            }
        } catch (IOException e) {
            System.err.println("[CityCodeLoader] 読み込みエラー: " + e.getMessage());
        }

        return map;
    }
}