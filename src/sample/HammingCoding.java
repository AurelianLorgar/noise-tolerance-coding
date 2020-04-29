package sample;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

class HammingCoding {

    private Alerts alerts = new Alerts();

    private ArrayList<Byte> codeConstructs = new ArrayList<>();
    private ArrayList<Byte> checkBit = new ArrayList<>();
    private ArrayList<Integer> zerosList = new ArrayList<>();
    private byte[][] matrixH = {{0, 1, 1, 1, 1, 0, 0}, {1, 1, 1, 0, 0, 1, 0}, {1, 0, 1, 1, 0, 0, 1}};
    private byte[][] matrixG = new byte[4][7];
    private int counter = 0;
    private int counterCode = 0;
    private int counterCheck = 0;

    ArrayList<Byte> getCodeConstructs() {
        return codeConstructs;
    }

    ArrayList<Byte> getCheckBit() {
        return checkBit;
    }

    byte[][] getMatrixG() {
        return matrixG;
    }

    byte[][] getMatrixH() {
        return matrixH;
    }

    void calculateNumbers(ArrayList<String> result) {

        matrixCalculate();

        String resultString = "";
        ArrayList<Byte> byteList = new ArrayList<>();

        for (String aResult : result) {
            resultString = resultString.concat(aResult);
        }

        if (resultString.length() > 0) {
            resultString = resultString.substring(1);
        }

        ArrayList<String> resultList = new ArrayList<>(Arrays.asList(resultString.split(" ")));

        for (int i = 0; i < resultList.size(); i++) {
            if (resultList.get(i).length() < 4) {

                String nResult = resultList.get(i);
                int zeros = 4 - resultList.get(i).length();
                zerosList.add(zeros);
                for (int j = 0; j < zeros; j++) {
                    nResult = nResult.concat("0");
                }
                resultList.remove(i);
                resultList.add(i, nResult);
            }
        }

        for (String aResultList : resultList) {
            char[] charArr = aResultList.toCharArray();

            for (char aCharArr : charArr) {

                if (aCharArr == '1') {
                    byteList.add((byte) 1);
                } else byteList.add((byte) 0);
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("zeros.dat"))) {

            Zeros zeros = new Zeros(zerosList);
            oos.writeObject(zeros);
        } catch (Exception ex) {

            alerts.alertFiles();
        }

        calculateHammingCode(byteList);
    }

    private void calculateHammingCode(ArrayList<Byte> byteList) {

        byte[] byteArray = new byte[4];
        byte[] codeArray = new byte[7];
        byte temp;

        try {
            for (int i = 0; i < 4; i++) {
                byteArray[i] = byteList.get(counter);
                counter++;
            }
        } catch (Exception e) {
            return;
        }

        System.arraycopy(byteArray, 0, codeArray, 0, 4);

        for (int j = 4; j < 7; j++) {
            temp = (byte) (byteArray[0] & matrixG[0][j] ^ byteArray[1] & matrixG[1][j] ^ byteArray[2] & matrixG[2][j]
                    ^ byteArray[3] & matrixG[3][j]);
            codeArray[j] = temp;
        }

        for (byte aCodeArray : codeArray) {
            codeConstructs.add(counterCode, aCodeArray);
            counterCode++;
        }

        temp = codeArray[0];
        for (int i = 0; i < codeArray.length - 1; i++) {
            temp = (byte) (temp ^ codeArray[i + 1]);
        }
        checkBit.add(counterCheck, temp);

        counterCheck++;
        calculateHammingCode(byteList);
    }

    private void matrixCalculate() {

        byte[][] matrixUnit = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
        byte[][] matrixMiddle = new byte[3][4];
        byte[][] matrixMiddleT = new byte[4][3];

        for (int i = 0; i < 3; i++) {
            System.arraycopy(matrixH[i], 0, matrixMiddle[i], 0, 4);
        }

        for (int i = 0; i < matrixMiddle.length; i++) {
            for (int j = 0; j < matrixMiddle[i].length; j++) {
                matrixMiddleT[j][i] = matrixMiddle[i][j];
            }
        }

        for (int i = 0; i < 4; i++) {
            System.arraycopy(matrixUnit[i], 0, matrixG[i], 0, 4);
        }

        for (int i = 0; i < 4; i++) {
            System.arraycopy(matrixMiddleT[i], 0, matrixG[i], 4, 3);
        }
    }
}

class Zeros implements Serializable {

    private ArrayList<Integer> zeros;

    Zeros(ArrayList<Integer> z) {

        zeros = z;
    }

    ArrayList<Integer> getZeros() {
        return zeros;
    }
}
