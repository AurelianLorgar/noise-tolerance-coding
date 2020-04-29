package sample;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

class Decoding {

    private Alerts alerts = new Alerts();

    private String decodeResult = "";

    String getDecodeResult() {
        return decodeResult;
    }

    void decoding(ArrayList<Byte> code) {

        int counterCode = 0;
        int missCounter = 0;

        ArrayList<Integer> zerosList = new ArrayList<>();
        ArrayList<String> resultString = new ArrayList<>();
        ArrayList<Character> resultChar = new ArrayList<>();
        char[] charOriginalText = new char[resultChar.size()];

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("values.dat"))) {
            Values values = (Values) ois.readObject();
            resultString = values.getResultString();
            resultChar = values.getResultChar();
            charOriginalText = values.getCharOriginalText();
        } catch (Exception ex) {

            alerts.alertFiles();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("zeros.dat"))) {
            Zeros zeros = (Zeros) ois.readObject();
            zerosList = zeros.getZeros();
        } catch (Exception ex) {

            alerts.alertFiles();
        }

        String[] decodeString = resultString.toArray(new String[0]);
        Character[] decodeChar = resultChar.toArray(new Character[0]);
        char[] decodeResultChar = new char[decodeString.length];

        int q = decodeString.length - zerosList.size();
        int zLS = zerosList.size();
        for (int i = zLS; i <= zLS + q; i++) {
            zerosList.add(0);
        }

        int[] newZerosList = new int[zerosList.size()];

        for (int i = 0; i < decodeString.length; i++) {
            String dS = decodeString[i].substring(1);
            newZerosList[i] = 4 - dS.length();
        }

        for (int i = 0; i < decodeString.length; i++) {

            int failCounter = 0;
            String codeString = "";
            for (int j = 0; j < 4; j++) {
                codeString = codeString.concat(String.valueOf(code.get(counterCode)));
                counterCode++;
            }
            counterCode += 3;

            String decode = decodeString[i].substring(1);

            for (int j = 0; j < 4; j++) {
                if (codeString.substring(codeString.length() - 1, codeString.length()).equals("0")) {
                    codeString = codeString.substring(0, 4 - j);
                    if (codeString.length() == (4 - newZerosList[i])) {
                        break;
                    }
                }
            }

            for (int j = 0; j < codeString.length(); j++) {

                if (decode.equals(codeString)) {
                    decodeResultChar[i] = decodeChar[i];
                    break;
                } else {
                    failCounter++;
                    if (failCounter == codeString.length()) {
                        missCounter++;
                    }
                }
            }
        }

        for (char aCharOriginalText : charOriginalText) {
            for (char aDecodeResultChar : decodeResultChar) {
                if (aCharOriginalText == aDecodeResultChar) {
                    decodeResult = decodeResult.concat(String.valueOf(aCharOriginalText));
                }
            }
        }

        for (int i = 0; i < missCounter; i++) {
            decodeResult = decodeResult.concat("*");
        }
    }
}