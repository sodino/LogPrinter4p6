package lab.sodino.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Sodino E-mail:sodino@qq.com
 * @version Time：2013年12月25日 下午3:22:29
 */
public class LogParser {
	private ArrayList<String> arrCode = new ArrayList<String>();
	/**文件的行数.*/
	private int lineNumber;
	private static String PRE = "param_FailCode=";
	public void parse(String path) {
		if(path == null){
			System.out.println("path[" + path +"] is null.");
			return;
		}
		
		File file = new File(path);
		if(file.exists() == false){
			System.out.println("path[" +path +"] does't exist.");
			return;
		}
		
		checkCode(file);
		int size = arrCode.size();
		for (int i = 0; i < size; i++) {
			filter(file, arrCode.get(i));
			System.out.println("-------->end<--------\n");
		}
	}
	
	private void filter(File file, String code){
		BufferedReader br = null;
		int lineCount = 0;
		String key = PRE + code;
		System.out.println(key);
		try{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
			String line = "";
			while ((line = br.readLine()) != null) {
				if(line.contains(key)){
					lineCount ++;
					System.out.println(line);
				}
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("amount:" + lineCount+" percent:" + (lineCount * 1.0f / this.lineNumber));
	}

	private void checkCode(File file) {
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
			String line = "";
			while((line = br.readLine()) != null){
				lineNumber ++;
				int idx = line.indexOf(PRE);
				if(idx > -1){
					int startIdx = idx + PRE.length();
					int endIdx = startIdx + 5;
					String code = line.substring(startIdx, endIdx);
					if(arrCode.contains(code) == false){
						arrCode.add(code);
					}
				}
			}
		}catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
