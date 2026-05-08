package gpacalculator;

public class GradePoint {
    private final String grade;
    private final double point;
    private final String label;

    public GradePoint(String grade, double point, String label) {
        this.grade = grade;
        this.point = point;
        this.label = label;
    }

    public String grade() {
        return grade;
    }

    public double point() {
        return point;
    }

    public String label() {
        return label;
    }
}
