package sample;

import javafx.scene.control.Alert;

class Alerts {

    void alertCode() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Неизвестная ошибка. Убедитесь, что вы выполнили все предыдущие действия, или же попробуйте ввести текст для кодирования заново");
        alert.showAndWait();
    }

    void alertFiles() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Убедитесь, что в папке с программой есть файлы values.dat и zeros.dat");
        alert.showAndWait();
    }
}
