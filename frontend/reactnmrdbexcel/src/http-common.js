import axios from "axios";
export default axios.create({
    baseURL: "http://141.2.128.223:8080",
    //baseURL: "http://localhost:8080",
    headers: {
        "Content-type": "application/json"
    }
});