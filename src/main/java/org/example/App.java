
package org.example;

import java.sql.*;

public class App {
    private Connection con = null;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Connecting to database...");
                    Thread.sleep(30000);

                    con = DriverManager.getConnection(
                            "jdbc:mysql://db:3306/employees?allowPublicKeyRetrieval=true&useSSL=false",
                            "root", "example"
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

    public void disconnect() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection");
        }
    }

    public Employee getEmployee(int ID) {
        try {
            Statement stmt = con.createStatement();

            String query =
                    "SELECT emp_no, first_name, last_name " +
                            "FROM employees WHERE emp_no = " + ID;

            ResultSet rset = stmt.executeQuery(query);

            if (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                return emp;
            }
        } catch (Exception e) {
            System.out.println("Failed to get employee: " + e.getMessage());
        }
        return null;
    }

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
        }
    }

    public static void main(String[] args) {
        App a = new App();
        a.connect();

        Employee emp = a.getEmployee(255530);
        a.displayEmployee(emp);

        a.disconnect();
    }
}
