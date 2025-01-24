package com.HyenaBgammon.models;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private static List<History> historyList = null;

    // Fetch history data from XML
    public static void loadHistory() {
        if (historyList != null) {
            return; // Already loaded
        }
        historyList = new ArrayList<>();
        File file = new File("save/history.xml");

        if (!file.exists()) {
            return; // No history file to load
        }

        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(file);
            Element root = document.getRootElement();

            for (Element historyElement : root.getChildren("history")) {
                String winner = historyElement.getChildText("winner");
                String loser = historyElement.getChildText("loser");
                String durationText = historyElement.getChildText("duration");
                GameDifficulty difficulty = GameDifficulty.valueOf(historyElement.getChildText("difficulty"));
                LocalDateTime endTime = LocalDateTime.parse(historyElement.getChildText("endTime"));

                Duration duration = parseDuration(durationText);
                
                historyList.add(new History(winner, loser, duration, difficulty, endTime));
            }
        } catch (Exception e) {
            System.err.println("Error loading history: " + e.getMessage());
        }
    }
    
    private static Duration parseDuration(String durationText) {
        if (durationText == null || !durationText.contains(":")) {
            throw new IllegalArgumentException("Invalid duration format: " + durationText);
        }

        String[] parts = durationText.split(":");
        long minutes = Long.parseLong(parts[0]);
        long seconds = Long.parseLong(parts[1]);

        return Duration.ofMinutes(minutes).plusSeconds(seconds);
    }

    // Save the list of histories to XML
    public static void saveHistory() {
        if (historyList == null) {
            return;
        }

        Element root = new Element("gameHistory");
        Document document = new Document(root);

        for (History record : historyList) {
            Element historyElement = new Element("history");

            Element winner = new Element("winner");
            winner.setText(record.getWinnerName());
            historyElement.addContent(winner);

            Element loser = new Element("loser");
            loser.setText(record.getLoserName());
            historyElement.addContent(loser);

            Element duration = new Element("duration");
            long minutes = record.getGameDuration().toMinutes();
            long seconds = record.getGameDuration().getSeconds() % 60;
            String formattedDuration = String.format("%02d:%02d", minutes, seconds);
            duration.setText(formattedDuration);
            historyElement.addContent(duration);

            Element difficulty = new Element("difficulty");
            difficulty.setText(record.getDifficultyLevel().toString());
            historyElement.addContent(difficulty);

            Element endTime = new Element("endTime");
            endTime.setText(record.getEndTime().toString());
            historyElement.addContent(endTime);

            root.addContent(historyElement);
        }

        try {
            File path = new File("save");
            if (!path.exists()) {
                path.mkdirs();
            }

            XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
            output.output(document, new FileOutputStream("save/history.xml"));
        } catch (IOException e) {
            System.err.println("Error saving history: " + e.getMessage());
        }
    }

    // Add a new game to the history
    public static void addHistory(History history) {
        if (historyList == null) {
            loadHistory();
        }
        historyList.add(history);
        saveHistory();
    }

    // Get the list of all histories
    public static List<History> getHistoryList() {
        if (historyList == null) {
            loadHistory();
        }
        return historyList;
    }
}

