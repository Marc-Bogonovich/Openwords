myNg.controller("StepsControl", function($scope, $http) {
    $scope.lesson;
    $scope.mode;
});

myNg.controller("StepPageControl", function($scope) {
    $scope.myIndex;
    $scope.step;
    $scope.answerPool = [];

    $scope.init = function(index) {
        $scope.myIndex = index;
        $scope.step = STEPS[index];

        if (!$scope.step.final) {
            $scope.step.lines.forEach(function(line) {
                line.forEach(function(item) {
                    if (item.type === "ans") {
                        item.text.forEach(function(answer) {
                            $scope.answerPool.push({type: "ans", text: answer});
                        });
                    }
                });
            });

            $scope.step.marplots.forEach(function(group) {
                group.text.forEach(function(mar) {
                    $scope.answerPool.push({type: "mar", text: mar});
                });
            });

            if ($scope.mode === "exam") {
                shuffle($scope.answerPool);
            }
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
        $scope.step.lines.forEach(function(line) {
            line.forEach(function(item) {
                if (found) {
                    return;
                }
                if (item.type === "ans") {
                    if (!item.userInput) {
                        found = true;
                        item.userInput = a.text;
                        removeAnswerFromPool(a);
                    }
                }
            });
        });
    };

    $scope.removeInput = function(item) {
        $scope.answerPool.push({text: item.userInput});
        item.userInput = null;
    };
});


