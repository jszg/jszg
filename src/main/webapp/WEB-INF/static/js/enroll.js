$(document).ready(function() {
    initWebUploader(); // 初始化照片上传控件
    initializeDatePicker(); // 初始化时间选择器
    handleNextAndPreviousEvents(); // 处理下一步，上一步的事件
    handleChangeProvincesEvent(); // 处理切换省时加载城市的事件
    handleChangeProvincesForCollegeEvent(); // 处理切换省时加载最高学历毕业学校的事件

    handleRegisterSubjectsDialog(); // 第四步的任教学科
    handleTeachSubjectsDialog(); // 第四步的现任教学科
    handleRequestRegisterOrgs(); // 第四步的注册机构
    handleSearchLocalSets(); // 第五步的搜索确认点
    handleGraduationCollegesDialog(); // 第七步的最高学历毕业学校
    handleMajorsDialog(); // 第七步的最高学历所学专业
    handleTechnicalJobsDialog(); // 第七步的教师职务（职称）

    requestLocalSets(); // TODO: 删除


    requestDicts();

    StepUtils.toStep(5); // 到第 N 步，测试使用

    // 点击取消按钮关闭弹出对话框
    $('.pop-dialog .cancel-button').click(function(event) {
        $("#lean_overlay").click();
    });

    $('tr:last', $('table')).css('border-bottom', 'none'); // 删除最后一行的 border-bottom
});

