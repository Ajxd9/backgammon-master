package com.HyenaBgammon.view;

import javax.swing.*;
import com.HyenaBgammon.models.History;
import com.HyenaBgammon.models.HistoryManager;
import com.HyenaBgammon.models.HistoryObserver;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.time.Duration;
import java.util.List;

public class HistoryView extends MonochromeView implements HistoryObserver {

    private static final long serialVersionUID = 1L;
    private MonochromeLabel titleLabel;
    private MonochromeButton backButton;
    private JTable historyTable;

    public HistoryView(ActionListener backListener) {
        setLayout(null);
        build(backListener);
        HistoryManager.addObserver(this); // Register as an observer

    }
    
   
    @Override
    public void onHistoryUpdated(List<History> updatedHistory) {
        System.out.println("HistoryView received updated history. Total records: " + updatedHistory.size());
        setTableData(updatedHistory);
    }


    private void build(ActionListener backListener) {
    	
    	titleLabel = new MonochromeLabel("Game History");
    	titleLabel.setFont(titleLabel.getFont().deriveFont(36f));
        titleLabel.setBounds(250, 50, 300, 50);
        add(titleLabel);

        backButton = new MonochromeButton("Back");
        backButton.setBounds(30, 52, 100, 40);
        backButton.addActionListener(backListener);
        add(backButton);
        
        String[] columnNames = {"Winner", "Date", "Time", "Difficulty", "Loser"};
        Object[][] rowData = {};
        historyTable = new JTable(rowData, columnNames);
        historyTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBounds(100, 200, 600, 300);
        add(scrollPane);
    
    }
    
    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
    
    public void setTableData(List<History> histories) {
        String[] columnNames = {"Winner", "Date", "Time", "Difficulty", "Loser"};
        Object[][] rowData = new Object[histories.size()][5];

        for (int i = 0; i < histories.size(); i++) {
            History history = histories.get(i);
            rowData[i][0] = history.getWinnerName();
            rowData[i][1] = history.getEndTime().toLocalDate().toString();
            rowData[i][2] = formatDuration(history.getGameDuration());
            rowData[i][3] = history.getDifficultyLevel().toString();
            rowData[i][4] = history.getLoserName();
        }

        historyTable.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames));
    }
    
    private String formatDuration(Duration duration) {
        long minutes = duration.toMinutes();
        long seconds = duration.getSeconds() % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Paint p;
        int h = getHeight();
        int w = getWidth();

        p = new RadialGradientPaint(new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0),
                getHeight(),
                new float[]{0.0f, 0.8f},
                new Color[]{new Color(0x333333), new Color(0x000000)},
                RadialGradientPaint.CycleMethod.NO_CYCLE);

        g2.setPaint(p);
        g2.fillRect(0, 0, w, h);

        p = new Color(0x808080);
        g2.setStroke(new BasicStroke(5.0f));
        g2.setPaint(p);
        g2.drawRect(2, 0, w - 5, h - 5);

        g2.dispose();
    }
    
}
