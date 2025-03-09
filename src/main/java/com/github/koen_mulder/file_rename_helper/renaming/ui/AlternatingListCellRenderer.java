package com.github.koen_mulder.file_rename_helper.renaming.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial") // Same-version serialization only
class AlternatingListCellRenderer extends DefaultListCellRenderer {

    private static final Color LIGHT_GRAY = new Color(0xFC, 0xFC, 0xFC);
    private static final Color HOVER_COLOR = new Color(0xE3, 0xEC, 0xFF);
    private static final Border BORDER_BOTTOM = new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY);
    private static final Border BORDER_NONE = BorderFactory.createEmptyBorder();
    private static final Border PADDING_BORDER = new EmptyBorder(2, 3, 2, 3);

    private int hoverIndex = -1;

    public AlternatingListCellRenderer(JList<?> list) {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoverIndex = -1;
                list.repaint();
            }
        });

        list.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point mousePoint = e.getPoint();
                int newHoverIndex = list.locationToIndex(mousePoint);

                // Check if the mouse is actually *within* the cell bounds
                if (newHoverIndex != -1) {
                    Rectangle cellBounds = list.getCellBounds(newHoverIndex, newHoverIndex);
                    if (!cellBounds.contains(mousePoint)) {
                        newHoverIndex = -1; // Mouse is not within the cell, treat as no hover
                    }
                }

                if (newHoverIndex != hoverIndex) {
                    hoverIndex = newHoverIndex;
                    list.repaint();
                }
            }
        });
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (!isSelected) {
            if (index == hoverIndex) {
                c.setBackground(HOVER_COLOR);
            } else {
                c.setBackground(index % 2 == 0 ? Color.WHITE : LIGHT_GRAY);
            }
        }

        ((JComponent) c).setBorder(BorderFactory
                .createCompoundBorder(index == hoverIndex || isSelected ? BORDER_NONE : BORDER_BOTTOM, PADDING_BORDER));

        return c;
    }
}