var exec = require('cordova/exec');

exports.AddHeader = function (header, success, error) {
    exec(success, error, 'AddHeadersOS', 'AddHeader', [header]);
};

exports.ListHeaders = function (success, error) {
    exec(success, error, 'AddHeadersOS', 'ListHeaders', []);
};

exports.ClearHeaders = function (success, error) {
    exec(success, error, 'AddHeadersOS', 'ClearHeaders', []);
};

exports.SetHeaders = function (headers, success, error) {
    exec(success, error, 'AddHeadersOS', 'SetHeaders', [headers]);
};