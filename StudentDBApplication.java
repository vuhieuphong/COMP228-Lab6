package lab6;

import java.sql.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;

public class StudentDBApplication extends Application {
	
	//shared nodes
	private PreparedStatement preparedStatement;
	private Connection connection;
	//tab 1 nodes
	private TextField tfSIN1 = new TextField();
	private Label lblStatus1 = new Label();
	//tab 2 nodes
	private TextField tfSIN2=new TextField();
	private TextField tfFirstName2=new TextField();
	private TextField tfMI2=new TextField();
	private TextField tfLastName2=new TextField();
	private TextField tfGrade2=new TextField();
	private Label lblStatus2=new Label();
	//tab 3 nodes
	private TextField tfSIN3=new TextField();
	private Label lblStatus3=new Label();
	//tab 4 nodes
	private TextField tfSIN4=new TextField();
	private TextField tfGrade4=new TextField();
	private Label lblStatus4=new Label();

	@Override
	public void start(Stage primaryStage) {
		//connect to database
		initializeDB();		
		
		//Adding Tab Pane with Tabs
		TabPane tabPane=new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		tabPane.setStyle("-fx-background-color: linear-gradient(from 30% 30% to 100% 100%,#F0FFFF ,white)");
		Tab showGradeTab=new Tab("SHOW GRADE");
		Tab addStudentTab=new Tab("ADD STUDENT");
		Tab delStudentTab=new Tab("DELETE STUDENT");	
		Tab editGradeTab=new Tab("EDIT GRADE");
		tabPane.getTabs().addAll(showGradeTab,addStudentTab,delStudentTab,editGradeTab);
		
		//Show Grade Tab 
		GridPane showGradePane=new GridPane();
		showGradePane.setHgap(5);
		showGradePane.setVgap(10);		
		showGradePane.setAlignment(Pos.CENTER);
		showGradeTab.setContent(showGradePane);			
		showGradePane.add(new Label("Enter SIN:"),0,0);
		showGradePane.add(tfSIN1,1,0);
		Button btShowGrade=new Button("Show Grade");
		showGradePane.add(btShowGrade, 1, 1);
		btShowGrade.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #36D1DC, #5B86E5);-fx-text-fill:white");
		btShowGrade.setOnAction(e->showGrade());
		showGradePane.add(lblStatus1, 1, 2);
		
		//Add Student Tab
		GridPane addStudentPane=new GridPane();
		addStudentPane.setHgap(5);
		addStudentPane.setVgap(10);
		addStudentPane.setAlignment(Pos.CENTER);
		addStudentTab.setContent(addStudentPane);			
		addStudentPane.add(new Label("Enter SIN:"),0,0);
		addStudentPane.add(new Label("First Name:"),0,1);
		addStudentPane.add(new Label("Enter MI:"),0,2);
		addStudentPane.add(new Label("Last Name:"),0,3);
		addStudentPane.add(new Label("Enter Grade:"),0,4);		
		addStudentPane.add(tfSIN2,1,0);
		addStudentPane.add(tfFirstName2, 1, 1);
		addStudentPane.add(tfMI2, 1,2);
		addStudentPane.add(tfLastName2, 1, 3);
		addStudentPane.add(tfGrade2, 1, 4);
		Button btAddStudent=new Button("Add Student");
		addStudentPane.add(btAddStudent, 1, 5);
		btAddStudent.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #36D1DC, #5B86E5);-fx-text-fill:white");
		btAddStudent.setOnAction(e->addStudent());
		addStudentPane.add(lblStatus2, 1, 6);
		
		//Delete Student Tab
		GridPane delStudentPane=new GridPane();
		delStudentPane.setHgap(5);
		delStudentPane.setVgap(10);
		delStudentPane.setAlignment(Pos.CENTER);
		delStudentTab.setContent(delStudentPane);
		delStudentPane.add(new Label("Enter SIN:"), 0, 0);
		delStudentPane.add(tfSIN3, 1, 0);
		Button btDelStudent=new Button("Delete Student");
		delStudentPane.add(btDelStudent, 1, 1);
		btDelStudent.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #36D1DC, #5B86E5);-fx-text-fill:white");
		btDelStudent.setOnAction(e->delStudent());
		delStudentPane.add(lblStatus3, 1, 2);
		
		//Edit Student Tab
		GridPane editGradePane=new GridPane();
		editGradePane.setHgap(5);
		editGradePane.setVgap(10);
		editGradePane.setAlignment(Pos.CENTER);
		editGradeTab.setContent(editGradePane);
		editGradePane.add(new Label("Enter SIN:"), 0, 0);
		editGradePane.add(tfSIN4, 1, 0);
		editGradePane.add(new Label("New Grade:"),0,1);
		editGradePane.add(tfGrade4, 1, 1);
		Button btEditGrade=new Button("Edit Grade");
		editGradePane.add(btEditGrade, 1, 2);
		btEditGrade.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #36D1DC, #5B86E5);-fx-text-fill:white");
		btEditGrade.setOnAction(e->editGrade());
		editGradePane.add(lblStatus4, 1, 3);
		
		
		Scene scene=new Scene(tabPane,480,480);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Student Database");
		primaryStage.show();
	}
	
