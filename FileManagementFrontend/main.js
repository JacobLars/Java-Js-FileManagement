
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
            console.log(list[i]);
            let fileName = list[i];
            let fixedName = fileName.trim().substring(1, fileName.length - 1)
            console.log(fixedName);
            out += '<tr><td class="file-name">' + fixedName + '</td><td><a class="download-link" href="http://localhost:8080/files/download/' + fixedName + '">Download</a><a class="edit-link" href="#" onclick="editFile(\'' + fixedName + '\')">Edit</a><a class="delete-link" href="index.html" onClick="deleteFile(\'' + fixedName + '\')">Delete</a></td></tr>';
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

function deleteFile(fileName) {

    var requestOptions = {
        method: 'POST',
        redirect: 'follow'
    };
   
    console.log(fileName);

    fetch("http://localhost:8080/files/delete/" + fileName, requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));
}


function editFile(fileName) {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };


    fetch("http://localhost:8080/files/get/content/" + fileName, requestOptions)
        .then(response => response.text())
        .then(result => {
            console.log(result);
            let out = '<form action="index.html"><label>Edit Content:</label><textarea id="new_content">' + result + '</textarea><input type="submit" onclick="updateFileContent(document.getElementById(\'new_content\').value,\'' + fileName + '\')"></input></form>';

            document.getElementById("main-container").innerHTML = out;

        })
        .catch(error => console.log('error', error));
}

function updateFileContent(newContent, fileName) {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "text/plain");

    var raw = newContent;

    var requestOptions = {
        method: 'PUT',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/files/edit/" + fileName, requestOptions)
        .then(response => response.text())
        .then(result => {

            console.log(result);
        })
        .catch(error => console.log('error', error));
}


function downloadFile(fileName){

}