$(document).ready(function() {
    handleNextAndPreviousEvents(); // 处理下一步，上一步的动作
    initWebUploader(); // 初始化上传照片控件
    StepUtils.toStep(7); // 到第 N 步，测试使用

    requestDicts(); // 请求字典数据，初始化省，政治面貌等
    handleSubjectsDialog(); // 任教学科对话框

    // 省变化时加载相应的市
    $('#provinces').change(function() {
        var provinceId = parseInt($('#provinces option:selected').val());
        requestCities(provinceId);
    });

    // 资格总类或者市变化时请求认证机构
    $('#cities, #certTypes').change(function() {
        var cityId = parseInt($('#cities option:selected').val());
        var certTypeId = parseInt($('#certTypes option:selected').val());
        requestOrgs(cityId, certTypeId);
    });

    // 第七步的省变化时加载这个省的毕业院校
    $('#provinces-for-college').change(function() {
        var provinceId = parseInt($('#provinces-for-college option:selected').val());
        requestGraduationColleges(provinceId);
    });

    $('tr:last', $('table')).css('border-bottom', 'none'); // 删除最后一行的 border-bottom
});

/**
 * 请求任教学科
 */
function requestSubjects() {
    var setting = {
        async: {
            enable: true,
            url: loadSubjectsUrl,
            type: 'GET',
            dataFilter: filter
        },
        view: {
            showIcon: false
        }
    };

    function filter(treeId, parentNode, result) {
        if (!result) return null;

        var childNodes = result.data;

        for (var i = 0, l = childNodes.length; i < l; i++) {
            childNodes[i].isParent = true;
        }

        return childNodes;
    }

    function loadSubjectsUrl(treeId, treeNode) {
        var provinceId = 12911;
        var certTypeId = 4;

        if(!treeNode) {
            return Urls.REST_SUBJECTS_ROOT.format({provinceId: provinceId, certTypeId: certTypeId});
        } else {
            return Urls.REST_SUBJECTS_CHILDREN.format({provinceId: provinceId, parentId: treeNode.id});
        }
    }

    $.fn.zTree.destroy();
    window.subjectsTree = $.fn.zTree.init($("#subjects"), setting);
    $('#subjects-dialog-trigger').click(); // 显示对话框
}

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

/**
 * 第三步校验:
 *      资格种类  id 不能为 -1
 *      省       id 不能为 -1
 *      市       id 不能为 -1
 *      认定机构 id 不能为 -1
 *      任教学科 id 不能为 -1
 * @return {bool} true 为验证通过，false 为验证不通过
 */
function validate3thStep() {
    var certTypeId = parseInt($('#certTypes option:selected').val()); // #provinces option:selected
    var provinceId = parseInt($('#provinces option:selected').val());
    var cityId     = parseInt($('#cities option:selected').val());
    var orgId      = parseInt($('#orgs option:selected').val());
    var subjectId  = parseInt($('#subject').attr('data-subject-id'));

    if (-1 == certTypeId) {
        alert('请选择: 资格种类');
        return false;
    }

    if (-1 == provinceId) {
        alert('请选择: 省');
        return false;
    }

    if (-1 == cityId) {
        alert('请选择: 市');
        return false;
    }

    if (-1 == orgId) {
        alert('请选择: 认定机构');
        return false;
    }

    if (-1 == subjectId) {
        alert('请选择: 任教学科');
        return false;
    }

    return true;
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
    $('#box-2-previous, #box-3-previous, #box-4-previous, #box-5-previous, #box-6-previous, #box-7-previous').click(function() {
        var step = parseInt($(this).attr('data-step'));
        StepUtils.toPreviousStep(step);
    });
}

/**
 * 请求字典数据，然后添加到 DOM 里
 */
function requestDicts() {
    // 请求字典
    $.rest.get({url: Urls.REST_DICTS, success: function(result) {
        var i = 0;
        var data = result.data;

        DictUtils.insertOptions('certTypes', data.certTypes);       // 资格种类
        DictUtils.insertOptions('provinces', data.provinces);       // 省
        DictUtils.insertOptions('provinces-for-college', data.provinces);
        DictUtils.insertOptions('id-types', data.idType, {name: '身份证'}); // 身份证
        DictUtils.insertOptions('nations', data.nation);            // 民族
        DictUtils.insertOptions('politicals', data.political);      // 政治面貌
        DictUtils.insertOptions('edu-levels', data.eduLevel);       // 最高学位
        DictUtils.insertOptions('degrees', data.degree);            // 最高学历
        DictUtils.insertOptions('pth-levels', data.pthLevel);       // 普通话水平
        DictUtils.insertOptions('learn-types', data.learnType);     // 学习形式
        DictUtils.insertOptions('occupations', data.occupation);    // 现从事职业
        DictUtils.insertOptions('normal-majors', data.normalMajor); // 专业类别
    }});
}

/**
 * 选择指定 provinceId 省下的市
 *
 * @param  {int} provinceId 省的 id
 */
function requestCities(provinceId) {
    // 先删除所有的市(不删除 "请选择" 选项)
    $('#cities option:gt(0)').remove();

    // provinceId 为 -1 表示选择了 "请选择"
    if (-1 != provinceId) {
        $.rest.get({url: Urls.REST_CITIES_BY_PROVINCE, urlParams: {provinceId: provinceId}, success: function(result) {
            DictUtils.insertOptions('cities', result.data);
        }});
    }
}

/**
 * 根据市和资格种类查询认证机构
 *
 * @param  {int} cityId     市的 id
 * @param  {int} certTypeId 资格种类的 id
 */
function requestOrgs(cityId, certTypeId) {
    // [1] 删除所有的认证机构
    // [2] 使用资格种类和市查询认证机构
    // [3] 添加认证机构到 #orgs 下
    $('#orgs option:gt(0)').remove();

    if (-1 != certTypeId && -1 != cityId) {
        $.rest.get({url: Urls.REST_ORGS_BY_CITY_AND_CERT_TYPE, urlParams: {cityId: cityId, certTypeId: certTypeId}, success: function(result) {
            DictUtils.insertOptions('orgs', result.data);
        }});
    }
}

/**
 * 加载省下的毕业院校
 *
 * @param  {int} provinceId 省的 id
 */
function requestGraduationColleges(provinceId) {
    $('#graduation-colleges option:gt(0)').remove();

    if (-1 != provinceId) {
        $.rest.get({url: Urls.REST_COLLEGES_BY_PROVINCE, urlParams: {provinceId: provinceId}, success: function(result) {
            DictUtils.insertOptions('graduation-colleges', result.data);
        }});
    }
}

/**
 * 任教学科对话框
 */
function handleSubjectsDialog() {
    $('#subjects-dialog-trigger').leanModal({top: 50, overlay : 0.4});
    $('#select-subjects').click(function(event) {
        // 判断
        // 请求任教学科
        requestSubjects();
    });
    // 点击取消按钮隐藏对话匡
    $('#subjects-dialog-buttons-holder .cancel').click(function(event) {
        $("#lean_overlay").click();
    });
    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#subjects-dialog-buttons-holder .ok').click(function(event) {
        var subjectNode = window.subjectsTree.getSelectedNodes()[0];
        if (subjectNode) {
            $('#subject').attr('data-subject-id', subjectNode.id).text(subjectNode.name);
            $("#lean_overlay").click();
        } else {
            alert('没有选中任教学科');
        }
    });
}
