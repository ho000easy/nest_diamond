$(document).ready(function () {
    let isShowSeed = false
    let addressTable = dataTable('addressList', 'wallet/addressList', ['index', 'value'], seedParams)
    let privateKeyTable = dataTable('privateKeyList', 'wallet/list', ['index', 'value'], seedParams)
    let privateKeySplit1Table = dataTable('privateKeySplit1', 'wallet/split1', ['index', 'value'], seedParams)
    let privateKeySplit2Table = dataTable('privateKeySplit2', 'wallet/split2', ['index', 'value'], seedParams)

    $("#generateAddress").click(function (event) {
        addressTable.ajax.reload(null, false)
        fillTextField('wallet/addressList', 'addressText');
    });

    $('#passwordModal').on('shown.bs.modal', function () {
        $('#unlockPassword').trigger('focus');
    });

    $("#generateKey").click(function (event) {
        $('#passwordModal').modal('show')
        $('#passwordModal').data('func', generateKey)
    });

    $("#splitKey").click(function (event) {
        $('#passwordModal').modal('show')
        $('#passwordModal').data('func', splitKey)
    });

    $("#generateSeed").click(function (event) {
        $('#passwordModal').modal('show')
        $('#passwordModal').data('func', generateSeed)
    });

    $("#splitSeed").click(function (event) {
        $('#passwordModal').modal('show')
        $('#passwordModal').data('func', splitSeed)
    });

    $('#unlockPassword').on('keydown', function (e) {
        if (e.key === 'Enter') {
            $('#confirmPasswordBtn').trigger('click');
        }
    });

    $('#confirmPasswordBtn').click(function (event){
        let func = $('#passwordModal').data('func')
        func()
        $('#unlockPassword').val('');
    })

    function generateKey(){
        isShowSeed = false

        privateKeyTable.ajax.reload(null, false)
        fillTextField('wallet/list', 'privateKeyText')
        $('#passwordModal').modal('hide')

    }

    function splitKey(){
        isShowSeed = false
        // let params = encodeURIComponent(seedParams(false))
        // let url = `wallet/split1?${params}`
        // privateKeySplit1Table.ajax.url(url).load();
        // privateKeySplit2Table.ajax.url(url).load();

        privateKeySplit1Table.ajax.reload(null, false)
        privateKeySplit2Table.ajax.reload(null, false)
        fillTextField('wallet/split1',  'privateKey1Text')
        fillTextField('wallet/split2',   'privateKey2Text')
        $('#passwordModal').modal('hide')
    }

    function generateSeed(){
        isShowSeed = true

        privateKeyTable.ajax.reload(null, false)
        fillTextField('wallet/list',  'privateKeyText')
        $('#passwordModal').modal('hide')

    }

    function splitSeed(){
        isShowSeed = true

        privateKeySplit1Table.ajax.reload(null, false)
        privateKeySplit2Table.ajax.reload(null, false)
        fillTextField('wallet/split1',  'privateKey1Text')
        fillTextField('wallet/split2',  'privateKey2Text')
        $('#passwordModal').modal('hide')
    }

    function seedParams() {
        let unlockPassword = $('#unlockPassword').val()
        let data = $('#addForm').serialize()
        data += `&unlockPassword=${unlockPassword}&isShowSeed=${isShowSeed ? 1 : 0}`
        return data
    }

    function fillTextField(url, id) {
        postForm(url, seedParams(), function (resp) {
            let keys = Object.keys(resp)
            if(keys.includes('isSuccess')){
                processResp(resp, '查询成功', null)
                return
            }
            let browserList = '';
            resp.data.forEach(
                function (element, index, array) {
                    browserList = `${browserList}\r\n${element.value}`
                }
            )
            $(`#${id}`).text(browserList.substring(2))
        })
    }
})