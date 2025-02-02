package com.HyenaBgammon.controller;

import com.HyenaBgammon.models.HistoryManager;
import com.HyenaBgammon.models.History;
import com.HyenaBgammon.view.HistoryView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HistoryController {
    private final HistoryView view;

    public HistoryController(HistoryView view) {
        this.view = view;

        initController();
    }

    private void initController() {
        loadHistoryData();
        
        view.addBackButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    private void loadHistoryData() {
        List<History> histories = HistoryManager.getHistoryList();
        if (histories.isEmpty()) {
            System.out.println("No game histories found.");
        } else {
            System.out.println("Loaded " + histories.size() + " game histories.");
        }
        view.setTableData(histories);
    }
}
