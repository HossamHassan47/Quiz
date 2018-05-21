package com.wordpress.hossamhassan47.quiz.model;

/**
 * Question Model Class
 *
 * This class used as a model class for the quiz question
 */
public class Question {

    /**
     * The question Id
     */
    private String id;

    /**
     * The question Title
     */
    private String title;

    /**
     * The question Option #1
     */
    private String option1;

    /**
     * The question Option #2
     */
    private String option2;

    /**
     * The question Option #3
     */
    private String option3;

    /**
     * The question Option #4
     */
    private String option4;

    /**
     * The question Correct Answer
     */
    private String correctAnswer;

    /**
     * The question Type i.e. Free Text, Radiobutton, or Checkbox
     */
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
