App.controller("RootControl", function($scope) {

    $scope.lesson = [
        {
            problemLines: [
                {items: ["Today is Monday."], tags: []},
                {items: ["@1", "是", "@2", "。"], tags: []}
            ],
            answers: [
                {items: ["今天", "今日"], tags: []},
                {items: ["星期一"], tags: []}
            ],
            marplots: {items: ["明天", "昨天", "星期日"], tags: []},
            done: true
        },
        {
            problemLines: [
                {items: [], tags: []}
            ],
            answers: [
            ],
            marplots: {items: [], tags: []},
            done: false
        }
    ];

    $scope.addNextProblem = function() {
        $scope.lesson.push({
            problemLines: [{items: [], tags: []}],
            answers: [],
            marplots: {items: [], tags: []},
            done: false
        });
    };

    $scope.deleteProblem = function(index) {
        $scope.lesson.splice(index, 1);
    };

    $scope.showLastAdd = function(index) {
        if (index === $scope.lesson.length - 1) {
            if ($scope.lesson[index].done) {
                return true;
            }
        }
        return false;
    };

    $scope.addProblemLine = function(problemLines) {
        problemLines.push({
            items: [], tags: []
        });
    };

    $scope.problemItemAdded = function(problem) {
        problem.answers = [];
        problem.problemLines.forEach(function(line) {
            line.tags.forEach(function(tag) {
                if (tag.text.indexOf("@") > -1) {
                    problem.answers.push({
                        items: [], tags: []
                    });
                }
            });
        });
    };

    $scope.confirmProblem = function(problem) {
        problem.problemLines.forEach(function(line) {
            line.tags.forEach(function(tag) {
                line.items.push(tag.text);
            });
        });

        problem.answers.forEach(function(answer) {
            answer.tags.forEach(function(tag) {
                answer.items.push(tag.text);
            });
        });

        problem.marplots.tags.forEach(function(tag) {
            problem.marplots.items.push(tag.text);
        });

        problem.done = true;
    };

    $scope.downloadLesson = function() {
        var out = "";
        $scope.lesson.forEach(function(problem) {
            out += "=fb\n";

            problem.problemLines.forEach(function(line) {
                out += "*";
                line.items.forEach(function(item) {
                    if (item.indexOf("@") > -1) {
                        out += "[@]";
                    } else {
                        out += "[" + item + "]";
                    }
                });
                out += "\n";
            });

            problem.answers.forEach(function(answer) {
                out += "#";
                answer.items.forEach(function(item) {
                    out += "[" + item + "]";
                });
                out += "\n";
            });

            out += "%";
            problem.marplots.items.forEach(function(item) {
                out += "[" + item + "]";
            });

            out += "\n\n\n";
        });

        var blob = new Blob([out], {type: "text/plain;charset=utf-8"});
        saveAs(blob, "your_lesson.txt");
    };
});

