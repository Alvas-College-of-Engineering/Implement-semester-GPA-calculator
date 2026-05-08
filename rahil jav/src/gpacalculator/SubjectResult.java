package gpacalculator;

public class SubjectResult {
    private final Subject subject;
    private final GradePoint gradePoint;
    private final double weightedPoints;

    public SubjectResult(Subject subject, GradePoint gradePoint) {
        this.subject = subject;
        this.gradePoint = gradePoint;
        this.weightedPoints = subject.credits() * gradePoint.point();
    }

    public Subject subject() {
        return subject;
    }

    public GradePoint gradePoint() {
        return gradePoint;
    }

    public double weightedPoints() {
        return weightedPoints;
    }
}
