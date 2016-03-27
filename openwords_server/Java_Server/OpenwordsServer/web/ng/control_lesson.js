App.controller("RootControl", function($scope, $sce) {

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
            done: true,
            answerDisplay: ["星期日", "星期一", "昨天", "今天", "明天", "今日"]
        },
        {
            problemLines: [
                {items: ["I am a cat."], tags: []},
                {items: ["@1", "@2", "@3", "@4"], tags: []}
            ],
            answers: [
                {items: ["我"], tags: []},
                {items: ["是"], tags: []},
                {items: ["一只"], tags: []},
                {items: ["猫。"], tags: []}
            ],
            marplots: {items: ["你", "不是", "狗。", "一支"], tags: []},
            done: true,
            answerDisplay: ["猫。", "一支", "我", "是", "一只", "狗。", "你", "不是"]
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
            done: false,
            answerDisplay: []
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

    $scope.addBlankItem = function(problem, line) {
        var blankOrder = 1;
        line.tags.forEach(function(tag) {
            if (tag.text.indexOf("@") > -1) {
                blankOrder += 1;
            }
        });
        line.tags.push({text: "@" + blankOrder});

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

        problem.answerDisplay = [];
        problem.answers.forEach(function(answer) {
            answer.tags.forEach(function(tag) {
                answer.items.push(tag.text);
                problem.answerDisplay.push(tag.text);
            });
        });

        problem.marplots.tags.forEach(function(tag) {
            problem.marplots.items.push(tag.text);
            problem.answerDisplay.push(tag.text);
        });

        problem.done = true;
        problem.answerDisplay.sort(function() {
            return 0.5 - Math.random();
        });
    };

    $scope.displayProblemLine = function(line) {
        var html = "";
        var totalBlank = 0;
        line.items.forEach(function(item) {
            if (S(item).contains("@")) {
                totalBlank += 1;
                html += "<span class='form-inline'><input type='text' style='width: 50px; color: #000000;' class='form-control'/></span>";
            } else {
                html += "<span>" + item + "</span>";
            }
        });
        if (totalBlank === line.items.length) {
            console.log("big blank");
            return $sce.trustAsHtml("<input type='text' style='width: 120px; color: #000000;' class='form-control'/>");
        }
        return $sce.trustAsHtml(html);
    };

    $scope.downloadLesson = function() {
        var out = "";
        $scope.lesson.forEach(function(problem) {
            out += "=fb\r\n";

            problem.problemLines.forEach(function(line) {
                out += "*";
                line.items.forEach(function(item) {
                    if (item.indexOf("@") > -1) {
                        out += "[@]";
                    } else {
                        out += "[" + item + "]";
                    }
                });
                out += "\r\n";
            });

            problem.answers.forEach(function(answer) {
                out += "#";
                answer.items.forEach(function(item) {
                    out += "[" + item + "]";
                });
                out += "\r\n";
            });

            out += "%";
            problem.marplots.items.forEach(function(item) {
                out += "[" + item + "]";
            });

            out += "\r\n\r\n\r\n";
        });

        var blob = new Blob([out], {type: "text/plain;charset=utf-8"});
        saveAs(blob, "your_lesson.txt");
    };
});

