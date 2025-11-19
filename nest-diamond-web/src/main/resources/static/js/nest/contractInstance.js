$(document).ready(function () {
    $('select').selectpicker();
    let columnDefs = [
        {
            'targets': 4,
            'className': 'contract_instance',
            'render': function (data, type, full, meta) {
                return editColumn(data)
            }
        },
        {
            'targets': 5,
            'className': 'dt-body-center',
            'render': function (data, type, full, meta) {
                if(full.minGasPriceFactor && full.maxGasPriceFactor) {
                    return `<span style="color:#e15927;font-weight: bold;">${full.minGasPriceFactor}</span>-
                            <span style="color:green;font-weight: bold;">${full.maxGasPriceFactor}</span>`
                }
                return ''
            }
        },{
            'targets': 6,
            'className': 'dt-body-center',
            'render': function (data, type, full, meta) {
                if(full.minGasLimitFactor && full.maxGasLimitFactor){
                    return `<span style="color:#e15927;font-weight: bold;">${full.minGasLimitFactor}</span>-
                            <span style="color:green;font-weight: bold;">${full.maxGasLimitFactor}</span>`
                }
                return ''
            }
        }
    ]
    let contractInstanceListTable = multiSelectDataTable('contractInstanceList', '/contractInstance/search',
        ['id', 'protocolName', 'contractName', 'chainName', 'address', 'createTime', 'modifyTime'],
        params, null, columnDefs)

    function params() {
        let data = $("#addForm").serialize()
        let initContractId = $('#initContractId').val()
        if(initContractId){
            data = data + `&contractId=${initContractId}`
        }
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
        let protocolId = $("#protocolId").val()
        postForm('/contract/findByProtocol', {protocolId: protocolId}, function(resp){
            if(resp.isSuccess){
                selectWithNullChange($('#contractId'), resp.data, 'name', 'id')
                let initContractId = $("#initContractId").val()
                if(initContractId != null){
                    $('#contractId').val(initContractId)
                    $('#contractId').selectpicker('refresh')

                    $('#initContractId').val(null)
                }
            }
        })
    }

})
