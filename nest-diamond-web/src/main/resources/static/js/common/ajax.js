function postJson(url, data, callback) {
    $.ajax({
            url: url,
            data: data,
            dataType: "json",
            type: 'post',
            contentType: "application/json; charset=utf-8",
            success: callback
        }
    )
}

function postForm(url, data, callback) {
    $.ajax({
            url: url,
            data: data,
            type: 'post',
            success: callback
        }
    )
}

function postText(url, data, callback) {
    $.ajax({
            url: url,
            data: data,
            type: 'post',
            contentType: "text/plain",
            success: callback
        }
    )
}

function get(url, callback) {
    $.ajax({
            url: url,
            type: 'get',
            success: callback
        }
    )
}

function syncGet(url, callback) {
    $.ajax({
        url: url,
        type: "GET",
        async: false, // 将 async 设置为 false，使请求变为同步
        success: callback
    });
}


/**
 * 归一化为 id 数组：
 * - 单个值 => [value]
 * - 数组 => 保留；若是对象数组，取 obj.id
 * - 过滤 null/undefined/空字符串；去重
 */
function toIdArray(ids) {
    const arr = Array.isArray(ids) ? ids : [ids];
    const mapped = arr.map(v => (v && typeof v === 'object' ? v.id : v));
    const cleaned = mapped.filter(v => v !== null && v !== undefined && v !== '');
    // 去重
    return Array.from(new Set(cleaned));
}

/**
 * 通用提交方法
 * @param {number|string|Array<number|string|object>} ids
 * @param {string} url
 * @param {string} successMsg  如 '开启成功'
 * @param {Function} [afterSuccess]
 */
function postJsonWithIds(ids, url, successMsg, datatable) {
    const idList = toIdArray(ids);
    if (idList.length === 0) {
        console.warn('doAction: empty ids');
        $.toast(failToast('id不能为空', 3000));
        return;
    }
    postJson(url, JSON.stringify(idList), function (resp) {
        processResp(resp, successMsg, function () {
            datatable.ajax.reload(null, false);
        });
    });
}


function postSelectedIds(url, successMsg, datatable) {
    if (!checkSelectedIds(datatable)) {
        return;
    }
    let ids = getSelectedIds(datatable)
    postJsonWithIds(ids, url, successMsg, datatable)
}


