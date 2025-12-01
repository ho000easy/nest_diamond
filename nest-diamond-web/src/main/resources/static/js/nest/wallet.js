$(document).ready(function () {
    let isShowSeed = false
    // 地址表保持不变，依旧走服务端分页或Ajax
    let addressTable = dataTable('addressList', 'wallet/addressList', ['index', 'value'], seedParams)

    // 私钥/种子相关的表格，初始化时先不绑定具体的URL，或者初始化为空
    // 注意：这里假设 dataTable 函数允许 url 为空或者能后续手动加载数据
    // 如果你的 dataTable 封装强制要求 url，可以填一个空接口或者 'wallet/list' 但设置 deferLoading
    let privateKeyTable = dataTable('privateKeyList', 'wallet/list', ['index', 'value'], seedParams)
    let privateKeySplit1Table = dataTable('privateKeySplit1', '', ['index', 'value'], seedParams)
    let privateKeySplit2Table = dataTable('privateKeySplit2', '', ['index', 'value'], seedParams)

    $("#generateAddress").click(function (event) {
        addressTable.ajax.reload(null, false)
        fillTextField('wallet/addressList', 'addressText');
    });

    $('#passwordModal').on('shown.bs.modal', function () {
        $('#unlockPassword').trigger('focus');
    });

    // 绑定按钮点击事件
    $("#generateKey").click(function () { setupModal(generateKey) });
    $("#splitKey").click(function () { setupModal(splitKey) });
    $("#generateSeed").click(function () { setupModal(generateSeed) });
    $("#splitSeed").click(function () { setupModal(splitSeed) });

    function setupModal(func) {
        $('#passwordModal').modal('show')
        $('#passwordModal').data('func', func)
    }

    $('#unlockPassword').on('keydown', function (e) {
        if (e.key === 'Enter') {
            $('#confirmPasswordBtn').trigger('click');
        }
    });

    $('#confirmPasswordBtn').click(function (event){
        let func = $('#passwordModal').data('func')
        if(func) func()
        $('#unlockPassword').val('');
    })

    // --- 核心逻辑修改区域 ---

    // 统一的数据获取与处理函数
    function fetchAndProcess(isSeed, isSplit) {
        isShowSeed = isSeed;

        // 1. 请求后端获取完整数据
        postForm('wallet/list', seedParams(), function (resp) {
            if (resp.isSuccess === false) { // 假设后端错误返回结构
                $.toast(failToast(resp.message, 30000))
                return;
            }

            let dataList = resp.data || []; // 假设返回结构是 { data: [{index:1, value:'...'}, ...] }

            if (!isSplit) {
                // === 模式A：生成完整列表 ===
                // 填充表格
                privateKeyTable.clear().rows.add(dataList).draw();
                // 填充文本域
                fillTextAreaFromData(dataList, 'privateKeyText');

            } else {
                // === 模式B：分裂列表 ===
                let list1 = [];
                let list2 = [];

                dataList.forEach(item => {
                    let parts;
                    if (isSeed) {
                        parts = splitSeedStr(item.value);
                    } else {
                        parts = splitKeyStr(item.value);
                    }

                    list1.push({ index: item.index, value: parts[0] });
                    list2.push({ index: item.index, value: parts[1] });
                });

                // 填充两个表格
                privateKeySplit1Table.clear().rows.add(list1).draw();
                privateKeySplit2Table.clear().rows.add(list2).draw();

                // 填充两个文本域
                fillTextAreaFromData(list1, 'privateKey1Text');
                fillTextAreaFromData(list2, 'privateKey2Text');
            }

            $('#passwordModal').modal('hide');
        });
    }

    function generateKey(){
        fetchAndProcess(false, false);
    }

    function splitKey(){
        fetchAndProcess(false, true);
    }

    function generateSeed(){
        fetchAndProcess(true, false);
    }

    function splitSeed(){
        fetchAndProcess(true, true);
    }

    // --- 本地算法实现 ---

    // 私钥分裂逻辑 (模拟后端 Java 的 substring(0, 35))
    function splitKeyStr(key) {
        if (!key) return ["", ""];
        if (key.length <= 35) return [key, ""];
        return [key.substring(0, 35), key.substring(35)];
    }

    // 种子分裂逻辑 (模拟后端 Java 的 splitSeed)
    function splitSeedStr(seed) {
        if (!seed) return ["", ""];
        // 按空白字符分割
        let words = seed.trim().split(/\s+/);

        if (words.length !== 12) {
            // 如果不是12个词，直接放第一段，模拟后端逻辑
            return [seed, ""];
        }

        let part1 = words.slice(0, 6).join(" ");
        let part2 = words.slice(6, 12).join(" ");
        return [part1, part2];
    }

    // --- 辅助工具 ---

    function seedParams() {
        let unlockPassword = $('#unlockPassword').val()
        let data = $('#addForm').serialize()
        // 注意：isShowSeed 需要传递给后端，以便后端决定查 seed 字段还是 privateKey 字段
        data += `&unlockPassword=${unlockPassword}&isShowSeed=${isShowSeed ? 1 : 0}`
        return data
    }

    // 新增：直接从数据数组填充文本域，不再发起二次请求
    function fillTextAreaFromData(dataList, elementId) {
        let textContent = '';
        dataList.forEach(element => {
            textContent += `\r\n${element.value}`;
        });
        // 去掉开头的换行符
        $(`#${elementId}`).text(textContent.substring(2));
    }

    // 保留原有的 fillTextField 用于 address (如果需要)
    // 但对于 Key/Seed，我们已经在 fetchAndProcess 里直接处理了，不需要再调一次接口
    function fillTextField(url, id) {
        postForm(url, seedParams(), function (resp) {
            // ... 原有逻辑 ...
            // 建议将 Address 的逻辑也统一，但为了最小改动，这里可以保留
            // 仅用于 generateAddress
            let browserList = '';
            if(resp.data) {
                resp.data.forEach(function (element) {
                    browserList = `${browserList}\r\n${element.value}`
                })
                $(`#${id}`).text(browserList.substring(2))
            }
        })
    }
})