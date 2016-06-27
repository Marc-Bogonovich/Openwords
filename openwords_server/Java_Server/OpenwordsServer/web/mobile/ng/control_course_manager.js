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

    $scope.courseListPack = {
        pageNumber: 1,
        pageSize: 5
    };

    $scope.listMyCourses = function(page) {
        $scope.courseListPack.pageNumer = page;
        listCourse($scope.courseListPack, $http);
    };

});


