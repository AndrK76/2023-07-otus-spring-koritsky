const urlGetAllBook = '/api/v1/book'
const urlGetLocalizedMessage = '/api/v1/message'


const jsonRequestHeader = {'Accept': 'application/json', 'Content-Type': 'application/json'}


async function getLocalizedMessages(lang, localizedMessages) {
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
