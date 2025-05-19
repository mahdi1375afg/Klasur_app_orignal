package org.example.domain;

public class Nutzer {

	private int id;
	private String Name;
	private String password;

	public Nutzer(int id, String name, String password) {
		this.id = id;
		this.Name = name;
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return Name;
	}
	public String getPassword() {return password;}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.Name = name;
	}


	public void printNutzer() {
		System.out.println("Nutzer: " + this.Name + " " + this.password + " " + this.id);
	}

}