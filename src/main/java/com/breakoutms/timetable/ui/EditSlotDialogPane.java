package com.breakoutms.timetable.ui;

import com.breakoutms.timetable.MainController;
import com.breakoutms.timetable.db.VenueDAO;
import com.breakoutms.timetable.db.DAO;
import com.breakoutms.timetable.model.Matrix;
import com.breakoutms.timetable.model.Properties;
import com.breakoutms.timetable.model.beans.*;
import com.breakoutms.timetable.service.SlotManager;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class EditSlotDialogPane extends DialogPane {

    @FXML private TextField courseFld;
    @FXML private TextField classFld;
    @FXML private TextField venueFld;
    @FXML private ComboBox<Venue> venueFilter;
    @FXML private StackPane gridPlaceholder;
    @FXML private Label title;

    private final Slot slot;
    private final TimetableGrid grid = new TimetableGrid();
    private final VenueDAO venueDAO = new VenueDAO();
    private final SlotManager slotManager = new SlotManager();
    private Slot newSlot;

    public EditSlotDialogPane(Slot slot){
        if(slot == null){
            throw new IllegalArgumentException("Slot cannot be null");
        }
        this.slot = slot;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_slot_dialog.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
            gridPlaceholder.getChildren().addAll(grid);
            setPrefSize(650,650);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @FXML
    void initialize() {
        title.setText(slot.getLecturerName());
        venueFilter.getItems().addAll(venueDAO.facultyAll());
        classFld.setText(slot.getStudentClassName());
        courseFld.setText(slot.getCourse().getName());
        venueFld.setText(slot.getVenueName());
        venueFilter.getSelectionModel().select(slot.getAllocatedVenue().getVenue());
        venueFilter.getSelectionModel().selectedItemProperty().addListener((ob, o, n)->{
            if(n != null){
                populateGrid(n.getName());
            }
        });
        populateGrid(slot.getVenueName());
    }

    private void populateGrid(String venue){
        for (int i = 0; i <= Properties.totalSessions(); i++) {
            for (int j = 0; j <= Properties.totalDays(); j++) {
                JFXButton button = new JFXButton();
                button.setCursor(Cursor.HAND);
                button.setMaxHeight(Double.MAX_VALUE);
                button.setMaxWidth(Double.MAX_VALUE);
                int index = Matrix.index(i-1, j-1, Properties.totalSessions());
                button.setId(String.valueOf(index));
                button.setOnAction(EditSlotDialogPane.this::slotSelected);
                StackPane stackPane = new StackPane(button);
                if(i > 0 && j > 0) {
                    add(stackPane, i, j);
                    GridPane.setFillWidth(stackPane, true);
                    GridPane.setFillHeight(stackPane, true);
                }
            }
        }
        for (Slot item: Project.INSTANCE.getSlots()){
            SlotView slotView = new VenueSlotView(item);
            if(item.getVenueName().equals(venue)){
                if(item.equals(currentSlot())){
                    Label label = new Label(item.getCourse().toString());
                    GridPane.setFillHeight(label, true);
                    GridPane.setFillWidth(label, true);
                    StackPane pane = new StackPane(label);
                    pane.setStyle("-fx-background-color: #b0bec5;");

                    add(pane, slotView.getCol()+1, slotView.getRow()+1);
                }
                else {
                    add(slotView, slotView.getCol()+1, slotView.getRow()+1);
                }
            }
            else if(item.getLecturerName().equals(slot.getLecturerName())){
                SlotView pane = new LecturerSlotView(item);
                pane.setStyle("-fx-background-color: #FFCCBC;");
                add(pane, slotView.getCol()+1, slotView.getRow()+1);
            }
            else if(item.getStudentClassName().equals(slot.getStudentClassName())){
                SlotView pane = new LecturerSlotView(item);
                pane.setStyle("-fx-background-color: #FFCCBC;");
                add(pane, slotView.getCol()+1, slotView.getRow()+1);
            }
        }
    }

    private Slot currentSlot() {
        return newSlot != null? newSlot : slot;
    }

    private void slotSelected(ActionEvent event) {
        Button button = (Button) event.getSource();
        Allocation al = new Allocation(slot.getLecturer(),
                slot.getCourse(), slot.getStudentClass(),
                venueFilter.getValue().getVenueType());
        al.setTimeIndex(Integer.valueOf(button.getId()));
        al.setStrictPreferredTime(true);
        al.setVenue(venueFilter.getValue());

        removeSlot(currentSlot());
        newSlot = slotManager.allocate(al);
        Project.INSTANCE.getSlots().add(currentSlot());
        populateGrid(currentSlot().getVenueName());
    }

    private void removeSlot(Slot slot) {
        log.info("Removing slot: {}", slot);
        Project.INSTANCE.getSlots().remove(currentSlot());
        MainController.getInstance().getLecturerGrids().get(slot.getLecturerName()).delete(slot);
        MainController.getInstance().getStudentGrids().get(slot.getStudentClassName()).delete(slot);
        MainController.getInstance().getVenueGrids().get(slot.getVenueName()).delete(slot);
    }

    private void add(Node node, int col, int row) {
        grid.getChildren().removeIf(it ->
                GridPane.getRowIndex(it) == row && GridPane.getColumnIndex(it) == col);
        String style = String.format("-fx-border-color: %s;", SlotView.BORDER_COLOR);
        node.setStyle(node.getStyle()+" "+ style);
        grid.add(node, col, row);
    }

    @FXML
    void reset(ActionEvent event) {
        var current = currentSlot();
        Project.INSTANCE.getSlots().remove(current);
        Project.INSTANCE.getSlots().add(slot);
        newSlot = null;
        populateGrid(slot.getVenueName());
        venueFilter.setValue(slot.getAllocatedVenue().getVenue());
    }

    @FXML void nextVenue(ActionEvent event) {
        int index = venueFilter.getItems().indexOf(venueFilter.getValue());
        if(index < venueFilter.getItems().size()-1){
            venueFilter.setValue(venueFilter.getItems().get(index+1));
        }
    }

    @FXML void previousVenue(ActionEvent event) {
        int index = venueFilter.getItems().indexOf(venueFilter.getValue());
        if(index > 0){
            venueFilter.setValue(venueFilter.getItems().get(index-1));
        }
    }

    public Slot getSlot(){
        return slot;
    }
}
