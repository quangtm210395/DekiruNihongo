package hmdq.js.codeproject.dekirunihongo.Quiz;

/**
 * Created by Phan M Duong on 5/3/2016.
 */
public class ArrayQuiz {
    private String question;
    private String[] answer;

    public ArrayQuiz() {
    }

    public ArrayQuiz(String question, String[] answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer(int i) {
        return answer[i];
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String[] answer) {
        this.answer = answer;
    }
}