	private void initializeDB()
	{
		try
		{
			//load jdbc driver
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver loaded.");
			//establish a connection
			try {
				connection = DriverManager.getConnection("jdbc:oracle:thin:@oracle1.centennialcollege.ca:1521:SQLD","COMP228_F19_sy_28", "password");
			} catch (Exception e) {
				connection = DriverManager.getConnection("jdbc:oracle:thin:@199.212.26.208:1521:SQLD","COMP228_F19_sy_28", "password");
			}			
			System.out.println("Database connected.");	
		}
		catch(Exception ex)
		{
			
			ex.printStackTrace();			
		}				
	}
	
	private void showGrade()
	{
		String queryString = "select first_name, mi, last_name, grade from students where sin = ?";
		String sin = tfSIN1.getText();
		//create a statement
		try {
		preparedStatement = connection.prepareStatement(queryString);
		preparedStatement.setString(1, sin);
		ResultSet rset = preparedStatement.executeQuery();
		if(rset.next())
		{
			String lastName = rset.getNString(1);
			String mi = rset.getNString(2);
			String firstName = rset.getNString(3);
			String grade = rset.getNString(4);			
			//display result in a label
			lblStatus1.setText(firstName + " " + mi + " " + lastName + "'s grade is " + grade);
		}
		else {
			lblStatus1.setText("Not Found!");			
		}	
		}catch(Exception ex){
			
			lblStatus1.setText("Not Found!");						
		}				
	}
	
	private void addStudent()
	{
		String queryString = "insert into students values(?,?,?,?,?)";	
		String sin = tfSIN2.getText();
		String firstName=tfFirstName2.getText();
		String mi=tfMI2.getText();
		String lastName=tfLastName2.getText();
		String grade=tfGrade2.getText();

		try {
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setString(1,sin);
			preparedStatement.setString(2,firstName);
			preparedStatement.setString(3,mi);
			preparedStatement.setString(4,lastName);
			preparedStatement.setString(5, grade);
			int rset=preparedStatement.executeUpdate();
			if(rset==1)
			{
			lblStatus2.setText("New Student Added!");
			}
			else {
				lblStatus2.setText("New Student Added!");	
			}
			}catch(Exception ex){
				lblStatus2.setText("Add Student Failed!");			
			}			
	}
	
	private void delStudent()
	{
		String queryString="delete from students where sin=?";
		String sin=tfSIN3.getText();
		
		try {
			preparedStatement=connection.prepareStatement(queryString);
			preparedStatement.setString(1, sin);
			int rset=preparedStatement.executeUpdate();
			if(rset==1)
			{
				lblStatus3.setText("Deleted a Student!");	
			}
			else {
				lblStatus3.setText("Delete Student Failed!");
			}
		} catch (Exception e) {
			lblStatus3.setText("Delete Student Failed!");
		}
	}
	
	private void editGrade()
	{
		String queryString="update students set grade=? where sin=?";
		String sin=tfSIN4.getText();
		String grade=tfGrade4.getText();
		try {
			preparedStatement=connection.prepareStatement(queryString);
			preparedStatement.setString(1, grade);
			preparedStatement.setString(2, sin);
			int rset=preparedStatement.executeUpdate();
			if(rset==1)
			{
				lblStatus4.setText("Grade Updated!");
			}
			else {
				lblStatus4.setText("Edit Grade Failed!");
			}
			
		} catch (SQLException e) {
			lblStatus4.setText("Edit Grade Failed!");
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
