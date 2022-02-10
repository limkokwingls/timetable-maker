package com.breakoutms.timetable;

import com.breakoutms.timetable.db.VenueDAO;
import com.breakoutms.timetable.db.DAO;
import com.breakoutms.timetable.model.Matrix;
import com.breakoutms.timetable.model.Properties;
import com.breakoutms.timetable.service.SlotManager;
import com.breakoutms.timetable.model.beans.*;
import com.breakoutms.timetable.pref.PreferencesController;
import com.breakoutms.timetable.service.WordExporter;
import com.breakoutms.timetable.ui.*;
import com.breakoutms.timetable.service.ProjectFileManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.SearchableComboBox;

import java.util.*;

@Log4j2
public class MainController {

	public static final String DEFAULT_DAY = "ANY";

	private enum DISPLAY_TYPE {
		LECTURERS,
		VENUES,
		STUDENTS
	}

	@FXML private SearchableComboBox<Lecturer> lecturer;
	@FXML private SearchableComboBox<Course> course;
	@FXML private SearchableComboBox<StudentClass> studentClass;
	@FXML private VBox timetableHolder;
	@FXML private ScrollPane tableHolder;
	@FXML private ComboBox<DISPLAY_TYPE> displayFor;
	@FXML private ComboBox<Venue.VenueType> venueType;
	@FXML private ComboBox<Venue> venue;
	@FXML private ComboBox<Lecturer> lecturersFilter;
	@FXML private ComboBox<String> day;
	@FXML private ComboBox<String> time;
	@FXML private TextField searchFld;

	private static MainController INSTANCE;

	private final TableView<Slot> slotTableView = SlotTable.get();
	private final DAO<Lecturer> lecturerDao = new DAO<>(Lecturer.class);
	private final DAO<Course> courseDao = new DAO<>(Course.class);
	private final DAO<StudentClass> studentClassesDao = new DAO<>(StudentClass.class);
	private final VenueDAO venueDAO = new VenueDAO();

	private final Map<String, TimetablePreviewPane> lecturerGrids = new TreeMap<>();
	private final Map<String, TimetablePreviewPane> studentGrids = new TreeMap<>();
	private final Map<String, TimetablePreviewPane> venueGrids = new TreeMap<>();
	private final SlotManager slotManager = new SlotManager();

	private final ProjectFileManager projectFile = new ProjectFileManager();

	private Course currentCourse;
	private StudentClass currentClass;

	public MainController(){
		INSTANCE = this;
	}

	public static SearchableComboBox<Lecturer> lecturesComboBox() {
		return MainController.INSTANCE.lecturer;
	}
	public static SearchableComboBox<Course> courseComboBox() {
		return MainController.INSTANCE.course;
	}
	public static SearchableComboBox<StudentClass> studentClassComboBox() {
		return MainController.INSTANCE.studentClass;
	}
	public static ComboBox<Venue> venueComboBox() {
		return MainController.INSTANCE.venue;
	}

	@FXML
	void initialize() {
		populateInputComboBoxes();

		venueType.getItems().addAll(Venue.VenueType.values());
		venueType.getSelectionModel().select(0);
		venueType.getSelectionModel().selectedItemProperty().addListener((obs, o, n)->{
			venue.getItems().clear();
			if(n != Venue.VenueType.ANY){
				venue.setDisable(false);
				for(var item : venueDAO.all()){
					if(item.getVenueType() == n){
						venue.getItems().add(item);
					}
				}
			}
			else venue.setDisable(true);
		});
		displayFor.getItems().addAll(DISPLAY_TYPE.values());
		displayFor.getSelectionModel().select(0);
		displayFor.getSelectionModel()
			.selectedItemProperty()
			.addListener(it -> renderPreviewPanel());
		tableHolder.setContent(slotTableView);

		lecturersFilter.getSelectionModel()
				.selectedItemProperty()
				.addListener((ob, oldv, newv)->{
			slotTableView.getItems().clear();
			for(Slot slot: Project.INSTANCE.getSlots()){
				if(newv != null &&
						slot.getLecturerName().equalsIgnoreCase(newv.getName())){
					slotTableView.getItems().add(slot);
				}
			}
		});

		time.getItems().addAll(Properties.allSessionLabels());
		day.getItems().add(DEFAULT_DAY);
		day.getItems().addAll(Properties.allDayLabels());
		day.getSelectionModel().select(0);
		day.getSelectionModel().selectedItemProperty().addListener((obs, o, n)->{
			if(n != null && !n.equals(DEFAULT_DAY)){
				time.setDisable(false);
				time.getSelectionModel().select(0);
			}
			else {
				time.getSelectionModel().clearSelection();
				time.setDisable(true);
			}
		});
		Platform.runLater(()->timetableHolder.requestFocus());
	}

	public void populateInputComboBoxes(){
		lecturer.getItems().clear();
		course.getItems().clear();
		studentClass.getItems().clear();

		lecturer.getItems().addAll(lecturerDao.all());
		course.getItems().addAll(courseDao.all());
		studentClass.getItems().addAll(studentClassesDao.all());
	}

