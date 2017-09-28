package serikov.dmitrii.model;

/**
 *
 * @author Dmitrii Serikov
 */

public class UserList {
	public static final boolean CREATED_BY_USER = false;
	public static final boolean CREATED_BY_SERVER = true;
	private String title;
	private String content;
	private boolean createdByServer = true;

	public UserList(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public UserList(String title, String content, boolean createdByServer) {
		this.title = title;
		this.content = content;
		this.createdByServer = createdByServer;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isCreatedByServer() {
		return createdByServer;
	}

	public void setCreatedByServer(boolean createdByServer) {
		this.createdByServer = createdByServer;
	}
	
	
}
