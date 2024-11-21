package com.example.pi_rates;

public class Task {

        private String operationType;
        private int difficultyLevel;
        private String question;
        private int correctAnswer;
        private String[] possibleAnswers;

        public Task(String operationType, int difficultyLevel, String question, int correctAnswer, String[] possibleAnswers) {
            this.operationType = operationType;
            this.difficultyLevel = difficultyLevel;
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.possibleAnswers = possibleAnswers;
        }

    public String getOperationType() {
        return operationType;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public String getQuestion() {
        return question;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}
