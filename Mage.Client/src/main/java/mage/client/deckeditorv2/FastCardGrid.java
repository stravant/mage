/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mage.client.deckeditorv2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import mage.cards.MageCard;
import mage.cards.repository.CardInfo;
import mage.client.cards.BigCard;
import mage.client.plugins.impl.Plugins;
import mage.view.CardView;
import org.mage.card.arcane.CardPanel;

/**
 *
 * @author stravant@gmail.com
 */
public class FastCardGrid extends JScrollPane {

    // Content for the scrolling pane
    // Forced to fill the card grid horizontally, but freely resized vertically
    // Also sets the scroll step to the card size
    private class FastCardGridContent extends JPanel implements Scrollable {
        public FastCardGridContent() {}

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return currentCardHeight + CARD_PADDING;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return currentCardHeight + CARD_PADDING;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
    
    public interface FastCardGridListener {
        public void doubleClicked(CardInfo card, boolean sideboard);
    }
    
    // Padding between cards in the grid
    private static final int CARD_PADDING = 10;

    // Desired card width to match
    private static final int CARD_WIDTH = 180;
    
    // How many hidden rows to pre-render?
    private static final int EXTRA_ROWS = 2;
    
    // The current sizing of the grid
    private int currentCardWidth;
    private int currentCardHeight;
    private int currentColCount;
    
    // The scrollPane contents
    private JPanel content;
    
    // The big card
    private BigCard bigCard;
    
    // The currently displayed cards and where they are at
    private List<CardInfo> cardList = new ArrayList<>();
    private int currentRowsIndex = 0;
    private List<List<MageCard>> currentRows = new ArrayList<>();
    
    // Event listeners
    private List<FastCardGridListener> listeners = new ArrayList<>();
    
    // Construct a new card grid
    public FastCardGrid(BigCard bigCard) {
        // Super init
        super(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Store bigcard to be used in card construction
        this.bigCard = bigCard;
        
        // Init
        initComponents();
    }
    
    /**
     * Add a listener listening for cards being clicked
     * @param listener 
     */
    public void addFastCardGridListener(FastCardGridListener listener) {
        listeners.add(listener);
    }
    
    // Init the component
    private void initComponents() {  
        // Content
        content = new FastCardGridContent();
        content.setOpaque(false);
        content.setLayout(null);
        setViewportView(content);

        // Adjust what's visible when scrolling
        getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                updateVisibleCards(null);
            }
        });
        
