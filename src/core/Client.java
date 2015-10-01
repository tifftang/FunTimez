package core;

public class Client {
	private String name;
	private String password;

	public Client(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public String getName() {
		return this.name;
	}
}
