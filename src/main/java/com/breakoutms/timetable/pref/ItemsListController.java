package com.breakoutms.timetable.pref;

import java.util.List;

import com.breakoutms.timetable.db.DAO;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public abstract class ItemsListController<T> {

    @FXML private ListView<T> listView;
    
    private final DAO<T> dao;
    
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
    	var list = createBean();
    	if(list == null || list.isEmpty()) return;
    	for (T value : list) {
        	if(!listView.getItems().contains(value)) {
            	listView.getItems().add(value);
            	dao.save(value);
        	}
		}
    	clear();
    }


	@FXML
    void remove(ActionEvent event) {
		var item = listView.getSelectionModel().getSelectedItem();
		listView.getItems().remove(item);
		dao.delete(item);
    }
	
	protected abstract void clear();
	
    protected abstract List<T> createBean();
}
