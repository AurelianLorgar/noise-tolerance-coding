package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ControllerSF {

    private ShennonFano shennonFano = new ShennonFano();
    private HammingCoding hammingCoding = new HammingCoding();
    private ArrayList<String> resultString;
    private ArrayList<Character> resultChar;
    private char[] charOriginalText;
    private ArrayList<Byte> codeConstructs;
    private ArrayList<Integer> hammingValues = new ArrayList<>();
    private String str = "";

    private int counterValue = 0;
    private int counter = 0;

    @FXML
    private TextField textFieldOriginalText;
    @FXML
    private TextArea textAreaCode;
    @FXML
    private TextArea textAreaGeneratingMatrix;
    @FXML
    private TextArea textAreaCheckMatrix;
    @FXML
    private TextArea textAreaCodeConstructs;
    @FXML
    private TextArea textAreaXor;
    @FXML
    private Text textHammingValue;

    @FXML
    private void calculate() {

        charOriginalText = textFieldOriginalText.getText().toCharArray();

        if (charOriginalText.length != 0) {
            shennonFano.shennonFano(charOriginalText, ' ', "", 0, charOriginalText.length);

            resultString = shennonFano.getResultString();
            resultChar = shennonFano.getResultChar();

            for (char aCharOriginalText : charOriginalText) {
                for (int j = 0; j < resultChar.size(); j++) {
                    if (aCharOriginalText == resultChar.get(j)) {
                        str = str.concat(resultString.get(j));
                    }
                }
            }
            textAreaCode.setText(str);

            try {
                hammingCoding();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void hammingCoding() {

        hammingCoding.calculateNumbers(resultString);

        byte[][] matrixG = hammingCoding.getMatrixG();
        byte[][] matrixH = hammingCoding.getMatrixH();
        codeConstructs = hammingCoding.getCodeConstructs();
        ArrayList<Byte> checkBit = hammingCoding.getCheckBit();

        addInformation(matrixG, textAreaGeneratingMatrix);
        addInformation(matrixH, textAreaCheckMatrix);
        addInformation(codeConstructs, textAreaCodeConstructs);
        addExtInformation(checkBit, textAreaXor);

        hammingValue();

        if (hammingValues.size() != 0) {
            textHammingValue.setText(String.valueOf(hammingValues.get(0)));
        } else {
            textHammingValue.setText(String.valueOf(0));
        }

    }

    @FXML
    private void hammingValue() {

        if (counter < (codeConstructs.size() / 7) - 1) {

            Byte[] construct = codeConstructs.toArray(new Byte[0]);
            Byte[][] construct2 = new Byte[2][4];
            Byte[] value = new Byte[4];

            int temp;
            int temp2 = 0;

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 4; j++) {
                    construct2[i][j] = construct[counterValue];
                    counterValue++;
                }
                counterValue += 3;
                if (counterValue >= codeConstructs.size()) {
                    break;
                }
            }

            for (int j = 0; j < 1; j++) {
                for (int i = 0; i < 4; i++) {
                    value[i] = (byte) (construct2[j][i] ^ construct2[j + 1][i]);
                }
            }

            for (int i = 0; i < (value.length) - 1; i = i + 2) {
                temp = value[i] + value[i + 1];
                temp2 += temp;
            }

            hammingValues.add(temp2);

            counter++;
            counterValue -= 7;
            hammingValue();
        }
    }

    @FXML
    private void sendCodes() throws Exception {

        if (str.length() != 0) {

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("values.dat"))) {

                Values values = new Values(hammingCoding.getCodeConstructs(), hammingCoding.getCheckBit(), resultString,
                        resultChar, charOriginalText);
                oos.writeObject(values);
            } catch (Exception ex) {

                System.out.println(ex.getMessage());
            }

            Stage secondStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Hamming.fxml"));
            secondStage.setTitle("Помехоустойчивое кодирование");
            secondStage.setScene(new Scene(root, 850, 400));
            secondStage.setResizable(false);
            secondStage.show();
        }
    }

    @FXML
    private void clearAll() {

        str = "";
        textFieldOriginalText.clear();
        textAreaCode.clear();
        textAreaGeneratingMatrix.clear();
        textAreaCheckMatrix.clear();
        textAreaCodeConstructs.clear();
        textAreaXor.clear();
        textHammingValue.setText(String.valueOf(0));
    }

    @FXML
    private void addInformation(byte[][] matrix, TextArea textArea) {
        String str = "";

        for (byte[] aMatrix : matrix) {
            for (byte anAMatrix : aMatrix) {
                str = str.concat(String.valueOf(anAMatrix) + " ");
            }
            str = str.concat("\r\n");
        }
        textArea.setText(str);
    }

    @FXML
    private void addInformation(ArrayList<Byte> byteList, TextArea textArea) {
        String str = "";
        int i = 0;

        for (Byte aByteList : byteList) {
            str = str.concat(String.valueOf(aByteList) + " ");
            i++;
            if (i % 7 == 0) {
                str = str.concat("\r\n");
            }
        }
        textArea.setText(str);
    }

    @FXML
    private void addExtInformation(ArrayList<Byte> byteList, TextArea textArea) {
        String str = "";

        for (Byte aByteList : byteList) {
            str = str.concat(String.valueOf(aByteList) + " ");
            str = str.concat("\r\n");
        }
        textArea.setText(str);
    }
}

class Values implements Serializable {

    private ArrayList<String> resultString;
    private ArrayList<Character> resultChar;
    private ArrayList<Byte> codeConstructs;
    private ArrayList<Byte> checkBit;
    private char[] charOriginalText;

    Values(ArrayList<Byte> cc, ArrayList<Byte> cb, ArrayList<String> rs, ArrayList<Character> rc, char[] cOT) {

        resultString = rs;
        resultChar = rc;
        codeConstructs = cc;
        checkBit = cb;
        charOriginalText = cOT;
    }

    char[] getCharOriginalText() {
        return charOriginalText;
    }

    ArrayList<String> getResultString() {
        return resultString;
    }

    ArrayList<Character> getResultChar() {
        return resultChar;
    }

    ArrayList<Byte> getCodeConstructs() {
        return codeConstructs;
    }

    ArrayList<Byte> getCheckBit() {
        return checkBit;
    }
}