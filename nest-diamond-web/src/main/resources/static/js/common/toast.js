function processResp(resp, msg, callback){
    if (resp.isSuccess && msg != null && msg !== '') {
        $.toast(successToast(msg))
        callback(resp.data);
        return;
    }
    $.toast(failToast(resp.message))
}

function failToast(msg, timeout) {
    timeout = timeout ?? 3000000;

    return {
        heading: '提示',
        text: msg,
        position: 'top-right',
        stack: false,
        icon: 'error',
        allowToastClose: true,
        hideAfter: timeout,
    }
}

function successToast(msg, timeout) {
    timeout = timeout ?? 30000;

    return {
        heading: '提示',
        text: msg,
        position: 'top-right',
        stack: false,
        icon: 'success',
        hideAfter: timeout,
    }
}