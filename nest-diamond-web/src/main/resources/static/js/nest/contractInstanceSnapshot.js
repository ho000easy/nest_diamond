$(document).ready(function () {
    $('select').selectpicker();
    let columnDefs = [
        {
            'targets': 4,
            'className': 'contract_instance',
            'render': function (data, type, full, meta) {
                return editColumn(data)
            }
        }
    ]
    let contractInstanceListTable = multiSelectDataTable('contractInstanceList', '/contractInstance/search',
        ['id', 'workOrderNo', 'protocolName', 'contractName', 'chainName', 'address', 'createTime', 'modifyTime'],
        params, null, columnDefs)

    function params() {
        let data = $("#addForm").serialize()
        return data
    }

    $("#search").click(function (event) {
        contractInstanceListTable.ajax.reload(null, false);
    })

    $("#add").click(function (event) {
        postForm('/contractInstance/add', $("#addForm").serialize(), function (resp) {
            processResp(resp, '添加成功', function () {
                contractInstanceListTable.ajax.reload(null, false);
            })
        })
    })
    $('#update').click(function () {
        postForm('/contractInstance/update', $("#editForm").serialize(), function (resp) {
            processResp(resp, '更新成功', function () {
                contractInstanceListTable.ajax.reload(null, false);
            })
        })

    })

    $('#contractInstanceList tbody').on('click', 'td.contract_instance img.edit', function () {
        let rowData = contractInstanceListTable.row($(this).closest('tr')).data()
        showEditModal($("#editModal"), 'editForm', rowData)
    });

    $('#delete').click(function () {
        if (!checkSelectedIds(contractInstanceListTable)) {
            return;
        }
        postJson('/contractInstance/delete', JSON.stringify(getSelectedIds(contractInstanceListTable)), function (resp) {
            processResp(resp, '删除成功', function () {
                contractInstanceListTable.ajax.reload(null, false);
            })
        })

    })
    protocolChange()
    $('#protocolId').change(protocolChange)

    function protocolChange(){
        let protocolName = $("#protocolName").val()
        postForm('/contract/findByProtocolName', {protocolName: protocolName}, function(resp){
            if(resp.isSuccess){
                selectWithNullChange($('#contractName'), resp.data, 'name', 'name')
            }
        })
    }

})
