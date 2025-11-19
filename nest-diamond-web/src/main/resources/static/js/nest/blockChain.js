$(document).ready(function () {
    $('select').selectpicker();
    let columnDefs = [
        {
            'targets': 2,
            'className': 'block_chain',
            'render': function (data, type, full, meta) {
                return editColumn(data)
            }
        }
    ]
    let columns = ['id', 'chainId', 'chainName', 'isSupportEip1559', 'isSupportWeb3j', 'isL2', 'isTestnet',
        'rpcVendor', /*'ipProxyVendor', */'vmType', 'rpcNode', 'wssNode', 'blockExplorerUrl', 'createTime', 'modifyTime']
    let blockChainListTable = multiSelectDataTable('blockChainList', '/blockChain/search',
        columns, params, null, columnDefs)

    function params() {
        return $("#addForm").serialize()
    }

    $("#search").click(function (event) {
        blockChainListTable.ajax.reload(null, false);
    })

    $("#add").click(function (event) {
        postForm('/blockChain/add', $("#addForm").serialize(), function (resp) {
            processResp(resp, '添加成功', function () {
                blockChainListTable.ajax.reload(null, false);
            })
        })
    })

    $('#update').click(function () {
        postForm('/blockChain/update', $("#editForm").serialize(), function (resp) {
            processResp(resp, '更新成功', function () {
                blockChainListTable.ajax.reload(null, false);
            })
        })

    })

    $('#blockChainList tbody').on('click', 'td.block_chain img.edit', function () {
        let rowData = blockChainListTable.row($(this).closest('tr')).data()
        showEditModal($("#editModal"), 'editForm', rowData)
    });

    $('#delete').click(function () {
        if (!checkSelectedIds(blockChainListTable)) {
            return;
        }
        postJson('/blockChain/delete', JSON.stringify(getSelectedIds(blockChainListTable)), function (resp) {
            processResp(resp, '删除成功', function () {
                blockChainListTable.ajax.reload(null, false);
            })
        })

    })

    $('#export').click(function () {
        if (!checkSelectedIds(blockChainListTable)) {
            return;
        }
        postJson('/blockChain/export', JSON.stringify(getSelectedIds(blockChainListTable)), function (resp) {
            $('#exportModal').modal('show')
            $('#exportJSON').val(JSON.stringify(resp.data, null, 2))
        })
    })

    $('#copyExportBtn').click(function () {
        let exportJson = $('#exportJSON').val()
        copyTextToClipboardFallback(exportJson);
        $.toast(successToast('复制成功'))
    })

    $('#import').click(function () {
        $('#importModal').modal('show')
    })

    $('#importBtn').click(function () {
        let importJSON = $('#importJSON').val()
        postJson('/blockChain/import', importJSON, function (resp) {
            processResp(resp, '导入成功', function () {
                blockChainListTable.ajax.reload(null, false);
            })
        })
    })

})
