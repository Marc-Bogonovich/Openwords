myNg.controller("CourseManagerControl", function($scope, $http, FileUploader) {

    $scope.createCourse = function() {
        myApp.prompt("Please enter a name for the course", "Creating Course", function(v) {
            var name = v;
            myApp.prompt("Please enter a simple description for the course", "Creating Course", function(v) {
                var comment = v;
                $http({
                    url: "createCourse",
                    method: "get",
                    params: {
                        name: name,
                        userId: userInfo.userId,
                        userName: userInfo.username,
                        comment: comment
                    }
                }).then(function(res) {
                    var r = res.data;
                    if (!r.errorMessage) {
                        myApp.alert(null, "Success");
                        $scope.listMyCourses(1);
                    } else {
                        myApp.alert(null, "Fail");
                    }
                });
            });
        });
    };

    $scope.listMyCourses = function(page) {
        $scope.rootMyCourseList.page = page;
        $scope.rootMyCourseList.userId = userInfo.userId;
        listCourse($scope.rootMyCourseList, $http);
    };

    var chosenCourse;
    var actionButtons = [
        {
            text: "Edit",
            onClick: function() {
                var CourseEditControl = getScope("CourseEditControl");
                CourseEditControl.course = chosenCourse;
                CourseEditControl.$apply();
                mainView.router.load({pageName: "course_edit"});
            }
        },
        {
            text: "Delete",
            color: "red",
            onClick: function() {
                myApp.confirm("Are you sure to delete Course \"" + chosenCourse.name + "\"?",
                        "Deleting Course",
                        function() {
                            $http({
                                url: "deleteCourse",
                                method: "get",
                                params: {
                                    pass: "别瞎删昂!",
                                    userId: chosenCourse.userId,
                                    courseId: chosenCourse.courseId
                                }
                            }).then(function(res) {
                                var r = res.data;
                                if (!r.errorMessage) {
                                    $scope.listMyCourses(1);
                                    myApp.alert(null, "Course deleted");
                                }
                            });
                        }
                );
            }
        },
        {
            text: "Cancel"
        }
    ];

    $scope.courseAction = function(c) {
        console.log(c);
        chosenCourse = c;
        myApp.actions(actionButtons);
    };
});


