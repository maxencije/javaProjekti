module com.example.zadatak_poruke {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.zadatak_poruke to javafx.fxml;
    exports com.example.zadatak_poruke;
}