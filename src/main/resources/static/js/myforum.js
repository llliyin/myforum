function post() {
    var questionId = $('#question_id').val();
    var content = $('#comment_content').val();
    comment2target(questionId, content, 1)
}

function comment2target(targetId, content, type) {
    console.log("进图回复的跳转")
    if (!content) {
        alert("不能回复空内容哦~~~~~")
        return;
    } else {
        $.ajax({
            type: "POST",
            url: "/comment",
            contentType: 'application/json',
            data: JSON.stringify({
                "parentId": targetId,
                "content": content,
                "type": type
            }),
            success: function (response) {
                if (response.code == 200) {
                    window.location.reload()
                } else {
                    if (response.code == 2003) {
                        var isAccept = confirm(response.message);
                        if (isAccept) {
                            window.open("https://github.com/login/oauth/authorize?client_id=227ea6b7e8ae954d0f89&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                            window.localStorage.setItem("closable", true);
                        }
                    } else {
                        alert(response.message);
                    }
                }
            },
            dataType: "json"
        });
    }
}

function myCollapse(e) {
//     var id=e.getAttribute("data-id");
//     var comments= $("#comment-"+id);
//     var collapse=e.getAttribute("data-collapse");
//     if(collapse){
//         //进行折叠
//         comments.removeClass("in");
//         e.removeAttribute("data-collapse");
//         e.classList.remove("active")
//     }else {
//         //进行伸展
//
//         console.log("进入方法")
//         $.getJSON("/comment/" + id, function (data) {
//
//             //将每个中的数据进行一个循环
//             $.each(data.data.reverse(), function (index, comment) {
//                 //定义了一个div
//                 var mediaLeftElement = $("<div/>", {
//                     "class": "media-left"
//                 }).append($("<img/>", {
//                     "class": "media-object img-rounded",
//                     "src": comment.user.avatarUrl
//                 }));
//
//                 var mediaBodyElement = $("<div/>", {
//                     "class": "media-body"
//                 }).append($("<h5/>", {
//                     "class": "media-heading",
//                     "html": comment.user.name
//                 })).append($("<div/>", {
//                     "html": comment.content
//                 })).append($("<div/>", {
//                     "class": "menu"
//                 }).append($("<span/>", {
//                     "class": "pull-right",
//                     "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
//                 })));
//
//                 var mediaElement = $("<div/>", {
//                     "class": "media"
//                 }).append(mediaLeftElement).append(mediaBodyElement);
//
//                 var commentElement = $("<div/>", {
//                     "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
//                 }).append(mediaElement);
//
//                 subCommentContainer.prepend(commentElement);
//             });
//             //展开二级评论
//             comments.addClass("in");
//             // 标记二级评论展开状态
//             e.setAttribute("data-collapse", "in");
//             e.classList.add("active");
//         });
//     }


    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

// 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {

                //将每个中的数据进行一个循环
                $.each(data.data.reverse(), function (index, comment) {
                    //定义了一个div
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object me-media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }

}

function comment(e) {
    var id = e.getAttribute("data-id");
    var content = $("#input-" + id).val();
    comment2target(id, content, 2);

}

function showSelectTag() {
    $("#select-tag").show();
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            //.val括号中加上参数额时候就是将值放入对应的东西当中
            $("#tag").val(value);
        }
    }
}