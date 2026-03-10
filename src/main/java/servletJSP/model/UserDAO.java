package servletJSP.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletContext;

import com.opencsv.CSVReader;

public class UserDAO {
	

	String dataPath;
    File file; 	
	public UserDAO( ServletContext context) {
        this.dataPath = context.getRealPath("WEB-INF/recordData.csv");
        this.file = new File(dataPath);
	}

	public List<User> findAll() {

	    List<User> list = new ArrayList<>();

	    if (!file.exists()) {
	        return list;
	    }

	    try (CSVReader reader =
	            new CSVReader(
	                    new InputStreamReader(
	                            new FileInputStream(file),
	                            StandardCharsets.UTF_8))) {

	        List<String[]> rows = reader.readAll();

	        for (int i = 1; i < rows.size(); i++) {

	            String[] row = rows.get(i);

	            User user = new User(
	                    row[0],
	                    row[1],
	                    row[2],
	                    row[3]
	            );

	            list.add(user);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}
		

	public boolean insert(User user) {
		return false;
		
	}

	public boolean add(User user) {
		return false;
		
	}
	
	 // ID存在チェック
    public boolean exists(String id) {

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
	
	
	public User login(String id,String pass) {
		
        if (!file.exists()) {
            return null;
        }

        try (CSVReader reader =
                new CSVReader(
                        new InputStreamReader(
                                new FileInputStream(file),
                                StandardCharsets.UTF_8))) {

            List<String[]> list = reader.readAll();
            
            for (int i = 1; i < list.size(); i++) {
       
                String[] row = list.get(i);
                String savedId = row[0];
                String savedPass = row[1];
                String savedName = row[2];
                String savedProfile = row[3];
                    
                    // 入力された情報と一致するか判定
                    if (savedId.equals(id) && savedPass.equals(pass)) {
                        return new User(savedId, savedName, savedPass, savedProfile); // 一致するユーザーが見つかった
                    }
                }
            } catch (IOException e) {
            	e.printStackTrace();
            }
        return null; // 見つからなかった
	}
		
}
