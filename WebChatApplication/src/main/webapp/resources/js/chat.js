var generateId = function () {
	var date = Date.now();
	var random = Math.random() * Math.random();

	return Math.floor(date * random).toString();
};

var theMessage = function (str1, str2) {
    return {
        user: str1,
        text: str2,
        id: generateId(),
        isDeleted: false,
        date: null
    };
};

var appState = {
    mainUrl : "chat",
    messageList:[],
    token : "TE11EN"
};

var selectFlag = 0;
var changeFlag = false;

function run() {    
    var appContainer = document.getElementsByClassName("body")[0];
    appContainer.addEventListener("keydown", keyPress);
    appContainer.addEventListener("keyup", delEnter);
    
    var messages = document.getElementsByClassName("messages")[0];
    messages.addEventListener("click", messageEvent);
    
    loop();
    
}

function loop(continueWith) {
        if(selectFlag == 0){
            restore();      
        }
        setTimeout(function() {
            loop(continueWith);
        }, 1000);
}

function restore(continueWith) {
    var url = appState.mainUrl + '?token=' + appState.token;


    get(url, function(responseText) {
        console.assert(responseText != null);

        var response = JSON.parse(responseText);
        console.log(response);

        appState.token = response.token;

        var boxMessages = document.getElementsByClassName("messages")[0];
        var scroll = boxMessages.scrollTop;
        var scrollHeight = boxMessages.scrollHeight;
        while(boxMessages.hasChildNodes())
            boxMessages.removeChild(boxMessages.lastChild);            
        createAllMessages(response.messages);
        boxMessages.scrollTop = scroll + (boxMessages.scrollHeight - scrollHeight);

        continueWith && continueWith();
    });
}

function keyPress(e) {
    if(e.keyCode == 13) {
        enterPress(e);
    }
}

function delEnter(e) {
    if(e.keyCode == 13) {
        if(e.target.className == "textArea") {
            document.getElementsByClassName("textArea")[0].value="";
        }
        if(e.target.className == "edit-name") {
            document.getElementsByClassName("edit-name")[0].value="";
        }
    }
}


function messageEvent(e) {
    var element = e.target;
    if(element.classList.contains("del-button") ) {
        deleteMessage(element.parentElement);    
    }
    if(element.classList.contains("change-message-button")) {
        if(!changeFlag)
            beginChangeMessage(element.parentElement);  
        else endChangeMessage(e);        
    }
    if (element.classList.contains("oneMessage") ) {        
        if(element.childNodes[0].innerHTML == document.getElementById("userName").innerHTML)
            selectMessage(element);
    }
    if (element.classList.contains("user-name") || element.classList.contains("text-message") ) {
        if(element.parentElement.childNodes[0].innerHTML == document.getElementById("userName").innerHTML)
            selectMessage(element.parentElement);
    }
}

function deleteMessage(oneMessage) {
    oneMessage.parentElement.removeChild(oneMessage);
    console.log(oneMessage.id);
    var message = theMessage();
    message.id = oneMessage.id;
    message.isDeleted = true;
    del(appState.mainUrl, JSON.stringify(message), function(){
        restore();
    });
    selectFlag--;
    oneMessage.remove();
}

function beginChangeMessage(oneMessage) {
    var divText = oneMessage.childNodes[3];
    var text = divText.innerHTML;
    var textArea = document.createElement("textarea");
    textArea.value = text;
    textArea.classList.add("temp-area");
    divText.innerHTML = null;
    divText.appendChild(textArea, divText);
    changeFlag = true;
}

function endChangeMessage(e) {
    var area = document.getElementsByClassName("temp-area")[0];
    var text = area.value;
    var parentDiv = area.parentElement;
    parentDiv.removeChild(area);
    changeFlag = false;
    parentDiv = parentDiv.parentElement;
    var message = theMessage();
    message.id = parentDiv.id;
    message.text = text;
    put(appState.mainUrl, JSON.stringify(message), function(){
        restore();
    });
    selectFlag--;
}

function selectMessage(div) {
    if(!div.isSelected ) {
        div.classList.add("select");
        div.isSelected = true;
        selectFlag++;
        div.childNodes[1].classList.remove("hidden");        
        div.childNodes[2].classList.remove("hidden");
    } else {
        div.classList.remove("select");
        div.isSelected = false;
        selectFlag--;
        div.childNodes[1].classList.add("hidden");        
        div.childNodes[2].classList.add("hidden");
    }
}

