package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ControllerH {

    private ErrorCorrection errorCorrection = new ErrorCorrection();
    private Decoding decoding = new Decoding();
    private Alerts alerts = new Alerts();

    private ArrayList<Byte> extendedCodeConstructs = new ArrayList<>();
    private ArrayList<Byte> codeConstructs = new ArrayList<>();
    private ArrayList<Byte> parityCode;
    private ArrayList<Byte> syndromeCode;

    @FXML
    private TextArea textAreaCodeConstructs;
    @FXML
    private TextArea textAreaXor;
    @FXML
    private TextArea textAreaS;
    @FXML
    private TextArea textAreaSyndrome;
    @FXML
    private TextArea textAreaFixed;
    @FXML
    private TextArea textAreaErrorMessage;
    @FXML
    private TextArea textAreaDecode;

    @FXML
    private void getCodes() {

        ArrayList<Byte> oldCodeConstructs = new ArrayList<>();
        ArrayList<Byte> checkBit = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("values.dat"))) {
            Values values = (Values) ois.readObject();
            oldCodeConstructs = values.getCodeConstructs();
            checkBit = values.getCheckBit();
        } catch (Exception e) {

            alerts.alertFiles();
        }

        addInformation(oldCodeConstructs, textAreaCodeConstructs, 7);
        addExtInformation(checkBit, textAreaXor);

    }

    @FXML
    private void getSyndrome() {

        findCodeConstructs();
        errorCorrection.calculateParityBit(extendedCodeConstructs);
        errorCorrection.calculateSyndrome(codeConstructs);

        parityCode = errorCorrection.getParityCode();
        syndromeCode = errorCorrection.getSyndromeCode();
        addExtInformation(parityCode, textAreaS);
        addInformation(syndromeCode, textAreaSyndrome, 4);

    }

    @FXML
    private void correctErrors() {

        try {
            errorCorrection.errorCorrection(codeConstructs, parityCode, syndromeCode);
            decoding.decoding(errorCorrection.getFixedCodeList());

            ArrayList<String> errorMessage = errorCorrection.getErrorMessage();
            ArrayList<Byte> fixedCodeList = errorCorrection.getFixedCodeList();
            String decodeResult = decoding.getDecodeResult();

            addInformation(fixedCodeList, textAreaFixed, 7);
            addInformation(errorMessage, textAreaErrorMessage);
            textAreaDecode.setText(decodeResult);
        } catch (Exception e) {
            alerts.alertCode();
        }

    }

    @FXML
    private void findCodeConstructs() {

        int counterExt = 0;
        String codeConstruct = textAreaCodeConstructs.getText();
        String checkBit = textAreaXor.getText();
        codeConstruct = codeConstruct.replaceAll(" |\\n|\\r\\n", "");
        checkBit = checkBit.replaceAll(" |\\n|\\r\\n", "");

        char[] codeConstructChar = codeConstruct.toCharArray();
        char[] checkBitChar = checkBit.toCharArray();
        char[] extendedChar = new char[codeConstructChar.length + checkBitChar.length];

        int j = 0;
        int k = 0;
        for (int i = 0; i < extendedChar.length; i++) {
            if (counterExt < 7) {
                extendedChar[i] = codeConstructChar[j];
                counterExt++;
                j++;
            } else {
                if (k < checkBitChar.length) {
                    extendedChar[i] = checkBitChar[k];
                    counterExt = 0;
                    k++;
                }
            }
        }

        for (char aCodeConstructChar : codeConstructChar) {
            if (aCodeConstructChar == '0') {
                codeConstructs.add((byte) 0);
            } else {
                codeConstructs.add((byte) 1);
            }
        }

        for (char anExtendedChar : extendedChar) {
            if (anExtendedChar == ('0')) {
                extendedCodeConstructs.add((byte) 0);
            } else {
                extendedCodeConstructs.add((byte) 1);
            }
        }
    }

    @FXML
    private void addInformation(ArrayList<String> stringList, TextArea textArea) {
        String str = "";

        for (String aStringList : stringList) {
            str = str.concat(aStringList);
        }
        textArea.setText(str);
    }

    @FXML
    private void addInformation(ArrayList<Byte> byteList, TextArea textArea, int divider) {
        String str = "";
        int i = 0;

        for (Byte aByteList : byteList) {
            str = str.concat(String.valueOf(aByteList) + " ");
            i++;
            if (i % divider == 0) {
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
