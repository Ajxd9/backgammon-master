package fr.ujm.tse.info4.pgammon.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.ujm.tse.info4.pgammon.models.Square;
import fr.ujm.tse.info4.pgammon.models.SquareColor;
import fr.ujm.tse.info4.pgammon.models.SixSidedDie;
import fr.ujm.tse.info4.pgammon.models.Game;
import fr.ujm.tse.info4.pgammon.models.Board;

public class BoardView extends JPanel {

    private static final long serialVersionUID = -7479996235423541957L;
    
    public static final ImageIcon arrowImage = new ImageIcon("images/small_arrows.png");

    private Game game;
    private Board board;
    private HashMap<Square, CaseButton> SquareButtons;
    private CaseButton candidate;
    private List<DieButton> dieButtons;
    private JLabel[] marks; // Labels for "?" and "!"

    public BoardView(Game game) {
        this.game = game;
        this.board = game.getBoard();
        this.SquareButtons = new HashMap<>();
        marks = new JLabel[4]; // Three '?' and one '!'
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(550, 450));
        this.setCandidate(null);  
        build();
        addMarksToTriangles();
    }

    public CaseButton getCandidate() {
        return candidate;
    }

    public void setPossible(List<Square> Squares) {
        // Reinitialization
        for (CaseButton btn : SquareButtons.values()) {
            btn.setPossible(false);
        }
        for (Square c : Squares) {
            CaseButton btn = SquareButtons.get(c);
            btn.setPossible(true);
        }
    }

    public void setCandidate(CaseButton newCandidate) {
        if (newCandidate == this.candidate) return;
        
        if (this.candidate != null)
            this.candidate.setCandidated(false);
        
        newCandidate.setCandidated(true);
        this.candidate = newCandidate;
    }
    
    public void setGame(Game game) {
        this.game = game;
        this.board = game.getBoard();
    }

    public void unCandidateAll() {
        if (this.candidate != null)
            this.candidate.setCandidated(false);
        
        this.candidate = null;
    }

    private void build() {
        setOpaque(false);
        setLayout(null);
        this.setPreferredSize(new Dimension(550, 450));
        
        for (Square c : board.getSquareList()) {
            createTriangle(c.getPosition(), c);
        }
        for (Square c : board.getBarSquare()) {
            createBarSquares(c);
        }
        for (Square c : board.getVictorySquare()) {
            createVictorySquares(c);
        }
        updateDice();
    }
    
    private void addMarksToTriangles() {
        Random random = new Random();
        String[] texts = {"?", "?", "?", "!"};

        // X-coordinates for top and bottom row triangles
        int[] topRowX = {25, 58, 91, 124, 157, 190, 285, 318, 351, 384, 417, 450}; // Top row
        int[] bottomRowX = {450, 417, 384, 351, 318, 285, 190, 157, 124, 91, 58, 25}; // Bottom row

        // Adjusted Y-coordinates
        int topY = 200;    // Y-position for top row (slightly below triangle tip)
        int bottomY = 410; // Y-position for bottom row (higher for perfect alignment)

        int[][] positions = new int[4][2]; // Holds positions for all 4 markers

        // Randomly pick unique triangle positions for the markers
        boolean[] usedIndices = new boolean[12];
        for (int i = 0; i < 4; i++) {
            int row = random.nextBoolean() ? 0 : 1; // Randomly choose top or bottom row
            int index;

            // Ensure each marker is in a unique position
            do {
                index = random.nextInt(12);
            } while (usedIndices[index]);
            usedIndices[index] = true;

            // Set position based on row
            positions[i][0] = (row == 0) ? topRowX[index] : bottomRowX[index]; // X-coordinate
            positions[i][1] = (row == 0) ? topY : bottomY;                     // Adjusted Y-coordinate
        }

        // Add and position the markers
        for (int i = 0; i < marks.length; i++) {
            marks[i] = new JLabel(texts[i], SwingConstants.CENTER);
            marks[i].setFont(new Font("Arial", Font.BOLD, 18));
            marks[i].setForeground(Color.GREEN);

            // Place the marker
            marks[i].setBounds(positions[i][0], positions[i][1], 30, 30);
            add(marks[i]);
        }
    }

    private void createVictorySquares(Square c) {
        // TODO: Manage the direction of victory Squares
        // TODO: Create victory Squares
        CaseButton btn = new BarCaseButton(c, true);
        int posX = 671 - 173;
        int posY = 30;
        
        if (c.getCheckerColor() == SquareColor.BLACK)
            posY = 266;
        
        btn.setBounds(posX, posY,
                btn.getPreferredSize().width, btn.getPreferredSize().height);
        
        add(btn);
        SquareButtons.put(c, btn);
    }

    private void createBarSquares(Square c) {
        // TODO: Manage the direction of bar Squares
        CaseButton btn = new BarCaseButton(c, true);
        int posX = 426 - 173;
        int posY = 30;
        
        if (c.getCheckerColor() == SquareColor.WHITE)
            posY = 266;
        
        btn.setBounds(posX, posY,
                btn.getPreferredSize().width, btn.getPreferredSize().height);
        
        add(btn);
        SquareButtons.put(c, btn);
    }

    private void createTriangle(final int position, final Square c) {
        int num = 25 - position;
        Point p = new Point(0, 0);
        if (num <= 6) {
            p = new Point(454 - (num - 1) * 33, 30);
        } else if (num <= 12) {
            p = new Point(392 - 173 - (num - 7) * 33, 30);
        } else if (num <= 18) {
            p = new Point(392 - 173 + (num - 18) * 33, 233);
        } else if (num <= 24) {
            p = new Point(454 + (num - 24) * 33, 233);
        }
        SquareColor color = (num % 2 != 0) ? SquareColor.WHITE : SquareColor.BLACK;
        TriangleCaseButton triangle = new TriangleCaseButton(c, color, (num >= 13));
        triangle.setBounds(p.x, p.y,
                triangle.getPreferredSize().width, triangle.getPreferredSize().height);
        add(triangle);
        SquareButtons.put(c, triangle);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create(); 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
        
        Paint p;
        int h = getHeight(); 
        int w = getWidth(); 
        
        // Background
        p = new Color(0x333333);
        g2.setPaint(p); 
        g2.fillRect(0, 0, w, h); 
        
        // Board background
        p = new Color(0xCCCCCC);
        g2.setPaint(p); 
        g2.fillRect(226 - 173, 61 - 30, 435 , 382); 

        p = new Color(0x333333);
        g2.setPaint(p); 
        g2.fillRect(252, 31, 36 , 387); 

        g2.drawImage(arrowImage.getImage(), 12, 200, this);
        
        g2.dispose(); 
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        // Border
        Graphics2D g2 = (Graphics2D) g.create(); 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 

        Paint p;
        int h = getHeight(); 
        int w = getWidth(); 
        p = new Color(0x808080);
        g2.setStroke(new BasicStroke(10.0f));
        g2.setPaint(p); 
        g2.drawRect(0, 0, w - 1, h - 1);
        
        g2.dispose(); 
    }

    public void updateDice() {
        // Retrieve the list of dice from the game
        List<SixSidedDie> dice = game.getSixSidedDie();

        // Remove existing DieButtons if they exist
        if (dieButtons != null) {
            for (DieButton dieBtn : dieButtons) {
                remove(dieBtn);
            }
        }

        // Reset the dieButtons list
        dieButtons = new ArrayList<>();

        int size = dice.size(); // Total number of dice
        int i = 0;

        // Dynamically create and position buttons for each die
        if (size > 0) {
            for (SixSidedDie die : dice) {
                DieButton btn = new DieButton(die); // Create DieButton for the current die

                // Adjust the Y-position to display dice vertically
                int y = (int) (252 + 40 * ((float) i - size / 2));

                // Set button position on the GUI
                btn.setBounds(427 - 173, y,
                        btn.getPreferredSize().width, btn.getPreferredSize().height);

                // Add the button to the display
                add(btn);
                dieButtons.add(btn);

                i++;
            }
        }

        // Refresh the GUI to ensure the updated dice are displayed
        revalidate();
        repaint();
    }


    public Collection<CaseButton> getSquareButtons() {
        return SquareButtons.values();
    }
}
