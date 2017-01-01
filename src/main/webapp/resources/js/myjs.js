/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function addNewType(){
    var text  =  document.getElementById('prefix').value;
     var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
     location.reload();
    }
  };
  xhttp.open("POST", "addType", true);
  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  var params = "prefix="+text;
  xhttp.send(params);
}


