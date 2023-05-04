
var requestOptions = {
    method: 'GET',
    redirect: 'follow'
};

fetch("http://localhost:8080/files/get", requestOptions)
    .then(response => response.text())
    .then(result => {
        let out = '<tr id="head-row"><th>Files</th><th>Operations</th></tr>';
        let list = result.trim().substring(1, result.length - 1).split(',');;

        for (let i = 0; i < list.length; i++) {
            out += '<tr><td id="file-name">' + list[i] + '</td><td><a id="download-link" href="index.html">Download</a><a id="edit-link" href="#">Edit</a><a id="delete-link" href="index.html" onClick="deleteFile(document.getElementById(\'file-name\').textContent)">Delete</a></td></tr>';
        }

        document.getElementById('files-container').innerHTML += out;
    })
    .catch(error => console.log('error', error));

function addFile(name, content) {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "text/plain");
    //   var raw = "Hello World!\r\n";

    var raw = content + "\r\n";

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/files/add/" + name, requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));
}

function deleteFile(fileName){
    var requestOptions = {
        method: 'POST',
        redirect: 'follow'
      };
      let fixedName = fileName.trim().substring(1, fileName.length -1)
      console.log(fileName);
      fetch("http://localhost:8080/files/delete/" + fixedName, requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));
}
