$(document).ready(function() {
    initWebUploader(); // 初始化照片上传控件
    handleNextAndPreviousEvents();
});

function initWebUploader() {
    if ( !WebUploader.Uploader.support() ) {
        alert( 'Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器,或则使用火狐、谷歌等浏览器');
        throw new Error( 'WebUploader does not support the browser you are using.' );
    }
    var uploader = WebUploader.create({
        auto: true,                 // 自动上传
        swf: Urls.WEB_UPLOADER_SWF, // swf 文件路径
        server: Urls.URI_UPLOAD_ENROLL_IMAGE, // 文件接收服务端
        pick: '#filePicker',       // 选择文件的按钮，内部根据当前运行时创建，可能是 input 元素，也可能是 flash.
        resize: true,              // 不压缩 image, 默认如果是 jpeg，文件上传前会压缩一把再上传！
        accept: { // 只允许上传图片
            title: 'Images',
            extensions: 'jpg,jpeg',
            // mimeTypes: 'image/*'
            mimeTypes: 'image/jpg,image/jpeg'
        },
        thumb: { // 对上传的图片进行裁剪处理
            width: 114,
            height: 156,
            // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
            allowMagnify: false,
            // 是否允许裁剪。
            crop: true
        },
        compress: { // 对上传的图片进行裁剪处理
            width: 114,
            height: 156,
            // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
            allowMagnify: false,
            // 是否允许裁剪。
            crop: false
        }
    });

    // 上传成功
    // response 为服务器返回来的数据
    uploader.onUploadSuccess = function(file, response) {
        UiUtils.setFormData('photo', -1, response.data);
        uploader.removeFile(file, true); // 启用多次上传
    };

    // 上传进度 [0.0, 1.0]
    // fileQueued 时创建进度条，uploadProgress 更新进度条
    // 可以使用 file.id 来确定是哪个文件的上传进度
    uploader.onUploadProgress = function(file, percentage) {
        // console.log(percentage);
        // console.log('uploadProgress:' + file.id);
    };

    // 当有文件添加进来的时候
    uploader.onFileQueued = function(file) {
        // 创建缩略图，如果为非图片文件，可以不用调用此方法，src 是 base64 格式的图片
        uploader.makeThumb(file, function(error, src) {
            if (error) {
                return;
            }

            $('#photo img').attr('src', src);
        }, 114, 156); // 100 * 100 为缩略图多大小
    };
}

function handleNextAndPreviousEvents() {
    // 第七步的提交
    var box1 = '#box-1';
    $('#box-7-submit').click(function() {
        var photo = UiUtils.getFormData(box1, 'photo').name; // 照片
        var regId= $.trim($('#reg_id').val());
         $.rest.create({url: Urls.URI_PICTURE_SUBMIT, urlParams:{photo:photo, regId: regId}, jsonRequestBody: true, async: false, success: function(result) {
            if (!result.success) {
                alert(result.message); // 弹出错误消息
            } else {
                passed = true;
            }
        }, error: function(error) {
            alert(error);
        }});
    });
}
