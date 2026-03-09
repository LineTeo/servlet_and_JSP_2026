package servletJSP.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class RegisterUserLogic {
	public boolean execute(User user) {
		// 登録処理（サンプルでは登録処理を行わないので以下を追加）
		String dataPath = "/WEB-INF/recordData.csv";
		File file = new File(dataPath);
        
        // 親ディレクトリ（WEB-INFなど）を取得
        File parentDir = file.getParentFile();
        
        // 親ディレクトリが存在しない場合は作成する
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        
		try (PrintWriter pw = new PrintWriter(new FileWriter(dataPath,true))) {
			// ファイルが空（新規作成）の時だけヘッダーを書く
            if (file.length() == 0) {
            	pw.println("ログインID,パスワード,名前");}

            pw.printf("%s, %s, %s, %s\n",user.getId(),user.getPass(),user.getName(), user.getProfile());
		
		} catch (IOException e) {
			System.err.println("CSV書き込みエラー: " + e.getMessage());
		}    
		return true;
	}
}
