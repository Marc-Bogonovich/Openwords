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
        myApp.alert(null, "Upload success", function() {
            console.log("refresh");
        });
    };
    uploader.onErrorItem = function(fileItem, response, status, headers) {
        myApp.alert(null, "Upload fail");
    };

});


