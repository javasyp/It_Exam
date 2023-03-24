
public class Article {
	int id;
	String regDate;
	String updateDate;
	String title;
	String body;
	String name;
	int hit;
	
	Article(int id, String regDate, String updateDate, String title, String body, String name, int hit) {
		this.id = id;
		this.regDate = regDate;
		this.updateDate = updateDate;
		this.title = title;
		this.body = body;
		this.name = name;
		this.hit = hit;
	}
}
