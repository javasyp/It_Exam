import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	static List<Article> articles = new ArrayList<>();
	static List<Member> members = new ArrayList<>();
	
	static Member loginedMember = null;
	
	public static void main(String[] args) {
		
		System.out.println("==프로그램 시작==");
		
		Scanner sc = new Scanner(System.in);
		
		makeTestData_A();
		makeTestData_M();
		
		int lastArticleId = 3;
		int lastMemberId = 3;
		
		while (true) {
			System.out.print("명령어 > ");
			String command = sc.nextLine().trim();
			
			// 로그인
			if (command.equals("member login")) {
				if (isLogined()) {
					System.out.println("로그아웃 후 이용해 주세요.");
					continue;
				}
				
				System.out.print("로그인 아이디 : ");
				String loginId = sc.nextLine();
				
				System.out.print("로그인 비밀번호 : ");
				String loginPw = sc.nextLine();
				
				loginedMember = getMember(loginId);
				
				if (loginedMember == null) {
					System.out.println("일치하는 회원이 없습니다.");
					continue;
				}
				
				System.out.println("로그인 성공! " + loginedMember.name + "님, 반갑습니다.");
				
			}
			
			// 로그아웃
			else if (command.equals("member logout")) {
				if (isLogined() == false) {		// 로그아웃 상태
					System.out.println("로그인 후 이용해 주세요.");
					continue;
				}
				
				loginedMember = null;
				
				System.out.println("로그아웃되었습니다.");
			
			}
			
			// 회원가입
			else if (command.equals("member join")) {
				if (isLogined()) {		// 로그인 중
					System.out.println("로그아웃 후 이용해 주세요.");
					continue;
				}
				
				int id = lastMemberId + 1;
				
				String loginId = null;
				String loginPw = null;
				
				while (true) {
					System.out.print("로그인 아이디 : ");
					loginId = sc.nextLine();
					
					if (isJoinableId(loginId) == false) {
						System.out.println("이미 사용 중인 아이디입니다.");
						continue;
					}
					
					break;
				}
				
				
				while (true) {
					
					System.out.print("로그인 비밀번호 : ");
					loginPw = sc.nextLine();
					
					System.out.print("로그인 비밀번호 확인 : ");
					String loginPwConfirm = sc.nextLine();
					
					if (loginPw.equals(loginPwConfirm) == false) {
						System.out.println("비밀번호가 일치하지 않습니다.");
						continue;
					}
					
					break;
				}
				
				System.out.print("이름 : ");
				String name = sc.nextLine();
				
				Member member = new Member(id, Util.getNowDate(), loginId, loginPw, name);
				members.add(member);
				
				System.out.println(id + "번 회원이 가입되었습니다.");
				
			}
			
			// 목록
			else if (command.equals("article list")) {
				
				System.out.println("번호  //  제목  //  조회  //  작성자");
				for (int i=articles.size()-1; i >= 0; i--) {
					Article article = articles.get(i);
					System.out.printf("%d   //  %s  // %d  // %s\n", article.id, article.title, article.hit, article.name);
				}
			}
			
			// 작성
			else if (command.equals("article write")) {
				if (isLogined() == false) {		// 로그아웃 상태
					System.out.println("로그인 후 이용해 주세요.");
					continue;
				}
				
				int id = lastArticleId + 1;
				
				System.out.print("제목 : ");
				String title = sc.nextLine();
				
				System.out.print("내용 : ");
				String body = sc.nextLine();
				
				Article article = new Article(id, Util.getNowDate(), Util.getNowDate(), title, body, loginedMember.name, 0);
				articles.add(article);
				
				System.out.println(id + "번 글이 생성되었습니다.");
			}
			
			// 세부사항
			else if (command.startsWith("article detail")) {
				
				String[] cmdDiv = command.split(" ");
				
				int id = Integer.parseInt(cmdDiv[2]);
				
				Article foundArticle = null; 
				
				for (Article article : articles) {
					if (id == article.id) {
						foundArticle = article;
						break;
					}
				}
				
				foundArticle.hit++;
				
				System.out.println("번호 : " + foundArticle.id);
				System.out.println("작성날짜 : " + foundArticle.regDate);
				System.out.println("수정날짜 : " + foundArticle.updateDate);
				System.out.println("작성자 : " + foundArticle.name);
				System.out.println("제목 : " + foundArticle.title);
				System.out.println("내용 : " + foundArticle.body);
				System.out.println("조회 : " + foundArticle.hit);
				
			}
			
			// 수정
			else if (command.startsWith("article modify")) {
				if (isLogined() == false) {		// 로그아웃 상태
					System.out.println("로그인 후 이용해 주세요.");
					continue;
				}
				
				String[] cmdDiv = command.split(" ");
				
				int id = Integer.parseInt(cmdDiv[2]);
				
				Article foundArticle = null;
				
				int i = 0;
				for (Article article : articles) {
					if (id == article.id) {
						foundArticle = article;
						break;
					}
					i++;
				}
				
				// 게시글 수정 시 권한 체크
				if (loginedMember.name != foundArticle.name) {
					System.out.println("권한이 없습니다.");
					continue;
				}
				
				System.out.print("새 제목 : ");
				String newTitle = sc.nextLine();
				
				System.out.print("새 내용 : ");
				String newBody = sc.nextLine();
				
				foundArticle.title = newTitle;
				foundArticle.body = newBody;
				foundArticle.updateDate = Util.getNowDate();
				System.out.println(id + "번 글을 수정했습니다.");
				
			}
			
			// 삭제
			else if (command.startsWith("article delete")) {
				if (isLogined() == false) {		// 로그아웃 상태
					System.out.println("로그인 후 이용해 주세요.");
					continue;
				}
				
				String[] cmdDiv = command.split(" ");
				
				int id = Integer.parseInt(cmdDiv[2]);
				
				Article foundArticle = null;
				
				int i = 0;
				for (Article article : articles) {
					if (id == article.id) {
						foundArticle = article;
						break;
					}
					i++;
				}
				
				// 게시글 삭제 시 권한 체크
				if (loginedMember.name != foundArticle.name) {
					System.out.println("권한이 없습니다.");
					continue;
				}
				
				articles.remove(foundArticle);
				System.out.println(id + "번 글을 삭제했습니다.");
				
			}
		}
	}
	
	// 로그인 상태 체크
	private static boolean isLogined() {
		return loginedMember != null;
	}
	
	
	private static Member getMember(String loginId) {
		int index = getMemberIndex(loginId);
		
		if (index == -1) {
			return null;
		}
				
		return members.get(index);
	}
	
	// 아이디 중복 검사
	private static boolean isJoinableId(String loginId) {
		int index = getMemberIndex(loginId);
		
		if (index == -1) {
			//System.out.println("사용 가능");
			return true;
		}
		return false;
	}
	
	private static int getMemberIndex(String loginId) {
		int i = 0;
		for (Member member : members) {
			member = members.get(i);
			if (member.loginId.equals(loginId) ) {
				return i;	// 있으면(중복)
			}
			i++;
		}
		return -1;	// 없으면(중복X)
	}

	private static void makeTestData_M() {
		System.out.println("테스트를 위한 게시글 데이터를 생성합니다.");
		articles.add(new Article(1, Util.getNowDate(), Util.getNowDate(), "제목1", "내용1", "김영희", 11));
		articles.add(new Article(2, Util.getNowDate(), Util.getNowDate(), "제목2", "내용2", "홍길동", 22));
		articles.add(new Article(3, Util.getNowDate(), Util.getNowDate(), "제목3", "내용3", "홍길순", 33));
	}

	private static void makeTestData_A() {
		System.out.println("테스트를 위한 회원 데이터를 생성합니다.");
		members.add(new Member(1, Util.getNowDate(), "test1", "test1", "김영희"));
		members.add(new Member(2, Util.getNowDate(), "test2", "test2", "홍길동"));
		members.add(new Member(3, Util.getNowDate(), "test3", "test3", "홍길순"));
		
	}
}