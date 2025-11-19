$(document).ready(function () {
    $('select').selectpicker();
    let columnDefs = [
        {
            'targets': 1,
            'render': function (data, type, full, meta) {
                return editColumn(data)
            }
        }, {
            'targets': 8,
            // 'className': 'link-primary',
            'render': function (data, type, full, meta) {
                return `
<a href="/contract?protocolId=${data.id}"  target="_blank" className="link-primary">添加合约</a> 
                `;
            }
        }
    ]
    let protocolListTable = multiSelectDataTable('protocolList', '/protocol/search',
        ['id', 'name', 'website', 'github', 'twitter', 'discord', 'createTime', 'modifyTime', null],
        params, null, columnDefs)

    function params() {
        return $("#addForm").serialize()
    }

    $("#search").click(function (event) {
        protocolListTable.ajax.reload(null, false);
    })

    $("#add").click(function (event) {
        postForm('/protocol/add', $("#addForm").serialize(), function (resp) {
            processResp(resp, '添加成功', function () {
                protocolListTable.ajax.reload(null, false);
            })
        })
    })

    $('#update').click(function () {
        postForm('/protocol/update', $("#editForm").serialize(), function (resp) {
            processResp(resp, '更新成功', function () {
                protocolListTable.ajax.reload(null, false);
            })
        })

    })

    $('#export').click(function () {
        if (!checkSelectedIds(protocolListTable)) {
            return;
        }
        postJson('/protocol/export', JSON.stringify(getSelectedIds(protocolListTable)), function (resp) {
            $('#protocolExportModal').modal('show')
            $('#protocolExportJSON').val(JSON.stringify(resp.data, null, 2))
        })
    })

    $('#copyProtocolExport').click(function () {
        let protocolJson = $('#protocolExportJSON').val()
        copyTextToClipboardFallback(protocolJson);
        $.toast(successToast('复制成功'))
    })

    $('#import').click(function () {
        $('#protocolImportModal').modal('show')
    })

    $('#importProtocol').click(function () {
        let protocolJson = $('#protocolImportJSON').val()
        postJson('/protocol/import', protocolJson, function (resp) {
            processResp(resp, '导入成功', function () {
                protocolListTable.ajax.reload(null, false);
            })
        })
    })

    $('#protocolList tbody').on('click', 'td img.edit', function () {
        let rowData = protocolListTable.row($(this).closest('tr')).data()
        showEditModal($("#editModal"), 'editForm', rowData)
    });

    $('#delete').click(function () {
        if (!checkSelectedIds(protocolListTable)) {
            return;
        }
        postJson('/protocol/delete', JSON.stringify(getSelectedIds(protocolListTable)), function (resp) {
            processResp(resp, '删除成功', function () {
                protocolListTable.ajax.reload(null, false);
            })
        })

    })

})
