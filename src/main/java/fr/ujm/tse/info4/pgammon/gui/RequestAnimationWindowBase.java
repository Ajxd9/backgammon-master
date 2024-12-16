package fr.ujm.tse.info4.pgammon.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.SortedSet;

public class RequestAnimationWindowBase extends AnimatedTransitionBase {

    private static final long serialVersionUID = -4787023438548267991L;
    private char[] titleChars;
    private char[] textChars;
    private boolean isClosing;
    private SortedSet<String> responses;
    private HashMap<MonochromeButton, Integer> buttonMappings;
    private double quadratic;

    public RequestAnimationWindowBase(String title, SortedSet<String> responses) {
        super(10, 300);
        setLayout(null);
        isClosing = false;
        setTitle(title);
        setText("");
        this.responses = responses;
        buildResponses();
        setOpaque(false);
    }

    public RequestAnimationWindowBase(String title, String text) {
        super(10, 300);
        setLayout(null);
        isClosing = false;
        setTitle(title);
        setText(text);
        this.responses = null;
        buildResponses();
        setOpaque(false);
    }

    public RequestAnimationWindowBase(String title) {
        super(10, 300);
        setLayout(null);
        isClosing = false;
        setTitle(title);
        setText("");
        this.responses = null;
        buildResponses();
        setOpaque(false);
    }

    private void buildResponses() {
        buttonMappings = new HashMap<>();
        if (responses == null) {
            addMouseListener(new MouseListener() {
                @Override
                public void mouseReleased(MouseEvent arg0) {
                    close();
                    fireActionPerformed(null);
                }

                @Override
                public void mousePressed(MouseEvent arg0) {}

                @Override
                public void mouseExited(MouseEvent arg0) {}

                @Override
                public void mouseEntered(MouseEvent arg0) {}

                @Override
                public void mouseClicked(MouseEvent arg0) {}
            });
            return;
        }
        int i = 0;
        for (String response : responses) {
            MonochromeButton btn = new MonochromeButton(response);
            btn.setName(response);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    close();
                    MonochromeButton btn = (MonochromeButton) evt.getSource();
                    String text = btn.getName();
                    fireActionPerformed(new ActionEvent(evt.getSource(), 0, text));
                }
            });
            add(btn);
            btn.setBounds(600 - 200 * i, 280, 150, 45);
            buttonMappings.put(btn, i);
            i++;
        }
    }

    public void close() {
        isClosing = true;
        start();
    }

    public void setTitle(String title) {
        titleChars = title.toCharArray();
    }

    @Override
    public void setText(String text) {
        textChars = text.toCharArray();
    }

    private Point getTitlePosition() {
        return new Point(100, (int) (245 + 400 * quadratic));
    }

    private Point getTextPosition() {
        return new Point(600 - textChars.length * 11, (int) (315 + 500 * quadratic));
    }

    private void updateQuadraticTransition() {
        quadratic = Math.pow((0.5 - getRatio()), 3);
    }

    private float getTitleAlpha() {
        float alpha = 2 * (0.5f - getRatio());
        alpha *= alpha;
        alpha = 1 - alpha;
        return Math.max(0, Math.min(alpha, 1));
    }

    private float getBGAlpha() {
        float alpha = 2 * (0.5f - getRatio());
        alpha *= alpha;
        alpha = 1 - alpha;
        return Math.max(0, Math.min(alpha, 1));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        updateQuadraticTransition();
        drawFullBackground(g2);
        drawBanner(g2);
        drawTitle(g2);
        drawText(g2);
        drawButtons(g2);

        g2.dispose();
        paintComponents(g);
    }

    private void drawButtons(Graphics2D g2) {
        Collection<MonochromeButton> buttons = buttonMappings.keySet();
        for (MonochromeButton btn : buttons)
            btn.setAlpha(getBGAlpha());
    }

    private void drawTitle(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 45));
        g2.setColor(new Color(0xFFFFFF));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getTitleAlpha()));

        Point p = getTitlePosition();
        g2.drawChars(titleChars, 0, titleChars.length, p.x, p.y);
    }

    private void drawText(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 30));
        g2.setColor(new Color(0xFFFFFF));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getTitleAlpha()));

        Point p = getTextPosition();
        g2.drawChars(textChars, 0, textChars.length, p.x, p.y);
    }

    private void drawBanner(Graphics2D g2) {
        int w = getWidth();

        Paint p;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f * getBGAlpha()));
        p = new Color(0x333333);
        g2.setPaint(p);
        g2.fillRect(0, 200, w, 150);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    private void drawFullBackground(Graphics2D g2) {
        int h = getHeight();
        int w = getWidth();

        Paint p;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f * getBGAlpha()));
        p = new Color(0);
        g2.setPaint(p);
        g2.fillRect(0, 0, w, h);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (!isClosing && 2 * value > duration)
            stop();
    }
}

