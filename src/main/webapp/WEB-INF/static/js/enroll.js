$(document).ready(function() {
    handleNextAndPreviousEvents(); // 处理下一步，上一步的动作
    initWebUploader(); // 初始化上传照片控件
    initializeDatePicker();
    requestDicts();
    StepUtils.toStep(7); // 到第 N 步，测试使用

    // 省变化时加载相应的市
    $('#provinces').change(function() {
        var $province = $('#provinces option:selected');
        var provinceId = parseInt($province.val());

        if ('false' === $province.attr('data-province-city')) {
            // 普通省则加载它的市
            requestCities(provinceId);
        } else {
            // 直辖市则市为它自己
            var cities = [{id: provinceId, name: $province.text()}];
            DictUtils.insertOptions('cities', cities, {remainFirstOption: false});
        }
    });

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
        if (Validator.validate3thStep()) {
            // 验证通过，进入第四步
            $('#box-3').hide();
            $('#box-4').show();
            $('.bz4').addClass('active');
        }
    });

    // 第四步的下一步
    $('#box-4-next').click(function() {
        if (Validator.validate4thStep()) {
            $('#box-4').hide();
            $('#box-5').show();
            $('.bz5').addClass('active');
        }
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
    $('#box-2-previous, #box-3-previous, #box-4-previous, #box-5-previous, #box-6-previous, #box-7-previous').click(function() {
        var step = parseInt($(this).attr('data-step'));
        StepUtils.toPreviousStep(step);
    });
}

/**
 * 请求字典数据，然后添加到 DOM 里
 */
function requestDicts() {
    $.rest.get({url: Urls.REST_DICTS, success: function(result) {
        var data = result.data;
        console.log(data.provinces);
        DictUtils.insertOptions('certTypes', data.certTypes);          // 资格种类
        DictUtils.insertOptions('provinces', data.provinces, {templateId: 'provinceOptionTemplate'});   // 省
        DictUtils.insertOptions('id-types', data.idType, {remainFirstOption: false, filters: ['身份证']}); // 身份证
        DictUtils.insertOptions('nations', data.nation);               // 民族
        DictUtils.insertOptions('teach-grades', data.teachGrade);      // 现任教学段
        DictUtils.insertOptions('politicals', data.political);         // 政治面貌
        DictUtils.insertOptions('edu-levels', data.eduLevel);          // 最高学位
        DictUtils.insertOptions('degrees', data.degree);               // 最高学历
        DictUtils.insertOptions('pth-levels', data.pthLevel);          // 普通话水平
        DictUtils.insertOptions('school-quales', data.schoolQuale);    // 现任教学校性质
        DictUtils.insertOptions('work-unit-types', data.workUnitType); // 任教学校所在地
        DictUtils.insertOptions('learn-types', data.learnType);        // 学习形式
        DictUtils.insertOptions('normal-majors', data.normalMajor);    // 最高学历专业类别
        DictUtils.insertOptions('post-quales', data.postQuale);        // 岗位性质
    }});
}

/**
 * 选择指定 provinceId 省下的市
 *
 * @param  {int} provinceId 省的 id
 */
function requestCities(provinceId) {
    $('#cities option:gt(0)').remove();

    // provinceId 为 -1 表示选择了 "请选择"，则不请求新的市数据
    if (-1 != provinceId) {
        $.rest.get({url: Urls.REST_CITIES_BY_PROVINCE, urlParams: {provinceId: provinceId}, success: function(result) {
            DictUtils.insertOptions('cities', result.data);
        }});
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                           验证                                                //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function Validator () {}

/**
 * 验证第三步数据，验证通过后并填充在其他步骤上对应的数据
 *      1. 身份证有效
 *      2.
 *
 * @return {bool} 验证通过返回 true，否则返回 false
 */
Validator.validate3thStep = function() {
    var idType = UiUtils.getSelectedOption('id-types'); // parseInt($('#id-types option:selected').val());
    var idNo = $.trim($('#idNo').val());
    var certNo = $.trim($('#certNo').val());

    if (-1 == idType.id) {
        alert('请选择 "证件类型"');
        return false;
    }

    if (!IdCard.validate(idNo)) {
        alert('请输入有效的身份证号码');
        return;
    }

    UiUtils.setFormData('idType', idType.id, idType.name);
    UiUtils.setFormData('certNo', -1, certNo);

    // 显示身份证上的信息
    var idCard = new IdCard('', idNo);
    UiUtils.setFormData('idNo', -1, idCard.idNo);
    UiUtils.setFormData('birthday', -1, idCard.birthdayString);
    UiUtils.setFormData('gender', -1, idCard.gender);

    return true;
};

Validator.validate4thStep = function() {
    var nation = UiUtils.getSelectedOption('nations');
    var certAssignDate = $.trim($('#cert-assign-date').val());
    var gender = UiUtils.getSelectedOption('gender');
    var name = $.trim($('#name').val());

    // if (-1 == nation.id) {
    //     alert('请选择 "民族"');
    //     return false;
    // }

    UiUtils.setFormData('nation', nation.id, nation.name);
    UiUtils.setFormData('cert-assign-date', -1, certAssignDate);
    UiUtils.setFormData('gender', gender.id, gender.name);
    UiUtils.setFormData('name', -1, name);

    return true;
};

/**
 * 初始化时间选择器
 */
function initializeDatePicker() {
    // 证书签发日期
    var certAssignDatePicker = {
        elem: '#cert-assign-date',
        format: 'YYYY-MM-DD',
        istoday: true
    };

    // 最高学历毕业时间
    var graduationDatePicker = {
        elem: '#graduation-date',
        format: 'YYYY-MM-DD',
        istoday: true
    };

    // 开始参加工作时间
    var startWorkDatePicker = {
        elem: '#start-work-date',
        format: 'YYYY-MM-DD',
        istoday: true
    };

    // 现任教学校聘用起始日期
    var currentWorkStartTime = {
        elem: '#current-work-start-time',
        format: 'YYYY-MM-DD',
        istoday: true
    };

    laydate(certAssignDatePicker);
    laydate(startWorkDatePicker);
    laydate(graduationDatePicker);
    laydate(currentWorkStartTime);
}
