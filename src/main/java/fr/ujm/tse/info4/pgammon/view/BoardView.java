package fr.ujm.tse.info4.pgammon.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

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

    public BoardView(Game game) {
        this.game = game;
        this.board = game.getBoard();
        this.SquareButtons = new HashMap<>();
        this.setCandidate(null);  
        build();
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
        List<SixSidedDie> dice = game.getSixSidedDie();
        
        if (dieButtons != null) {
            for (DieButton dieBtn : dieButtons) {
                remove(dieBtn);
            }
        }
        dieButtons = new ArrayList<>();
        
        int size = dice.size();
        int i = 0;
        if (size > 0) {
            for (SixSidedDie die : dice) {
                DieButton btn = new DieButton(die);
                int y = (int) (252 + 40 * ((float) i - size / 2));
                btn.setBounds(427 - 173, y,
                        btn.getPreferredSize().width, btn.getPreferredSize().height);
                add(btn);
                dieButtons.add(btn);
                i++;
            }
        }
    }

    public Collection<CaseButton> getSquareButtons() {
        return SquareButtons.values();
    }
}
