myNg.controller("StepsControl", function($scope, $http) {
    $scope.lesson = null;
    $scope.mode = null;
    $scope.studyState = {
        reachFinal: false
    };
});

myNg.controller("StepPageControl", function($scope) {
    $scope.myIndex;
    $scope.step;
    $scope.answerPool = [];

    $scope.init = function(index) {
        $scope.myIndex = index;
        $scope.step = STEPS[index];

        if (!$scope.step.final) {
            var noAnswer = true;
            $scope.step.lines.forEach(function(line) {
                line.forEach(function(item) {
                    if (item.type === "ans") {
                        noAnswer = false;
                        item.text.forEach(function(answer) {
                            $scope.answerPool.push({type: "ans", text: answer});
                        });
                    }
                });
            });
            if (noAnswer) {
                $scope.step.check = true;
            }

            $scope.step.marplots.forEach(function(group) {
                group.text.forEach(function(mar) {
                    $scope.answerPool.push({type: "mar", text: mar});
                });
            });

            if ($scope.mode === "exam") {
                shuffle($scope.answerPool);
            }
        } else {
            $scope.steps = STEPS;
        }
    };

    function removeAnswerFromPool(a) {
        for (var i = 0; i < $scope.answerPool.length; i++) {
            if ($scope.answerPool[i] === a) {
                $scope.answerPool.splice(i, 1);
            }
        }
    }

    $scope.pickAnswer = function(a) {
        var found = false;
        var allOk = true;
        $scope.step.lines.forEach(function(line) {
            line.forEach(function(item) {
                if (item.type === "ans") {
                    if (!item.userInput && !found) {
                        found = true;
                        item.userInput = a.text;
                        removeAnswerFromPool(a);
                    }
                    item.ok = checkAnswerText(item.text, item.userInput);
                    if (!item.ok) {
                        allOk = false;
                    }
                }
            });
        });

        $scope.step.check = allOk;
        $scope.lesson.ok = checkLesson(STEPS);
    };

    function checkAnswerText(all, incoming) {
        for (var i = 0; i < all.length; i++) {
            if (all[i] === incoming) {
                return true;
            }
        }
        return false;
    }

    function checkLesson(steps) {
        for (var i = 0; i < steps.length - 1; i++) {
            if (!steps[i].check) {
                return false;
            }
        }
        return true;
    }

    $scope.removeInput = function(item) {
        $scope.answerPool.push({text: item.userInput});
        item.userInput = null;
        $scope.step.check = false;
        $scope.lesson.ok = false;
    };

    $scope.slideTo = function(index) {
        stepsUI.slideTo(index);
    };
});


