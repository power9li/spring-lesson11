$(document).ready(function(){
    $("#userInfoBtn").click(function(){
        $(".with-errors").html("");
        $.ajax("/distSession/saveUserInfo",{
            type: "POST",
            data:{
                name : $("#name").val(),
                age  : $("#age").val()
            },
            success : function(data){
                var type = typeof(data);
                if(type == "string"){
                    data = eval("(" + data + ")");
                }
                if(data.success){
                    console.log("success = true");
                    var userInfo = data.userInfo;
                    alert("成功保存用户信息("+userInfo+")到分布式session中");
                    location.reload();
                }
                else{
                    console.log("success = false");
                    var msgs = data.reasons;
                    console.log(msgs);
                    for( i in msgs) {
                        console.log("msg=" + msgs[i]);
                        var ary = msgs[i].split(".");
                        var finalMsg = ary[0]+"的"+ary[1]+"属性"+ary[2];
                        console.log("finalMsg=" + finalMsg);
                        console.log("dom = " + "#ermsg_" + ary[1]);
                        $("#ermsg_"+ary[1]).html(finalMsg);
                    }
                }
            },
            error : function (msg) {
                console.log("msg=" + msg);
            }
        });

    });
});