package servletJSP.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.servlet.ServletContext;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class RegisterUserLogic {

    public boolean execute(User user, ServletContext context) {

        try {

            String path = context.getRealPath("WEB-INF/recordData.csv");
            File file = new File(path);

            // ID重複チェック
            if (isIdExists(user.getId(), file)) {
                return false;
            }

            boolean isNewFile = !file.exists();

            try (CSVWriter writer =
                    new CSVWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(file, true),
                                    StandardCharsets.UTF_8))) {

                // 新規ファイルならヘッダー
                if (isNewFile) {
                    String[] header = {"ログインID","パスワード","名前","自己紹介"};
                    writer.writeNext(header);
                }

                String[] row = {
                        user.getId(),
                        user.getPass(),
                        user.getName(),
                        user.getProfile()
                };

                writer.writeNext(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // ID存在チェック
    private boolean isIdExists(String id, File file) {

        if (!file.exists()) {
            return false;
        }

        try (CSVReader reader =
                new CSVReader(
                        new InputStreamReader(
                                new FileInputStream(file),
                                StandardCharsets.UTF_8))) {

            List<String[]> list = reader.readAll();

            for (int i = 1; i < list.size(); i++) { // ヘッダー除外
                String[] row = list.get(i);

                if (row[0].equals(id)) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}