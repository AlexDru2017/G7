package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.Client;
import client.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import resources.Message;
import resources.Question;
import resources.Utilities;

public class TeacherWindowController implements Initializable, IScreenController {

	// region Fields

	@FXML
	private AnchorPane mainAnchorPane;

	@FXML
	private Text TextGroup7;

	@FXML
	private ImageView Group7Logo;

	@FXML
	private Button logoutButton;

	@FXML
	private Text date;

	@FXML
	private Text welcomeText;

	@FXML
	private MenuItem editOrRemoveQuestion;
	
	@FXML
	private AnchorPane questionsTableAnchorPane;

	@FXML
	private TableView<Question> tableView;

	@FXML
	private TableColumn<Question, String> questionIDColumn;

	@FXML
	private TableColumn<Question, String> authorColumn;

	@FXML
	private TableColumn<Question, String> questionTextColumn;

	@FXML
	private TableColumn<Question, String> possibleAnswersColumn;

	@FXML
	private TableColumn<Question, String> correctAnswerColumn;
	
	@FXML
	private Button saveButton;
	
	@FXML
	private AnchorPane addQuestionAnchorPane; 
	
	@FXML
	private Label subject;
	
	@FXML
	private ComboBox<String> subjectComboBox;

	@FXML
	private Label questionText;
	
	@FXML
	private TextField questionTextField;
	
	@FXML
	private Label firstAnswer;
	
	@FXML
	private TextField firstAnswerField;
	
	@FXML
	private Label secondAnswer;
	
	@FXML
	private TextField secondAnswerField;
	
	@FXML
	private Label thirdAnswer;
	
	@FXML
	private TextField thirdAnswerField;
	
	@FXML
	private Label fourthAnswer;

	@FXML
	private TextField forthAnswerField;
	
	@FXML
	private Label correcthAnswer;
	
	@FXML
	private ComboBox<String> correctAnswerComboBox;

	private ScreensController screensController;

	private Client client;

	// end region -> Fields

	// region Setters

	@Override
	public void setScreenParent(ScreensController screenParent) {
		screensController = screenParent;
	}

	// end region -> Setters

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		date.setText(Utilities.setDate());
		this.client = MainApp.getClient();
		setColumns();
		setQuestionsTableInfo();
		tableView.setEditable(true);
		addQuestionAnchorPane.setVisible(false);
	}

	// region Public Methods

	public void logOutButtonHandler(ActionEvent event) throws Exception {
		if (tableView.isVisible()) {
			tableView.getItems().clear();
			setQuestionsTableInfo();
		}
		this.client.handleMessageFromClientUI(Message.logout);
		screensController.setScreen(MainApp.loginScreenID);
	}

	/**
	 * Edit\Remove Question was pressed
	 */
	public void openEditorRemove(ActionEvent event) {
		try {
			questionsTableAnchorPane.setVisible(true);
			addQuestionAnchorPane.setVisible(false);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/*
	 * sends message to the server to update the data base
	 */
	public void saveButtonHandler(ActionEvent event) {
		ObservableList<Question> newQuestions = FXCollections.observableArrayList();
		ArrayList<Question> updateDB = new ArrayList<Question>();
		newQuestions = tableView.getItems();
		for (Question question : newQuestions) {
			int answerNumber = Integer.parseInt(question.getCorrectAnswer());
			if (answerNumber < 1 || answerNumber > 4) {
				// add error window
				System.out.println("answerNumber 1<-->4");
				return;
			}
			updateDB.add(question);
		}
		client.handleMessageFromClientUI(updateDB);
	}
	/**
	 * Add Question was pressed
	 */
	public void openAddQuestion(ActionEvent event) {
		try {
			addQuestionAnchorPane.setVisible(true);
//			tableView.setVisible(false);
//			saveButton.setVisible(false);
			questionsTableAnchorPane.setVisible(false);
			subjectComboBox.setPromptText("Select Subject");
			subjectComboBox.getItems().addAll("Software","Math");
			correctAnswerComboBox.setPromptText("Select");
			correctAnswerComboBox.getItems().addAll("1","2","3","4");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	// end region -> Public Methods

	// region Private Methods

	/**
	 * Define the columns
	 */
	private void setColumns() {
		questionIDColumn.setCellValueFactory(new PropertyValueFactory<>("questionID"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
		questionTextColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
		possibleAnswersColumn.setCellValueFactory(new PropertyValueFactory<>("possibleAnswers"));
		correctAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));

		// define the columns editable

		authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		authorColumn.setOnEditCommit(new EventHandler<CellEditEvent<Question, String>>() {
			@Override
			public void handle(CellEditEvent<Question, String> t) {
				((Question) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAuthor(t.getNewValue());
			}
		});
		questionTextColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		questionTextColumn.setOnEditCommit(new EventHandler<CellEditEvent<Question, String>>() {
			@Override
			public void handle(CellEditEvent<Question, String> t) {
				((Question) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setQuestionText(t.getNewValue());
			}
		});
		possibleAnswersColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		possibleAnswersColumn.setOnEditCommit(new EventHandler<CellEditEvent<Question, String>>() {
			@Override
			public void handle(CellEditEvent<Question, String> t) {
				((Question) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setPossibleAnswers(t.getNewValue());
			}
		});
		correctAnswerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		correctAnswerColumn.setOnEditCommit(new EventHandler<CellEditEvent<Question, String>>() {
			@Override
			public void handle(CellEditEvent<Question, String> t) {
				((Question) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setCorrectAnswer(t.getNewValue());
			}
		});
	}
	
	/*
	 * Updates the GUI questions table from the data base
	 */
	private void setQuestionsTableInfo() {
//		tableView.setVisible(false);
//		saveButton.setVisible(false);
		questionsTableAnchorPane.setVisible(false);
		client.handleMessageFromClientUI(resources.Message.EditorRemove);
		tableView.setItems(client.getQuestionsFromDB());
	}

	// end region -> Private Methods

}