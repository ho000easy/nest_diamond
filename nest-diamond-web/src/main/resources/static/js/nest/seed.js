$(document).ready(function () {
    $('select').selectpicker();
    let _columnDefs = [{
        'targets': 1,
        'render': function (data, type, full, meta) {
            return linkColumn(data)

        }
    }];

    let seedListTable = multiSelectDataTable('seedList', '/seed/search',
        ['id', 'seedPrefix', 'walletGenerateType', 'walletVendor', 'remark', 'createTime'], params, null, _columnDefs)

    function params() {
        return $('#addForm').serialize()
    }

    $('#seedList tbody').on('click', 'td img.link', function () {
        let seedId = seedListTable.row($(this).closest('tr')).data()['id']
        window.open(`account?seedId=${seedId}`);
    });

    $("#search").click(function (event) {
        seedListTable.ajax.reload(null, false);
    })


    $("#generateSeed").click(function (event) {
        get('/seed/generate', function (resp) {
            processResp(resp, '种子生成成功', function (data) {
                $('#showSeedModal').modal('show')
                $('#showSeed').text(data)
                copyTextToClipboardFallback(data)
                $.toast(successToast('复制成功'))
            })
        })
    })

    $('#delete').click(function () {
        if (!checkSelectedIds(seedListTable)) {
            return;
        }
        confirmDangerDelete('确认删除选中的种子吗？', function () {
            postJson('/seed/delete', JSON.stringify(getSelectedIds(seedListTable)), function (resp) {
                processResp(resp, '删除成功', function () {
                    seedListTable.ajax.reload(null, false);
                });
            });
        });
    })

    // 绑定主页面的“添加”按钮，打开弹窗
    $("#addSeed").off('click').on('click', function () {
        // 重置表单
        $('#seedAddForm')[0].reset();
        // 设置默认类型并触发 UI 更新
        $('#modalWalletGenerateType').val('SINGLE_SEED').selectpicker('refresh');
        toggleModalFields('SINGLE_SEED');
        $('#addSeedModal').modal('show');
    });

    // 弹窗内：类型切换监听
    $('#modalWalletGenerateType').on('change', function () {
        toggleModalFields($(this).val());
    });

    // 核心切换函数
    function toggleModalFields(type) {
        // 1. 先隐藏所有动态区域
        $('.area-type-content').addClass('d-none');
        // 2. 显示选中的区域
        const $targetArea = $('#area-' + type);
        $targetArea.removeClass('d-none');
        // 3. 刷新该区域内可能存在的 selectpicker（如“是否保留种子”）
        $targetArea.find('.selectpicker').selectpicker('refresh');
    }

    // 确认添加
    $("#confirmAddSeed").click(function () {
        const type = $('#modalWalletGenerateType').val();
        let payload = {
            walletVendor: $('#seedAddForm [name="walletVendor"]').val(),
            walletGenerateType: type
        };

        // 根据类型手动取值
        if (type === 'SINGLE_SEED') {
            payload.seedWordsList = $('#area-SINGLE_SEED [name="singleSeedWords"]').val();
            payload.count = $('#area-SINGLE_SEED [name="count"]').val();
        } else if (type === 'MULTI_SEED') {
            payload.seedWordsList = $('#area-MULTI_SEED [name="multiSeedWordsList"]').val();
            payload.isReserveSeed = $('#area-MULTI_SEED [name="isReserveSeed"]').val();
        } else if (type === 'MULTI_PRIVATE_KEY') {
            payload.privateKeyList = $('#area-MULTI_PRIVATE_KEY [name="privateKeyList"]').val();
        } else if (type === 'MULTI_ADDRESS') {
            payload.addressList = $('#area-MULTI_ADDRESS [name="addressList"]').val();
        }

        postJson('/seed/add', JSON.stringify(payload), function (resp) {
            processResp(resp, '资产添加成功', function () {
                $('#addSeedModal').modal('hide');
                seedListTable.ajax.reload(null, false);
            });
        });
    });

})
