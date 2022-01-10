package com.breakoutms.timetable.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.breakoutms.timetable.db.VenueDAO;
import com.breakoutms.timetable.model.Properties;
import com.breakoutms.timetable.model.beans.*;
import lombok.extern.log4j.Log4j2;

import static com.breakoutms.timetable.model.beans.Venue.VenueType.*;

@Log4j2
public class SlotManager {
	
	private static final Set<Slot> slots = Project.INSTANCE.getSlots();
	private static final int RETRIES = 1500;

	private final LecturerService lecturerService = new LecturerService();
	private final StudentClassService studentClassService = new StudentClassService();
	private final CourseService courseService = new CourseService();
	private final VenueDAO venueRepository = new VenueDAO();

	public Slot allocate(Allocation al) {
		Slot slot = createSlot(al);
		slots.add(slot);
		log.info("SlotGenerator.allocate(): "+ slot);
		return slot;
	}

	public static boolean contains(Allocation al) {
		for(Slot slot: slots){
			if(slot.getCourse().equals(al.getCourse())
				&& slot.getLecturer().equals(al.getLecturer())
				&& slot.getStudentClass().equals(al.getStudentClass())){
				return true;
			}
		}
		return false;
	}

	private Slot createSlot(Allocation al) {
		courseService.create(al.getCourse());
		LecturerIndex lecturer = lecturerService.createIndex(al.getLecturer());
		StudentClassIndex stdClass = studentClassService.createIndex(al.getStudentClass());

		AllocatedVenue allocatedVenue = attemptMatchingIndexes(al, lecturer);
		for (int i = 0; i < RETRIES; i++) {
			Venue venue = allocatedVenue.getVenue();
			if(venue != null && al.getVenueType() == Venue.VenueType.ANY){
				break;
			}
			if(!al.isStrictPreferredTime() &&
					(venue == null || (venue.getVenueType() != al.getVenueType()))){
				allocatedVenue = attemptMatchingIndexes(al, lecturer);
			}
		}

		Venue venue = allocatedVenue.getVenue();
		if((venue == null ||
				(al.getVenueType() != ANY && venue.getVenueType() != al.getVenueType()))){
			throw new IllegalArgumentException("Unable to find a vacant slot for the selected venue or venue type");
		}

		lecturer.setTimeIndex(allocatedVenue.getTimeIndex());
		stdClass.setTimeIndex(allocatedVenue.getTimeIndex());

		return new Slot(lecturer, stdClass, allocatedVenue, al.getCourse());
	}

	private AllocatedVenue attemptMatchingIndexes(Allocation al, LecturerIndex lecturer) {
		int lectureTime = lecturerTimeIndex(lecturer, al.getTimeIndex());
		int studentsTime = studentsTimeIndex(al.getStudentClass(), lectureTime);

		for (int i = 0; i < RETRIES; i++) {
			if(lectureTime == studentsTime) {
				break;
			}
			lectureTime = lecturerTimeIndex(lecturer, al.getTimeIndex());
		}
		if(lectureTime != studentsTime) {
			throw new IllegalStateException("Unable to find slot matching both student and lecturer");
		}

		AllocatedVenue allocatedVenue = new AllocatedVenue();
		Venue venue;
		if(al.getVenue() != null){
			venue = vacantVenue(al.getVenue(), lectureTime);
		}
		else venue = randomVenue(al.getVenueType(), lectureTime);
		allocatedVenue.setVenue(venue);
		allocatedVenue.setTimeIndex(lectureTime);
		return allocatedVenue;
	}

	private Venue vacantVenue(Venue venue, Integer timeIndex) {
		var filter = slots.stream()
				.map(Slot::getAllocatedVenue)
				.filter(it -> it.getVenue().equals(venue))
				.filter(it -> it.getTimeIndex() == timeIndex)
				.map(AllocatedVenue::getVenue)
				.findFirst();
		if(filter.isEmpty()){
			return venue;
		}
		else return null;
	}

	private Venue randomVenue(Venue.VenueType venueType, Integer preferredTime) {
		Venue venue = randomVenue(preferredTime);
		if(venueType == null || venueType == Venue.VenueType.ANY) {
			return venue;
		}
		for (int i = 0; i < RETRIES; i++) {
			if(venue != null
					&& venue.getVenueType() == venueType) {
				return venue;
			}
			venue = randomVenue(preferredTime);
		}
		return venue;
	}

	private Venue randomVenue(Integer timeIndex) {
		List<Venue> exclude = slots.stream()
				.map(Slot::getAllocatedVenue)
				.filter(it -> it.getTimeIndex() == timeIndex)
				.map(AllocatedVenue::getVenue)
				.toList();
		List<Venue> allVenues = venueRepository.all();
		return allVenues.stream()
				.filter(it -> !exclude.contains(it))
				.skip(random(0, allVenues.size()))
				.findAny().orElse(null);
	}

	private Integer lecturerTimeIndex(LecturerIndex lecturer, Integer preferredTime) {
		var exclude = slots.stream()
				.map(Slot::getLecturerIndex)
				.filter(it -> it.getName().equalsIgnoreCase(lecturer.getName()))
				.map(LecturerIndex::getTimeIndex)
				.collect(Collectors.toSet());
		return randomTimeIndex(exclude, preferredTime);
	}

	private Integer studentsTimeIndex(StudentClass studentClass, Integer preferredTime) {
		var exclude = slots.stream()
				.filter(slot -> slot.getStudentClass().equals(studentClass))
				.map(Slot::getStudentClassIndex)
				.map(StudentClassIndex::getTimeIndex)
				.collect(Collectors.toSet());
		return randomTimeIndex(exclude, preferredTime);
	}

	private Integer randomTimeIndex(Set<Integer> exclude, Integer preferredTime) {
		if(preferredTime != null && !exclude.contains(preferredTime)) {
			return preferredTime;
		}
		int timeIndex = -1;
		for (int i = 0; i < RETRIES; i++) {
			if(timeIndex >= 0 && !exclude.contains(timeIndex)) {
				return timeIndex;
			}
			timeIndex = random(0, Properties.totalTimeSlots());
		}
		throw new IllegalStateException("Unable to find unallocated time slots for (lecture and/or student)");
	}

	public int random(int min, int max) {
		return ThreadLocalRandom.current()
				.nextInt(min, max);
	}
}
