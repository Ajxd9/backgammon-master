import junit.framework.TestCase;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import com.HyenaBgammon.models.Question;

class QuestionTest {

    private static final String TEST_FILE_PATH = "test_questions.json";

    /********* Test for saveQuestions function **********/
    @Test
    public void testSaveQuestions() throws IOException {
        // Create test questions
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("What is 2+2?", new String[]{"1", "2", "3", "4"}, "4", "Easy"));
        questions.add(new Question("Is the Earth round?", new String[]{"Yes", "No"}, "Yes", "Easy"));

        // Save questions to a JSON file
        JSONObject root = new JSONObject();
        JSONArray questionsArray = new JSONArray();

        for (Question q : questions) {
            JSONObject questionObj = new JSONObject();
            questionObj.put("question", q.getQuestion());
            questionObj.put("answers", new JSONArray(q.getAnswers()));
            questionObj.put("correct_ans", q.getCorrectAns());
            questionObj.put("difficulty", q.getDifficulty());
            questionsArray.put(questionObj);
        }

        root.put("questions", questionsArray);

        FileWriter file = null;
        try {
            file = new FileWriter(TEST_FILE_PATH);
            file.write(root.toString(2));
        } finally {
            if (file != null) {
                file.close();
            }
        }

        // Verify the file was created
        File testFile = new File(TEST_FILE_PATH);
        assertTrue("File was not created", testFile.exists());

        // Verify the content of the file
        FileReader reader = new FileReader(testFile);
        StringBuilder content = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            content.append((char) ch);
        }
        reader.close();

        JSONObject savedRoot = new JSONObject(content.toString());
        JSONArray savedQuestionsArray = savedRoot.getJSONArray("questions");

        // Verify the number of questions
        assertEquals(questions.size(), savedQuestionsArray.length(), "Number of questions mismatch");

        // Verify the content of each question
        for (int i = 0; i < questions.size(); i++) {
            JSONObject savedQuestionObj = savedQuestionsArray.getJSONObject(i);
            Question originalQuestion = questions.get(i);

            assertEquals(originalQuestion.getQuestion(), savedQuestionObj.getString("question"), "Question text mismatch");
            assertEquals(originalQuestion.getCorrectAns(), savedQuestionObj.getString("correct_ans"), "Correct answer mismatch");
            assertEquals(originalQuestion.getDifficulty(), savedQuestionObj.getString("difficulty"), "Difficulty mismatch");
            JSONArray savedAnswersArray = savedQuestionObj.getJSONArray("answers");

            for (int j = 0; j < originalQuestion.getAnswers().length; j++) {
                assertEquals(originalQuestion.getAnswers()[j], savedAnswersArray.getString(j), "Answer mismatch");
            }
        }
    }
    
    /********* Test for readFile function **********/
    @Test
    public void testReadFile() throws IOException {
        // Prepare test content
        String testContent = "This is a test file content.";

        // Write test content to a file
        FileWriter writer = new FileWriter(TEST_FILE_PATH);
        writer.write(testContent);
        writer.close();

        // Read the file content using the readFile method
        String result = readFile(TEST_FILE_PATH);

        // Verify the content matches
        assertEquals(testContent, result, "File content does not match");
    }

    private String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return content.toString();
    }
    
}
