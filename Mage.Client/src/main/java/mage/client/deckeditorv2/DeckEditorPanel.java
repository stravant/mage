/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mage.client.deckeditorv2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import mage.cards.Card;
import mage.cards.MageCard;
import mage.cards.Sets;
import mage.cards.decks.Deck;
import mage.cards.decks.importer.DeckImporterUtil;
import mage.cards.mock.MockCard;
import mage.cards.repository.CardCriteria;
import mage.cards.repository.CardInfo;
import mage.cards.repository.CardRepository;
import mage.client.MageFrame;
import mage.client.cards.BigCard;
import mage.client.constants.Constants;
import mage.client.constants.Constants.DeckEditorMode;
import mage.client.deckeditor.DeckArea;
import mage.client.plugins.impl.Plugins;
import mage.client.util.Event;
import mage.client.util.Listener;
import mage.client.util.NaturalOrderCardNumberComparator;
import mage.client.util.NaturalOrderComparator;
import mage.filter.FilterCard;
import mage.game.GameException;
import mage.interfaces.plugin.CardPlugin;
import mage.utils.ThreadUtils;
import mage.view.CardView;
import mage.view.SimpleCardView;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mage.card.arcane.CardPanel;
import org.mage.card.arcane.UI;
import org.mage.card.arcane.Util;
import org.mage.plugins.card.CardPluginImpl;

/**
 * @author stravant@gmail.com
 */
public class DeckEditorPanel extends JPanel {
    private Logger LOGGER = Logger.getLogger(DeckEditorPanel.class);
    
    // Mode the deck editor is in (edit from collection or edit from limited pool)
    private DeckEditorMode mode;
    
    // Components
    //<editor-fold>
    // Main components
    private JComponent filterPanel;
    private BigCard previewPanel;
    private JComponent topPanel;
    private DeckArea bottomPanel;
    private JSplitPane horizontalSplit;
    
    // Deck name / save / load
    private JComponent deckNameGridBag;
    private JLabel deckNameLabel;
    private JTextField deckNameInput;
    private JButton saveDeckButton;
    private JButton loadDeckButton;
    
    // Timer and submit
    private JComponent deckbuildTimeContainer;
    private JLabel deckbuildTimeLeft;
    private JButton deckbuildSubmit;
    
    // Search bar above card grid
    private JTextField searchInput;
    private JButton searchSubmit;
    private JButton searchClear;
    private final JComponent searchBar;
    
    // Save dialgo
    private JFileChooser loadFileChooser;
    
    // Grid to show the cards
    private final JComponent topCenterPanel;
    private final FastCardGrid cardGrid;
    //</editor-fold>
    
    // What is the currently searched for text?
    private String lastSearchText = "";
    private List<CardInfo> lastSearchCards;
    
    // The deck we're operating on
    private Deck deck = new Deck();
    
    // Current search info
    private boolean searchInProgress = false;
    private int currentSearchId = 0;
    private String currentSearchString = "";
    
    // All of cards
    private static class SearchEntry {
        public SearchEntry(CardInfo info, String text) {
            this.text = text;
            this.info = info;
        }
        final String text;
        final CardInfo info;
    }
    private final List<CardInfo> allCards;
    private final List<SearchEntry> searchDatabase;
    
