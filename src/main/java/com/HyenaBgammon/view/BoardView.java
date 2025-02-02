package com.HyenaBgammon.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.HyenaBgammon.models.Square;
import com.HyenaBgammon.models.SquareColor;
import com.HyenaBgammon.models.SquareType;
import com.HyenaBgammon.models.SixSidedDie;
import com.HyenaBgammon.models.Game;
import com.HyenaBgammon.controller.BoardController;
import com.HyenaBgammon.models.Board;

public class BoardView extends JPanel {

    private static final long serialVersionUID = -7479996235423541957L;
    private static final ImageIcon QUESTION_ICON = new ImageIcon("images/question_icon.png");
    private static final ImageIcon SURPRISE_ICON = new ImageIcon("images/surprise_icon.png");
    public static final ImageIcon arrowImage = new ImageIcon("images/small_arrows.png");
    private Set<Square> questionTriangles = new HashSet<>();
    private Set<Square> surpriseTriangles = new HashSet<>();

    private Game game;
    private Board board;
    private Set<Square> questionStations = new HashSet<>();
    private Set<Square> surpriseStations = new HashSet<>();
    private HashMap<Square, CaseButton> SquareButtons;
    private CaseButton candidate;
    private List<DieButton> dieButtons;
    private JLabel[] marks; // Labels for "?" and "!"
    private BoardController boardController;


