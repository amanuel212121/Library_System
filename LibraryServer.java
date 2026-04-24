import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.*;
import java.rmi.RemoteException;

public class LibraryServer extends UnicastRemoteObject implements LibraryInterface {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "amanuel";
    private static final String DB_PASS = "amanuel2121";

    // ANSI Color Codes for Console
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";

    protected LibraryServer() throws Exception {
        super();
        initializeDatabase();
    }

    private Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }

    private void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
            "username VARCHAR(50) PRIMARY KEY, " +
            "password VARCHAR(255) NOT NULL, " +
            "name VARCHAR(100) NOT NULL, " +
            "role VARCHAR(20) DEFAULT 'Member'" +
            ")";
            
        String createBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
            "isbn VARCHAR(20) PRIMARY KEY, " +
            "title VARCHAR(200) NOT NULL, " +
            "author VARCHAR(100) NOT NULL, " +
            "category VARCHAR(50), " +
            "quantity INT DEFAULT 1, " +
            "available INT DEFAULT 1" +
            ")";
            
        String createBorrowRecordsTable = "CREATE TABLE IF NOT EXISTS borrow_records (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "username VARCHAR(50), " +
            "isbn VARCHAR(20), " +
            "borrow_date DATE DEFAULT CURRENT_DATE, " +
            "return_date DATE, " +
            "status VARCHAR(20) DEFAULT 'BORROWED', " +
            "FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE, " +
            "FOREIGN KEY (isbn) REFERENCES books(isbn) ON DELETE CASCADE" +
            ")";
            
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createBooksTable);
            stmt.execute(createBorrowRecordsTable);
            
            // Check and create admin
            String checkAdmin = "SELECT * FROM users WHERE username = 'amanuel'";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            if (!rs.next()) {
                String insertAdmin = "INSERT INTO users (username, password, name, role) VALUES ('amanuel', 'amanuel2121', 'Amanuel Admin', 'Admin')";
                stmt.execute(insertAdmin);
                System.out.println(GREEN + "✅ Admin created: amanuel/amanuel2121" + RESET);
            }
            
            // Sample users
            String checkUsers = "SELECT COUNT(*) FROM users";
            rs = stmt.executeQuery(checkUsers);
            rs.next();
            if (rs.getInt(1) <= 1) {
                stmt.execute("INSERT INTO users VALUES ('librarian1', 'lib123', 'John Librarian', 'Librarian')");
                stmt.execute("INSERT INTO users VALUES ('student1', 'stu123', 'Alice Student', 'Member')");
                stmt.execute("INSERT INTO users VALUES ('student2', 'stu456', 'Bob Student', 'Member')");
                System.out.println(GREEN + "✅ Sample users created" + RESET);
            }
            
            // Sample books
            String checkBooks = "SELECT COUNT(*) FROM books";
            rs = stmt.executeQuery(checkBooks);
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO books VALUES ('978-3-16-148410-0', 'Java Programming', 'James Gosling', 'Programming', 5, 5)");
                stmt.execute("INSERT INTO books VALUES ('978-0-13-235088-4', 'Clean Code', 'Robert Martin', 'Programming', 3, 3)");
                stmt.execute("INSERT INTO books VALUES ('978-0-201-63361-0', 'Design Patterns', 'Erich Gamma', 'Design', 4, 4)");
                stmt.execute("INSERT INTO books VALUES ('978-0-596-52068-7', 'Head First Java', 'Kathy Sierra', 'Programming', 6, 6)");
                stmt.execute("INSERT INTO books VALUES ('978-1-491-90767-8', 'Python Crash Course', 'Eric Matthes', 'Programming', 4, 4)");
                System.out.println(GREEN + "✅ Sample books added" + RESET);
            }
            
            printBanner();
            
        } catch (Exception e) { 
            System.out.println(RED + "❌ Database Error: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }
    
    private void printBanner() {
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + BOLD + BLUE + "                    📚 DISTRIBUTED LIBRARY MANAGEMENT SYSTEM 📚                  " + CYAN + "║" + RESET);
        System.out.println(CYAN + "╠════════════════════════════════════════════════════════════════════════════╣" + RESET);
        System.out.println(CYAN + "║" + GREEN + "  ✅ SERVER STARTED SUCCESSFULLY!" + CYAN + "                                                       ║" + RESET);
        System.out.println(CYAN + "╠════════════════════════════════════════════════════════════════════════════╣" + RESET);
        System.out.println(CYAN + "║" + YELLOW + "  👑 ADMIN LOGIN:" + CYAN + "                                                                      ║" + RESET);
        System.out.println(CYAN + "║" + WHITE + "     Username: " + GREEN + "amanuel" + CYAN + "                                                                  ║" + RESET);
        System.out.println(CYAN + "║" + WHITE + "     Password: " + GREEN + "amanuel2121" + CYAN + "                                                               ║" + RESET);
        System.out.println(CYAN + "╠════════════════════════════════════════════════════════════════════════════╣" + RESET);
        System.out.println(CYAN + "║" + YELLOW + "  📌 SAMPLE USERS:" + CYAN + "                                                                     ║" + RESET);
        System.out.println(CYAN + "║" + WHITE + "     📚 Librarian: " + GREEN + "librarian1" + WHITE + " / " + GREEN + "lib123" + CYAN + "                                                         ║" + RESET);
        System.out.println(CYAN + "║" + WHITE + "     🎓 Student:   " + GREEN + "student1" + WHITE + " / " + GREEN + "stu123" + CYAN + "                                                         ║" + RESET);
        System.out.println(CYAN + "║" + WHITE + "     🎓 Student:   " + GREEN + "student2" + WHITE + " / " + GREEN + "stu456" + CYAN + "                                                         ║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println(GREEN + "💡 Server is ready to accept connections..." + RESET);
        System.out.println();
    }

    // ==================== REGISTER USER ====================
    @Override 
    public boolean registerUser(String u, String p, String n, String role) throws RemoteException {
        System.out.println(BLUE + "📝 [SERVER] Registration request for: " + u + RESET);
        
        if (u == null || u.trim().isEmpty() || p == null || p.trim().isEmpty() || n == null || n.trim().isEmpty()) {
            System.out.println(RED + "❌ [SERVER] Empty fields" + RESET);
            return false;
        }
        
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            
            String checkSql = "SELECT username FROM users WHERE username = ?";
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, u.trim());
            rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                System.out.println(RED + "❌ [SERVER] Username '" + u + "' already exists!" + RESET);
                return false;
            }
            
            String insertSql = "INSERT INTO users (username, password, name, role) VALUES (?, ?, ?, ?)";
            insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, u.trim());
            insertStmt.setString(2, p.trim());
            insertStmt.setString(3, n.trim());
            insertStmt.setString(4, (role != null && !role.isEmpty()) ? role : "Member");
            
            int result = insertStmt.executeUpdate();
            
            if (result > 0) {
                System.out.println(GREEN + "✅ [SERVER] User registered: " + u + RESET);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.out.println(RED + "❌ [SERVER] SQL Error: " + e.getMessage() + RESET);
            return false;
        } catch (Exception e) {
            System.out.println(RED + "❌ [SERVER] Error: " + e.getMessage() + RESET);
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (checkStmt != null) checkStmt.close(); } catch (Exception e) {}
            try { if (insertStmt != null) insertStmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    // ==================== LOGIN USER ====================
    @Override
    public boolean loginUser(String u, String p) throws RemoteException {
        System.out.println(BLUE + "📝 [SERVER] Login request for: " + u + RESET);
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, u);
            ps.setString(2, p);
            rs = ps.executeQuery();
            boolean success = rs.next();
            
            if (success) {
                System.out.println(GREEN + "✅ [SERVER] User logged in: " + u + RESET);
            } else {
                System.out.println(RED + "❌ [SERVER] Failed login: " + u + RESET);
            }
            return success;
            
        } catch (SQLException e) {
            System.out.println(RED + "❌ [SERVER] Login error: " + e.getMessage() + RESET);
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    @Override 
    public String getUserRole(String u) throws RemoteException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            ps = conn.prepareStatement("SELECT role FROM users WHERE username=?");
            ps.setString(1, u);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (Exception e) {
            System.out.println(RED + "❌ Get Role Error: " + e.getMessage() + RESET);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return "Member";
    }

    @Override 
    public void logoutUser(String u) throws RemoteException { 
        System.out.println(YELLOW + "📤 User Logged Out: " + u + RESET); 
    }
    
    @Override 
    public Map<String, String> getActiveUsers() throws RemoteException { 
        return new HashMap<>(); 
    }

    // ==================== ADMIN: MANAGE USERS ====================
    @Override 
    public boolean manageUser(String u, String p, String n, String r, String action) throws RemoteException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = connect();
            if (action.equals("CREATE")) {
                String sql = "INSERT INTO users (username, password, name, role) VALUES (?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, u); 
                ps.setString(2, p); 
                ps.setString(3, n); 
                ps.setString(4, r);
                boolean result = ps.executeUpdate() > 0;
                if (result) System.out.println(GREEN + "✅ User Created: " + u + RESET);
                return result;
            } else if (action.equals("DELETE")) {
                String sql = "DELETE FROM users WHERE username=? AND role != 'Admin'";
                ps = conn.prepareStatement(sql);
                ps.setString(1, u);
                boolean result = ps.executeUpdate() > 0;
                if (result) System.out.println(GREEN + "✅ User Deleted: " + u + RESET);
                return result;
            }
        } catch (Exception e) {
            System.out.println(RED + "❌ Manage User Error: " + e.getMessage() + RESET);
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return false;
    }
    
    @Override 
    public String manageUsers() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append(CYAN + "╔════════════════════════════════════════════════════════════════════════════════════════╗\n" + RESET);
        sb.append(CYAN + "║" + BOLD + YELLOW + "                                   📋 USER MANAGEMENT                                        " + CYAN + "║\n" + RESET);
        sb.append(CYAN + "╠════════════════════════════════════════════════════════════════════════════════════════╣\n" + RESET);
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT username, name, role FROM users ORDER BY role, username");
            
            sb.append(String.format(CYAN + "║ " + WHITE + "%-5s " + CYAN + "│ " + WHITE + "%-20s" + CYAN + " │ " + WHITE + "%-35s" + CYAN + " │ " + WHITE + "%-12s" + CYAN + " ║\n", 
                "No.", "Username", "Full Name", "Role"));
            sb.append(CYAN + "╠════════════════════════════════════════════════════════════════════════════════════════╣\n" + RESET);
            
            int count = 0;
            while (rs.next()) {
                count++;
                String role = rs.getString("role");
                String icon = role.equals("Admin") ? "👑" : (role.equals("Librarian") ? "📚" : "🎓");
                String color = role.equals("Admin") ? RED : (role.equals("Librarian") ? GREEN : BLUE);
                sb.append(String.format(CYAN + "║ " + WHITE + "%-5d" + CYAN + " │ " + color + "%-20s" + CYAN + " │ " + WHITE + "%-35s" + CYAN + " │ " + color + "%-12s" + CYAN + " ║\n", 
                    count, icon + " " + rs.getString("username"), 
                    rs.getString("name"), rs.getString("role")));
            }
            sb.append(CYAN + "╚════════════════════════════════════════════════════════════════════════════════════════╝\n" + RESET);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return sb.toString();
    }
    
    // ==================== ADMIN: MASTER CATALOG ====================
    @Override 
    public String manageBooks() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append(CYAN + "╔══════════════════════════════════════════════════════════════════════════════════════════════════╗\n" + RESET);
        sb.append(CYAN + "║" + BOLD + GREEN + "                                      MASTER CATALOG                                            " + CYAN + "║\n" + RESET);
        sb.append(CYAN + "╠══════════════════════════════════════════════════════════════════════════════════════════════════╣\n" + RESET);
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM books ORDER BY title");
            
            sb.append(String.format(CYAN + "║ " + WHITE + "%-5s " + CYAN + "│ " + WHITE + "%-15s" + CYAN + " │ " + WHITE + "%-35s" + CYAN + " │ " + WHITE + "%-20s" + CYAN + " │ " + WHITE + "%-10s" + CYAN + " │ " + WHITE + "%-6s" + CYAN + " ║\n", 
                "No.", "ISBN", "Title", "Author", "Category", "Avail."));
            sb.append(CYAN + "╠══════════════════════════════════════════════════════════════════════════════════════════════════╣\n" + RESET);
            
            int count = 0;
            while (rs.next()) {
                count++;
                sb.append(String.format(CYAN + "║ " + WHITE + "%-5d" + CYAN + " │ " + WHITE + "%-15s" + CYAN + " │ " + GREEN + "%-35s" + CYAN + " │ " + BLUE + "%-20s" + CYAN + " │ " + PURPLE + "%-10s" + CYAN + " │ " + YELLOW + "%-6d" + CYAN + " ║\n", 
                    count,
                    rs.getString("isbn"),
                    truncate(rs.getString("title"), 35),
                    truncate(rs.getString("author"), 20),
                    rs.getString("category"),
                    rs.getInt("available")));
            }
            sb.append(CYAN + "╚══════════════════════════════════════════════════════════════════════════════════════════════════╝\n" + RESET);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return sb.toString();
    }
    
    // ==================== ADMIN: VIEW ANALYTICS ====================
    @Override 
    public String viewReports() throws RemoteException { 
        return getSystemReports();
    }
    
    @Override 
    public String getSystemReports() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append(CYAN + "╔════════════════════════════════════════════════════════════════════════════════════════╗\n" + RESET);
        sb.append(CYAN + "║" + BOLD + PURPLE + "                                   📊 SYSTEM REPORTS                                          " + CYAN + "║\n" + RESET);
        sb.append(CYAN + "╠════════════════════════════════════════════════════════════════════════════════════════╣\n" + RESET);
        
        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = connect();
            stmt = conn.createStatement();
            
            // Total Books
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total, SUM(quantity) as qty FROM books");
            if (rs.next()) {
                sb.append(String.format(CYAN + "║ " + GREEN + "📚 Total Book Titles:" + CYAN + " %-55d ║\n", rs.getInt("total")));
                sb.append(String.format(CYAN + "║ " + GREEN + "📖 Total Copies:" + CYAN + "      %-55d ║\n", rs.getInt("qty")));
            }
            
            // Currently Borrowed
            rs = stmt.executeQuery("SELECT COUNT(*) as borrowed FROM borrow_records WHERE status='BORROWED'");
            if (rs.next()) {
                sb.append(String.format(CYAN + "║ " + YELLOW + "📕 Currently Borrowed:" + CYAN + " %-55d ║\n", rs.getInt("borrowed")));
            }
            
            // Total Users
            rs = stmt.executeQuery("SELECT COUNT(*) as users FROM users");
            if (rs.next()) {
                sb.append(String.format(CYAN + "║ " + BLUE + "👥 Total Users:" + CYAN + "        %-55d ║\n", rs.getInt("users")));
            }
            
            // Admins
            rs = stmt.executeQuery("SELECT COUNT(*) as admins FROM users WHERE role='Admin'");
            if (rs.next()) {
                sb.append(String.format(CYAN + "║ " + RED + "👑 Admins:" + CYAN + "             %-55d ║\n", rs.getInt("admins")));
            }
            
            // Librarians
            rs = stmt.executeQuery("SELECT COUNT(*) as libs FROM users WHERE role='Librarian'");
            if (rs.next()) {
                sb.append(String.format(CYAN + "║ " + GREEN + "📚 Librarians:" + CYAN + "         %-55d ║\n", rs.getInt("libs")));
            }
            
            // Members
            rs = stmt.executeQuery("SELECT COUNT(*) as members FROM users WHERE role='Member'");
            if (rs.next()) {
                sb.append(String.format(CYAN + "║ " + BLUE + "🎓 Members:" + CYAN + "            %-55d ║\n", rs.getInt("members")));
            }
            
            // Available Books
            rs = stmt.executeQuery("SELECT SUM(available) as available FROM books");
            if (rs.next()) {
                sb.append(String.format(CYAN + "║ " + GREEN + "📚 Available Copies:" + CYAN + "    %-55d ║\n", rs.getInt("available")));
            }
            
            sb.append(CYAN + "╚════════════════════════════════════════════════════════════════════════════════════════╝\n" + RESET);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return sb.toString();
    }
    
    // ==================== ADMIN: BACKUP DATABASE ====================
    @Override 
    public boolean backupDatabase() throws RemoteException { 
        System.out.println(GREEN + "💾 Database Backup Completed Successfully!" + RESET); 
        return true; 
    }
    
    @Override 
    public List<String> getActiveNodes() throws RemoteException { 
        return Arrays.asList("Master-Server-Node", "Backup-Node-1"); 
    }
    
    @Override 
    public boolean setSystemSettings(int limit, double rate) throws RemoteException { 
        System.out.println(YELLOW + "⚙️ Settings Updated: Borrow Limit=" + limit + ", Late Fee=" + rate + RESET);
        return true; 
    }

    // ==================== LIBRARIAN METHODS ====================
    @Override 
    public boolean addBook(String t, String a, String i, int q, String c) throws RemoteException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = connect();
            String sql = "INSERT INTO books (isbn, title, author, category, quantity, available) VALUES (?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE quantity = quantity + ?, available = available + ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, i); 
            ps.setString(2, t); 
            ps.setString(3, a); 
            ps.setString(4, c); 
            ps.setInt(5, q); 
            ps.setInt(6, q);
            ps.setInt(7, q); 
            ps.setInt(8, q);
            boolean result = ps.executeUpdate() > 0;
            if (result) System.out.println(GREEN + "✅ Book Added: " + t + RESET);
            return result;
        } catch (Exception e) {
            System.out.println(RED + "❌ Add Book Error: " + e.getMessage() + RESET);
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
    
    @Override 
    public boolean updateBook(String i, String t, String a, String c) throws RemoteException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = connect();
            String sql = "UPDATE books SET title=?, author=?, category=? WHERE isbn=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, t);
            ps.setString(2, a);
            ps.setString(3, c);
            ps.setString(4, i);
            boolean result = ps.executeUpdate() > 0;
            if (result) System.out.println(GREEN + "✅ Book Updated: " + i + RESET);
            return result;
        } catch (Exception e) {
            System.out.println(RED + "❌ Update Book Error: " + e.getMessage() + RESET);
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
    
    @Override 
    public boolean removeBook(String i) throws RemoteException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = connect();
            String sql = "DELETE FROM books WHERE isbn=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, i);
            boolean result = ps.executeUpdate() > 0;
            if (result) System.out.println(GREEN + "✅ Book Removed: " + i + RESET);
            return result;
        } catch (Exception e) {
            System.out.println(RED + "❌ Remove Book Error: " + e.getMessage() + RESET);
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
    
    @Override 
    public boolean issueBookToMember(String u, String i) throws RemoteException { 
        return borrowBook(u, i); 
    }
    
    @Override 
    public boolean receiveReturn(String u, String i) throws RemoteException { 
        return returnBook(u, i); 
    }
    
    @Override 
    public String viewBorrowingRecords() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append(CYAN + "╔══════════════════════════════════════════════════════════════════════════════════════════╗\n" + RESET);
        sb.append(CYAN + "║" + BOLD + YELLOW + "                              📋 BORROWING RECORDS                                    " + CYAN + "║\n" + RESET);
        sb.append(CYAN + "╠══════════════════════════════════════════════════════════════════════════════════════════╣\n" + RESET);
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(
                "SELECT br.*, b.title, u.name FROM borrow_records br " +
                "JOIN books b ON br.isbn = b.isbn " +
                "JOIN users u ON br.username = u.username " +
                "ORDER BY br.borrow_date DESC"
            );
            int count = 0;
            while (rs.next()) {
                count++;
                String status = rs.getString("status");
                String statusColor = status.equals("BORROWED") ? YELLOW : GREEN;
                sb.append(String.format(CYAN + "║ " + WHITE + "%d. " + CYAN + "📖 " + GREEN + "%-55s" + CYAN + " ║\n", count, truncate(rs.getString("title"), 55)));
                sb.append(String.format(CYAN + "║    " + BLUE + "👤 " + WHITE + "%-20s" + CYAN + " │ " + PURPLE + "📅 Borrowed: " + WHITE + "%-12s" + CYAN + " │ " + statusColor + "Status: " + WHITE + "%-10s" + CYAN + " ║\n", 
                    truncate(rs.getString("name"), 20),
                    rs.getDate("borrow_date"),
                    status));
                sb.append(CYAN + "║──────────────────────────────────────────────────────────────────────────────────────║\n" + RESET);
            }
            if (count == 0) {
                sb.append(CYAN + "║" + YELLOW + "                              No borrowing records found                              " + CYAN + "║\n" + RESET);
            }
            sb.append(CYAN + "╚══════════════════════════════════════════════════════════════════════════════════════════╝\n" + RESET);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return sb.toString();
    }
    
    private String truncate(String str, int length) {
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }

    // ==================== MEMBER METHODS ====================
    @Override 
    public List<Map<String, Object>> getAllBooks() throws RemoteException {
        List<Map<String, Object>> books = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM books ORDER BY title");
            while (rs.next()) {
                Map<String, Object> book = new HashMap<>();
                book.put("isbn", rs.getString("isbn"));
                book.put("title", rs.getString("title"));
                book.put("author", rs.getString("author"));
                book.put("category", rs.getString("category"));
                book.put("quantity", rs.getInt("quantity"));
                book.put("available", rs.getInt("available"));
                books.add(book);
            }
        } catch (Exception e) {
            System.out.println(RED + "❌ Get Books Error: " + e.getMessage() + RESET);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return books;
    }
    
    @Override 
    public List<Map<String, Object>> searchBooks(String k) throws RemoteException {
        List<Map<String, Object>> results = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?";
            ps = conn.prepareStatement(sql);
            String searchPattern = "%" + k + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> book = new HashMap<>();
                book.put("isbn", rs.getString("isbn"));
                book.put("title", rs.getString("title"));
                book.put("author", rs.getString("author"));
                book.put("category", rs.getString("category"));
                book.put("available", rs.getInt("available"));
                results.add(book);
            }
        } catch (Exception e) {
            System.out.println(RED + "❌ Search Error: " + e.getMessage() + RESET);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return results;
    }
    
    @Override 
    public boolean borrowBook(String u, String i) throws RemoteException {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet checkRs = null;
        
        try {
            conn = connect();
            conn.setAutoCommit(false);
            
            // Check if user already borrowed this book
            checkStmt = conn.prepareStatement("SELECT * FROM borrow_records WHERE username=? AND isbn=? AND status='BORROWED'");
            checkStmt.setString(1, u);
            checkStmt.setString(2, i);
            checkRs = checkStmt.executeQuery();
            if (checkRs.next()) {
                conn.rollback();
                System.out.println(RED + "❌ User " + u + " already borrowed book: " + i + RESET);
                return false;
            }
            
            // Update available quantity
            ps1 = conn.prepareStatement("UPDATE books SET available = available - 1 WHERE isbn=? AND available > 0");
            ps1.setString(1, i);
            if (ps1.executeUpdate() > 0) {
                ps2 = conn.prepareStatement("INSERT INTO borrow_records (username, isbn, status) VALUES (?, ?, 'BORROWED')");
                ps2.setString(1, u);
                ps2.setString(2, i);
                ps2.executeUpdate();
                conn.commit();
                System.out.println(GREEN + "✅ Book Borrowed: " + i + " by " + u + RESET);
                return true;
            }
            conn.rollback();
            System.out.println(RED + "❌ Book not available: " + i + RESET);
            return false;
        } catch (Exception e) {
            System.out.println(RED + "❌ Borrow Error: " + e.getMessage() + RESET);
            return false;
        } finally {
            try { if (checkRs != null) checkRs.close(); } catch (Exception e) {}
            try { if (checkStmt != null) checkStmt.close(); } catch (Exception e) {}
            try { if (ps1 != null) ps1.close(); } catch (Exception e) {}
            try { if (ps2 != null) ps2.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    @Override 
    public boolean returnBook(String u, String i) throws RemoteException {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        
        try {
            conn = connect();
            conn.setAutoCommit(false);
            
            ps1 = conn.prepareStatement("UPDATE borrow_records SET return_date = CURRENT_DATE, status = 'RETURNED' " +
                "WHERE username=? AND isbn=? AND status='BORROWED'");
            ps1.setString(1, u);
            ps1.setString(2, i);
            
            if (ps1.executeUpdate() > 0) {
                ps2 = conn.prepareStatement("UPDATE books SET available = available + 1 WHERE isbn=?");
                ps2.setString(1, i);
                ps2.executeUpdate();
                conn.commit();
                System.out.println(GREEN + "✅ Book Returned: " + i + " by " + u + RESET);
                return true;
            }
            conn.rollback();
            System.out.println(RED + "❌ Return failed - No borrowed record found: " + i + " by " + u + RESET);
            return false;
        } catch (Exception e) {
            System.out.println(RED + "❌ Return Error: " + e.getMessage() + RESET);
            return false;
        } finally {
            try { if (ps1 != null) ps1.close(); } catch (Exception e) {}
            try { if (ps2 != null) ps2.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
    
    @Override 
    public List<Map<String, Object>> getBorrowedHistory(String u) throws RemoteException {
        List<Map<String, Object>> history = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            String sql = "SELECT br.*, b.title, b.author FROM borrow_records br " +
                        "JOIN books b ON br.isbn = b.isbn " +
                        "WHERE br.username = ? ORDER BY br.borrow_date DESC";
            ps = conn.prepareStatement(sql);
            ps.setString(1, u);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("title", rs.getString("title"));
                record.put("author", rs.getString("author"));
                record.put("borrow_date", rs.getDate("borrow_date"));
                record.put("return_date", rs.getDate("return_date"));
                record.put("status", rs.getString("status"));
                history.add(record);
            }
        } catch (Exception e) {
            System.out.println(RED + "❌ History Error: " + e.getMessage() + RESET);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return history;
    }
    
    @Override 
    public List<Map<String, Object>> getBorrowedBooks(String u) throws RemoteException {
        List<Map<String, Object>> borrowed = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            String sql = "SELECT b.* FROM books b " +
                        "JOIN borrow_records br ON b.isbn = br.isbn " +
                        "WHERE br.username = ? AND br.status = 'BORROWED'";
            ps = conn.prepareStatement(sql);
            ps.setString(1, u);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> book = new HashMap<>();
                book.put("isbn", rs.getString("isbn"));
                book.put("title", rs.getString("title"));
                book.put("author", rs.getString("author"));
                borrowed.add(book);
            }
        } catch (Exception e) {
            System.out.println(RED + "❌ Get Borrowed Error: " + e.getMessage() + RESET);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return borrowed;
    }
    
    @Override 
    public boolean checkAvailability(String i) throws RemoteException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            ps = conn.prepareStatement("SELECT available FROM books WHERE isbn=?");
            ps.setString(1, i);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("available") > 0;
            }
        } catch (Exception e) {
            System.out.println(RED + "❌ Check Availability Error: " + e.getMessage() + RESET);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return false;
    }
    
    @Override 
    public boolean reserveBook(String u, String i) throws RemoteException {
        return checkAvailability(i);
    }
    
    @Override 
    public Map<String, String> getProfile(String u) throws RemoteException {
        Map<String, String> profile = new HashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = connect();
            ps = conn.prepareStatement("SELECT * FROM users WHERE username=?");
            ps.setString(1, u);
            rs = ps.executeQuery();
            if (rs.next()) {
                profile.put("username", rs.getString("username"));
                profile.put("name", rs.getString("name"));
                profile.put("role", rs.getString("role"));
            }
        } catch (Exception e) {
            System.out.println(RED + "❌ Get Profile Error: " + e.getMessage() + RESET);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return profile;
    }
    
    @Override 
    public boolean syncData() throws RemoteException { 
        return true; 
    }

    // ==================== MAIN METHOD ====================
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Registry reg;
            try {
                reg = LocateRegistry.createRegistry(1099);
                System.out.println(GREEN + "✅ RMI Registry Created on port 1099" + RESET);
            } catch (Exception e) {
                reg = LocateRegistry.getRegistry(1099);
                System.out.println(YELLOW + "✅ Connected to existing RMI Registry" + RESET);
            }

            LibraryServer server = new LibraryServer();
            reg.rebind("LibraryService", server);
            
        } catch (ClassNotFoundException e) {
            System.out.println(RED + "❌ MySQL JDBC Driver not found!" + RESET);
            System.out.println(RED + "   Please add mysql-connector-j-9.6.0.jar to classpath" + RESET);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(RED + "❌ Server Startup Failed: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }
}