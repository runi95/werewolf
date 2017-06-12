package com.werewolf.entities;

import javax.persistence.*;

@Entity
public class NameDictionary {

	@SequenceGenerator(name="Emp_Gen", sequenceName="Emp_Seq")
	@Id @GeneratedValue(generator="Emp_Gen")
	private int id;
	
	@Column(name = "name")
	public String name;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
}
