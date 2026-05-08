package gpacalculator;

import java.util.ArrayList;
import java.util.List;

public class SemesterCalculator {
    private final GradeScale gradeScale;

    public SemesterCalculator(GradeScale gradeScale) {
        this.gradeScale = gradeScale;
    }

    public SemesterResult calculate(List<Subject> subjects) {
        List<SubjectResult> subjectResults = new ArrayList<>();
        int totalCredits = 0;
        double totalWeightedPoints = 0.0;

        for (Subject subject : subjects) {
            GradePoint gradePoint = subject.marks() == null
                    ? gradeScale.evaluate(subject.grade())
                    : gradeScale.evaluate(clampMarks(subject.marks()));
            SubjectResult result = new SubjectResult(subject, gradePoint);
            subjectResults.add(result);
            totalCredits += subject.credits();
            totalWeightedPoints += result.weightedPoints();
        }

        return new SemesterResult(subjectResults, totalCredits, totalWeightedPoints);
    }

    private double clampMarks(double marks) {
        return Math.max(0.0, Math.min(100.0, marks));
    }
}
