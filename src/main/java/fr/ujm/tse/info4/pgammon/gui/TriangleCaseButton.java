package fr.ujm.tse.info4.pgammon.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;

import fr.ujm.tse.info4.pgammon.models.Case;
import fr.ujm.tse.info4.pgammon.models.CouleurCase;

/**
 * Provides the component for displaying triangle cases
 * @author Jean-Mi
 *
 */
public class TriangleCaseButton extends CaseButton {
    private static final long serialVersionUID = -7438830652988320775L;
    private CouleurCase color;

    private final int MAX_CHECKERS_DRAWN = 5;
    private final int CHECKER_SPACING = 27;

    private boolean isDirectionUp;

    /**
     * Triangle synchronized with a case
     * @param _case Associated case
     * @param _color Physical color of the case
     * @param _isDirectionUp Direction of the case
     */
    public TriangleCaseButton(Case _case, CouleurCase _color, boolean _isDirectionUp) {
        super(_case);
        color = _color;
        isDirectionUp = _isDirectionUp;
        build();
    }

    public TriangleCaseButton(Case _case, CouleurCase _color) {
        super(_case);
        color = _color;
        isDirectionUp = true;
        build();
    }

    private void build() {
        setOpaque(false);
        setLayout(null);
        setPreferredSize(new Dimension(33, 180));

        updateData();
    }

    private void updateData() {
        // Reserved for data updates if needed
    }

    @Override
    protected void paintComponent(Graphics g) {
        drawTriangle(g);
        drawCheckers(g);
    }

    private void drawCheckers(Graphics g) {
        Case c = getCase();
        if (c == null) return;

        CouleurCase checkerColor = c.getCouleurDame();

        if (checkerColor == CouleurCase.EMPTY || c.getNbDame() == 0) return;

        int numCheckers = c.getNbDame();
        if (isCandidate()) numCheckers--;

        int count = Math.min(numCheckers, MAX_CHECKERS_DRAWN);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int offset = 0;
        if (model.isRollover()) offset++;
        if (model.isPressed()) offset++;

        ImageIcon icon;
        if (getCase().getCouleurDame() == CouleurCase.EMPTY) {
            icon = new ImageIcon();
        } else if (isPossible()) {
            icon = (getCase().getCouleurDame() == CouleurCase.WHITE) ? iconeAideBlanc : iconeAideNoir;
        } else {
            icon = (getCase().getCouleurDame() == CouleurCase.WHITE) ? iconeBlanche : iconeNoire;
        }

        for (int i = 0; i < count; i++) {
            int y;
            if (isDirectionUp) {
                y = getHeight() - ((i + 1) * (CHECKER_SPACING) + (i + 1) * offset);
            } else {
                y = i * (CHECKER_SPACING) + (i + 1) * offset;
            }

            g2.drawImage(icon.getImage(), 0, y, this);
        }

        if (numCheckers > MAX_CHECKERS_DRAWN) {
            String num = Integer.toString(numCheckers);
            g2.setFont(new Font("Arial", Font.BOLD, 18));

            if (c.getCouleurDame() == CouleurCase.WHITE) {
                g2.setColor(new Color(0x111111));
            } else {
                g2.setColor(new Color(0xCCCCCC));
            }

            int y;
            if (isDirectionUp) {
                y = getHeight() - (MAX_CHECKERS_DRAWN) * (CHECKER_SPACING + offset) - 6 + CHECKER_SPACING;
            } else {
                y = MAX_CHECKERS_DRAWN * (CHECKER_SPACING + offset) - 6;
            }

            g2.drawChars(num.toCharArray(), 0, num.length(), 11 - (num.length() - 1) * 5, y);
        }

        if (isCandidate() && c.getNbDame() > 0) {
            int i = count + 1;
            int y;
            if (isDirectionUp) {
                y = (int) (getHeight() - (i + 0.2) * (CHECKER_SPACING) + (i + 1) * (-offset));
            } else {
                y = (int) ((i - 0.8) * (CHECKER_SPACING) + (i + 1) * offset);
            }
            ImageIcon iconTransp = (getCase().getCouleurDame() == CouleurCase.WHITE) ? iconeBlancheTransp : iconeNoireTransp;
            g2.drawImage(iconTransp.getImage(), 0, y, this);
        }

        g2.dispose();
    }

    private void drawTriangle(Graphics g) {
        updateData();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Paint p;
        int w = getWidth();
        int h = getHeight();

        int triangleWidth = w;
        int triangleHeight = (int) (h * 0.96);
        if (color == CouleurCase.WHITE) triangleHeight *= 0.9;

        Point p1;
        Point p2;
        Point p3;

        if (isDirectionUp) {
            p1 = new Point((w - triangleWidth) / 2, h);
            p2 = new Point((w + triangleWidth) / 2, h);
            p3 = new Point(w / 2, h - triangleHeight);
        } else {
            p1 = new Point((w - triangleWidth) / 2, 0);
            p2 = new Point((w + triangleWidth) / 2, 0);
            p3 = new Point(w / 2, triangleHeight);
        }
        int[] xs = {p1.x, p2.x, p3.x};
        int[] ys = {p1.y, p2.y, p3.y};
        Polygon triangle = new Polygon(xs, ys, xs.length);

        if (isPossible()) {
            p = new Color(0x000099);
        } else if (model.isPressed()) {
            p = (getColor() == CouleurCase.WHITE) ? new Color(0xFFFFFF) : new Color(0x777777);
        } else {
            if (model.isRollover()) {
                p = (getColor() == CouleurCase.WHITE) ? new Color(0xEEEEEE) : new Color(0x555555);
            } else {
                p = (getColor() == CouleurCase.WHITE) ? new Color(0xCECECE) : new Color(0x333333);
            }
        }
        g2.setPaint(p);
        g2.fillPolygon(triangle);

        if (isPossible()) {
            p = new Color(0x0000FF);
        } else if (model.isPressed()) {
            p = (getColor() == CouleurCase.WHITE) ? new Color(0xAAAAAA) : new Color(0x666666);
        } else {
            p = (getColor() == CouleurCase.WHITE) ? new Color(0x888888) : new Color(0x444444);
        }
        g2.setStroke(new BasicStroke(2.0f));
        g2.setPaint(p);
        g2.drawPolygon(triangle);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border painting
    }

    public CouleurCase getColor() {
        return color;
    }

    public void setColor(CouleurCase color) {
        this.color = color;
    }
}
