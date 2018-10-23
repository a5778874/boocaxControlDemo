package com.example.aiui;


//eventbus用的信息类型
public class RobotMsg {

    private RobotMsg() {
    }

    //ASR
    public static final int EVENT_TYPE_ASR = 1;
    //IAT
    public static final int EVENT_TYPE_NLP = 2;


    public static class NLPMsg {
        private String question;
        private String answer;

        public NLPMsg(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }


    public static class ASRMsg {
        private int asrID;
        private String question;

        public ASRMsg(int asrID, String question) {
            this.question = question;
            this.asrID = asrID;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public int getAsrID() {
            return asrID;
        }

        public void setAsrID(int asrID) {
            this.asrID = asrID;
        }
    }
}
