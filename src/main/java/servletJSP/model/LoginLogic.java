package servletJSP.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginLogic {
    public boolean execute(User user) {
        // 保存先ファイルのパス（登録時に保存した場所と同じ）
		String dataPath = "/WEB-INF/recordData.csv";
        
        try (BufferedReader br = new BufferedReader(new FileReader(dataPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // ファイル内のデータを分割（ID,名前,パスワード の想定）
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String savedPass = data[0];
                    String savedId = data[2];
                    
                    // 入力された情報と一致するか判定
                    if (savedId.equals(user.getId()) && savedPass.equals(user.getPass())) {
                        return true; // 一致するユーザーが見つかった
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // 見つからなかった
    }
}