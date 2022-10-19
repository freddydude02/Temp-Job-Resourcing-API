package com.nology.java.consolidation.temp;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.nology.java.consolidation.job.Job;

public class TempDTO {

	@NotNull
	public String FirstName;
	
	@NotNull
	public String LastName;
	
	
	public List<Job> jobArr;


	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
	public List<Job> getJobArr() {
		return jobArr;
	}
	public void setJobArr(List<Job> jobArr) {
		this.jobArr = jobArr;
	}

	
	
	
	
}