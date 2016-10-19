$(document).ready(function() {
    initWebUploader();

    // 请求资格种类
    $.rest.get({url: Urls.REST_CERT_TYPE, success: function(result) {
        // [1] 删除所有的资格种类
        // [2] 查询资格种类
        // [3] 添加资格种类到 #certTypes 下
        $('#certTypes option:gt(0)').remove();
        var $certTypes = $('#certTypes');
        var cts = result.data;
        for (var i = 0; i < cts.length; ++i) {
            $certTypes.append(template('optionTemplate', cts[i]));
        }
    }});

    // 请求省
    $.rest.get({url: Urls.REST_PROVINCES, success: function(result) {
        // [1] 删除所有的省
        // [2] 查询所有的省
        // [3] 添加省到 #provinces 下
        $('#provinces option:gt(0)').remove();
        var $provinces = $('#provinces');
        var ps = result.data;
        for (var i = 0; i < ps.length; ++i) {
            $provinces.append(template('optionTemplate', ps[i]));
        }
    }});

    // 请求市
    $('#provinces').change(function() {
        // [1] 删除所有的市
        // [2] 查询省下的所有市
        // [3] 添加市到 #citys 下
        $('#citys option:gt(0)').remove();
        var provinceId = parseInt($('#provinces option:selected').val());

        // provinceId 为 -1 表示选择了 "请选择"
        if (-1 != provinceId) {
            $.rest.get({url: Urls.REST_CITIES_BY_PROVINCE, urlParams: {provinceId: provinceId}, success: function(result) {
                var $citys = $('#citys');
                var cs = result.data;
                for (var i = 0; i < cs.length; ++i) {
                    $citys.append(template('optionTemplate', cs[i]));
                }
            }});
        }
    });

    // 资格总类或者市变化时请求认证机构
    $('#citys, #certTypes').change(function() {
        // [1] 删除所有的认证机构
        // [2] 使用资格种类和市查询认证机构
        // [3] 添加认证机构到 #orgs 下
        $('#orgs option:gt(0)').remove();
        var cityId = parseInt($('#citys option:selected').val());
        var certTypeId = parseInt($('#certTypes option:selected').val());

        if (-1 != certTypeId && -1 != cityId) {
            $.rest.get({url: Urls.REST_ORGS_BY_CITY_AND_CERT_TYPE, urlParams: {cityId: cityId, certTypeId: certTypeId}, success: function(result) {
                var $orgs = $('#orgs');
                var orgs = result.data;
                for (var i = 0; i < orgs.length; ++i) {
                    $orgs.append(template('optionTemplate', orgs[i]));
                }
            }});
        }
    });

    $('#subjects-dialog-trigger').leanModal({top: 50, overlay : 0.4});
    // $('#select-subjects').leanModal();
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

    // 请求字典
    $.rest.get({url: Urls.REST_DICTS, success: function(result) {
        var data = result.data;
        var i = 0;
        // 名族
        $('#nations option:gt(0)').remove();
        var $nations = $('#nations');
        var nations = data.nation;
        for (i = 0; i < nations.length; ++i) {
            $nations.append(template('optionTemplate', nations[i]));
        }

        // 政治面貌
        $('#politicals option:gt(0)').remove();
        var $politicals = $('#politicals');
        var politicals = data.political;
        for (i = 0; i < politicals.length; ++i) {
            $politicals.append(template('optionTemplate', politicals[i]));
        }

        // 最高学历
        $('#degrees option:gt(0)').remove();
        var $degrees = $('#degrees');
        var degrees = data.degree;
        for (i = 0; i < degrees.length; ++i) {
            $degrees.append(template('optionTemplate', degrees[i]));
        }

        // 普通话水平
        $('#pth-levels option:gt(0)').remove();
        var $pthLevels = $('#pth-levels');
        var pthLevels = data.pthLevel;
        for (i = 0; i < pthLevels.length; ++i) {
            $pthLevels.append(template('optionTemplate', pthLevels[i]));
        }

        // 学习形式
        $('#learn-types option:gt(0)').remove();
        var $learnTypes = $('#learn-types');
        var learnTypes = data.learnType;
        for (i = 0; i < learnTypes.length; ++i) {
            $learnTypes.append(template('optionTemplate', learnTypes[i]));
        }

        // 现从事职业
        $('#occupations option:gt(0)').remove();
        var $occupations = $('#occupations');
        var occupations = data.occupation;
        for (i = 0; i < occupations.length; ++i) {
            $occupations.append(template('optionTemplate', occupations[i]));
        }

        // 专业类别
        $('#normal-majors option:gt(0)').remove();
        var $normalMajors = $('#normal-majors');
        var normalMajors = data.normalMajor;
        for (i = 0; i < normalMajors.length; ++i) {
            $normalMajors.append(template('optionTemplate', normalMajors[i]));
        }
    }});

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
        if (!$('#_checkbox_bd').get(0).checked){
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

    $('tr:last', $('table')).css('border-bottom', 'none'); // 删除最后一行的 border-bottom

    $('#box-1-next').click();
    $('#box-2-next').click();
    $('#box-3-next').click();
    $('#box-4-next').click();
    $('#box-5-next').click();
    $('#box-6-next').click();
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
    var cityId     = parseInt($('#citys option:selected').val());
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
