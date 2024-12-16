package history;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class HistoryPage extends JFrame {

    public HistoryPage() {
        
    	setTitle("History");
        setLayout(new BorderLayout());
        
        ///////////////////////////////////header///////////////////////////////////////
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 220));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(75, 50, 10, 90));
        
        //Title
        JLabel title = new JLabel("HISTORY", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 50));
        title.setForeground(new Color(128, 64, 0));
        headerPanel.add(title, BorderLayout.CENTER);

        //Subtitle
        JLabel subtitle = new JLabel("<html>Welcome to your Game History!<br>"
        		+ "Here, you can review the details of your past games, including wins, losses and other important stats.</html>",
        		JLabel.CENTER);
        subtitle.setFont(new Font("Serif", Font.PLAIN, 23));
        subtitle.setForeground(Color.BLACK);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        //making the page a Desktop sized screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); 
        
        //the back button
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/history/tools/button.jpg")); //image for the background of the button
        Image scaledImage = originalIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        Icon scaledIcon = new ImageIcon(scaledImage);
        //creating the button
        JButton backButton = new JButton();
        backButton.setIcon(scaledIcon);
        backButton.setPreferredSize(new Dimension(70, 70));
        headerPanel.add(backButton, BorderLayout.WEST);
        //added for later (delete when connecting) !!!
        backButton.addActionListener(e -> {dispose(); });
        
        ///////////////////////// Table /////////////////////////
        //fetching the data that was created in the data class (would be useful later when we implement a Database)
        GameHistoryData results = new GameHistoryData();
        List<GameRecord> gameRecords = results.getGameRecords();
        String[] columnNames = {"Player", "Date", "Time", "Result", "Difficulty", "Questions Answered", "Opponent"};
        Object[][] data = new Object[gameRecords.size()][columnNames.length];
        for (int i = 0; i < gameRecords.size(); i++) {
            GameRecord record = gameRecords.get(i);
            data[i][0] = record.getPlayer();
            data[i][1] = record.getDate();
            data[i][2] = record.getTime();
            data[i][3] = record.getResult();
            data[i][4] = record.getDifficulty();
            data[i][5] = record.getQuestionsAnswered();
            data[i][6] = record.getOpponent();
        }
        
        //building the table
        DefaultTableModel table = new DefaultTableModel(data, columnNames);
        JTable historyTable = new JTable(table);
        historyTable.setRowHeight(90);
        historyTable.setFont(new Font("SansSerif", Font.PLAIN, 22));
        historyTable.setPreferredScrollableViewportSize(new Dimension(1750, 450));
        
        //header
        JTableHeader header = historyTable.getTableHeader();
        header.setForeground(Color.BLACK);
        header.setFont(new Font("SansSerif", Font.PLAIN, 26));

        // Add custom coloring for the state of the player, green for "Victory" and red for "Defeat"
        historyTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String result = table.getValueAt(row, 3).toString();
                if ("Victory".equals(result)) {
                    cell.setBackground(new Color(85, 170, 85)); // Light green for victory
                    cell.setForeground(Color.BLACK);
                } else if ("Defeat".equals(result)) {
                    cell.setBackground(new Color(200, 85, 85)); // Light red for defeat
                    cell.setForeground(Color.BLACK);
                } else {
                    cell.setBackground(Color.WHITE); // Default background
                    cell.setForeground(Color.BLACK); // Default text color
                }
                return cell;
            } });
        
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(new Color(245, 245, 220));
        tablePanel.add(new JScrollPane(historyTable)); //adding Scroll bar
        add(tablePanel, BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(90, 10, 10, 10));
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }

	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HistoryPage historyPage = new HistoryPage();
            historyPage.setVisible(true);
        });
    }
}

