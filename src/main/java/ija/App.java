package ija;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App extends Application {

    private TextArea outputTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Java App Builder");

        // Text area to display output
        outputTextArea = new TextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setWrapText(true);

        // Button to build and execute Java app
        Button buildButton = new Button("Build and Execute Java App");
        buildButton.setOnAction(e -> buildAndExecute());

        VBox root = new VBox(10);
        root.getChildren().addAll(buildButton, outputTextArea);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void buildAndExecute() {
        // Sample Java app code
        String javaCode = """
                public class HelloWorld {
                    public static void main(String[] args) {
                        System.out.println("Hello, World!");
                    }
                }""";

        // Display Java app code in the output text area
        outputTextArea.appendText("Java Code:\n" + javaCode + "\n\n");

        // Compile and execute Java app
        try {
            // Compile Java app
            Process compileProcess = Runtime.getRuntime().exec("javac -d . HelloWorld.java");
            compileProcess.waitFor();

            // Execute Java app
            Process execProcess = Runtime.getRuntime().exec("java HelloWorld");
            BufferedReader reader = new BufferedReader(new InputStreamReader(execProcess.getInputStream()));

            // Read and display output
            String line;
            while ((line = reader.readLine()) != null) {
                outputTextArea.appendText(line + "\n");
            }

            // Close reader
            reader.close();
        } catch (IOException | InterruptedException ex) {
            outputTextArea.appendText("Error: " + ex.getMessage() + "\n");
        }
    }
}
