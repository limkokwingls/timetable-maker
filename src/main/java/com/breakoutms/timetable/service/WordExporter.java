package com.breakoutms.timetable.service;

import com.breakoutms.timetable.model.Properties;
import com.breakoutms.timetable.model.beans.Slot;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class WordExporter {

    public static final String LIGHT_GRAY = "ACACAC";
    public static final int ROW_HEIGHT = 950;
    public static final String TAHOMA = "Tahoma";

    private WordExporter() {
    }

    public static void export(Set<Slot> slots) throws IOException {

        var lecturers = lecturerSlots(slots).entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        lecturersTable(createDocument(), lecturers);

        var students = studentSlots(slots).entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        mergeGroups(students);

        studentsTable(createDocument(), students);

        Map<String, List<Slot>> venues = venueSlots(slots).entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        venueTable(createDocument(), venues);

    }

    private static void mergeGroups(LinkedHashMap<String, List<Slot>> students) {
        List<String> merged = new ArrayList<>();
        students.forEach((key, value) -> {
            var a = students.get(key + "(A)");
            if (a != null) {
                a.addAll(value);
            }
            var b = students.get(key + "(B)");
            if (b != null) {
                b.addAll(value);
                merged.add(key);
            }
            var c = students.get(key + "(C)");
            if (c != null) {
                c.addAll(value);
            }
        });
        students.entrySet().removeIf(entry -> merged.contains(entry.getKey()));
    }

    private static XWPFDocument createDocument() {
        var document = new XWPFDocument();
        XWPFHeaderFooterPolicy headerFooterPolicy = document.getHeaderFooterPolicy();
        if (headerFooterPolicy == null)
            headerFooterPolicy = document.createHeaderFooterPolicy();

        // create header start
        XWPFHeader header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);
        XWPFParagraph paragraph = header.createParagraph();

        XWPFRun run = paragraph.createRun();
        run.setColor("666666");
        run.setSmallCaps(true);
        run.setBold(true);
        run.setText("Faculty of Information and Communication Technology");
        return document;
    }

    private static Map<String, List<Slot>> lecturerSlots(Set<Slot> slots) {
        Map<String, List<Slot>> map = new HashMap<>();
        for (Slot item : slots) {
            List<Slot> list = map.computeIfAbsent(item.getLecturerName(), k -> new ArrayList<>());
            list.add(item);
        }
        return map;
    }

    private static Map<String, List<Slot>> venueSlots(Set<Slot> slots) {
        Map<String, List<Slot>> map = new HashMap<>();
        for (Slot item : slots) {
            List<Slot> list = map.computeIfAbsent(item.getVenueName(), k -> new ArrayList<>());
            list.add(item);
        }
        return map;
    }

    private static Map<String, List<Slot>> studentSlots(Set<Slot> slots) {
        Map<String, List<Slot>> map = new HashMap<>();
        for (Slot item : slots) {
            List<Slot> list = map.computeIfAbsent(item.getStudentClassName(), k -> new ArrayList<>());
            list.add(item);
        }
        return map;
    }

    private static void lecturersTable(XWPFDocument document, Map<String, List<Slot>> map) throws IOException {
        FileOutputStream out = new FileOutputStream(
                ProjectFileManager.projectFilePath().getPath() + "/lecturers.docx");
        for (var entry : map.entrySet()) {
            if (!isICTLecturer(entry.getKey())) {
                continue;
            }
            addTitle(document, entry.getKey());
            XWPFTable table = document.createTable();
            addLabels(table);
            for (var slot : entry.getValue()) {
                var row = table.getRow(slot.row() + 1);
                var cell = row.getCell(slot.col() + 1);
                writeToCell(cell, slot.getCourse().getName(),
                        slot.getVenueName(), slot.getStudentClassName(), " \t");
            }
            document.createParagraph().setPageBreak(true);
        }
        document.write(out);
        out.close();
    }

    private static void studentsTable(XWPFDocument document, Map<String, List<Slot>> map) throws IOException {
        FileOutputStream out = new FileOutputStream(
                ProjectFileManager.projectFilePath().getPath() + "/students.docx");
        for (var entry : map.entrySet()) {
            if (!isICTClass(entry.getKey())) {
                continue;
            }
            addTitle(document, entry.getKey());
            XWPFTable table = document.createTable();
            addLabels(table);
            for (var slot : entry.getValue()) {
                var row = table.getRow(slot.row() + 1);
                var cell = row.getCell(slot.col() + 1);
                writeToCell(cell, slot.getCourse().getName(),
                        respectable(slot.getLecturerName()), slot.getVenueName(), "     ");
            }
            document.createParagraph().setPageBreak(true);
        }
        document.write(out);
        out.close();
    }

    private static void venueTable(XWPFDocument document, Map<String, List<Slot>> map) throws IOException {
        FileOutputStream out = new FileOutputStream(
                ProjectFileManager.projectFilePath().getPath() + "/venues.docx");
        for (var entry : map.entrySet()) {
            if (!isICTVenue(entry.getKey())) {
                continue;
            }
            addTitle(document, entry.getKey());
            XWPFTable table = document.createTable();
            addLabels(table);
            for (var slot : entry.getValue()) {
                var row = table.getRow(slot.row() + 1);
                var cell = row.getCell(slot.col() + 1);
                writeToCell(cell, slot.getCourse().getName(),
                        slot.getLecturerName(), slot.getStudentClassName(), " \t");
            }
            document.createParagraph().setPageBreak(true);
        }
        document.write(out);
        out.close();
    }

    private static boolean isICTClass(String name) {
        String[] students = { "MSE", "INT", "BIT", "BSSM", "BSIT", "BSBT", "DMSE",
                "DIT", "DBIT", "BSCSM", "BSCIT", "BSBIT", "TVET", "CBIT" };
        for (String item : students) {
            if (name != null && name.toLowerCase().contains(item.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isICTVenue(String name) {
        return name.startsWith("MM") ||
                name.equalsIgnoreCase("workshop") ||
                name.toLowerCase().startsWith("net") ||
                name.equalsIgnoreCase("Room 1") ||
                name.equalsIgnoreCase("Hall 6");
    }

    private static String respectable(String name) {
        String[] ms = { "Macheli", "Moopisa", "Ranyali",
                "Ebisoh", "Sekopo", "Mokete", "Molapo", "Ntho", "Mathe", "Mokhachane",
                "Serutla", };
        for (String item : ms) {
            if (name != null && item.toLowerCase().contains(name.toLowerCase())) {
                return "Ms. " + name;
            }
        }

        String[] mr = { "Brotho", "Nkhatho", "Makheka", "Monaheng", "Bhila", "Nthunya",
                "Hlabeli", "Takalimane", "Jegede", "Morutwa", "Rantai", "Liphoto", "Tlali",
                "New", "Matjele", "Borotho", "Mokhamo", "Mofolo", "Mpotla", "Maseli", "Makhaola" };
        for (String item : mr) {
            if (name != null && item.toLowerCase().contains(name.toLowerCase())) {
                return "Mr. " + name;
            }
        }
        return name;
    }

    private static boolean isICTLecturer(String name) {
        String[] lecturers = { "Macheli", "Mahlakeng", "Brotho", "Nkhatho", "Moopisa", "Ranyali", "Makheka",
                "Ebisoh", "Monaheng", "Bhila", "Nthunya", "Hlabeli", "Sekopo", "Takalimane", "Jegede",
                "Mokete", "Morutwa", "Molapo", "Rantai", "Ntho", "Liphoto", "Tlali", "Mathe", "Mokhachane",
                "Serutla", "New", "Matjele", "Borotho", "Mokhamo", "Mofolo", "Mpotla", "Maseli", "Makhaola", "New 1",
                "New 2", "New 3", "TUTOR" };
        for (String item : lecturers) {
            if (name != null && item.toLowerCase().contains(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private static void writeToCell(XWPFTableCell cell, String topText,
            String leftText, String rightText, String seperator) {
        cell.setColor("F6F6F6");
        var topPara = cell.getParagraphs().get(0);
        topPara.setAlignment(ParagraphAlignment.CENTER);
        var top = topPara.createRun();
        top.setText(spelling(topText));
        top.setFontFamily(TAHOMA);
        top.setFontSize(8);
        cell.addParagraph();
        var bottomPara = cell.addParagraph();
        var left = bottomPara.createRun();
        left.setText(leftText);
        left.setFontFamily(TAHOMA);
        left.setFontSize(8);
        bottomPara.setIndentationLeft(3);
        bottomPara.createRun().setText(seperator);
        var right = bottomPara.createRun();
        right.setFontFamily(TAHOMA);
        right.setFontSize(8);
        right.setText(underscore(rightText));
    }

    private static void addTitle(XWPFDocument document, String title) {
        var p = document.createParagraph();
        var titleRun = p.createRun();
        titleRun.setText(title);
        titleRun.setFontSize(12);
        titleRun.setFontFamily(TAHOMA);
        titleRun.setColor("303030");
        titleRun.setBold(true);
        titleRun.setCapitalized(true);
        titleRun.addBreak();
    }

    private static String spelling(String name) {
        if (name.contains("Entrepreneuship")) {
            return name.replace("Entrepreneuship", "Entrepreneurship");
        } else if (name.contains("Mathemetics")) {
            return name.replace("Mathemetics", "Mathematics");
        } else if (name.contains("Descrete")) {
            return name.replace("Descrete", "Discrete");
        } else if (name.contains("Artifitial")) {
            return name.replace("Artifitial", "Artificial");
        }
        return name;
    }

    private static void addLabels(XWPFTable table) {
        XWPFTableRow hRow = table.getRow(0);
        hRow.getCell(0).setText("");
        hRow.getCell(0).setColor(LIGHT_GRAY);
        hRow.setHeight(ROW_HEIGHT);
        for (int i = 1; i <= Properties.totalSessions(); i++) {
            var cell = hRow.createCell();
            cell.setColor(LIGHT_GRAY);
            var paragraph = cell.getParagraphs().get(0);
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            paragraph.setSpacingBefore(400);
            XWPFRun run = paragraph.createRun();
            run.setFontFamily(TAHOMA);
            run.setText(Properties.sessionLabel(i));
        }

        for (int i = 1; i <= Properties.totalDays(); i++) {
            XWPFTableRow row = table.createRow();
            row.setHeight(ROW_HEIGHT);
            var cell = row.getCell(0);
            cell.setColor(LIGHT_GRAY);
            var paragraph = cell.getParagraphs().get(0);
            paragraph.setSpacingBefore(350);
            var run = paragraph.createRun();
            run.setSmallCaps(true);
            run.setText(Properties.dayLabel(i));
        }
    }

    private static String underscore(String name) {
        if (name != null) {
            if (name.contains(" (") && name.contains(")")) {
                name = name.replace(" (", "_");
                name = name.replace(")", "");
            }
            return name;
        }
        return null;
    }
}
