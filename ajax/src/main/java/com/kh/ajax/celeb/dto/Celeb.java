package com.kh.ajax.celeb.dto;

public class Celeb {
	private int no;
	private String name;
	private CelebType type;
	private String profile; // 이미지 경로
	
	public Celeb() {
		super();
	}

	public Celeb(int no, String name, CelebType type, String profile) {
		super();
		this.no = no;
		this.name = name;
		this.type = type;
		this.profile = profile;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CelebType getType() {
		return type;
	}

	public void setType(CelebType type) {
		this.type = type;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {
		return no + ", " + name + ", " + type + ", " + profile;
	}
	
	
}
