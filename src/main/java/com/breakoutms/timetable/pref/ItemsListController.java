package com.breakoutms.timetable.pref;

import java.util.ArrayList;
import java.util.List;

import com.breakoutms.timetable.db.DAO;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import org.controlsfx.control.SearchableComboBox;

public abstract class ItemsListController<T> {

    @FXML
	protected ListView<T> listView;
    
    protected final DAO<T> dao;
    
	public ItemsListController(Class<T> type) {
		this.dao = new DAO<>(type);
	}
	
    @FXML
    void initialize() {
    	var items = dao.all();
    	if(!items.isEmpty()) {
    		listView.setItems(FXCollections.observableArrayList(items));
    	}
    	postInit();
    }

    protected void postInit() {
    	
	}

	@FXML
    void add(ActionEvent event) {
    	var bean = createBean();
    	if(bean == null) return;
		if(!listView.getItems().contains(bean)) {
			dao.save(bean);
			listView.getItems().add(bean);
			comboBox().getItems().add(bean);
		}
    	clear();
    }


	@FXML
    void remove(ActionEvent event) {
		var item = listView.getSelectionModel().getSelectedItem();
		listView.getItems().remove(item);
		dao.delete(item);
		comboBox().getItems().remove(item);
    }
	
	protected abstract void clear();
	
    protected abstract T createBean();

	protected abstract ComboBox<T> comboBox();
}
