const organismsReq = new XMLHttpRequest();
organismsReq.responseType = 'json';
organismsReq.open("GET", "/api/v1/kingdoms", true);
organismsReq.addEventListener("load", function () {
        for (let i = 0; i < organismsReq.response.length; i++) {
            document.querySelector("#kingdoms").insertAdjacentHTML("beforeend", `
            <div>${organismsReq.response[i].nameRussian}</div>`)
        }
    }
)

organismsReq.send(null);