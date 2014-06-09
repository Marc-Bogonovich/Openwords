package com.openwords.dto;

import java.sql.Date;

public class UserPerfDto {
	public int user_id;
	public int connection_id;
	public int total_correct;
	public int total_skipped;
	public int total_exposure;
	public int last_time;
	public int last_performance;
	public int user_exclude;
	
	public UserPerfDto()
	{
		this.user_id=0;
		this.connection_id=0;
		this.total_correct=0;
		this.total_skipped=0;
		this.total_exposure=0;
		this.last_time=0;
		this.last_performance=0;
		this.user_exclude=0;
	}
	
	
	public UserPerfDto(int user_id, int connection_id, int total_correct, int total_skipped,int total_exposure, int last_time, int last_performance, int user_exclude)
	{
		this.user_id=user_id;
		this.connection_id=connection_id;
		this.total_correct=total_correct;
		this.total_skipped=total_correct;
		this.total_exposure=total_exposure;
		this.last_time=last_time;
		this.last_performance=last_performance;
		this.user_exclude=user_exclude;
	}
}
