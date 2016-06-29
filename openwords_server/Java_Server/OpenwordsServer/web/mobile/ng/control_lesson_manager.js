myNg.controller("LessonManagerControl", function($scope, $http, FileUploader) {
    var uploader = $scope.uploader = new FileUploader({
        url: "uploadLesson",
        queueLimit: 1
    });

    var name;

    $scope.doUpload = function() {
        myApp.prompt("Please enter a name for the lesson", "Uploading Lesson", function(v) {
            name = v;
            if (name) {
                uploader.uploadAll();
            } else {
                myApp.alert(null, "You've provided invalid information");
            }

        });
    };

    uploader.onBeforeUploadItem = function(item) {
        item.formData.push({userId: userInfo.userId});
        item.formData.push({name: name});
    };
    uploader.onSuccessItem = function(fileItem, response, status, headers) {
        uploader.clearQueue();
        myApp.alert(null, "Upload success");
        $scope.listMyLessons(1);
    };
    uploader.onErrorItem = function(fileItem, response, status, headers) {
        myApp.alert(null, "Upload fail");
    };

    $scope.lessonListPack = {
        page: 1,
        pageSize: 100
    };

    $scope.listMyLessons = function(page) {
        $scope.lessonListPack.page = page;
        $scope.lessonListPack.userId = userInfo.userId;
        listLesson($scope.lessonListPack, $http);
    };

    var chosenLesson = null;
    var actionButtons = [
        {
            text: "Preview",
            onClick: function() {
                console.log(chosenLesson);
                STEPS = chosenLesson.json.steps;
                mainView.router.load({pageName: "steps"});
            }
        },
        {
            text: "Delete",
            color: "red"
        },
        {
            text: "Cancel"
        }
    ];

    $scope.lessonAction = function(le) {
        chosenLesson = le;
        myApp.actions(actionButtons);
    };
});


