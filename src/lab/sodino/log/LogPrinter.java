package lab.sodino.log;
/**
 * 指定要分析的文件,并按"param_FailCode"逐一输出。<br/>
 * 
 * @author Sodino E-mail:sodino@qq.com
 * @version Time：2013年12月25日 下午3:20:02
 */
public class LogPrinter {
	public static void main(String[]args){
		String path ="D:\\test\\15_03.txt";
		LogParser parser = new LogParser();
		parser.parse(path);
	}
}
