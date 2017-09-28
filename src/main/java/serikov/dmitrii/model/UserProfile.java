package serikov.dmitrii.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitrii Serikov
 */
public class UserProfile {
	private String username;
	private String password;
	private String avatar;
	private boolean loggedIn;
	private int timeout;

	private List<String> lists;

	//
	public UserProfile() {
	}

	public UserProfile(String username, String avatar) {
		this.username = username;
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void clearPassword() {
		this.password = "";
	}

	public String getAvatar() {
		return avatar;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public int getListsSize() {
		if (lists != null) {
			return lists.size();
		}
		return 0;
	}

	public List<String> getLists() {
		if (lists == null) {
			lists = new ArrayList<>();
		}
		return lists;
	}

	public void setListTitles(List<String> lists) {
		this.lists = lists;
	}

	public boolean removeList(String listName) {
		try {
			this.lists.remove(listName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}