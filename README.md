<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Library Management System - RMI Distributed Project</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Helvetica Neue', sans-serif;
            background: linear-gradient(135deg, #f5f7fa 0%, #e8ecf1 100%);
            color: #2c3e50;
            line-height: 1.6;
        }

        /* Navigation */
        .navbar {
            background: linear-gradient(135deg, #1a252f 0%, #2c3e50 100%);
            padding: 1rem 2rem;
            position: sticky;
            top: 0;
            z-index: 1000;
            box-shadow: 0 2px 20px rgba(0,0,0,0.1);
        }

        .nav-container {
            max-width: 1400px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 1rem;
        }

        .logo {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .logo-icon {
            font-size: 2rem;
        }

        .logo-text {
            font-size: 1.3rem;
            font-weight: bold;
            color: white;
        }

        .nav-links {
            display: flex;
            gap: 1.5rem;
            flex-wrap: wrap;
        }

        .nav-links a {
            color: white;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s;
        }

        .nav-links a:hover {
            color: #3498db;
        }

        /* Hero Section */
        .hero {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 4rem 2rem;
            text-align: center;
        }

        .hero h1 {
            font-size: 2.8rem;
            margin-bottom: 1rem;
            animation: fadeInDown 0.8s ease;
        }

        .hero p {
            font-size: 1.2rem;
            max-width: 800px;
            margin: 0 auto;
            opacity: 0.9;
        }

        @keyframes fadeInDown {
            from {
                opacity: 0;
                transform: translateY(-30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* Badges */
        .badge-container {
            margin: 2rem 0;
            display: flex;
            justify-content: center;
            gap: 1rem;
            flex-wrap: wrap;
        }

        .badge {
            display: inline-block;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            font-weight: 600;
            font-size: 0.85rem;
        }

        .badge-java { background: #ED8B00; color: white; }
        .badge-rmi { background: #007396; color: white; }
        .badge-mysql { background: #4479A1; color: white; }
        .badge-swing { background: #5382a1; color: white; }

        /* Container */
        .container {
            max-width: 1400px;
            margin: 0 auto;
            padding: 2rem;
        }

        /* Cards */
        .card {
            background: white;
            border-radius: 16px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 10px 40px rgba(0,0,0,0.08);
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .card:hover {
            transform: translateY(-3px);
            box-shadow: 0 20px 60px rgba(0,0,0,0.12);
        }

        .card h2 {
            color: #2c3e50;
            margin-bottom: 1.5rem;
            border-left: 4px solid #3498db;
            padding-left: 1rem;
            font-size: 1.8rem;
        }

        .card h3 {
            color: #34495e;
            margin: 1.5rem 0 1rem 0;
            font-size: 1.3rem;
        }

        /* Grid */
        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
            gap: 1.5rem;
            margin: 1.5rem 0;
        }

        /* Feature Box */
        .feature-box {
            background: #f8f9fa;
            border-radius: 12px;
            padding: 1.2rem;
            border-left: 3px solid #3498db;
        }

        .feature-box ul {
            list-style: none;
            padding-left: 0;
        }

        .feature-box li {
            padding: 0.4rem 0;
            padding-left: 1.5rem;
            position: relative;
        }

        .feature-box li:before {
            content: "✅";
            position: absolute;
            left: 0;
        }

        /* Architecture Diagram */
        .architecture {
            background: #1a1a2e;
            color: #eee;
            padding: 2rem;
            border-radius: 12px;
            overflow-x: auto;
            font-family: monospace;
            font-size: 0.85rem;
            text-align: center;
        }

        .architecture pre {
            color: #00ff88;
            margin: 0;
            white-space: pre-wrap;
            word-wrap: break-word;
        }

        /* Code Block */
        .code-block {
            background: #1e1e1e;
            color: #d4d4d4;
            padding: 1rem;
            border-radius: 8px;
            overflow-x: auto;
            font-family: 'Courier New', monospace;
            font-size: 0.85rem;
            margin: 1rem 0;
        }

        .code-block pre {
            margin: 0;
        }

        /* Table */
        .table-wrapper {
            overflow-x: auto;
            margin: 1rem 0;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 8px;
            overflow: hidden;
        }

        th {
            background: #3498db;
            color: white;
            padding: 12px;
            text-align: left;
            font-weight: 600;
        }

        td {
            padding: 10px 12px;
            border-bottom: 1px solid #ddd;
        }

        tr:hover {
            background: #f5f5f5;
        }

        /* Stats Grid */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
            margin: 1rem 0;
        }

        .stat-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 1.5rem;
            border-radius: 12px;
            text-align: center;
        }

        .stat-number {
            font-size: 2rem;
            font-weight: bold;
        }

        .stat-label {
            font-size: 0.85rem;
            opacity: 0.9;
        }

        /* Button */
        .btn {
            display: inline-block;
            background: #3498db;
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            transition: background 0.3s;
            margin-top: 1rem;
        }

        .btn:hover {
            background: #2980b9;
        }

        /* Footer */
        .footer {
            background: #1a252f;
            color: white;
            text-align: center;
            padding: 2rem;
            margin-top: 2rem;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .hero h1 { font-size: 1.8rem; }
            .card h2 { font-size: 1.4rem; }
            .nav-container { flex-direction: column; }
        }
    </style>
</head>
<body>

<!-- Navigation -->
<nav class="navbar">
    <div class="nav-container">
        <div class="logo">
            <span class="logo-icon">📚</span>
            <span class="logo-text">Library Management System</span>
        </div>
        <div class="nav-links">
            <a href="#overview">Overview</a>
            <a href="#features">Features</a>
            <a href="#architecture">Architecture</a>
            <a href="#setup">Setup</a>
            <a href="#database">Database</a>
            <a href="#api">API</a>
        </div>
    </div>
</nav>

<!-- Hero Section -->
<section class="hero">
    <h1>📚 Library Management System</h1>
    <p>A complete distributed Library Management System built using Java RMI architecture with three-tier distributed architecture for client, server, and database.</p>
    
    <div class="badge-container">
        <span class="badge badge-java">☕ Java</span>
        <span class="badge badge-rmi">📡 RMI</span>
        <span class="badge badge-mysql">🐬 MySQL</span>
        <span class="badge badge-swing">🎨 Swing</span>
    </div>
</section>

<div class="container">

    <!-- Project Overview -->
    <section id="overview" class="card">
        <h2>🎯 Project Overview</h2>
        <p>A complete distributed Library Management System built using <strong>Java RMI (Remote Method Invocation)</strong> architecture. The system demonstrates a three-tier distributed architecture with separate components for client, server, and database.</p>
    </section>

    <!-- Key Features -->
    <section id="features" class="card">
        <h2>✅ Key Features</h2>
        <div class="grid">
            <div class="feature-box">
                <ul>
                    <li>Complete Book Management (Add, Update, Delete, Search)</li>
                    <li>Member Registration & Management</li>
                    <li>Borrow & Return System with Transaction Tracking</li>
                    <li>Advanced Search (by Title, Author, ISBN)</li>
                </ul>
            </div>
            <div class="feature-box">
                <ul>
                    <li>Real-time Statistics Dashboard</li>
                    <li>Secure Authentication (Admin & Member Roles)</li>
                    <li>Professional Colorful GUI using Java Swing</li>
                    <li>Console Client Alternative</li>
                </ul>
            </div>
            <div class="feature-box">
                <ul>
                    <li>MySQL Database Integration</li>
                    <li>Transaction History for each member</li>
                    <li>Popular Books Reports</li>
                    <li>Cross-platform compatibility</li>
                </ul>
            </div>
        </div>
    </section>

    <!-- System Architecture -->
    <section id="architecture" class="card">
        <h2>🏗️ System Architecture</h2>
        <div class="architecture">
            <pre>
┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
│                 │         │                 │         │                 │
│   GUI Client    │◄──────►│   RMI Server    │◄──────►│     MySQL       │
│   (Java Swing)  │   RMI   │   (Java RMI)    │   JDBC  │   Database      │
│                 │         │                 │         │                 │
└─────────────────┘         └─────────────────┘         └─────────────────┘
        │                           │                           │
        │                           │                           │
    Machine 1                   Machine 2                   Machine 3
   (Client PC)                 (Server PC)                (Database PC)
            </pre>
        </div>
    </section>

    <!-- Project Structure -->
    <section class="card">
        <h2>📁 Project Structure</h2>
        <div class="code-block">
            <pre>
LibrarySystem/
│
├── Server.java              # RMI Server Implementation
├── LibraryGUI.java          # Colorful GUI Client
├── Client.java              # Console Client (Alternative)
│
├── mysql-connector-j-8.0.33.jar  # MySQL JDBC Driver
│
└── README.md                # Project Documentation
            </pre>
        </div>
    </section>

    <!-- Installation & Setup -->
    <section id="setup" class="card">
        <h2>🚀 Installation & Setup</h2>
        
        <h3>📋 Prerequisites</h3>
        <ul style="margin-left: 2rem; margin-bottom: 1rem;">
            <li>Java JDK 11 or higher</li>
            <li>XAMPP (for MySQL database)</li>
            <li>MySQL Connector/J</li>
        </ul>

        <h3>📝 Step-by-Step Setup</h3>
        
        <div class="code-block">
            <pre># 1. Start XAMPP and MySQL
# Open XAMPP Control Panel → Click "Start" next to MySQL

# 2. Download MySQL Connector
# Save mysql-connector-j-8.0.33.jar in the project folder

# 3. Compile the Server (Windows)
javac -cp "mysql-connector-j-8.0.33.jar;." Server.java

# 4. Start the RMI Server
java -cp "mysql-connector-j-8.0.33.jar;." LibraryServerImpl

# 5. Run GUI Client (New Terminal)
javac -cp "mysql-connector-j-8.0.33.jar;." LibraryGUI.java
java -cp "mysql-connector-j-8.0.33.jar;." LibraryGUI</pre>
        </div>

        <h3>📤 Expected Server Output</h3>
        <div class="code-block">
            <pre>
=========================================
LIBRARY RMI SERVER STARTING
=========================================

✅ RMI Registry started on port 1099
✅ Connected to MySQL Server
✅ Database ready
✅ Tables created
✅ Sample data inserted

✅ SERVER READY!
URL: rmi://localhost:1099/LibraryService

LOGIN CREDENTIALS:
  Admin:  admin / admin123
  Member: john / member123

Press Ctrl+C to stop</pre>
        </div>
    </section>

    <!-- Login Credentials -->
    <section class="card">
        <h2>🔐 Login Credentials</h2>
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr><th>Role</th><th>Username</th><th>Password</th><th>Access Level</th></tr>
                </thead>
                <tbody>
                    <tr><td>👑 Admin</td><td>admin</td><td>admin123</td><td>Full System Access</td></tr>
                    <tr><td>👤 Member</td><td>john</td><td>member123</td><td>Member Portal Access</td></tr>
                </tbody>
            </table>
        </div>
    </section>

    <!-- Sample Data -->
    <section class="card">
        <h2>📖 Sample Data</h2>
        
        <h3>Sample Books (Pre-loaded)</h3>
        <div class="table-wrapper">
            <table>
                <thead><tr><th>ISBN</th><th>Title</th><th>Author</th><th>Copies</th></tr></thead>
                <tbody>
                    <tr><td>9780136091813</td><td>Distributed Systems</td><td>Tanenbaum</td><td>3</td></tr>
                    <tr><td>9780201633610</td><td>Design Patterns</td><td>Gamma</td><td>2</td></tr>
                    <tr><td>9780596007126</td><td>Head First Java</td><td>Sierra</td><td>5</td></tr>
                </tbody>
            </table>
        </div>

        <h3>Sample Members (Pre-loaded)</h3>
        <div class="table-wrapper">
            <table>
                <thead><tr><th>Member ID</th><th>Name</th><th>Email</th><th>Phone</th></tr></thead>
                <tbody>
                    <tr><td>M001</td><td>John Doe</td><td>john@email.com</td><td>0912345678</td></tr>
                    <tr><td>M002</td><td>Jane Smith</td><td>jane@email.com</td><td>0923456789</td></tr>
                </tbody>
            </table>
        </div>
    </section>

    <!-- Features Detail -->
    <section class="card">
        <h2>🎮 Features in Detail</h2>
        <div class="grid">
            <div class="feature-box">
                <strong>📚 Book Management Tab</strong>
                <ul>
                    <li>View all books in formatted table</li>
                    <li>Add/Update/Delete books</li>
                    <li>Real-time availability status</li>
                </ul>
            </div>
            <div class="feature-box">
                <strong>🔍 Search Tab</strong>
                <ul>
                    <li>Search by Title, Author, ISBN</li>
                    <li>Instant results display</li>
                    <li>Clear search functionality</li>
                </ul>
            </div>
            <div class="feature-box">
                <strong>📖 Borrow/Return Tab</strong>
                <ul>
                    <li>Select member from dropdown</li>
                    <li>Borrow with availability check</li>
                    <li>View currently borrowed books</li>
                </ul>
            </div>
            <div class="feature-box">
                <strong>👥 Members Tab</strong>
                <ul>
                    <li>View all registered members</li>
                    <li>Register/Update/Delete members</li>
                    <li>Automatic member ID generation</li>
                </ul>
            </div>
            <div class="feature-box">
                <strong>📊 Statistics Tab</strong>
                <ul>
                    <li>System-wide statistics</li>
                    <li>Most popular books report</li>
                    <li>Recent transactions history</li>
                </ul>
            </div>
        </div>
    </section>

    <!-- Technologies -->
    <section class="card">
        <h2>🛠️ Technologies Used</h2>
        <div class="table-wrapper">
            <table>
                <thead><tr><th>Category</th><th>Technology</th><th>Purpose</th></tr></thead>
                <tbody>
                    <tr><td>Language</td><td>Java 11+</td><td>Core development</td></tr>
                    <tr><td>RMI</td><td>Java RMI</td><td>Remote communication</td></tr>
                    <tr><td>Database</td><td>MySQL</td><td>Data persistence</td></tr>
                    <tr><td>GUI</td><td>Java Swing</td><td>User interface</td></tr>
                    <tr><td>JDBC</td><td>MySQL Connector/J</td><td>Database connectivity</td></tr>
                </tbody>
            </table>
        </div>
    </section>

    <!-- Database Schema -->
    <section id="database" class="card">
        <h2>📊 Database Schema</h2>
        <div class="code-block">
            <pre>
-- Books Table
CREATE TABLE books (
    isbn VARCHAR(20) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    total_copies INT DEFAULT 1,
    available_copies INT DEFAULT 1,
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Members Table
CREATE TABLE members (
    member_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    registered_date DATE
);

-- Transactions Table
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id VARCHAR(10),
    isbn VARCHAR(20),
    borrow_date DATETIME,
    return_date DATETIME,
    status VARCHAR(20),
    FOREIGN KEY (member_id) REFERENCES members(member_id),
    FOREIGN KEY (isbn) REFERENCES books(isbn)
);

-- Users Table (Authentication)
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50),
    role VARCHAR(20),
    full_name VARCHAR(100),
    email VARCHAR(100),
    created_date DATE
);
            </pre>
        </div>
    </section>

    <!-- API Reference -->
    <section id="api" class="card">
        <h2>🚦 API Reference (RMI Methods)</h2>
        
        <h3>Book Operations</h3>
        <div class="code-block">
            <pre>boolean addBook(String isbn, String title, String author, int copies)
boolean updateBook(String isbn, String title, String author, int copies)
boolean deleteBook(String isbn)
String searchBooks(String keyword)
String getAllBooks()</pre>
        </div>

        <h3>Borrow/Return Operations</h3>
        <div class="code-block">
            <pre>boolean borrowBook(String memberId, String isbn)
boolean returnBook(String memberId, String isbn)
String getBorrowedBooks(String memberId)
String getBorrowHistory(String memberId)</pre>
        </div>

        <h3>Member Operations</h3>
        <div class="code-block">
            <pre>String registerMember(String name, String email, String phone)
boolean updateMember(String memberId, String name, String email, String phone)
boolean deleteMember(String memberId)
String getMemberDetails(String memberId)
String getAllMembers()</pre>
        </div>

        <h3>Statistics & Authentication</h3>
        <div class="code-block">
            <pre>String getSystemStats()
String getPopularBooks()
String getRecentTransactions(int limit)
boolean authenticate(String username, String password)
String getUserRole(String username)</pre>
        </div>
    </section>

    <!-- Troubleshooting -->
    <section class="card">
        <h2>🐛 Troubleshooting</h2>
        <div class="grid">
            <div class="feature-box">
                <strong>Port Already in Use (1099)</strong>
                <div class="code-block" style="margin-top: 0.5rem;">
                    <pre># Windows
netstat -ano | findstr 1099
taskkill /F /PID &lt;PID&gt;

# Linux/Mac
lsof -i :1099
kill -9 &lt;PID&gt;</pre>
                </div>
            </div>
            <div class="feature-box">
                <strong>Database Connection Error</strong>
                <ul>
                    <li>Ensure XAMPP MySQL is running</li>
                    <li>Check MySQL port (default: 3306)</li>
                    <li>Verify database credentials in Server.java</li>
                </ul>
            </div>
            <div class="feature-box">
                <strong>Class Not Found Error</strong>
                <ul>
                    <li>Ensure MySQL Connector JAR is in classpath</li>
                    <li>Verify JAR file name matches command</li>
                </ul>
            </div>
            <div class="feature-box">
                <strong>RMI Connection Refused</strong>
                <ul>
                    <li>Ensure server is running before client</li>
                    <li>Check firewall settings for port 1099</li>
                    <li>Use 'localhost' for local testing</li>
                </ul>
            </div>
        </div>
    </section>

    <!-- Performance Metrics -->
    <section class="card">
        <h2>📈 Performance Metrics</h2>
        <div class="stats-grid">
            <div class="stat-card"><div class="stat-number">&lt;100ms</div><div class="stat-label">Search Books</div></div>
            <div class="stat-card"><div class="stat-number">&lt;150ms</div><div class="stat-label">Borrow Book</div></div>
            <div class="stat-card"><div class="stat-number">&lt;150ms</div><div class="stat-label">Return Book</div></div>
            <div class="stat-card"><div class="stat-number">&lt;200ms</div><div class="stat-label">Add Member</div></div>
        </div>
        <div class="stats-grid">
            <div class="stat-card"><div class="stat-number">100+</div><div class="stat-label">Concurrent Users (Search)</div></div>
            <div class="stat-card"><div class="stat-number">50+</div><div class="stat-label">Concurrent Users (Borrow)</div></div>
        </div>
    </section>

    <!-- Future Enhancements -->
    <section class="card">
        <h2>💡 Future Enhancements</h2>
        <div class="grid">
            <div class="feature-box">
                <ul>
                    <li>Email notifications for due dates</li>
                    <li>Fine calculation for late returns</li>
                    <li>Reservation system for books</li>
                    <li>PDF report generation</li>
                </ul>
            </div>
            <div class="feature-box">
                <ul>
                    <li>Barcode scanner integration</li>
                    <li>Mobile app version</li>
                    <li>Cloud database deployment</li>
                    <li>Multi-branch library support</li>
                </ul>
            </div>
        </div>
    </section>

</div>

<!-- Footer -->
<footer class="footer">
    <p>📚 Library Management System - RMI Distributed Project</p>
    <p>Developed with ☕ Java and ❤️ for Distributed Systems</p>
    <p style="margin-top: 1rem; font-size: 0.85rem; opacity: 0.8;">Distributed Systems Course Project | Year: 2024</p>
    <p style="margin-top: 0.5rem; font-size: 0.85rem; opacity: 0.8;">
        <a href="#" style="color: #3498db; text-decoration: none;">⭐ Star this repository if you found it helpful!</a>
    </p>
</footer>

</body>
</html>
