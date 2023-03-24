import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

	public static String getNowDate() {
		
		LocalDateTime now = LocalDateTime.now();
		
		String formatedNow = now.format(DateTimeFormatter.ofPattern("yy년 MM월dd일 HH시mm분ss초"));

		return formatedNow;
	}

}
