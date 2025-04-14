package org.example.domain;

public class Nutzer {

	private int id;
	private String Name;
	private int age;
	private String email;

	public Nutzer(int id, String name, int age, String email) {
		this.id = id;
		this.Name = name;
		this.age = age;
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return Name;
	}
	public int getAge() {
		return age;
	}
	public String getEmail() {
		return email;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setEmail(String email) {
		this.email = email;
	}


	public void kontoErstellen() {
		// TODO - implement Nutzer.kontoErstellen
		throw new UnsupportedOperationException();
	}

	public void kontoLoeschen() {
		// TODO - implement Nutzer.kontoLoeschen
		throw new UnsupportedOperationException();
	}

}