	@FXML
	void preferences(ActionEvent event) {
		Dialog<Void> dialog = new Dialog<>();
		dialog.setDialogPane(new PreferencesController());
		dialog.showAndWait();
	}

	@FXML
	void onOpen(ActionEvent event){
		var project = projectFile.openFile();
		if(project.isPresent()){
			lecturerGrids.clear();
			venueGrids.clear();
			studentGrids.clear();
			Project.INSTANCE.getSlots().clear();
			Project.INSTANCE.getSlots().addAll(project.get().getSlots());
			for(var slot: Project.INSTANCE.getSlots()){
				addSlotToPreviewPanel(slot);
				addToLecturersFilter(slot.getLecturer());
			}
		}
	}

	@FXML
	void onSave(ActionEvent event){
		projectFile.saveFile();
	}

	@FXML
	void onExport(ActionEvent event) {
		try {
			WordExporter.export(Project.INSTANCE.getSlots());
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setContentText("Done");
			alert.showAndWait();
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(ex.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void onAdd(ActionEvent event) {
		if(!validInput()){
			return;
		}
		Allocation al = new Allocation(lecturer.getValue(),
				course.getValue(), studentClass.getValue(),
				venueType.getValue());
		al.setVenue(venue.getSelectionModel().getSelectedItem());
		if(!day.getSelectionModel().getSelectedItem().equals(DEFAULT_DAY)){
			al.setStrictPreferredTime(true);
			int row = time.getSelectionModel().getSelectedIndex();
			int col =  day.getSelectionModel().getSelectedIndex() - 1; //-1 for DEFAULT_DAY
			int index = Matrix.index(row, col, Properties.totalSessions());
			al.setTimeIndex(index);
		}

		try {
			if(!SlotManager.contains(al)) {
				addToLecturersFilter(lecturer.getValue());
				Slot slot = slotManager.allocate(al);
				currentClass = slot.getStudentClass();
				currentCourse = slot.getCourse();
				addSlotToPreviewPanel(slot);
				clearInputFields();
			}
			else warn("Already added: "+al);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(ex.getMessage());
			alert.showAndWait();
		}
	}

	private void addToLecturersFilter(Lecturer lec) {
		if (!lecturersFilter.getItems().contains(lec)) {
			lecturersFilter.getItems().add(lec);
			lecturersFilter.getItems()
					.sort(Comparator.comparing(Lecturer::getName));
		}
		lecturersFilter.getSelectionModel().select(lec);
	}

	private void clearInputFields() {
		Lecturer selectedLecturer = lecturersFilter.getValue();
		if(lecturer != null){
			lecturer.getSelectionModel().select(selectedLecturer);
		}
		course.getSelectionModel().select(currentCourse);
		studentClass.getSelectionModel().select(currentClass);
		day.getSelectionModel().select(0);
	}

	private boolean validInput() {
		List<String> missing = new ArrayList<>();
		if(lecturer.getValue() == null){
			missing.add("Lecturer");
		}
		if(studentClass.getValue() == null){
			missing.add("Class");
		}
		if(course.getValue() == null){
			missing.add("Course");
		}
		if(!missing.isEmpty()){
			warn("Please fill in fields for "+ missing);
			return false;
		}
		return true;
	}

	private void warn(String msg) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setContentText(msg);
		alert.showAndWait();
	}

	private void addSlotToPreviewPanel(Slot slot) {
		slotTableView.getItems().add(slot);
		addToLecturers(slot);
		addToStudents(slot);
		addToVenues(slot);
		renderPreviewPanel();
	}

	@FXML
	void editSlot(ActionEvent event){
		EditSlotDialogPane pane = new EditSlotDialogPane(
				slotTableView.getSelectionModel().getSelectedItem());
		Dialog<Void> dialog = new Dialog<>();
		dialog.setTitle("Edit Slot");
		dialog.setDialogPane(pane);
		dialog.showAndWait();
		slotTableView.getItems().clear();
		venueGrids.clear();
		studentGrids.clear();
		slotTableView.getItems().remove(pane.getSlot());
		for(var slot: Project.INSTANCE.getSlots()){
			if(lecturersFilter.getValue() != null &&
					lecturersFilter.getValue().getName()
							.equalsIgnoreCase(slot.getLecturerName())) {
					addSlotToPreviewPanel(slot);
			}
		}
	}

	@FXML
	void deleteSlot(ActionEvent event){
		Slot slot = slotTableView.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Delete Allocation?");
		alert.setContentText("Are you sure you want to delete "+slot+"?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK){
			slotTableView.getItems().remove(slot);
			lecturerGrids.get(slot.getLecturerName()).delete(slot);
			studentGrids.get(slot.getStudentClassName()).delete(slot);
			venueGrids.get(slot.getVenueName()).delete(slot);
			Project.INSTANCE.getSlots().remove(slot);
			renderPreviewPanel();
		}
	}

	@FXML
	void onPreview(ActionEvent event){
		Node node = (Node) event.getSource();
		if(node.getId().equals("lecturerPreview")){
			searchFld.setText(lecturer.getValue().getName());
		}
		else if(node.getId().equals("classPreview")){
			searchFld.setText(studentClass.getValue().getName());
		}
		else if(node.getId().equals("venuePreview")){
			if(venue.getValue() == null){
				if(venueType.getValue() == Venue.VenueType.MULTIMEDIA_ROOM){
					searchFld.setText("MM");
				}
				else if(venueType.getValue() == Venue.VenueType.CLASS_ROOM){
					searchFld.setText("Room 1");
				}
				else if(venueType.getValue() == Venue.VenueType.LECTURE_HALL){
					searchFld.setText("Hall 6");
				}
				else if(venueType.getValue() == Venue.VenueType.NET_LAB){
					searchFld.setText("Net");
				}
				else if(venueType.getValue() == Venue.VenueType.WORK_SHOP){
					searchFld.setText("Work");
				}
			}
			else {
				searchFld.setText(venue.getValue().getName());
			}
		}
		renderPreviewPanel();
	}

	@FXML
	void searchPreviewPane(ActionEvent event) {
		renderPreviewPanel();
	}

    private void addToLecturers(Slot slot) {
		LecturerIndex lec = slot.getLecturerIndex();
		TimetablePreviewPane grid = lecturerGrids.get(lec.getName());
		if(grid == null) {
			grid = new TimetablePreviewPane(lec.getName());
			lecturerGrids.put(lec.getName(), grid);
		}
		var slotView = new LecturerSlotView(slot);
		grid.add(slotView);
	}

	private void addToStudents(Slot slot) {
		var item = slot.getStudentClassIndex();
		TimetablePreviewPane grid = studentGrids.get(item.getName());
		if(grid == null) {
			grid = new TimetablePreviewPane(item.getName());
			studentGrids.put(item.getName(), grid);
		}
		var slotView = new StudentSlotView(slot);
		grid.add(slotView);
	}

	private void addToVenues(Slot slot) {
		var item = slot.getAllocatedVenue();
		TimetablePreviewPane grid = venueGrids.get(item.getName());
		if(grid == null) {
			grid = new TimetablePreviewPane(item.getName());
			venueGrids.put(item.getName(), grid);
		}
		var slotView = new VenueSlotView(slot);
		grid.add(slotView);
	}

	private void renderPreviewPanel() {
		var searchKay = searchFld.getText();
		var timetables = timetableHolder.getChildren();
		timetables.clear();

		int countFound = 0;
		if(StringUtils.isNoneBlank(searchKay)){
			searchKay = searchKay.toLowerCase();
			DISPLAY_TYPE foundIn = null;
			for (var entry : studentGrids.entrySet()) {
				if (entry.getKey().toLowerCase().contains(searchKay)) {
					foundIn = DISPLAY_TYPE.STUDENTS;
					countFound++;
					break;
				}
			}
			for (var entry : lecturerGrids.entrySet()) {
				if (entry.getKey().toLowerCase().contains(searchKay)) {
					foundIn = DISPLAY_TYPE.LECTURERS;
					countFound++;
					break;
				}
			}
			for (var entry : venueGrids.entrySet()) {
				if (entry.getKey().toLowerCase().contains(searchKay)) {
					foundIn = DISPLAY_TYPE.VENUES;
					countFound++;
					break;
				}
			}
			if(countFound == 1){
				displayFor.getSelectionModel().select(foundIn);
			}
		}

		var selected = displayFor.getSelectionModel().getSelectedItem();

		if(selected == DISPLAY_TYPE.STUDENTS) {
			renderPreviewPaneFor(searchKay, timetables, studentGrids);
		}
		else if(selected == DISPLAY_TYPE.VENUES) {
			renderPreviewPaneFor(searchKay, timetables, venueGrids);
		}
		else {
			renderPreviewPaneFor(searchKay, timetables, lecturerGrids);
			filterSlotsFor(searchKay);
		}
	}

	private void filterSlotsFor(String searchKay) {
		if(StringUtils.isNoneBlank(searchKay)){
			for(Lecturer item: lecturersFilter.getItems()){
				if(StringUtils.isNoneBlank(item.getName())
						&& item.getName().toLowerCase().contains(searchKay.toLowerCase())){
					lecturersFilter.getSelectionModel().select(item);
					break;
				}
			}
		}
	}

	private void renderPreviewPaneFor(String searchKay, ObservableList<Node> timetables,
									  Map<String, TimetablePreviewPane> sortedMap) {
		sortedMap.forEach((k, v) -> {
			if (StringUtils.isNoneBlank(searchKay)) {
				if (k.toLowerCase().contains(searchKay.toLowerCase())
						&& !timetables.contains(v)){
					timetables.add(v);
				}
			} else {
				if(!timetables.contains(v)){
					timetables.add(v);
				}
			}
		});
	}
}
