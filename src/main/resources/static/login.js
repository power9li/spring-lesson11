$(document).ready(function(){
    $("#loginBtn").click(function(){
        var obj = {
            username: $("#username").val(),
            password: $("#password").val(),
            "remember-me": $("#rememberMe").val(),
            "_csrf":$("#_csrf").val()
        };
        console.log("before login params 1= " + JSON.stringify(obj));

        $.ajax('/mylogin', {
            type: "POST",
            dataType: 'json',
            //headers:{
            //    "X-Content-Type-Options":"nosniff"
            //},
            data:{
                username: $("#username").val(),
                password: $("#password").val(),
                "remember-me": $("#rememberMe").val(),
                "_csrf":$("#_csrf").val()
            },
            success: function (data) {
                console.log("data=" + JSON.stringify(data));
                var success = data["success"];
                if(success == true){
                    var redirectUrl = data["redirectUrl"];
                    if(redirectUrl != null){
                        location.href = redirectUrl;
                    }
                }
                else{
                    var reason = data["reason"];
                    BootstrapDialog.show({
                        type: BootstrapDialog.TYPE_DANGER,
                        title: '登录失败!',
                        message: '失败原因:'+reason,
                        buttons: [{
                            label: '关闭',
                            action: function(dialogItself){
                                dialogItself.close();
                            }
                        }]
                    });
                }
            },
            error: function (e) {
                console.log("e.tojson="+JSON.stringify(e));
                if(e["redirectUrl"]){
                    location.href = e["redirectUrl"];
                }
                else if(e["responseJSON"]) {
                    responseJSON = e["responseJSON"];
                    var reason = responseJSON["reason"];
                    if (reason) {
                        BootstrapDialog.show({
                            type: BootstrapDialog.TYPE_DANGER,
                            title: '登录失败!',
                            message: '失败原因:' + reason,
                            buttons: [{
                                label: '关闭',
                                action: function (dialogItself) {
                                    dialogItself.close();
                                }
                            }]
                        });
                    }
                }
            }
        });
    });
});