package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClimbingRoutesGUI extends JFrame {
    private final DefaultListModel<Author> authorListModel;
    private final DefaultListModel<Route> routeListModel;
    private final JList<Author> authorList;
    private final JList<Route> routeList;
    private final SessionFactory sessionFactory;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final JLabel routesHeaderLabel;

    public ClimbingRoutesGUI(SessionFactory factory) {
        this.sessionFactory = factory;
        setTitle("Climbing Routes Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Create list models
        authorListModel = new DefaultListModel<>();
        routeListModel = new DefaultListModel<>();

        // Create lists
        authorList = new JList<>(authorListModel);
        routeList = new JList<>(routeListModel);

        // Custom cell renderers
        authorList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Author) {
                    setText(((Author) value).getName());
                }
                return this;
            }
        });

        routeList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Route) {
                    Route route = (Route) value;
                    setText(String.format("%s (Grade: %d) - Added: %s",
                            route.getRouteName(),
                            route.getRouteGrade(),
                            route.getDateOfSetting().format(dateFormatter)));
                }
                return this;
            }
        });

        // create headers
        JLabel authorHeaderLabel = new JLabel("Routesetters");
        authorHeaderLabel.setFont(new Font(authorHeaderLabel.getFont().getName(), Font.BOLD, 14));
        authorHeaderLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        routesHeaderLabel = new JLabel("Routes");
        routesHeaderLabel.setFont(new Font(routesHeaderLabel.getFont().getName(), Font.BOLD, 14));
        routesHeaderLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // create panels for authors
        JPanel authorsPanel = new JPanel(new BorderLayout());
        authorsPanel.add(authorHeaderLabel, BorderLayout.NORTH);
        authorsPanel.add(new JScrollPane(authorList), BorderLayout.CENTER);

        // create panel for routes
        JPanel routesPanel = new JPanel(new BorderLayout());
        routesPanel.add(routesHeaderLabel, BorderLayout.NORTH);
        routesPanel.add(new JScrollPane(routeList), BorderLayout.CENTER);

        // create split pane for two lists
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                authorsPanel,
                routesPanel);
        splitPane.setDividerLocation(300);

        //create buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addAuthorButton = new JButton("Add Author");
        JButton addRouteButton = new JButton("Add Route");
        JButton deleteAuthorButton = new JButton("Delete Author");
        JButton deleteRouteButton = new JButton("Delete Route");

        //add buttons
        buttonPanel.add(addAuthorButton);
        buttonPanel.add(addRouteButton);
        buttonPanel.add(deleteAuthorButton);
        buttonPanel.add(deleteRouteButton);

        // set the layout
         setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // create event listeners for interactions
        authorList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Author selectedAuthor = authorList.getSelectedValue();
                if (selectedAuthor != null) {
                    try (Session session = sessionFactory.openSession()) {
                        // Reload the author within the session to get fresh data
                        Author reloadedAuthor = session.get(Author.class, selectedAuthor.getId());
                        routesHeaderLabel.setText("Routes by " + reloadedAuthor.getName());
                        updateRouteList(reloadedAuthor);
                    }
                } else {
                    routesHeaderLabel.setText("Routes");
                    routeListModel.clear();
                }
            }
        });

        routeList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Route selectedRoute = routeList.getSelectedValue();
                    if (selectedRoute != null) {
                        showRouteDialog(selectedRoute);
                    }
                }
            }
        });

        addAuthorButton.addActionListener(e -> addNewAuthor());
        addRouteButton.addActionListener(e -> addNewRoute());
        deleteAuthorButton.addActionListener(e -> deleteSelectedAuthor());
        deleteRouteButton.addActionListener(e -> deleteSelectedRoute());

        // load initial data
        loadAuthors();
    }

    //get the list of authors from database
    private void loadAuthors() {
        try (Session session = sessionFactory.openSession()) {
            Query<Author> query = session.createQuery("from Author", Author.class);
            List<Author> authors = query.list();
            authorListModel.clear();
            authors.forEach(authorListModel::addElement);
        }
    }

    // used when the user clicks one of the authors, changes the route list
    private void updateRouteList(Author author) {
        routeListModel.clear();
        if (author != null) {
            try (Session session = sessionFactory.openSession()) {

                Author reloadedAuthor = session.get(Author.class, author.getId());

                reloadedAuthor.getRoutes().forEach(routeListModel::addElement);
            }
        }
    }

    //additional dialog for editing a chosen route
    private void showRouteDialog(Route route) {
        JDialog dialog = new JDialog(this, "Edit Route", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // route name field
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Route Name:"), gbc);
        JTextField nameField = new JTextField(route.getRouteName(), 20);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        // date, not editable
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Date Added:"), gbc);
        JTextField dateField = new JTextField(route.getDateOfSetting().format(dateFormatter), 20);
        dateField.setEditable(false);
        dateField.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        dialog.add(dateField, gbc);

        // spinner to change the grade, 1-10 values
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Grade:"), gbc);
        SpinnerNumberModel gradeModel = new SpinnerNumberModel(
                route.getRouteGrade(), 1, 10, 1);
        JSpinner gradeSpinner = new JSpinner(gradeModel);
        gbc.gridx = 1;
        dialog.add(gradeSpinner, gbc);

        // show the restrictions list for the route
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Restrictions:"), gbc);
        DefaultListModel<Route.Restriction> restrictionListModel = new DefaultListModel<>();
        route.getRestrictions().forEach(restrictionListModel::addElement);
        JList<Route.Restriction> restrictionList = new JList<>(restrictionListModel);
        restrictionList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Route.Restriction) {
                    Route.Restriction restriction = (Route.Restriction) value;
                    setText(String.format("%s - Added: %s",
                            restriction.getDescription(),
                            restriction.getDateAdded().format(dateFormatter)));
                }
                return this;
            }
        });
        JScrollPane restrictionScrollPane = new JScrollPane(restrictionList);
        gbc.gridx = 1;
        dialog.add(restrictionScrollPane, gbc);

        // add restriction button
        JButton addRestrictionButton = new JButton("Add Restriction");
        gbc.gridx = 1; gbc.gridy = 4;
        dialog.add(addRestrictionButton, gbc);

        // save button
        JButton saveButton = new JButton("Save");
        gbc.gridx = 1; gbc.gridy = 5;
        dialog.add(saveButton, gbc);

        // event handlers
        addRestrictionButton.addActionListener(e -> {
            String description = JOptionPane.showInputDialog(dialog, "Enter restriction description:");
            if (description != null && !description.trim().isEmpty()) {
                Route.Restriction restriction = route.createRestriction(description);
                restrictionListModel.addElement(restriction);
            }
        });

        saveButton.addActionListener(e -> {
            try {
                route.setRouteName(nameField.getText());
                route.setRouteGrade((Integer) gradeSpinner.getValue());

                try (Session session = sessionFactory.openSession()) {
                    Transaction transaction = session.beginTransaction();
                    session.update(route);
                    transaction.commit();
                }

                dialog.dispose();
                updateRouteList(route.getAuthor());
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Illegal argument", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    //adding new author
    private void addNewAuthor() {
        String name = JOptionPane.showInputDialog(this, "Enter author name:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                Author author = new Author(name);
                try (Session session = sessionFactory.openSession()) {
                    Transaction transaction = session.beginTransaction();
                    session.save(author);
                    transaction.commit();
                }
                loadAuthors();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Illegal argument", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //adding new route, must pick an author first
    private void addNewRoute() {
        Author selectedAuthor = authorList.getSelectedValue();
        if (selectedAuthor == null) {
            JOptionPane.showMessageDialog(this, "Please select an author first");
            return;
        }

        // changing grade or adding restrictions only after clicking the new route from the list
        String name = JOptionPane.showInputDialog(this, "Enter route name:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                Route route = new Route(name, 1, LocalDate.now());
                route.setAuthor(selectedAuthor);

                try (Session session = sessionFactory.openSession()) {
                    Transaction transaction = session.beginTransaction();
                    session.save(route);
                    transaction.commit();
                }

                updateRouteList(selectedAuthor);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Illegal argument", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //deleting the author and his routes
    private void deleteSelectedAuthor() {
        Author selectedAuthor = authorList.getSelectedValue();
        if (selectedAuthor != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this author and all associated routes?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Session session = sessionFactory.openSession()) {
                    Transaction transaction = session.beginTransaction();
                    session.delete(selectedAuthor);
                    transaction.commit();
                }
                loadAuthors();
                routeListModel.clear();
            }
        }
    }

    //deleting selected route
    private void deleteSelectedRoute() {
        Route selectedRoute = routeList.getSelectedValue();
        if (selectedRoute != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this route?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Author author = selectedRoute.getAuthor();
                try (Session session = sessionFactory.openSession()) {
                    Transaction transaction = session.beginTransaction();
                    session.delete(selectedRoute);
                    transaction.commit();
                }
                updateRouteList(author);
            }
        }
    }

}