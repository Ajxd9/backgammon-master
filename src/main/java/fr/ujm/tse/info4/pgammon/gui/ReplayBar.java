package fr.ujm.tse.info4.pgammon.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import fr.ujm.tse.info4.pgammon.models.SixSidedDie;
import fr.ujm.tse.info4.pgammon.models.Movement;
import fr.ujm.tse.info4.pgammon.models.Turn;

/**
 * Automatic replay bar. The bar provides 4 buttons and a display
 * of the turns.
 * @author Jean-Mi
 */
public class ReplayBar extends JPanel {
    private static final long serialVersionUID = 1318001554445843500L;
    private List<Turn> turns;
    private LinkedList<SixSidedDie> allDice;
    private LinkedList<Movement> allMovements;
    private int current;
    private int oldCurrent;
    private int total;
    private final int SEPARATION = 30;
    private final int ELEMENTS_TO_SHOW = 13;
    private final int DELAY = 30;
    private JPanel elementContainer;

    public static final ImageIcon nextArrow = new ImageIcon("images/bar_btn_next.png");
    public static final ImageIcon prevArrow = new ImageIcon("images/bar_btn_prev.png");
    public static final ImageIcon endArrow = new ImageIcon("images/bar_btn_end.png");
    public static final ImageIcon startArrow = new ImageIcon("images/bar_btn_begin.png");
    public static final ImageIcon arrowIcon = new ImageIcon("images/arrow_bar.png");

    private JButton nextBtn;
    private JButton prevBtn;
    private JButton endBtn;
    private JButton startBtn;

    private JLabel nextLabel;
    private JLabel prevLabel;
    private JLabel endLabel;
    private JLabel startLabel;
    private JLabel arrowLabel;

    private Timer timer;
    private int currentPosition;
    private int finalPosition;
    private boolean advancing;

    /**
     * Creates the full bar.
     * @param turns List of turns.
     */
    public ReplayBar(List<Turn> turns) {
        this.turns = turns;
        current = 0;
        oldCurrent = 0;
        advancing = false;
        initialize();
        build();
        initializePosition();
    }

    /**
     * Changes the list of turns.
     * @param turns List of turns.
     */
    public void setTurns(List<Turn> turns) {
        this.turns = turns;
        current = 0;
        oldCurrent = 0;
        initialize();
        initializePosition();
        rebuild();
    }

    @Override
    public void setVisible(boolean flag) {
        super.setVisible(flag);
        if (flag)
            timer.start();
        else
            timer.stop();
    }

    /**
     * Goes to the given movement.
     * @param movement Movement to navigate to.
     * @param isAdvancing Indicates whether moving forward or backward.
     */
    public void goTo(Movement movement, boolean isAdvancing) {
        advancing = isAdvancing;
        setCurrent(getIndexOf(movement));
    }

    /**
     * Deprecated: Does not handle direction.
     * @param movement
     */
    @Deprecated
    public void goTo(Movement movement) {
        setCurrent(getIndexOf(movement));
    }

    /**
     * Goes to the next non-null movement.
     * Deprecated: For synchronization, use goTo().
     */
    @Deprecated
    public void goNext() {
        int index = current + 1;
        while (index < total - 1 && allMovements.get(index) == null)
            index++;
        setCurrent(index);
    }

    /**
     * Goes to the previous non-null movement.
     * Deprecated: For synchronization, use goTo().
     */
    @Deprecated
    public void goPrevious() {
        int index = current - 1;
        while (index > 0 && allMovements.get(index) == null)
            index--;

        setCurrent(index);
    }

    /**
     * Goes back to the first movement.
     */
    public void goToStart() {
        advancing = false;
        setCurrent(0);
    }

    /**
     * Goes to the last movement.
     */
    public void goToEnd() {
        setCurrent(total - 1);
        advancing = false;
    }

    private void setCurrent(int value) {
        if (value < 0)
            value = 0;
        if (value >= total)
            value = total - 1;

        oldCurrent = current;
        current = value;
        rebuild();
    }

