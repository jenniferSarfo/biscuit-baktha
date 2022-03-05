package com.biscuit.commands.epic;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.models.Epic;
import com.biscuit.models.Project;
import com.biscuit.models.Release;
import com.biscuit.models.Sprint;
import com.biscuit.models.UserStory;
import com.biscuit.models.services.DateService;

import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthLongestLine;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;

public class ListEpics implements Command {

//	Project project = null;
	Sprint sprint = null;
	UserStory userstory = null;
	String name = "";
	boolean isFilter = false;
	boolean isSort = false;
	static boolean isReverse = false;
	private String filterBy;
	private String sortBy;
	private static String lastSortBy = "";


	public ListEpics(Sprint sprint, String name) {
		super();
		this.sprint = sprint;
		this.name = name;
	}


	public ListEpics(Sprint sprint, String title, boolean isFilter, String filterBy, boolean isSort, String sortBy) {
		super();
		this.sprint = sprint;
		this.name = title;
		this.isFilter = isFilter;
		this.filterBy = filterBy.toLowerCase();
		this.isSort = isSort;
		this.sortBy = sortBy.toLowerCase();
	}


	public ListEpics(UserStory userstory, String title) {
		super();
		this.userstory = userstory;
		this.name = title;
	}


//	public ListEpics(Release release, String title, boolean isFilter, String filterBy, boolean isSort, String sortBy) {
//		super();
//		this.release = release;
//		this.title = title;
//		this.isFilter = isFilter;
//		this.filterBy = filterBy.toLowerCase();
//		this.isSort = isSort;
//		this.sortBy = sortBy.toLowerCase();
//	}


	@Override
	public boolean execute() throws IOException {

		V2_AsciiTable at = new V2_AsciiTable();
		String tableString;

		List<Epic> epics = new ArrayList<>();

		if (sprint != null) {
			epics.addAll(sprint.epics);
		} 
//		else if (release != null) {
//			sprints.addAll(release.sprints);
//		}

//		if (isFilter) {
//			doFilter(epics);
//		}
//
//		if (isSort) {
//			doSort(sprints);
//		}

		at.addRule();
		if (!this.name.isEmpty()) {
			at.addRow(null, null, null, null, null,null).setAlignment(new char[] { 'c', 'c', 'c', 'c', 'c', 'c' });
			at.addRule();
		}
		at.addRow("Name", "Description", "State", "Start Date", "Due Date", "Epic Planning Completed")
				.setAlignment(new char[] { 'l', 'l', 'c', 'c', 'c', 'c'});

		if (epics.size() == 0) {
			String message;
			if (!isFilter) {
				message = "There are no epics!";
			} else {
				message = "No results.";
			}
			at.addRule();
			at.addRow(null, null, null, null, message,null);
		} else {
			for (Epic s : epics) {
				at.addRule();

				at.addRow(s.name, s.description, s.state, DateService.getDateAsString(s.startDate), DateService.getDateAsString(s.dueDate), s.epicCompleted
						).setAlignment(new char[] { 'l', 'l', 'c', 'c', 'c', 'c' });
			} // for
		}

		at.addRule();
		at.addRow(null, null, null, null, null, "Total: " + epics.size());
		at.addRule();

		V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
		rend.setTheme(V2_E_TableThemes.PLAIN_7BIT.get());
		rend.setWidth(new WidthLongestLine());

		RenderedTable rt = rend.render(at);
		tableString = rt.toString();

		tableString = colorize(tableString);

		System.out.println();
		System.out.println(tableString);

		return false;
	}


	private void doSort(List<Epic> epics) {

		Comparator<Sprint> byName = (s1, s2) -> s1.name.compareTo(s2.name);
		Comparator<Sprint> byDescription = (s1, s2) -> s1.description.compareTo(s2.description);
		Comparator<Sprint> byState = (s1, s2) -> Integer.compare(s1.state.getValue(), s2.state.getValue());
		Comparator<Sprint> byStartDate = (s1, s2) -> s1.startDate.compareTo(s2.startDate);
		Comparator<Sprint> byDueDate = (s1, s2) -> s1.dueDate.compareTo(s2.dueDate);
//		Comparator<Sprint> byAssignedEffort = (s1, s2) -> Integer.compare(s1.assignedEffort, s2.assignedEffort);
//		Comparator<Sprint> byVelocity = (s1, s2) -> Integer.compare(s1.velocity, s2.velocity);
		Comparator<Sprint> byFiled = null;

		if (sortBy.equals(Epic.fields[0])) {
			byFiled = byName;
		} else if (sortBy.equals(Epic.fields[1])) {
			byFiled = byDescription;
		} else if (sortBy.equals(Epic.fields[2])) {
			byFiled = byState;
		} else if (sortBy.equals(Epic.fields[3])) {
			byFiled = byStartDate;
		} else if (sortBy.equals(Epic.fields[4])) {
			byFiled = byDueDate;
		} 
//		else if (sortBy.equals(Sprint.fields[5])) {
//			byFiled = byAssignedEffort;
//		} 
//		else if (sortBy.equals(Sprint.fields[6])) {
//			byFiled = byVelocity;
//		} 
		else {
			return;
		}

//		List<Epic> sorted = epics.stream().sorted(byFiled).collect(Collectors.toList());
//
//		if (sortBy.equals(lastSortBy)) {
//			isReverse = !isReverse;
//			if (isReverse) {
//				Collections.reverse(sorted);
//			}
//		} else {
//			lastSortBy = sortBy;
//			isReverse = false;
//		}
//
//		epics.clear();
//		epics.addAll(sorted);
	}


	private void doFilter(List<Epic> epic) {
		List<Epic> filtered = epic.stream()
				.filter(r -> r.name.toLowerCase().contains(filterBy) || r.description.toLowerCase().contains(filterBy)
						|| r.state.toString().toLowerCase().contains(filterBy) || DateService.getDateAsString(r.startDate).toLowerCase().contains(filterBy)
						|| DateService.getDateAsString(r.dueDate).toLowerCase().contains(filterBy))
				.collect(Collectors.toList());
		epic.clear();
		epic.addAll(filtered);
	}


	private String colorize(String tableString) {
		for (String header : Sprint.fieldsAsHeader) {
			tableString = tableString.replaceFirst(header, ColorCodes.BLUE + header + ColorCodes.RESET);
		}

		return tableString;
	}

}
