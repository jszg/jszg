$(document).ready(function() {
    // 请求资格种类
    $.rest.get({url: Urls.REST_CERT_TYPE, success: function(result) {
        // [1] 删除所有的资格种类
        // [2] 查询资格种类
        // [3] 添加资格种类到 #certType 下
        $('#certType option:gt(0)').remove();
        var $certType = $('#certType');
        var cts = result.data;
        for (var i = 0; i < cts.length; ++i) {
            $certType.append('<option value="{id}">{name}</option>'.format({id: cts[i].id, name: cts[i].name}));
        }
    }});

    // 请求省
    $.rest.get({url: Urls.REST_PROVINCES, success: function(result) {
        // [1] 删除所有的省
        // [2] 查询所有的省
        // [3] 添加到省的 #province 下
        $('#province option:gt(0)').remove();
        var $province = $('#province');
        var ps = result.data;
        for (var i = 0; i < ps.length; ++i) {
            $province.append('<option value="{id}">{name}</option>'.format({id: ps[i].id, name: ps[i].name}));
        }
    }});

    // 请求市
    $('#province').change(function() {
        // [1] 删除所有的市
        // [2] 查询省下的所有市
        // [3] 添加到市的 #city 下
        $('#city option:gt(0)').remove();
        var $p = $(this).find("option:selected");
        var provinceId = parseInt($p.val());

        // provinceId 为 -1 表示选择了 "请选择"
        if (-1 != provinceId) {
            $.rest.get({url: Urls.REST_CITIES_BY_PROVINCE, urlParams: {provinceId: provinceId}, success: function(result) {
                var $city = $('#city');
                var cs = result.data;
                for (var i = 0; i < cs.length; ++i) {
                    $city.append('<option value="{id}">{name}</option>'.format({id: cs[i].id, name: cs[i].name}));
                }
            }});
        }
    });

    // 请求认证机构

    /* 第一步的下一步 */
    $('#box-1-next').click(function(){
        $('#box-1').hide();
        $('#box-2').show();

        $('.bz2').addClass('active');
    });

    /* 第二步的上一步 */
    $('#box-2-previous').click(function(){
        $('#box-2').hide();
        $('#box-1').show();
        $('.bz2').removeClass('active');
    });

    /* 第二步的下一步 */
    $('#box-2-next').click(function(){
        if ($('#_checkbox_bd').get(0).checked){
            $('#box-2').hide();
            $('#box-3').show();

            $('.bz3').addClass('active');
        }else{
            alert('请先阅读网上申报协议并同意后才可以申报！');
        }
    });

    /* 第三步的上一步 */
    $('#box-3-previous').click(function(){
        $('#box-3').hide();
        $('#box-2').show();
        $('.bz3').removeClass('active');
    });

    /* 第三步的下一步 */
    $('#box-3-next').click(function(){
        $('#box-3').hide();
        $('#box-4').show();

        $('.bz4').addClass('active');
    });

    /* 第四步的上一步 */
    $('#box-4-previous').click(function(){
        $('#box-4').hide();
        $('#box-3').show();

        $('.bz4').removeClass('active');
    });

    /* 第四步的下一步 */
    $('#box-4-next').click(function(){
        $('#box-4').hide();
        $('#box-5').show();
        $('.bz5').addClass('active');
    });

    /* 第五步的上一步 */
    $('#box-5-previous').click(function(){
        $('#box-5').hide();
        $('#box-4').show();
        $('.bz5').removeClass('active');
    });

    /*第五步的下一步*/
    $('#box-5-next').click(function(){
        $('#box-5').hide();
        $('#box-6').show();
        $('.bz6').addClass('active');
    });

    /*第六步的上一步*/
    $('#box-6-previous').click(function(){
        $('#box-6').hide();
        $('#box-5').show();
        $('.bz6').removeClass('active');
    });

    /*第六步的下一步*/
    $('#box-6-next').click(function(){
        $('#box-6').hide();
        $('#box-7').show();
        $('.bz7').addClass('active');
    });

    /*第七步的上一步*/
    $('#box-7-previous').click(function(){
        $('#box-7').hide();
        $('#box-6').show();
        $('.bz7').removeClass('active');
    });

    /*第七步的下一步*/
    $('#box-7-next').click(function(){
        $('#box-7').hide();
        $('#box-8').show();
        $('.bz8').addClass('active');
    });

    /*****************弹出框***********************/
    /*第三步的弹出框*/
    $('#box-3 .ui-button-text-1').click(function(){
    $('#box-3 .popup-1').show();
        $('#box-3 .close-1').click(function(){
            $('#box-3 .popup-1').hide();
        });
    });

    $('#box-3 .ui-button-text-2').click(function(){
        $('#box-3 .popup-2').show();
        $('#box-3 .close-2').click(function(){
            $('#box-3 .popup-2').hide();
        });
    });

    $('#box-1-next').click();
    $('#box-2-next').click();
});