    private void initialize() {
        allDice = new LinkedList<>();
        allMovements = new LinkedList<>();

        if (turns != null) {
            for (Turn turn : turns) {
                int length = turn.getDice().size();
                List<SixSidedDie> dice = turn.getDice();
                List<Movement> movements = turn.getMovements();

                for (int i = 0; i < length; i++) {
                    Movement movement = null;
                    SixSidedDie die = new SixSidedDie(dice.get(i).getColor(), dice.get(i).getValue());
                    if (movements != null && movements.size() > i) {
                        movement = movements.get(i);
                    }
                    allDice.add(die);
                    allMovements.add(movement);
                }
            }
        }
        total = allDice.size();
    }

    private void build() {
        setOpaque(false);
        setPreferredSize(new Dimension(400, 200));
        setLayout(null);

        elementContainer = new JPanel();
        elementContainer.setLayout(null);
        elementContainer.setOpaque(false);

        nextLabel = new JLabel(nextArrow);
        nextLabel.setBounds(660, 10, 47, 84);

        prevLabel = new JLabel(prevArrow);
        prevLabel.setBounds(100, 10, 47, 84);

        endLabel = new JLabel(endArrow);
        endLabel.setBounds(700, 10, 47, 84);

        startLabel = new JLabel(startArrow);
        startLabel.setBounds(50, 10, 47, 84);

        arrowLabel = new JLabel(arrowIcon);
        arrowLabel.setBounds(376, 10, 47, 84);

        add(nextLabel);
        add(prevLabel);
        add(endLabel);
        add(startLabel);
        add(arrowLabel);

        nextBtn = new ReplayBarButton("next");
        prevBtn = new ReplayBarButton("prev");
        endBtn = new ReplayBarButton("end");
        startBtn = new ReplayBarButton("start");
        startBtn.setBounds(0, 0, 370, 100);
        prevBtn.setBounds(100, 0, 270, 100);
        nextBtn.setBounds(430, 0, 270, 100);
        endBtn.setBounds(430, 0, 370, 100);
        add(nextBtn);
        add(prevBtn);
        add(endBtn);
        add(startBtn);
        add(elementContainer);

        rebuild();
    }

    private void rebuild() {
        elementContainer.setPreferredSize(new Dimension((total) * 30, 100));

        elementContainer.removeAll();
        int min = Math.min(current, oldCurrent);
        int max = Math.max(current, oldCurrent);
        for (int i = 0; i < ELEMENTS_TO_SHOW; i++) {
            putElement(min - 1 - i);
            putElement(max + 1 + i);
        }
        for (int i = min; i <= max; i++) {
            putElement(i);
        }
        int advanceOffset = 0;
        if (advancing)
            advanceOffset = -30;
        finalPosition = (getSize().width) / 2 + advanceOffset - current * SEPARATION;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        rebuild();
    }

    private void putElement(int index) {
        if (index < 0 || index >= total)
            return;
        DieSixFaces die = allDice.get(index);
        Movement movement = allMovements.get(index);

        ReplayBarElement element = new ReplayBarElement(die, movement);
        element.setBounds(SEPARATION * index, 0,
                element.getPreferredSize().width, element.getPreferredSize().height);
        elementContainer.add(element);
    }

    private void initializePosition() {
        elementContainer.setBounds(finalPosition, 0, elementContainer.getPreferredSize().width, elementContainer.getPreferredSize().height);
        if (timer == null)
            timer = new Timer(DELAY, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int diff = (int) (0.7 * (finalPosition - currentPosition));
                    currentPosition = finalPosition - diff;
                    if (Math.abs(diff) < 1)
                        currentPosition = finalPosition;
                    elementContainer.setBounds(currentPosition, 0, elementContainer.getPreferredSize().width, elementContainer.getPreferredSize().height);

                }
            });
        timer.start();
        currentPosition = finalPosition;
    }

    private int getIndexOf(Movement movement) {
        int i = 0;
        for (Movement m : allMovements) {
            if (m != null) {
                if (m.getId() == movement.getId())
                    return i;
            }
            i++;
        }
        return i;
    }

    /**
     * @return The next button.
     */
    public JButton getNextButton() {
        return nextBtn;
    }

    /**
     * @return The previous button.
     */
    public JButton getPreviousButton() {
        return prevBtn;
    }

    /**
     * @return The end button.
     */
    public JButton getEndButton() {
        return endBtn;
    }

    /**
     * @return The start button.
     */
    public JButton getStartButton() {
        return startBtn;
    }
}