    public BoardView(Game game) {
        this.game = game;
        this.board = game.getBoard();
        this.SquareButtons = new HashMap<>();
       // marks = new JLabel[4]; // Three '?' and one '!'
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(550, 450));
        this.setCandidate(null);  
        build();
        initializeMarkers();
        addMarkers();
    }
    
    public BoardView(Game game, BoardController boardController) {
        this.game = game;
        this.board = game.getBoard();
        this.boardController = boardController; // Save reference to the controller
        this.SquareButtons = new HashMap<>();
        marks = new JLabel[4]; // Three '?' and one '!'
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(550, 450));
        this.setCandidate(null);
        build();
        //addMarksToTriangles();
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
        setPreferredSize(new Dimension(550, 450));

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
    private void initializeMarkers() {
        Random random = new Random();
        List<Square> allSquares = new ArrayList<>(board.getSquareList());

        // Ensure 3 unique question mark squares
        while (questionTriangles.size() < 3) {
            Square square = allSquares.get(random.nextInt(allSquares.size()));
            if (square.getSquareType() == null || square.getSquareType() == SquareType.REGULAR) {
                questionTriangles.add(square);
                square.setSquareType(SquareType.QUESTION);
            }
        }

        // Ensure 1 unique surprise mark square (not overlapping with question stations)
        while (surpriseTriangles.size() < 1) {
            Square square = allSquares.get(random.nextInt(allSquares.size()));
            if (square.getSquareType() == null || square.getSquareType() == SquareType.REGULAR) {
                surpriseTriangles.add(square);
                square.setSquareType(SquareType.SURPRISE);
            }
        }

        // Remaining squares are regular
        for (Square square : allSquares) {
            if (!questionTriangles.contains(square) && !surpriseTriangles.contains(square)) {
                square.setSquareType(SquareType.REGULAR);
            }
        }
    }




    private void addMarkers() {
        // Add `?` icons for question triangles
        for (Square square : questionTriangles) {
            addIconToSquare(square, QUESTION_ICON);
        }

        // Add `!` icon for surprise triangle
        for (Square square : surpriseTriangles) {
            addIconToSquare(square, SURPRISE_ICON);
        }

        revalidate();
        repaint();
    }

    // method to check tringle postion : 
    
    public String getTriangleType(Square square) {
        if (square.getSquareType() == SquareType.QUESTION) {
            return "QUESTION";
        } else if (square.getSquareType() == SquareType.SURPRISE) {
            return "SURPRISE";
        } else {
            return "REGULAR";
        }
    }


    private void addIconToSquare(Square square, ImageIcon originalIcon) {
        CaseButton caseButton = SquareButtons.get(square);

        if (caseButton != null) {
            // Resize the icon to be smaller
            Image scaledImage = originalIcon.getImage().getScaledInstance(
                    caseButton.getWidth() / 4, // Quarter the width of the triangle
                    caseButton.getHeight() / 4, // Quarter the height of the triangle
                    Image.SCALE_SMOOTH // Smooth scaling for better quality
            );

            // Create a new ImageIcon with the resized image
            ImageIcon resizedIcon = new ImageIcon(scaledImage);

            // Add the icon as a JLabel
            JLabel iconLabel = new JLabel(resizedIcon);
            iconLabel.setBounds(
                    caseButton.getX() + caseButton.getWidth() / 2 - resizedIcon.getIconWidth() / 2, // Center horizontally
                    caseButton.getY() + caseButton.getHeight() / 4 - resizedIcon.getIconHeight() / 2, // Slightly above the triangle center
                    resizedIcon.getIconWidth(),
                    resizedIcon.getIconHeight()
            );

            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iconLabel.setVerticalAlignment(SwingConstants.TOP);

            // Add the icon to the panel
            add(iconLabel);

            // Bring the icon to the front to ensure it appears above the triangle
            setComponentZOrder(iconLabel, 0);
        }
    }




    private void createVictorySquares(Square c) {
        // Create victory squares
        CaseButton btn = new BarCaseButton(c, true);
        int posX = 671 - 173;
        int posY = (c.getCheckerColor() == SquareColor.BLACK) ? 266 : 30;

        btn.setBounds(posX, posY, btn.getPreferredSize().width, btn.getPreferredSize().height);
        add(btn);
        SquareButtons.put(c, btn);
    }

    private void createBarSquares(Square c) {
        // Create bar squares
        CaseButton btn = new BarCaseButton(c, true);
        int posX = 426 - 173;
        int posY = (c.getCheckerColor() == SquareColor.WHITE) ? 266 : 30;

        btn.setBounds(posX, posY, btn.getPreferredSize().width, btn.getPreferredSize().height);
        add(btn);
        SquareButtons.put(c, btn);
    }

    private void createTriangle(final int position, final Square c) {
        int num = 25 - position;
        Point p = calculateTrianglePosition(num);
        SquareColor color = (num % 2 != 0) ? SquareColor.WHITE : SquareColor.BLACK;

        TriangleCaseButton triangle = new TriangleCaseButton(c, color, (num >= 13));
        triangle.setBounds(p.x, p.y, triangle.getPreferredSize().width, triangle.getPreferredSize().height);
        add(triangle);
        SquareButtons.put(c, triangle);
    }
    private Point calculateTrianglePosition(int num) {
        if (num <= 6) {
            return new Point(454 - (num - 1) * 33, 30);
        } else if (num <= 12) {
            return new Point(392 - 173 - (num - 7) * 33, 30);
        } else if (num <= 18) {
            return new Point(392 - 173 + (num - 18) * 33, 233);
        } else {
            return new Point(454 + (num - 24) * 33, 233);
        }
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

/*    public void updateDice() {
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
*/
    
    
    
   
    public void updateDice() {
        List<SixSidedDie> dice = game.getSixSidedDie();

        if (dieButtons != null) {
            for (DieButton dieBtn : dieButtons) {
                remove(dieBtn);
            }
        }

        dieButtons = new ArrayList<>();
        int size = dice.size();
        int i = 0;

        for (SixSidedDie die : dice) {
            DieButton btn = new DieButton(die);
            int y = (int) (252 + 40 * ((float) i - size / 2));
            btn.setBounds(427 - 173, y, btn.getPreferredSize().width, btn.getPreferredSize().height);

            btn.addActionListener(e -> {
                boardController.handleDicePress(die);
            });
            
            add(btn);
            dieButtons.add(btn);
            i++;
        }

        revalidate();
        repaint();
    }
    
    
    
 
    	
    public void updateMarkers() {
        questionStations.clear();
        surpriseStations.clear();
        initializeMarkers();
    }
    public Collection<CaseButton> getSquareButtons() {
        return SquareButtons.values();
    }
}
