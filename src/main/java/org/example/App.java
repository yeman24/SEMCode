
package org.example;

import java.sql.*;

public class App {

    private Connection con = null;

    // Connect to MySQL
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Connecting to database...");
                    Thread.sleep(30000);

                    con = DriverManager.getConnection(
                            "jdbc:mysql://db:3306/employees?allowPublicKeyRetrieval=true&useSSL=false",
                            "root",
                            "example"
                    );

                    System.out.println("Successfully connected");
                    break;

                } catch (SQLException e) {
                    System.out.println("Connection failed: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Disconnect from MySQL
    public void disconnect() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection");
        }
    }

    // View Employee Record - Lab 3a
    public Employee getEmployee(int ID) {

        String sql = """
            SELECT e.emp_no, e.first_name, e.last_name,
                   t.title, s.salary, d.dept_name,
                   CONCAT(m.first_name, ' ', m.last_name) AS manager
            FROM employees e
            LEFT JOIN titles t ON e.emp_no = t.emp_no
                AND t.to_date = '9999-01-01'
            LEFT JOIN salaries s ON e.emp_no = s.emp_no
                AND s.to_date = '9999-01-01'
            LEFT JOIN dept_emp de ON e.emp_no = de.emp_no
                AND de.to_date = '9999-01-01'
            LEFT JOIN departments d ON de.dept_no = d.dept_no
            LEFT JOIN dept_manager dm ON d.dept_no = dm.dept_no
                AND dm.to_date = '9999-01-01'
            LEFT JOIN employees m ON dm.emp_no = m.emp_no
            WHERE e.emp_no = ?
            """;

        if (con == null) {
            System.out.println("Database not connected");
            return null;
        }

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Employee emp = new Employee();

                    emp.emp_no = rs.getInt("emp_no");
                    emp.first_name = rs.getString("first_name");
                    emp.last_name = rs.getString("last_name");
                    emp.title = rs.getString("title");
                    emp.salary = rs.getInt("salary");
                    emp.dept_name = rs.getString("dept_name");
                    emp.manager = rs.getString("manager");

                    return emp;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Display Employee Record - Lab 3a
    public void displayEmployee(Employee emp) {

        if (emp != null) {
            System.out.println(
                    emp.emp_no + " " +
                            emp.first_name + " " +
                            emp.last_name + "\n" +
                            emp.title + "\n" +
                            "Salary: " + emp.salary + "\n" +
                            emp.dept_name + "\n" +
                            "Manager: " + emp.manager
            );
        } else {
            System.out.println("Employee not found");
        }
    }

    // Salary by Role - Lab 3b
    public void getSalariesByRole(String title) {

        String sql = """
            SELECT e.emp_no, e.first_name, e.last_name, s.salary
            FROM employees e
            JOIN salaries s ON e.emp_no = s.emp_no
            JOIN titles t ON e.emp_no = t.emp_no
            WHERE s.to_date = '9999-01-01'
              AND t.to_date = '9999-01-01'
              AND t.title = ?
            ORDER BY e.emp_no ASC
            """;

        if (con == null) {
            System.out.println("Database not connected");
            return;
        }

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, title);

            try (ResultSet rs = stmt.executeQuery()) {

                System.out.println("\nSalary Report for Role: " + title);
                System.out.println("-----------------------------------------");
                System.out.printf(
                        "%-10s %-15s %-20s %-10s%n",
                        "Emp No", "First Name", "Last Name", "Salary"
                );

                int count = 0;

                while (rs.next()) {
                    System.out.printf(
                            "%-10d %-15s %-20s %-10d%n",
                            rs.getInt("emp_no"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getInt("salary")
                    );

                    count++;
                }

                System.out.println("-----------------------------------------");
                System.out.println("Total employees: " + count);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Main Program
    public static void main(String[] args) {

        App a = new App();

        a.connect();

        // Lab 3a - View Employee
        Employee emp = a.getEmployee(255530);
        a.displayEmployee(emp);

        // Lab 3b - Salary Report by Role
        a.getSalariesByRole("Engineer");

        a.disconnect();
    }
}