function enterPress(e) {
    if(e.target.className == "textArea") {
        onClickSendMessage();
    }
    if(e.target.className == "edit-name") {
        onClickChange();
    }
    if(e.target.className == "temp-area") {
        endChangeMessage(e);
    }
}

function onClickChange() {
    var textArea = document.getElementsByClassName("edit-name")[0];
    if(textArea.value != "") {
        document.getElementById("userName").innerHTML =
            textArea.value;
        textArea.value = "";
    }
}

function onClickSendMessage() {
    var message = document.getElementsByClassName("textArea")[0];
    var user = document.getElementById("userName");
    var theMes = theMessage(user.innerHTML, message.value);
    post(appState.mainUrl, JSON.stringify(theMes), function(){
        restore();
    });
    
    message.value = "";
    
}

function addMessage(theMes) {
    if (!theMes.text) {
        return;
    }
    var message = createMessage(theMes);
    message.id = theMes.id;
    var messages = document.getElementsByClassName("messages")[0];
    messages.appendChild(message);
    //messages.scrollTop = messages.scrollHeight;
}

function createMessage(theMes) {
    var oneMessage = document.createElement("div"); 
    oneMessage.classList.add("oneMessage");
    oneMessage.isSelected = false;
    if(theMes.isDeleted) {
        oneMessage.innerHTML = "Message was deleted.";
        oneMessage.classList.add("deleted");
    } else {
        var userName = document.createElement("div");
        userName.classList.add("user-name");
        userName.innerHTML = theMes.user;
        oneMessage.appendChild(userName);    

        var delSpan = document.createElement("span");
        delSpan.classList.add("glyphicon");
        delSpan.classList.add("glyphicon-remove");    
        delSpan.classList.add("del-button");
        delSpan.classList.add("hidden");
        delSpan.setAttribute("title", "Delete message");
        oneMessage.appendChild(delSpan);


        var changeSpan = document.createElement("span");
        changeSpan.classList.add("glyphicon");
        changeSpan.classList.add("glyphicon-pencil");    
        changeSpan.classList.add("change-message-button");
        changeSpan.classList.add("hidden");
        changeSpan.setAttribute("title", "Change message"); 
        oneMessage.appendChild(changeSpan);

        var textMessage = document.createElement("div");
        textMessage.classList.add("text-message"); 
        textMessage.innerHTML = theMes.text;
        oneMessage.appendChild(textMessage);
    }
    
    return oneMessage;
}

function isConnected(flag, str) {
    var divConn = document.getElementsByClassName("server-state")[0];
    if(flag) {
        divConn.innerHTML = "You are in space";
        divConn.setAttribute("title", str);
    }
    else {
        divConn.innerHTML = "You are not in space";
        divConn.setAttribute("title", "Server connection error!");
    }
}

function createAllMessages (messages){
    if(messages != null) {        
        for(var i = 0; i < messages.length; i++) {
            addMessage(messages[i]);
        }
    }
}

function get(url, continueWith, continueWithError) {
    ajax('GET', url, null, continueWith, continueWithError);
}

function post(url, data, continueWith, continueWithError) {
    ajax('POST', url, data, continueWith, continueWithError);   
}

function put(url, data, continueWith, continueWithError) {
    ajax('PUT', url, data, continueWith, continueWithError);    
}

function del(url, data, continueWith, continueWithError) {
    ajax('DELETE', url, data, continueWith, continueWithError);    
}


function ajax(method, url, data, continueWith, continueWithError) {
    var xhr = new XMLHttpRequest();

    continueWithError = continueWithError;// || defaultErrorHandler;
    xhr.open(method || 'GET', url, true);

    xhr.onload = function () {
        if (xhr.readyState !== 4)
            return;

        if(xhr.status != 200) {
            continueWithError('Error on the server side, response ' + xhr.status);
            return;
        }
        isConnected(true, url);
        continueWith(xhr.responseText);
    };    

    xhr.ontimeout = function () {
        continueWithError('Server timed out !');
    };

    
    xhr.onerror = function (e) {
        var errMsg = 'Server connection error !\n'+
        '\n' +
        'Check if \n'+
        '- server is active\n'+
        '- server sends header "Access-Control-Allow-Origin:*"';        
        isConnected(false, url);
        
        continueWithError(errMsg);
    };

    xhr.send(data);
}

