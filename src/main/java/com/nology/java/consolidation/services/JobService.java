package com.nology.java.consolidation.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nology.java.consolidation.dtos.JobDTO;
import com.nology.java.consolidation.entities.Job;
import com.nology.java.consolidation.entities.Temp;
import com.nology.java.consolidation.repositories.JobRepository;
import com.nology.java.consolidation.repositories.TempRepository;

@Service
@Transactional
public class JobService {
	
	@Autowired
	private JobRepository jRepo;
	
	@Autowired
	private TempRepository tRepo;
	
	
	public List<Job> getAllJobs() {
		return jRepo.findAll();
	}

	public List<Job> getAllJobsTF(Boolean bool) {
		List<Job> all = getAllJobs();
		List<Job> filtered = new ArrayList<>();
		for (Job job: all) {
			if( bool == true) {
				if (job.getTemp() != null) {
					filtered.add(job);
				}
			}
			if(bool == false) {
				if (job.getTemp() == null) {
					filtered.add(job);
				}
			}
		}
		return filtered;
	}

	public Job createJob(JobDTO job) {	
		Job j = new Job(job.getName(),job.getStartDate(),job.getEndDate());
		
		return jRepo.save(j);
		
	}

	public Optional<Job> getJob(Long id) {
		return jRepo.findById(id);
	}
	
	
	public Job updateJob (Long jobId,JobDTO jobData) {
		Optional<Job> fetchedJob = jRepo.findById(jobId);
		if(fetchedJob.isEmpty()) return null;
		Job existentJob = jRepo.findById(jobId).get();
		
//		details update begins:
		
		if(jobData.getName() != null && !jobData.getName().isEmpty()) {
			existentJob.setName(jobData.getName());
		}
		if(jobData.getStartDate() != null) {
			existentJob.setStartDate(jobData.getStartDate());
		}
		if(jobData.getEndDate() != null) {
			existentJob.setEndDate(jobData.getEndDate());
		}
		if(jobData.getTid() != null) {
			existentJob.settId(jobData.getTid());
		}
		
		Long tId = existentJob.gettId();
		Optional<Temp> fetchedTemp = tRepo.findById(tId);
		if(fetchedTemp.isEmpty()) return null;
		
		Temp existentTemp = tRepo.findById(tId).get();
//		date checking begins:
//		zero check
		if(existentTemp.getJobArr().size() == 0) {
			existentJob.setTemp(existentTemp);
			return this.jRepo.save(existentJob);
		}
		LocalDate aS,aF,bS,bF;
		aS = existentJob.getStartDate();
		aF = existentJob.getEndDate();
	
//		sort jobArr
		ArrayList<Job> jobArr = new ArrayList<>();
		for (Job job : existentTemp.getJobArr()) {
			jobArr.add(job);
		}
		Collections.sort(jobArr, new Comparator<Job>() {
			public int compare(Job a, Job b) {
				return a.getStartDate().compareTo(b.getStartDate());
			}
		});
		
//		start and end checks
			bS = jobArr.get(0).getStartDate();
			bF = jobArr.get(jobArr.size() - 1).getEndDate();
			
			if (aF.isBefore(bS)) {
				existentJob.setTemp(existentTemp);
				return this.jRepo.save(existentJob);
			}
			if (bF.isBefore(aS)) {
				existentJob.setTemp(existentTemp);
				return this.jRepo.save(existentJob);
			}
			
//			in between checks
		for (int i = 0; i < jobArr.size() - 1; i++) {
		
			bF = jobArr.get(i).getEndDate();
			bS = jobArr.get(i + 1).getStartDate();
			if (bF.isBefore(aS) && aF.isBefore(bS)) {
				existentJob.setTemp(existentTemp);
				return this.jRepo.save(existentJob);
			}
		}
		
		return this.jRepo.save(existentJob);
	}
	
	public Boolean deleteJob (Long id) {
		Optional<Job> fetchedJob = getJob(id);
		if (fetchedJob.isEmpty()) return null;
		jRepo.deleteById(id);
		return true;
	}
	
	
	
}