        // Relayout grid on resized
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                layoutGrid();
            }

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {}

            @Override
            public void componentHidden(ComponentEvent e) {}
        });       
    }
    
    /**
     * Set the contents of the grid to a given list of cards
     */
    public void setCardList(List<CardInfo> cards) {
        Map<CardInfo, MageCard> temp = temporarySaveCards();
        cardList = cards;
        updateScrollContentSize();
        clearVisibleCards();
        updateVisibleCards(temp);
    }
    
    // Update the size of the scroll content
    private void updateScrollContentSize() {
        int totalRowCount;
        if (currentColCount > 0) {
            totalRowCount = (int)Math.ceil(((float)cardList.size()) / currentColCount);
        } else {
            totalRowCount = 0;
        }
        content.setPreferredSize(new Dimension(0, CARD_PADDING + totalRowCount*(CARD_PADDING + currentCardHeight)));
        this.revalidate();
    }
    
    // Lay out the card grid, 
    private void layoutGrid() {
        // Card sizing (static for now)
        currentCardWidth = CARD_WIDTH;
        currentCardHeight = (int)(CardPanel.ASPECT_RATIO * currentCardWidth);
        
        // How many cols?
        int areaWidth = content.getWidth();
        int newColCount = (areaWidth - CARD_PADDING) / (currentCardWidth + CARD_PADDING);
        
        // If we changed col count, clear the current display
        if (newColCount == currentColCount) {
            // Nothing to do, state is still good
        } else {
            // Changed col count
            Map<CardInfo, MageCard> temp = temporarySaveCards(); // Do this first before changing the col count
            currentColCount = newColCount;
            
            // Set how big the scroll content is
            updateScrollContentSize();
            
            // Update cards shown
            clearVisibleCards();
            updateVisibleCards(temp);
        }       
    }
    
    private Map<CardInfo, MageCard> temporarySaveCards() {
        Map<CardInfo, MageCard> temporary = new HashMap<>();
        for (int i = 0; i < currentRows.size(); ++i) {
            List<MageCard> row = currentRows.get(i);
            for (int j = 0; j < row.size(); ++j) {
                CardInfo info = cardList.get(currentColCount * (currentRowsIndex + i) + j);
                temporary.put(info, row.get(j));
            }
        }
        return temporary;
    }
    
    // Clear the visible cards
    private void clearVisibleCards() {
        // Remove the exiting displays
        for (List<MageCard> row: currentRows) {
            for (MageCard card: row) {
                content.remove(card);
            }
        }
        
        // Reset the state
        currentRowsIndex = 0;
        currentRows.clear();
    }
    
    // Set up a given card
    private MageCard createCardView(final CardInfo source) {
        CardView view = new CardView(source.getMockCard());
        MageCard display = Plugins.getInstance().getMageCard(view, bigCard, new Dimension(currentCardWidth, currentCardHeight), null, true);
        for (MouseWheelListener l: display.getMouseWheelListeners()) {
            display.removeMouseWheelListener(l);
        }
        display.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    for (FastCardGridListener l: listeners) {
                        l.doubleClicked(source, /* send to sideboard = */e.isAltDown());
                    }
                }
            }
        });
        
        return display;
    }
    
    // Update which cards are currently show
    private void updateVisibleCards(Map<CardInfo, MageCard> oldViews) {
        // What can be seen right now?
        Rectangle visRect = this.getViewport().getViewRect();
        
        // What are the first and last visible rows?
        int firstVisRowIndex = (int)Math.floor(((float)visRect.getY()) / (CARD_PADDING + currentCardHeight)) - EXTRA_ROWS;
        int lastVisRowIndex = (int)Math.floor(((float)(visRect.getMaxY() - CARD_PADDING)) / (CARD_PADDING + currentCardHeight)) + EXTRA_ROWS;
        int rowCount = lastVisRowIndex - firstVisRowIndex + 1;
        
        // Generate new panels or re-use existing ones
        List<List<MageCard>> newRowPanels = new ArrayList<>();
        for (int i = 0; i < rowCount; ++i) {
            int rowIndex = firstVisRowIndex + i;
            if (rowIndex >= currentRowsIndex && rowIndex < currentRowsIndex + currentRows.size()) {
                // Row exists in current table
                newRowPanels.add(currentRows.get(rowIndex - currentRowsIndex));
            } else {
                // Need to make a fresh row
                List<MageCard> row = new ArrayList<>();
                
                // Card index
                int firstCardIndex = rowIndex * currentColCount;
                for (int j = 0; j < currentColCount; ++j) {
                    int cardIndex = firstCardIndex + j;
                    if (cardIndex >= cardList.size() || cardIndex < 0) {
                        // No more cards for this row
                        break;
                    } else {
                        CardInfo cardInfo = cardList.get(cardIndex);
                        MageCard display; 
                        // Create the view, add it to the row, and add it to the scroll content. Or reuse an old view if we have it
                        if (oldViews == null || (display = oldViews.get(cardInfo)) == null) {
                            display = createCardView(cardInfo);
                        }
                        content.add(display);
                        row.add(display);
                    }
                }
                
                // Add the row
                newRowPanels.add(row);
            }
        }
        
        // Delete old now unused panels in the old list
        for (int i = 0; i < currentRows.size(); ++i) {
            int row = currentRowsIndex + i;
            if (row < firstVisRowIndex || row > lastVisRowIndex) {
                // Delete the contents
                for (MageCard display: currentRows.get(i)) {
                    content.remove(display);
                }
            }
        }
        
        // Update the current panels
        currentRowsIndex = firstVisRowIndex;
        currentRows = newRowPanels;
        
        // Lay out the now current panels
        for (int i = 0; i < currentRows.size(); ++i) {
            int rowIndex = currentRowsIndex + i;
            List<MageCard> rowData = currentRows.get(i);
            for (int colIndex = 0; colIndex < rowData.size(); ++colIndex) {
                MageCard card = rowData.get(colIndex);
                card.setBounds(
                        CARD_PADDING + colIndex*(CARD_PADDING + currentCardWidth), 
                        CARD_PADDING + rowIndex*(CARD_PADDING + currentCardHeight),
                        currentCardWidth,
                        currentCardHeight);
            }
        }
        
        // Schedule a repaint
        repaint();
    }
}
