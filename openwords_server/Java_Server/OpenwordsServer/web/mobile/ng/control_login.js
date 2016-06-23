myNg.controller("LoginControl", function($scope, $http) {
    $scope.full = function() {
        var elem = document.getElementById("RootControl");
        if (elem.requestFullscreen) {
            elem.requestFullscreen();
        } else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
        } else if (elem.mozRequestFullScreen) {
            elem.mozRequestFullScreen();
        } else if (elem.webkitRequestFullscreen) {
            elem.webkitRequestFullscreen();
        }
    };

    $scope.login = function() {
        var loginData = myApp.formToJSON("#my-login-form");

        var loginOk = false;
        myApp.showPreloader("Connecting to Openwords...");

        $http({
            url: "loginUser",
            method: "get",
            params: {
                username: loginData.username,
                password: loginData.pass
            }
        }).then(function(res) {
            loginOk = true;
            myApp.hidePreloader();

            var r = res.data;
            console.log(r);
            userInfo = r;
            userInfo.username = loginData.username;
            if (r.result) {
                var CourseManagerControl = getScope("CourseManagerControl");
                CourseManagerControl.courseListPack.userId = userInfo.userId;
                CourseManagerControl.listMyCourses(1);

                mainView.router.load({pageName: "course_list"});
            } else {
                myApp.alert(r.errorMessage, "Login fail");
            }
        });

        setTimeout(function() {
            myApp.hidePreloader();
            if (!loginOk) {
                myApp.alert("No response from server", "Error");
            }
        }, 10000);
    };
});


