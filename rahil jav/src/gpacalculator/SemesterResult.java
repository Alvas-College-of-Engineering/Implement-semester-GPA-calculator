package gpacalculator;

import java.util.List;

public class SemesterResult {
    private final List<SubjectResult> subjectResults;
    private final int totalCredits;
    private final double totalWeightedPoints;
    private final double gpa;

    public SemesterResult(List<SubjectResult> subjectResults, int totalCredits, double totalWeightedPoints) {
        this.subjectResults = List.copyOf(subjectResults);
        this.totalCredits = totalCredits;
        this.totalWeightedPoints = totalWeightedPoints;
        this.gpa = totalCredits == 0 ? 0.0 : totalWeightedPoints / totalCredits;
    }

    public List<SubjectResult> subjectResults() {
        return subjectResults;
    }

    public int totalCredits() {
        return totalCredits;
    }

    public double totalWeightedPoints() {
        return totalWeightedPoints;
    }

    public double gpa() {
        return gpa;
    }
}
