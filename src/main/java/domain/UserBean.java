package domain;

/**
 * 用户实体类
 */
public class UserBean {

	private String name, password;
	private int authority;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAuthority() {
		return authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "UserBean{" +
				"name='" + name + '\'' +
				", password='" + password + '\'' +
				", authority=" + authority +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserBean userBean = (UserBean) o;

		return name != null ? name.equals(userBean.name) : userBean.name == null;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
