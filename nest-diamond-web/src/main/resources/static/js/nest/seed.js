$(document).ready(function () {
    $('select').selectpicker();
    let _columnDefs = [{
        'targets': 1,
        'render': function (data, type, full, meta) {
            return linkColumn(data)

        }
    }];

    let seedListTable = multiSelectDataTable('seedList', '/seed/query',
        ['id', 'seedPrefix', 'remark', 'createTime'], params, null, _columnDefs)

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

    $("#addSeed").click(function (event) {
        postJson('/seed/add', $('#addForm').serializeJSON(), function (resp) {
            processResp(resp, '添加成功', function () {})
            seedListTable.ajax.reload(null, false);

        })
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
        postJson('/seed/delete', JSON.stringify(getSelectedIds(seedListTable)), function (resp) {
            processResp(resp, '删除成功', function () {
                seedListTable.ajax.reload(null, false);
            })
        })
    })

})
