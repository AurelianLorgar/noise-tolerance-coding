package sample;

import java.util.ArrayList;
import java.util.HashMap;

class ShennonFano {

    private ArrayList<Character> resultChar = new ArrayList<>();
    private ArrayList<String> resultString = new ArrayList<>();

    ArrayList<Character> getResultChar() {
        return resultChar;
    }

    ArrayList<String> getResultString() {
        return resultString;
    }

    void shennonFano(char[] arrayChar, char branch, String fullBranch, int startPos, int endPos) {

        int sym = 0;
        int amm = 0;
        int counter;
        double middle = 0;
        int middleLetter;
        int sumLeftBranch;
        String branchTurn;

        ArrayList<Character> symbols = new ArrayList<>();
        ArrayList<Integer> amount = new ArrayList<>();

        HashMap<Character, Integer> charMap = new HashMap<>();

        for (char aCharOriginalText : arrayChar) {
            if (charMap.containsKey(aCharOriginalText)) {
                charMap.put(aCharOriginalText, charMap.get(aCharOriginalText) + 1);
            } else {
                charMap.put(aCharOriginalText, 1);
            }
        }

        for (char symbol : charMap.keySet()) {
            symbols.add(sym, symbol);
            sym++;
        }

        for (int number : charMap.values()) {
            amount.add(amm, number);
            amm++;
        }

        if (!symbols.equals(' ')) {
            branchTurn = fullBranch + branch;
        } else {
            branchTurn = "";
        }

        if (startPos == endPos) {
            try {
                resultChar.add(symbols.get(startPos));
                resultString.add(branchTurn);
                return;
            } catch (Exception e) {
                return;
            }
        }

        if (amount.size() > endPos) {
            for (int i = startPos; i < endPos; i++) {
                middle = middle + amount.get(i);
            }
        } else {
            for (int i = startPos; i < amount.size(); i++) {
                middle = middle + amount.get(i);
            }
        }
        middle = middle / 2;

        sumLeftBranch = 0;
        counter = startPos;
        middleLetter = counter;

        try {
            while (((sumLeftBranch + amount.get(counter)) < middle) && (counter < endPos)) {
                sumLeftBranch = sumLeftBranch + amount.get(counter);
                counter++;
                middleLetter++;
            }
        } catch (Exception e) {
            return;
        }

        shennonFano(arrayChar, '1', branchTurn, startPos, middleLetter);
        shennonFano(arrayChar, '0', branchTurn, middleLetter + 1, endPos);
    }
}