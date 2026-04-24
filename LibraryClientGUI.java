import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

public class LibraryClientGUI extends JFrame {
    private LibraryInterface libraryServer;
    private String currentUser = "";
    private String userRole = "";

    public LibraryClientGUI() {
        super("📚 Distributed Library Management System");
        connectToServer();
        showMainScreen();
    }

    private void connectToServer() {
        try {
            Registry reg = LocateRegistry.getRegistry("10.175.98.109", 1099);
            libraryServer = (LibraryInterface) reg.lookup("LibraryService");
            JOptionPane.showMessageDialog(null, "✅ Connected to Server!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Server Connection Error: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    // ==================== MAIN SCREEN ====================
    private void showMainScreen() {
        getContentPane().removeAll();
        setSize(550, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 242, 245));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        JLabel title = new JLabel("Library Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(44, 62, 80));

        JButton loginBtn = createStyledButton("🔐 Login to Account", new Color(52, 152, 219));
        JButton registerBtn = createStyledButton("📝 Create New Account", new Color(46, 204, 113));
        JButton guestBtn = createStyledButton("👤 Continue as Guest", new Color(155, 89, 182));
        JButton exitBtn = createStyledButton("🚪 Exit", new Color(231, 76, 60));

        loginBtn.addActionListener(e -> showLoginScreen());
        registerBtn.addActionListener(e -> showRegisterScreen());
        guestBtn.addActionListener(e -> showGuestOptions());
        exitBtn.addActionListener(e -> System.exit(0));

        mainPanel.add(title);
        mainPanel.add(loginBtn);
        mainPanel.add(registerBtn);
        mainPanel.add(guestBtn);
        mainPanel.add(exitBtn);

        add(mainPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ==================== GUEST OPTIONS ====================
    private void showGuestOptions() {
        String[] options = {"📚 View Books Only", "🔍 Search Books", "↩️ Back to Main"};
        int choice = JOptionPane.showOptionDialog(this, 
            "Select Guest Access Level", "Guest Mode",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);
        
        if (choice == 0) {
            currentUser = "Guest";
            userRole = "Guest";
            viewAllBooks();
            showMainScreen();
        } else if (choice == 1) {
            currentUser = "Guest";
            userRole = "Guest";
            searchBooks();
            showMainScreen();
        } else if (choice == 2) {
            showMainScreen();
        }
    }

    // ==================== REGISTER SCREEN ====================
    private void showRegisterScreen() {
        getContentPane().removeAll();
        setSize(500, 600);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(255, 255, 255));
        setLocationRelativeTo(null);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Create New Account", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(46, 204, 113));

        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPasswordField confirmField = new JPasswordField(15);
        confirmField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTextField nameField = new JTextField(15);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Member", "Librarian"});
        roleCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JButton registerBtn = createStyledButton("📝 Register & Login", new Color(46, 204, 113));
        JButton backBtn = createStyledButton("↩️ Back", new Color(99, 110, 114));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; add(title, gbc);
        gbc.gridy = 1; gbc.gridwidth = 1; add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; add(confirmField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; add(roleCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 6; add(registerBtn, gbc);
        gbc.gridx = 1; add(backBtn, gbc);

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirm = new String(confirmField.getPassword()).trim();
            String name = nameField.getText().trim();
            String role = (String) roleCombo.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Please fill all fields!");
                return;
            }
            
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "❌ Passwords do not match!");
                return;
            }
            
            if (password.length() < 4) {
                JOptionPane.showMessageDialog(this, "❌ Password must be at least 4 characters!");
                return;
            }
            
            try {
                if (libraryServer.registerUser(username, password, name, role)) {
                    JOptionPane.showMessageDialog(this, "✅ Registration Successful!");
                    
                    if (libraryServer.loginUser(username, password)) {
                        currentUser = username;
                        userRole = libraryServer.getUserRole(username);
                        JOptionPane.showMessageDialog(this, "✅ Auto-Login Successful!\nWelcome " + currentUser + "!\nRole: " + userRole);
                        openDashboard();
                    } else {
                        JOptionPane.showMessageDialog(this, "Please login with your credentials.");
                        showLoginScreen();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Username already exists! Please choose another.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> showMainScreen());
        revalidate();
        repaint();
    }

    // ==================== LOGIN SCREEN ====================
    private void showLoginScreen() {
        getContentPane().removeAll();
        setSize(450, 450);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(255, 255, 255));
        setLocationRelativeTo(null);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel title = new JLabel("Login to Your Account", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(52, 152, 219));
        
        JTextField userField = new JTextField(15);
        userField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPasswordField passField = new JPasswordField(15);
        passField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JButton loginBtn = createStyledButton("🔐 Login", new Color(52, 152, 219));
        JButton backBtn = createStyledButton("↩️ Back", new Color(99, 110, 114));
        JButton registerNowBtn = createStyledButton("📝 Don't have account? Register", new Color(46, 204, 113));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; add(title, gbc);
        gbc.gridy = 1; gbc.gridwidth = 1; add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; add(passField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(loginBtn, gbc);
        gbc.gridx = 1; add(backBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; add(registerNowBtn, gbc);

        loginBtn.addActionListener(e -> {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword()).trim();
            
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Please enter username and password!");
                return;
            }
            
            try {
                if (libraryServer.loginUser(u, p)) {
                    currentUser = u;
                    userRole = libraryServer.getUserRole(u);
                    JOptionPane.showMessageDialog(this, "✅ Welcome " + currentUser + "!\nRole: " + userRole);
                    openDashboard();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Invalid username or password!\nPlease check your credentials or register.", 
                                                "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "❌ Server Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> showMainScreen());
        registerNowBtn.addActionListener(e -> showRegisterScreen());
        revalidate(); 
        repaint();
    }

    // ==================== DASHBOARD ====================
    private void openDashboard() {
        getContentPane().removeAll();
        setTitle("🚀 " + userRole.toUpperCase() + " DASHBOARD - " + currentUser);
        setSize(1000, 750);
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(45, 52, 54));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel userLabel = new JLabel("👤 " + currentUser + " | Role: " + userRole);
        userLabel.setForeground(new Color(178, 190, 195));
        userLabel.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 15));
        
        JButton logoutBtn = createStyledButton("🚪 Logout", new Color(214, 48, 49));
        logoutBtn.addActionListener(e -> {
            currentUser = "";
            userRole = "";
            showMainScreen();
        });
        
        topPanel.add(userLabel, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);
        
        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 15, 15));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        menuPanel.setBackground(new Color(241, 242, 246));

        if (userRole.equalsIgnoreCase("Admin")) {
            addAdminMenu(menuPanel);
        } else if (userRole.equalsIgnoreCase("Librarian")) {
            addLibrarianMenu(menuPanel);
        } else if (userRole.equalsIgnoreCase("Member")) {
            addMemberMenu(menuPanel);
        } else {
            addGuestMenu(menuPanel);
        }

        add(topPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addAdminMenu(JPanel menuPanel) {
        JButton usersBtn = createMenuButton("👥 Manage Users", new Color(108, 92, 231));
        JButton reportsBtn = createMenuButton("📊 View Analytics", new Color(0, 184, 148));
        JButton backupBtn = createMenuButton("💾 Backup Database", new Color(253, 150, 68));
        JButton booksBtn = createMenuButton("📚 Master Catalog", new Color(162, 155, 254));
        
        usersBtn.addActionListener(e -> showUserManagementDialog());
        reportsBtn.addActionListener(e -> showReports());
        backupBtn.addActionListener(e -> backupDatabase());
        booksBtn.addActionListener(e -> viewAllBooks());
        
        menuPanel.add(usersBtn);
        menuPanel.add(booksBtn);
        menuPanel.add(reportsBtn);
        menuPanel.add(backupBtn);
    }

    private void addLibrarianMenu(JPanel menuPanel) {
        JButton addBookBtn = createMenuButton("➕ Add New Book", new Color(38, 166, 91));
        JButton issueBookBtn = createMenuButton("📖 Issue Book", new Color(243, 156, 18));
        JButton returnBookBtn = createMenuButton("🔄 Return Book", new Color(52, 152, 219));
        JButton viewRecordsBtn = createMenuButton("📋 Borrowing Logs", new Color(142, 68, 173));
        JButton searchBtn = createMenuButton("🔍 Search Books", new Color(44, 62, 80));
        
        addBookBtn.addActionListener(e -> addNewBook());
        issueBookBtn.addActionListener(e -> issueBook());
        returnBookBtn.addActionListener(e -> returnBook());
        viewRecordsBtn.addActionListener(e -> viewBorrowingRecords());
        searchBtn.addActionListener(e -> searchBooks());
        
        menuPanel.add(addBookBtn);
        menuPanel.add(issueBookBtn);
        menuPanel.add(returnBookBtn);
        menuPanel.add(viewRecordsBtn);
        menuPanel.add(searchBtn);
    }

    private void addMemberMenu(JPanel menuPanel) {
        JButton viewBooksBtn = createMenuButton("📚 View All Books", new Color(46, 204, 113));
        JButton searchBtn = createMenuButton("🔍 Search Books", new Color(52, 152, 219));
        JButton borrowBtn = createMenuButton("📖 Borrow Book", new Color(230, 126, 34));
        JButton returnBtn = createMenuButton("🔄 Return Book", new Color(155, 89, 182));
        JButton historyBtn = createMenuButton("📅 My History", new Color(52, 73, 94));
        
        viewBooksBtn.addActionListener(e -> viewAllBooks());
        searchBtn.addActionListener(e -> searchBooks());
        borrowBtn.addActionListener(e -> borrowBook());
        returnBtn.addActionListener(e -> returnBookMember());
        historyBtn.addActionListener(e -> viewMyHistory());
        
        menuPanel.add(viewBooksBtn);
        menuPanel.add(searchBtn);
        menuPanel.add(borrowBtn);
        menuPanel.add(returnBtn);
        menuPanel.add(historyBtn);
    }

    private void addGuestMenu(JPanel menuPanel) {
        JButton viewBooksBtn = createMenuButton("📚 View All Books", new Color(46, 204, 113));
        JButton searchBtn = createMenuButton("🔍 Search Books", new Color(52, 152, 219));
        JButton loginBtn = createMenuButton("🔐 Login to Borrow", new Color(230, 126, 34));
        
        viewBooksBtn.addActionListener(e -> viewAllBooks());
        searchBtn.addActionListener(e -> searchBooks());
        loginBtn.addActionListener(e -> showLoginScreen());
        
        menuPanel.add(viewBooksBtn);
        menuPanel.add(searchBtn);
        menuPanel.add(loginBtn);
    }

    // ==================== MANAGE USERS DIALOG (ADD & DELETE) ====================
    private void showUserManagementDialog() {
        JDialog dialog = new JDialog(this, "User Management", true);
        dialog.setSize(800, 600);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);
        
        // Tabbed Pane for Add and Delete
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Panel for Add User
        JPanel addPanel = createAddUserPanel(dialog);
        tabbedPane.addTab("➕ Add New User", addPanel);
        
        // Panel for Delete User
        JPanel deletePanel = createDeleteUserPanel(dialog);
        tabbedPane.addTab("🗑️ Delete User", deletePanel);
        
        // Panel for View Users
        JPanel viewPanel = createViewUsersPanel();
        tabbedPane.addTab("📋 View All Users", viewPanel);
        
        dialog.add(tabbedPane, BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeBtn);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private JPanel createAddUserPanel(JDialog dialog) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField nameField = new JTextField(20);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Member", "Librarian"});
        
        gbc.gridx = 0; gbc.gridy = 0; addLabel(panel, "Username:", gbc);
        gbc.gridx = 1; panel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; addLabel(panel, "Password:", gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; addLabel(panel, "Full Name:", gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; addLabel(panel, "Role:", gbc);
        gbc.gridx = 1; panel.add(roleCombo, gbc);
        
        JButton addBtn = new JButton("✅ Add User");
        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.BLACK);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(addBtn, gbc);
        
        addBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String name = nameField.getText().trim();
            String role = (String) roleCombo.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "❌ Please fill all fields!");
                return;
            }
            
            if (password.length() < 4) {
                JOptionPane.showMessageDialog(dialog, "❌ Password must be at least 4 characters!");
                return;
            }
            
            try {
                if (libraryServer.manageUser(username, password, name, role, "CREATE")) {
                    JOptionPane.showMessageDialog(dialog, "✅ User created successfully!\nUsername: " + username + "\nPassword: " + password);
                    usernameField.setText("");
                    passwordField.setText("");
                    nameField.setText("");
                    // Refresh view panel
                    dialog.dispose();
                    showUserManagementDialog();
                } else {
                    JOptionPane.showMessageDialog(dialog, "❌ Failed to create user!\nUsername may already exist.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "❌ Error: " + ex.getMessage());
            }
        });
        
        return panel;
    }
    
    private JPanel createDeleteUserPanel(JDialog dialog) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        try {
            String userList = libraryServer.manageUsers();
            JTextArea textArea = new JTextArea(userList);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textArea);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            JPanel bottomPanel = new JPanel(new FlowLayout());
            JTextField deleteField = new JTextField(15);
            JButton deleteBtn = new JButton("🗑️ Delete Selected User");
            deleteBtn.setBackground(new Color(231, 76, 60));
            deleteBtn.setForeground(Color.BLACK);
            
            bottomPanel.add(new JLabel("Username to delete:"));
            bottomPanel.add(deleteField);
            bottomPanel.add(deleteBtn);
            panel.add(bottomPanel, BorderLayout.SOUTH);
            
            deleteBtn.addActionListener(e -> {
                String username = deleteField.getText().trim();
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "❌ Please enter username to delete!");
                    return;
                }
                
                if (username.equals("amanuel")) {
                    JOptionPane.showMessageDialog(dialog, "❌ Cannot delete the main Admin account!");
                    return;
                }
                
                int confirm = JOptionPane.showConfirmDialog(dialog, 
                    "Are you sure you want to delete user: " + username + "?", 
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        if (libraryServer.manageUser(username, "", "", "", "DELETE")) {
                            JOptionPane.showMessageDialog(dialog, "✅ User deleted successfully!");
                            deleteField.setText("");
                            dialog.dispose();
                            showUserManagementDialog();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "❌ Failed to delete user!\nUser may not exist or is an Admin.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "❌ Error: " + ex.getMessage());
                    }
                }
            });
            
        } catch (Exception e) {
            panel.add(new JLabel("Error loading users: " + e.getMessage()), BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    private JPanel createViewUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        try {
            String userList = libraryServer.manageUsers();
            JTextArea textArea = new JTextArea(userList);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
            JScrollPane scrollPane = new JScrollPane(textArea);
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            panel.add(new JLabel("Error loading users: " + e.getMessage()), BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    private void addLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label, gbc);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return btn;
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(280, 55));
        return btn;
    }

    // ==================== FUNCTIONAL METHODS ====================
    
    private void showReports() {
        try {
            String reports = libraryServer.getSystemReports();
            JTextArea textArea = new JTextArea(reports);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            JOptionPane.showMessageDialog(this, scrollPane, "System Reports", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void backupDatabase() {
        try {
            if (libraryServer.backupDatabase()) {
                JOptionPane.showMessageDialog(this, "✅ Database backup completed successfully!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void addNewBook() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField categoryField = new JTextField();
        
        Object[] message = {
            "Title:", titleField,
            "Author:", authorField,
            "ISBN:", isbnField,
            "Quantity:", quantityField,
            "Category:", categoryField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Add New Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String isbn = isbnField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                String category = categoryField.getText().trim();
                
                if (libraryServer.addBook(title, author, isbn, quantity, category)) {
                    JOptionPane.showMessageDialog(this, "✅ Book added successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to add book!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void issueBook() {
        JTextField usernameField = new JTextField();
        JTextField isbnField = new JTextField();
        
        Object[] message = {
            "Member Username:", usernameField,
            "Book ISBN:", isbnField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Issue Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                if (libraryServer.issueBookToMember(usernameField.getText().trim(), isbnField.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "✅ Book issued successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to issue book!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void returnBook() {
        JTextField usernameField = new JTextField();
        JTextField isbnField = new JTextField();
        
        Object[] message = {
            "Member Username:", usernameField,
            "Book ISBN:", isbnField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Return Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                if (libraryServer.receiveReturn(usernameField.getText().trim(), isbnField.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "✅ Book returned successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to return book!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void viewBorrowingRecords() {
        try {
            String records = libraryServer.viewBorrowingRecords();
            JTextArea textArea = new JTextArea(records);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(800, 500));
            JOptionPane.showMessageDialog(this, scrollPane, "Borrowing Records", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void viewAllBooks() {
        try {
            List<Map<String, Object>> books = libraryServer.getAllBooks();
            if (books == null || books.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No books found in the database!");
                return;
            }
            
            String[] columns = {"ISBN", "Title", "Author", "Category", "Available"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            
            for (Map<String, Object> book : books) {
                Object[] row = {
                    book.get("isbn"),
                    book.get("title"),
                    book.get("author"),
                    book.get("category"),
                    book.get("available")
                };
                model.addRow(row);
            }
            
            JTable table = new JTable(model);
            table.setRowHeight(25);
            table.getTableHeader().setBackground(new Color(108, 92, 231));
            table.getTableHeader().setForeground(Color.BLACK);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(900, 500));
            JOptionPane.showMessageDialog(this, scrollPane, "All Books", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void searchBooks() {
        String keyword = JOptionPane.showInputDialog(this, "Enter search keyword (title/author/ISBN):");
        if (keyword != null && !keyword.trim().isEmpty()) {
            try {
                List<Map<String, Object>> results = libraryServer.searchBooks(keyword);
                if (results == null || results.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No books found matching: " + keyword);
                    return;
                }
                
                String[] columns = {"ISBN", "Title", "Author", "Available"};
                DefaultTableModel model = new DefaultTableModel(columns, 0);
                
                for (Map<String, Object> book : results) {
                    Object[] row = {
                        book.get("isbn"),
                        book.get("title"),
                        book.get("author"),
                        book.get("available")
                    };
                    model.addRow(row);
                }
                
                JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(800, 400));
                JOptionPane.showMessageDialog(this, scrollPane, "Search Results", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void borrowBook() {
        JTextField isbnField = new JTextField();
        Object[] message = { "Enter Book ISBN to borrow:", isbnField };
        int option = JOptionPane.showConfirmDialog(this, message, "Borrow Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                if (libraryServer.borrowBook(currentUser, isbnField.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "✅ Book borrowed successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to borrow book! (Not available or already borrowed)");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void returnBookMember() {
        JTextField isbnField = new JTextField();
        Object[] message = { "Enter Book ISBN to return:", isbnField };
        int option = JOptionPane.showConfirmDialog(this, message, "Return Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                if (libraryServer.returnBook(currentUser, isbnField.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "✅ Book returned successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to return book!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void viewMyHistory() {
        try {
            List<Map<String, Object>> history = libraryServer.getBorrowedHistory(currentUser);
            if (history == null || history.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No borrowing history found!");
                return;
            }
            
            String[] columns = {"Title", "Author", "Borrow Date", "Return Date", "Status"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            for (Map<String, Object> record : history) {
                Object[] row = {
                    record.get("title"), 
                    record.get("author"), 
                    record.get("borrow_date"),
                    record.get("return_date") != null ? record.get("return_date") : "-",
                    record.get("status")
                };
                model.addRow(row);
            }
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(800, 400));
            JOptionPane.showMessageDialog(this, scrollPane, "My Borrowing History", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LibraryClientGUI());
    }
}