$(document).ready(function () {
    $('select').selectpicker();
    let columnDefs = [
        {
            'targets': 1,
            'render': function (data, type, full, meta) {
                return editColumn(data)
            }
        },
        {
        'targets': 7,
        // 'className': 'link-primary',
        'render': function (data, type, full, meta) {
            return `<a href="/contractInstance?protocolId=${data.protocolId}&contractId=${data.id}"  target="_blank" className="link-primary">添加实例</a> 
                `;
        }
    }];
    let contractListTable = multiSelectDataTable('contractList', '/contract/search',
        ['id', 'name', 'protocolName', 'remark', 'abi', 'createTime', 'modifyTime', null], params, null, columnDefs)

    function params() {
        return $("#addForm").serialize()
    }

    $("#search").click(function (event) {
        contractListTable.ajax.reload(null, false);
    })

    $("#add").click(function (event) {
        postForm('/contract/add', $("#addForm").serialize(), function (resp) {
            processResp(resp, '添加成功', function () {
                contractListTable.ajax.reload(null, false);
            })
        })
    })

    $('#contractList tbody').on('click', 'td img.edit', function () {
        let rowData = contractListTable.row($(this).closest('tr')).data()
        showEditModal($("#editModal"), 'editForm', rowData)
    });

    $('#update').click(function () {
        postForm('/contract/edit', $("#editForm").serialize(), function (resp) {
            processResp(resp, '更新成功', function () {
                contractListTable.ajax.reload(null, false);
            })
        })

    })

    $('#delete').click(function () {
        if (!checkSelectedIds(contractListTable)) {
            return;
        }
        postJson('/contract/delete', JSON.stringify(getSelectedIds(contractListTable)), function (resp) {
            processResp(resp, '删除成功', function () {
                contractListTable.ajax.reload(null, false);
            })
        })

    })

})