    private GridBagConstraints makeGBC(int x, int y, int w, int h, double weightx, double weighty) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = w;
        c.gridheight = h;
        c.gridx = x;
        c.gridy = y;
        c.weightx = weightx;
        c.weighty = weighty;
        c.fill = GridBagConstraints.HORIZONTAL;
        return c;
    }
    
    // Constructor
    public DeckEditorPanel() {
        // Layout
        setLayout(new BorderLayout());
        
        // Card preview panel
        previewPanel = new BigCard();
        previewPanel.setOpaque(false);
        
        // Filter panel
        filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBorder(BorderFactory.createEtchedBorder());
        
        // Extra container to put all of the filter panel stuff at the top
        JPanel filterContainer = new JPanel();
        filterContainer.setPreferredSize(new Dimension(200, 0));
        filterContainer.setLayout(new BorderLayout());
        filterContainer.add(filterPanel, BorderLayout.PAGE_START);
        
        // Deck name area
        deckNameLabel = new JLabel("Deck Name:");
        loadDeckButton = new JButton("Load");
        saveDeckButton = new JButton("Save");
        deckNameInput = new JTextField();
        deckNameGridBag = new JPanel();
        deckNameGridBag.setLayout(new GridBagLayout());
        deckNameGridBag.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Load / Save Deck"));
        deckNameGridBag.add(deckNameLabel, makeGBC(0, 0, 1, 1, 1.0, 0));
        deckNameGridBag.add(saveDeckButton, makeGBC(1, 0, 1, 1, 0, 0));
        deckNameGridBag.add(loadDeckButton, makeGBC(2, 0, 1, 1, 0, 0));
        deckNameGridBag.add(deckNameInput, makeGBC(0, 1, 3, 1, 1.0, 0));
        filterPanel.add(deckNameGridBag);
        loadFileChooser = new JFileChooser();
        
        // Limited deckbuilding timer
        deckbuildTimeLeft = new JLabel("Time left: ");
        deckbuildSubmit = new JButton("Submit Deck");
        deckbuildTimeContainer = new JPanel();
        deckbuildTimeContainer.setLayout(new GridBagLayout());
        deckbuildTimeContainer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Limited Deckbuilding"));
        deckbuildTimeContainer.add(deckbuildTimeLeft, makeGBC(0, 0, 1, 1, 1.0, 0));
        deckbuildTimeContainer.add(deckbuildSubmit, makeGBC(0, 1, 1, 1, 1.0, 0));
        filterPanel.add(deckbuildTimeContainer);
        
        loadDeckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDeck();
            }
        });
        
        saveDeckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDeck();
            }
        });
        
        // Search bar
        searchInput = new JTextField();
        searchSubmit = new JButton("Search");
        searchClear = new JButton("Clear");
        searchBar = new JPanel();
        searchBar.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        searchBar.add(searchClear);
        searchBar.add(searchSubmit);
        searchBar.add(searchInput, c);
        
        searchClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doClearSearch();
            }
        });
        searchSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initiateSearch();
            }
        });
        searchInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<CardInfo> results = searchImmediate();
                searchInput.requestFocus();
                searchInput.selectAll();
                if (results != null) {
                    tryToAddCardFromResults(results);
                }
            }
        });
        searchInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                initiateSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                initiateSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                initiateSearch();
            }
        });
        
        // Card Grid
        cardGrid = new FastCardGrid(previewPanel);
        cardGrid.setBorder(null);
        cardGrid.addFastCardGridListener(new FastCardGrid.FastCardGridListener() {
            @Override
            public void doubleClicked(CardInfo card, boolean sideboard) {
                if (sideboard) {
                    addToSideboard(card);
                } else {
                    addToMaindeck(card);
                }
            }
        });
        
        topCenterPanel = new JPanel();
        topCenterPanel.setLayout(new BorderLayout());
        topCenterPanel.add(searchBar, BorderLayout.NORTH);
        topCenterPanel.add(cardGrid, BorderLayout.CENTER);
        topCenterPanel.setBorder(BorderFactory.createEtchedBorder());
        
        // Top panel, with preview, filter panel, and card grid
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(filterContainer, BorderLayout.WEST);
        topPanel.add(topCenterPanel, BorderLayout.CENTER);
        topPanel.add(previewPanel, BorderLayout.EAST);
        
        // Bottom panel, nothing for now
        bottomPanel = new DeckArea();
        bottomPanel.addDeckEventListener(new Listener<Event>() {
            @Override
            public void event(Event event) {
                switch (event.getEventName()) {
                    case "double-click":
                        removeFromDeck((SimpleCardView)event.getSource());
                        break;
                    case "alt-double-click":
                        moveToSideboard((SimpleCardView)event.getSource());
                        break;
                }
                refreshDeck();
            }
        });
        bottomPanel.addSideboardEventListener(new Listener<Event>() {
            @Override
            public void event(Event event) {
                switch (event.getEventName()) {
                    case "double-click":
                        removeFromSideboard((SimpleCardView)event.getSource());
                        break;
                    case "alt-double-click":
                        moveToMaindeck((SimpleCardView)event.getSource());
                        break;
                }
                refreshDeck();
            }
        });
        
        // Main horizontal split
        horizontalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        horizontalSplit.setOneTouchExpandable(false);
        horizontalSplit.setResizeWeight(0.5);
        
        // Add the main split
        add(horizontalSplit, BorderLayout.CENTER);
        
        // Load in all of the cards
        allCards = loadAllCards();
        searchDatabase = buildSearch(allCards);
        
        // Show them
        cardGrid.setCardList(allCards);
    }
    
    private void addToSideboard(CardInfo view) {
        deck.getSideboard().add(view.getMockCard());
        refreshDeck();
    }
    
    private void addToMaindeck(CardInfo view) {
        deck.getCards().add(view.getMockCard());
        refreshDeck();
    }
    
    private void moveToSideboard(SimpleCardView view) {
        for (Card card: deck.getCards()) {
            if (card.getId().equals(view.getId())) {
                deck.getCards().remove(card);
                deck.getSideboard().add(card);
                break;
            }
        }
        refreshDeck();        
    }
    
    private void moveToMaindeck(SimpleCardView view) {
        for (Card card: deck.getSideboard()) {
            if (card.getId().equals(view.getId())) {
                deck.getSideboard().remove(card);
                deck.getCards().add(card);
                break;
            }
        }
        refreshDeck();        
    }
    
    private void removeFromDeck(SimpleCardView toRemove) {
        for (Card card: deck.getCards()) {
            if (card.getId().equals(toRemove.getId())) {
                deck.getCards().remove(card);
                break;
            }
        }
        refreshDeck();
    }
    
    private void removeFromSideboard(SimpleCardView toRemove) {
        for (Card card: deck.getSideboard()) {
            if (card.getId().equals(toRemove.getId())) {
                deck.getSideboard().remove(card);
                break;
            }
        }
        refreshDeck();
    }
    
    private void refreshDeck() {
        bottomPanel.loadDeck(deck, previewPanel);
    }
    
    private void newDeck() {
        if (mode == DeckEditorMode.SIDEBOARDING || mode == DeckEditorMode.LIMITED_BUILDING) {
            // TODO:
        } else {
            deck = new Deck();
        }
        refreshDeck();
    }
    
    private void loadDeck() {
        String lastFolder = MageFrame.getPreferences().get("lastDeckFolder", "");
        if (!lastFolder.isEmpty()) {
            loadFileChooser.setCurrentDirectory(new File(lastFolder));
        }
        int ret = loadFileChooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = loadFileChooser.getSelectedFile();
            try {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                deck = Deck.load(DeckImporterUtil.importDeck(file.getPath()), true, true);
            } catch (GameException ex) {
                JOptionPane.showMessageDialog(MageFrame.getDesktop(), ex.getMessage(), "Error loading deck", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                LOGGER.fatal(ex);
            } finally {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            refreshDeck();
            try {
                if (file != null) {
                    MageFrame.getPreferences().put("lastDeckFolder", file.getCanonicalPath());
                }
            } catch (IOException ex) {
            }
        }
        loadFileChooser.setSelectedFile(null);
    }
    
    private void saveDeck() {
        String lastFolder = MageFrame.getPreferences().get("lastDeckFolder", "");
        if (!lastFolder.isEmpty()) {
            loadFileChooser.setCurrentDirectory(new File(lastFolder));
        }
        deck.setName(deckNameInput.getText());
        int ret = loadFileChooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = loadFileChooser.getSelectedFile();
            try {
                String fileName = file.getPath();
                if (!fileName.endsWith(".dck")) {
                    fileName += ".dck";
                }
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                Sets.saveDeck(fileName, deck.getDeckCardLists());
            } catch (Exception ex) {
                LOGGER.fatal(ex);
            } finally {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            try {
                MageFrame.getPreferences().put("lastDeckFolder", file.getCanonicalPath());
            } catch (IOException ex) {
            }
        }        
    }

    
    /**
     * Try to add a card based on a result set. Use the first card in the
     * result set.
     */
    private void tryToAddCardFromResults(List<CardInfo> cards) {
        if (!cards.isEmpty()) {
            // TODO: Maybe some other behavior?
            addToMaindeck(cards.get(0));
        }
    }
    
    /**
     * Search now on the UI thread, for when the user presses enter and wants
     * the results ASAP.
     */
    private List<CardInfo> searchImmediate() {
        // Cancel any outstanding search
        cancelSearch();
        // And start a new search process
        return searchImmediateProc();
    }
    
    /**
     * Initiate a delayed search on a separate thread
     */
    private void initiateSearch() {
        String text = searchInput.getText().trim();
        
        // Ignore if it's the same search
        if (text.equalsIgnoreCase(lastSearchText)) {
            return;
        } else {
            lastSearchText = text;
        }
        
        // Do the search
        if (text.equals("")) {
            doClearSearch();
        } else {
            initiateSearch(text);
        }
    }
    
    private void cancelSearch() {
        synchronized(this) {
            ++currentSearchId;
            currentSearchString = "";
        }
    }
    
    private void initiateSearch(String s) {
        synchronized(this) {
            ++currentSearchId;
            currentSearchString = s;
            if (!searchInProgress) {
                Util.threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        doSearchProc();
                    }
                });
            }
        }
    }
    
    private List<CardInfo> searchImmediateProc() {
        String text = searchInput.getText().trim();
        if (text.equals(lastSearchText)) {
            return lastSearchCards;
        } else {
            lastSearchText = text;
        }
        if (text.equals("")) {
            doClearSearch();
            return allCards;
        } else {
            Set<CardInfo> included = new HashSet<>();
            List<CardInfo> filteredCards = new ArrayList<>();
            // #1: Exact name equality
            for (SearchEntry searchEntry: searchDatabase) {
                if (StringUtils.equalsIgnoreCase(searchEntry.info.getName(), text)) {
                    filteredCards.add(searchEntry.info);
                    included.add(searchEntry.info);
                }
            }
            // #2: Name contains
            for (SearchEntry searchEntry: searchDatabase) {
                if (StringUtils.containsIgnoreCase(searchEntry.info.getName(), text)) {
                    if (!included.contains(searchEntry.info)) {
                        filteredCards.add(searchEntry.info);
                        included.add(searchEntry.info);
                    }
                }
            }
            // #3: Any card text contains
            for (SearchEntry searchEntry: searchDatabase) {
                if (StringUtils.containsIgnoreCase(searchEntry.text, text)) {
                    if (!included.contains(searchEntry.info)) {
                        filteredCards.add(searchEntry.info);
                        included.add(searchEntry.info);
                    }
                }
            }
            cardGrid.setCardList(filteredCards);
            lastSearchCards = filteredCards;
            return filteredCards;
        }       
    }
    
    private void doSearchProc() {
        // Pick up the things we need to search
        List<CardInfo> result = null;
        int thisSearchId;
        String thisSearchString;
        synchronized(this) {
            thisSearchId = currentSearchId;
            thisSearchString = currentSearchString;
            if (thisSearchString.isEmpty()) {
                searchInProgress = false;
                return;
            }
        }
        
        // Textual search
        for (;;) {
            boolean complete = true;
            List<CardInfo> filteredCards = new ArrayList<>();
            Set<CardInfo> included = new HashSet<>();
            int left = 1000;
            // #1 exact name match
            for (SearchEntry searchEntry: searchDatabase) {
                if (--left < 0) {
                    // Check if there's a new search that we should switch to instead
                    synchronized(this) {
                        if (currentSearchId != thisSearchId) {
                            thisSearchId = currentSearchId;
                            thisSearchString = currentSearchString;
                            if (thisSearchString.isEmpty()) {
                                searchInProgress = false;
                                return;
                            }
                            complete = false;
                            break;
                        }
                    }
                    left = 1000;
                }
                if (StringUtils.equalsIgnoreCase(searchEntry.info.getName(), thisSearchString)) {
                    if (!included.contains(searchEntry.info)) {
                        filteredCards.add(searchEntry.info);
                        included.add(searchEntry.info);
                    }
                }
            }
            // #2 Name text contains
            if (complete) {
                for (SearchEntry searchEntry: searchDatabase) {
                    if (--left < 0) {
                        // Check if there's a new search that we should switch to instead
                        synchronized(this) {
                            if (currentSearchId != thisSearchId) {
                                thisSearchId = currentSearchId;
                                thisSearchString = currentSearchString;
                                if (thisSearchString.isEmpty()) {
                                    searchInProgress = false;
                                    return;
                                }
                                complete = false;
                                break;
                            }
                        }
                        left = 1000;
                    }
                    if (StringUtils.containsIgnoreCase(searchEntry.info.getName(), thisSearchString)) {
                        if (!included.contains(searchEntry.info)) {
                            filteredCards.add(searchEntry.info);
                            included.add(searchEntry.info);
                        }
                    }
                }
            }
            // #3 Any body text contains
            if (complete) {
                for (SearchEntry searchEntry: searchDatabase) {
                    if (--left < 0) {
                        // Check if there's a new search that we should switch to instead
                        synchronized(this) {
                            if (currentSearchId != thisSearchId) {
                                thisSearchId = currentSearchId;
                                thisSearchString = currentSearchString;
                                if (thisSearchString.isEmpty()) {
                                    searchInProgress = false;
                                    return;
                                }
                                complete = false;
                                break;
                            }
                        }
                        left = 1000;
                    }
                    if (StringUtils.containsIgnoreCase(searchEntry.text, thisSearchString)) {
                        if (!included.contains(searchEntry.info)) {
                            filteredCards.add(searchEntry.info);
                            included.add(searchEntry.info);
                        }
                    }
                }
            }
            if (complete) {
                synchronized (this) {
                    if (currentSearchId == thisSearchId) {
                        // All done
                        result = filteredCards;
                        searchInProgress = false;
                        break;
                    } else if (thisSearchString.isEmpty()) {
                        searchInProgress = false;
                        return;
                    } else {
                        thisSearchId = currentSearchId;
                        thisSearchString = currentSearchString;
                    }
                }
            }
        }
        
        // Handle the result
        onSearchResult(result);
    }
    
    private void onSearchResult(final List<CardInfo> result) {
        UI.invokeLater(new Runnable() {
            @Override
            public void run() {
                lastSearchCards = result;
                cardGrid.setCardList(result);
            }
        });
    }
    
    private void doClearSearch() {
        cardGrid.setCardList(allCards);
    }
    
    private static class GeneralCardComparator extends NaturalOrderComparator {
        @Override
        public int compare(Object o1, Object o2) {
            CardInfo cardInfo1 = (CardInfo) o1;
            CardInfo cardInfo2 = (CardInfo) o2;
            int a = cardInfo1.getSetCode().compareTo(cardInfo2.getSetCode());
            if (a == 0) {
                return super.compare(cardInfo1.getCardNumber(), cardInfo2.getCardNumber());
            } else {
                return a;
            }
        }
    }
    
    /**
     * Load in all cards in the order that the should be shown
     * @return 
     */
    private static List<CardInfo> loadAllCards() {
        final CardCriteria cardCriteria = new CardCriteria();
        cardCriteria.setOrderBy("setCode");
        List<CardInfo> allCards = CardRepository.instance.findCards(cardCriteria);
        Collections.sort(allCards, new GeneralCardComparator());
        return allCards;
    }
    
    private static List<SearchEntry> buildSearch(List<CardInfo> allCards) {
        final List<SearchEntry> search = new ArrayList<>();
        for (CardInfo info: allCards) {
            StringBuilder totalText = new StringBuilder();
            totalText.append(info.getName());
            for (String rule: info.getRules()) {
                totalText.append(rule);
            }
            SearchEntry entry = new SearchEntry(info, totalText.toString());
            search.add(entry);
        }
        return search;
    }
    
    void changeGUISize() {
        bottomPanel.changeGUISize();
    }

    DeckEditorMode getDeckEditorMode() {
        return this.mode;
    }

    void showDeckEditor(DeckEditorMode mode, Deck deck, UUID tableId, int time) {
        // Change modes
        this.mode = mode;
    } 
}
