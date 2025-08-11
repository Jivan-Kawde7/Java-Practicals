import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DatabaseGUI {
    private JFrame frame;
    private JTextField idField;
    private JTextField nameField;
    private JButton insertButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;

    public DatabaseGUI() {
        // Create GUI components
        frame = new JFrame("Database GUI");
        idField = new JTextField(10);
        nameField = new JTextField(20);
        insertButton = new JButton("Insert");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        table = new JTable(tableModel);

        // Connect to database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql:                                                      
            displayData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "//localhost:3306/mydatabase", "username", "password");
            displayData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error connecting to database: " + e.getMessage());
        }

                               
        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertRecord();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateRecord();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteRecord();
            }
        });

                                
        JPanel panel = new JPanel();
        panel.add(new JLabel("// Add action listeners
        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertRecord();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateRecord();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteRecord();
            }
        });

        // Layout GUI components
        JPanel panel = new JPanel();
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(insertButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void displayData() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mytable");
            tableModel.setRowCount(0);
            while (resultSet.next()) {
                tableModel.addRow(new Object[]{resultSet.getInt("id"), resultSet.getString("name")});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error displaying data: " + e.getMessage());
        }
    }

    private void insertRecord() {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO mytable (id, name) VALUES (?, ?)");
            statement.setInt(1, Integer.parseInt(idField.getText()));
            statement.setString(2, nameField.getText());
            statement.executeUpdate();
            displayData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error inserting record: " + e.getMessage());
        }
    }

    private void updateRecord() {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE mytable SET name = ? WHERE id = ?");
            statement.setString(1, nameField.getText());
            statement.setInt(2, Integer.parseInt(idField.getText()));
            statement.executeUpdate();
            displayData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error updating record: " + e.getMessage());
        }
    }

    private void deleteRecord() {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM mytable WHERE id = ?");
            statement.setInt(1, Integer.parseInt(idField.getText()));
            statement.executeUpdate();
            displayData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error deleting record: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new DatabaseGUI();
    }
}