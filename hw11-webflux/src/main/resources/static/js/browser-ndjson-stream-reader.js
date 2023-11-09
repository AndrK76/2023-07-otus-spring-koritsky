/* FOR THE BROWSER
Utility function to read a ND-JSON HTTP stream.
`processLine` is a function taking a JSON object. It will be called with each element of the stream.
`response` is the result of a `fetch` request.
See usage example in the next file.
*/

const readStream = (processLine, processError) => response =>
{
    const stream = response.body.getReader();
    const matcher = /\r?\n/;
    const decoder = new TextDecoder();
    let buf = '';

    let resultFunc = processLine;

    if (!response.ok){
        resultFunc = processError;
    }

    const loop = () =>
        stream.read().then(({ done, value }) => {
            if (done) {
                if (buf.length > 0) resultFunc(JSON.parse(buf), response.status);
            } else {
                const chunk = decoder.decode(value, {
                    stream: true
                });
                buf += chunk;

                const parts = buf.split(matcher);
                buf = parts.pop();
                for (const i of parts.filter(p => p)) resultFunc(JSON.parse(i), response.status);
                return loop();
            }
        });

    return loop();
}