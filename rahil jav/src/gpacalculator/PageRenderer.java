package gpacalculator;

import java.util.List;

public class PageRenderer {
    public String render(int subjectCount, List<Subject> subjects, SemesterResult result) {
        StringBuilder page = new StringBuilder();
        page.append("""
                <!doctype html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Semester GPA Calculator</title>
                    <style>
                """);
        page.append(styles());
        page.append("""
                    </style>
                </head>
                <body>
                    <main class="shell">
                        <section class="intro">
                            <div>
                                <p class="eyebrow">Java Dynamic Web Project</p>
                                <h1>Semester GPA Calculator</h1>
                                <p class="subtle">Enter marks or direct grades, assign credits, and calculate weighted semester GPA instantly.</p>
                            </div>
                """);
        page.append(summary(result));
        page.append("""
                        </section>
                        <section class="workspace">
                            <form method="get" class="subject-count">
                                <label for="subjectCount">Subjects</label>
                                <select id="subjectCount" name="subjectCount">
                """);
        for (int count = 1; count <= 12; count++) {
            page.append("<option value=\"").append(count).append("\"")
                    .append(count == subjectCount ? " selected" : "")
                    .append(">").append(count).append("</option>");
        }
        page.append("""
                                </select>
                                <button class="small-button" type="submit">Update</button>
                            </form>
                            <form method="post" class="calculator">
                                <input type="hidden" name="subjectCount" value="%d">
                                <div class="grid header">
                                    <span>Subject</span><span>Credits</span><span>Input</span><span>Marks</span><span>Grade</span>
                                </div>
                """.formatted(subjectCount));

        for (int index = 1; index <= subjectCount; index++) {
            Subject subject = index <= subjects.size() ? subjects.get(index - 1) : new Subject("Subject " + index, 3, null, "");
            page.append(subjectRow(index, subject));
        }

        page.append("""
                                <button class="primary" type="submit">Calculate GPA</button>
                            </form>
                        </section>
                """);
        if (result != null) {
            page.append(performance(result));
        }
        page.append("""
                    </main>
                </body>
                </html>
                """);
        return page.toString();
    }

    public String renderError(String message) {
        return """
                <!doctype html>
                <html><head><title>Input Error</title><style>%s</style></head>
                <body><main class="shell"><section class="workspace"><h1>Input Error</h1><p>%s</p><a class="primary link" href="/">Back to calculator</a></section></main></body></html>
                """.formatted(styles(), escape(message));
    }

    private String subjectRow(int index, Subject subject) {
        boolean gradeMode = subject.marks() == null && !subject.grade().isBlank();
        return """
                <div class="grid row">
                    <input name="name%d" value="%s" aria-label="Subject %d name">
                    <input type="number" name="credits%d" min="1" max="10" value="%d" aria-label="Subject %d credits">
                    <select name="inputType%d" aria-label="Subject %d input type">
                        <option value="marks"%s>Marks</option>
                        <option value="grade"%s>Grade</option>
                    </select>
                    <input type="number" name="marks%d" min="0" max="100" step="0.01" value="%s" aria-label="Subject %d marks">
                    <select name="grade%d" aria-label="Subject %d grade">
                        %s
                    </select>
                </div>
                """.formatted(
                index, escape(subject.name()), index,
                index, subject.credits(), index,
                index, index,
                gradeMode ? "" : " selected",
                gradeMode ? " selected" : "",
                index, subject.marks() == null ? "" : format(subject.marks()), index,
                index, index, gradeOptions(subject.grade())
        );
    }

    private String summary(SemesterResult result) {
        if (result == null) {
            return """
                    <div class="score-card">
                        <span>GPA</span>
                        <strong>--</strong>
                        <small>Awaiting input</small>
                    </div>
                    """;
        }
        return """
                <div class="score-card">
                    <span>GPA</span>
                    <strong>%s</strong>
                    <small>%d credits</small>
                </div>
                """.formatted(format(result.gpa()), result.totalCredits());
    }

    private String performance(SemesterResult result) {
        StringBuilder section = new StringBuilder("""
                <section class="results">
                    <h2>Subject Performance</h2>
                    <div class="cards">
                """);
        for (SubjectResult item : result.subjectResults()) {
            section.append("""
                    <article class="card">
                        <div>
                            <h3>%s</h3>
                            <p>%s grade point across %d credits</p>
                        </div>
                        <strong>%s</strong>
                        <span>%s</span>
                    </article>
                    """.formatted(
                    escape(item.subject().name()),
                    format(item.gradePoint().point()),
                    item.subject().credits(),
                    escape(item.gradePoint().grade()),
                    escape(item.gradePoint().label())
            ));
        }
        section.append("""
                    </div>
                </section>
                """);
        return section.toString();
    }