function initWebUploader() {
    var uploader = WebUploader.create({
        auto: true,               // 自动上传
        swf: Urls.WEB_UPLOADER_SWF, // swf 文件路径
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

            requestLocalSets(); // 请求确认点
        }
    });

    // 第五步的下一步
    $('#box-5-next').click(function() {
        $('#box-5').hide();
        $('#box-6').show();
        $('.bz6').addClass('active');

        var name = $.trim($('#name').val());
        UiUtils.setFormData('name', -1, name);

        // 查找确认点的信息，显示在第六步的注意事项下
        var localSetId = parseInt($('#local-sets-table input:radio:checked').val());
        $.rest.get({url: Urls.REST_LOCALSET_INFO, urlParams: {localSetId: localSetId}, success: function(result) {
            if (result.data.info) {
                $('#local-set-info').html(result.data.info);
            }
        }});
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
 * 请求确认点
 */
function requestLocalSets() {
    $('#local-sets-table tr:gt(0)').remove(); // 先删除所有的确认点
    var orgId = UiUtils.getSelectedOption('register-orgs').id; // orgId 为注册机构的 id
    orgId = 21;

    if (-1 == orgId) {
        return;
    }

    $.rest.get({url: Urls.REST_LOCALSETS, urlParams: {orgId: orgId}, success: function(result) {
        $('#local-sets-table').append(template('localSetsTemplate', {localSets: result.data}));
    }});
}

/**
 * 第五步的搜索
 */
function handleSearchLocalSets() {
    $('#local-sets-div .search-button').click(searchLocalSets);
    $('#local-sets-div .search-input').keyup(function(event) {
        if (event.keyCode==13) {
            searchLocalSets();
        }
    });

    function searchLocalSets() {
        $('#local-sets-table input:radio').removeAttr("checked"); // 取消选中
        var text = $.trim($('#local-sets-div .search-input').val()); // 输入的 text

        // [1] 如果输入内容为空白，显示所有的确认点
        // [2] 如果确认点的名字包含输入的 text 则显示，否则隐藏
        $('#local-sets-table tr:gt(0)').each(function(index, el) {
            var name = $('td[name="name"]', this).text();

            if (-1 != name.indexOf(text)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }
}

/**
 * 请求字典数据，然后添加到 DOM 里
 */
function requestDicts() {
    $.rest.get({url: Urls.REST_DICTS, success: function(result) {
        var data = result.data;
        UiUtils.insertOptions('certTypes', data.certTypes);          // 资格种类
        UiUtils.insertOptions('provinces', data.provinces, {templateId: 'provinceOptionTemplate'});   // 省
        UiUtils.insertOptions('provinces-for-college', data.provinces, {templateId: 'provinceOptionTemplate'}); // 省
        UiUtils.insertOptions('id-types', data.idType, {remainFirstOption: false, filters: ['身份证']}); // 身份证
        UiUtils.insertOptions('nations', data.nation);               // 民族
        UiUtils.insertOptions('teach-grades', data.teachGrade);      // 现任教学段
        UiUtils.insertOptions('politicals', data.political);         // 政治面貌
        UiUtils.insertOptions('edu-levels', data.eduLevel);          // 最高学位
        UiUtils.insertOptions('degrees', data.degree);               // 最高学历
        UiUtils.insertOptions('pth-levels', data.pthLevel);          // 普通话水平
        UiUtils.insertOptions('school-quales', data.schoolQuale);    // 现任教学校性质
        UiUtils.insertOptions('work-unit-types', data.workUnitType); // 任教学校所在地
        UiUtils.insertOptions('learn-types', data.learnType);        // 学习形式
        UiUtils.insertOptions('normal-majors', data.normalMajor);    // 最高学历专业类别
        UiUtils.insertOptions('post-quales', data.postQuale);        // 岗位性质
    }});
}

/**
 * 处理切换省时加载城市的事件
 * 1. 如果是直辖市，则它的城市为自己且没有 '请选择' 选项
 * 2. 如果不是直辖市，则加载省下的城市，且有 '请选择' 选项
 */
function handleChangeProvincesEvent() {
    $('#provinces').change(function() {
        UiUtils.onlyPleaseSelectOption('cities');
        var $province = $('#provinces option:selected');
        var provinceId = parseInt($province.val());
        var isProvinceCity = ('true' === $province.attr('data-province-city')); // 是否直辖市

        if (isProvinceCity) {
            // 直辖市的市为它自己
            var cities = [{id: provinceId, name: $province.text(), provinceCity: true}];
            UiUtils.insertOptions('cities', cities, {templateId: 'provinceOptionTemplate'});
        } else if (-1 != provinceId) {
            // provinceId 为 -1 表示选择了 "请选择"，则不加载省的城市
            $.rest.get({url: Urls.REST_CITIES_BY_PROVINCE, urlParams: {provinceId: provinceId}, success: function(result) {
                UiUtils.insertOptions('cities', result.data);
            }});
        }
    });
}

/**
 * 处理切换省时加载最高学历毕业学校的事件
 */
function handleChangeProvincesForCollegeEvent() {
    $('#provinces-for-college').change(function() {
        $('#graduation-colleges-holder').empty();
        var provinceId = parseInt($('#provinces-for-college option:selected').val());

        if (-1 != provinceId) {
            $.rest.get({url: Urls.REST_COLLEGES_BY_PROVINCE, urlParams: {provinceId: provinceId}, success: function(result) {
                $('#graduation-colleges-holder').append(template('graduationCollegesId', {colleges: result.data}));
            }});
        }
    });

    $(document).on('click', '#graduation-colleges li', function() {
        $(this).siblings().removeClass('active');
        $(this).addClass('active');
    });

    // 点击搜索学校按钮或者输入后按下回车，搜索学校
    var $dlg = $('#graduation-colleges-dialog');
    $('.toolbar .search-button', $dlg).click(searchCollege);
    $('.toolbar .search-input', $dlg).keyup(function(event) {
        if (event.keyCode==13) {
            searchCollege();
        }
    });

    function searchCollege() {
        $('#graduation-colleges li').removeClass('active'); // 删除被选中状态
        var provinceId = UiUtils.getSelectedOption('provinces-for-college').id;

        if (-1 == provinceId) {
            alert('请选择省，然后再进行搜索');
            return;
        }

        var text = $.trim($('.toolbar .search-input', $dlg).val());
        var $colleges = $('#graduation-colleges li');

        // [1] 如果输入内容为空白，显示所有的学校
        // [2] 如果学校的名字包含输入的 text 则显示，否则隐藏
        $colleges.each(function() {
            var schoolName = $(this).text();
            if (-1 != schoolName.indexOf(text)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }
}

/**
 * 当现任教学段和市变化时加载注册机构
 * 使用 teachGradeId(现任教学段) 和 cityId(市) 加载注册机构
 */
function handleRequestRegisterOrgs() {
    $('#teach-grades, #cities').change(function(event) {
        var teachGradeId = UiUtils.getSelectedOption('teach-grades').id;
        var cityId = UiUtils.getSelectedOption('cities').id;
        var provinceCity = UiUtils.getSelectedOption('cities').option.attr('data-province-city') === 'true';

        if (-1 === teachGradeId || -1 === cityId) {
            return;
        }

        $.rest.get({url: Urls.REST_ORGS_REG, urlParams: {teachGradeId: teachGradeId, cityId: cityId, provinceCity: provinceCity},
            success: function(result) {
                console.log(result.data);
                UiUtils.insertOptions('register-orgs', result.data);
        }});
    });
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

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                        第四步的对话框                                           //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 处理选择任教学科的相关事件
 *
 * 1. 点击 '任教学科' 按钮
 *    如果 certTypeId 不为 -1，则加载任教学科并显示在对话框中
 *    如果 certTypeId 为 -1，则提示选择资格种类
 * 2. 点击取消按钮隐藏对话匡
 * 3. 点击确定按钮，设置选中的学科，并隐藏对话框
 */
function handleRegisterSubjectsDialog() {
    // 初始化 LeanModal 对话框
    $('#register-subjects-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    // 点击 '任教学科' 按钮
    // 如果 certTypeId 不为 -1，则请求任教学科
    // 如果 certTypeId 为 -1，则提示选择资格种类
    $('#select-register-subject-button').click(function(event) {
        var certTypeId = UiUtils.getSelectedOption('certTypes').id;

        if (-1 === certTypeId) {
            alert('请先选择 "资格种类"，然后才能选择 "任教学科"');
            return;
        }

        $('#register-subjects-dialog-trigger').click(); // 显示对话框

        // 加载任教学科
        UiUtils.requestDataAndShowInTree($('#register-subjects-dialog .ztree'), function(treeId, treeNode) {
            if(!treeNode) {
                return Urls.REST_SUBJECTS_BY_CERT_TYPE.format({certTypeId: certTypeId});
            } else {
                return Urls.REST_SUBJECTS_BY_PARENT.format({parentId: treeNode.id});
            }
        });
    });

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#register-subjects-dialog .ok-button').click(function(event) {
        var subjectNode = window.subjectsTree.getSelectedNodes()[0];
        if (subjectNode) {
            UiUtils.setFormData('registerSubject', subjectNode.id, subjectNode.name);
            $("#lean_overlay").click();
        } else {
            alert('没有选中任教学科');
        }
    });
}

/**
 * 第四步的现任教学科
 */
function handleTeachSubjectsDialog() {
    // 初始化 LeanModal 对话框
    $('#teach-subjects-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    $('#select-teach-subject-button').click(function(event) {
        var provinceId = UiUtils.getSelectedOption('provinces').id;
        var teachGradeId = UiUtils.getSelectedOption('teach-grades').id;

        if (-1 === teachGradeId) {
            alert('请先选择 "现任教学段"，然后才能选择 "现任教学科"');
            return;
        }

        if (-1 === provinceId) {
            alert('请先选择 "省"，然后才能选择 "现任教学科"');
            return;
        }

        $('#teach-subjects-dialog-trigger').click(); // 显示对话框

        // 加载现任教学科
        UiUtils.requestDataAndShowInTree($('#teach-subjects-dialog .ztree'), function(treeId, treeNode) {
            if(!treeNode) {
                return Urls.REST_SUBJECTS_TEASUBJECT.format({provinceId: provinceId, teachGradeId: teachGradeId});
            } else {
                return Urls.REST_SUBJECTS_CHILDREN.format({provinceId: provinceId, parentId: treeNode.id});
            }
        });
    });

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#teach-subjects-dialog .ok-button').click(function(event) {
        var subjectNode = window.subjectsTree.getSelectedNodes()[0];
        if (subjectNode) {
            UiUtils.setFormData('teachSubject', subjectNode.id, subjectNode.name);
            $("#lean_overlay").click();
        } else {
            alert('没有选中现任教学科');
        }
    });
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                        第七步的对话框                                           //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 最高学历毕业学校
 */
function handleGraduationCollegesDialog() {
    // 初始化 LeanModal 对话框
    $('#graduation-colleges-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    $('#select-graduation-college-button').click(function(event) {
        $('#graduation-colleges-dialog-trigger').click();
    });

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#graduation-colleges-dialog .ok-button').click(function(event) {
        var $college = $('#graduation-colleges li.active');

        if ($college.length > 0) {
            var id = $college.attr('data-id');
            var name = $college.attr('data-name');
            UiUtils.setFormData('graduationCollege', id, name);
            $("#lean_overlay").click();
        } else {
            alert('没有选中毕业学校');
        }
    });
}

/**
 * 最高学历所学专业
 */
function handleMajorsDialog() {
    // 初始化 LeanModal 对话框
    $('#majors-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#majors-dialog .ok-button').click(function(event) {
        var subjectNode = window.subjectsTree.getSelectedNodes()[0];
        if (subjectNode) {
            UiUtils.setFormData('major', subjectNode.id, subjectNode.name);
            $("#lean_overlay").click();
        } else {
            alert('没有选中任教学科');
        }
    });

    $('#select-major-button').click(function(event) {
        var certTypeId = UiUtils.getSelectedOption('certTypes').id;
        var eduLevelId = UiUtils.getSelectedOption('edu-levels').id;

        // certTypeId = 2;
        // eduLevelId = 132;

        if (-1 === certTypeId) {
            alert('请先选择 "资格种类"，然后才能选择 "最高学历所学专业"');
            return;
        }

        if (-1 == eduLevelId) {
            alert('请先选择 "最高学历"，然后才能选择 "最高学历所学专业"');
            return;
        }

        $('#majors-dialog-trigger').click(); // 显示对话框

        // 加载最高学历所学专业
        UiUtils.requestDataAndShowInTree($('#majors-dialog .ztree'), function(treeId, treeNode) {
            if(!treeNode) {
                return Urls.REST_ZHUCE_MAJOR_PARENT.format({certTypeId: certTypeId, eduLevelId: eduLevelId});
            } else {
                return Urls.REST_SUBJECTS_BY_PARENT.format({parentId: treeNode.id});
            }
        });
    });
}

/**
 * 教师职务（职称）
 */
function handleTechnicalJobsDialog() {
    // 初始化 LeanModal 对话框
    $('#technical-jobs-dialog-trigger').leanModal({top: 50, overlay : 0.4});

    // 点击确定按钮，设置选中的学科，并隐藏对话框
    $('#technical-jobs-dialog .ok-button').click(function(event) {
        var subjectNode = window.subjectsTree.getSelectedNodes()[0];
        if (subjectNode) {
            UiUtils.setFormData('technicalJob', subjectNode.id, subjectNode.name);
            $("#lean_overlay").click();
        } else {
            alert('没有选中教师职务（职称）');
        }
    });

    $('#select-technical-job-button').click(function(event) {
        $('#technical-jobs-dialog-trigger').click(); // 显示对话框

        // 加载最高学历所学专业
        UiUtils.requestDataAndShowInTree($('#technical-jobs-dialog .ztree'), function(treeId, treeNode) {
            if(!treeNode) {
                return Urls.REST_TECHNICAL_JOB_ROOT;
            } else {
                return Urls.REST_TECHNICAL_JOB_CHILDREN.format({parentId: treeNode.id});
            }
        });
    });
}
