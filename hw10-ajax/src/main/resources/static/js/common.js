const urlGetAllBook = '/api/v1/book2'
const urlGetLocalizedMessage = '/api/v1/message'


const jsonRequestHeader = {'Accept': 'application/json', 'Content-Type': 'application/json'}

let localizedMessages = new Map([
    ['error', 'Error'],
    ['error.detail', 'Details'],
    ['error.status.400', 'Bad Request'],
    ['error.status.404', 'Not Found'],
    ['error.status.405', 'Method Not Allowed'],
    ['error.status.500', 'Internal Server Error'],
]);


async function getLocalizedMessages(lang) {
    let names = [...localizedMessages.keys()].reduce((str, key) => str + '&name=' + key, '');
    names = (names === '') ? names : names.substring(1);
    let response = await fetch(urlGetLocalizedMessage+'/'+lang+'?'+ names,
        {method: "GET", headers: jsonRequestHeader});
    if (response.ok){
        let result = await response.json();
        [...localizedMessages.keys()].forEach(key=>{
           const localizedText = result[key];
           if (localizedText !== ''){
               localizedMessages.set(key,localizedText);
           }
        });
    }
}
