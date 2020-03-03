var exec = require('cordova/exec');
/**
 * Obter status da impressora
 * @param arg0
 * @param success
 * @param error
 */
exports.getState = function (arg0, success, error) {
    if (!arg0) {
        arg0 = {};
    }
    exec(success, error, 'MUsbPrinter', 'getState', [arg0]);
};
/**
 * Imprimir bilhete pequeno
 * @param arg0
 * @param success
 * @param error
 */
exports.printTicket = function (arg0, success, error) {
    if (!arg0) {
        arg0 = {};
    }
    exec(success, error, 'MUsbPrinter', 'printTicket', [arg0]);
};