package gpacalculator;

public class GradeScale {
    public GradePoint evaluate(double marks) {
        if (marks >= 90) {
            return new GradePoint("O", 10.0, "Outstanding");
        }
        if (marks >= 80) {
            return new GradePoint("A+", 9.0, "Excellent");
        }
        if (marks >= 70) {
            return new GradePoint("A", 8.0, "Very Good");
        }
        if (marks >= 60) {
            return new GradePoint("B+", 7.0, "Good");
        }
        if (marks >= 50) {
            return new GradePoint("B", 6.0, "Average");
        }
        if (marks >= 40) {
            return new GradePoint("C", 5.0, "Pass");
        }
        return new GradePoint("F", 0.0, "Needs Improvement");
    }

    public GradePoint evaluate(String grade) {
        String normalized = grade == null ? "" : grade.trim().toUpperCase();
        return switch (normalized) {
            case "O", "S" -> new GradePoint(normalized, 10.0, "Outstanding");
            case "A+" -> new GradePoint("A+", 9.0, "Excellent");
            case "A" -> new GradePoint("A", 8.0, "Very Good");
            case "B+" -> new GradePoint("B+", 7.0, "Good");
            case "B" -> new GradePoint("B", 6.0, "Average");
            case "C" -> new GradePoint("C", 5.0, "Pass");
            case "D" -> new GradePoint("D", 4.0, "Low Pass");
            case "F" -> new GradePoint("F", 0.0, "Needs Improvement");
            default -> new GradePoint("F", 0.0, "Needs Improvement");
        };
    }
}
