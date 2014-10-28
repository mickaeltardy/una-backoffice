package com.mtdev.una.controller.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.business.WorkoutsManager;
import com.mtdev.una.data.dao.TaskDao;
import com.mtdev.una.data.dao.WorkoutDao;
import com.mtdev.una.model.Task;
import com.mtdev.una.model.WorkoutImpl;

@Controller
@RequestMapping("/tasks")
public class TasksService extends WorkoutService<Task> {

	@Autowired
	protected TaskDao mTaskDao;
	@Autowired
	protected WorkoutsManager mWorkoutsManager;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody Object createNewWorkout(@RequestBody List<Task> pTasks) {

		return super.createNewWorkout(pTasks);

	}

	@RequestMapping(value = "/retrieve", method = RequestMethod.POST)
	public @ResponseBody Object retrieveWorkouts(
			@RequestBody Map<Object, Object> pRequest) {

		return super.retrieveWorkouts(pRequest, Task.class);
	}

	@Override
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public Object removeWorkouts(@RequestBody List<Task> pWorkouts) {
		return super.removeWorkouts(pWorkouts);
	}

	@RequestMapping(value = "/calendar/{category}/{sex}/{level}", produces = "text/calendar")
	public @ResponseBody String retrieveCalendar(
			@PathVariable("category") String pCategory,
			@PathVariable("sex") String pSex,
			@PathVariable("level") String pLevel) {
		Map<Object, Object> lRequest = new HashMap<Object, Object>();

		if (!StringUtils.isEmpty(pCategory)) {
			lRequest.put("athleteCategory", pCategory);
		}

		if (!StringUtils.isEmpty(pLevel)) {
			lRequest.put("athleteLevel", pLevel);
		}

		if (!StringUtils.isEmpty(pSex)) {
			if (pSex.compareTo("mf") == 0) {
				String[] lSexes = new String[2];
				lSexes[0] = "m";
				lSexes[1] = "f";
				lRequest.put("athleteSex", lSexes);
			} else {

				lRequest.put("athleteSex", pSex);
			}
		}

		List<Task> lWorkouts = (List<Task>) retrieveWorkouts(lRequest);

		SimpleDateFormat lDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
		SimpleDateFormat lWorkoutDateFormat = new SimpleDateFormat("yyyyMMdd");
		StringBuffer lBuffer = new StringBuffer();
		String lOutputDate = lDateFormat.format(new Date()) + "Z";
		String lCalName = pCategory + "-" + pLevel + "-" + pSex;

		lBuffer.append("BEGIN:VCALENDAR\n");
		lBuffer.append("VERSION:2.0\n");
		lBuffer.append("LAST-MODIFIED:" + lOutputDate + "\n");
		lBuffer.append("DTSTAMP:" + lOutputDate + "\n");
		lBuffer.append("X-WR-CALNAME:" + lCalName + "\n");
		lBuffer.append("PRODID:-//Apple Inc.//Mac OS X 10.9.1//EN\n");
		lBuffer.append("X-APPLE-CALENDAR-COLOR:#0252D4\n");
		lBuffer.append("X-WR-TIMEZONE:Europe/Paris\n");
		lBuffer.append("CALSCALE:GREGORIAN\n");
		lBuffer.append("BEGIN:VTIMEZONE\n");
		lBuffer.append("TZID:Europe/Paris\n");
		lBuffer.append("BEGIN:DAYLIGHT\n");
		lBuffer.append("TZOFFSETFROM:+0100\n");
		lBuffer.append("RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU\n");
		lBuffer.append("DTSTART:19810329T020000\n");
		lBuffer.append("TZNAME:CEST\n");
		lBuffer.append("TZOFFSETTO:+0200\n");
		lBuffer.append("END:DAYLIGHT\n");
		lBuffer.append("BEGIN:STANDARD\n");
		lBuffer.append("TZOFFSETFROM:+0200\n");
		lBuffer.append("RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU\n");
		lBuffer.append("DTSTART:19961027T030000\n");
		lBuffer.append("TZNAME:CET\n");
		lBuffer.append("TZOFFSETTO:+0100\n");
		lBuffer.append("END:STANDARD\n");
		lBuffer.append("END:VTIMEZONE\n");

		if (lWorkouts != null && lWorkouts.size() > 0) {

			for (Task lWorkout : lWorkouts) {
				StringBuffer lLocalBuffer = new StringBuffer();
				String lWorkoutDateOutput = lWorkoutDateFormat.format(lWorkout
						.getDate());
				lLocalBuffer.append("\nBEGIN:VEVENT");
				lLocalBuffer.append("\nCREATED:" + lOutputDate);
				lLocalBuffer.append("\nLAST-MODIFIED:" + lOutputDate);
				lLocalBuffer.append("\nUID:" + lWorkout.getId());
				lLocalBuffer.append("\nDTEND;VALUE=DATE:" + lWorkoutDateOutput);
				lLocalBuffer.append("\nTRANSP:TRANSPARENT");
				lLocalBuffer.append("\nSUMMARY:"
						+ mWorkoutsManager.getWorkoutLabel(lWorkout));
				lLocalBuffer.append("\nDTSTART;VALUE=DATE:"
						+ lWorkoutDateOutput);
				lLocalBuffer.append("\nDTSTAMP:" + lOutputDate);
				lLocalBuffer.append("\nSEQUENCE:5");
				lLocalBuffer
						.append("\nURL;VALUE=URI:message:%3CCALfokf1yp8O5Q--d83xtvLe8CVrmpwaAfnVSQFOG6_-2C2");
				lLocalBuffer.append("\n VcNw@mail.gmail.com%3E");
				lLocalBuffer.append("\nBEGIN:VALARM");
				String lAlarmID = UUID.randomUUID().toString();
				lLocalBuffer.append("\nX-WR-ALARMUID:" + lAlarmID);
				lLocalBuffer.append("\nUID:" + lAlarmID);
				lLocalBuffer
						.append("\nTRIGGER;VALUE=DATE-TIME:19760401T005545Z");
				lLocalBuffer.append("\nX-APPLE-LOCAL-DEFAULT-ALARM:TRUE");
				lLocalBuffer.append("\nACTION:NONE");
				lLocalBuffer.append("\nEND:VALARM");
				lLocalBuffer.append("\nEND:VEVENT\n");

				lBuffer.append(lLocalBuffer.toString());
			}
		}

		lBuffer.append("\nEND:VCALENDAR");

		return lBuffer.toString();
	}

	WorkoutDao<Task> getDao() {
		return mTaskDao;
	}

}
