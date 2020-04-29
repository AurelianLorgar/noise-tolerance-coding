package sample;

import java.util.ArrayList;
import java.util.Arrays;

class ErrorCorrection {

    private byte[][] matrixHT = {{0, 1, 1}, {1, 1, 0}, {1, 1, 1}, {1, 0, 1}, {1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    private ArrayList<String> errorMessage = new ArrayList<>();
    private ArrayList<Byte> parityCode = new ArrayList<>();
    private ArrayList<Byte> syndromeCode = new ArrayList<>();
    private ArrayList<Byte> fixedCodeList = new ArrayList<>();
    private int counterParityBit = 0;
    private int counterCycleBit = 0;
    private int counterArrayList = 0;
    private int counterSyndrome = 0;
    private int counterErrorSyndrome = 0;
    private int counterErrorBit = 0;
    private int counterCode = 0;
    private int counterCodeArray = 0;

    ArrayList<Byte> getParityCode() {
        return parityCode;
    }

    ArrayList<Byte> getSyndromeCode() {
        return syndromeCode;
    }

    ArrayList<String> getErrorMessage() {
        return errorMessage;
    }

    ArrayList<Byte> getFixedCodeList() {
        return fixedCodeList;
    }

    void calculateParityBit(ArrayList<Byte> code) {

        if (counterParityBit < code.size() / 8) {
            int temp;
            temp = (code.get(counterCycleBit) + code.get(counterCycleBit + 1) + code.get(counterCycleBit + 2) + code.get(counterCycleBit + 3) + code.get(counterCycleBit + 4) + code.get(counterCycleBit + 5) + code.get(counterCycleBit + 6) + code.get(counterCycleBit + 7)) % 2;
            counterCycleBit += 8;
            counterParityBit++;
            parityCode.add((byte) temp);
            calculateParityBit(code);
        }
    }

    void calculateSyndrome(ArrayList<Byte> code) {

        byte[] tempArray = new byte[7];
        byte[] codeArray = new byte[4];
        byte temp;

        if (counterArrayList < code.size()) {
            for (int i = 0; i < 7; i++) {
                tempArray[i] = code.get(counterArrayList);
                counterArrayList++;
            }
        } else {
            return;
        }

        for (int j = 1; j < 4; j++) {
            temp = (byte) (tempArray[0] & matrixHT[0][j - 1] ^ tempArray[1] & matrixHT[1][j - 1] ^ tempArray[2] & matrixHT[2][j - 1]
                    ^ tempArray[3] & matrixHT[3][j - 1] ^ tempArray[4] & matrixHT[4][j - 1] ^ tempArray[5] & matrixHT[5][j - 1] ^
                    tempArray[6] & matrixHT[6][j - 1]);
            codeArray[j] = temp;
        }
        codeArray[0] = parityCode.get(counterCodeArray);

        for (byte aCodeArray : codeArray) {
            syndromeCode.add(counterSyndrome, aCodeArray);
            counterSyndrome++;
        }
        counterCodeArray++;
        calculateSyndrome(code);
    }

    void errorCorrection(ArrayList<Byte> code, ArrayList<Byte> codeS, ArrayList<Byte> codeSyndrome) {

        if (counterErrorBit < (code.size() / 7)) {

            int zeroErr = 0;
            int oneErr = 0;
            int twoErr = 0;
            int threeErr = 0;

            byte[] codeSyndromeArray = new byte[4];
            byte[] codeArray = new byte[7];
            byte parityBit = codeS.get(counterErrorBit);

            for (int i = 0; i < 4; i++) {
                codeSyndromeArray[i] = codeSyndrome.get(counterErrorSyndrome);
                counterErrorSyndrome++;
            }

            for (int i = 0; i < 7; i++) {
                codeArray[i] = code.get(counterCode);
                counterCode++;
            }

            if (counterErrorBit >= (code.size() / 7)) {
                return;
            }

            if (parityBit == 1) {
                for (int i = 1; i < 4; i++) {
                    if (codeSyndromeArray[i] == 1) {
                        oneErr++;
                    } else {
                        threeErr++;
                    }
                }
            } else {
                for (int i = 1; i < 4; i++) {
                    if (codeSyndromeArray[i] == 1) {
                        twoErr++;
                    } else {
                        zeroErr++;
                    }
                }
            }

            if (zeroErr == 3) {
                errorMessage.add("В строке " + (counterErrorBit + 1) + " ошибок не обнаружено\r\n");

                for (byte aCodeArray : codeArray) {
                    fixedCodeList.add(aCodeArray);
                }
            }

            if (oneErr > 0) {
                errorMessage.add("В строке " + (counterErrorBit + 1) + " обнаружена единичная ошибка\r\n");
                fixErrors(codeArray, codeSyndromeArray);
            }

            if (twoErr > 0) {
                errorMessage.add("В строке " + (counterErrorBit + 1) + " обнаружена двойная ошибка\r\n");
                for (byte aCodeArray : codeArray) {
                    fixedCodeList.add(aCodeArray);
                }
            }

            if (threeErr == 3) {
                errorMessage.add("В строке " + (counterErrorBit + 1) + " обнаружена тройная ошибка\r\n");
                fixErrors(codeArray, codeSyndromeArray);
            }

            if (counterErrorBit < (code.size() / 7)) {
                counterErrorBit++;
                errorCorrection(code, codeS, codeSyndrome);
            }

        }
    }

    private void fixErrors(byte[] codeArray, byte[] codeSyndrome) {

        byte[] codeSyndromeFix = new byte[codeSyndrome.length - 1];

        System.arraycopy(codeSyndrome, 1, codeSyndromeFix, 0, 3);

        for (int j = 0; j < matrixHT.length; j++) {

            if (Arrays.equals(codeSyndromeFix, matrixHT[j])) {
                for (int i = 0; i < 7; i++) {
                    if (i == j) {
                        codeArray[i] = (byte) ((codeArray[i] + 1) % 2);
                    }
                    codeArray[i] = (byte) ((codeArray[i]) % 2);
                    fixedCodeList.add(codeArray[i]);
                }
                return;
            }
        }
        for (byte aCodeArray : codeArray) {
            fixedCodeList.add(aCodeArray);
        }
    }
}