    private String gradeOptions(String selected) {
        String[] grades = {"O", "A+", "A", "B+", "B", "C", "D", "F"};
        StringBuilder options = new StringBuilder();
        for (String grade : grades) {
            options.append("<option value=\"").append(grade).append("\"")
                    .append(grade.equalsIgnoreCase(selected) ? " selected" : "")
                    .append(">").append(grade).append("</option>");
        }
        return options.toString();
    }

    private String styles() {
        return """
                :root{color-scheme:light;--ink:#18212f;--muted:#627084;--line:#d8e0ea;--paper:#ffffff;--soft:#f5f8fb;--accent:#087f8c;--accent-dark:#05606b;--gold:#f4b942;--rose:#d95d62}
                *{box-sizing:border-box}body{margin:0;font-family:Inter,Segoe UI,Arial,sans-serif;background:linear-gradient(135deg,#eef7f6 0%,#f8fafc 42%,#fff7e4 100%);color:var(--ink);min-height:100vh}
                .shell{width:min(1120px,calc(100% - 32px));margin:0 auto;padding:42px 0}
                .intro{display:grid;grid-template-columns:1fr auto;gap:28px;align-items:end;margin-bottom:24px}
                .eyebrow{margin:0 0 8px;color:var(--accent-dark);font-weight:800;text-transform:uppercase;font-size:12px;letter-spacing:0}
                h1{font-size:58px;line-height:1;margin:0 0 14px;letter-spacing:0}
                h2{margin:0 0 18px;font-size:24px}.subtle{margin:0;max-width:620px;color:var(--muted);font-size:18px;line-height:1.55}
                .score-card{background:var(--ink);color:white;border-radius:8px;padding:22px 28px;min-width:190px;box-shadow:0 18px 40px #18212f24}
                .score-card span,.score-card small{display:block;color:#d5e6ee}.score-card strong{display:block;font-size:52px;line-height:1;margin:8px 0}
                .workspace,.results{background:rgba(255,255,255,.86);border:1px solid var(--line);border-radius:8px;padding:22px;box-shadow:0 20px 60px #4762761f}
                .subject-count{display:flex;align-items:center;gap:12px;margin-bottom:18px;font-weight:800}
                .small-button{border:1px solid var(--accent);background:#e8f6f7;color:var(--accent-dark);border-radius:7px;min-height:44px;padding:0 18px;font:inherit;font-weight:900;cursor:pointer}
                select,input{width:100%;border:1px solid var(--line);border-radius:7px;background:var(--paper);color:var(--ink);font:inherit;padding:11px 12px;min-height:44px}
                select:focus,input:focus{outline:3px solid #087f8c24;border-color:var(--accent)}
                .grid{display:grid;grid-template-columns:2fr .8fr .9fr .9fr .9fr;gap:10px;align-items:center}
                .header{color:var(--muted);font-size:13px;font-weight:800;text-transform:uppercase;margin:8px 0}
                .row{padding:10px 0;border-top:1px solid #e8eef4}.row:first-of-type{border-top:0}
                .primary{margin-top:18px;width:100%;border:0;border-radius:7px;background:var(--accent);color:white;min-height:50px;font-weight:900;font-size:16px;cursor:pointer}
                .primary:hover{background:var(--accent-dark)}.link{display:inline-flex;align-items:center;justify-content:center;text-decoration:none;padding:14px 18px}
                .results{margin-top:24px}.cards{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:14px}
                .card{background:var(--paper);border:1px solid var(--line);border-left:5px solid var(--gold);border-radius:8px;padding:16px;display:grid;grid-template-columns:1fr auto;gap:8px;align-items:start}
                .card h3{margin:0 0 6px;font-size:17px}.card p{margin:0;color:var(--muted);font-size:14px;line-height:1.4}.card strong{font-size:30px}.card span{grid-column:1/-1;color:var(--accent-dark);font-weight:800}
                @media (max-width:780px){.shell{width:min(100% - 20px,1120px);padding:22px 0}.intro{grid-template-columns:1fr}.grid{grid-template-columns:1fr 1fr}.header{display:none}.row{background:var(--soft);border:1px solid var(--line);border-radius:8px;padding:12px;margin-bottom:12px}.score-card{min-width:0}h1{font-size:38px}}
                """;
    }

    private String format(double value) {
        return String.format("%.2f", value);
    }

    private String escape(String value) {
        return value == null ? "" : value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
