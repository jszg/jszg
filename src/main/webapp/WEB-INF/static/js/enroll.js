$(document).ready(function() {
    handleNextAndPreviousEvents(); // 处理下一步，上一步的动作
    toStep(7); // 到第 N 步，测试使用
    initWebUploader(); // 初始化上传照片控件



    $('tr:last', $('table')).css('border-bottom', 'none'); // 删除最后一行的 border-bottom
});

function initWebUploader() {
    var uploader = WebUploader.create({
        auto: true,               // 自动上传
        swf: 'http://cdn.staticfile.org/webuploader/0.1.5/Uploader.swf', // swf 文件路径
        server: Urls.URI_UPLOAD_PERSON_IMAGE, // 文件接收服务端
        pick: '#filePicker',      // 选择文件的按钮，内部根据当前运行时创建，可能是 input 元素，也可能是 flash.
        resize: false,            // 不压缩 image, 默认如果是 jpeg，文件上传前会压缩一把再上传！
        accept: { // 只允许上传图片
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        },
        compress: { // 对上传的图片进行裁剪处理
            width: 114,
            height: 156,
            allowMagnify: false,
            crop: false
        }
    });

    // 上传成功
    // response 为服务器返回来的数据
    uploader.onUploadSuccess = function(file, response) {
        console.log(response);
    };

    // 上传成功，例如抛异常
    // response 为服务器返回来的数据
    uploader.onUploadError = function(file, response) {
        console.log(response);
    };

    // 上传进度 [0.0, 1.0]
    // fileQueued 时创建进度条，uploadProgress 更新进度条
    // 可以使用 file.id 来确定是哪个文件的上传进度
    uploader.onUploadProgress = function(file, percentage) {
        console.log(percentage);
        console.log('uploadProgress:' + file.id);
    };

    // 当有文件添加进来的时候
    // 如果是图片，还可以创建缩略图
    uploader.onFileQueued = function(file) {
        console.log('fileQueued:' + file.id);

        // 创建缩略图
        // 如果为非图片文件，可以不用调用此方法。
        // src 是 base64 格式的图片
        uploader.makeThumb(file, function(error, src) {
            if (error) {
                return;
            }

            $('#photo img').attr('src', src);
        }, 114, 156); // 100 * 100 为缩略图多大小
    };
}

function handleNextAndPreviousEvents() {
    ////////////////////////////////////////////////////////////////////////
    ///                                下一步                              //
    ////////////////////////////////////////////////////////////////////////
    // 第一步的下一步
    $('#box-1-next').click(function() {
        $('#box-1').hide();
        $('#box-2').show();
        $('.bz2').addClass('active');
    });

    // 第二步的下一步
    $('#box-2-next').click(function() {
        if (!$('#agree-checkbox').get(0).checked){
            alert('请先阅读网上申报协议并同意后才可以申报！');
            return;
        }

        $('#box-2').hide();
        $('#box-3').show();
        $('.bz3').addClass('active');
    });

    // 第三步的下一步
    $('#box-3-next').click(function() {
        // if (validate3thStep()) {
            // 验证通过，进入第四步
            $('#box-3').hide();
            $('#box-4').show();
            $('.bz4').addClass('active');
        // }
    });

    // 第四步的下一步
    $('#box-4-next').click(function() {
        $('#box-4').hide();
        $('#box-5').show();
        $('.bz5').addClass('active');
    });

    // 第五步的下一步
    $('#box-5-next').click(function() {
        $('#box-5').hide();
        $('#box-6').show();
        $('.bz6').addClass('active');
    });

    // 第六步的下一步
    $('#box-6-next').click(function() {
        $('#box-6').hide();
        $('#box-7').show();
        $('.bz7').addClass('active');
    });

    // 第七步的下一步
    $('#box-7-next').click(function() {
        $('#box-7').hide();
        $('#box-8').show();
        $('.bz8').addClass('active');
    });

    ////////////////////////////////////////////////////////////////////////
    ///                                上一步                              //
    ////////////////////////////////////////////////////////////////////////
    // 第二步的上一步
    $('#box-2-previous').click(function(){
        $('#box-2').hide();
        $('#box-1').show();
        $('.bz2').removeClass('active');
    });

    // 第三步的上一步
    $('#box-3-previous').click(function(){
        $('#box-3').hide();
        $('#box-2').show();
        $('.bz3').removeClass('active');
    });

    // 第四步的上一步
    $('#box-4-previous').click(function(){
        $('#box-4').hide();
        $('#box-3').show();

        $('.bz4').removeClass('active');
    });

    // 第五步的上一步
    $('#box-5-previous').click(function(){
        $('#box-5').hide();
        $('#box-4').show();
        $('.bz5').removeClass('active');
    });

    // 第六步的上一步
    $('#box-6-previous').click(function(){
        $('#box-6').hide();
        $('#box-5').show();
        $('.bz6').removeClass('active');
    });

    // 第七步的上一步
    $('#box-7-previous').click(function(){
        $('#box-7').hide();
        $('#box-6').show();
        $('.bz7').removeClass('active');
    });
}

function toStep(step) {
    for (var i = 1; i < step; ++i) {
        $('#box-' + i + '-next').click();
    }
}
