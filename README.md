📚 Library Management System - RMI Distributed Project
<div align="center">
https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white
https://img.shields.io/badge/RMI-007396?style=for-the-badge&logo=java&logoColor=white
https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white
https://img.shields.io/badge/Swing-5382a1?style=for-the-badge&logo=java&logoColor=white

</div>
🎯 Project Overview
A complete distributed Library Management System built using Java RMI (Remote Method Invocation) architecture. The system demonstrates a three-tier distributed architecture with separate components for client, server, and database.

Key Features
✅ Complete Book Management (Add, Update, Delete, Search)

✅ Member Registration & Management

✅ Borrow & Return System with Transaction Tracking

✅ Advanced Search (by Title, Author, ISBN)

✅ Real-time Statistics Dashboard

✅ Secure Authentication (Admin & Member Roles)

✅ Professional Colorful GUI using Java Swing

✅ Console Client Alternative

✅ MySQL Database Integration

✅ Transaction History for each member

✅ Popular Books Reports

🏗️ System Architecture
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
   📁 Project Structure
   LibrarySystem/
│
├── Server.java              # RMI Server Implementation
├── LibraryGUI.java          # Colorful GUI Client
├── Client.java              # Console Client (Alternative)
│
├── mysql-connector-j-8.0.33.jar  # MySQL JDBC Driver
│
└── README.md                # Project Documentation
🚀 Installation & Setup
Prerequisites
Java JDK 11 or higher (Download)

XAMPP (for MySQL database) (Download)

MySQL Connector/J (Download)

Step-by-Step Setup
1. Install and Start MySQL
# Start XAMPP Control Panel
# Click "Start" button next to MySQL
# Ensure MySQL is running on port 3306
2. Database Setup
The database tables will be created automatically when you run the server for the first time. The following tables will be created:
-- books: Store book information
-- members: Store member details  
-- transactions: Track borrow/return records
-- users: Store login credentials (Admin/Member)
   3. Download MySQL Connector
   # Download from: https://dev.mysql.com/downloads/connector/j/
# Save mysql-connector-j-8.0.33.jar in the project folder
🎮 Features in Detail
1. Book Management Tab
View all books in a formatted table

Add new books with ISBN, Title, Author, Copies

Update existing book information

Delete books from the system

Real-time availability status

2. Search Tab
Search by Title, Author, or ISBN

Instant results display

Clear search functionality

3. Borrow/Return Tab
Select member from dropdown

Borrow books (checks availability)

Return books with automatic availability update

View currently borrowed books for selected member

Real-time stock updates

4. Members Tab
View all registered members

Register new members

Update member information

Delete member records

Automatic member ID generation

5. Statistics Tab
System-wide statistics (Book counts, Member counts, Active borrows)

Most popular books report

Recent transactions history

Real-time data refresh

🛠️ Technologies Used
Category	Technology	Purpose
Language	Java 11+	Core development
RMI	Java RMI	Remote communication
Database	MySQL	Data persistence
GUI	Java Swing	User interface
JDBC	MySQL Connector/J	Database connectivity
Build	Command line	Compilation & execution
     SIMPLE STEP FOR YOU
