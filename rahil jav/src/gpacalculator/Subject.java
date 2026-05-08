package gpacalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Subject {
    private final String name;
    private final int credits;
    private final Double marks;
    private final String grade;

    public Subject(String name, int credits, Double marks, String grade) {
        this.name = name == null || name.isBlank() ? "Subject" : name.trim();
        this.credits = Math.max(1, credits);
        this.marks = marks;
        this.grade = grade == null ? "" : grade.trim();
    }

    public static List<Subject> fromForm(Map<String, String> form, int subjectCount) {
        List<Subject> subjects = new ArrayList<>();
        for (int index = 1; index <= subjectCount; index++) {
            String name = form.getOrDefault("name" + index, "Subject " + index);
            int credits = FormParser.intValue(form, "credits" + index, 3, 1, 10);
            String inputType = form.getOrDefault("inputType" + index, "marks");
            Double marks = "marks".equals(inputType) ? FormParser.doubleValue(form, "marks" + index, 0.0) : null;
            String grade = "grade".equals(inputType) ? form.getOrDefault("grade" + index, "F") : "";
            subjects.add(new Subject(name, credits, marks, grade));
        }
        return subjects;
    }

    public static List<Subject> samples(int subjectCount) {
        List<Subject> subjects = new ArrayList<>();
        for (int index = 1; index <= subjectCount; index++) {
            subjects.add(new Subject("Subject " + index, 3, null, ""));
        }
        return subjects;
    }

    public String name() {
        return name;
    }

    public int credits() {
        return credits;
    }

    public Double marks() {
        return marks;
    }

    public String grade() {
        return grade;
    }
}
