package com.openwords.util;
public class LanguagePageTool {

	private String name;
	private int id;
	private boolean checked;
	
	public LanguagePageTool(String name, int id, boolean checked)
	{
		super();
		this.setName(name);
		this.setId(id);
		this.checked = checked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isSelected() {
		  return checked;
	}
	public void setSelected(boolean selected) {
		  this.checked = selected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
