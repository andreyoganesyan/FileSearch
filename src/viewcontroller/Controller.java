package viewcontroller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.List;
import java.util.concurrent.*;

import model.*;

import static java.lang.Thread.yield;

public class Controller {
    private ObservableList<SearchResult> searchResults;
    private File chosenFile;
    @FXML
    TableView<SearchResult> searchResultsTableView;
    @FXML
    TableColumn<SearchResult, File> fileColumn;
    @FXML
    TableColumn<SearchResult, Integer> lineColumn;
    @FXML
    TableColumn<SearchResult, Integer> posColumn;
    @FXML
    TextField maskTextField;
    @FXML
    TextField searchQueryTextField;
    @FXML
    TextField fileNameTextField;

    @FXML
    private void initialize(){
        fileColumn.setCellValueFactory(new PropertyValueFactory<SearchResult, File>("file"));
        lineColumn.setCellValueFactory(new PropertyValueFactory<SearchResult, Integer>("line"));
        posColumn.setCellValueFactory(new PropertyValueFactory<SearchResult, Integer>("pos"));
    }

    public void chooseDir() {
        fileNameTextField.setText("");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        chosenFile = directoryChooser.showDialog(Main.primaryStage);
        if (chosenFile != null) fileNameTextField.setText(chosenFile.getAbsolutePath());
    }

    public void startSearch() {
        if(chosenFile==null) return;
        ExecutorService cashedThreadPool = Executors.newCachedThreadPool();
        Searcher searcher = new Searcher(chosenFile,searchQueryTextField.getText(), maskTextField.getText());
        Future<List<SearchResult>> futureSearchResult = cashedThreadPool.submit(searcher);

        while(!futureSearchResult.isDone()) yield();
        cashedThreadPool.shutdown();
        try {
            searchResults = FXCollections.observableArrayList(futureSearchResult.get());
            searchResultsTableView.setItems(searchResults);
        } catch (ExecutionException e){
            System.err.println(e);
            throw new RuntimeException(e);
        } catch (InterruptedException e){
            System.err.println(e);
            throw new RuntimeException(e);
        }
    }
}
