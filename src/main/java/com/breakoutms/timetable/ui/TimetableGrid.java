package com.breakoutms.timetable.ui;

import com.breakoutms.timetable.model.Properties;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class TimetableGrid extends GridPane {

    public TimetableGrid(){
        for (int i = 0; i < Properties.totalSessions()+1; i++) { //+1 is added for column labels
            ColumnConstraints con = new ColumnConstraints();
            con.setPercentWidth(100D/Properties.totalSessions());
            con.setFillWidth(true);
            con.setHgrow(Priority.ALWAYS);
            getColumnConstraints().add(con);
        }
        for (int i = 0; i <= Properties.totalDays(); i++) {
            RowConstraints con = new RowConstraints();
            con.setPercentHeight(100D/Properties.totalDays());
            con.setFillHeight(true);
            con.setVgrow(Priority.ALWAYS);
            getRowConstraints().add(con);
        }
        addGridPlaceHolders();
    }

    private void addGridPlaceHolders() {
        int rows = Properties.totalSessions();
        int cols = Properties.totalDays();

        for (int i = 0; i <= cols; i++) {
            if(i > 0) {
                Label label = rowColLabel(Properties.dayLabel(i));
                add(label, 0, i);
            }
        }

        for (int i = 0; i < rows+1; i++) {
            if(i > 0) {
                Label label = rowColLabel(Properties.sessionLabel(i));
                add(label, i, 0);
            }
        }
        add(rowColLabel(""), 0, 0);

        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j <= cols; j++) {
                StackPane pane = new StackPane();
                String style = String.format("-fx-border-color: %s;", SlotView.BORDER_COLOR);
                if(i > 0 && j > 0) {
                    style += "-fx-background-color: #ffffff;";
                }
                pane.setStyle(style);
                add(pane, i, j);
            }
        }
    }

    private Label rowColLabel(String text) {
        Font font = Font.font(fontFamily(), FontWeight.NORMAL, 12);
        Label label = new Label(text);
        label.setMaxHeight(Double.POSITIVE_INFINITY);
        label.setMaxWidth(Double.POSITIVE_INFINITY);
        label.setFont(font);
        label.setStyle("-fx-background-color: #E0E0E0;");
        label.setAlignment(Pos.CENTER);
        return label;
    }

    public String fontFamily() {
        return Font.getDefault().getFamily();
    }
}
