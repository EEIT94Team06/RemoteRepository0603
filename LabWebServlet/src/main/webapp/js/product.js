var request = null;
function sendGetRequest(id, path) {
	request = new XMLHttpRequest();
	request.onreadystatechange=callback;
	request.open("GET", path+"?action="+action+"&id="+id+"&dummy="+new Date().getTime());
	request.send(null);
}
function sendPostRequest(id, path) {
	request = new XMLHttpRequest();
	request.onreadystatechange=callback;
	request.open("POST", path);
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.send("action="+action+"&id="+id);
}
function sendPostJsonRequest(id, path) {
	request = new XMLHttpRequest();
	request.onreadystatechange=callback;
	request.open("POST", path);
	request.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
	request.send(JSON.stringify({
		"action": "jsonJson",
		"id": id
	}));
}
function processText(data) {
	var text = data;
	var index = data.indexOf(":");
	if(index!=-1) {
		text = data.substring(0, index);
		var temp = data.substring(index+1);
		var array = temp.split(",");
		document.getElementsByTagName("form")[0].id.value = array[0];
		document.getElementsByTagName("form")[0].name.value = array[1];
		document.getElementsByTagName("form")[0].price.value = array[2];
		document.getElementsByTagName("form")[0].make.value = array[3];
		document.getElementsByTagName("form")[0].expire.value = array[4];
	}
	var textNode = document.createTextNode(text);
	var spanElement = document.getElementsByTagName("span")[0];
	spanElement.appendChild(textNode);
}
function processXML(dom) {
	document.getElementsByTagName("span")[0].innerHTML = dom.getElementsByTagName("text")[0].firstChild.nodeValue;           
	var hasMoreData = dom.getElementsByTagName("hasMoreData")[0].firstChild.nodeValue;
	if(hasMoreData=="true") {
		document.getElementsByTagName("form")[0].id.value = dom.getElementsByTagName("id")[0].firstChild.nodeValue;
		document.getElementsByTagName("form")[0].name.value = dom.getElementsByTagName("name")[0].firstChild.nodeValue;
		document.getElementsByTagName("form")[0].price.value = dom.getElementsByTagName("price")[0].firstChild.nodeValue;
		document.getElementsByTagName("form")[0].make.value = dom.getElementsByTagName("make")[0].firstChild.nodeValue;
		document.getElementsByTagName("form")[0].expire.value = dom.getElementsByTagName("expire")[0].firstChild.nodeValue;
	}
}
function processJSON(data) {
	var json = JSON.parse(data);
	document.getElementsByTagName("span")[0].innerHTML = json[0].text;
	if(json[0].hasMoreData) {
		document.getElementsByTagName("form")[0].id.value = json[1].id;
		document.getElementsByTagName("form")[0].name.value = json[1].name;
		document.getElementsByTagName("form")[0].price.value = json[1].price;
		document.getElementsByTagName("form")[0].make.value = json[1].make;
		document.getElementsByTagName("form")[0].expire.value = json[1].expire;
	}
}
var action = "textJson";
function callback() {
	if(request.readyState==4) {
		if(request.status==200) {
//			processText(request.responseText);
//			processXML(request.responseXML);
			processJSON(request.responseText);
			
			document.getElementById("img").style.display = "none";
			document.getElementsByTagName("form")[0].id.disabled = false;
		} else {
			alert("Error:"+request.status);
		}
	}
}
