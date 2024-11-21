package com.example.pi_rates;

import java.util.Random;

public class TaskGenerator {

    private static Random random = new Random();

    public static Task generateTask(int level) {
        String operationType;
        int num1, num2, correctAnswer;
        String question;

        int minValue = Math.min(10, level );
        int maxValue = Math.min(50, level * 10);
        int maxResult = Math.min(100, level * 10+10);

        int operationChoice = random.nextInt(Math.min(level, 4));
        switch (operationChoice) {
            case 0:
                operationType = "+";
                return generateAdditionTask(minValue, maxValue, maxResult);
            case 1:
                operationType = "-";
                return generateSubtractionTask(minValue, maxValue, maxResult);
            case 2:
                operationType = "*";
                return generateMultiplicationTask(minValue, maxValue, maxResult);
            case 3:
                operationType = "/";
                return generateDivisionTask(minValue, maxValue, maxResult, level);
            default:
                throw new IllegalStateException("Unexpected operation choice: " + operationChoice);
        }
    }

    private static Task generateAdditionTask(int minValue, int maxValue, int maxResult) {
        int retries = 0;
        int maxRetries = 100;
        int num1, num2, correctAnswer;
        do {
            num1 = getRandomNumber(minValue, maxValue);
            num2 = getRandomNumber(minValue, maxValue);
            correctAnswer = num1 + num2;
            retries++;
            if (retries > maxRetries) {
                return generateAdditionTask(minValue, maxValue, maxResult);
            }
        } while (correctAnswer > maxResult);

        String question = num1 + " + " + num2;
        String[] possibleAnswers = generatePossibleAnswers(correctAnswer);
        return new Task("+", 1, question, correctAnswer, possibleAnswers);
    }

    private static Task generateSubtractionTask(int minValue, int maxValue, int maxResult) {
        int retries = 0;
        int maxRetries = 100;
        int num1, num2, correctAnswer;
        do {
            num1 = getRandomNumber(minValue, maxValue);
            num2 = getRandomNumber(minValue, maxValue);
            if (num1 < num2) {
                int temp = num1;
                num1 = num2;
                num2 = temp;
            }
            correctAnswer = num1 - num2;
            retries++;
            if (retries > maxRetries) {
                return generateSubtractionTask(minValue, maxValue, maxResult);
            }
        } while (correctAnswer > maxResult);

        String question = num1 + " - " + num2;
        String[] possibleAnswers = generatePossibleAnswers(correctAnswer);
        return new Task("-", 1, question, correctAnswer, possibleAnswers);
    }

    private static Task generateMultiplicationTask(int minValue, int maxValue, int maxResult) {
        int retries = 0;
        int maxRetries = 100;
        int num1, num2, correctAnswer;



        while (retries <= maxRetries) {

            num1 = getRandomNumber(minValue, maxValue);
            num2 = getRandomNumber(minValue, maxValue);
            correctAnswer = num1 * num2;

            if (correctAnswer <= maxResult) {
                String question = num1 + " * " + num2;
                String[] possibleAnswers = generatePossibleAnswers(correctAnswer);
                return new Task("*", 1, question, correctAnswer, possibleAnswers);
            }

            retries++;
            maxValue = Math.max(minValue, maxValue - 1);
        }

        return new Task("*", 1, "Retry Limit Exceeded", -1, new String[]{"0", "0", "0", "0"});
    }

    private static Task generateDivisionTask(int minValue, int maxValue, int maxResult, int level) {
        int retries = 0;
        int maxRetries = 100;
        int num1, num2, correctAnswer;
        do {

            num2 =  getRandomNumber(1,Math.min(10, level+2 ));
            num1 = num2 *  getRandomNumber(1,Math.min(10, level+2 ));
            correctAnswer = num1 / num2;
            retries++;
            if (retries > maxRetries || correctAnswer == 0) {
                return generateDivisionTask(minValue, maxValue, maxResult, level);
            }
        } while (correctAnswer > maxResult);

        String question = num1 + " / " + num2;
        String[] possibleAnswers = generatePossibleAnswers(correctAnswer);
        return new Task("/", 1, question, correctAnswer, possibleAnswers);
    }

    private static int getRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private static String[] generatePossibleAnswers(int correctAnswer) {
        String[] answers = new String[4];
        answers[0] = String.valueOf(correctAnswer);

        int incorrectType = random.nextInt(3) + 1;

        if (incorrectType == 3) {
            answers[1] = String.valueOf(correctAnswer - random.nextInt(10) - 1);
            answers[2] = String.valueOf(correctAnswer - random.nextInt(10) - 1);
            answers[3] = String.valueOf(correctAnswer - random.nextInt(10) - 1);
        } else if (incorrectType == 2) {
            answers[1] = String.valueOf(correctAnswer - random.nextInt(10) - 1);
            answers[2] = String.valueOf(correctAnswer + random.nextInt(10) + 1);
            answers[3] = String.valueOf(correctAnswer + random.nextInt(10) + 1);
        } else {
            answers[1] = String.valueOf(correctAnswer + random.nextInt(10) + 1);
            answers[2] = String.valueOf(correctAnswer + random.nextInt(10) + 1);
            answers[3] = String.valueOf(correctAnswer + random.nextInt(10) + 1);
        }

        for (int i = 0; i < answers.length; i++) {
            int swapIndex = random.nextInt(answers.length);
            String temp = answers[i];
            answers[i] = answers[swapIndex];
            answers[swapIndex] = temp;
        }

        return answers;
    }
}
