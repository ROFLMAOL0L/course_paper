import com.example.course_paper.Field;

module com.example.course_paper {
    requires javafx.controls;
    requires javafx.fxml;
    uses Field;


    opens com.example.course_paper to javafx.fxml;
    exports com.example.course_paper;
}