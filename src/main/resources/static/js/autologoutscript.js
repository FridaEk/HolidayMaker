/*
    To use, call: checkLoginTime(); at the top of your JS file
    To implement, import this script file to HTML BEFORE your own
*/

function checkLoginTime() {
    
    if (localStorage.getItem('user') !== null) {
        
        const startSession = Date.now();
        if (localStorage.getItem('time') === null) {
            localStorage.setItem('time', startSession);
        } else {
            if (startSession - localStorage.getItem('time') >= 1800000) {
                localStorage.removeItem('time');
                signOut();
            } else {
                localStorage.setItem('time', startSession);
            }
        }
    }
}