module com.breakoutms.timetable.desktop.timetabledesktop {

    exports com.breakoutms.timetable.model;
    exports com.breakoutms.timetable;
    exports com.breakoutms.timetable.model.beans;
    exports com.breakoutms.timetable.pref;
    exports com.breakoutms.timetable.ui;

    opens com.breakoutms.timetable to javafx.fxml;
    opens com.breakoutms.timetable.pref to javafx.fxml;
    opens com.breakoutms.timetable.ui to javafx.fxml;
    opens com.breakoutms.timetable.model.beans to org.hibernate.orm.core;
    exports com.breakoutms.timetable.service;

    requires javafx.base;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires static lombok;
    requires org.apache.commons.lang3;
    requires org.apache.logging.log4j;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.sql;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires java.xml;
    requires java.naming;
    requires java.validation;
    requires java.prefs;
    requires com.jfoenix;
    requires poi;
    requires poi.ooxml;
    requires poi.ooxml.schemas;
    requires org.apache.commons.collections4;
    requires com.opencsv;
}