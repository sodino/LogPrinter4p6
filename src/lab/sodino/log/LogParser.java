package lab.sodino.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Sodino E-mail:sodino@qq.com
 * @version Time：2013年12月25日 下午3:22:29
 */
public class LogParser {
	private ArrayList<String> arrCode = new ArrayList<String>();
	/**文件的行数.*/
	private int lineNumber;
	private static String PRE = "param_FailCode=";
	private RandomAccessFile raFos = null;
	
	public void parse(String path) {
		if(path == null){
			logCat("path[" + path +"] is null.");
			return;
		}
		File file = new File(path);
		if(file.exists() == false){
			logCat("path[" +path +"] does't exist.");
			return;
		}
		
		setInputPath(file);
		checkCode(file);
		int size = arrCode.size();
		for (int i = 0; i < size; i++) {
			filter(file, arrCode.get(i));
			logCat("-------->end<--------\n");
		}
		
		if(raFos != null){
			try {
				raFos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void filter(File file, String code){
		BufferedReader br = null;
		int lineCount = 0;
		String key = PRE + code;
		logCat(key);
		try{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
			String line = "";
			while ((line = br.readLine()) != null) {
				if(line.contains(key)){
					lineCount ++;
					logCat(line);
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
		logCat("amount:" + lineCount+" percent:" + (lineCount * 1.0f / this.lineNumber));
	}

	private void checkCode(File file) {
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
			String line = "";
			while ((line = br.readLine()) != null) {
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
	
	public void setInputPath(File fileInput) {
		if(fileInput == null){
			return;
		}
		Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("hh_mm_ss");
        String subfix = "_"+dateFormat.format(date) + ".txt";
		File fileOutput = new File(fileInput.getParentFile().getAbsolutePath()+File.separator+(fileInput.getName().replace(".txt", subfix)));
		if(fileOutput.exists()){
			fileOutput.delete();
		}
		try {
			fileOutput.createNewFile();
			raFos = new RandomAccessFile(fileOutput, "rw");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void logCat(String content){
		if(content == null){
			return;
		}
		System.out.println(content);
		try {
			raFos.write((content + "\n").getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
