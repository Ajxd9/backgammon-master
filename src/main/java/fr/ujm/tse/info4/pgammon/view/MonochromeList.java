package fr.ujm.tse.info4.pgammon.view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

/**
 * Base component for graphically enhanced lists.
 * Author: Jean-Mi
 * @param <T> Class corresponding to the represented elements.
 */
public class MonochromeList<T> extends MonochromePanel {
    private static final long serialVersionUID = 1L;

    private Collection<T> ref;
    private Vector<T> elements;
    private JList<T> list;
    private JScrollPane scrollPane;

    /**
     * Constructs the component.
     * @param title : Title of the window.
     * @param list : List of elements to represent.
     * @param cellRenderer : Renderer responsible for displaying the components.
     */
    public MonochromeList(String title, Collection<T> list, ListCellRenderer<T> cellRenderer) {
        super(title);
        setLayout(null);
        setOpaque(false);
        ref = list;
        elements = new Vector<T>(ref);
        this.list = new JList<T>(elements);
        this.list.setCellRenderer(cellRenderer);
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(this.list);
        scrollPane.setBounds(0, 0, getPreferredSize().width, getPreferredSize().height);

        this.list.setOpaque(false);
        scrollPane.setOpaque(false);
        add(scrollPane);

        setBackground(Color.BLACK);
        this.list.setBackground(Color.BLACK);
        scrollPane.setBackground(Color.BLACK);

        scrollPane.getViewport().setBackground(null);
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setBorder(null);
        scrollPane.setBorder(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        scrollPane.setBounds(5, 5 + MonochromePanel.TITLE_HEIGHT, getBounds().width - 10, getBounds().height - 10 - MonochromePanel.TITLE_HEIGHT);

        super.paintComponent(g);
    }

    /**
     * Retrieves the selected value from the list and returns it.
     * @return The selected value from the list.
     */
    public T getSelectedValue() {
        return list.getSelectedValue();
    }

    /**
     * Returns the JList component associated with the list for listening purposes.
     * @return The associated JList.
     */
    public JList<T> getList() {
        return list;
    }

    /**
     * Updates the data.
     * @param listData : new collection.
     */
    public void setListData(Collection<T> listData) {
        ref = listData;
        elements = new Vector<T>(ref);
        list.setListData(elements);
        updateUI();
    }

    /**
     * Updates the list after a change from the corresponding CellRenderer.
     * @param newCellRenderer corresponding CellRenderer.
     */
    public void updateList(ListCellRenderer<T> newCellRenderer) {
        elements = new Vector<T>(ref);
        list.setListData(elements);
        list.setCellRenderer(newCellRenderer);
        list.repaint();
    }
}
