import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface LibraryInterface extends Remote {
    // Auth & Role
    boolean loginUser(String u, String p) throws RemoteException;
    String getUserRole(String u) throws RemoteException;
    void logoutUser(String u) throws RemoteException;
    boolean registerUser(String u, String p, String n, String role) throws RemoteException;
    Map<String, String> getActiveUsers() throws RemoteException;

    // Admin Operations
    boolean manageUser(String u, String p, String n, String r, String action) throws RemoteException;
    String manageUsers() throws RemoteException;
    String manageBooks() throws RemoteException;
    String viewReports() throws RemoteException;
    String getSystemReports() throws RemoteException;
    boolean backupDatabase() throws RemoteException;
    List<String> getActiveNodes() throws RemoteException;
    boolean setSystemSettings(int limit, double rate) throws RemoteException;

    // Librarian Operations
    boolean addBook(String t, String a, String i, int q, String c) throws RemoteException;
    boolean updateBook(String i, String t, String a, String c) throws RemoteException;
    boolean removeBook(String i) throws RemoteException;
    boolean issueBookToMember(String u, String i) throws RemoteException;
    boolean receiveReturn(String u, String i) throws RemoteException;
    String viewBorrowingRecords() throws RemoteException;

    // Member Operations
    List<Map<String, Object>> getAllBooks() throws RemoteException;
    List<Map<String, Object>> searchBooks(String k) throws RemoteException;
    boolean borrowBook(String u, String i) throws RemoteException;
    boolean returnBook(String u, String i) throws RemoteException;
    List<Map<String, Object>> getBorrowedHistory(String u) throws RemoteException;
    List<Map<String, Object>> getBorrowedBooks(String u) throws RemoteException;
    boolean checkAvailability(String i) throws RemoteException;
    boolean reserveBook(String u, String i) throws RemoteException;
    Map<String, String> getProfile(String u) throws RemoteException;
    boolean syncData() throws RemoteException;
}