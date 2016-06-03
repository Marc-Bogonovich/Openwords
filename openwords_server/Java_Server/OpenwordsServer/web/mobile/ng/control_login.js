myNg.controller("LoginControl", function($scope, $http) {
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
            if (r.result) {
                mainView.router.loadPage("home.html");
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

        console.log(JSON.stringify(loginData));
    };
});


