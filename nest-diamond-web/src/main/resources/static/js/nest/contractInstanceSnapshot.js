$(document).ready(function () {
    $('select').selectpicker();
    let columnDefs = [
    ]
    let contractInstanceListTable = multiSelectDataTable('contractInstanceList', '/contractInstanceSnapshot/search',
        ['id', 'ticketNo', 'protocolName', 'contractName', 'chainName', 'address', 'createTime', 'modifyTime'],
        params, null, columnDefs)

    function params() {
        let data = $("#addForm").serialize()
        return data
    }

    $("#search").click(function (event) {
        contractInstanceListTable.ajax.reload(null, false);
    })

    // $("#add").click(function (event) {
    //     postForm('/contractInstanceSnapshot/add', $("#addForm").serialize(), function (resp) {
    //         processResp(resp, '添加成功', function () {
    //             contractInstanceListTable.ajax.reload(null, false);
    //         })
    //     })
    // })
    // $('#update').click(function () {
    //     postForm('/contractInstance/update', $("#editForm").serialize(), function (resp) {
    //         processResp(resp, '更新成功', function () {
    //             contractInstanceListTable.ajax.reload(null, false);
    //         })
    //     })
    //
    // })

    // $('#contractInstanceList tbody').on('click', 'td.contract_instance img.edit', function () {
    //     let rowData = contractInstanceListTable.row($(this).closest('tr')).data()
    //     showEditModal($("#editModal"), 'editForm', rowData)
    // });

    $('#delete').click(function () {
        if (!checkSelectedIds(contractInstanceListTable)) {
            return;
        }
        postJson('/contractInstanceSnapshot/delete', JSON.stringify(getSelectedIds(contractInstanceListTable)), function (resp) {
            processResp(resp, '删除成功', function () {
                contractInstanceListTable.ajax.reload(null, false);
            })
        })

    })
    protocolChange()
    $('#protocolName').change(protocolChange)

    function protocolChange(){
        let protocolName = $("#protocolName").val()
        postForm('/contract/findByProtocolName', {protocolName: protocolName}, function(resp){
            if(resp.isSuccess){
                selectWithNullChange($('#contractName'), resp.data, 'name', 'name')
            }
        })
    }

})
